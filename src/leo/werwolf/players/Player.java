package leo.werwolf.players;

import leo.werwolf.GameData;
import leo.werwolf.utils.Strings;

import java.util.List;

import static leo.rustjava.iterator.Iterators.once;

public abstract class Player {
	protected boolean alive = true;
	public final String name;

	protected final static Strings strings = Strings.getDefault();

	public Player(String name) {
		this.name = name;
	}

	public static boolean callIfNone(Class<? extends Player> clazz) {
		return switch (clazz.getSimpleName()) {
			case "Villager", "Werewolf" -> false;
			case "Seer", "Witch" -> true;
			default -> throw new IllegalStateException("fallthrough: need to add cases");
		};
	}

	public abstract boolean isWerewolf();
	public abstract void action(GameData gameData);
	public boolean alive() {
		return alive;
	}

	public void die() {
		alive = false;
	}

	public List<Player> onKill() {
		alive = false;
		return once(this).toList();
	}

	public void onOtherKill(Player otherPlayer, GameData data) {}

	@Override
	public String toString() {
		return name;
	}

	public String role() {
		return Strings.getDefault().getName(getClass());
	}
}
