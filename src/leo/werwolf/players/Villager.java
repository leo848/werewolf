package leo.werwolf.players;

import leo.rustjava.iterator.Iterators;
import leo.werwolf.GameData;

import java.util.List;

public class Villager extends Player {
	public Villager(String name) {
		super(name);
	}

	@Override
	public boolean isWerewolf() {
		return false;
	}

	@Override
	public void action(GameData gameData) { }

	@Override
	public boolean alive() {
		return alive;
	}
}
