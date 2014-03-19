package controllers;

import static org.junit.Assert.*;

import org.junit.Test;

public class ControlTest {
    
	@Test
	public void indexRecebeStringDeDisciplinas() {
    	String disciplinasParaString = Control.index();
    	String disciplinasParaStringSemEspacos = disciplinasParaString.replace(" ", "");
        assertTrue(disciplinasParaStringSemEspacos.contains("[26,\"SistemasdeInformaçãoI\",4,5,4,0]"));
        assertTrue(disciplinasParaStringSemEspacos.contains("[1,\"ProgramaçãoI\",4,4,1,1]"));
	}
	
	@Test
	public void alocaDisciplina() {
		Control.index();
    	
		// Nao pode alocar disciplinas que nao existem
		assertEquals("erro:Você não pode alocar disciplinas que não existem.", Control.alocarDisciplina(200, 2));

		// Pode alocar sem problema
		assertEquals("alocar:13,2", Control.alocarDisciplina(13, 2));
		
		// Nao pode alocar no primeiro periodo
    	assertEquals("erro:Não podem ser alocadas disciplinas para o primeiro período.", Control.alocarDisciplina(13, 1));
    	
    	for(int i = 0; i < 6; i++)
    		Control.alocarDisciplina(3, 2);
    	
    	// Periodo 2 esta com 28 creditos
    	assertEquals("erro:O período não pode ter mais de 28 créditos.", Control.alocarDisciplina(13, 2));
    	
    	// Falta pre-requisito pra poder alocar
    	assertEquals("erro:Pré-requisitos não cumpridos:<br /><span class=\"glyphicon glyphicon-asterisk\" style=\"font-size:10px\"></span> Matemática Discreta<br />" +
    			"<span class=\"glyphicon glyphicon-asterisk\" style=\"font-size:10px\"></span> Teoria dos Grafos<br />", Control.alocarDisciplina(18, 3));
	}
	
	/*@Test
	public void desalocaDisciplina() {
		Control.index();
		// Desaloca sem problema
		Control.alocarDisciplina(13, 2);
    	assertEquals("desalocar:13", Control.desalocarDisciplina(13, false));
    	
    	// Ja esta desalocado
    	assertEquals("erro:Esta disciplina já está desalocada.", Control.desalocarDisciplina(13, false));
    	
    	// Nao pode desalocar do primeiro periodo
    	assertEquals("erro:Disciplinas do primeiro período não podem ser desalocadas.", Control.desalocarDisciplina(3, false));
    	
    	// Tem que confirmar desalocacao por causa de pos-requisito alocado
    	Control.alocarDisciplina(13, 2);
    	Control.alocarDisciplina(17, 3);
    	assertEquals("confirmar:/desalocarDisciplina/13/true,Ao desalocar esta disciplina, serão desalocadas também estas outras:<br /><span class" +
    			"=\"glyphicon glyphicon-asterisk\" style=\"font-size:10px\"></span> Probabilidade e Est.<br />", Control.desalocarDisciplina(13, false));
    	
    	// Desaloca sem problema por ter permissao do usuario
    	String result = Control.desalocarDisciplina(13, true);
    	if (!result.equals("desalocar:13,17") && !result.equals("desalocar:17,13")) {
    		fail("Deveria desalocar os dois.");
    	}
	}
	
	@Test
	public void reseta() {
		Control.index();
		Control.alocarDisciplina(13, 2);
    	assertEquals("desalocar:13", Control.desalocarDisciplina(13, false));
    	

		Control.alocarDisciplina(13, 2);
		Control.resetar();
    	assertEquals("erro:Esta disciplina já está desalocada.", Control.desalocarDisciplina(13, false));
	}*/
}
