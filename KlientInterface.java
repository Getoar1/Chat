package klient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KlientInterface extends Remote{
	public void dergoFajllin(String emriFajllit, byte[] teDhenat)throws RemoteException;
	public void dergoMesazhin(String pseudonimi, String mesazhi)throws RemoteException;
	public String getEmri ()throws RemoteException;
}
