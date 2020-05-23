
public class TransitionResult {
	private State nextState;
	private double reward;
	
	public TransitionResult(String nextState, double reward, int usedNum) {
		switch (nextState) {
		case "New":
			this.nextState = new NewState();
			this.reward = reward;
			break;
		case "Used":
			this.nextState = new UsedState(usedNum);
			this.reward = reward;
			break;
		case "Dead":
			this.nextState = new DeadState();
			this.reward = reward;
			break;
		default:
			this.nextState = null;
			this.reward = 0;
			System.out.println("Tried to create TransitionResult with an invalid state.");
			
		}
	}
	
	public State getNextState() {
		return this.nextState;
	}
	
	public double getReward() {
		return this.reward;
	}

}
