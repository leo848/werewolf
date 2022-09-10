package leo.werwolf.utils;

import leo.rustjava.iterator.Iterator;
import leo.werwolf.GameData;
import leo.werwolf.players.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public abstract class Strings {
	static public Strings getDefault() {
		return GermanStrings.get();
	}

	static private final Random random = new Random();

	static public String random(String... strings) {
		return strings[random.nextInt(strings.length)];
	}
	abstract public String died(Player player);

	abstract public String died();

	abstract public String villagersKilled(List<Player> player);

	abstract public String unknownPlayer(String string, GameData data);

	public abstract String unknownBool();

	public abstract String moreWerewolves();

	public abstract String noWerewolves();

	public abstract String villagerWin(String msg);

	public abstract String werewolfWin(String msg);

	public abstract String pressEnter();

	public abstract String wakeUp(Class<? extends Player> clazz, Iterator<Player> players);

	public abstract String getName(Class<? extends Player> clazz);
	public abstract String getNamePlural(Class<? extends Player> clazz);

	public abstract String ambiguousName(String input, GameData data);

	public abstract String seerPrompt();

	public abstract String alreadyDead(Player p);

	public abstract String alreadyDead();

	public abstract String skippedVote();

	public abstract String dayBegins();

	public abstract String nightBegins();

	public abstract String roleAssign();

	public abstract String finalizeRoles();

	public abstract String was();

	public abstract String is();

	public abstract String redLadyPrompt();

	public abstract String everybodyWakesUp();

	public abstract String whoStillLives();

	public abstract String startDiscussion();

	public abstract String youCanVote();

	public abstract String redLadyVisitsHerself();

	public abstract String witchAskHeal(Player victim);

	public abstract String witchHealed(Player victim);

	public abstract String witchAskKill();

	public abstract String witchKilled(Player player);

	public abstract String noPotions();

	public abstract String goToBed(Class<? extends Player> clazz);

	public abstract String examplePlayer(GameData data);
}
