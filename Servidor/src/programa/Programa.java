package programa;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import salas.Salas;
import tratadordeconexao.TratadorDeConexao;

public class Programa {
	
    public static void main (String[] args)
    {     
    	class Conexoes extends Thread {
    		private Socket conexao;
    		private Salas salas;
    		
    		public Conexoes(Salas salas, Socket conexao) throws Exception {
    			this.conexao = conexao;
    			this.salas = salas;
    		}

    		public void run() {
    			try {
    				new TratadorDeConexao(this.salas, this.conexao).start();
    			}
    			catch (Exception e) {}
    		}
    	}
    	
    	class Servidor extends Thread {
        	private ServerSocket pedido;
        	private Salas salas;
        	private boolean fim;
        	
        	public Servidor() {
        		try {
        			this.pedido = new ServerSocket(12346);
        			this.fim = false;
        			
    				this.salas = new Salas();
            		
            		this.salas.novaSala("Dinalva");
            		this.salas.novaSala("Claudio");
            		this.salas.novaSala("Lapa");
            		this.salas.novaSala("Disney");
            		this.salas.novaSala("Direção");
            		this.salas.novaSala("Sala da Teresa Helena");
        		}
        		catch (Exception e) {}
        	}
        	
            public void run() {
            	while (!this.fim) {
            		try {
        				Socket conexao = this.pedido.accept();
        				new Conexoes(this.salas, conexao).start();
        			} catch (Exception e) {}
            	}
            }
            
            public void pare() {
            	this.fim = true;
            }
        }
    	
        Servidor server = new Servidor();
        server.start();
        
        System.out.println ("Servidor operante...");
        System.out.println ("De o comando 'shutdown' para derrubar o servidor");
        
        String input = null;
        while (input == null || !input.equals("SHUTDOWN")) {
            Scanner teclado = new Scanner(System.in);
            input = teclado.nextLine().trim().toUpperCase();
        }
        
        server.pare();
        
        System.out.println("Servidor encerrado");
    }
    
}