package controllers;

import models.InvalidOperationException;
import models.Usuario;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
	
    public static Result index() {
    	if (!estaLogado()) {
    		return ok(views.html.index.render(Control.index()));
    	}
    	
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
    	// TODO: armazenar senha hasheada na session em vez de senha normal
    	String usuario = session("usuario");
    	String senha = session("senha");
    	return Control.logar(usuario, senha) == "sucesso";
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
    
    public static Result inicializarUsuarios() {
    	if (!Usuario.existeUsuario("usuario")) {
    		try {
    			Usuario.registrar("Usuario", "usuario", "usuario");
    			Usuario.registrar("Rafael Perrella", "rafaelclp", "rafael");
    			Usuario.registrar("RÚmy De Fru", "dfremy", "remydf");
    			Usuario.registrar("Bilbo Bolseiro", "bilbo", "12345678");
    			Usuario.registrar("Samwise Gamgee", "sam", "12345678");
    			Usuario.registrar("Galadriel", "driel", "12345678");
    			Usuario.registrar("Frodo Bolseiro", "frodo", "12345678");
    			Usuario.registrar("Boromir", "boromir", "12345678");
    			Usuario.registrar("Smaug", "smaug", "12345678");
    			Usuario.registrar("Legolas", "legolas", "12345678");
    			Usuario.registrar("Gollum", "smeagol", "12345678");
    			Usuario.registrar("Azog", "azog", "12345678");
    			Usuario.registrar("Saruman", "contact", "12345678");
    			Usuario.registrar("Gandalf", "gandalf", "12345678");
    			Usuario.registrar("Radagast", "radagast", "12345678");
    			Usuario.registrar("Aragorn", "aragorn", "12345678");
    			Usuario.registrar("Faramir", "faramir", "12345678");
    			Usuario.registrar("Celeborn", "celeborn", "12345678");
    			Usuario.registrar("Peregrin Tuk", "pippin", "12345678");
    			Usuario.registrar("Meriadoc Brandybuck", "merry", "12345678");
    			Usuario.registrar("Belladonna Tuk", "bella", "12345678");
    			Usuario.registrar("Sauron", "sauron", "12345678");
    			Usuario.registrar("Beorn", "beorn", "12345678");
    			Usuario.registrar("Balin", "balin", "12345678");
    			Usuario.registrar("Thorin", "thorin", "12345678");
    			Usuario.registrar("Bifur", "bifur", "12345678");
    			Usuario.registrar("Bombur", "bombur", "12345678");
    			Usuario.registrar("Bofur", "bofur", "12345678");
    			Usuario.registrar("Dori", "dori", "12345678");
    			Usuario.registrar("Durin", "durin", "12345678");
    			Usuario.registrar("Fili", "fili", "12345678");
    			Usuario.registrar("Kili", "kili", "12345678");
    			Usuario.registrar("Gimli", "gimli", "12345678");
    			Usuario.registrar("Ori", "ori", "12345678");
    			Usuario.registrar("Fangorn", "fangorn", "12345678");
    			Usuario.registrar("Barbarvore", "barbarvore", "12345678");
    			Usuario.registrar("Fimbrethil", "fimbrethil", "12345678");
    			Usuario.registrar("Tolkien", "tolkien", "12345678");
    			Usuario.registrar("Iluvatar", "god", "12345678");
    		} catch (InvalidOperationException e) {
    			e.printStackTrace();
    		}
        	return ok(views.html.response.render("Usuários inicializados."));
    	}
    	return ok(views.html.response.render("Os usuários já foram inicializados..."));
    }
}
