package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

/**
 * Entidade responsável por armazenar os dados de um período,
 * geralmente pertencente a uma grade, e realizar operações
 * básicas sobre ele, como alocar/desalocar disciplina.
 */
@Entity
public class Periodo extends Model {
	private static final long serialVersionUID = 18217409829147L;

	@Id
	private Long id = null;
	
	@ManyToMany(cascade=CascadeType.ALL)
	private List<Disciplina> disciplinas;

	private static final int CREDITOS_MAXIMO = 28;
	
	/**
	 * Construtor genérico.
	 */
	public Periodo() {
		resetar();
	}

	/**
	 * Reseta o periodo.
	 */
	public void resetar() {
		disciplinas = new ArrayList<Disciplina>();
	}

	/**
	 * Aloca disciplina no período.
	 * 
	 * @param disciplina Disciplina a ser inserida.
	 * @param ignorarCreditos Se deve ser considerado o limite de créditos para esta alocação.
	 * @throws InvalidOperationException Se não for válido alocar tal disciplina neste período.
	 */
	public void alocarDisciplina(Disciplina disciplina, boolean ignorarCreditos) throws InvalidOperationException {
		if (!podeAlocar(disciplina, ignorarCreditos)) {
			throw new InvalidOperationException("Disciplina não pode ser alocada neste período.");
		}
		this.disciplinas.add(disciplina);
	}

	/**
	 * Desaloca disciplina do período.
	 * 
	 * @param disciplina Disciplina a ser removida.
	 * @throws InvalidOperationException Se não for válido desalocar (disciplina não alocada).
	 */
	public void desalocarDisciplina(Disciplina disciplina) throws InvalidOperationException {
		if (!disciplinas.remove(disciplina)) {
			throw new InvalidOperationException("Disciplina não existente neste período.");
		}
	}

	/**
	 * Informa se o período contém a disciplina.
	 * 
	 * @param disciplina Disciplina que se quer verificar se está alocada.
	 * @return Se o período possui ou não tal disciplina.
	 */
	public boolean contains(Disciplina disciplina) {
		return this.disciplinas.contains(disciplina);
	}
	
	/**
	 * Verifica se a disciplina pode ser alocada neste periodo.
	 * 
	 * @param disciplina Disciplina a ser alocada.
	 * @param ignorarCreditos Se deve ser considerado o limite de créditos para esta alocação.
	 * 
	 * @return Se é possível ou não alocar.
	 */
	public boolean podeAlocar(Disciplina disciplina, boolean ignorarCreditos) {
		if (!ignorarCreditos && totalDeCreditos() + disciplina.getCreditos() > CREDITOS_MAXIMO) {
			return false;
		}
		return true;
	}

	/**
	 * Verifica se o periodo tem mais créditos do que o máximo permitido.
	 * 
	 * @return Se o período tem mais créditos do que o máximo ou não.
	 */
	public boolean passouDoLimiteDeCreditos() {
		return CREDITOS_MAXIMO < totalDeCreditos();
	}
	
	/**
	 * Calcula total de creditos cursados no período.
	 * 
	 * @return Total de creditos do período.
	 */
	public int totalDeCreditos() {
		int creditos = 0;
		
		for (Disciplina i : disciplinas) {
			creditos += i.getCreditos();
		}
		
		return creditos;
	}
	
	/**
	 * Obtém o id do período no bd.
	 * 
	 * @return Identificador do período no bd.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribui um id ao período.
	 * 
	 * @param id Novo identificador do período.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Obtém uma lista com as disciplinas alocadas no período.
	 * 
	 * @return Lista com as disciplinas alocadas.
	 */
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}
}
