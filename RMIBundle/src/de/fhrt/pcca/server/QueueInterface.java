package de.fhrt.pcca.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import de.fhrt.pcca.problem.Node;

public interface QueueInterface extends Remote {

	void add(Node node) throws RemoteException;

	Node get() throws RemoteException;

}