package clienthandler;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import pacote.Pacote;

public class ClientHandler {
	protected Socket conexao;
	protected ObjectInputStream receptor;
	protected ObjectOutputStream transmissor;
	
	protected String nick;
	protected String sala;
	
	public ClientHandler(Socket conn) throws Exception {
		if (conn == null)
			throw new Exception("Conexao nula");
		
		this.conexao = conn;
    	this.transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
    	this.receptor = new ObjectInputStream(this.conexao.getInputStream());
	}
	
	public String[] getSalas() {
		String[] salas = null;
		
		Pacote dados = null;
		while (dados == null) {
			dados = this.receber();
			if (dados != null)
				if (!dados.getCmd().equals("LISTA_SALAS"))
					dados = null;
		}
		salas = dados.getComplementos();
		
		return salas;
	}
	
	public void entrar(String nick, String sala) throws Exception {
		if (nick == null || nick.trim() == "")
			throw new Exception("Nick vazia");
		
		if (sala == null || sala.trim() == "")
			throw new Exception("Nome da sala vazio");
		
		String[] dados = new String[2];
		dados[0] = nick.trim();
		dados[1] = sala.trim();
		
		Pacote entrar = new Pacote("ESCOLHER_SALA", dados);
		this.enviar(entrar);
		
		Pacote confirmacao = null;
		while (confirmacao == null)
			confirmacao = this.receber();
		
		switch (confirmacao.getCmd()) {
		case "CONFIRMACAO_ENTROU":
			this.nick = nick.trim();
			this.sala = sala.trim();
			break;
		case "SALA_INVALIDA":
			throw new Exception("Sala inválida!");
		case "NOME_JA_USADO":
			throw new Exception("Nome já usado!");
		case "NOME_INVALIDO":
			throw new Exception("Nome inválido!");
		default:
			throw new Exception("Ocorreu algum erro!");
		}
	}
	
	public void enviarMensagem(String mensagem, String destinatario) throws Exception {
		if (mensagem == null || mensagem.trim() == "")
			throw new Exception("Mensagem vazia");
		
		if (destinatario == null || destinatario.trim() == "")
			throw new Exception("Destinatario vazia");
		
		String[] comp = null;
		Pacote msg = null;
		if (destinatario.equals("TODOS")) {
			comp = new String[2];
			comp[0] = this.nick.trim();
			comp[1] = mensagem.trim();
			
			msg = new Pacote("MENSAGEM_ABERTA", comp);
		} else {
			comp = new String[3];
			
			comp[0] = destinatario.trim();
			comp[1] = this.nick.trim();
			comp[2] = mensagem.trim();
			
			msg = new Pacote("MENSAGEM_FECHADA", comp);
		}
		this.enviar(msg);
	}
	
	public void desconectar() {
		if (this.conexao.isConnected() && this.conexao != null) {
			try {
				Pacote saida = new Pacote("SAI", new String[0]);
				this.enviar(saida);
				
				this.conexao.close();
				this.receptor.close();
				this.transmissor.close();
			} catch (Exception e) {}
		}

		this.conexao = null;
		this.receptor = null;
		this.transmissor = null;
	}
	
	public String getNick() {
		return this.nick;
	}
	
	public String getNomeSala() {
		return this.sala;
	}
	
	public void enviar(Pacote p) {
		if (this.conexao.isConnected() && this.conexao != null) {
			try {
				this.transmissor.writeObject(p);
				this.transmissor.flush();
			} catch (Exception e) {}
		}
	}
	
	public Pacote receber() {
		Pacote dados = null;
		
		if (this.conexao.isConnected() && this.conexao != null) {
			try {
				dados = (Pacote)this.receptor.readObject();
			} catch (Exception e) {}
		}
		
		return dados;
	}
}
