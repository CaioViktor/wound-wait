import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
public class WoundWait{
	
	private static HashMap<String,Dado> dados;
	private static ArrayList<Transacao> transacoes;
	private static List<Operacao> operacoesEntrada;
	private static Escalonador escalonador;
	private static FileReader inFile;
	private static FileWriter outFile;
	private static BufferedReader bufferIn;
	private static BufferedWriter bufferOut;

	public static void main(String args[]){
		
		dados = new HashMap<>();
		transacoes = new ArrayList<>();
		operacoesEntrada = new ArrayList<>();
		if(args.length == 2){
			try{
				inFile = new FileReader(args[0]);
				outFile = new FileWriter(args[1]);
				
				bufferIn = new BufferedReader(inFile);
				bufferOut = new BufferedWriter(outFile);


				lerEntrada();
				// testar();
				entrarListaEntrada();
				escalonador = new Escalonador(transacoes,operacoesEntrada);
				// System.out.println("Entrada: " + operacoesEntrada);
				gravarSaida();

				bufferIn.close();
				inFile.close();
				System.out.println();
			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			System.out.println("Erro na passagem de Argumentos:\n Devem haver 2 argumentos.\nEx.:\njava WoundWait ArquivoEntrada.txt ArquivoSaida.txt");
		}
	}


	public static void entrarListaEntrada(){
		int transacoesConcluidas = 0;
		Scanner in = new Scanner(System.in);
		while(transacoesConcluidas < transacoes.size()){
			int indiceTransacao = 0;
			int operacaoSelecionada;
			System.out.println("Selecione a próxima operação:");
			for(Transacao t : transacoes){
				if(t.hasProximaOperacao()){
					System.out.println(indiceTransacao + ") " + t.getOperacaoAtual());
				}
				indiceTransacao++;
			}
			operacaoSelecionada = in.nextInt();
			if( (operacaoSelecionada >= 0 && operacaoSelecionada < transacoes.size()) && transacoes.get(operacaoSelecionada).hasProximaOperacao()){//Verifica se operação selecionada é válida
				
				Transacao transacaoSelecionada = transacoes.get(operacaoSelecionada);
				Operacao operacao = transacaoSelecionada.getOperacaoAtual();
				operacoesEntrada.add(operacao);

				if(transacaoSelecionada.isPrimeiraOperacao()){//Atualiza o timestamp caso seja a primeira operação
					transacaoSelecionada.setTimestamp();
				}

				transacaoSelecionada.passarOperacao();
				if(operacao instanceof Commit)
					transacoesConcluidas++;

			}
		}
		for(Transacao t : transacoes){//Reinicia as transações
			t.abort();
			t.start(t.getTimestamp());
		}
	}

	//Ler arquivo de entrada
	public static void lerEntrada() throws Exception{
		String linha;

		while(bufferIn.ready()){
			linha = bufferIn.readLine();
			linha.toLowerCase();

			if(!(linha.charAt(0) == 'T' || linha.charAt(0) == 't'))
				continue;

			int caracterAtual = 1;
			String identificadorTransacao = "";
			
			while(caracterAtual < linha.length() && linha.charAt(caracterAtual) != ':' ){ //Descobre identificador da transação
				identificadorTransacao += linha.charAt(caracterAtual);
				caracterAtual++;
			}

			Transacao transacao = new Transacao(Integer.parseInt(identificadorTransacao));

			caracterAtual++;//Pula ":"
			while(caracterAtual < linha.length()){//Descobre operações
				
				Operacao operacao;
				char tipoOperacao = linha.charAt(caracterAtual);
				caracterAtual++;

				if(tipoOperacao == 'c'){ // operação commit
					operacao = new Commit(transacao);
					caracterAtual += 2; //pula "()"
				}else if(tipoOperacao == 'r' || tipoOperacao == 'w'){ // operação de escrita ou leitura
					caracterAtual++;//Pula "("		
					String identificadorDado = "";
					
					while( caracterAtual < linha.length() && linha.charAt(caracterAtual) != ')' ){ //Descobre identificador do Dado
						identificadorDado += linha.charAt(caracterAtual);
						caracterAtual++;
					}

					Dado dado;
					if(dados.containsKey(identificadorDado)) // Verifica se o dado já foi instanciado anteriormente
						dado = dados.get(identificadorDado);
					else{//Cria novo dado
						dado = new Dado(identificadorDado);
						dados.put(identificadorDado,dado);
					}

					if(tipoOperacao == 'r')//operação de leitura
						operacao = new Read(transacao,dado);
					else//operação de escrita
						operacao = new Write(transacao,dado);
					caracterAtual++;//Pula ")"

				}else//Operação não válida
					continue;
					
				transacao.addOperacao(operacao); // Adiciona operação na transação
			}

			transacoes.add(transacao);
		}
	}

	public static void gravarSaida() throws Exception{
		List<Operacao> listaEntrada = escalonador.getOperacoesEntrada();
		List<Transacao> listaEfetivadas = escalonador.getTransacoesEfetivadas();
		List<Transacao> listaAbortadas = escalonador.getTransacoesAbortadas();
		List<Operacao> listaSaida = escalonador.escalonar();

		bufferOut.write("Schedule de Entrada: ");
		System.out.println("Schedule de Entrada: ");
		for(Operacao entrada:listaEntrada){
			bufferOut.write(entrada.toString());
			System.out.print(entrada.toString());
		}
		System.out.println();
		bufferOut.newLine();

		bufferOut.write("Schedule de Saída: ");
		System.out.println("Schedule de Saída: ");
		for(Operacao saida:listaSaida){
			bufferOut.write(saida.toString());
			System.out.print(saida.toString());
		}
		System.out.println();
		bufferOut.newLine();

		bufferOut.write("Deadlock: " +escalonador.getDeadLock() );
		System.out.println("Deadlock: " +escalonador.getDeadLock() );
		bufferOut.newLine();

		bufferOut.write("Transações Abortadas: ");
		System.out.println("Transações Abortadas: ");
		for(Transacao abortadas:listaAbortadas){
			bufferOut.write(abortadas.toString() + ",");
			System.out.print(abortadas.toString() + ",");
		}
		System.out.println();
		bufferOut.newLine();

		bufferOut.write("Transações Efetivadas: ");
		System.out.print("Transações Efetivadas: ");
		for(Transacao efetivadas:listaEfetivadas){
			bufferOut.write(efetivadas.toString() + ",");
			System.out.print(efetivadas.toString() + ",");
		}
		System.out.println();
		bufferOut.newLine();

		for(Transacao transacao : transacoes){
			// Map<Dado,Espera> esperando = transacao.getEsperando();
			for(Espera esperando :transacao.getEsperando().values())
				for(Transacao gerouEspera : esperando.getTransacoesEspera()){
					bufferOut.write(transacao + " --> " + gerouEspera + "\tPor: " + esperando. getDadoEspera() + "\n");
					System.out.print(transacao + " --> " + gerouEspera + "\tPor: " + esperando. getDadoEspera() + "\n");
				}
		}

		bufferOut.flush();
	}

	public static void testar(){
		System.out.println("Dados:");
		for(Dado dado : dados.values())
			System.out.println(dado.getIdentificador());
		System.out.println("Transações:");
		for(Transacao transacao : transacoes){
			System.out.print("T" + transacao.getIdentificador() + " : " );
			while(transacao.hasProximaOperacao()){
				System.out.print(transacao.getOperacaoAtual());
				transacao.passarOperacao();
			}
			System.out.println();
		}
	}

	public static void testarSaida(){
		List<Operacao> testeSaida = escalonador.escalonar();
		System.out.println("Escalonamento de Saída");
		for(Operacao o : testeSaida)
			System.out.print(o);
		System.out.println();
	}
}