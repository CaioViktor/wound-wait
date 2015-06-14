public class Abort extends Operacao{

	//Atributos
	private Transacao transacao;
	
	//Construtor
	public Abort( int identificador, Transacao transacao ){
		
		setIdentificadorTransacao( identificador );
		
		this.transacao = transacao;		
		
	}
		
	//Metodo
	public boolean operar(){
		
		return true;
		
	}
	
}
