
//package agent;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import fr.irit.smac.libs.tooling.scheduling.IAgentStrategy;
import fr.irit.smac.libs.tooling.scheduling.impl.system.AbstractSystemStrategy;
import fr.irit.smac.libs.tooling.scheduling.impl.system.SynchronizedSystemStrategy;

//import simulateur.SimulateurAgent;
//import agent.context.Context;
//import agent.controller.Controller;
//import agent.data.Data;
//import agent.effector.Effector;
//import agent.sensor.Sensor;



public class SequentialSystemStrategyForOppoCompo extends AbstractSystemStrategy<IAgentStrategy>
{

	private static enum AGENT
	{
		CONTEXT, SERVICE, INSTANCE, SIMULATEUR
	};

	private AGENT currentAgent = AGENT.SIMULATEUR;
	private String currentAgentClassName;

	protected class AgentWrapper implements IAgentStrategy
	{

		private final IAgentStrategy agent;

		public AgentWrapper(IAgentStrategy agent)
		{
			this.agent = agent;
		}

		@Override
		public void nextStep()
		{
			if (agent.getClass().getName().equals(currentAgentClassName))
			{
				agent.nextStep();
			}

		}

	}

	private final Map<IAgentStrategy, AgentWrapper> agentWrappers = new ConcurrentHashMap<IAgentStrategy, AgentWrapper>();
	private final BlockingQueue<IAgentStrategy> pendingAddedAgents = new LinkedBlockingDeque<IAgentStrategy>();
	private final BlockingQueue<IAgentStrategy> pendingRemovedAgents = new LinkedBlockingDeque<IAgentStrategy>();

	private final AbstractSystemStrategy<IAgentStrategy> internalSystemStrategy;

	public SequentialSystemStrategyForOppoCompo(Collection<IAgentStrategy> agents, ExecutorService agentExecutor)
	{
		super(agentExecutor);

		internalSystemStrategy = new SynchronizedSystemStrategy(new LinkedHashSet<IAgentStrategy>(), agentExecutor);
		this.addAgents(agents);
	}

	public SequentialSystemStrategyForOppoCompo(HashSet<IAgentStrategy> hashSet)
	{
		// setting a "reasonable" default size for the thread pool of 2xNbCores
		// (according to e.g.
		// http://codeidol.com/java/java-concurrency/Applying-Thread-Pools/Sizing-Thread-Pools/)
		// user should probably override it to a more suitable value
		this(hashSet, Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));

	}

	private void addPendingAgents()
	{

		Collection<IAgentStrategy> drainedAgents = new HashSet<IAgentStrategy>();
		pendingAddedAgents.drainTo(drainedAgents);

		for (IAgentStrategy agent : drainedAgents)
		{
			AgentWrapper wrapper = new AgentWrapper(agent);
			agentWrappers.put(agent, wrapper);
			internalSystemStrategy.addAgent(wrapper);
		}

	}

	private void removePendingAgents()
	{
		Collection<IAgentStrategy> drainedAgents = new HashSet<IAgentStrategy>();
		pendingRemovedAgents.drainTo(drainedAgents);

		for (IAgentStrategy agent : drainedAgents)
		{
			internalSystemStrategy.removeAgent(agentWrappers.get(agent));
			agentWrappers.remove(agent);

		}
	}

	@Override
	protected void doStep()
	{

		pauseLock.lock();

		addPendingAgents();
		try
		{
			internalSystemStrategy.step().get();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
		}

		removePendingAgents();

		pauseLock.unlock();

		/*if(Context.affichage)
		System.out.println("----------- "+currentAgent);*/
		switch (currentAgent)
		{
//		case SIMULATEUR:
//			currentAgentClassName = SimulateurAgent.class.getName();
//			currentAgent = AGENT.SENSOR;
//			break;
//		case SENSOR:
//			currentAgentClassName = Sensor.class.getName();
//			currentAgent = AGENT.EFFECTOR;
//			break;
//		case EFFECTOR:
//			currentAgentClassName = Effector.class.getName();
//			currentAgent = AGENT.DATA;
//			break;
//		case DATA:
//			currentAgentClassName = Data.class.getName();
//			currentAgent = AGENT.CONTEXT;
//			break;
//		case CONTEXT:
//			currentAgentClassName = Context.class.getName();
//			currentAgent = AGENT.CONTROLLER;
//			break;
//		case CONTROLLER:
//			currentAgentClassName = Controller.class.getName();
//			currentAgent = AGENT.SENSOR2;
//			break;
//		case SENSOR2:
//			currentAgentClassName = Sensor.class.getName();
//			currentAgent = AGENT.EFFECTOR2;
//			break;
//		case EFFECTOR2:
//			currentAgentClassName = Effector.class.getName();
//			currentAgent = AGENT.SIMULATEUR;
//			break;
		default:
			throw new RuntimeException("agent not covered");
		}
	}

	@Override
	public void addAgent(final IAgentStrategy agent)
	{
		if (!this.agents.contains(agent))
		{
			super.addAgent(agent);
			pendingAddedAgents.add(agent);
		}

	}

	@Override
	public void removeAgent(IAgentStrategy agent)
	{
		if (this.agents.contains(agent))
		{
			super.removeAgent(agent);
			pendingRemovedAgents.add(agent);

		}
	}

}
