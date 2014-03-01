package model;

import static org.junit.Assert.*;

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
	public void criaPeriodoCorretamente() {
		// lista de disciplinas inicial vazia
		assertTrue(periodoTeste.getDisciplinas().isEmpty());
	}

	@Test
	public void adicionaDisciplinas() {
		periodoTeste.addDisciplina(calculoI);
		periodoTeste.addDisciplina(vetorial);

		assertTrue(periodoTeste.contains(calculoI));
		assertTrue(periodoTeste.contains(vetorial));
		assertTrue(periodoTeste.getDisciplinas().contains(calculoI));
		assertTrue(periodoTeste.getDisciplinas().contains(vetorial));
		assertFalse(periodoTeste.getDisciplinas().contains(ffc));
		
		for(int i = 0; i < 5; i ++) {
			assertTrue(periodoTeste.podeAlocar(calculoI));
			periodoTeste.addDisciplina(calculoI);
		}
		// periodoTeste esta com 28 creditos
		assertFalse(periodoTeste.podeAlocar(calculoI));
	}

	@Test
	public void removeDisciplinas() {
		periodoTeste.addDisciplina(calculoI);
		periodoTeste.addDisciplina(vetorial);

		periodoTeste.removeDisciplina(calculoI);
		assertFalse(periodoTeste.contains(calculoI));
		assertTrue(periodoTeste.getDisciplinas().contains(vetorial));
		assertFalse(periodoTeste.getDisciplinas().isEmpty());

		periodoTeste.removeDisciplina(vetorial);
		assertFalse(periodoTeste.contains(vetorial));
		assertTrue(periodoTeste.getDisciplinas().isEmpty());
	}

	@Test
	public void calculaCreditos() {
		periodoTeste.addDisciplina(calculoI);
		periodoTeste.addDisciplina(vetorial);
		assertEquals(8, periodoTeste.getCreditos());
		
		periodoTeste.addDisciplina(ffc);
		assertEquals(12, periodoTeste.getCreditos());
	}
}
