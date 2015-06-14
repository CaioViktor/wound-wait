public class Commit extends Operacao{

	//Construtor
	public Commit( Transacao transacao ){
		
		setIdentificadorTransacao( transacao.getIdentificador() );
		
		setTransacao( transacao );		
	
	}
		
	//Metodo
	public boolean operar(){
		getTransacao().commit();
		return true;
		
	}
	public String toString(){
		String string;
		string = "c" + getIdentificadorTransacao() + "()";
		return string;
	}
	
}