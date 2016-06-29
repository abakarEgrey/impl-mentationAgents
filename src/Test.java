import java.util.ArrayList;
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
	public static ArrayList<SAMsgBoxHistoryAgent> listSAM = new ArrayList<SAMsgBoxHistoryAgent>(4); 
	
	private static void doSAMstep(){
		for (SAMsgBoxHistoryAgent sam : listSAM){
			sam.nextStep();
		}
	}
	
	public static void main(String[] args) {

		// 2 boutons
		/*
		 * IMutableDirectory<AbstractMessage> iMutableDirectory = new
		 * BasicMutableDirectory<AbstractMessage>(); IMsgBox<AbstractMessage>
		 * buttonMessageBox = new AgentMsgBox<AbstractMessage>("Button",
		 * iMutableDirectory);
		 */
		// Routage routage = new Routage();
		// creation de ImpressJ
		
		
		ImpressInstance impress = new ImpressInstance("ImpressJ", null);
		
		
		HashSet<ServiceAgent> listServiceAgents = new HashSet<ServiceAgent>();
		listServiceAgents.addAll(impress.getServiceAgentList());

		ButtonInstance boutonPred = new ButtonInstance("@Button", null,
				"prevButton", listServiceAgents);
		/*ButtonInstance boutonSuiv = new ButtonInstance("@Button1", null,
				"nextButton", listServiceAgents);*/

		// Ajout dans hashset

		HashSet<IAgentStrategy> hashSet = new HashSet<IAgentStrategy>();
		hashSet.addAll(boutonPred.getServiceAgentList());
		// hashSet.addAll(boutonSuiv.getServiceAgentList());
		// hashSet.addAll(impress.getServiceAgentList());

		
		  /*SequentialSystemStrategyForOppoCompo sssfoc = new
		  SequentialSystemStrategyForOppoCompo(hashSet);
		  sssfoc.doStep();
		  sssfoc.doStep();
		  sssfoc.doStep();
		  sssfoc.doStep();*/
		 
		// boutonPred.getServiceAgentList().get(0).nextStep();
		/*
		 * IAgentStrategy agent = null;
		 * SequentialSystemStrategyForOppoCompo.AgentWrapper agentRapper =
		 * sssfoc.new AgentWrapper(agent);
		 */

		
		// step-1: le boutonPred envoie une annonce aux 2 agents de ImpressJ
				System.out.println("/*==============debut de l'execution du step-1====================*/");
				//doSAMstep();
				boutonPred.getServiceAgentList().get(0).nextStep();
				//execution des agents contextes
				//ArrayList<ContextAgent> contextAgentList = boutonPred.getServiceAgentList().get(0).getContextAgents();
//				if (!contextAgentList.isEmpty()){
//					for (ContextAgent cA : contextAgentList){
//						cA.nextStep();
//					}
//					
//				}
				
				
				
				System.out.println("/*==============execution du step-1 terminé=======================*/");

				// step-2: les agents de ImpressJ repondent à l'annonce precedente et le
				// bouton suivant va faire une annonce
				
				System.out.println("/*===================debut de l'execution du step-2===============*/");
				//doSAMstep();
				for (int i = 0; i < impress.getServiceAgentList().size(); i++) {
					impress.getServiceAgentList().get(i).nextStep();
					
					//execution des agents contextes
					//contextAgentList = impress.getServiceAgentList().get(i).getContextAgents();
//					if (!contextAgentList.isEmpty()){
//						for (ContextAgent cA : contextAgentList){
//							cA.nextStep();
//						}
//						
//					}
				}
				
				
				ButtonInstance boutonSuiv = new ButtonInstance("@Button1", null,
						"nextButton", listServiceAgents);
				boutonSuiv.getServiceAgentList().get(0).nextStep();
				//execution des agents contextes
//				contextAgentList = boutonSuiv.getServiceAgentList().get(0).getContextAgents();
//				if (!contextAgentList.isEmpty()){
//					for (ContextAgent cA : contextAgentList){
//						cA.nextStep();
//					}
//					
//				}
//				
				
				System.out.println("/*===================execution du step-2 terminé===================*/");

				// step-3: le bouton pred traite les reponses recues de la part de la
				// part de ImpressJ (par exemple demande de connxion pour l'une des
				// interfaces de ImpressJ). Le bouton suiv peut recevoir des reponses a
				// son annonce au cycle precedent. Les agents de ImpressJ feront des annonces.
				System.out.println("/*====================debut de l'execution du step-3==============*/");
				//doSAMstep();
				
				boutonPred.getServiceAgentList().get(0).nextStep();
				//execution des agents contextes
//				contextAgentList = boutonPred.getServiceAgentList().get(0).getContextAgents();
//				if (!contextAgentList.isEmpty()){
//					for (ContextAgent cA : contextAgentList){
//						cA.nextStep();
//					}
//					
//				}
				boutonSuiv.getServiceAgentList().get(0).nextStep();
				//execution des agents contextes
//				contextAgentList = boutonSuiv.getServiceAgentList().get(0).getContextAgents();
//				if (!contextAgentList.isEmpty()){
//					for (ContextAgent cA : contextAgentList){
//						cA.nextStep();
//					}
//					
//				}
				for (int i = 0; i < impress.getServiceAgentList().size(); i++) {
					impress.getServiceAgentList().get(i).nextStep();
					
					//execution des agents contextes
//					contextAgentList = impress.getServiceAgentList().get(i).getContextAgents();
//					if (!contextAgentList.isEmpty()){
//						for (ContextAgent cA : contextAgentList){
//							cA.nextStep();
//						}
//						
//					}
				}
				System.out.println("/*====================execution du step-3 terminé================*/");
		//
		
		
	}

}
