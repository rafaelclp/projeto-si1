package controllers;

import java.util.List;

import model.Disciplina;
import model.Grade;
import model.InvalidOperationException;

public class Control {

	// CREATOR: O Control é o responsável pela grade
	// Grade de disciplinas/periodos
    static Grade grade = new Grade();

    /**
     * Responsavel pela message do index
     * 
     * @return a lista de disciplinas em formato JSON
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String index() {
        return grade.disciplinasParaString();
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
        Disciplina disciplina = grade.getDisciplinaPorID(id);
        String resposta;
        
        try {
        	if (disciplina == null) {
        		// Resposta => erro:<mensagem de erro>
        		resposta = montarResposta("erro", "Você não pode alocar disciplinas que não existem.");
        	} else if (grade.associarDisciplinaAoPeriodo(disciplina, periodo)) {
        		// Resposta => alocar:<id>,<periodo>
        		resposta = montarResposta("alocar", id +"," + periodo);
        	} else {
        		// Resposta => erro:<mensagem de erro>
        		List<Disciplina> preRequisitosFaltando = grade.preRequisitosFaltando(disciplina, periodo);
        		resposta = "Pré-requisitos não cumpridos:<br />";
        	    for (Disciplina i : preRequisitosFaltando) {
        	    	resposta += "<span class=\"glyphicon glyphicon-asterisk\" style=\"font-size:10px\"></span> "
        	                + i.getNome() + "<br />";
        	    }
            	resposta = montarResposta("erro", resposta);
        	}
        } catch (InvalidOperationException e) {
        	// Resposta => erro:<mensagem de erro>
        	resposta = montarResposta("erro", e.getMessage());
        }

        return resposta;
    }

    /**
     * Desaloca uma determinada disciplina e seus pos-requisitos alocados
     * 
     * @param id
     *            da disciplina
     * @return string com a resposta da requisicao
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String desalocarDisciplina(int id) {
        Disciplina disciplina = grade.getDisciplinaPorID(id);
        List<Disciplina> disciplinasDesalocadas = grade.posRequisitosAlocados(disciplina);
        String resposta = "" + disciplina.getID();
        // Resposta => desalocar:<id1>,<id2>,<...>
	    for (int i = 0; i < disciplinasDesalocadas.size(); i++) {
	    	disciplina = disciplinasDesalocadas.get(i);
	    	if (i > 0) {
	    		resposta += ",";
	    	}
	    	resposta += disciplina.getID();
	    }
        return montarResposta("desalocar", resposta);
    }

    /**
     * Reseta todas as alteracoes feitas pelo usuario
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

	public static String obterPosRequisitosAlocados(int id) {
		Disciplina disciplina = grade.getDisciplinaPorID(id);
		List <Disciplina> posRequisitosAlocados = grade.posRequisitosAlocados(disciplina);
		String resposta = "" + disciplina.getID();
		for (Disciplina disc : posRequisitosAlocados) {
	        resposta += "," + disc.getID();
		}
		return montarResposta("posRequisitos", resposta);
	}

}