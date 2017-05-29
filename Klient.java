package klient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTextArea;
//Klasa Klient implementon interfejsin KlientiInterface
public class Klient extends UnicastRemoteObject implements KlientInterface{
	private final JTextArea mesazhi;
    private final String emri;
  
	
	public Klient(String emri,JTextArea mesazhi) throws RemoteException{
	this.mesazhi=mesazhi;
    this.emri=emri;
	}
	@Override  
	//Metoda dergoMesazhin dergon mesazhin me dy parametra userName dhe MesazhiPerDergim
	public void dergoMesazhin(String userName,String mesazhiPerDergim)throws RemoteException{
		String mesazhet=mesazhi.getText();
		mesazhet +=userName+":"+mesazhiPerDergim+"\n";//mesazhet-ruan mesazhet e shkruara nga perdoruesi
		mesazhi.setText(mesazhet);//shfaqet mesazhi se bashku me emrin e perdoruesit qe e ka derguar
	}
	
	@Override
	//Metoda dergoFajllin dergon fajllin me dy parametra emriFajllit
	//dhe nje varg ne byte i te dhenave te fajllit
	public void dergoFajllin(String emriFajllit, byte[] teDhenat)
			throws RemoteException {
		try{
			File direktoriumi=new File("C:\\Users\\Getoar\\teDhenat");//lokacioni ku do te krijohet nje folder

			FileOutputStream kopjimiFajllit=new FileOutputStream(direktoriumi+"\\"+emriFajllit);//lokacioni i fajllit qe do te dergohet
			kopjimiFajllit.write(teDhenat);//shkruhet fajlli ne lokacionin e ri
			kopjimiFajllit.close();			
		    //10.103.48.167
			
		}
		
		catch(IOException ex){
			Logger.getLogger(Klient.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	
	@Override 
   public String getEmri()throws RemoteException{
   return emri;}
}