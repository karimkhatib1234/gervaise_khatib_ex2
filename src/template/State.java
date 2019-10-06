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
	
	public State(boolean taskAvailable, City departure, City destination) {
		super();
		this.taskAvailable = taskAvailable;
		this.departure = departure;
		this.destination = destination;
	}

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
