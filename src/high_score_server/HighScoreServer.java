package high_score_server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class HighScoreServer {
	public static final int PORT = 15000;
	public static final String SERVER_IP = "10.65.10.24";

	public static final int HIGH_SCORE_QUERY = 0;
	public static final int UPDATE_QUERY = 1;

	private final HighScoreUpdate hsu = new HighScoreUpdate();
	private String name;
	private int score;
	private double time;
	private final HighScore[] scores = new HighScore[HighScoreUpdate.NUM_SCORES];

	public void start() {
		while (true) {
			try {
				final ServerSocket ss = new ServerSocket(PORT);
				System.out.print("Accepting a connection...");
				final Socket client = ss.accept();

				System.out
						.println("Accepted connection from: " + client.getInetAddress() + ":" + client.getLocalPort());

				final DataInputStream dis = new DataInputStream(client.getInputStream());

				switch (dis.readInt()) {
				case HIGH_SCORE_QUERY:
					sendHighScores(client.getOutputStream());
					break;
				case UPDATE_QUERY:
					System.out.println("got an update query");
					updateHighScores(ss, dis, client.getOutputStream());
					break;
				}
			} catch (IOException e) {
				System.out.println("There was an exception: " + e.getLocalizedMessage());
				System.exit(1);
			}
		}
	}

	private void updateHighScores(final ServerSocket ss, final DataInputStream dis, final OutputStream os)
			throws IOException {
		final DataOutputStream dos = new DataOutputStream(os);
		readScore(dis); // sets score and time

		if (hsu.isHighScore(score, time)) {
			dos.writeChar('y');
			System.out.println("sent response of \"yes\"");
			readName(dis);
			hsu.addHighScore(name, score, time);
			ss.close();
		} else {
			dos.writeChar('n');
			// don't close socket (let client close it)
		}
	}

	private void sendHighScores(final OutputStream os) throws IOException {
		final DataOutputStream dos = new DataOutputStream(os);
		final int numScores = hsu.readHighScores(scores);
		dos.writeInt(numScores);
		System.out.println("sending: " + numScores + " scores");
		for (int i = 0; i < numScores; ++i) {
			writeString(scores[i].name, dos);
			System.out.println((i + 1) + ": wrote name: " + scores[i].name);
			dos.writeInt(scores[i].score);
			System.out.println((i + 1) + ": wrote integer: " + scores[i].score);
			dos.writeDouble(scores[i].time);
			System.out.println((i + 1) + ": wrote double: " + scores[i].time);
		}
	}

	public static void writeString(final String str, final DataOutputStream dos) throws IOException {
		dos.writeInt(str.length());

		for (final char c : str.toCharArray())
			dos.writeChar(c);
	}

	public static String readString(final DataInputStream dis) throws IOException {
		final char[] str = new char[dis.readInt()];

		for (int i = 0; i < str.length; ++i)
			str[i] = dis.readChar();

		return new String(str);
	}

	private void readScore(final DataInputStream dis) throws IOException {
		// read score
		score = dis.readInt();
		System.out.println("read length: " + score);
		time = dis.readDouble();
		System.out.println("read time: " + time);
	}

	private void readName(final DataInputStream dis) throws IOException {
		final int len = dis.readInt();

		name = "";
		for (int i = 0; i < len; ++i)
			name += dis.readChar();

		System.out.println("read name: " + name);
	}

	public static void main(String... args) {
		final HighScoreServer server = new HighScoreServer();

		server.start();
	}
}
