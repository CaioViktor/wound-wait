import java.util.*;
public class Escalonador{
	private int transacaoDaVez;
	private List<Transacao> transacoes;
	private List<Operacao> operacoesEntrada;
	private List<Operacao> operacoesSaida;
	private boolean deadLock;
	private List<Transacao> transacoesAbortadas;
	private List<Transacao> transacoesEfetivadas;

	public Escalonador(List<Transacao> transacoes,List<Operacao> operacoesEntrada){
		this.transacoes = transacoes;
		this.operacoesEntrada = operacoesEntrada;
		deadLock = false;
		operacoesSaida = new ArrayList<>();
		transacoesAbortadas = new ArrayList<>();
		transacoesEfetivadas = new ArrayList<>();
		transacaoDaVez = 0;
	}

	public List<Operacao> getOperacoesEntrada(){
		return operacoesEntrada;
	}

	public List<Operacao> getOperacoesSaida(){
		return  operacoesSaida;
	}

	public List<Transacao> getTransacoesAbortadas(){
		return transacoesAbortadas;
	}

	public List<Transacao> getTransacoesEfetivadas(){
		return  transacoesEfetivadas;
	}

	public boolean getDeadLock(){
		return deadLock;
	}

	private Operacao escolherOperacao(){
		//TODO: Escolher qual operação executar
		Transacao transacaoAtual = transacoes.get(transacaoDaVez);
		// System.out.print(transacaoAtual);
		if(!transacaoAtual.getEstado().equalsIgnoreCase("PROCESSANDO"))//Se a transação não estiver livre para processar ela é pulada
			return null;
		return transacaoAtual.getOperacaoAtual();

	}
	//Método para escalonar
	public List<Operacao> escalonar(){
		//Loop em tadas as transações
		int transacoesConcluidas = 0;
		// int c = 0;
		for(Operacao operacaoAtual:operacoesEntrada){//Tenta executar as operção da lista de entrada
			// System.out.println(operacaoAtual.getTransacao());
			if(operacaoAtual.equals(operacaoAtual.getTransacao().getOperacaoAtual()) && operar(operacaoAtual)){ // Verifica se a operção da lista de entrada é realmente a próxima operação da transação e se ele pode ser feita
				operacoesSaida.add(operacaoAtual);
				operacaoAtual.getTransacao().passarOperacao();
				if(operacaoAtual instanceof Commit){
					transacoesConcluidas++;
					transacoesEfetivadas.add(operacaoAtual.getTransacao());
				}
			}
		}

		while(transacoesConcluidas < transacoes.size() ){//Passa a executar as transações que não puderam concluir
			// c++;
			Operacao operacaoAtual = escolherOperacao();
			// System.out.println("Transação atual: " + transacaoDaVez + " : " + operacaoAtual  + "\tTransações concluidas: " + transacoesConcluidas);
			if(operacaoAtual != null){
				if(operar(operacaoAtual)){
					operacoesSaida.add(operacaoAtual);
					operacaoAtual.getTransacao().passarOperacao();
					if(operacaoAtual instanceof Commit){
						transacoesConcluidas++;
						transacoesEfetivadas.add(operacaoAtual.getTransacao());
					}
				}
			}

			//Faz escolha circular de transações para operarem
			transacaoDaVez++;
			if(transacaoDaVez >= transacoes.size())
				transacaoDaVez = 0;
			//Faz escolha circular de transações para operarem
			// for(Operacao o:operacoesSaida)
			// 	System.out.print(o);
			// System.out.println();
		}
		return operacoesSaida;
	}



	//Dada a operação ele faz a operação do wound-wait
	private boolean operar(Operacao operacaoAtual){
		if(! operacaoAtual.getTransacao().getEstado().equalsIgnoreCase("PROCESSANDO")) //Verifica se a transação está disponível
			return false;
		if(!operacaoAtual.isConflito()){// não há conflito entre operações
			operacaoAtual.operar();
			return true;
		} 
		//há conflitos
		Transacao transacaoAtual = operacaoAtual.getTransacao();
		Transacao transacaoBloqueiaDado = null;
		Dado dadoAcessado = operacaoAtual.getDado();
		if(operacaoAtual instanceof Read){
			transacaoBloqueiaDado = dadoAcessado.getBloqueioEscrita();
			if(transacaoAtual.compareTo(transacaoBloqueiaDado) < 0){//Transação atual é mais antiga
				
				transacaoBloqueiaDado.abort();
				operacoesSaida.add(new Abort(transacaoBloqueiaDado));
				transacaoBloqueiaDado.start(transacaoBloqueiaDado.getTimestamp());//Recomeça transação parada com o mesmo timestamp
				
				operacaoAtual.operar();
				return true;
			}else{
				transacaoAtual.waitFila(dadoAcessado);
				dadoAcessado.addFilaEspera(transacaoAtual);
				return false;
			}
		}//Fim do conflito de Read

		else if(operacaoAtual instanceof Write){

			if(dadoAcessado.isBloqueadoEscrita()){//Dado está com bloqueio exclusivo para escrita

				transacaoBloqueiaDado = dadoAcessado.getBloqueioEscrita();
				if(transacaoAtual.compareTo(transacaoBloqueiaDado) < 0){//Transação atual é mais antiga
					
					transacaoBloqueiaDado.abort();
					operacoesSaida.add(new Abort(transacaoBloqueiaDado));
					transacaoBloqueiaDado.start(transacaoBloqueiaDado.getTimestamp());//Recomeça transação parada com o mesmo timestamp
					
					operacaoAtual.operar();
					return true;
				}else{
					transacaoAtual.waitFila(dadoAcessado);
					dadoAcessado.addFilaEspera(transacaoAtual);
					return false;
				}

			}else{//Dado está com bloqueio compartilhado para leitura
				Set<Transacao> listaTrasacoesBloqueioEscrita = dadoAcessado.getBloqueioLeitura();
				Set<Transacao> transacoesMaisNovas = new HashSet<>();
				for(Transacao t:listaTrasacoesBloqueioEscrita){//Percorre lista de transações que bloquearam o dado e seleciona as mais novas que a transação atual
					if(transacaoAtual.compareTo(t) < 0){//Trasação atual é mais antiga
						transacoesMaisNovas.add(t); //pode dar erro aqui pelo uso do for e não do iterator
					}
				}
				for(Transacao t:transacoesMaisNovas){//Mata transações mais novas
					listaTrasacoesBloqueioEscrita.remove(t);//pode dar erro pelo uso do for
					t.abort();
					operacoesSaida.add(new Abort(t));
					t.start(t.getTimestamp());//Recomeça transação parada com o mesmo timestamp
				}
				if(operacaoAtual.isConflito()){//Verifica se ainda há conflito
					transacaoAtual.waitFila(dadoAcessado);
					dadoAcessado.addFilaEspera(transacaoAtual);
					return false;
				}else{//Não há mais conflito
					operacaoAtual.operar();
					return true;
				}
			}

		}//Fim conflito de Write

		return false;

	}//Fim da operação
}