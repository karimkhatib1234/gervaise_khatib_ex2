package template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	private HashMap<City, List<State>> statesInCity = new HashMap<City, List<State>>();
	private HashMap<State, List<AgentAction>> actionsInState = new HashMap<State, List<AgentAction>>();
	private HashMap<State, AgentAction> bestActionInState = new HashMap<State, AgentAction>();
	private HashMap<AgentAction, Double> actionReward = new HashMap<AgentAction, Double>();
	private HashMap<State, Double> stateProbability = new HashMap<State, Double>();
	private HashMap<State, Double> bestValueInState = new HashMap<State, Double>();
	
	private Random random;
	private double pPickup;
	private int numActions;
	private Agent myAgent;

	@Override
	public void setup(Topology topology, TaskDistribution td, Agent agent) {

		// Reads the discount factor from the agents.xml file.
		// If the property is not present it defaults to 0.95
		Double discount = agent.readProperty("discount-factor", Double.class,
				0.95);
		
		allCities = topology.cities();
		
		for (City cityA : allCities) {
			Double taskTrueProbability = (double) 0;
			
			List<State> statesInCityA = new ArrayList<State>();
			
			for (City cityB : allCities) {
				State taskState = new State(true, cityA, cityB);
				statesInCityA.add(taskState);
				stateProbability.put(taskState, td.probability(cityA, cityB));
				bestValueInState.put(taskState, INITVALUE);
				
				List<AgentAction> stateActionsList = new ArrayList<AgentAction>();
				AgentAction deliveryAction = new AgentAction(true, cityA, cityB);
				stateActionsList.add(deliveryAction);
				double cost = cityA.distanceTo(cityB)*agent.vehicles().get(0).costPerKm();
				actionReward.put(deliveryAction, td.reward(cityA, cityB) - cost);
				
				for (City neighboringCity : cityA.neighbors()) {
					
				}
			}
		}

		this.random = new Random();
		this.pPickup = discount;
		this.numActions = 0;
		this.myAgent = agent;
	}

	@Override
	public Action act(Vehicle vehicle, Task availableTask) {
		Action action;

		if (availableTask == null || random.nextDouble() > pPickup) {
			City currentCity = vehicle.getCurrentCity();
			action = new Move(currentCity.randomNeighbor(random));
		} else {
			action = new Pickup(availableTask);
		}
		
		if (numActions >= 1) {
			System.out.println("The total profit after "+numActions+" actions is "+myAgent.getTotalProfit()+" (average profit: "+(myAgent.getTotalProfit() / (double)numActions)+")");
		}
		numActions++;
		
		return action;
	}
}
