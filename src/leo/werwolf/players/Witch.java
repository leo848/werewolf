package leo.werwolf.players;

import leo.rustjava.Pair;
import leo.werwolf.GameData;
import leo.werwolf.utils.Prompts;

import static java.lang.System.out;
import static leo.rustjava.Option.None;

public class Witch extends Villager{
	boolean healPotion = true;
	boolean killPotion = true;

	public Witch(String name) {
		super(name);
	}

	@Override
	public void action(GameData data) {
		if (!healPotion && !killPotion) {
			out.println(strings.noPotions());
			return;
		}
		if (healPotion && data.victim.isSomeAnd(p -> Prompts.bool(strings.witchAskHeal(p)))) {
			healPotion = false;
			var luckyVictim = data.victim.take().unwrap();
			out.println(strings.witchHealed(luckyVictim));
		}
		if (killPotion && Prompts.bool(strings.witchAskKill())) {
			killPotion = false;
			var player = Prompts.player(data);
			data.die(player);
			out.println(strings.witchKilled(player));
		}
	}
}
