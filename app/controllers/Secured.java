package controllers;

import models.CadastroDeUsuario;
import play.mvc.Http.Context;
import play.mvc.Result;
import play.mvc.Security;

/**
 * Classe de "autenticação".
 */
public class Secured extends Security.Authenticator {

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

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.indexLogin());
    }
}