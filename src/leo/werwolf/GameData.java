package leo.werwolf;

import leo.rustjava.Option;
import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.werwolf.players.Player;

import java.util.*;

import static leo.rustjava.Option.None;
import static leo.rustjava.iterator.Iterators.*;

public class GameData {
	public final List<Player> players;
	public final Map<Player, Integer> voteKills = new HashMap<>();
	public Option<Player> victim = None();
	public List<Player> deaths = new ArrayList<>();
	public boolean outputDeathRoles;

	public GameData(GameDataBuilder builder) {
		players = initPlayers(builder.roles, builder.names, builder.shuffle);
		outputDeathRoles = builder.outputDeathRoles;
	}

	private List<Player> initPlayers(Map<Class<? extends Player>, Integer> roles, List<String> names, boolean shuffle) {
		List<Class<? extends Player>> roleList = from(roles.entrySet()).fold(new ArrayList<>(), (list, entry) -> {
			range(0, entry.getValue()).forEach((_n) -> list.add(entry.getKey()));
			return list;
		});
		if (shuffle) Collections.shuffle(roleList);
		return from(roleList).zipWith(from(names), (clazz, name) -> {
			try {
				return (Player) clazz.getConstructor(String.class).newInstance(name);
			} catch (ReflectiveOperationException e) {
				throw new RuntimeException(e);
			}
		}).toList();
	}

	public void die(Player player) {
		deaths.add(player);
	}

	public void voteKill(Player player) {
		voteKills.put(player, voteKills.getOrDefault(player, 0) + 1);
	}

	public void finishVote() {
		var highestVote = from(voteKills.entrySet())
				.sortedByKey(Map.Entry::getValue)
				.rev()
				.groupBy(Map.Entry::getValue)
				.map(Pair::right)
				.next();

		if (highestVote.isSomeAnd(v -> v.size() == 1)) {
			victim = highestVote.map(l -> l.get(0).getKey());
		} else victim = None();
		voteKills.clear();
	}

	public List<Player> execute() {
		List<Player> list = new ArrayList<>(victim.take().mapOrElse(ArrayList::new, Player::onKill));
		list.addAll(from(deaths).inspect(Player::die).toList());
		deaths.clear();
		alivePlayers().forEach(p -> from(list).forEach(k -> p.onOtherKill(k, this)));
		return list;
	}

	public Iterator<Player> players() {
		return from(players);
	}

	public Iterator<Player> alivePlayers() {
		return players().filter(Player::alive);
	}
}
