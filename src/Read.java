public class Read extends Operacao{

	//Construtor
	public Read( int identificador, Dado dado ){
		
		setIdentificadorTransacao( identificador );
		
		setDado( dado );
		
	}
	
	//Metodo
	public boolean operar(){
		
		return true;
		
	}
	
}