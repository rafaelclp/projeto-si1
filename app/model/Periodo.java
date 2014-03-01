package model;

import java.util.ArrayList;
import java.util.List;

public class Periodo {

	// CREATOR: Periodo contem uma lista de disciplinas
	// Lista com todas as disciplinas alocadas no periodo
	private ArrayList<Disciplina> disciplinas;

	// Quantidade maxima de creditos no periodo
	final int CREDITOS_MAXIMO = 28;

	public Periodo() {
		disciplinas = new ArrayList<Disciplina>();
	}

	/**
	 * Retorna todas as disciplinas do periodo
	 * 
	 * @return Lista com todas as disciplinas do periodo
	 */
	// INFORMATION EXPERT: Periodo contem uma lista com suas disciplinas
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	/**
	 * Adiciona elemento indicado ao conjunto de disciplinas do periodo
	 * 
	 * @param disciplina
	 *            a ser inserida
	 */
	// INFORMATION EXPERT: Periodo contem uma lista com suas disciplinas
	public void addDisciplina(Disciplina disciplina) {
		this.disciplinas.add(disciplina);
	}

	/**
	 * Retorna true caso o periodo possua a disciplina especificada
	 * 
	 * @param disciplina
	 *            a qual presenca sera testada no periodo
	 * @return true caso a lista possua disciplina especificada
	 */
	// INFORMATION EXPERT: Periodo contem uma lista com suas disciplinas
	public boolean contains(Disciplina disciplina) {
		return this.disciplinas.contains(disciplina);
	}

	/**
	 * Remove disciplina especificada, se presente
	 * 
	 * @param disciplina
	 *            a ser removida
	 */
	// INFORMATION EXPERT: Periodo contem uma lista com suas disciplinas
	public void removeDisciplina(Disciplina disciplina) {
		disciplinas.remove(disciplina);
	}
	
	/**
	 * Calcula quantos creditos o periodo tem
	 * 
	 * @return quantos creditos o periodo tem
	 */
	// INFORMATION EXPERT: Periodo contem uma lista com suas disciplinas
	public int getCreditos() {
		int creditos = 0;
		for (Disciplina i : disciplinas) {
			creditos += i.getCreditos();
		}
		return creditos;
	}
	
	/**
	 * Verifica se a disciplina pode ser alocada neste periodo
	 * @param disciplina 
	 * 			a ser alocada
	 * @return boolean informado se pode ou nao alocar a disciplina
	 */
	// INFORMATION EXPERT: Periodo consegue verificar seus creditos
	public boolean podeAlocar(Disciplina disciplina) {
		if (getCreditos() + disciplina.getCreditos() > CREDITOS_MAXIMO) {
			return false;
		}
		return true;
	}

	/**
	 * Reseta o periodo
	 * 
	 */
	// INFORMATION EXPERT: Periodo contem uma lista com suas disciplinas
	public void resetar() {
		disciplinas.clear();
	}
}
