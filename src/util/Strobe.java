package util;

public class Strobe implements Runnable {

	public final long sleepTime;

	private final Strobable strobe;
	private boolean running = false;

	public Strobe(final Strobable strobe, final double fps) {
		this(strobe, (long) (1000.0 / fps));
	}

	public Strobe(final Strobable strobe, final long sleepTime) {
		this.sleepTime = sleepTime;
		this.strobe = strobe;
	}

	@Override
	public void run() {
		running = true;
		while (running) {
			strobe.strobe();

			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				// do nothing
			}
		}
	}

	public void start() {
		(new Thread(this)).start();
	}

	public void stop() {
		running = false;
	}

	public boolean running() {
		return running;
	}
}
