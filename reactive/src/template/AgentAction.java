package template;

import logist.topology.Topology.City;

/*This class defines how the agent conducts an action through 3 properties:
 * - Whether the agent is delivering a task in this action.
 * - Departure city
 * - Destination city
 */

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

	// We use HashMap to store agent actions, thus we override the methods hashCode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (delivering ? 1231 : 1237);
		result = prime * result + ((departure == null) ? 0 : departure.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AgentAction other = (AgentAction) obj;
		if (delivering != other.delivering)
			return false;
		if (departure == null) {
			if (other.departure != null)
				return false;
		} else if (!departure.equals(other.departure))
			return false;
		if (destination == null) {
			if (other.destination != null)
				return false;
		} else if (!destination.equals(other.destination))
			return false;
		return true;
	}
	
}
