public class Commit extends Operacao{

	//Atributo
	private Transacao transacao;

	//Construtor
	public Commit( int identificador, Transacao transacao ){
		
		setIdentificadorTransacao( identificador );
		
		this.transacao = transacao;		
	
	}
		
	//Metodo
	public boolean operar(){
		
		return true;
		
	}
	
}