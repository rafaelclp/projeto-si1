package controllers;

import java.util.List;

import models.Disciplina;
import models.Grade;
import models.InvalidOperationException;
import models.Usuario;

public class Control {
    
    private static Usuario usuario;
    private static Grade grade;

    /**
     * Responsavel pela message do index
     * 
     * @return a lista de disciplinas em formato JSON
     */
    // CONTROLLER: Funcionalidade pro usuario
    public static String index() {
    	if (grade == null) {
    		return "[]"; // grade vazia
    	}
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
     *            da resposta (erro, alocar, desalocar, ...)
     * @param parametros
     *            lista de parametros da resposta separados por virgula
     * @return tipo:parametros
     */
    // PURE FABRICATION: Usado para formatar as respostas das requisições
    private static String montarResposta(String tipo, String parametros) {
    	return tipo + ":" + parametros;
    }
    
    /**
     * Monta uma resposta para uma requisicao, no formato tipo
     *
     * @param tipo
     * 			da resposta
     * @return tipo
     */
    private static String montarResposta(String tipo) {
    	return tipo;
    }
    
    /**
     * Atribui um usuario a grade
     * 
     * @param usuario atribuido
     */
    public static void setUsuario(Usuario usuario) {
    	Control.usuario = usuario;
    	if (usuario != null) {
    		Control.grade = Control.usuario.getGrade();
    	}
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

    /**
     * Loga o aluno
     * 
     * @param usuario do aluno
     * @param senha do aluno
     * @return string com a resposta da requisicao
     */
    public static String logar(String usuario, String senha) {
    	try {
    		setUsuario(null);
    		setUsuario(Usuario.logar(usuario, senha));
    	} catch (InvalidOperationException e) {
    		return montarResposta("erro", e.getMessage());
    	}
    	return montarResposta("sucesso");
    }
    
    /**
     * Registra o aluno
     * 
     * @param nome do aluno
     * @param usuario do aluno
     * @param senha do aluno
     * @return string com a resposta da requisicao
     */
    public static String registrar(String nome, String usuario, String senha) {
    	try {
    		setUsuario(null);
    		setUsuario(Usuario.registrar(nome, usuario, senha));
    	} catch (InvalidOperationException e) {
    		return montarResposta("erro", e.getMessage());
    	}
    	return montarResposta("sucesso");
    }
}