import java.util.HashSet;

import fr.irit.smac.libs.tooling.avt.range.IMutableRange;
import fr.irit.smac.libs.tooling.messaging.IMsgBox;
import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;
import fr.irit.smac.libs.tooling.messaging.impl.AgentMsgBox;
import fr.irit.smac.libs.tooling.messaging.impl.BasicMutableDirectory;
import fr.irit.smac.libs.tooling.messaging.impl.IMutableDirectory;


public class Test {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		//2 boutons
		/*IMutableDirectory<AbstractMessage> iMutableDirectory =   new BasicMutableDirectory<AbstractMessage>();
		IMsgBox<AbstractMessage> buttonMessageBox = new AgentMsgBox<AbstractMessage>("Button", iMutableDirectory);*/
		//Routage routage = new Routage();
		ButtonInstance boutonPred = new ButtonInstance("@Button", null, "prevButton");
		ButtonInstance boutonSuiv = new ButtonInstance("@Button1", null, "nextButton");
		//creation de ImpressJ
		ImpressInstance impress = new ImpressInstance("ImpressJ", null);
		//Ajout dans hashset
		
		HashSet<IAgentStrategy> hashSet = new HashSet<IAgentStrategy>();
		hashSet.addAll(boutonPred.getServiceAgentList());
		hashSet.addAll(boutonSuiv.getServiceAgentList());
		hashSet.addAll(impress.getServiceAgentList());
		
        SequentialSystemStrategyForOppoCompo sssfoc = new SequentialSystemStrategyForOppoCompo(hashSet);
        //sssfoc.doStep();
        boutonPred.getServiceAgentList().get(0).nextStep();
        /*IAgentStrategy agent = null;
        SequentialSystemStrategyForOppoCompo.AgentWrapper agentRapper = sssfoc.new AgentWrapper(agent);*/
    }
	
	
	
}
