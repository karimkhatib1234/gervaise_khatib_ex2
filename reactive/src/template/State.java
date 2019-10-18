package template;

import logist.topology.Topology.City;

/*
 * This class has 3 properties: 
 *  - Whether there is a task available for pick up
 *  - City where the agent is located (which equals the departure city if a task is available)
 *  - Task destination city
 *  Note : If property 1 is "false", properties 2 and 3 are NULL.
 */
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

	public State(boolean taskAvailable, City departure) {
		super();
		this.taskAvailable = taskAvailable;
		this.departure = departure;
		this.destination = null;
	}
	
	public State(boolean taskAvailable, City departure, City destination) {
		super();
		this.taskAvailable = taskAvailable;
		this.departure = departure;
		this.destination = destination;
	}

	// We use HashMap to store agent actions, thus we override the methods hashCode and equals
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departure == null) ? 0 : departure.hashCode());
		result = prime * result + ((destination == null) ? 0 : destination.hashCode());
		result = prime * result + (taskAvailable ? 1231 : 1237);
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
		State other = (State) obj;
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
		if (taskAvailable != other.taskAvailable)
			return false;
		return true;
	}
	
}
