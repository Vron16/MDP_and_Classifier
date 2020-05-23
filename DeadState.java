
public class DeadState extends State{
	
	public DeadState() {
		super();
		this.possibleActions.add("Replace");
	}
	
	public TransitionResult takeAction(String action) {
		if (!this.possibleActions.contains(action)) {
			System.out.println("The current state cannot perform the specified action.");
			return null;
		}
		// Only action that can be taken is "Replace".
		// Goes to state New with probability 1 and reward -255.
		return new TransitionResult("New", -255.0, 0);
		
	}
	
	public double updateMaxUtility(double beta, double[] oldMaxUtilities, double[] newMaxUtilities,
			String[] optimalPolicies) {
		// Iterate through all possible actions that can be taken from the current
		// state and update the maximum utility accordingly.
		double maxUtility = Integer.MIN_VALUE;
		for (int i = 0; i < this.possibleActions.size(); i++) {
			String action = this.possibleActions.get(i);
			if (action.equals("Replace")) {
				// This is effectively guaranteed, as it's the only one on the list.
				double immediateUtility = -255;
				// Future utility has no probability component as its guaranteed to 
				// transition to New.
				double futureUtility = beta*oldMaxUtilities[0]; // Most recently calculated max utility at New.
				double totalUtility = immediateUtility + futureUtility;
				maxUtility = Math.max(totalUtility, maxUtility);
			}
		}
		// Update maxUtilities[0] with the newly calculated value and return the difference
		// for purposes of determining convergence.
		
		newMaxUtilities[newMaxUtilities.length-1] = maxUtility;
		optimalPolicies[optimalPolicies.length-1] = "Replace"; // This is our only option here.
//		System.out.println("Dead State Optimal Utility: " + maxUtility + " Optimal Policy: "
//				+ optimalPolicies[optimalPolicies.length-1]);
		return Math.abs(newMaxUtilities[newMaxUtilities.length-1] - oldMaxUtilities[oldMaxUtilities.length-1]);
	}
	
	public String getStateName() {
		return "Dead";
	}

}
