package leo.werwolf;

import leo.rustjava.Result;
import leo.rustjava.Unit;
import leo.werwolf.players.*;
import leo.werwolf.utils.Prompts;
import leo.werwolf.utils.Strings;

import java.util.List;
import java.util.Map;

import static java.lang.System.out;
import static leo.rustjava.Result.*;
import static leo.rustjava.Unit.Unit;
import static leo.rustjava.iterator.Iterators.from;

public class Game {
	public GameData data;
	private final Strings strings = Strings.getDefault();

	public Game() {
		this.data = new GameDataBuilder()
				.role(Werewolf.class, 2)
				.role(Villager.class, 3)
				.role(Witch.class, 1)
				.role(RedLady.class, 1)
				.role(Seer.class, 1)
				.players("Annette", "Adalbert", "Manuel", "Judith", "Torben", "Heinrich", "Gerhard", "Ekkehard")
				.assertPlayers(8)
				.build();
	}
	public void play() {
		assignRoles();
		do {
			var nightDeaths = night();
			day(nightDeaths);
		} while (status().isOk());

		status().unwrapErr().ifElse(
				villagerWin -> out.println(strings.villagerWin(villagerWin)),
				werewolfWin -> out.println(strings.werewolfWin(werewolfWin))
		);
		finalizeRoles();
	}

	private void printRoles(String sep) {
		from(data.players).map(p -> p.name + sep + p.role()).forEach(out::println);
	}

	private void assignRoles() {
		out.println(strings.roleAssign());
		printRoles(" - ");
	}

	private void finalizeRoles() {
		out.println(strings.finalizeRoles());
		printRoles(" %s ".formatted(strings.was()));
	}

	private List<Player> night() {
		Prompts.waitForInput(strings.nightBegins() + "\n");

		wakeUp(RedLady.class);

		wakeUp(Werewolf.class);
		data.finishVote();

		wakeUp(Witch.class);

		var nightDeaths = data.execute();

		wakeUp(Seer.class);

		return nightDeaths;
	}

	@SuppressWarnings("SameParameterValue")
	private void wakeUp(Class<? extends Player> clazz) {
		var livingPlayers = data.alivePlayers().filter(clazz::isInstance);

		if (livingPlayers.copy().count() == 0 && !Player.callIfNone(clazz)) return;
		out.println(strings.wakeUp(clazz, livingPlayers.copy()));

		livingPlayers.copy().forEach(p -> p.action(data));

		out.println(strings.goToBed(clazz, livingPlayers.copy()) + "\n");
	}


	private void day(List<Player> nightDeaths) {
		Prompts.waitForInput(strings.dayBegins());

		out.printf("\n%s%n", strings.everybodyWakesUp());
		out.printf("%s: %s%n", strings.died(), from(nightDeaths)
				.map(p -> data.outputDeathRoles ? p.name + " - " + p.role() : p.name)
				.join(", "));

		out.println(strings.whoStillLives() + data.alivePlayers().join(", "));
		out.println(strings.startDiscussion());

		Prompts.waitForInput();

		data.alivePlayers().forEach(player -> {
			out.printf("%s, %s.%n", player, strings.youCanVote());
			Prompts.maybeAlivePlayer(data).ifSome(data::voteKill);
		});

		from(data.voteKills.entrySet())
				.sortedByKey(Map.Entry::getValue)
				.map(e -> "%s - %d".formatted(e.getKey(), e.getValue()))
				.forEach(out::println);

		data.finishVote();

		var dayKills = data.execute();
		out.println(strings.villagersKilled(dayKills));
		from(dayKills)
				.map(p -> p.name + (data.outputDeathRoles ? " %s %s".formatted(strings.was(), p.role()) : ""))
				.forEach(out::println);
	}

	private Result<Unit, Result<String, String>> status() {
		if (data.alivePlayers()
				.map(Player::isWerewolf)
				.fold(0, (n, p) -> p ? n - 1 : n + 1) <= 0) {
			return Err(Err(strings.moreWerewolves()));
		} else if (data.alivePlayers().map(Player::isWerewolf).all(b -> !b)) {
			return Err(Ok(strings.noWerewolves()));
		} else return Ok(Unit());
	}
}
