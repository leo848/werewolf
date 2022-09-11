package leo.werwolf.players;

import leo.rustjava.iterator.Iterators;
import leo.werwolf.GameData;
import leo.werwolf.utils.Prompts;

import java.util.List;

import static java.lang.System.out;
import static leo.rustjava.Result.*;
import static leo.rustjava.Unit.Unit;

public class RedLady extends Villager{
	Player visiting;

	public RedLady(String name) {
		super(name);
	}

	@Override
	public void action(GameData data) {
		visiting = Prompts.player(strings.redLadyPrompt(),
				player -> this == player ? Err(strings.redLadyVisitsHerself()) : Ok(Unit()),
				data);
		if (visiting.isWerewolf()) data.die(this);
		out.println(strings.redLadyVisits(visiting));
	}

	@Override
	public List<Player> onKill() {
		return Iterators.<Player>empty().toList();
	}

	@Override
	public void onOtherKill(Player otherPlayer, GameData data) {
		if (otherPlayer == visiting) data.die(this);
	}
}
