package models;

import static org.junit.Assert.assertEquals;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Test;

import play.test.FakeApplication;

/**
 * Teste direto no carregamento de disciplinas para confirmar a funcionalidade
 * principal do carregador. Testes mais complexos são realizados indiretamente
 * nas outras classes de teste.
 * 
 * Testa CarregadorDeDisciplinas.
 */
public class CarregadorDeDisciplinasTest {

	/**
	 * Tenta carregar disciplinas dos 3 tipos de fluxograma.
	 * Não verifica se salva no banco de dados, mas isso é testado
	 * em outras classes de teste já. A intenção aqui é verificar se
	 * está carregando do arquivo apenas.
	 */
	@Test
	public void carregarDisciplinas() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);
		CarregadorDeDisciplinas.limparCache();

		List<Disciplina> l = CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_OFICIAL);
		assertEquals(l.size(), 64);

		l = CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_MAIS_COMUMENTE_PAGO);
		assertEquals(l.size(), 72);
		
		l = CarregadorDeDisciplinas.carregaDisciplinas(TipoDeGrade.FLUXOGRAMA_VIGENTE_APOS_REFORMA);
		assertEquals(l.size(), 55);
	}

}
