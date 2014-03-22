package controllers;

import java.util.List;

import model.Disciplina;
import model.Grade;
import model.InvalidOperationException;
import model.Usuario;

public class Control {
    
    static Usuario usuario = new Usuario("qwerty123", "qwerty123", "qwerty123");
    static Grade grade = usuario.getGrade();

    /**
     * Responsavel pela message do index
     * 
     * @return a lista de disciplinas em formato JSON
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String index() {
        return grade.toString();
    }

    /**
     * Verifica se e possivel alocar uma determinada disciplina em determinado
     * periodo e aloca caso seja
     * 
     * @param id
     *            da disciplina
     * @param periodo
     *            escolhido
     * @return string com a resposta da requisicao
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String alocarDisciplina(int id, int periodo) {
    	String resposta;
        Disciplina disciplina = grade.getDisciplinaPorID(id);
       	if (disciplina == null) {
       		// Resposta => erro:<mensagem de erro>
       		resposta = montarResposta("erro", "Você não pode alocar disciplinas que não existem.");
       	} else {
            resposta = "" + disciplina.getId();
       		// Resposta => erro:<mensagem de erro>
       		List<Disciplina> preRequisitosFaltando = grade.preRequisitosFaltando(disciplina, periodo);
       	    for (Disciplina i : preRequisitosFaltando) {
       	    	resposta += ", " + i.getId();
       	    }
           	resposta = montarResposta("erro", resposta);
       	}
        return resposta;
    }
    
    public static void setUsuario (Usuario usuario) {
    	Control.usuario = usuario;
    	Control.grade = Control.usuario.getGrade();
    }

    /**
     * Verifica se e possivel desalocar uma determinada disciplina e desaloca
     * caso seja
     * 
     * @param id
     *            da disciplina
     * @param force
     *            confirmacao do usuario de forcar a desalocacao
     * @return string com a resposta da requisicao
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String desalocarDisciplina(int id, boolean force) {
        Disciplina disciplina = grade.getDisciplinaPorID(id);
        String resposta = "";

        try {
        	List<Disciplina> disciplinasDesalocadas = grade.desalocarDisciplina(disciplina, force);
        	
        	if (!disciplinasDesalocadas.isEmpty()) {
        		// Resposta => desalocar:<id1>,<id2>,<...>
	        	for (int i = 0; i < disciplinasDesalocadas.size(); i++) {
	        		disciplina = disciplinasDesalocadas.get(i);
	        		if (i > 0) {
	        			resposta += ",";
	        		}
	        		resposta += disciplina.getId();
	        	}
	        	resposta = montarResposta("desalocar", resposta);
        	} else {
        		// Resposta => confirmar:<url caso confirme>,<mensagem da caixa>
        		resposta = "/desalocarDisciplina/" + id + "/true,";
        		resposta += "Ao desalocar esta disciplina, serão desalocadas também estas outras:<br />";
        		List<Disciplina> posRequisitosAlocados = grade.posRequisitosAlocados(disciplina);
        		for (Disciplina i : posRequisitosAlocados) {
        	        resposta += "<span class=\"glyphicon glyphicon-asterisk\" style=\"font-size:10px\"></span> "
        	                + i.getNome() + "<br />";
        	    }
        		resposta = montarResposta("confirmar", resposta);
        	}
        } catch (InvalidOperationException e) {
        	// Resposta => erro:<mensagem de erro>
        	resposta = montarResposta("erro", e.getMessage());
        }
        return resposta;
    }

    /**
     * Reseta todas as alteracoes feitas pelo usuario
     * @throws InvalidOperationException 
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static void resetar() {
        grade.resetar();
    }

    /**
     * Monta uma resposta para uma requisicao, no formato
     * tipo:parametros
     * 
     * @param tipo
     *            da resposta (erro, alocar, desalocar, confirmar)
     * @param parametros
     *            lista de parametros da resposta separados por virgula
     * @return tipo:parametros
     */
    // PURE FABRICATION: Usado para formatar as respostas das requisições
    private static String montarResposta(String tipo, String parametros) {
    	return tipo + ":" + parametros;
    }

}