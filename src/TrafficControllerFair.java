import java.util.concurrent.locks.*;

public class TrafficControllerFair implements TrafficController {
	private TrafficRegistrar registrar;
	private Lock lock = new ReentrantLock(true);
	private Condition bridgeFree = lock.newCondition();
	private boolean bridgeOccupied = false;

	public TrafficControllerFair(TrafficRegistrar r) {
		this.registrar = r;
	}

	private void occupyBridge() {
		while (bridgeOccupied) {
			try {
				bridgeFree.await();
			} catch (InterruptedException e) {}
		}
		bridgeOccupied = true;
	}

	private void freeBridge() {
		bridgeOccupied = false;
		bridgeFree.signal();
	}

	public void enterRight(Vehicle v) {
		lock.lock();
		try {
			occupyBridge();
			registrar.registerRight(v);
		} finally {
			lock.unlock();
		}
	}

	public void enterLeft(Vehicle v) {
		lock.lock();
		try {
			occupyBridge();
			registrar.registerLeft(v);
		} finally {
			lock.unlock();
		}
	}

	public void leaveLeft(Vehicle v) {
		lock.lock();
		try {
			registrar.deregisterLeft(v);
			freeBridge();
		} finally {
			lock.unlock();
		}
	}

	public void leaveRight(Vehicle v) {
		lock.lock();
		try {
			registrar.deregisterRight(v);
			freeBridge();
		} finally {
			lock.unlock();
		}
	}
}
