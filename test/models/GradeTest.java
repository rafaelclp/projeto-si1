package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.db.ebean.Model.Finder;
import play.test.FakeApplication;

public class GradeTest {

    private Periodo periodoTeste;
    private Grade gradeTeste;

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

    @Before
    public void setUp() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);
		CarregadorDeDisciplinas.limparCache();
    	gradeTeste = new Grade(CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL));
        periodoTeste = new Periodo();

        // nome , creditos, dificuldade, periodo, id
        calculoI = new Disciplina("Cálculo I", 4, 7, 1, 3L);
        vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4L);
        lpt = new Disciplina("Leitura e Prod. de Textos", 4, 2, 1, 2L);
        p1 = new Disciplina("Programação I", 4, 4, 1, 1L);
        ic = new Disciplina("Int. à Computacação", 4, 5, 1, 5L);
        lp1 = new Disciplina("Lab. de Programação I", 4, 4, 1, 6L);
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
        assertTrue(disciplinasParaStringSemEspacos.contains("[26,\"SistemasdeInformaçãoI\",4,5,4,4,0]"));
        assertTrue(disciplinasParaStringSemEspacos.contains("[2,\"LeituraeProd.deTextos\",4,2,1,1,0]"));
        
        try {
        	Disciplina si1 = gradeTeste.getDisciplinaPorID(26);
			gradeTeste.associarDisciplinaAoPeriodo(si1, 1);			
		} catch (InvalidOperationException e) {
			fail("Nao deveria lançar exceção");
		}

    	disciplinasParaString = gradeTeste.toString();
    	disciplinasParaStringSemEspacos = disciplinasParaString.replace(" ", "");
        
        assertTrue(disciplinasParaStringSemEspacos.contains("[26,\"SistemasdeInformaçãoI\",4,5,4,1,1]"));
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
    		gradeTeste.associarDisciplinaAoPeriodo(calculoI, 6);
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir alocar.");
    	}
    	
    	assertEquals(6, gradeTeste.getPeriodoDaDisciplina(calculoI));

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
			calculoII = gradeTeste.getDisciplinaPorID(3);
		} catch (InvalidOperationException e) {
			fail("Deveria ser possível obter pelo id.");
		}

    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoI, 1);
    		gradeTeste.associarDisciplinaAoPeriodo(p1, 1);
    		gradeTeste.associarDisciplinaAoPeriodo(ic, 1);
    		gradeTeste.associarDisciplinaAoPeriodo(lp1, 1);
    		gradeTeste.associarDisciplinaAoPeriodo(lpt, 1);
    		gradeTeste.desalocarDisciplina(calculoI);
    	} catch (InvalidOperationException e) {
    		fail("Nao deveria lançar exceção");
    	}
    	
    	try {
    		gradeTeste.desalocarDisciplina(calculoI);
    		fail("Não deveria ser válido desalocar uma disciplina que já está desalocada.");
    	} catch (InvalidOperationException e) {
    		assertEquals("Esta disciplina já está desalocada.", e.getMessage());
    	}
    	
    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoI, 1);
    		gradeTeste.associarDisciplinaAoPeriodo(vetorial, 1);
    		gradeTeste.desalocarDisciplina(calculoI);
    	} catch (InvalidOperationException e) {
    		assertEquals("", e.getMessage());
    		fail("Deveria ser valido tentar desalocar uma disciplina alocada.");
    	}
    }

    @Test
    public void reseta() {
    	try {
    		gradeTeste.associarDisciplinaAoPeriodo(calculoI, 1);
    		gradeTeste.desalocarDisciplina(calculoI);
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir alocar/desalocar.");
    	}
    	
		gradeTeste.resetar();

		Periodo periodo = gradeTeste.getPeriodo(1);
		List<Disciplina> disciplinas = periodo.getDisciplinas();
		assertEquals(6, disciplinas.size());
		assertTrue(disciplinas.contains(calculoI));
    }

    @Test
    public void registraNoBD() {
    	Grade grade = new Grade();
    	grade.setDisciplinas(CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL));
    	
    	grade.setPeriodoCursando(2);
    	grade.save();
    	
    	int id = grade.getId().intValue();
		Finder<Long, Grade> find = new Finder<Long, Grade>(Long.class,
				Grade.class);
		grade = find.byId(new Long(id));
		assertEquals(2, grade.getPeriodoCursando());
		assertEquals(0, grade.getPeriodo(1).getDisciplinas().size());

		Disciplina d = null, d2 = null;
		try {
			boolean associou = false;
			grade.setDisciplinas(CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL));
			assertEquals(64, grade.getDisciplinas().size());
			for (int i = 0; i < grade.getDisciplinas().size(); i++) {
				d = grade.getDisciplinas().get(i);
				if (d.getPreRequisitos().size() == 0) {
					if (d2 == null) {
						d2 = d;
					} else {
						grade.associarDisciplinaAoPeriodo(d, 1);
						associou = true;
						break;
					}
				}
			}
			assertTrue(associou);
		} catch (InvalidOperationException e) {
			fail("Deveria ser possível alocar uma disciplina sem pre-requisitos.");
		}
		grade.save();

		Long periodo_id = grade.getPeriodo(1).getId();
		Finder<Long, Periodo> findp = new Finder<Long, Periodo>(Long.class,
				Periodo.class);
		Periodo p = findp.byId(periodo_id);
		assertEquals(1, grade.getPeriodo(1).getDisciplinas().size());
		assertEquals(1, p.getDisciplinas().size());
		assertEquals(d, p.getDisciplinas().get(0));

		try {
			p.alocarDisciplina(d2, false);
		} catch (InvalidOperationException e) {
			fail("Deveria alocar.");
		}
		p.save();
		p = findp.byId(p.getId());
		assertEquals(2, p.getDisciplinas().size());
		assertEquals(d, p.getDisciplinas().get(1));
		assertEquals(d2, p.getDisciplinas().get(0));

		grade = find.byId(grade.getId());
		assertNotNull(grade.getPeriodos());
		assertEquals(12, grade.getPeriodos().size());
		assertEquals(2, grade.getPeriodo(1).getDisciplinas().size());

		List<Disciplina> l = grade.getPeriodo(1).getDisciplinas();
		d2 = l.get(1);
		assertEquals(d.getId(), d2.getId());
    }
    
    @Test
    public void persistenciaAoResetar() {
    	Grade grade = new Grade();
    	grade.setDisciplinas(CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL));
    	grade.resetar();
    	
    	assertEquals(6, grade.getPeriodo(1).getDisciplinas().size());
    	
    	grade.save();
    	Finder<Long, Grade> find = new Finder<Long, Grade>(Long.class,
    			Grade.class);
    	Grade g = find.byId(grade.getId());
    	assertEquals(6, grade.getPeriodo(1).getDisciplinas().size());
    }
}
