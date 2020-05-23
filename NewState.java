
public class NewState extends State {
	
	public NewState() {
		super();
		this.possibleActions.add("Use");
	}
	
	public TransitionResult takeAction(String action) {
		if (!this.possibleActions.contains(action)) {
			System.out.println("The current state cannot perform the specified action.");
			return null;
		}
		// Only action that can be taken is "Use".
		// Goes to state Used1 with probability 1 and reward 100.
		return new TransitionResult("Used", 100.0, 1);
		
	}
	
	public double updateMaxUtility(double beta, double[] oldMaxUtilities, double[] newMaxUtilities,
			String[] optimalPolicies) {
		// Iterate through all possible actions that can be taken from the current
		// state and update the maximum utility accordingly.
		double maxUtility = Integer.MIN_VALUE;
		for (int i = 0; i < this.possibleActions.size(); i++) {
			String action = this.possibleActions.get(i);
			if (action.equals("Use")) {
				// This is effectively guaranteed, as it's the only one on the list.
				double immediateUtility = 100;
				// Future utility has no probability component as its guaranteed to 
				// transition to Used1.
				double futureUtility = beta*oldMaxUtilities[1]; // Most recently calculated max utility at Used1.
				double totalUtility = immediateUtility + futureUtility;
				maxUtility = Math.max(totalUtility, maxUtility);
			}
		}
		// Update maxUtilities[0] with the newly calculated value and return the difference
		// for purposes of determining convergence.
		newMaxUtilities[0] = maxUtility;
		optimalPolicies[0] = "Use"; // This is the only policy we can take here.
//		System.out.println("New State Optimal Utility: " + maxUtility + " Optimal Policy: "
//				+ optimalPolicies[0]);
		return Math.abs(newMaxUtilities[0] - oldMaxUtilities[0]);
	}
	
	public String getStateName() {
		return "New";
	}

}
