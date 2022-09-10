package leo.werwolf.utils;

import leo.rustjava.Pair;
import leo.rustjava.iterator.Iterator;
import leo.werwolf.GameData;
import leo.werwolf.players.Player;

import java.util.List;
import java.util.Random;

import static leo.rustjava.iterator.Iterators.from;

@SuppressWarnings("SpellCheckingInspection")
public class GermanStrings extends Strings {
	public static final Strings instance = new GermanStrings();
	private final Random random = new Random();

	private GermanStrings() {
	}

	public static Strings get() {
		return instance;
	}

	@Override
	public String died(Player player) {
		return player.name + " ist tot.";
	}

	@Override
	public String died() {
		return "Gestorben ist";
	}

	@Override
	public String villagersKilled(List<Player> players) {
		if (players.isEmpty()) return "Ihr habt niemanden getötet.";
		if (players.size() == 1) return players.get(0).name + " wurde verbrannt.";
		if (players.size() == 2) return players.get(0) + " und " + players.get(1) + " sind nun tot.";
		return "Durch eure Wahl starben: " + from(players).join(", ");
	}

	@Override
	public String unknownPlayer(String string, GameData data) {
		var bestMatches = data
				.players()
				.map(p -> p.name)
				.map(p -> new Pair<>(MathUtils.levenshtein(string, p), p))
				.sortedByKey(Pair::left)
				.enumerate()
				.takeWhile(p -> p.left() < 3 && p.right().left() < 6)
				.map(Pair::right)
				.map(Pair::right)
				.join(", ");
		return "Ungültiger Spieler: " + string
				+ (bestMatches.isEmpty() ? "" : "\nMeintest du vielleicht: " + bestMatches);
	}

	@Override
	public String unknownBool() {
		return "Gib bitte 'Ja' oder 'Nein' ein.";
	}

	@Override
	public String moreWerewolves() {
		return "Es gibt mehr Werwölfe als Dorfbewohner";
	}

	@Override
	public String noWerewolves() {
		return "Es gibt keine Werwölfe mehr";
	}

	@Override
	public String villagerWin(String msg) {
		return "\nDie Dorfbewohner haben gewonnen: " + msg + "!";
	}

	@Override
	public String werewolfWin(String msg) {
		return "\nDie Werwölfe haben gewonnen: " + msg + "!";
	}

	@Override
	public String pressEnter() {
		return "Drücke <Enter>, um fortzufahren...";
	}

	@Override
	public String wakeUp(Class<? extends Player> clazz, Iterator<Player> players) {
		if (players.copy().count() == 1) {
			return "%s wacht auf. (%s)".formatted(getName(clazz), players.join(""));
		} else {
			return "Die %s wachen auf. (%s)".formatted(getNamePlural(clazz), players.join(", "));
		}
	}

	@Override
	public String getName(Class<? extends Player> clazz) {
		return switch (clazz.getSimpleName()) {
			case "Villager" -> "Dorfbewohner";
			case "Werewolf" -> "Werwolf";
			case "Seer" -> "Seherin";
			case "Witch" -> "Hexe";
			case "RedLady" -> "Prostituierte";
			case "Cupid" -> "Amor";
			default -> clazz.getSimpleName() + " (keine Übersetzung)";
		};
	}

	@Override
	public String getNamePlural(Class<? extends Player> clazz) {
		return switch (clazz.getSimpleName()) {
			case "Villager" -> "Dorfbewohner";
			case "Werewolf" -> "Werwölfe";
			case "Seer" -> "Seher";
			case "Witch" -> "Hexen";
			case "RedLady" -> "Prostituierte";
			case "Cupid" -> "Amore";
			default -> clazz.getSimpleName() + " (keine Übersetzung)";
		};
	}

	@Override
	public String ambiguousName(String input, GameData data) {
		return "mehrdeutig: '" + input + "' könnte sich auf " + data.players()
				.filter(p -> p.name.toLowerCase().startsWith(input.toLowerCase()))
				.join(" oder ") + " beziehen.";
	}

	@Override
	public String seerPrompt() {
		return random("Wessen Rolle möchtest du aufdecken? ", "Wem willst du unter die Karten schauen?");
	}

	@Override
	public String alreadyDead(Player p) {
		return random(
				"%s ist nicht mehr mit uns.",
				"%s ist tot."
		).formatted(p.name);
	}

	@Override
	public String alreadyDead() {
		return "Spiele nicht mit den Toten.";
	}

	@Override
	public String skippedVote() {
		return "<übersprungen>";
	}

	@Override
	public String dayBegins() {
		return "Drücke <Enter>, um den Tag zu beginnen\n";
	}

	@Override
	public String nightBegins() {
		return "Alle schlafen ein.\nDrücke Enter, um fortzufahren.\n";
	}

	@Override
	public String roleAssign() {
		return "Rollenzuweisung: ";
	}

	@Override
	public String finalizeRoles() {
		return random("Das waren die Rollen: ", "Die Rollen waren: ", "Die Karten werden aufgedeckt: ");
	}

	@Override
	public String was() {
		return "war";
	}

	@Override
	public String is() {
		return "ist";
	}

	@Override
	public String redLadyPrompt() {
		return random("Bei wem möchtest du heute übernachten?",
				"Wessen Haus besuchst du heute?",
				"Wer ist heute der oder die Glückliche?");
	}

	@Override
	public String everybodyWakesUp() {
		return random("Alle erwachen...", "Der Schlaf ist vorbei...", "Alle stehen auf...");
	}

	@Override
	public String whoStillLives() {
		return "Es leben noch: ";
	}

	@Override
	public String startDiscussion() {
		return random("Ihr könnt jetzt diskutieren...", "Die Diskussion beginnt.");
	}

	@Override
	public String youCanVote() {
		return "du kannst jetzt abstimmen";
	}

	@Override
	public String redLadyVisitsHerself() {
		return random("ಠ_ಠ", "ಠ_ಠ", "ಠ_ಠ", "ಠ_ಠ", "Du hast zu wenig Geld");
	}

	@Override
	public String witchAskHeal(Player victim) {
		return "%s ist das Opfer. Möchtest du es heilen?".formatted(victim.name);
	}

	@Override
	public String witchHealed(Player victim) {
		return "Du hast %s geheilt.".formatted(victim.name);
	}

	@Override
	public String witchAskKill() {
		return "Möchtest du jemanden töten?";
	}

	@Override
	public String witchKilled(Player player) {
		return random("%s wurde vergiftet.", "Der Trank hat %s sofort getötet.").formatted(player.name);
	}

	@Override
	public String noPotions() {
		return random("Du hast keine Tränke mehr.", "Dein Inventar ist leer.");
	}

	@Override
	public String goToBed(Class<? extends Player> clazz) {
		return "%s/%s schläft wieder ein.".formatted(getName(clazz), getNamePlural(clazz));
	}

	@Override
	public String examplePlayer(GameData data) {
		return "Bitte gib einen Spieler ein.\nBeispiel: %s"
				.formatted(data.players.get(random.nextInt(data.players.size())));
	}
}