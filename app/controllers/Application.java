package controllers;

import models.Usuario;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
	
    public static Result index() {
    	if (!estaLogado()) {
    		return ok(views.html.index.render(Control.index()));
    	}
    	
    	//return ok(views.html.indexLogado.render(Control.index(), 12, new Usuario("rafaelclp", "rafael", "rafael")));
    	return ok(views.html.indexLogado.render(Control.index(), Control.getPeriodoCursando(), Control.getUsuario()));
    }
    
    public static Result obterPreRequisitosNaoAlocados(Long id, Long periodo) {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	return ok(views.html.response.render(Control.obterPreRequisitosNaoAlocados(id.intValue(), periodo.intValue())));    	
    }
	
    public static Result alocarDisciplina(Long id, Long periodo) {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	return ok(views.html.response.render(Control.alocarDisciplina(id.intValue(), periodo.intValue())));
	}
    
    public static Result obterPosRequisitosAlocados(Long id) {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	return ok(views.html.response.render(Control.obterPosRequisitosAlocados(id.intValue())));    	
    }
	
    public static Result desalocarDisciplina(Long id) {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	return ok(views.html.response.render(Control.desalocarDisciplina(id.intValue())));
	}
    
    public static Result moverDisciplina(Long id, Long periodo) {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	return ok(views.html.response.render(Control.moverDisciplina(id.intValue(), periodo.intValue())));
    }
    
    public static Result alterarPeriodoCursando(Long periodo) {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	return ok(views.html.response.render(Control.alterarPeriodoCursando(periodo.intValue())));
    }
	
    public static Result resetar() {
    	if (!estaLogado()) {
    		return ok(views.html.response.render("semPermissao"));
    	}
    	Control.resetar();
    	return redirect(routes.Application.index());
	}

    private static boolean estaLogado() {
    	return Control.logar(session("usuario"), session("senha")) == "sucesso";
    }

    public static Result deslogar() {
    	session().clear();
    	return redirect(routes.Application.index());
    }
    
    public static Result logar() {
    	String resposta = "";
    	if (request().body().asFormUrlEncoded() != null) {
    		String[] usuario = request().body().asFormUrlEncoded().get("usuario");
    		String[] senha = request().body().asFormUrlEncoded().get("senha");
    		
    		if (usuario != null && senha != null) {
    			resposta = Control.logar(usuario[0], senha[0]);
    			if (resposta == "sucesso") {
    				session("usuario", usuario[0]);
    				session("senha", senha[0]);
    			}
    		}
    	}
    	return ok(views.html.response.render(resposta));
    }

    public static Result registrar() {
    	String resposta = "";
    	if (request().body().asFormUrlEncoded() != null) {
    		String[] nome = request().body().asFormUrlEncoded().get("nome");
    		String[] usuario = request().body().asFormUrlEncoded().get("usuario");
    		String[] senha = request().body().asFormUrlEncoded().get("senha");

    		if (nome != null && usuario != null && senha != null) {
    			resposta = Control.registrar(nome[0], usuario[0], senha[0]);
    			if (resposta == "sucesso") {
    				session("usuario", usuario[0]);
    				session("senha", senha[0]);
    			}
    		}
    	}
    	return ok(views.html.response.render(resposta));
    }
}
