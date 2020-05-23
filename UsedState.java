
public class UsedState extends State {
	private int usedNum;
	
	public UsedState(int usedNum) {
		super();
		this.possibleActions.add("Use");
		this.possibleActions.add("Replace");
		this.usedNum = usedNum;
	}
	
	public TransitionResult takeAction(String action) {
		if (!this.possibleActions.contains(action)) {
			System.out.println("The current state cannot perform the specified action.");
			return null;
		}
		if (action.equals("Use")) {
			if (usedNum == 8) {
				// Use8 is a special case, where it transitions to Dead if the randomly
				// generated number is less than 0.8.
				double randNum = Math.random();
				System.out.println("Probability: " + 0.8);
				System.out.println("Randomly generated number: " + randNum);
				if (randNum < 0.8) {
					// nextState is Dead and reward is 20.
					return new TransitionResult("Dead", 20.0, 0);
				}
				// Otherwise, next state is Used8 again.
				return new TransitionResult("Used", 20.0, 8);
			}
			// For Used1 - Used7, the following applies.
			// A randomly generated number between 0 and 1 should be less than the specified
			// probability benchmark about probability% of the time. In those cases, we transition
			// to the next state. The reward is always 100 - 10*usedNum.
			double probability = 0.1*usedNum;
			System.out.println("Probability: " + probability);
			double randNum = Math.random();
			System.out.println("Randomly generated number: " + randNum);
			double reward = 100 - (10*usedNum);
			if (randNum < probability) {
				// nextState is Used(i+1).
				return new TransitionResult("Used", reward, ++usedNum);
			}
			// Otherwise, next state is Used(i) again.
			return new TransitionResult("Used", reward, usedNum);
		}
		// Only other action is Replace.
		return new TransitionResult("New", -255.0, 0);
		
	}
	
	public double updateMaxUtility(double beta, double[] oldMaxUtilities, double[] newMaxUtilities,
			String[] optimalPolicies) {
		// Iterate through all possible actions that can be taken from the current
		// state and update the maximum utility accordingly.
		double maxUtility = Integer.MIN_VALUE;
		for (int i = 0; i < this.possibleActions.size(); i++) {
			String action = this.possibleActions.get(i);
			if (action.equals("Use")) {
				// When the Use action is taken on any Used State, immediate utility is 
				// calculated with the same formula as reward.
				double immediateUtility = 100 - (10*usedNum);
//				System.out.println("Used State" + usedNum + " immediateUtility: " + immediateUtility);
				double probabilityTransition = 0.1*usedNum; // Probability that next state will be different.
				// If transition occurs, then its maximum possible utility will be found in maxUtilities[usedNum+1]
				double futureUtility = beta*((probabilityTransition)*(oldMaxUtilities[usedNum+1]) 
						+ (1-probabilityTransition)*(oldMaxUtilities[usedNum]));
//				System.out.println("Used State" + usedNum + " futureUtility: " + futureUtility);
				// Compute the sum, as that is the expected utility for taking the "Used" action
				// for any used state. Update maxUtility with the larger value of the two.
				double totalUtility = immediateUtility + futureUtility;
				if (totalUtility > maxUtility)
					optimalPolicies[usedNum] = action;
				maxUtility = Math.max(maxUtility, totalUtility);
			} else {
				// The only other action that could be taken is Replace.
				double immediateUtility = -255;
				// Guaranteed to transition to the New state immediately, whose maximum
				// utility calculated so far is stored in maxUtilities[0].
				double futureUtility = beta*oldMaxUtilities[0];
				double totalUtility = immediateUtility + futureUtility;
				if (totalUtility > maxUtility)
					optimalPolicies[usedNum] = action;
				maxUtility = Math.max(maxUtility, totalUtility);
			}
		}
		// Update maxUtilities[usedNum] with computed new maxUtility.
//		System.out.println("Used State " + usedNum + " Optimal Utility: " + maxUtility + " Optimal Policy: "
//				+ optimalPolicies[usedNum]);
		newMaxUtilities[usedNum] = maxUtility;
		// Return the difference between the new and old utilities for use in convergence.
		return Math.abs(newMaxUtilities[usedNum] - oldMaxUtilities[usedNum]);
	}
	
	public String getStateName() {
		return "Used" + this.usedNum;
	}
	
}
