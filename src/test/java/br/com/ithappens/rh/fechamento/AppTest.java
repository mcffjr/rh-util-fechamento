package br.com.ithappens.rh.fechamento;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class AppTest {
	
    private Path pathOrigem, pathResult;
	
	@TempDir
    Path tempDir;
	
	@BeforeEach
    public void setUp() {
        try {
        	this.pathOrigem = tempDir.resolve( "testfileOrigem.txt" );
        	this.pathResult = tempDir.resolve( "testfileResult.txt" );
            Files.write(pathOrigem, Arrays.asList(
            		"15 - Açailândia;Açougue;03.Março_2021;-1,00%;-1,60%",
            		" ",
            		"AsAsAs;AsAs",
            		"15 - Acailandia;Acougue;03.Marco;-1,00%;-1,60%"));
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }
	
	@Test
	void deveRetornarLinhaNoPadraoDesejado() throws IOException {
		String[] args = {pathOrigem.toString()};
		Collection<String> esperada = Arrays.asList(
				"1002;15;2;03;2021;-1.00;-1.60;",
				"1002;15;2;03;2021;-1.00;-1.60;");
		Collection<String> atual = App.readTextFile(Paths.get(args[0]));
		assertEquals(esperada, atual);
	}
	
	@Test
	void deveEscreverArquivoSemLancarExcepion() throws IOException {
		Collection<String> linhas = Arrays.asList(
				"1002;15;2;03;2021;-1.00;-1.60;",
				"1002;15;2;03;2021;-1.00;-1.60;");
		assertDoesNotThrow(() -> App.writeTextFile(pathResult, linhas)); 
	}
	
	@Test
	void deveRodarSemLancarException() throws IOException {
		String[] args = {pathOrigem.toString()};
		assertDoesNotThrow(() -> App.main(args)); 
	}
	
}
