package template;

import logist.topology.Topology.City;

public class AgentAction {
	private boolean delivering;
	private City departure;
	private City destination;

	public boolean isDelivering() {
		return delivering;
	}

	public void setDelivering(boolean delivering) {
		this.delivering = delivering;
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

	public AgentAction(boolean delivering, City departure, City destination) {
		super();
		this.delivering = delivering;
		this.departure = departure;
		this.destination = destination;
	}
	
	
}
