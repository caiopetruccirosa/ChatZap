package forms;

import java.awt.Color;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import clienthandler.ClientHandler;
import pacote.Pacote;
import java.awt.Font;

public class FrmChat extends JFrame {
	
	private JPanel contentPane;
	private JTextField txtMensagem;
	private JButton btnEnviar;
	private JComboBox<String> cbxDest;
	private JLabel lblSala;
	private JLabel lblNick;
	private JLabel lblDest;
	private TextArea txtAreaSala;
	
	private Loader load;
	private ClientHandler clHandler;

	private class Loader extends Thread {
		public void run() {
			for (;;) {
				try {
					Pacote dados = clHandler.receber();
					String msg = null;
					
					if (dados != null) {				
						String[] comp = dados.getComplementos();
						
						switch (dados.getCmd()) {
						case "MUDANCA_ENTROU":
							for (int i = 0; i < comp.length; i++) {
								txtAreaSala.append(comp[i] + " entrou na sala. \n");
								if (!clHandler.getNick().equals(comp[i]))
									cbxDest.addItem(comp[i]);
							}
							break;
						case "MUDANCA_SAIU":
							for (int i = 0; i < comp.length; i++) {
								txtAreaSala.append(comp[i] + " saiu da sala. \n");
								cbxDest.removeItem(comp[i]);
							}
							break;
						case "MENSAGEM_ABERTA":
							msg = comp[0] + ": " + comp[1];
							txtAreaSala.append(msg + "\n");
							break;
						case "MENSAGEM_FECHADA":
							msg = "<Mensagem privada> " + comp[1] + ": " + comp[2];
							txtAreaSala.append(msg + "\n");
							break;
						}
					}
 				} catch (Exception e) {}
			}
		}
	}
	
	/**
	 * Create the frame.
	 */
	public FrmChat(ClientHandler ch) throws Exception {
		if (ch == null)
			throw new Exception("Cliente nulo");
		
		this.clHandler = ch;
		initialize();
		
		this.load = new Loader();
		this.load.start();
	}
	
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				clHandler.desconectar();
			}
		});
		
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 554, 417);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.setBounds(445, 334, 71, 33);
		contentPane.add(btnEnviar);
		
		txtMensagem = new JTextField();
		txtMensagem.setBounds(154, 335, 281, 32);
		txtMensagem.setColumns(10);
		txtMensagem.grabFocus();
		contentPane.add(txtMensagem);
		
		cbxDest = new JComboBox<String>();
		cbxDest.setBounds(32, 334, 112, 33);
		cbxDest.setFocusable(false);
		cbxDest.addItem("TODOS");
		contentPane.add(cbxDest);

		
		lblSala = new JLabel("Sala: " + clHandler.getNomeSala());
		lblSala.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblSala.setBounds(32, 12, 169, 33);
		contentPane.add(lblSala);
		
		lblNick = new JLabel("Nick: " + clHandler.getNick());
		lblNick.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNick.setBounds(319, 12, 197, 33);
		contentPane.add(lblNick);
		
		lblDest = new JLabel("Destinat\u00E1rio:");
		lblDest.setBounds(32, 312, 112, 23);
		contentPane.add(lblDest);
		
		txtAreaSala = new TextArea();
		txtAreaSala.setBackground(new Color(255, 255, 255));
		txtAreaSala.setEditable(false);
		txtAreaSala.setFocusable(false);
		txtAreaSala.setBounds(32, 51, 484, 255);
		contentPane.add(txtAreaSala);
		
		btnEnviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String mensagem = txtMensagem.getText();
				String destinatario = (String)cbxDest.getSelectedItem();
				
				if (!mensagem.trim().equals("")) {			
					try {
						if (!destinatario.equals("TODOS"))
							txtAreaSala.append("<Mensagem privada> " + clHandler.getNick() + ": " + mensagem);
						clHandler.enviarMensagem(mensagem, destinatario);
						txtMensagem.setText("");
						txtMensagem.grabFocus();
					} catch (Exception e) {}
				}
			}
		});
		
		txtMensagem.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnEnviar.doClick();
				}
			}
		});
	}
}
