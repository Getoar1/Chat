package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import klient.KlientInterface;

public class Server extends UnicastRemoteObject implements ServerInterface{

	List<KlientInterface> klientet=new ArrayList<>();
	
	public Server() throws RemoteException
	{}
	@Override
	public void shtoKlient(KlientInterface klient) throws RemoteException {
		klientet.add(klient);
	}
	@Override
	public List<KlientInterface> merrKlientet() throws RemoteException {
		return klientet;
	}
}
