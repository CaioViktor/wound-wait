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
	
	public boolean operar(){
		if(!super.operar())
			return false;
		if(!getDado().isBloqueadoLeitura())
			return true;
		//TODO: Verificar lista se algum timestamp é maior para matá-los
		return true;
	}
}