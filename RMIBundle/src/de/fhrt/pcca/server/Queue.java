package de.fhrt.pcca.server;

import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.LinkedBlockingDeque;

import de.fhrt.pcca.problem.Node;

public class Queue extends UnicastRemoteObject implements QueueInterface {

	private static LinkedBlockingDeque<Node> queue = new LinkedBlockingDeque<>();
	private Registry reg;

	public Queue() throws RemoteException {
		super();
	}

	public Queue start() {
		try {
			reg = LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		}

		catch (RemoteException ex) {
			System.out.println(ex.getMessage());
		}
		try {
			reg.rebind("Server", new Queue());
		} catch (AccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return this;
	}

	public void stop() {
		try {
			reg.unbind("Server");
			UnicastRemoteObject.unexportObject(this, true);
			System.exit(0);
		} catch (AccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			UnicastRemoteObject.unexportObject(reg, true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void add(Node node) throws RemoteException {
		// TODO Auto-generated method stub
		queue.add(node);

	}

	@Override
	public Node get() throws RemoteException {
		// TODO Auto-generated method stub
		if (queue.size() != 0) {
			return queue.remove();
		}
		return null;
	}
}