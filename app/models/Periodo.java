package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

/**
 * Classe que representa um periodo de um aluno
 * 
 */
@Entity
public class Periodo extends Model {
	private static final long serialVersionUID = 18217409829147L;

	@Id
	private int id;
	
	@ManyToMany
	private List<Disciplina> disciplinas;

	final int CREDITOS_MINIMO = 12;
	final int CREDITOS_MAXIMO = 28;

	/**
	 * Cria um novo periodo
	 */
	public Periodo() {
		disciplinas = new ArrayList<Disciplina>();
	}

	/**
	 * Retorna id do periodo
	 * 
	 * @return int do id do periodo
	 */
	public int getId() {
		return id;
	}

	/**
	 * Atribui um id ao periodo
	 * 
	 * @param id
	 * 			do periodo
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Retorna todas as disciplinas do periodo
	 * 
	 * @return List com todas as disciplinas do periodo
	 */
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}

	/**
	 * Aloca disciplina no periodo
	 * 
	 * @param disciplina
	 * 			a ser inserida
	 * @param ignorarCreditos
	 * 			se deve ser considerado o limite de credito para esta alocacao
	 * 
	 * @throws InvalidOperationException se não puder alocar
	 */
	public void alocarDisciplina(Disciplina disciplina, boolean ignorarCreditos) throws InvalidOperationException {
		if (!podeAlocar(disciplina, ignorarCreditos)) {
			throw new InvalidOperationException("Disciplina não pode ser alocada neste período.");
		}
		this.disciplinas.add(disciplina);
	}

	/**
	 * Desaloca disciplina solicitada, se possivel
	 * 
	 * @param disciplina
	 *            a ser removida
	 * @param ignorarCreditos
	 * 			se deve ser considerado o limite de credito para esta desalocacao
	 * 
	 * @throws InvalidOperationException se não puder desalocar
	 */
	public void desalocarDisciplina(Disciplina disciplina, boolean ignorarCreditos) throws InvalidOperationException {
		if (!podeDesalocar(disciplina, ignorarCreditos)) {
			throw new InvalidOperationException("Disciplina não pode ser desalocada deste período.");
		}
		if (!disciplinas.remove(disciplina)) {
			throw new InvalidOperationException("Disciplina não existente neste período.");
		}
	}
	
	/**
	 * Retorna true caso o periodo possua a disciplina especificada
	 * 
	 * @param disciplina
	 *            a qual presenca sera testada no periodo
	 * @return se a lista possui a disciplina especificada
	 */
	public boolean contains(Disciplina disciplina) {
		return this.disciplinas.contains(disciplina);
	}
	
	/**
	 * Calcula total de creditos do periodo
	 * 
	 * @return total de creditos do periodo
	 */
	public int totalDeCreditos() {
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
	 * @param ignorarCreditos
	 * 			se deve ser considerado o limite de credito para esta alocacao
	 * 
	 * @return boolean informado se pode ou nao alocar a disciplina
	 */
	public boolean podeAlocar(Disciplina disciplina, boolean ignorarCreditos) {
		if (!ignorarCreditos && this.totalDeCreditos() + disciplina.getCreditos() > CREDITOS_MAXIMO) {
			return false;
		}
		return true;
	}
	
	/**
	 * Verifica se a disciplina pode ser desalocada deste periodo
	 * @param disciplina 
	 * 			a ser desalocada
	 * @param ignorarCreditos
	 * 			se deve ser considerado o limite de credito para esta desalocacao
	 * 
	 * @return boolean informado se pode ou nao desalocar a disciplina
	 */
	public boolean podeDesalocar(Disciplina disciplina, boolean ignorarCreditos) {
		if (!ignorarCreditos && this.totalDeCreditos() - disciplina.getCreditos() < CREDITOS_MINIMO) {
			return false;
		}
		return true;
	}
	
	/**
	 * Verifica se o periodo tem mais creditos do que o maximo permitido
	 * 
	 * @return boolean informando se o periodo tem mais credito que o maximo
	 */
	public boolean passouDoLimiteDeCreditos() {
		return CREDITOS_MAXIMO < totalDeCreditos();
	}

	/**
	 * Reseta o periodo
	 * 
	 */
	public void resetar() {
		disciplinas.clear();
	}

}
