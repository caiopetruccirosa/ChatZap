package pacote;

import java.io.Serializable;

public class Pacote implements Serializable, Cloneable 
{
	protected String cmd;
	protected String[] complementos;

	public Pacote(String comando, String[] comp) throws Exception {
		if (comando == null || comp == null)
			throw new Exception("Parametro nulo");
		
		this.cmd = comando;
		this.complementos = comp;
	}

	public String getCmd() {
		return this.cmd;
	}

	public String[] getComplementos() {
		return this.complementos;
	}

	public void setCmd(String novoCmd) throws Exception {
		if (novoCmd == null)
			throw new Exception("Comando nulo");

		this.cmd = novoCmd;
	}	

	public void setComplementos(String[] novoComp) throws Exception {
		if (novoComp == null)
			throw new Exception("Complementos nulos");

		this.complementos = novoComp;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if (obj.getClass() != this.getClass())
			return false;

		Pacote p = (Pacote)obj;

		if (this.cmd != p.cmd)
			return false;

		if (this.complementos.length != p.complementos.length)
			return false;

		for (int i = 0; i < this.complementos.length; i++)
			if (this.complementos[i] != p.complementos[i])
				return false;

		return true;
	}

	public String toString() {
		String ret = "{";
		ret += this.cmd + ",";
		ret += "[";
		for (int i = 0; i < this.complementos.length; i++) {
			ret += this.complementos[i];

			if (i < this.complementos.length-1)
				ret += ",";
		}
		ret += "]";
		ret += "}";

		return ret;
	}

	public int hashCode() {
		int ret = 7;

		ret = ret*7 + this.cmd.hashCode();
		for (int i = 0; i < this.complementos.length; i++) {
			ret = ret*7 + this.complementos[i].hashCode();
		}

		return ret;
	}

	public Pacote(Pacote modelo) throws Exception {
		if (modelo == null)
			throw new Exception("Modelo nulo");

		this.cmd = modelo.cmd;
		this.complementos = modelo.complementos;
	}

	public Object clone() {
		Pacote ret = null;

		try {
			ret = new Pacote(this);
		}
		catch (Exception e) {}

		return ret;
	}
}