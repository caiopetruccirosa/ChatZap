package desordenada;

import java.lang.reflect.*;

public class ListaDesordenada <X> implements Cloneable{

	protected class No {
		protected X  info;
		protected No prox;

		public X getInfo() {
			return this.info;
		}

		public No getProx() {
			return this.prox;
		}

		public void setInfo(X x) {
			this.info = x;
		}


		public void setProx(No n) {
			this.prox = n;
		}

		public No (X x, No n) {
			this.info = x;
			this.prox = n;
		}

		public No (X x) {
			this(x, null);
		}

		public int hashCode() {
			int ret = 44;

			ret = ret*7 + this.info.hashCode();

			if (this.prox != null)
				ret = ret*7 + this.prox.hashCode();

			return ret;
		}

		public boolean equals(Object obj) {
			if (obj == this)
				return true;

			if (obj == null)
				return false;

			if (!this.getClass().equals(obj.getClass()))
				return false;

			No no = (No)obj;

			if (!(this.info).equals(no.info))
				return false;

			if (this.prox == null)
				if (no.prox == null)
					return true;
				else
					return false;

			if (!(this.prox).equals(no.prox))
				return false;

			return true;
		}
	}

	protected No prim;
	protected No ult;

	protected X meuCloneDeX (X x) {
		X ret = null;

		try {
			Class<?> classe = x.getClass();
			Class<?>[] tipoDoParametroFormal = null;
			Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
			Object[] parametroReal = null;

			ret = ((X)metodo.invoke (x, parametroReal));
		}
		catch (NoSuchMethodException erro){}
		catch (InvocationTargetException erro){}
		catch (IllegalAccessException erro){}

		return ret;
    	}

	public void inserirNoInicio(X x) throws Exception {
		if (x == null)
			throw new Exception("Informacao nula");

		X info = null;
		if (x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		this.prim = new No(info, this.prim);
		if (this.prim.getProx() == null)
			this.ult = this.prim;
	}

	public void inserirNoFim(X x) throws Exception {
		if (x == null)
			throw new Exception("Informacao nula");

		X info= null;
		if (x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		if (this.prim == null) {
			this.prim = new No(info, null);
			this.ult = this.prim;
		} else {
			this.ult.setProx(new No(info, null));
			this.ult = this.ult.getProx();
		}
	}

	public String toString() {
		String ret = "{";
		No atual = this.prim;

		while (atual != null) {
			ret += atual.getInfo();

			if (atual.getProx() != null)
				ret += ",";

			atual = atual.getProx();
		}

		ret += "}";

		return ret;
	}

	public int hashCode() {
		int ret = 44;

		ret = ret*7 + prim.hashCode();

		return ret;
	}

	public boolean equals (Object obj) {
		if (obj == this)
			return true;

		if (obj == null)
			return false;

		if (!this.getClass().equals(obj.getClass()))
			return false;

		ListaDesordenada<X> lis = (ListaDesordenada<X>)obj;

		if (!((this.prim).equals(lis.prim)))
			return false;

		return true;
	}

	public Object clone () {
		ListaDesordenada<X> ret = null;
		try {
			ret = new ListaDesordenada<X>(this);
		}catch(Exception erro) {}

		return ret;
	}

	public ListaDesordenada(ListaDesordenada<X> m) throws Exception {
		if (m == null)
			throw new Exception("Modelo ausente");

		this.prim = null;
		if (m.prim != null) {
			this.prim = new No(m.prim.getInfo());

			No atual = m.prim.getProx();
			No aux   = this.prim;

			while (atual != null) {
				aux.setProx(new No(atual.getInfo()));

				if (atual.getProx() == null)
					this.ult = atual;
				
				atual = atual.getProx();
				aux   = aux.getProx();
			}
		}
	}

	public ListaDesordenada() 
	{
		this.prim = null;
		this.ult = null;
	}

	public X jogueForaPrimeiro() throws Exception {
		if(this.prim == null)
			throw new Exception("Lista vazia");

		X ret = this.prim.getInfo();

		this.prim = this.prim.getProx();
		if (this.prim == null)
			this.ult = null;

		return ret;
	}

	public X jogueForaUltimo() throws Exception {
		if(this.prim == null)
			throw new Exception("Lista vazia");

		X ret = this.ult.getInfo();
		
		if (this.prim.getProx() == null) {
			this.prim = null;
			this.ult = null;
		} else {
			No atual = this.prim;
			
			while (atual.getProx().getProx() != null)
				atual = atual.getProx();
			
			atual.setProx(null);
			this.ult = atual;
		}

		return ret;
	}

	public void excluir(X x) throws Exception {
		if (x == null)
			throw new Exception("Informacao nula");
		
		No atual = this.prim;

		if (this.prim.getInfo().equals(x))
			this.prim = this.prim.getProx();
		if (this.prim.getProx() == null)
			this.ult = this.prim;
		
		while (atual != null) {
			if (atual.getProx().getInfo().equals(x))
			{
				if (atual.getProx() == this.ult)
					this.ult = atual;
				
				atual.setProx(atual.getProx().getProx());
				return;
			}

			atual = atual.getProx();
		}

		throw new Exception("Informacao inexistente");
	}
}