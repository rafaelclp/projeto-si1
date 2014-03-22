package controllers;

import java.util.List;

import model.Disciplina;
import model.Grade;
import model.InvalidOperationException;
import model.Usuario;

public class Control {
    
    private static Usuario usuario = new Usuario("qwerty123", "qwerty123", "qwerty123");
    private static Grade grade = usuario.getGrade();

    /**
     * Responsavel pela message do index
     * 
     * @return a lista de disciplinas em formato JSON
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String index() {
        return grade.toString();
    }
    
    public static String obterPreRequisitosNaoAlocados(int id, int periodo) {
    	String resposta = "" + id;
    	Disciplina disciplina;
		try {
			disciplina = grade.getDisciplinaPorID(id);
		} catch (InvalidOperationException e) {
			return montarResposta("erro", e.getMessage());
		}
    	List<Disciplina> preRequisitosNaoAlocados = grade.preRequisitosFaltando(disciplina, periodo);
    	for (Disciplina d : preRequisitosNaoAlocados) {
    		resposta += "," + d.getId();
    	}
    	return montarResposta("ids", resposta);
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
        Disciplina disciplina = null;
		try {
			disciplina = grade.getDisciplinaPorID(id);
		} catch (InvalidOperationException e) {
			// nunca vai vir aqui
			e.printStackTrace();
		}
		
		try {
			grade.associarDisciplinaAoPeriodo(disciplina, periodo);
		} catch (InvalidOperationException e) {
			return montarResposta("erro", e.getMessage());
		}
		
        resposta = disciplina.getId() + ", " + periodo;
       	resposta = montarResposta("alocar", resposta);
        return resposta;
    }
    
    public static String obterPosRequisitosAlocados(int id) {
    	String resposta = "" + id;
    	Disciplina disciplina;
		try {
			disciplina = grade.getDisciplinaPorID(id);
		} catch (InvalidOperationException e) {
			return montarResposta("erro", e.getMessage());
		}
    	List<Disciplina> posRequisitosAlocados = grade.posRequisitosAlocados(disciplina);
    	for (Disciplina d : posRequisitosAlocados) {
    		resposta += "," + d.getId();
    	}
    	return montarResposta("ids", resposta);
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
    public static String desalocarDisciplina(int id) {
        Disciplina disciplina;
        String resposta = "" + id;
        
        try {
			disciplina = grade.getDisciplinaPorID(id);
		} catch (InvalidOperationException e) {
			return montarResposta("erro", e.getMessage());
		}

        try {
        	grade.desalocarDisciplina(disciplina);
        	List<Disciplina> posRequisitosAlocados = grade.posRequisitosAlocados(disciplina);
        	
        	// Resposta => desalocar:<id1>,<id2>,<...>
	       	for (Disciplina d : posRequisitosAlocados) {
	       		resposta += "," + d.getId();
	       	}
	       	resposta = montarResposta("desalocar", resposta);
        } catch (InvalidOperationException e) {
        	// Resposta => erro:<mensagem de erro>
        	resposta = montarResposta("erro", e.getMessage());
        }
        
        return resposta;
    }
    
    public static String moverDisciplina(int id, int periodo) {
    	Disciplina disciplina;
    	String resposta = "";
    	try {
			disciplina = grade.getDisciplinaPorID(id);
	    	grade.associarDisciplinaAoPeriodo(disciplina, periodo);
		} catch (InvalidOperationException e) {
			return montarResposta("erro", e.getMessage());
		}
    	List<Disciplina> irregulares = grade.obterDisciplinasIrregulares();
    	for (int i = 0; i < irregulares.size(); i++) {
    		if (i > 0) {
    			resposta += ",";
    		}
    		Disciplina d = irregulares.get(i);
    		resposta += d.getId();
    	}
    	return montarResposta("irregulares", resposta);
    }

	public static String alterarPeriodoCursando(int periodo) {
		try {
			grade.setPeriodoCursando(periodo);
		} catch (InvalidOperationException e) {
			return montarResposta("erro", e.getMessage());
		}
		return montarResposta("periodoCursando", "" + periodo);
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
    
    public static void setUsuario(Usuario usuario) {
    	Control.usuario = usuario;
    	Control.grade = Control.usuario.getGrade();
    }
    
    public static Usuario getUsuario() {
    	return usuario;
    }
    
    public static int getPeriodoCursando() {
    	return grade.getPeriodoCursando();
    }

}