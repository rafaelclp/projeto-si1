package controllers;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import models.CadastroDeUsuario;
import models.InvalidOperationException;
import models.Usuario;

import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

/**
 * Testa o controlador de cadastro parcialmente.
 * Não é possível testar registro/login sem integration.
 */
public class ControladorDeCadastroTest {
	ControladorDeCadastro c = new ControladorDeCadastro();
	CadastroDeUsuario cadastro = new CadastroDeUsuario();

    @Before
    public void setUp() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);
    }

    /**
     * Responsável por testar a obtenção de uma lista de usuários do bd
     * (obter todos, ou pesquisar).
     */
	@Test
	public void obterListaDeUsuarios() {
		// Confirma que não há usuários no começo
		List<Usuario> l = c.obterTodosOsUsuarios();
		assertEquals(l.size(), 0);

		// Cadastra um usuário e confirma que só há ele cadastrado
		Usuario u = null;
		try {
			u = cadastro.registrar("Nome qualquer", "usuarioTeste", "senhaQualquer");
		} catch (InvalidOperationException e) {
			fail("Deveria ser possível registrar. Teste o CadastroDeUsuarioTest antes.");
		}
		l = c.obterTodosOsUsuarios();
		assertEquals(l.size(), 1);
		assertEquals(l.get(0).getId(), u.getId());

		// Cadastra mais dois usuários e testa
		Usuario u2 = null, u3 = null;
		try {
			u2 = cadastro.registrar("Outro nome", "usuarioTeste2", "senhaQualquer");
			u3 = cadastro.registrar("Ainda outro nome", "usuarioTeste3", "senhaQualquer");
		} catch (InvalidOperationException e) {
			fail("Deveria ser possível registrar. Teste o CadastroDeUsuarioTest antes.");
		}
		l = c.obterTodosOsUsuarios();
		assertEquals(l.size(), 3);
		assertEquals(l.get(1).getId(), u2.getId());
		assertEquals(l.get(2).getId(), u3.getId());

		// Pesquisa pelos usuários
		l = c.pesquisarUsuarios("Nome");
		assertEquals(l.size(), 3);
		
		l = c.pesquisarUsuarios("outro");
		assertEquals(l.size(), 2);
		
		l = c.pesquisarUsuarios("ai nda o");
		assertEquals(l.size(), 1);
	}

	/**
	 * Testa a obtenção de um único usuário.
	 */
	@Test
	public void obterUsuario() {
		// Tenta obter usuário inexistente
		Usuario u = null;
		try {
			u = c.obterUsuario(1L);
			if (u != null) {
				fail("Não deveria ser possível obter usuário inexistente.");
			}
		} catch (Exception e) {
		}
		
		// Cadastra um usuário e confirma que pode ser obtido.
		try {
			u = cadastro.registrar("Nome qualquer", "usuarioTeste", "senhaQualquer");
		} catch (InvalidOperationException e) {
			fail("Deveria ser possível registrar. Teste o CadastroDeUsuarioTest antes.");
		}
		Usuario u2 = c.obterUsuario(u.getId());
		assertNotNull(u2);
		assertEquals(u.getId(), u2.getId());
	}
}
