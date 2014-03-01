package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

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
        // preencher periodoTeste com disciplinas primeiroPeriodo
        periodoTeste.addDisciplina(calculoI);
        periodoTeste.addDisciplina(vetorial);
        periodoTeste.addDisciplina(lpt);
        periodoTeste.addDisciplina(p1);
        periodoTeste.addDisciplina(ic);
        periodoTeste.addDisciplina(lp1);

        List<Disciplina> DisciplinasNaGrade = gradeTeste.getTodasDisciplinas();
        List<Disciplina> DisciplinasNoPrimPeriodo = periodoTeste.getDisciplinas();

        // possui 64 disciplinas
        assertEquals(DisciplinasNaGrade.size(), 64);
        
        // testa se possui todas as disciplinas do primeiro periodo
        for (Disciplina i : DisciplinasNoPrimPeriodo) {
            if (!(DisciplinasNaGrade.contains(i))) {
                fail("Grade não possui todas disciplinas primeiro período");
            }
        }
    }
    
    @Test
    public void ConsegueConverterTodasAsDisciplinasParaString() {
    	String disciplinasParaString = gradeTeste.disciplinasParaString();
    	String disciplinasParaStringSemEspacos = disciplinasParaString.replace(" ", "");
        assertTrue(disciplinasParaStringSemEspacos.contains("[26,\"SistemasdeInformaçãoI\",4,5,4,0]"));
        assertTrue(disciplinasParaStringSemEspacos.contains("[2,\"LeituraeProd.deTextos\",4,2,1,1]"));
    }
    
    @Test
    public void associaDisciplinaAoPeriodo() {
    	// Pode alocar
    	calculoII = gradeTeste.getDisciplinaPorID(13);
    	try {
    		assertTrue(gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2));
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir alocar.");
    	}
    	assertEquals(2, gradeTeste.getPeriodoDaDisciplina(calculoII));

    	// Nao pode alocar no primeiro periodo
    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 1);
    		fail("Não deveria conseguir alocar no primeiro periodo.");
    	} catch (InvalidOperationException e) {
    	}

    	// Periodos nao existentes (<0 ou >12)
    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 0);
    		fail("Não deveria conseguir alocar em um periodo menor que 1.");
    	} catch (InvalidOperationException e) {
    	}

    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 13);
    		fail("Não deveria conseguir alocar em um periodo maior que 12.");
    	} catch (InvalidOperationException e) {
    	}

    	// Periodo com 28 creditos
    	for (int i = 7; i <= 13; i++) { // disciplinas do segundo periodo: id 7 ao 13
    		try {
    			if (i != calculoII.getID()) {
    				assertTrue(gradeTeste.associarDisciplinaAoPeriodo(gradeTeste.getDisciplinaPorID(i), 2));
    			}
    		} catch (InvalidOperationException e) {
    			fail("Deveria conseguir alocar.");
    		}
    	}
    	try {
    		Disciplina DC = gradeTeste.getDisciplinaPorID(40); // Direito e cidadania
    		gradeTeste.associarDisciplinaAoPeriodo(DC, 2);
    		fail("Não deveria conseguir alocar mais de 28 créditos.");
    	} catch (InvalidOperationException e) {
    	}

    	// Falta pre-requisito pra poder alocar
    	try {
    		Disciplina OAC = gradeTeste.getDisciplinaPorID(23); // Org. e Arq. de Comp.
    		assertFalse(gradeTeste.associarDisciplinaAoPeriodo(OAC, 3));
    	} catch (InvalidOperationException e) {
    		fail("Não deveria causar exceção.");
    	}
    }
    
    @Test
    public void desalocaDisciplina() {
    	List<Disciplina> disciplinasDesalocadas;
    	Disciplina disciplina;

    	// Desaloca sem problema
    	calculoII = gradeTeste.getDisciplinaPorID(13);
    	try {
    		assertTrue(gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2));

    		disciplinasDesalocadas = gradeTeste.desalocarDisciplina(calculoII, false);
    		assertEquals(1, disciplinasDesalocadas.size());
    		disciplina = disciplinasDesalocadas.get(0);
    		assertEquals(calculoII.getID(), disciplina.getID());
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir desalocar.");
    	}
    	
    	// Ja esta desalocado
    	try {
    		gradeTeste.desalocarDisciplina(calculoII, false);
    		fail("Não deveria ser válido desalocar uma disciplina que já está desalocada.");
    	} catch (InvalidOperationException e) {
    	}
    	
    	// Nao pode desalocar do primeiro periodo
    	try {
    		gradeTeste.desalocarDisciplina(calculoI, false);
    		fail("Não deveria ser válido desalocar uma disciplina do primeiro periodo.");
    	} catch (InvalidOperationException e) {
    	}

    	// Nenhuma disciplina e desalocada se houver pos-requisitos e nao forcarmos a desalocacao
    	try {
    		calculoII = gradeTeste.getDisciplinaPorID(13);
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    		probabilidade = gradeTeste.getDisciplinaPorID(17);
    		gradeTeste.associarDisciplinaAoPeriodo(probabilidade, 3);
    		disciplinasDesalocadas = gradeTeste.desalocarDisciplina(calculoII, false);
    		assertEquals(0, disciplinasDesalocadas.size());
    	} catch (InvalidOperationException e) {
    		fail("Deveria ser valido tentar desalocar uma disciplina alocada.");
    	}
    	
    	// Desaloca a disciplina e seus pos-requisitos alocados (desalocacao forcada)
    	try {
    		disciplinasDesalocadas = gradeTeste.desalocarDisciplina(calculoII, true);
    		assertEquals(2, disciplinasDesalocadas.size());
    	} catch (InvalidOperationException e) {
    		fail("Deveria ser valido tentar desalocar uma disciplina alocada.");
    	}
    }

    @Test
    public void reseta() {
    	// Aloca o necessario para o teste
    	calculoII = gradeTeste.getDisciplinaPorID(13);
    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    		gradeTeste.desalocarDisciplina(calculoII, true);
    		gradeTeste.associarDisciplinaAoPeriodo(calculoII, 2);
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir alocar/desalocar.");
    	}
    	
    	// Verifica se apos o reset ainda esta alocado
		gradeTeste.resetar();
		try {
			gradeTeste.desalocarDisciplina(calculoII, false);
			fail("Cálculo II já deveria estar desalocado.");
		} catch (InvalidOperationException e) {
		}

		// Verifica se apos o reset o primeiro periodo tem 6 cadeiras
		Periodo periodo = gradeTeste.getPeriodo(1);
		List<Disciplina> disciplinas = periodo.getDisciplinas();
		assertEquals(6, disciplinas.size());
    }
}
