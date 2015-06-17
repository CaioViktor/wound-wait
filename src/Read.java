public class Read extends Operacao{

	//Construtor
	public Read( Transacao transacao , Dado dado ){
		
		setTransacao( transacao );

		setIdentificadorTransacao( transacao.getIdentificador() );
		
		setDado( dado );
		
	}
	
	//Metodo
	public String toString(){
		String string;
		string = "r" + getIdentificadorTransacao() + "("+ getDado().getIdentificador() +")";
		return string;
	}

	public boolean isConflito(){
		return getDado().isBloqueadoEscrita();
	}

	public boolean operar(){
		Dado dado = getDado();
		dado.setBloqueioLeitura(getTransacao());
		return true;
	}
}