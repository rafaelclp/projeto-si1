package models;

import static org.junit.Assert.*;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.start;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import play.test.FakeApplication;

/**
 * Responsável por testar o cadastro de usuário:
 * registrar, logar, obter/pesquisar, etc.
 * 
 * Testa CadastroDeUsuario e Usuario (indiretamente).
 */
public class CadastroDeUsuarioTest {
	private static CadastroDeUsuario c = new CadastroDeUsuario();

    @Before
    public void setUp() {
		FakeApplication app = fakeApplication(inMemoryDatabase());
		start(app);
    }
    
    /**
     * Verifica se o registro está funcionando corretamente.
     */
    @Test
    public void registrar() {
    	// Limites mínimos de tamanho
    	try {
			c.registrar("", "usuario", "usuario");
			fail("Não deveria permitir usuário vazio.");
		} catch (InvalidOperationException e) {
		}
    	
    	try {
			c.registrar("usuario", "", "usuario");
			fail("Não deveria permitir senha vazia.");
		} catch (InvalidOperationException e) {
		}
    	
    	try {
			c.registrar("usuario", "usuario", "");
			fail("Não deveria permitir email vazio.");
		} catch (InvalidOperationException e) {
		}
    	
    	// Limites grandes de tamanho
    	String stringGrande = "";
    	for (int i = 0; i < 128; i++) {
    		stringGrande += "a";
    	}

    	try {
			c.registrar(stringGrande, "usuario", "usuario");
			fail("Não deveria permitir usuário gigante.");
		} catch (InvalidOperationException e) {
		}
    	
    	try {
			c.registrar("usuario", stringGrande, "usuario");
			fail("Não deveria permitir senha gigante.");
		} catch (InvalidOperationException e) {
		}
    	
    	try {
			c.registrar("usuario", "usuario", stringGrande);
			fail("Não deveria permitir email gigante.");
		} catch (InvalidOperationException e) {
		}
    	
    	// Registro válido
    	try {
			c.registrar("usuario", "usuario", "usuario");
		} catch (InvalidOperationException e) {
			fail("Deveria ser possível cadastrar.");
		}

    	// Registro de usuário já existente
    	try {
			c.registrar("nome do usuario", "usuario", "senha");
			fail("Não deveria ser possível cadastrar usuário que já existe.");
		} catch (InvalidOperationException e) {
		}
    }
    
    /**
     * Verifica se os métodos de obter e pesquisar usuário estão
     * funcionando corretamente.
     */
    @Test
    public void obterEpesquisar() {
    	Usuario u = null;

    	try {
    		u = c.registrar("nome qualquer", "usuario", "senhateste");
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir registrar; verifique o teste 'registrar()'.");
    	}

    	// Verificar se usuario existe
    	assertFalse(c.existeUsuario("usuario2"));
    	assertTrue(c.existeUsuario("usuario"));
    	
    	// Obter usuario pelo id
    	try {
    		Usuario u2 = c.obterUsuarioPorId(u.getId());
    		assertNotNull(u2);
			assertEquals(u.getId(), u2.getId());
		} catch (DataNotFoundException e) {
			fail("Deveria ser possível obter o usuário, ele acabou de ser cadastrado.");
		}
    	
    	// Obter usuario por nome de usuario
    	try {
			assertEquals(u.getId(), c.obterUsuarioPorNomeDeUsuario("usuario").getId());
		} catch (DataNotFoundException e) {
			fail("Deveria ser possível obter usuário existente.");
		}
    	
    	try {
			c.obterUsuarioPorNomeDeUsuario("usuario2");
			fail("Não deveria ser possível obter usuário inexistente.");
		} catch (DataNotFoundException e) {
		}
    	
    	// Pesquisar usuario
    	List<Usuario> l = null;
		try {
			l = c.pesquisarUsuarioPorNome("nome qualquer");
		} catch (DataNotFoundException e) {
			fail("Deveria retornar resultados, existe um.");
		}
    	assertEquals(l.size(), 1);
    	assertEquals(l.get(0).getId(), u.getId());
    	
    	try {
			l = c.pesquisarUsuarioPorNome("nome2 qualquer");
			fail("Deveria retornar 0 resultados, mas retornou " + l.size());
		} catch (DataNotFoundException e) {
		}
    }
    
    /**
     * Verifica se é possível logar corretamente.
     */
    @Test
    public void logar() {
    	try {
    		c.registrar("nome qualquer", "usuario", "senhateste");
    	} catch (InvalidOperationException e) {
    		fail("Deveria conseguir registrar; verifique o teste 'registrar()'.");
    	}
    	
    	// Verifica usuário inexistente
    	try {
			c.logar("usuario2", "senhateste");
			fail("Não deveria conseguir logar.");
		} catch (InvalidOperationException e) {
			fail("Não deveria testar a senha antes de buscar usuário.");
		} catch (DataNotFoundException e) {
		}
    	
    	// Verifica senha incorreta
    	try {
			c.logar("usuario", "senhateste2");
			fail("Não deveria conseguir logar.");
		} catch (InvalidOperationException e) {
		} catch (DataNotFoundException e) {
			fail("O usuário existe, não deveria entrar aqui.");
		}
    	
    	// Verifica usuario e senha corretos
    	try {
			c.logar("usuario", "senhateste");
		} catch (InvalidOperationException e) {
			fail("A senha está correta, não deveria entrar aqui.");
		} catch (DataNotFoundException e) {
			fail("O usuário existe, não deveria entrar aqui.");
		}
    }
}
