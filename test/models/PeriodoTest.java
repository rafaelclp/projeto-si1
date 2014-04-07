package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import models.Disciplina;
import models.InvalidOperationException;
import models.Periodo;

import org.junit.Before;
import org.junit.Test;

import play.db.ebean.Model.Finder;
import play.test.FakeApplication;

public class PeriodoTest {

	private Periodo periodoTeste;
	private Disciplina calculoI;
	private Disciplina vetorial;
	private Disciplina ffc;
	private Disciplina p1;

	@Before
	public void setUp() {
		periodoTeste = new Periodo();
		
		calculoI = new Disciplina("Cáĺculo I", 4, 7, 1, 3L);
		vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4L);
		ffc = new Disciplina("Fund. de Física Clássica", 4, 8, 2, 12L);
		p1 = new Disciplina("Programação 1", 4, 8, 1, 6L);
	}

	@Test
	public void criaPeriodo() {
		assertTrue(periodoTeste.getDisciplinas().isEmpty());
	}

	@Test
	public void alocaDisciplinas() {
		try {
			periodoTeste.alocarDisciplina(calculoI, false);
			periodoTeste.alocarDisciplina(vetorial, false);
			
			assertFalse(periodoTeste.passouDoLimiteDeCreditos());
			
			assertTrue(periodoTeste.contains(calculoI));
			assertTrue(periodoTeste.contains(vetorial));
			
			assertTrue(periodoTeste.getDisciplinas().contains(calculoI));
			assertTrue(periodoTeste.getDisciplinas().contains(vetorial));
			assertFalse(periodoTeste.getDisciplinas().contains(ffc));
			
			for(int i = 0; i < 5; i ++) {
				assertTrue(periodoTeste.podeAlocar(calculoI, false));
				periodoTeste.alocarDisciplina(calculoI, false);
			}
		} catch (InvalidOperationException e) {
			fail("Nao deveria lançar exceção.");
		}
			
		assertEquals(periodoTeste.totalDeCreditos(), 28);
		assertFalse(periodoTeste.podeAlocar(calculoI, false));
		
		try {
			periodoTeste.alocarDisciplina(calculoI, false);
		} catch (InvalidOperationException e) {
			assertEquals(e.getMessage(), "Disciplina não pode ser alocada neste período.");
		}

		assertTrue(periodoTeste.podeAlocar(calculoI, true));
		
		try {
			periodoTeste.alocarDisciplina(calculoI, true);
		} catch (InvalidOperationException e) {
			fail("Nao deveria lançar exceção.");
		}
		
		assertTrue(periodoTeste.passouDoLimiteDeCreditos());
	}

	@Test
	public void desalocaDisciplinas() {
		try {
			periodoTeste.alocarDisciplina(calculoI, false);
			periodoTeste.alocarDisciplina(vetorial, false);
			periodoTeste.alocarDisciplina(ffc, false);
			periodoTeste.alocarDisciplina(p1, false);
			
			periodoTeste.desalocarDisciplina(calculoI);
			assertFalse(periodoTeste.contains(calculoI));
			
			assertTrue(periodoTeste.getDisciplinas().contains(vetorial));
			assertFalse(periodoTeste.getDisciplinas().isEmpty());

			periodoTeste.desalocarDisciplina(vetorial);
			assertFalse(periodoTeste.getDisciplinas().contains(vetorial));

			assertTrue(periodoTeste.contains(p1));
			periodoTeste.desalocarDisciplina(p1);
			
			assertTrue(periodoTeste.contains(ffc));
			periodoTeste.desalocarDisciplina(ffc);
			
			assertTrue(periodoTeste.getDisciplinas().isEmpty());
		
		} catch (InvalidOperationException e) {
			fail("Não deveria lançar excessão.");
		}
		
		try {
			periodoTeste.desalocarDisciplina(vetorial);
		} catch (InvalidOperationException e) {
			assertEquals(e.getMessage(), "Disciplina não existente neste período.");
		}
	}

	@Test
	public void calculaCreditos() {
		try {
			assertEquals(periodoTeste.totalDeCreditos(), 0);
			
			assertTrue(periodoTeste.podeAlocar(calculoI, false));
			periodoTeste.alocarDisciplina(calculoI, false);
			assertEquals(periodoTeste.totalDeCreditos(), 4);

			assertTrue(periodoTeste.podeAlocar(vetorial, false));
			periodoTeste.alocarDisciplina(vetorial, false);
			assertEquals(periodoTeste.totalDeCreditos(), 8);

			assertTrue(periodoTeste.podeAlocar(ffc, false));
			periodoTeste.alocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 12);

			assertTrue(periodoTeste.podeAlocar(p1, false));
			periodoTeste.alocarDisciplina(p1, false);
			assertEquals(periodoTeste.totalDeCreditos(), 16);
			
			periodoTeste.desalocarDisciplina(ffc);
			assertEquals(periodoTeste.totalDeCreditos(), 12);
			
			periodoTeste.desalocarDisciplina(vetorial);
			assertEquals(periodoTeste.totalDeCreditos(), 8);

			assertTrue(periodoTeste.podeAlocar(ffc, false));
			periodoTeste.alocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 12);
			
			periodoTeste.desalocarDisciplina(calculoI);
			assertEquals(periodoTeste.totalDeCreditos(), 8);

			periodoTeste.desalocarDisciplina(ffc);
			assertEquals(periodoTeste.totalDeCreditos(), 4);
			
			periodoTeste.desalocarDisciplina(p1);
			assertEquals(periodoTeste.totalDeCreditos(), 0);
		
		} catch (InvalidOperationException e) {
			fail("Nao deveria lançar exceção");
		}
	}

	@Test
	public void resetar() {
		try {
			periodoTeste.alocarDisciplina(calculoI, false);
			periodoTeste.alocarDisciplina(p1, false);
			periodoTeste.alocarDisciplina(ffc, false);
			periodoTeste.alocarDisciplina(vetorial, false);
		} catch (InvalidOperationException e) {
			fail("Nao deveria lançar exceção");
		}
		
		assertTrue(periodoTeste.totalDeCreditos() == 16);
		assertTrue(periodoTeste.contains(p1));
		periodoTeste.resetar();
		assertTrue(periodoTeste.totalDeCreditos() == 0);
		assertFalse(periodoTeste.contains(p1));
	}
	
	@Test
	public void registraNoBD() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);

		CarregadorDeDisciplinas.limparCache();
		List<Disciplina> disciplinas = CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL);
		if (disciplinas.size() < 5) {
			fail("Verifique se o carregador está correto, ele é necessário para testar o Periodo.");
		}

		Periodo p = new Periodo();
		Disciplina d1 = disciplinas.get(0), d2 = disciplinas.get(1);
		try {
			p.alocarDisciplina(d1, false);
			p.alocarDisciplina(d2, false);
		} catch (InvalidOperationException e) {
			fail("Deveria alocar");
		}
		p.save();

		int id = p.getId().intValue();
		Finder<Long, Periodo> find = new Finder<Long, Periodo>(Long.class,
				Periodo.class);
		p = find.byId(new Long(id+1));
		assertNull(p);

		p = find.byId(new Long(id));
		assertEquals(2, p.getDisciplinas().size());
		assertEquals(d1.getNome(), p.getDisciplinas().get(0).getNome());
		assertEquals(d2.getNome(), p.getDisciplinas().get(1).getNome());
		
		Periodo p2 = new Periodo();
		try {
			p2.alocarDisciplina(d1, false);
			p2.alocarDisciplina(d2, false);
		} catch (InvalidOperationException e) {
			fail("Deveria alocar");
		}
		p2.save();

		assertNotEquals(p.getId(), p2.getId());
		p2 = find.byId(new Long(p2.getId()));
		assertEquals(2, p.getDisciplinas().size());
		assertEquals(d1.getNome(), p.getDisciplinas().get(0).getNome());
		assertEquals(d2.getNome(), p.getDisciplinas().get(1).getNome());
	}
}
