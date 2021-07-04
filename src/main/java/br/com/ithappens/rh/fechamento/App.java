package br.com.ithappens.rh.fechamento;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.exception.ExceptionUtils;

public class App {
	
	private static Logger logger = Logger.getLogger(App.class.getSimpleName());
	
	public static void main(String[] args) throws IOException {
		try {
			App.run(args);
		} catch (Exception e) {
			logger.log(Level.SEVERE, () -> ExceptionUtils.getStackTrace(e));
		}
	}
	
	public static void run(String[] args) throws IOException{
		logger.log(Level.INFO, () -> " >> INICIANDO LEITURA DO ARQUIVO");
		Collection<String> linhas = App.readTextFile(Paths.get(args[0]));

		logger.log(Level.INFO, () -> ">>> INICIANDO ESCRITA DO ARQUIVO");
		Path path = Paths.get("saida-"+LocalDate.now()+".txt");
		App.writeTextFile(path, linhas);
	}
	
	public static Collection<String> readTextFile(Path path) throws IOException {
		return Files.readAllLines(path).stream().filter(App::isValidLine).map(App::prepareLine).collect(Collectors.toList());
	}
	
	public static void writeTextFile(Path path, Collection<String> linhas) throws IOException {
		Files.write(path, linhas);
	}
	
	public static String prepareLine(String linha) {
		String[] itensLinha = linha.split(";", 5);
		StringBuilder linhaStringBuilder = new StringBuilder();
		linhaStringBuilder.append("1002;");
		linhaStringBuilder.append(App.builderFilial(itensLinha[0]));
		linhaStringBuilder.append(App.builderSetor(itensLinha[1]));
		linhaStringBuilder.append(App.builderMesAno(itensLinha[2]));
		linhaStringBuilder.append(App.tratandoPercentuais(itensLinha[3]));
		linhaStringBuilder.append(App.tratandoPercentuais(itensLinha[4]));
		return linhaStringBuilder.toString();
	}
	
	public static String builderFilial(String itemLinha) {
		return itemLinha.replaceAll("\\s+","").split("-")[0] + ";";
	}
	
	public static String builderSetor(String itemLinha) {
		Map<String, String> setores = new HashMap<>();
		setores.put("Reposicao", "1");
		setores.put("Acougue", "2");
		setores.put("Frios", "3");
		setores.put("Peixaria", "4");
		setores.put("Hortifruti", "5");
		setores.put("Granjeiro", "6");
		setores.put("Padaria & Lanchonete", "7");
		return setores.get(App.removerAcentos(itemLinha)) + ";";
	}
	
	public static String builderMesAno(String itemLinha) {
		String[] itensLinha = itemLinha.replace("_", "\\.").split("\\.");
		String mes = itensLinha[0];
		String ano = itensLinha.length > 2 ? itensLinha[2] : String.valueOf(LocalDate.now().getYear());
		return mes + ";" + ano + ";";
	}

	public static String tratandoPercentuais(String itemLinha) {
		return itemLinha.replace(",",".").replace("%","") + ";";
	}
	
	public static String removerAcentos(String valorAcentuado){
		return Normalizer.normalize(valorAcentuado, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
	}
	
	public static boolean isValidLine(String linha){
		boolean check = true;
		if (linha.isBlank() || linha.split(";").length != 5) {
			logger.log(Level.WARNING, () -> "[LINHA]: " + linha);
			check = false;
		}
		return check;
	}
	
}
