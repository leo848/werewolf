package leo.werwolf.players;

import leo.rustjava.iterator.Iterators;
import leo.werwolf.GameData;
import leo.werwolf.utils.Prompts;

import java.util.List;

public class Werewolf extends Player {
	public Werewolf(String name) {
		super(name);
	}

	@Override
	public boolean isWerewolf() {
		return true;
	}

	@Override
	public void action(GameData data) {
		System.out.println(name + ", du darfst jetzt als Werwolf abstimmen.");
		data.voteKill(Prompts.alivePlayer(
				"Gib den Spieler ein, den du töten möchtest: " + data.alivePlayers().map(p -> p.name).join(", "),
				data
		));
	}

	@Override
	public boolean alive() {
		return alive;
	}

	@Override
	public List<Player> onKill() {
		alive = false;
		return Iterators.<Player>once(this).toList();
	}
}
