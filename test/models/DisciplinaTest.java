package models;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

import java.util.List;

import models.Disciplina;

import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

public class DisciplinaTest {

	private Disciplina calculoI;
	private Disciplina calculoII;
	private Disciplina vetorial;
	private Disciplina ffc;

	@Before
	public void setUp() {
		calculoI = new Disciplina("Cálculo I", 4, 7, 1, 3L);
		vetorial = new Disciplina("Algebra Vetorial", 4, 3, 1, 4L);
		calculoII = new Disciplina("Cálculo II", 4, 7, 2, 13L);
		ffc = new Disciplina("Fund. de Física Clássica", 4, 8, 2, 12L);
	}

	@Test
	public void criacaoDisciplina() {
		assertEquals("Cálculo I", calculoI.getNome());
		assertEquals(4, calculoI.getCreditos());
		assertEquals(7, calculoI.getDificuldade());
		assertEquals(1, calculoI.getPeriodoPrevisto());
		assertEquals(3, calculoI.getId().intValue());
		
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
		calculoI.addPosRequisito(calculoII);
		
		assertTrue(calculoI.getPosRequisitos().contains(calculoII));
		
		calculoI.addPosRequisito(ffc);	
		vetorial.addPosRequisito(ffc);
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

	@Test
	public void registraNoBD() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);

		List<Disciplina> resultado = Disciplina.obterTodas();
		assertEquals(resultado.size(), 0);
		Disciplina d = new Disciplina("Algebra", 4, 6, 1, 7L);
		d.save();

		resultado = Disciplina.obterTodas();
		assertNotNull(resultado);
		assertEquals(resultado.size(), 1);
		assertEquals(resultado.get(0).getId().intValue(), 7);
		assertEquals("Algebra", resultado.get(0).getNome());

		d = new Disciplina("Algebra 2", 4, 6, 1, 7L);
		d.update();
		resultado = Disciplina.obterTodas();
		assertNotNull(resultado);
		assertEquals(resultado.size(), 1);
		assertEquals(resultado.get(0).getId().intValue(), 7);
		assertEquals("Algebra 2", resultado.get(0).getNome());
	}
	
	/**
	 * Teste para verificar se não há problema nas relações
	 * dos atributos de Disciplina que as impeçam de serem carregadas
	 * corretamente no banco de dados
	 */
	@Test
	public void carregaNoBD() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);

		CarregadorDeDisciplinas.limparCache();
		List<Disciplina> l = CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL);
		assertEquals(64, l.size());

		List<Disciplina> all = Disciplina.obterTodas();
		assertEquals(l.size(), all.size());
		
		Disciplina programacaoI = null;
		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).getNome().equals("Programação I")) {
				programacaoI = all.get(i);
			}
		}
		assertEquals(4, programacaoI.getPosRequisitos().size());
	}
}
