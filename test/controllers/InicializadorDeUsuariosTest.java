package controllers;
import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import javax.persistence.PersistenceException;

import models.CadastroDeUsuario;
import models.CarregadorDeDisciplinas;
import models.Usuario;

import org.junit.Before;
import org.junit.Test;

import controllers.InicializadorDeUsuarios;
import play.db.ebean.Model.Finder;
import play.test.FakeApplication;

/**
 * Testa se o inicializador consegue inicializar a lista de usuários.
 */
public class InicializadorDeUsuariosTest {
    @Before
    public void setUp() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);
		
		CarregadorDeDisciplinas.limparCache();
    }

    /**
     * Verifica se inicializa a lista de usuários.
     */
	@Test
	public void test() {
		// O usuário 'usuario' não está cadastrado inicialmente
		CadastroDeUsuario c = new CadastroDeUsuario();
		assertFalse(c.existeUsuario("usuario"));
		
		// Confirma que não há usuários cadastrados
		Finder<Long, Usuario> find = new Finder<Long, Usuario>(Long.class, Usuario.class);
		assertTrue(find.all() == null || find.all().size() == 0);

		// Inicializa, e confirma que 'usuario' foi cadastrado
		InicializadorDeUsuarios i = new InicializadorDeUsuarios();
		i.inicializarUsuarios();
		assertTrue(c.existeUsuario("usuario"));
	
		// Confirma que há 30 ou mais usuários cadastrados
		assertTrue(find.all().size() >= 30);
	}

}
