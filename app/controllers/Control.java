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
    
    /**
     * Retorna os ids dos pre-requisitos faltando e dele mesmo
     * 
     * @param id da disciplina
     * @param periodo em que se quer alocar a disciplina
     * @return string com a resposta da requisicao
     */
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
     * Aloca uma determinada disciplina em um determinado periodo
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
    
    /**
     * Retorna os ids dos pos-requisitos alocados e dele mesmo
     * 
     * @param id da disciplina
     * @return string com a resposta da requisicao
     */
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
     * Desaloca uma determinada disciplina
     * 
     * @param id
     *            da disciplina
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
    
    /**
     * Move a disciplina para um periodo
     * 
     * @param id da disciplina
     * @param periodo para onde a disciplina sera movida
     * @return string com a resposta da requisicao
     */
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

    /**
     * Altera o periodo que o aluno esta cursando
     * 
     * @param periodo que o aluno esta cursando
     * @returnstring com a resposta da requisicao
     */
	public static String alterarPeriodoCursando(int periodo) {
		grade.setPeriodoCursando(periodo);
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
    
    /**
     * Atribui um usuario a grade
     * 
     * @param usuario atribuido
     */
    public static void setUsuario(Usuario usuario) {
    	Control.usuario = usuario;
    	Control.grade = Control.usuario.getGrade();
    }
    
    /**
     * Retorna o usuario da grade
     * 
     * @return usuario da grade
     */
    public static Usuario getUsuario() {
    	return usuario;
    }
    
    /**
     * Retorna o periodo que o aluno esta cursando
     * 
     * @return periodo sendo cursado
     */
    public static int getPeriodoCursando() {
    	return grade.getPeriodoCursando();
    }

}