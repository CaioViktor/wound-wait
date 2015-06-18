import java.util.*;
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
	
	public boolean isConflito(){
		Dado dado = getDado();
		if(!dado.isBloqueadoEscrita() && !dado.isBloqueadoLeitura())
			return false;
		boolean conflito = false;
		if(dado.isBloqueadoEscrita() && !dado.getBloqueioEscrita().equals(this))
			conflito = true;
		if(dado.isBloqueadoLeitura()){
			Set<Transacao> bloqueios = dado.getBloqueioLeitura();
			if(bloqueios.size() == 1 && bloqueios.contains(getTransacao()))
				conflito = conflito || false;
			else
				conflito = true;
		}
		return conflito;
	}
	public boolean operar(){
		
		Dado dado = getDado();
		dado.setBloqueioEscrita(getTransacao());
		return true;
	}
}