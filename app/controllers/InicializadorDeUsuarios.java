package controllers;

import java.util.Random;

import models.CadastroDeUsuario;
import models.Grade;
import models.InvalidOperationException;
import models.TipoDeGrade;
import models.Usuario;
import play.mvc.Controller;

/**
 * Responsável por inicializar a lista de usuários,
 * cadastrando alguns usuários com planos de curso randômicos.
 *
 * Arquivo gerado [parcialmente] automaticamente.
 */
public class InicializadorDeUsuarios extends Controller {
	private final static CadastroDeUsuario cadastroDeUsuario = new CadastroDeUsuario();
	
	/**
	 * Cadastra um usuário com um plano de curso aleatório.
	 * 
	 * @param nome Nome real do usuário.
	 * @param usuario Nome de usuário.
	 * @param senha Senha do usuário.
	 */
	private void registrar(String nome, String usuario, String senha) {
		int tipoDeGradeIndex = Math.abs((new Random()).nextInt()) % TipoDeGrade.values().length;
		TipoDeGrade tipoDeGrade = TipoDeGrade.values()[tipoDeGradeIndex];

		try {
			Usuario u = cadastroDeUsuario.registrar(nome, usuario, senha);
			Grade g = u.getGrade();
			g.setTipoDeGrade(tipoDeGrade);
			g.gerarPlanoAleatorio();
		} catch (InvalidOperationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cadastra uma lista de usuários com planos de curso aleatórios.
	 * Código gerado automaticamente.
	 */
	public void inicializarUsuarios() {
		if (!cadastroDeUsuario.existeUsuario("usuario")) {
			registrar("Usuario", "usuario", "usuario");
			registrar("Rafael Perrella", "rafaelclp", "rafael");
			registrar("Remy De Fru", "dfremy", "remydf");
			registrar("Bilbo Bolseiro", "bilbo", "12345678");
			registrar("Samwise Gamgee", "sam", "12345678");
			registrar("Galadriel", "driel", "12345678");
			registrar("Frodo Bolseiro", "frodo", "12345678");
			registrar("Boromir", "boromir", "12345678");
			registrar("Smaug", "smaug", "12345678");
			registrar("Legolas", "legolas", "12345678");
			registrar("Gollum", "smeagol", "12345678");
			registrar("Azog", "azog", "12345678");
			registrar("Saruman", "contact", "12345678");
			registrar("Gandalf", "gandalf", "12345678");
			registrar("Radagast", "radagast", "12345678");
			registrar("Aragorn", "aragorn", "12345678");
			registrar("Faramir", "faramir", "12345678");
			registrar("Celeborn", "celeborn", "12345678");
			registrar("Peregrin Tuk", "pippin", "12345678");
			registrar("Meriadoc Brandybuck", "merry", "12345678");
			registrar("Belladonna Tuk", "bella", "12345678");
			registrar("Sauron", "sauron", "12345678");
			registrar("Beorn", "beorn", "12345678");
			registrar("Balin", "balin", "12345678");
			registrar("Thorin", "thorin", "12345678");
			registrar("Bifur", "bifur", "12345678");
			registrar("Bombur", "bombur", "12345678");
			registrar("Bofur", "bofur", "12345678");
			registrar("Dori", "dori", "12345678");
			registrar("Durin", "durin", "12345678");
			registrar("Fili", "fili", "12345678");
			registrar("Kili", "kili", "12345678");
			registrar("Gimli", "gimli", "12345678");
			registrar("Ori", "ori", "12345678");
			registrar("Fangorn", "fangorn", "12345678");
			registrar("Barbarvore", "barbarvore", "12345678");
			registrar("Fimbrethil", "fimbrethil", "12345678");
			registrar("Tolkien", "tolkien", "12345678");
			registrar("Iluvatar", "god", "12345678");
		}
	}
}