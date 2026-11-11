public class TrafficControllerSimple implements TrafficController {
	private TrafficRegistrar registrar;
	private boolean bridgeOccupied = false;

	public TrafficControllerSimple(TrafficRegistrar r) {
		this.registrar = r;
	}

	private void occupyBridge() {
		while (bridgeOccupied) {
			try {
				wait();
			} catch (InterruptedException e) {}
		}
		bridgeOccupied = true;
	}

	private void freeBridge() {
		bridgeOccupied = false;
		notifyAll();
	}

	public synchronized void enterRight(Vehicle v) {
		occupyBridge();
		registrar.registerRight(v);
	}

	public synchronized void enterLeft(Vehicle v) {
		occupyBridge();
		registrar.registerLeft(v);
	}

	public synchronized void leaveLeft(Vehicle v) {
		registrar.deregisterLeft(v);
		freeBridge();
	}

	public synchronized void leaveRight(Vehicle v) {
		registrar.deregisterRight(v);
		freeBridge();
	}
}
