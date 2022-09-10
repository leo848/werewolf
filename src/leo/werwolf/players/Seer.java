package leo.werwolf.players;

import leo.werwolf.GameData;
import leo.werwolf.utils.Prompts;

import static java.lang.System.out;

public class Seer extends Villager {
	public Seer(String name) {
		super(name);
	}

	public void action(GameData gameData) {
		var player = Prompts.alivePlayer(strings.seerPrompt(), gameData);
		out.println(player.name + " " + strings.is() + " " + player.role());
	}
}