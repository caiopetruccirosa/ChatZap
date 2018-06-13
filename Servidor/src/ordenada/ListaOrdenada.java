package ordenada;

import java.lang.reflect.*;

public class ListaOrdenada <X extends Comparable<X>> implements Cloneable
{
	protected class No implements Cloneable
	{
		protected X info;
		protected No prox;
        protected No ant;

		public No (No a, X x, No n){
            this.ant  = a;
			this.info = x;
			this.prox = n;
		}

		public No (X x) {
			this (null, x, null);
		}

		public X getInfo () {
			return info;
		}

		public No getProx () {
			return prox;
		}
        
        public No getAnt () {
            return ant;
        }

		public void setInfo (X x) {
			info = x;
		}

		public void setProx (No no) {
			prox = no;
		}
        
        public void setAnt (No no){
            ant = no;
        }

        public boolean equals(Object obj) {
        	if (obj == null)
        		return false;

        	if (this == obj)
        		return true;

        	if (this.getClass() != obj.getClass())
        		return false;

        	No no = (No)obj;

        	if (!this.info.equals(no.info))
        		return false;

        	if (!this.prox.equals(no.prox))
        		return false;

        	return true;
        }
	}

	protected No prim;
    protected No ult;
    protected int qtd;

	public ListaOrdenada ()
	{
		this.prim = null;
        this.ult = null; 
        this.qtd = 0;
	}
    
    public ListaOrdenada (ListaOrdenada<X> l) throws Exception
    {
        if (l == null)
            throw new Exception ("Lista Nula");
            
        this.qtd = l.qtd;
        No aux = l.prim;
        if (aux != null)
        {
            this.prim = new No (aux.getInfo());            
            
            aux = aux.getProx();
            No atu = this.prim;
            while (aux != null)
            {                
               atu.setProx(new No(atu, aux.getInfo(), null));
               atu = atu.getProx();
               aux = aux.getProx();                          
            }
            this.ult = atu;
        }
        else
        {
            this.prim = null;
            this.ult = null;
        }
              
    }    

    public void inserir(X x) throws Exception
    {
    	if (x == null)
			throw new Exception("Informacao ausente");

		X info = null;
		if (x instanceof Cloneable)
			info = meuCloneDeX(x);
		else
			info = x;

		if (this.prim == null) {
			this.prim = new No(info);
			this.ult = this.prim;
		}
		else
		{
			No atual = this.prim;
			while(atual.getInfo().compareTo(info) < 0 && atual.getProx() != null)
				atual = atual.getProx();

			atual.setProx(new No(atual.getAnt(), info, atual.getProx()));
			
			if (atual.getAnt() != null)
				atual.getAnt().setProx(atual.getProx());
			
			if (atual.getProx() != null)
				atual.getProx().setAnt(atual.getAnt());
			
			if (this.ult.getProx() != null)
				this.ult = this.ult.getProx();
		}
		
		this.qtd++;   	    
    }
    
    public X jogueForaPrimeiro() throws Exception
    {
        if (this.prim == null)
            throw new Exception ("Lista Vazia");
        
        X ret = this.prim.getInfo();

        this.prim = this.prim.getProx();
        this.prim.setAnt(null);
        this.qtd--;

        return ret;
    }
    
    public X jogueForaUltimo() throws Exception
    {
        if (this.prim == null)
            throw new Exception ("Lista Vazia");
            
        X ret = this.ult.getInfo();

        if (this.ult.getAnt() != null)
        {
            this.ult = ult.getAnt();
            this.ult.setProx(null);
        }
        else // So tem um item;
        {
            this.ult = null;
            this.prim = null;
        }
        this.qtd--;

        return ret;
    }

    public void excluir(X x) throws Exception {
    	if (x == null)
    		throw new Exception("Informacao nula");

    	if (this.prim == null)
    		throw new Exception("Lista vazia");

    	No atual = this.prim;
    	while (!atual.getInfo().equals(x) && atual.getProx() != null)
    		atual = atual.getProx();

    	if (atual.getInfo().equals(x)) {
    		if (atual == this.prim) {
    			if (atual.getProx() == null) {
    				this.prim = null;
    				this.ult = null;
    			} else {
    				this.prim = this.prim.getProx();
    				this.prim.setAnt(null);
    			}
    		} else if (atual == this.ult) {
    			if (atual.getAnt() == null) {
    				this.prim = null;
    				this.ult = null;
    			} else {
    				this.ult = this.ult.getAnt();
    				this.ult.setProx(null);
    			}
    		} else {
    			atual.getProx().setAnt(atual.getAnt());
    			atual.getAnt().setProx(atual.getProx());
    		}
    		this.qtd--;
    	}
    }

    public ListaOrdenada<X> menos(ListaOrdenada<X> l) throws Exception {
    	if (l == null)
    		throw new Exception("Lista nula");

    	if (l.prim == null)
    		return (ListaOrdenada<X>)this.clone();

    	if (this.prim == null)
    		return new ListaOrdenada<X>();

    	ListaOrdenada<X> ret = (ListaOrdenada<X>)this.clone();
		ListaOrdenada<X> lista = (ListaOrdenada<X>)l.clone();

    	No a = ret.prim;
    	No b = lista.prim;

    	while (a != null && b != null) {
			int comparacao = a.getInfo().compareTo(b.getInfo());
			
			if (comparacao == 0) {
				if (a == ret.prim && a.getProx() == null) {
					ret.prim = null;
					ret.ult = null;
					a = null;
				} else {
					if (a == ret.prim) {
						ret.prim = a.getProx();
						a.getProx().setAnt(null);
					}
					else if (a == ret.ult) {
						ret.ult = a.getAnt();
						a.getAnt().setProx(null);
					} 
					else {
						a.getAnt().setProx(a.getProx());
						a.getProx().setAnt(a.getAnt());
					}
					
					a = a.getProx();
					b = b.getProx();
				}
				ret.qtd--;
			} 
			else if (comparacao < 0)
				a = a.getProx();
			else if (comparacao > 0)
				b = b.getProx();
		}

    	return ret;
    }

    public String[] toStringArray() {
		if (this.prim == null)
			return new String[0];
		
		String[] ret = new String[this.qtd];
		
		No aux = this.prim;
		for (int i = 0; i < ret.length; i++) {
			ret[i] = aux.getInfo().toString();
			aux = aux.getProx();
		}
		
		return ret;
	}


	public X meuCloneDeX(X x)
	{
		X ret = null;

        try
        {
            Class<?> classe = x.getClass();
            Class<?>[] tipoDoParametroFormal = null;
            Method metodo = classe.getMethod ("clone", tipoDoParametroFormal);
            Object[] parametroReal = null;
            ret = ((X)metodo.invoke (x, parametroReal));
        }
        catch (NoSuchMethodException erro)
        {}
        catch (InvocationTargetException erro)
        {}
        catch (IllegalAccessException erro)
        {}

        return ret;
	}	

	public String toString()
	{
		String ret = "{";

		No atual = this.prim;
		while (atual != null)
		{
			ret += atual.getInfo();
			if (atual.getProx() != null)
				ret += ", ";				

			atual = atual.getProx();
		}

		ret += "}";

		return ret;
	}

	public boolean equals(Object obj)
	{
		if (obj == null)
			return false;

		if (obj == this)
			return true;

		if(!this.getClass().equals(obj.getClass()))
			return false;

		ListaOrdenada<X> l = (ListaOrdenada<X>)obj;		

		if (this.prim == null && l.prim != null)
			return false;

		if (!this.prim.equals(l.prim))
			return false;

		return true;
	}

	public int hashCode()
	{
		int ret = 3;

		ret = ret * 7 + this.prim.hashCode();

		return ret;
	}

	public Object clone()
	{
		ListaOrdenada<X> ret = null;
		try
		{
			ret = new ListaOrdenada<X>(this);
		}
		catch (Exception e) {}

		return ret;
	}

}
