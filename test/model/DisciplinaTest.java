package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class DisciplinaTest {

	private Disciplina calculoI;
	private Disciplina calculoII;
	private Disciplina vetorial;
	private Disciplina ffc;

	@Before
	public void setUp() {
		calculoI = new Disciplina("Cálculo I", 4, 7, 1, 3);
		vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4);
		calculoII = new Disciplina("Cálculo II", 4, 7, 2, 13);
		ffc = new Disciplina("Fund. de Física Clássica", 4, 8, 2, 12);
	}

	@Test
	public void criacaoDisciplina() {
		assertEquals("Cálculo I", calculoI.getNome());
		assertEquals(4, calculoI.getCreditos());
		assertEquals(7, calculoI.getDificuldade());
		assertEquals(1, calculoI.getPeriodoPrevisto());
		assertEquals(3, calculoI.getID());
		
		assertNotEquals(calculoI.toString(), vetorial.toString());
	}
	
	@Test
	public void adicionaPreRequisitos(){
		calculoII.addPreRequisito(calculoI);
		assertTrue(calculoII.getPreRequisitos().contains(calculoI));
		
		ffc.addPreRequisito(calculoI);
		ffc.addPreRequisito(vetorial);
		assertTrue(ffc.getPreRequisitos().contains(calculoI));
		assertTrue(ffc.getPreRequisitos().contains(vetorial));
		assertFalse(ffc.getPreRequisitos().contains(calculoII));
	}
	
	@Test
	public void adicionaPosRequisito(){
		calculoII.addPreRequisito(calculoI);
		assertTrue(calculoI.getPosRequisitos().contains(calculoII));
		
		ffc.addPreRequisito(calculoI);	
		ffc.addPreRequisito(vetorial);
		assertTrue(calculoI.getPosRequisitos().contains(ffc));
		assertTrue(vetorial.getPosRequisitos().contains(ffc));
		assertFalse(calculoII.getPosRequisitos().contains(ffc));
	}
	
	@Test
	public void transformaEmString(){
		assertEquals("3, \"Cálculo I\", 4, 7, 1", calculoI.toString());
		assertEquals("4, \"Algebra Vetorial\", 4, 3, 1", vetorial.toString());
		assertNotEquals("3, \"Cálculo I\", 4, 7, 1", ffc.toString());
		assertNotEquals("1, \"Fund. de Física Clássica\", 1, 1, 1", ffc.toString());
	}

}
