package forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import clienthandler.ClientHandler;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.SwingConstants;

public class FrmSettings extends JFrame {

	/**
	 * 
	 */
	private FrmChat frmChat;
	private JPanel contentPane;
	private JTextField txtIP;
	private JTextField txtNick;

	private ClientHandler clHandler;

	/**
	 * Create the frame.
	 */
	public FrmSettings() {
		initialize();
	}
	
	public void initialize() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 461, 290);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel plConectar = new JPanel();
		plConectar.setBounds(10, 10, 437, 240);
		contentPane.add(plConectar);
		plConectar.setLayout(null);
		plConectar.setVisible(true);
		
		JPanel plEntrarSala = new JPanel();
		plEntrarSala.setBounds(10, 10, 437, 240);
		contentPane.add(plEntrarSala);
		plEntrarSala.setLayout(null);
		plEntrarSala.setVisible(false);
		
		txtIP = new JTextField();
		txtIP.setBounds(75, 83, 279, 31);
		plConectar.add(txtIP);
		txtIP.setColumns(10);
		
		JButton btnConectar = new JButton("Conectar");
		btnConectar.setBounds(75, 125, 279, 31);
		plConectar.add(btnConectar);
		
		JLabel lblIP = new JLabel("Digite o IP do servidor:");
		lblIP.setBounds(75, 47, 151, 31);
		plConectar.add(lblIP);
		
		txtNick = new JTextField();
		txtNick.setBounds(161, 39, 214, 29);
		plEntrarSala.add(txtNick);
		txtNick.setColumns(10);
		
		JButton btnEntrar = new JButton("Entrar na Sala");
		btnEntrar.setBounds(64, 149, 311, 41);
		plEntrarSala.add(btnEntrar);
		
		JLabel lblNick = new JLabel("Escolha seu nick:");
		lblNick.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNick.setBounds(34, 43, 112, 20);
		plEntrarSala.add(lblNick);
		
		JComboBox<String> cbxSalas = new JComboBox<String>();
		cbxSalas.setBounds(161, 92, 214, 29);
		plEntrarSala.add(cbxSalas);
		
		JLabel lblSalas = new JLabel("Escolha a sala:");
		lblSalas.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSalas.setBounds(34, 96, 112, 20);
		plEntrarSala.add(lblSalas);
		
		btnConectar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!txtIP.getText().trim().equals("") && txtIP.getText() != null) {
					try {
						Socket conn = new Socket(txtIP.getText().trim(), 12346);
						
						clHandler = new ClientHandler(conn);
						String [] salas = clHandler.getSalas();
						for (int i = 0; i < salas.length; i++)
						{
							cbxSalas.addItem(salas[i]);
						}
						
						plConectar.setVisible(false);
						plEntrarSala.setVisible(true);
						txtNick.grabFocus();
					}  catch (Exception e) {
						txtIP.setText("");
						txtIP.grabFocus();
						JOptionPane.showMessageDialog(null,"Servidor indisponível!");
					}
				} else
					txtIP.grabFocus();
			}
		});
		
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nickEscolhido = txtNick.getText().trim();
				String salaEscolhida = cbxSalas.getSelectedItem().toString().trim();
				
				if (!nickEscolhido.equals("") && !salaEscolhida.equals("")) {
					try {
						clHandler.entrar(nickEscolhido, salaEscolhida);
						
						frmChat = new FrmChat(clHandler);
						frmChat.setVisible(true);
						setVisible(false);
					} catch (Exception ex) {
						txtNick.setText("");
						txtNick.grabFocus();
						JOptionPane.showMessageDialog(null,ex.getMessage());
					}
				} else
					txtNick.grabFocus();
			}
		});
		
		txtIP.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnConectar.doClick();
				}
			}
		});
		
		txtNick.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnEntrar.doClick();
				}
			}
		});
		
		cbxSalas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					btnEntrar.doClick();
				}
			}
		});
	}
}
