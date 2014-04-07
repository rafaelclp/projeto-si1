package controllers;

import models.CadastroDeUsuario;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Classe de "autenticação".
 */
public class Secured extends Security.Authenticator {

	/**
	 * Realiza algumas checagens de usuário. Caso não falhe
	 * em nenhuma, retorna o nome de usuário logado.
	 * 
	 * @param ctx Contexto que contém a sessão.
	 * 
	 * @return Nome de usuário logado, ou null.
	 */
    @Override
    public String getUsername(Context ctx) {
    	CadastroDeUsuario cadastroDeUsuario = new CadastroDeUsuario();
    	String usuario = ctx.session().get("usuario");
    	if (usuario != null && !cadastroDeUsuario.existeUsuario(usuario)) {
    		usuario = null;
    	}
    	if (usuario != null && Application.controladorDeCadastro.getUsuarioLogado() == null) {
    		usuario = null;
    	}
    	if (usuario != null) {
    		Application.inicializarControladorDeGrade();
    	}
    	return usuario;
    }

    /**
     * Em caso de não estar autorizado, retorna um redirecionamento
     * para a página de login (indexLogin).
     * 
     * @param ctx Contexto que contém a sessão [ignorado].
     * 
     * @return Página com o redirecionamento.
     */
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.indexLogin());
    }
}