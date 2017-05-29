package server;

import java.rmi.Remote;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import klient.KlientInterface;

public interface ServerInterface extends Remote{
	public void shtoKlient(KlientInterface klient) throws RemoteException;
    public List<KlientInterface> merrKlientet() throws RemoteException;
}
