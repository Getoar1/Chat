package server;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;


import klient.BisedaMeTransferFajllashUpdate;

public class LogIn extends JFrame{
	 
	    
	    private JLabel label1;
	    private JLabel label2;
	    private JLabel label3;
	    private JTextField Hosti;//fusha per shenimin e hostit apo IP
	    private JTextField user;//fusha per shenimin e emrit te perdoruesit
	    private JTextField Porti;//fusha per shenimin e portit
	    private JButton aktivizoServerin;//butoni per aktivizim te serverit
	    private JButton Kycu;//butoni per kycje ne bised
	    
 // konstruktori pa parametra
 public LogIn() { 
 	ndertoDritaren();  
 }
//Metoda ndertoDritaren nderton dritaren grafike per aktivizim te serverit dhe per kycje
 private void ndertoDritaren() {

     label1 = new JLabel(); 
     label2 = new JLabel(); 
     Hosti = new JTextField();
     Porti = new JTextField(); 
     aktivizoServerin = new JButton();
     Kycu = new JButton(); 
     user = new JTextField(); 
     label3 = new JLabel(); 

     setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

     label1.setText("IP:"); 

     label2.setText("Port:"); 
     
     label3.setText("Emri i perdoruesit");

     Porti.setText("10005"); 
     Porti.addActionListener(new java.awt.event.ActionListener() { 
         public void actionPerformed(java.awt.event.ActionEvent evt) { 
             fillimi(evt); 
         }
     });

     aktivizoServerin.setText("Aktivizo serverin"); 
     aktivizoServerin.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
             leshoServerin(evt); //metoda lesho serverin thirret pas shtypjes se butonit Aktivizo Serverin
         }
     });

     Kycu.setText("Kycu"); 
     Kycu.addActionListener(new java.awt.event.ActionListener() {
         public void actionPerformed(java.awt.event.ActionEvent evt) {
             mundesoKycjen(evt); //metoda lidhu thirret pas shtypjes se butonit Kycu
         }
     });

      

     //paraqiten fushat dhe butonat ne dritaren grafike
     Container cp=getContentPane();
     cp.setLayout(new GridLayout(0,1));
     cp.add(label1);
     cp.add(Hosti);
     cp.add(label3);
     cp.add(user);
     cp.add(label2);
     cp.add(Porti);
     cp.add(Kycu);
     cp.add(aktivizoServerin);
     

     pack();
     setLocationRelativeTo(null); 
 }
//metoda lesho serverin thirret pas shtypjes se butonit Aktivizo Serverin
 private void leshoServerin(java.awt.event.ActionEvent evt) {
     Registry reg; 
     try {
    	 //lidhja me server
         reg = LocateRegistry.createRegistry(10005); 
         reg.rebind("lidhja", new Server()); //lidhet me klasen Server
         aktivizoServerin.setEnabled(false); 
         Kycu.setEnabled(true); 
         JOptionPane.showMessageDialog(null, "Serveri eshte leshuar me sukses");
     } catch (RemoteException ex) { 
         JOptionPane.showMessageDialog(null, ex, "Error!", JOptionPane.ERROR_MESSAGE);
         Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
     }
 }

//metoda lidhu thirret pas shtypjes se butonit Kycu
 private void mundesoKycjen(java.awt.event.ActionEvent evt) {
     krijoKycjen();//metoda qe mundeson kycjen
 }

 private void fillimi(java.awt.event.ActionEvent evt) { 
 }
  private void krijoKycjen() { 
      try {
          String pseudonimi = user.getText(); 
          if (pseudonimi.trim().length() == 0) { 
              JOptionPane.showMessageDialog(this, "Shkruani nje pseudonim");
              return; 
          }
          //lidhja me server
          Registry regjistri = LocateRegistry.getRegistry(Hosti.getText(), Integer.parseInt(Porti.getText()));
          ServerInterface serveri = (ServerInterface) regjistri.lookup("lidhja");
         
          if (serveri == null) {
              JOptionPane.showMessageDialog(this, "Nuk mund te realizohet lidhja ne server"); 
              return;
          }
          
          setVisible(false); 
          new BisedaMeTransferFajllashUpdate(pseudonimi,serveri).setVisible(false);
      } catch (NotBoundException ex) {
          JOptionPane.showMessageDialog(null, ex, "Error!", JOptionPane.ERROR_MESSAGE);
          Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
      } catch (RemoteException ex) {
          JOptionPane.showMessageDialog(null, ex, "Error!", JOptionPane.ERROR_MESSAGE);
          Logger.getLogger(LogIn.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  
 
  public static void main(String args[]) throws RemoteException, AlreadyBoundException {

      java.awt.EventQueue.invokeLater(new Runnable() {
          public void run() { 
              new LogIn().setVisible(true); 
          }
      });
  }
}
