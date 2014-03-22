package models;

import static org.junit.Assert.*;
import models.Disciplina;
import models.InvalidOperationException;
import models.Periodo;

import org.junit.Before;
import org.junit.Test;

public class PeriodoTest {

	private Periodo periodoTeste;
	private Disciplina calculoI;
	private Disciplina vetorial;
	private Disciplina ffc;
	private Disciplina p1;

	@Before
	public void setUp() {
		periodoTeste = new Periodo();
		
		calculoI = new Disciplina("Cáĺculo I", 4, 7, 1, 3);
		vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4);
		ffc = new Disciplina("Fund. de Física Clássica", 4, 8, 2, 12);
		p1 = new Disciplina("Programação 1", 4, 8, 1, 6);
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
			
			assertTrue(periodoTeste.podeDesalocar(calculoI, false));
			periodoTeste.desalocarDisciplina(calculoI, false);
			assertFalse(periodoTeste.contains(calculoI));
			assertTrue(periodoTeste.getDisciplinas().contains(vetorial));
			assertFalse(periodoTeste.getDisciplinas().isEmpty());

			assertFalse(periodoTeste.podeDesalocar(vetorial, false));
			assertTrue(periodoTeste.podeDesalocar(vetorial, true));
			periodoTeste.desalocarDisciplina(vetorial, true);
			assertFalse(periodoTeste.getDisciplinas().contains(vetorial));
			
			assertTrue(periodoTeste.contains(p1));
			periodoTeste.desalocarDisciplina(p1, true);
			periodoTeste.desalocarDisciplina(ffc, true);
			assertTrue(periodoTeste.getDisciplinas().isEmpty());
		
		} catch (InvalidOperationException e) {
			fail("Não deveria lançar excessão.");
		}
		
		try {
			periodoTeste.desalocarDisciplina(vetorial, true);
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

			assertTrue(periodoTeste.podeAlocar(vetorial, true));
			periodoTeste.alocarDisciplina(vetorial, false);
			assertEquals(periodoTeste.totalDeCreditos(), 8);

			periodoTeste.alocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 12);
			
			periodoTeste.alocarDisciplina(p1, false);
			assertEquals(periodoTeste.totalDeCreditos(), 16);
			
			periodoTeste.desalocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 12);
			
			periodoTeste.desalocarDisciplina(vetorial, true);
			assertEquals(periodoTeste.totalDeCreditos(), 8);
			
			periodoTeste.alocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 12);
			
			periodoTeste.desalocarDisciplina(calculoI, true);
			assertEquals(periodoTeste.totalDeCreditos(), 8);

			periodoTeste.desalocarDisciplina(ffc, true);
			assertEquals(periodoTeste.totalDeCreditos(), 4);
			
			periodoTeste.desalocarDisciplina(p1, true);
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
}
