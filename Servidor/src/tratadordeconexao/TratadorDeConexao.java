package tratadordeconexao;

import java.net.Socket;

import desordenada.ListaDesordenada;
import ordenada.ListaOrdenada;
import pacote.Pacote;
import sala.Sala;
import salas.Salas;
import usuario.Usuario;

public class TratadorDeConexao extends Thread
{
    protected Sala sala;
    protected ListaOrdenada<String> nicks;
    protected Usuario usuario;
    protected boolean fim;
    
    public TratadorDeConexao(Salas salas, Socket conexao) throws Exception
    {
    	if (salas == null || conexao == null)
    		throw new Exception("Parametro nulo");
    	
		try {
			this.usuario = new Usuario((Salas)salas.clone(), conexao);
			this.nicks = new ListaOrdenada<String>();
			
			String nomeSala = this.usuario.getNomeSala();
			this.sala = salas.getSala(nomeSala);
			this.sala.entra(this.usuario);
			this.fim = false;
		}
		catch (Exception e) {}
    }

    public void run()
    {
        while (!this.fim)
        {
        	try {
    			ListaOrdenada<String> nicksAtuais = this.sala.getNicks();
				ListaOrdenada<String> lisEntrou = nicksAtuais.menos(this.nicks);
				ListaOrdenada<String> lisSaiu   = this.nicks.menos(nicksAtuais);
				
				this.nicks = (ListaOrdenada<String>)nicksAtuais.clone();
				   
				Pacote entrou = null;
				Pacote saiu = null;
				entrou = new Pacote("MUDANCA_ENTROU", lisEntrou.toStringArray());
				saiu = new Pacote("MUDANCA_SAIU", lisSaiu.toStringArray());
				this.usuario.envia(entrou);
				this.usuario.envia(saiu);
				
				Pacote dados = this.usuario.recebeTemporario();
				if (dados != null) {
					switch (dados.getCmd()) {
						case "SAI":
							this.pare();
							break;
						case "MENSAGEM_ABERTA":
							this.sala.enviarMensagemAberta(dados);
							break;
						case "MENSAGEM_FECHADA":
							this.sala.enviarMensagemFechada(dados);
							break;
					}
				}
	        } catch (Exception e) {}
        }
    }

    public void pare()
    {
		try {
			this.fim = true;
			this.usuario.desconectar();
			this.sala.remover(this.usuario);
		}
		catch (Exception e) {}
    }

    public boolean equals(Object obj) {
    	if (obj == null)
    		return false;

    	if (obj == this)
    		return true;

    	if (obj.getClass() != this.getClass())
    		return false;

    	TratadorDeConexao tdc = (TratadorDeConexao)obj;

    	if (!this.sala.equals(tdc.sala))
    		return false;

    	if (!this.nicks.equals(tdc.nicks))
    		return false;

    	if (!this.usuario.equals(tdc.usuario))
    		return false;

    	if (this.fim != tdc.fim)
    		return false;

    	return true;
    }

    public int hashCode() {
		int ret = 7;

		ret = ret*7 + this.sala.hashCode();
		ret = ret*7 + this.nicks.hashCode();
		ret = ret*7 + this.usuario.hashCode();
		ret = ret*7 + new Boolean(this.fim).hashCode();

    	return ret;
    }

    public String toString() {
    	String ret = "[";
    	ret += this.sala + ",";
    	ret += this.nicks + ",";
    	ret += this.usuario + ",";
    	ret += this.fim;
    	ret += "]";

    	return ret;
    }

    public TratadorDeConexao(TratadorDeConexao m) throws Exception {
    	if (m == null)
    		throw new Exception("Modelo inexistente");

    	this.sala = (Sala)m.sala.clone();
    	this.nicks = (ListaOrdenada<String>)m.nicks.clone();
    	this.usuario = (Usuario)m.usuario.clone();
    	this.fim = m.fim;
    }

    public Object clone() {
    	TratadorDeConexao ret = null;

    	try {
    		ret = new TratadorDeConexao(this);
    	}
    	catch (Exception e) {}

    	return ret;
    }
}