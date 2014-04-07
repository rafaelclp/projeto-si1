package controllers;

import java.util.ArrayList;
import java.util.List;

import models.CadastroDeUsuario;
import models.DataNotFoundException;
import models.InvalidOperationException;
import models.TipoDeGrade;
import models.Usuario;
import play.mvc.Controller;

public class ControladorDeCadastro extends Controller {
	private final static CadastroDeUsuario cadastroDeUsuario = new CadastroDeUsuario();
	private Usuario usuarioLogado;

	/**
	 * Obtém o usuário logado.
	 * 
	 * @return Referência para o usuário logado.
	 */
	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	/**
	 * Altera o usuário logado e salva na sessão.
	 * 
	 * @param usuarioLogado Usuário logado.
	 */
	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
		session("usuario", usuarioLogado.getUsuario());
		this.usuarioLogado.getGrade().carregarDisciplinas();
	}

	/**
	 * Desloga (limpa a sessão).
	 */
	public void deslogar() {
		session().clear();
	}

	/**
	 * Tenta logar obtendo usuário e senha de um formulário HTML.
	 */
	public void logar() {
		if (request().body().asFormUrlEncoded() != null) {
    		String[] usuario = request().body().asFormUrlEncoded().get("usuario");
    		String[] senha = request().body().asFormUrlEncoded().get("senha");
    		
    		if (usuario != null && senha != null) {
    			logar(usuario[0], senha[0]);
    		}
    	}
	}

	/**
	 * Tenta logar um usuário.
	 * 
	 * @param usuario Nome de usuário que deve ser logado.
	 * @param senha Senha a ser testada.
	 */
	private void logar(String usuario, String senha) {
    	try {
    		setUsuarioLogado(cadastroDeUsuario.logar(usuario, senha));
    		flash("sucesso", "Logado com sucesso.");
    	} catch (InvalidOperationException e) {
    		flash("erro", e.getMessage());
    	} catch (DataNotFoundException e) {
    		flash("erro", e.getMessage());
    	}
	}
   
    /**
     * Tenta cadastrar obtendo os dados do cadastro de um formulário HTML.
     */
	public void registrar() {
		if (request().body().asFormUrlEncoded() != null) {
			String[] nome = request().body().asFormUrlEncoded().get("nome");
    		String[] usuario = request().body().asFormUrlEncoded().get("usuario");
    		String[] senha = request().body().asFormUrlEncoded().get("senha");
    		String[] tipoDeGrade = request().body().asFormUrlEncoded().get("tipoDeGrade");
    		
    		if (nome != null && usuario != null && senha != null && tipoDeGrade != null) {
    			try {
    				TipoDeGrade tipo = TipoDeGrade.values()[Integer.parseInt(tipoDeGrade[0])];
    				registrar(nome[0], usuario[0], senha[0], tipo);
    			} catch (NumberFormatException e) {
    				// Só acontece se algum usuário tentar "hackear" o sistema;
    				// a interface em si não permite chegar aqui.
    				flash("erro", "Deve ser especificado um número inteiro para o tipo de grade.");
    			} catch (IndexOutOfBoundsException e) {
    				// Mesma situação do catch acima.
    				flash("erro", "O tipo de grade informado não existe.");
    			}
    		}
		}
	}
	
	/**
	 * Tenta cadastrar um usuário.
	 * 
	 * @param nome Nome real do usuário.
	 * @param usuario Nome de usuário.
	 * @param senha Senha do usuário.
	 * @param tipoDeGrade Tipo da grade usada no seu plano.
	 */
	private void registrar(String nome, String usuario, String senha, TipoDeGrade tipoDeGrade) {
    	try {
    		Usuario u = cadastroDeUsuario.registrar(nome, usuario, senha);
    		u.getGrade().setTipoDeGrade(tipoDeGrade);
    		setUsuarioLogado(u);
    		flash("sucesso", "Cadastrado com sucesso.");
    	} catch (InvalidOperationException e) {
    		flash("erro", e.getMessage());
    	}
	}

	/**
	 * Obtém um usuário pelo id.
	 * 
	 * @param id Identificador numérico do usuário.
	 * @return O usuário cujo identificador é "id", ou null.
	 */
	public Usuario obterUsuario(Long id) {
		Usuario u = null;
		try {
			u = cadastroDeUsuario.obterUsuarioPorId(id);
		} catch (DataNotFoundException e) {
			flash("erro", "O id informado é desconhecido.");
		}
		return u;
	}

	/**
	 * Pesquisa usuários por nome e retorna lista com os encontrados.
	 * 
	 * @param query Query a ser buscada.
	 * @return Lista de usuários encontrados.
	 */
	public List<Usuario> pesquisarUsuarios(String query) {
		List<Usuario> l = new ArrayList<Usuario>();
		try {
			l = cadastroDeUsuario.pesquisarUsuarioPorNome(query);
		} catch (DataNotFoundException e) {
			flash("erro", "A busca não retornou nenhum resultado.");
		}
		return l;
	}
}
