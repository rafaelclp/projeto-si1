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
	private final static ControladorDeGrade controladorDeGrade = new ControladorDeGrade();
	
	/**
	 * Cadastra um usuário com um plano de curso aleatório.
	 * 
	 * @param nome Nome real do usuário.
	 * @param usuario Nome de usuário.
	 * @param senha Senha do usuário.
	 */
	private void registrar(String nome, String usuario, String senha) {
		int tipoDeGradeIndex = Math.abs((new Random()).nextInt()) % TipoDeGrade.values().length;
		TipoDeGrade tipoDeGrade = controladorDeGrade.converterInteiroParaTipoDeGrade(tipoDeGradeIndex);

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
		//%RegisterCode(2)%//
	}
}