public class Write extends Operacao{
	
	//Construtor
	public Write( Transacao transacao , Dado dado ){
		
		setTransacao( transacao );

		setIdentificadorTransacao( transacao.getIdentificador() );
		
		setDado( dado );
		
	}
	
	//Metodo
	public String toString(){
		String string;
		string = "w" + getIdentificadorTransacao() + "("+ getDado().getIdentificador() +")";
		return string;
	}
	
	
}