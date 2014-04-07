package controllers;

import java.util.List;

import models.Disciplina;
import models.Grade;
import models.InvalidOperationException;
import models.TipoDeGrade;
import play.mvc.Controller;

public class ControladorDeGrade extends Controller {
	private Grade grade;

	/**
	 * Construtor genérico
	 * 
	 * @param grade Grade na qual o controlador executará as operações.
	 */
	public ControladorDeGrade(Grade grade) {
		this.grade = grade;
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
	 */
	public List<Disciplina> obterPreRequisitosNaoAlocados(Long id, int periodo) {
		List<Disciplina> preRequisitosNaoAlocados = null;
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			preRequisitosNaoAlocados = grade.preRequisitosFaltando(disciplina, periodo);
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
	 */
	public List<Disciplina> obterPosRequisitosAlocados(Long id) {
		List<Disciplina> posRequisitosAlocados = null;
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			posRequisitosAlocados = grade.posRequisitosAlocados(disciplina);
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
	 */
	public void alocarDisciplina(Long id, int periodo) {
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			grade.associarDisciplinaAoPeriodo(disciplina, periodo);
			grade.save();
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
	}

	/**
	 * Obtém uma lista das disciplinas desalocadas ao desalocar certa disciplina.
	 * Inclui a própria disciplina.
	 * 
	 * @param disciplina Disciplina que seria desalocada.
	 * @return Lista das disciplinas que seriam desalocadas ao desalocar tal disciplina.
	 */
	public List<Disciplina> obterDisciplinasDesalocadasAoDesalocar(Disciplina disciplina) {
		List<Disciplina> disciplinasDesalocadas = grade.posRequisitosAlocados(disciplina);
		disciplinasDesalocadas.add(disciplina);
		return disciplinasDesalocadas;
	}

	/**
	 * Tenta desalocar uma disciplina pelo id.
	 * 
	 * @param id Identificador da disciplina a ser desalocada.
	 */
	public void desalocarDisciplina(Long id) {
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			grade.desalocarDisciplina(disciplina);
			grade.save();
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
	}

	/** Tenta mover uma disciplina para outro período.
	 * 
	 * @param id Identificador da disciplina a ser movida.
	 * @param periodo Período para o qual deve ser movida.
	 */
	public void moverDisciplina(Long id, int periodo) {
		try {
			Disciplina disciplina = grade.getDisciplinaPorID(id);
			grade.associarDisciplinaAoPeriodo(disciplina, periodo);
			grade.save();
		} catch (InvalidOperationException e) {
			flash("erro", e.getMessage());
		}
    }
	
	/**
	 * Altera o período que está sendo cursado.
	 * 
	 * @param periodo Período que está sendo cursado.
	 */
	public void alterarPeriodoCursando(int periodo) {
		grade.setPeriodoCursando(periodo);
		grade.save();
	}

	/**
	 * Reseta a grade para outro tipo de grade.
	 *
	 * @param tipoDeGrade Novo tipo de grade.
	 */
	private void resetarGrade(TipoDeGrade tipoDeGrade) {
		grade.setTipoDeGrade(tipoDeGrade);
		grade.resetar();
		grade.save();
	}

	/**
	 * Reseta a grade para outro tipo de grade.
	 * 
	 * @param tipoDeGrade Novo tipo de grade.
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
}
