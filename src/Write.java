public class Write extends Operacao{
	
	//Construtor
	public Write( int identificador, Dado dado ){
		
		setIdentificadorTransacao( identificador );
		
		setDado( dado );
		
	}
	
	//Metodo
	public boolean operar(){
		
		return true;
		
	}
	
	
}