import java.util.List;
import java.util.ArrayList;
public abstract class State {
	protected List<String> possibleActions;
	
	public State() {
		this.possibleActions = new ArrayList<String>();
	}
	
	public abstract TransitionResult takeAction(String action);
	public abstract String getStateName();
	public abstract double updateMaxUtility(double beta, double[] oldMaxUtilities, double[] newMaxUtilities,
			String[] optimalPolicies);
	
	public List<String> getPossibleActions() {
		return this.possibleActions;
	}
	
	
	
}
