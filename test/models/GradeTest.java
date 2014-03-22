package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import models.Disciplina;
import models.Grade;
import models.InvalidOperationException;
import models.Periodo;

import org.junit.Before;
import org.junit.Test;

public class GradeTest {

    private Periodo periodoTeste;
    private Grade gradeTeste = new Grade();

    /*
     * DISCIPLINAS
     */
    // Primeiro periodo
    private Disciplina calculoI;
    private Disciplina vetorial;
    private Disciplina lpt;
    private Disciplina p1;
    private Disciplina ic;
    private Disciplina lp1;

    private Disciplina calculoII;
    private Disciplina probabilidade;

    @Before
    public void setUp() {
        periodoTeste = new Periodo();
        // nome , creditos, dificuldade, periodo, id
        calculoI = new Disciplina("Cálculo I", 4, 7, 1, 3);
        vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4);
        lpt = new Disciplina("Leitura e Prod. de Textos", 4, 2, 1, 2);
        p1 = new Disciplina("Programação I", 4, 4, 1, 1);
        ic = new Disciplina("Int. à Computacação", 4, 5, 1, 5);
        lp1 = new Disciplina("Lab. de Programação I", 4, 4, 1, 6);
    }

    @Test
    public void possuiDisciplinasPrimeiroPeriodo() {
        try {
        	periodoTeste.alocarDisciplina(calculoI, false);
        	periodoTeste.alocarDisciplina(vetorial, false);
        	periodoTeste.alocarDisciplina(lpt, false);
        	periodoTeste.alocarDisciplina(p1, false);
        	periodoTeste.alocarDisciplina(ic, false);
        	periodoTeste.alocarDisciplina(lp1, false);
        
        } catch (InvalidOperationException e) {
        	fail("Não deveria lançar exceção.");
        }
        
        List<Disciplina> DisciplinasNaGrade = gradeTeste.getDisciplinas();
        List<Disciplina> DisciplinasNoPrimPeriodo = periodoTeste.getDisciplinas();

        assertEquals(DisciplinasNaGrade.size(), 64);
        
        for (Disciplina i : DisciplinasNoPrimPeriodo) {
            if (!(DisciplinasNaGrade.contains(i))) {
                fail("Grade não possui todas disciplinas primeiro período");
            }
        }
    }
    
    @Test
    public void ConsegueConverterTodasAsDisciplinasParaString() {
    	String disciplinasParaString = gradeTeste.toString();
    	String disciplinasParaStringSemEspacos = disciplinasParaString.replace(" ", "");
        assertTrue(disciplinasParaStringSemEspacos.contains("[26,\"SistemasdeInformaçãoI\",4,5,4,0,0]"));
        assertTrue(disciplinasParaStringSemEspacos.contains("[2,\"LeituraeProd.deTextos\",4,2,1,1,0]"));
    }
    
    @Test
    public void associaDisciplinaAoPeriodo() {
    	try {
    		calculoII = gradeTeste.getDisciplinaPorID(13);
    	} catch (InvalidOperationException e) {
    		fail("Não deveria falhar");
    	}
    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir alocar.");
    	}
    	assertEquals(2, gradeTeste.getPeriodoDaDisciplina(calculoII));

    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 0);
    		fail("Não deveria conseguir alocar em um periodo menor que 1.");
    	} catch (InvalidOperationException e) {
    		assertEquals(e.getMessage(), "Não podem ser alocadas disciplinas para períodos que não existem.");
    	}

    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 13);
    		fail("Não deveria conseguir alocar em um periodo maior que 12.");
    	} catch (InvalidOperationException e) {
    		assertEquals(e.getMessage(), "Não podem ser alocadas disciplinas para períodos que não existem.");
    	}

    	for (int i = 7; i <= 13; i++) {
    		try {
    			if (i != calculoII.getId()) {
    				gradeTeste.associarDisciplinaAoPeriodo(gradeTeste.getDisciplinaPorID(i), 2);
    			}
    		} catch (InvalidOperationException e) {
    			fail("Deveria conseguir alocar.");
    		}
    	}
    	
    	try {
    		
    		Disciplina SI = gradeTeste.getDisciplinaPorID(26);
    		Disciplina DireitoCidadania = gradeTeste.getDisciplinaPorID(40);
    		
    		gradeTeste.associarDisciplinaAoPeriodo(DireitoCidadania, 3);
    		gradeTeste.associarDisciplinaAoPeriodo(SI, 2);
    		
    		fail("Não deveria conseguir alocar mais de 28 créditos.");
    	} catch (InvalidOperationException e) {
    		assertEquals(e.getMessage(), "Disciplina não pode ser alocada neste período.");
    	}
    }
    
    @Test
    public void desalocaDisciplina() {
    	try {
			calculoII = gradeTeste.getDisciplinaPorID(13);
		} catch (InvalidOperationException e1) {
			fail("Deveria ser possível obter pelo id.");
		}

    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    		gradeTeste.desalocarDisciplina(calculoII);
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir desalocar.");
    	}
    	
    	try {
    		gradeTeste.desalocarDisciplina(calculoII);
    		fail("Não deveria ser válido desalocar uma disciplina que já está desalocada.");
    	} catch (InvalidOperationException e) {
    		assertEquals(e.getMessage(), "Esta disciplina já está desalocada.");
    	}
    	
    	try {
    		calculoII = gradeTeste.getDisciplinaPorID(13);
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    		probabilidade = gradeTeste.getDisciplinaPorID(17);
    		gradeTeste.associarDisciplinaAoPeriodo(probabilidade, 3);
    		gradeTeste.desalocarDisciplina(calculoII);
    	} catch (InvalidOperationException e) {
    		fail("Deveria ser valido tentar desalocar uma disciplina alocada.");
    	}
    }

    @Test
    public void reseta() {
    	// Aloca o necessario para o teste
    	try {
			calculoII = gradeTeste.getDisciplinaPorID(13);
		} catch (InvalidOperationException e1) {
		}

    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    		gradeTeste.desalocarDisciplina(calculoII);
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir alocar/desalocar.");
    	}
    	
    	// Verifica se apos o reset ainda esta alocado
		gradeTeste.resetar();
		try {
			gradeTeste.desalocarDisciplina(calculoII);
			fail("Cálculo II já deveria estar desalocado.");
		} catch (InvalidOperationException e) {
		}

		// Verifica se apos o reset o primeiro periodo tem 6 cadeiras
		Periodo periodo = gradeTeste.getPeriodo(1);
		List<Disciplina> disciplinas = periodo.getDisciplinas();
		assertEquals(6, disciplinas.size());
    }
}
