import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
public class WoundWait{
	
	private static HashMap<String,Dado> dados;
	private static ArrayList<Transacao> transacoes;
	private static Escalonador escalonador;
	private static FileReader inFile;
	private static FileWriter outFile;
	private static BufferedReader bufferIn;
	private static BufferedWriter bufferOut;

	public static void main(String args[]){
		
		//Testes
		Transacao t1 = new Transacao(1);
		Dado d1 = new Dado("D1");
		Operacao op1 = new Read(t1,d1);
		Operacao op2 = new Abort(t1);
		t1.addOperacao(op1);
		t1.addOperacao(op2);

		System.out.println(t1 + "\n" + d1);
		t1.start();
		op1.operar();
		System.out.println(t1 + "\n" + d1);
		op2.operar();
		System.out.println(t1 + "\n" + d1);
		//Testes

		dados = new HashMap<>();
		transacoes = new ArrayList<>();
		if(args.length == 2){
			try{
				inFile = new FileReader(args[0]);
				outFile = new FileWriter(args[1]);
				
				bufferIn = new BufferedReader(inFile);
				bufferOut = new BufferedWriter(outFile);


				lerEntrada();

				testar();

				bufferIn.close();
				inFile.close();

			}catch(Exception e){
				System.out.println(e);
			}
		}else{
			System.out.println("Erro na passagem de Argumentos:\n Devem haver 2 argumentos.\nEx.:\njava WoundWait ArquivoEntrada.txt ArquivoSaisa.txt");
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

	public static void gravarSaida(){

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
}