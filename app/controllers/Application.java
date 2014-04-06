package controllers;

import models.CadastroDeUsuario;
import models.CarregadorDeDisciplinas;
import models.InvalidOperationException;
import models.TipoDeGrade;
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
    
    private static void registrarUsuario(String nome, String usuario, String senha) {
    	try {
			Usuario u = (new CadastroDeUsuario()).registrar(nome, usuario, senha);
			u.getGrade().carregarDisciplinas();
			u.getGrade().gerarPlanoAleatorio();
			u.getGrade().save();
		} catch (InvalidOperationException e) {
			e.printStackTrace();
		}
    }
    
    public static Result inicializarUsuarios() {
    	if (!(new CadastroDeUsuario()).existeUsuario("usuario")) {
    		registrarUsuario("Usuario", "usuario", "usuario");
    		registrarUsuario("Rafael Perrella", "rafaelclp", "rafael");
    		registrarUsuario("Remy De Fru", "dfremy", "remydf");
    		registrarUsuario("Bilbo Bolseiro", "bilbo", "12345678");
    		registrarUsuario("Samwise Gamgee", "sam", "12345678");
    		registrarUsuario("Galadriel", "driel", "12345678");
    		registrarUsuario("Frodo Bolseiro", "frodo", "12345678");
    		registrarUsuario("Boromir", "boromir", "12345678");
    		registrarUsuario("Smaug", "smaug", "12345678");
    		registrarUsuario("Legolas", "legolas", "12345678");
    		registrarUsuario("Gollum", "smeagol", "12345678");
    		registrarUsuario("Azog", "azog", "12345678");
    		registrarUsuario("Saruman", "contact", "12345678");
    		registrarUsuario("Gandalf", "gandalf", "12345678");
    		registrarUsuario("Radagast", "radagast", "12345678");
    		registrarUsuario("Aragorn", "aragorn", "12345678");
    		registrarUsuario("Faramir", "faramir", "12345678");
    		registrarUsuario("Celeborn", "celeborn", "12345678");
    		registrarUsuario("Peregrin Tuk", "pippin", "12345678");
    		registrarUsuario("Meriadoc Brandybuck", "merry", "12345678");
    		registrarUsuario("Belladonna Tuk", "bella", "12345678");
    		registrarUsuario("Sauron", "sauron", "12345678");
    		registrarUsuario("Beorn", "beorn", "12345678");
    		registrarUsuario("Balin", "balin", "12345678");
    		registrarUsuario("Thorin", "thorin", "12345678");
    		registrarUsuario("Bifur", "bifur", "12345678");
    		registrarUsuario("Bombur", "bombur", "12345678");
    		registrarUsuario("Bofur", "bofur", "12345678");
    		registrarUsuario("Dori", "dori", "12345678");
    		registrarUsuario("Durin", "durin", "12345678");
    		registrarUsuario("Fili", "fili", "12345678");
    		registrarUsuario("Kili", "kili", "12345678");
    		registrarUsuario("Gimli", "gimli", "12345678");
    		registrarUsuario("Ori", "ori", "12345678");
    		registrarUsuario("Fangorn", "fangorn", "12345678");
    		registrarUsuario("Barbarvore", "barbarvore", "12345678");
    		registrarUsuario("Fimbrethil", "fimbrethil", "12345678");
    		registrarUsuario("Tolkien", "tolkien", "12345678");
    		registrarUsuario("Iluvatar", "god", "12345678");
        	return ok(views.html.response.render("Usuários inicializados."));
    	}
    	return ok(views.html.response.render("Os usuários já foram inicializados..."));
    }
}
