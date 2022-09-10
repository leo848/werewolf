package leo.werwolf.utils;

import leo.rustjava.*;
import leo.werwolf.GameData;
import leo.werwolf.players.Player;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.lang.System.out;
import static leo.rustjava.Option.*;
import static leo.rustjava.Result.*;
import static leo.rustjava.Unit.Unit;

public class Prompts {
	static private final Scanner scan = new Scanner(System.in);

	static private final Strings strings = Strings.getDefault();

	static private final List<String> positives = List.of("y", "yes", "j", "ja", "true", "1");
	static private final List<String> negatives = List.of("n", "no", "nein", "false", "0");


	public static boolean bool() {
		do {
			var input = scan.nextLine();
			if (positives.contains(input)) return true;
			else if (negatives.contains(input)) return false;
			else {
				out.println(strings.unknownBool());
			}
		} while (true);
	}

	public static boolean bool(String prompt) {
		out.print(prompt + "\t");
		return bool();
	}

	public static String next() {
		return scan.nextLine();
	}

	public static String next(String prompt) {
		out.print(prompt + "\t");
		return next();
	}

	public static void waitForInput() {
		waitForInput(strings.pressEnter());
	}

	public static void waitForInput(String input) {
		out.print(input);
		scan.nextLine();
	}

	public static Player player(GameData data) {
		return maybePlayer(data).unwrapOrElse(() -> {
			out.println(strings.examplePlayer(data));
			return player(data);
		});
	}

	public static Player alivePlayer(GameData data) {
		return Some(player(data))
				.andThen(p -> {
					if (p.alive()) return Some(p);
					out.println(strings.alreadyDead(p));
					return None();
				})
				.unwrapOrElse(() -> alivePlayer(data));
	}

	public static Option<Player> maybePlayer(GameData data) {
		var input = next().toLowerCase();
		if (input.isBlank()) {
			return None();
		}
		if (input.strip().equalsIgnoreCase("list")) {
			out.println(data.players().join(", "));
			return maybePlayer(data);
		}
		for (Player s: data.players()) {
			if (s.name.toLowerCase().equals(input)) return Some(s);
		}
		Option<Option<Player>> foundPlayer = data.players()
				.map(p -> new Pair<>(p.toString(), p))
				.filter(pair -> pair.left().toLowerCase().startsWith(input))
				.map(Pair::right)
				.fold(None(), (opt, s) -> {
					if (opt.isNone()) return Some(Some(s));
					else return Some(None());
				});

		if (Option.flatten(foundPlayer).isSome()) {
			var player = foundPlayer.unwrap().unwrap();
			out.println(player.name);
			return Some(player);
		} else if (foundPlayer.isNone()) {
			out.println(strings.unknownPlayer(input, data));
		} else {
			out.println(strings.ambiguousName(input, data));
		}

		return maybePlayer(data);
	}

	public static Player player(String prompt, GameData data) {
		out.println(prompt + "\t");
		return player(data);
	}

	public static Player player(String prompt, Function<Player, Result<Unit, String>> verifier, GameData data) {
		out.println(prompt);
		return player(verifier, data);
	}

	public static Player player(Function<Player, Result<Unit, String>> verifier, GameData data) {
		var player = player(data);
		var verification = verifier.apply(player);
		if (verification.isOk()) return player;
		verification.inspectErr(out::println);
		return player(verifier, data);
	}

	public static Player alivePlayer(String prompt, GameData data) {
		out.println(prompt + "\t");
		return alivePlayer(data);
	}

	public static Option<Player> maybePlayer(String prompt, GameData data) {
		out.println(prompt + "\t");
		return maybePlayer(data);
	}

	public static Option<Player> maybePlayer(String prompt, Function<Player, Result<Unit, String>> verifier, GameData data) {
		out.println(prompt);
		return maybePlayer(verifier, data);
	}

	public static Option<Player> maybePlayer(Function<Player, Result<Unit, String>> verifier, GameData data) {
		var player = maybePlayer(data);
		if (player.isNone()) return None();
		var verify = verifier.apply(player.unwrap());
		if (verify.isOk()) return Some(player.unwrap());
		verify.inspectErr(out::println);
		return maybePlayer(verifier, data);
	}

	public static Option<Player> maybeAlivePlayer(String prompt, GameData data) {
		out.println(prompt + "\t");
		return maybeAlivePlayer(data);
	}

	public static Option<Player> maybeAlivePlayer(GameData data) {
		return maybePlayer(player -> player.alive() ? Ok(Unit()) : Err(strings.alreadyDead(player)), data);
	}
}
