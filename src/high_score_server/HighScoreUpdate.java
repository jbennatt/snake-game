package high_score_server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeSet;

public class HighScoreUpdate implements Runnable {
	public static final int NAME_INDEX = 0;
	public static final int TIME_INDEX = 1;
	public static final int SCORE_INDEX = 2;

	public static final int NUM_SCORES = 10;
	public static final int NUM_BANNED = 1249;
	public static final int MAX_NAME_LENGTH = 30;

	private static final String HIGH_SCORE_FILE = "high scores.txt";
	private static final String BANNED_WORDS_FILE = "banned words.txt";

	private final ArrayList<String> bannedWords = new ArrayList<>(NUM_BANNED);
	private final TreeSet<HighScore> highScores = new TreeSet<>();

	public HighScoreUpdate() {
		readHighScores();
		readBannedWords();

		Runtime.getRuntime().addShutdownHook(new Thread(this));
	}

	public void run() {
		// this is the shut down hook which will write the contents of High
		// Scores to the high scores file

		try {
			final PrintWriter pw = new PrintWriter(new File(HIGH_SCORE_FILE));

			System.err.println("writing to: " + HIGH_SCORE_FILE);
			for (final HighScore hs : highScores) {
				System.err.println("writing: " + hs);
				hs.write(pw);
			}

			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void readHighScores() {
		Scanner scanner;
		try {
			scanner = new Scanner(new File(HIGH_SCORE_FILE));

			while (scanner.hasNextLine()) {
				highScores.add(new HighScore(scanner));
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void readBannedWords() {
		try {
			final Scanner scanner = new Scanner(new File(BANNED_WORDS_FILE));

			while (scanner.hasNextLine())
				bannedWords.add(scanner.nextLine().toLowerCase());

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public synchronized boolean isHighScore(final int score, final double time) {
		// check lowest score
		if (highScores.size() < NUM_SCORES)
			return true;
		// else check if this goes into the list

		final HighScore lowest = highScores.last();

		// if lowest's compares to score and time and it's positive,
		// then lowest comes after this score in the list (so should
		// be removed later)
		return lowest.compareTo(score, time) > 0;
	}

	public synchronized int readHighScores(final HighScore[] scores) {
		// return updated high scores into scores if non-null
		if (scores != null) {
			int index = 0;
			for (final HighScore hs : highScores) {
				// System.out.println((index + 1) + ": " + hs);
				if (index < scores.length)
					scores[index++] = hs;
				else // quit
					return index;
			}
		}

		return highScores.size();
	}

	/**
	 * It is assumed that it's already been established that this is a high
	 * score so this will always either just add the score (if high scores isn't
	 * full) or remove the lowest score and then add this new one.
	 */
	public synchronized void addHighScore(final String name, final int score, final double time) {
		System.out.println("adding high score: " + name + ", " + score + ", " + time);
		if (name.length() > MAX_NAME_LENGTH || banned(name)) {
			System.out.println(name + " was banned or too long");
			// if name is too long or banned, don't insert high score and just
			// return old set of high scores
			return;
		}

		// high scores isn't full, just insert
		if (highScores.size() < NUM_SCORES)
			highScores.add(new HighScore(name, score, time));
		else {
			highScores.pollLast(); // remove lowest score
			highScores.add(new HighScore(name, score, time));
		}
	}

	private boolean banned(final String name) {
		final String nameLC = name.toLowerCase();
		for (final String banned : bannedWords)
			if (nameLC.contains(banned)) {
				System.out.println(name + " contained " + banned);
				return true;
			}

		return false;
	}

}

class HighScore implements Comparable<HighScore> {
	final String name;
	final int score;
	final double time;

	public HighScore(final String name, final int score, final double time) {
		this.name = name;
		this.score = score;
		this.time = time;
	}

	public HighScore(final Scanner scanner) {
		this(scanner.nextLine(), Integer.parseInt(scanner.nextLine()), Double.parseDouble(scanner.nextLine()));
	}

	public void write(final PrintWriter pw) {
		pw.println(name);
		pw.println(score);
		pw.println(time);
	}

	@Override
	public String toString() {
		return name + " " + score + " " + time;
	}

	public boolean equals(final Object o) {
		if (o instanceof HighScore) {
			final HighScore hs2 = (HighScore) o;
			return this.score == hs2.score && Double.compare(this.time, hs2.time) == 0;
		}
		// else
		return false;
	}

	@Override
	public int compareTo(HighScore hs2) {
		return compareTo(hs2.score, hs2.time);
	}

	public int compareTo(final int score, final double time) {
		// max comes first, compare in reverse order
		int cmp = Integer.compare(score, this.score);
		if (cmp == 0) {
			cmp = Double.compare(this.time, time);
			return cmp;
		}

		return cmp;
	}
}
