package controllers;

import java.util.List;

import models.Disciplina;
import models.Grade;
import models.InvalidOperationException;
import models.TipoDeGrade;
import play.db.ebean.Model;
import play.mvc.Controller;

public class ControladorDeGrade extends Controller {
	private Grade grade;
	private Model saveEntity;

	/**
	 * Construtor genérico
	 * 
	 * @param grade Grade na qual o controlador executará as operações.
	 * @param saveEntity Entidade usada para salvar mudanças na grade.
	 * 			Neste caso, provavelmente será o usuário que a possui.
	 */
	public ControladorDeGrade(Grade grade, Model saveEntity) {
		this.grade = grade;
		grade.carregarDisciplinas();
		this.saveEntity = saveEntity;
	}

	/**
	 * Obtém uma lista dos pré-requisitos não alocados para certa disciplina.
	 * Pré-requisitos alocados depois de certo período são considerados não
	 * alocados.
	 * 
	 * @param id Identificador da disciplina.
	 * @param periodo Período a partir do qual os requisitos são considerados
	 * 			não alocados.
	 * @return A lista de pré-requisitos não alocados.
	 * 
	 * @Flash erro: se o id for inválido.
	 * @Flash ids: contendo uma lista com os pré-requisitos não alocados.
	 */
	public List<Disciplina> obterPreRequisitosNaoAlocados(Long id, int periodo) {
		List<Disciplina> preRequisitosNaoAlocados = null;
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			preRequisitosNaoAlocados = grade.preRequisitosFaltando(disciplina, periodo);
			flash("ids", preRequisitosNaoAlocados);
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
    	return preRequisitosNaoAlocados;
    }

	/**
	 * Obtém uma lista dos pós-requisitos alocados para certa disciplina.
	 * 
	 * @param id Identificador da disciplina.
	 * @return A lista de pós-requisitos alocados.
	 * 
	 * @Flash erro: se o id for inválido.
	 * @Flash ids: contendo uma lista com os pós-requisitos alocados.
	 */
	public List<Disciplina> obterPosRequisitosAlocados(Long id) {
		List<Disciplina> posRequisitosAlocados = null;
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			posRequisitosAlocados = grade.posRequisitosAlocados(disciplina);
			flash("ids", posRequisitosAlocados);
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
    	
    	return posRequisitosAlocados;
    }

	/**
	 * Tenta alocar uma disciplina em um período.
	 * 
	 * @param id Identificador numérico da disciplina a ser alocada.
	 * @param periodo Número do período onde a disciplina será alocada.
	 * 
	 * @Flash erro: se o id seja inválido ou não possa alocar.
	 * @Flash alocar: com "id,periodo", se conseguir alocar.
	 */
	public void alocarDisciplina(Long id, int periodo) {
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			grade.associarDisciplinaAoPeriodo(disciplina, periodo);
			saveEntity.save();
			flash("alocar", id + "," + periodo);
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
	}

	/**
	 * Tenta desalocar uma disciplina pelo id.
	 * 
	 * @param id Identificador da disciplina a ser desalocada.
	 * 
	 * @Flash erro: caso o id seja inválido ou já esteja desalocado.
	 * @Flash desalocar: lista com as disciplinas desalocadas, se conseguiu.
	 */
	public void desalocarDisciplina(Long id) {
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			List<Disciplina> disciplinasDesalocadas = grade.posRequisitosAlocados(disciplina);
			disciplinasDesalocadas.add(disciplina);
			grade.desalocarDisciplina(disciplina);
			saveEntity.save();
			flash("desalocar", disciplinasDesalocadas);
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
	}

	/**
	 * Tenta mover uma disciplina para outro período.
	 * 
	 * @param id Identificador da disciplina a ser movida.
	 * @param periodo Período para o qual deve ser movida.
	 * 
	 * @Flash erro: se o id for inválido ou não conseguir mover.
	 * @Flash irregulares: lista de ids das disciplinas irregulares após mover.
	 */
	public void moverDisciplina(Long id, int periodo) {
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			grade.associarDisciplinaAoPeriodo(disciplina, periodo);
			saveEntity.save();
			flash("irregulares", grade.obterDisciplinasIrregulares());
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
    }

	/**
	 * Altera o período que está sendo cursado.
	 * 
	 * @param periodo Período que está sendo cursado.
	 * 
	 * @Flash periodoCursando: com o período que está sendo cursado.
	 */
	public void alterarPeriodoCursando(int periodo) {
		grade.setPeriodoCursando(periodo);
		saveEntity.save();
		flash("periodoCursando", ""+periodo);
	}

	/**
	 * Reseta a grade para outro tipo de grade.
	 *
	 * @param tipoDeGrade Novo tipo de grade.
	 */
	private void resetarGrade(TipoDeGrade tipoDeGrade) {
		grade.setTipoDeGrade(tipoDeGrade);
		grade.resetar();
		saveEntity.save();
	}

	/**
	 * Reseta a grade para outro tipo de grade.
	 * 
	 * @param tipoDeGrade Novo tipo de grade.
	 * 
	 * @Flash erro: se o tipo de grade informado não existe.
	 */
	public void resetarGrade(int tipoDeGrade) {
		try {
			resetarGrade(TipoDeGrade.values()[tipoDeGrade]);
		} catch (IndexOutOfBoundsException e) {
			// Só acontece se algum usuário tentar "hackear" o sistema;
			// a interface em si não permite chegar aqui.
			flash("erro", "O tipo de grade informado não existe.");
		}
	}
	
	/**
	 * Expande o método flash para aceitar (String, List<Disciplina>).
	 * As disciplinas são transformadas em uma string com uma lista
	 * de seus ids, no formato id1,id2,...,idN.
	 * 
	 * @param chave Chave (do flash) em que será salva a lista.
	 * @param disciplinas Lista de disciplinas a serem salvas.
	 */
	private static void flash(String chave, List<Disciplina> disciplinas) {
		String valor = "";
		if (disciplinas != null) {
			for (Disciplina d : disciplinas) {
				valor += d.getId() + ",";
			}
		}
		if (valor.length() > 0) {
			valor = valor.substring(0, valor.length() - 1);
		}
		flash(chave, valor);
	}

}
