package klient;

import server.ServerInterface;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BisedaMeTransferFajllashUpdate extends JFrame {
    private String userName;
    private ServerInterface serveri;
    private Klient klienti;

    DefaultListModel onlineUsers = new DefaultListModel();

    JTextArea chatArea;
    JTextField sendTextField;
    JList clientsList;
    JFrame frame;
    JButton attachFileBtn;
    JPanel clients = new JPanel();
    JPanel connectedPanel = new JPanel();

    //konstruktori me dy parametra
    public BisedaMeTransferFajllashUpdate(String userName, ServerInterface serveri) {
        ndertoDritaren();
        this.userName = userName;
        this.serveri = serveri;
        try {
            klienti = new Klient(userName, chatArea);//instance e Klientit
            serveri.shtoKlient(klienti);//serveri shton klientin
            kushEshteOnline();
        } catch (RemoteException ex) {
            Logger.getLogger(BisedaMeTransferFajllashUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Metoda ndertoDritaren nderton dritaren grafike per bised dhe transfer fajllash
    private void ndertoDritaren() {

        frame = new JFrame("Group Chat");
        JPanel main = new JPanel();
        JPanel cn = new JPanel();
        JPanel bottom = new JPanel();
        sendTextField = new JTextField();
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JButton sendBtn = new JButton(" Send ");
        attachFileBtn = new JButton("Attach");
        clientsList = new JList();
        clientsList.setBorder(new EmptyBorder(3, 10, 3, 10));


        connectedPanel.setLayout(new BorderLayout(5, 5));
        main.setLayout(new BorderLayout(5, 5));
        cn.setLayout(new BorderLayout(5, 5));
        bottom.setLayout(new BorderLayout(5, 5));
        cn.add(new JScrollPane(chatArea), BorderLayout.CENTER);
        cn.add(connectedPanel, BorderLayout.EAST);
        bottom.add(sendTextField, BorderLayout.CENTER);
        bottom.add(sendBtn, BorderLayout.EAST);
        main.add(cn, BorderLayout.CENTER);
        main.add(bottom, BorderLayout.SOUTH);
        main.setBorder(new EmptyBorder(10, 10, 10, 10));

         

        clientsList.setModel(onlineUsers);
        clients.setLayout(new BorderLayout(5, 5));
        clients.add(new JLabel("Online:"), BorderLayout.NORTH);
        clients.add(clientsList, BorderLayout.CENTER);

        connectedPanel.add(clients, BorderLayout.CENTER);
        connectedPanel.add(attachFileBtn, BorderLayout.SOUTH);

        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                kushEshteOnline();
                dergo(); //metoda dergo thirret pas shtypjes se butonit Dergo
                sendTextField.setText("");
            }
        });
        sendTextField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {

                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    kushEshteOnline();
                    dergo(); //metoda dergo thirret pas shtypjes se tastit Enter
                    sendTextField.setText("");
                }
            }
        });

        attachFileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                try {
                    bashkangjit(evt);    //metoda bashkangjit thirret pas shtypjes se butonit Bashkangjit
                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });

        frame.setContentPane(main);
        frame.setSize(600, 500);
        frame.setVisible(true);
    }

    //metoda dergo thirret pas shtypjes se butonit dergo per dergim te mesazhit
    private void dergo() {
        if (sendTextField.getText().trim().length() > 0) { //mesazhi duhet te kete gjatesine me te madhe se 0
            try {
                List<KlientInterface> klientet = serveri.merrKlientet(); //merr listen e perdoruesve

                for (KlientInterface KlientetTjere : klientet) {
                    KlientetTjere.dergoMesazhin(userName, sendTextField.getText()); //dergo mesazhin perdoruesve
                }

            } catch (RemoteException ex) {
                JOptionPane.showMessageDialog(this, ex);
                Logger.getLogger(BisedaMeTransferFajllashUpdate.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //Metoda qe thirret kur shtypet butoni online per te pare se kush eshte online
    private void kushEshteOnline() {
        try {
            List<KlientInterface> klientet = serveri.merrKlientet(); //merr listen e perdoruesve
            onlineUsers.clear();
            for (KlientInterface KlientetTjere : klientet) {
                onlineUsers.addElement(KlientetTjere.getEmri());
            }
            clientsList.setModel(onlineUsers);
            clientsList.updateUI();

        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, ex);
            Logger.getLogger(BisedaMeTransferFajllashUpdate.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // metoda qe thirret pas shtypjes se butonit Bashkangjit per bashkangjitje te fajllave
    private void bashkangjit(ActionEvent event) throws IOException {
        //perzhgjidhet fajlli nga nje dritare
        kushEshteOnline();

        JFileChooser zgjedhFajllin = new JFileChooser();
        zgjedhFajllin.showOpenDialog(null);
        File fajlli = zgjedhFajllin.getSelectedFile();
        String emri = fajlli.getName();
        Path lokacioniFajllit = Paths.get(fajlli.getAbsolutePath());
        byte[] teDhenat = Files.readAllBytes(lokacioniFajllit);//fajlli ruhet ne varg te byteve



        List<KlientInterface> klientet = serveri.merrKlientet(); //merr listen e perdoruesve

        for (KlientInterface KlientetTjere : klientet) {
            KlientetTjere.dergoFajllin(emri, teDhenat);//dergon fajllin
            KlientetTjere.dergoMesazhin(userName, " " + fajlli.getName());//shfaqet mesazhi me emrin e fajllit
        }

    }
}