package leo.werwolf;

import leo.werwolf.players.Player;

import static leo.rustjava.iterator.Iterators.*;

import java.util.*;

public class GameDataBuilder {
	final Map<Class<? extends Player>, Integer> roles = new HashMap<>();
	final List<String> names = new ArrayList<>();
	boolean shuffle = true;
	boolean outputDeathRoles = true;

	public GameDataBuilder() {
	}

	public GameDataBuilder role(Class<? extends Player> roleClass, int amount) {
		if (roles.containsKey(roleClass)) {
			throw new IllegalStateException("tried assigning a role twice");
		}
		roles.put(roleClass, amount);
		return this;
	}

	public GameDataBuilder players(String... addNames) {
		for (String name : addNames) {
			if (names.contains(name)) throw new IllegalStateException("can't give two players the same name");
			names.add(name);
		}
		return this;
	}

	public GameDataBuilder shuffle(boolean shuffle) {
		this.shuffle = shuffle;
		return this;
	}

	public GameDataBuilder outputDeathRoles(boolean outputDeathRoles) {
		this.outputDeathRoles = outputDeathRoles;
		return this;
	}

	public GameDataBuilder assertPlayers(int i) {
		int amountOfRoles = from(roles.values()).reduce(Integer::sum).unwrapOr(0);
		if (i != amountOfRoles) throw new AssertionError(
				"Invalid amount of roles: expected " + i + ", got " + amountOfRoles
		);
		if (i != names.size()) throw new AssertionError(
				"Invalid amount of players: expected " + i + ", got " + names.size()
		);
		return this;
	}

	public GameData build() {
		return new GameData(this);
	}
}
