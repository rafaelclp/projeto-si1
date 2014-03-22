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

	@Before
	public void setUp() {
		periodoTeste = new Periodo();
		
		calculoI = new Disciplina("Cáĺculo I", 4, 7, 1, 3);
		vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4);
		ffc = new Disciplina("Fund. de Física Clássica", 4, 8, 2, 12);
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
	}

	@Test
	public void removeDisciplinas() {
		try {
			periodoTeste.alocarDisciplina(calculoI, false);
			periodoTeste.alocarDisciplina(vetorial, false);
			
			periodoTeste.desalocarDisciplina(calculoI);
			assertFalse(periodoTeste.contains(calculoI));
			assertTrue(periodoTeste.getDisciplinas().contains(vetorial));
			assertFalse(periodoTeste.getDisciplinas().isEmpty());
			
			periodoTeste.desalocarDisciplina(vetorial);
			assertFalse(periodoTeste.contains(vetorial));
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
			
			periodoTeste.alocarDisciplina(calculoI, false);
			assertEquals(periodoTeste.totalDeCreditos(), 4);
			
			periodoTeste.alocarDisciplina(vetorial, false);
			assertEquals(periodoTeste.totalDeCreditos(), 8);
			
			periodoTeste.alocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 12);
			
			periodoTeste.desalocarDisciplina(ffc);
			assertEquals(periodoTeste.totalDeCreditos(), 8);
			
			periodoTeste.desalocarDisciplina(vetorial);
			assertEquals(periodoTeste.totalDeCreditos(), 4);
			
			periodoTeste.alocarDisciplina(ffc, false);
			assertEquals(periodoTeste.totalDeCreditos(), 8);
			
			periodoTeste.desalocarDisciplina(calculoI);
			assertEquals(periodoTeste.totalDeCreditos(), 4);
			
			periodoTeste.desalocarDisciplina(ffc);
			assertEquals(periodoTeste.totalDeCreditos(), 0);
		
		} catch (InvalidOperationException e) {
			fail("Nao deveria lançar exceção");
		}
	}
}
