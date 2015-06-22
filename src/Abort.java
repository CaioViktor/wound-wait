public class Abort extends Operacao{
	
	//Construtor
	public Abort( Transacao transacao ){
		
		setIdentificadorTransacao( transacao.getIdentificador() );
		
		setTransacao( transacao );		
		
	}
		
	//Metodo
	public boolean operar(){
		// System.out.println("Abort " + getTransacao());
		getTransacao().abort();
		return true;
		
	}
	public String toString(){
		String string;
		string = "a" + getIdentificadorTransacao() + "()";
		return string;
	}
	
}
