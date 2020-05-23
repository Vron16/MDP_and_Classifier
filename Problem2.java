import java.util.Scanner;

public class Problem2 {
	
	public static double[] valueIteration(double beta) {
		State[] states = new State[10]; // These are all of the states in the MDP.
		double[] oldMaxUtilities = new double[10]; // This stores the states' corresponding max utilities at time k-1.
		double[] newMaxUtilities = new double[10]; // This stores the states' corresponding max utilities being computed at time k.
		String[] optimalPolicies = new String[10]; // This stores the optimal policies for each state when we reach optimal utilities.
		for (int i = 0; i < states.length; i++) {
			switch (i) {
			case 0:
				states[i] = new NewState();
				break;
			case 9:
				states[i] = new DeadState();
				break;
			default:
				// Must be a UsedState
				states[i] = new UsedState(i);
			}
		}
		
		boolean hasConverged = false;
		
		while (!hasConverged) {
			hasConverged = true;
			for (int i = 0; i < states.length; i++) {
				// For each state, call state.updateMaxUtility to get an updated value.
				// Use the return value to gauge whether this particular value is 
				// approaching convergence.
				//System.out.println("Press c to continue: ");
				//Scanner reader = new Scanner(System.in);
				//reader.nextLine();
				double change = states[i].updateMaxUtility(beta, oldMaxUtilities, newMaxUtilities, optimalPolicies);
				//System.out.println(states[i].getStateName() + ": " + change);
				if (change > 0.001)
					hasConverged = false;
				
			}
			// Update oldMaxUtilities to contain the values stored in newMaxUtilities.
			for (int i = 0; i < oldMaxUtilities.length; i++) {
				oldMaxUtilities[i] = newMaxUtilities[i];
			}
		}
		for (int i = 0; i < oldMaxUtilities.length; i++) {
			switch (i) {
			case 0:
//				System.out.println("Optimal utility of state " + new NewState().getStateName() +
//						" is " + oldMaxUtilities[i]);
				System.out.println("Optimal policy for state " + new NewState().getStateName() + 
						" is " + optimalPolicies[i]);
				break;
			case 9:
//				System.out.println("Optimal utility of state " + new DeadState().getStateName() +
//						" is " + oldMaxUtilities[i]);
				System.out.println("Optimal policy for state " + new DeadState().getStateName() + 
						" is " + optimalPolicies[i]);
				break;
			default:
				// Must be a UsedState
//				System.out.println("Optimal utility of state " + new UsedState(i).getStateName() +
//						" is " + oldMaxUtilities[i]);
				System.out.println("Optimal policy for state " + new UsedState(i).getStateName() + 
						" is " + optimalPolicies[i]);
			}
			
		}
		return oldMaxUtilities;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*State currentState = new NewState();
		System.out.println("Starting State: New");
		while (!(currentState instanceof DeadState)) {
			System.out.println("Current state: " + currentState.getStateName()); 
			TransitionResult next = currentState.takeAction("Use");
			currentState = next.getNextState();
			double reward = next.getReward();
			System.out.println("Transitioned to state: " + currentState.getStateName());
			System.out.println("Reward: " + reward);
			System.out.println();
		}*/
		for (int i = 1; i < 100; i += 2) {
			System.out.println("Value Iteration for Beta = " + i*0.01);
			valueIteration(i*0.01);
			System.out.println();
		}
		for (int i = 1; i < 10; i += 2) {
			double beta = 0.99 + i*0.001;
			System.out.println("Value Iteration for Beta = " + beta);
			valueIteration(beta);
			System.out.println();
		}
		System.out.println("Value Iteration for Beta = " + 0.9999);
		valueIteration(0.9999);
		System.out.println();

	}

}
