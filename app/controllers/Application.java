package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
	
    public static Result index() {
        return ok(views.html.index.render(Control.index()));
    }
    
    public static Result obterPreRequisitosNaoAlocados(Long id, Long periodo) {
    	return ok(views.html.response.render(Control.obterPreRequisitosNaoAlocados(id.intValue(), periodo.intValue())));    	
    }
	
    public static Result alocarDisciplina(Long id, Long periodo) {
    	return ok(views.html.response.render(Control.alocarDisciplina(id.intValue(), periodo.intValue())));
	}
    
    public static Result obterPosRequisitosAlocados(Long id) {
    	return ok(views.html.response.render(Control.obterPosRequisitosAlocados(id.intValue())));    	
    }
	
    public static Result desalocarDisciplina(Long id) {
    	return ok(views.html.response.render(Control.desalocarDisciplina(id.intValue())));
	}
    
    public static Result moverDisciplina(Long id, Long periodo) {
    	return ok(views.html.response.render(Control.moverDisciplina(id.intValue(), periodo.intValue())));
    }
	
    public static Result resetar() {
    	Control.resetar();
    	return redirect(routes.Application.index());
	}

}
