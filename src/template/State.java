package template;

import logist.topology.Topology.City;

public class State {
	private boolean taskAvailable;
	private City departure;
	private City destination;

	public boolean isTaskAvailable() {
		return taskAvailable;
	}
	
	public void setTaskAvailable(boolean taskAvailable) {
		this.taskAvailable = taskAvailable;
	}
	
	public City getDeparture() {
		return departure;
	}
	
	public void setDeparture(City departure) {
		this.departure = departure;
	}
	
	public City getDestination() {
		return destination;
	}
	
	public void setDestination(City destination) {
		this.destination = destination;
	}

	public State(boolean taskAvailable) {
		super();
		this.taskAvailable = taskAvailable;
		this.departure = null;
		this.destination = null;
	}
	
//	public State(boolean taskAvailable, City departure, City destination) {
//		super();
//		this.taskAvailable = taskAvailable;
//		this.departure = departure;
//		this.destination = destination;
//	}
	
}
