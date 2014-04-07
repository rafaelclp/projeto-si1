package controllers;

import java.util.List;

import models.Grade;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Controlador geral da aplicação. Responsável por receber as requisições,
 * requisitar a execução das operações aos outros controladores, e devolver
 * a resposta da requisição.
 */
public class Application extends Controller {
	public static ControladorDeCadastro controladorDeCadastro = new ControladorDeCadastro();
	public static ControladorDeGrade controladorDeGrade;

	/**
	 * Página principal caso esteja logado.
	 */
	@Security.Authenticated(Secured.class)
	public static Result index() {
		if (controladorDeCadastro.getUsuarioLogado() == null) {
			return redirect(routes.Application.indexLogin());
		}
    	return ok(views.html.indexLogado.render(controladorDeCadastro.getUsuarioLogado()));
	}

	/**
	 * Página de login. Será redirecionado para cá ao tentar acessar uma
	 * página que requer autenticação sem estar logado.
	 */
	public static Result indexLogin() {
		return ok(views.html.index.render());
	}

	/**
	 * Desloga o usuário.
	 */
	public static Result deslogar() {
		controladorDeCadastro.deslogar();
		return redirect(routes.Application.index());
	}

	/**
	 * [AJAX] Loga um usuário através de formulário HTML.
	 */
	public static Result logar() {
		controladorDeCadastro.logar();
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Registra um usuário através de formulário HTML.
	 */
	public static Result registrar() {
		controladorDeCadastro.registrar();
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * Cria alguns usuários (mais de 30) de uma lista, com planos aleatórios.
	 */
	public static Result inicializarUsuarios() {
		InicializadorDeUsuarios inicializador = new InicializadorDeUsuarios();
		inicializador.inicializarUsuarios();
		return redirect(routes.Application.index());
	}

	/**
	 * Exibe uma página com o perfil (grade) de um usuário.
	 * 
	 * @param id Identificador do usuário que será exibido o perfil.
	 */
	public static Result perfil(Long id) {
		Usuario usuario = controladorDeCadastro.obterUsuario(id);
		usuario.getGrade().carregarDisciplinas();
		return ok(views.html.perfil.render(usuario, controladorDeCadastro.getUsuarioLogado()));
	}

	/**
	 * Realiza uma busca nos usuários por nome [real] e exibe uma página
	 * com os resultados.
	 * 
	 * @param query Query com o nome a ser buscado.
	 */
	public static Result pesquisar(String query) {
		List<Usuario> usuarios = controladorDeCadastro.pesquisarUsuarios(query);
		return ok(views.html.pesquisa.render(usuarios, controladorDeCadastro.getUsuarioLogado()));
	}

	/**
	 * Página com uma lista de todos os usuários cadastrados.
	 */
	public static Result listarUsuarios() {
		List<Usuario> usuarios = controladorDeCadastro.obterTodosOsUsuarios();
		return ok(views.html.listaDeUsuarios.render(usuarios, controladorDeCadastro.getUsuarioLogado()));
	}

	/**
	 * [AJAX] Obtém uma lista de ids dos pré-requisitos não alocados de uma
	 * disciplina, considerando que tal disciplina está no período informado.
	 * 
	 * @param id Identificador da disciplina.
	 * @param periodo Número (1..12) do período.
	 */
	@Security.Authenticated(Secured.class)
	public static Result obterPreRequisitosNaoAlocados(Long id, Integer periodo) {
		controladorDeGrade.obterPreRequisitosNaoAlocados(id, periodo);
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Obtém uma lista de ids dos pré-requisitos não alocados de uma
	 * disciplina, considerando que tal disciplina está no período informado.
	 * 
	 * @param id Identificador da disciplina.
	 * @param periodo Número (1..12) do período.
	 * @param usuarioId Id do usuário que contém a grade a ser acessada.
	 */
	public static Result obterPreRequisitosFaltando(Long id, Integer periodo, Long usuarioId) {
		Usuario u = controladorDeCadastro.obterUsuario(usuarioId);
		if (u != null) {
			Grade g = u.getGrade();
			g.carregarDisciplinas();
			ControladorDeGrade c = new ControladorDeGrade(g, u);
			c.obterPreRequisitosNaoAlocados(id, periodo);
		}
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Obtém uma lista de ids dos pós-requisitos alocados de uma disciplina.
	 * 
	 * @param id Identificador da disciplina.
	 */
	@Security.Authenticated(Secured.class)
	public static Result obterPosRequisitosAlocados(Long id) {
		controladorDeGrade.obterPosRequisitosAlocados(id);
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Tenta alocar uma disciplina.
	 * 
	 * @param id Identificador da disciplina.
	 * @param periodo Número (1..12) do período em que se quer alocar.
	 */
	@Security.Authenticated(Secured.class)
	public static Result alocarDisciplina(Long id, Integer periodo) {
		controladorDeGrade.alocarDisciplina(id,  periodo);
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Tenta desalocar uma disciplina.
	 * 
	 * @param id Identificador da disciplina.
	 */
	@Security.Authenticated(Secured.class)
	public static Result desalocarDisciplina(Long id) {
		controladorDeGrade.desalocarDisciplina(id);
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Tenta mover uma disciplina para outro período.
	 * 
	 * @param id Identificador da disciplina.
	 * @param periodo Número (1..12) do período em que se quer alocar.
	 */
	@Security.Authenticated(Secured.class)
	public static Result moverDisciplina(Long id, Integer periodo) {
		controladorDeGrade.moverDisciplina(id,  periodo);
		return ok(views.html.ajaxResponse.render());
	}

	/**
	 * [AJAX] Altera o período que o usuário está cursando.
	 * 
	 * @param periodo Número (1..12) do novo período.
	 */
	@Security.Authenticated(Secured.class)
	public static Result alterarPeriodoCursando(Integer periodo) {
		controladorDeGrade.alterarPeriodoCursando(periodo);
		return ok(views.html.ajaxResponse.render());
	}
	
	/**
	 * Reseta a grade do usuário.
	 * 
	 * @param tipoDeGrade Número representando o tipo de grade.
	 */
	@Security.Authenticated(Secured.class)
	public static Result resetar(Integer tipoDeGrade) {
		controladorDeGrade.resetarGrade(tipoDeGrade);
		return redirect(routes.Application.index());
	}

	/**
	 * Inicializa o controlador de grade com a grade do usuário logado.
	 */
	public static void inicializarControladorDeGrade() {
		Usuario usuarioLogado = controladorDeCadastro.getUsuarioLogado();
		Grade grade = usuarioLogado.getGrade();
		controladorDeGrade = new ControladorDeGrade(grade, usuarioLogado);
	}
}
