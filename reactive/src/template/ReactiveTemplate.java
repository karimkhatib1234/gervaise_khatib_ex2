package template;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
//import java.util.Random;

import logist.simulation.Vehicle;
import logist.agent.Agent;
import logist.behavior.ReactiveBehavior;
import logist.plan.Action;
import logist.plan.Action.Move;
import logist.plan.Action.Pickup;
import logist.task.Task;
import logist.task.TaskDistribution;
import logist.topology.Topology;
import logist.topology.Topology.City;

public class ReactiveTemplate implements ReactiveBehavior {
	
	private static final double INITVALUE = -999999;

	private List<City> allCities;
	private HashMap<City, List<State>> statesInCity = new HashMap<City, List<State>>(); // all possible states for a given city
	private HashMap<State, List<AgentAction>> actionsInState = new HashMap<State, List<AgentAction>>(); // available actions for a given state
	private HashMap<State, AgentAction> bestActionInState = new HashMap<State, AgentAction>(); // best action for a given state
	private HashMap<AgentAction, Double> actionReward = new HashMap<AgentAction, Double>(); // reward for a given action
	private HashMap<State, Double> stateProbability = new HashMap<State, Double>();
	private HashMap<State, Double> bestValueInState = new HashMap<State, Double>(); // best value for a given state (used in the learning phase)
	
	private double pPickup;

	@Override
	public void setup(Topology topology, TaskDistribution td, Agent agent) {

		// Reads the discount factor from the agents.xml file
		// If the property is not present it defaults to 0.95
		Double discount = agent.readProperty("discount-factor", Double.class,
				0.95);
		
		// Reads all the cities from the topology.xml file
		allCities = topology.cities();
		
		/*For each city, find all possible states and related actions with their reward 
		  that correspond to the current location of the agent.*/
		for (City cityA : allCities) {
			
			Double taskTrueProbability = (double) 0;
			List<State> statesInCityA = new ArrayList<State>();
			
			//Find all the states in which there is an available task
			for (City cityB : allCities) {
				if (cityB != cityA) {
					taskTrueProbability += td.probability(cityA, cityB);
					State taskState = new State(true, cityA, cityB);
					statesInCityA.add(taskState);
					stateProbability.put(taskState, td.probability(cityA, cityB));
					bestValueInState.put(taskState, INITVALUE);
					
					List<AgentAction> stateActionsList = new ArrayList<AgentAction>();
					AgentAction deliveryAction = new AgentAction(true, cityA, cityB);
					stateActionsList.add(deliveryAction);
					double cost = cityA.distanceTo(cityB)*agent.vehicles().get(0).costPerKm();
					actionReward.put(deliveryAction, td.reward(cityA, cityB) - cost);
					cost = 0;
					
					for (City neighboringCity : cityA.neighbors()) {
						AgentAction noDeliveryAction = new AgentAction(false, cityA, neighboringCity);
						stateActionsList.add(noDeliveryAction);
						cost = cityA.distanceTo(neighboringCity)*agent.vehicles().get(0).costPerKm();
						actionReward.put(noDeliveryAction, -cost);
					}
					actionsInState.put(taskState, stateActionsList);
				}
			}
			
			//Then find all the states in which there is no available task.
			State noTaskState = new State(false, cityA);
			double noTaskProbability = 1 - taskTrueProbability;
			statesInCityA.add(noTaskState);
			stateProbability.put(noTaskState, noTaskProbability);
			bestValueInState.put(noTaskState, INITVALUE);
			
			List<AgentAction> stateActionsList = new ArrayList<AgentAction>();
			for (City neighboringCity : cityA.neighbors()) {
				AgentAction noDeliveryAction = new AgentAction(false, cityA, neighboringCity);
				stateActionsList.add(noDeliveryAction);
				double cost = cityA.distanceTo(neighboringCity)*agent.vehicles().get(0).costPerKm();
				actionReward.put(noDeliveryAction, -cost);
			}
			actionsInState.put(noTaskState, stateActionsList);
			statesInCity.put(cityA, statesInCityA);
		}

		this.pPickup = discount;
		
		plan(); //offline reinforcement learning
	}

	@Override
	public Action act(Vehicle vehicle, Task availableTask) {
		
		Action action;
		City currentCity = vehicle.getCurrentCity();
		
		if(availableTask != null && currentCity == availableTask.pickupCity){ //search if a task is available in the current city
			State nowState = new State(true, currentCity, availableTask.deliveryCity);
			AgentAction agentAction =  bestActionInState.get(nowState);
			
			if(agentAction.isDelivering()){
				System.out.println(vehicle.name() + " picks up a task from "+ availableTask.pickupCity + " to " + availableTask.deliveryCity + ".");
				action = new Pickup(availableTask);
			} else{
				System.out.println(vehicle.name() + " refuses a task from "+ availableTask.pickupCity + " to "+  availableTask.deliveryCity + ". It moves to " + agentAction.getDestination() + ".");
				action = new Move(agentAction.getDestination());
			}
			
		} else{	
			State nowState = new State(false, currentCity);
			AgentAction agentAction =  bestActionInState.get(nowState);
			action = new Move(agentAction.getDestination());
			System.out.println(vehicle.name() + " has no available task. It just moves from " + vehicle.getCurrentCity() + " to " + agentAction.getDestination() + ".");

		}
		return action;
	}
	
	public void plan(){
		
		boolean isConvergence=false;
		
		// Markov Decision Process iterations
		while(!isConvergence){ // Loop until good enough
			
			isConvergence=true;
			
			for(City city:allCities){
				
				for(State state:statesInCity.get(city)){ 
					
					List<AgentAction> actionLists = actionsInState.get(state);
					AgentAction maxAction= bestActionInState.get(state);
					double maxActionValue = bestValueInState.get(state);
					
					for(AgentAction action:actionLists){
						double accumulateValue = 0;
						double reward = actionReward.get(action);
						accumulateValue = reward;
						City destination = action.getDestination();
						for(State nextState:statesInCity.get(destination)){
							accumulateValue += this.pPickup * stateProbability.get(nextState)*bestValueInState.get(nextState);
						}
						
						if(maxActionValue<accumulateValue){
							maxActionValue = accumulateValue;
							maxAction=action;
							isConvergence=false;
						}
					}
					
					bestActionInState.put(state, maxAction);
					bestValueInState.put(state, maxActionValue);
				}
			}
		}
	}
	
}