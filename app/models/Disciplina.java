package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import play.db.ebean.Model;

/**
 * Entidade responsável por armazenar os dados de uma disciplina.
 */
@Entity
public class Disciplina extends Model {
	private static final long serialVersionUID = 4983178414872139L;

	@Id
	private Long id;
	
	private String nome;
	private int creditos;
	private int dificuldade;
	private int periodoPrevisto;

	@ManyToMany
	@JoinTable(name="disciplinas_preRequisitos", joinColumns=@JoinColumn(name="disciplina_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="prerequisito_id", referencedColumnName="id"))
	private List<Disciplina> preRequisitos;
	
	@ManyToMany
	@JoinTable(name="disciplinas_posRequisitos", joinColumns=@JoinColumn(name="disciplina_id", referencedColumnName="id"),
			inverseJoinColumns=@JoinColumn(name="posrequisito_id", referencedColumnName="id"))
	private List<Disciplina> posRequisitos;

	/**
	 * Cria uma nova disciplina
	 * 
	 * @param nome Nome da disciplina a ser criada.
	 * @param creditos Créditos que a disciplina vale.
	 * @param dificuldade Dificuldade (especulada) da disciplina.
	 * @param periodoPrevisto Período em que a disciplina deveria ser cursada.
	 * @param id Id numérico da disciplina (deve ser único para cada disciplina).
	 */
	public Disciplina(String nome, int creditos, int dificuldade, int periodoPrevisto, Long id) {
		setNome(nome);
		setCreditos(creditos);
		setDificuldade(dificuldade);
		setPeriodoPrevisto(periodoPrevisto);
		setId(new Long(id));
		
		this.preRequisitos = new ArrayList<Disciplina>();
		this.posRequisitos = new ArrayList<Disciplina>();
	}

	/**
	 * Obtém o nome da disciplina.
	 * 
	 * @return Nome da disciplina
	 */
	public String getNome() {
		return this.nome;
	}
	
	/**
	 * Atribui um nome à disciplina.
	 * 
	 * @param nome Nome da disciplina
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Obtém o ID da disciplina.
	 * 
	 * @return Identificador numérico da disciplina.
	 */
	public Long getId(){
		return this.id;
	}
	
	/**
	 * Atribui um id à disciplina.
	 * 
	 * @param id Identificador numérico da disciplina.
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Obtém a dificuldade da disciplina.
	 * 
	 * @return Dificuldade da disciplina
	 */
	public int getDificuldade(){
		return this.dificuldade;
	}
	
	/**
	 * Atribui uma dificuldade à disciplina.
	 * 
	 * @param dificuldade Dificuldade da disciplina.
	 */
	public void setDificuldade(int dificuldade) {
		this.dificuldade = dificuldade;
	}

	/**
	 * Obtém a quantidade de créditos da disciplina.
	 * 
	 * @return Quantidade de créditos da disciplina.
	 */
	public int getCreditos() {
		return this.creditos;
	}
	
	/**
	 * Atribui os créditos da disciplina
	 * 
	 * @param creditos Créditos da disciplina.
	 */
	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
	
	/**
	 * Obtém o período previsto para disciplina ser cursada.
	 * 
	 * @return Período previsto para disciplina ser cursada.
	 */
	public int getPeriodoPrevisto() {
		return periodoPrevisto;
	}
	
	/**
	 * Atribui o periodo previsto da disciplina.
	 * 
	 * @param periodoPrevisto Período previsto da disciplina.
	 */
	public void setPeriodoPrevisto(int periodoPrevisto) {
		this.periodoPrevisto = periodoPrevisto;
	}
	
	/**
	 * Obtém a lista dos pré-requisitos da disciplina.
	 * 
	 * @return Lista com os pré-requisitos.
	 */
	public List<Disciplina> getPreRequisitos() {
		return preRequisitos;
	}

	/**
	 * Adiciona uma disciplina à lista de pré-requisitos desta disciplina.
	 * 
	 * @param disciplina Disciplina a ser adicionada à lista de requisitos.
	 */
	public void addPreRequisito(Disciplina disciplina) {
		this.preRequisitos.add(disciplina);
	}

	/**
	 * Obtém a lista dos pos-requisitos da disciplina.
	 * 
	 * @return Lista com os pos-requisitos.
	 */
	public List<Disciplina> getPosRequisitos() {
		return posRequisitos;
	}
	
	/**
	 * Adiciona uma disciplina à lista de pos-requisitos desta disciplina.
	 * 
	 * @param disciplina Disciplina a ser adicionada à lista de requisitos.
	 */
	public void addPosRequisito(Disciplina disciplina) {
		this.posRequisitos.add(disciplina);
	}

	@Override
	public String toString() {
		return id + ", \"" + nome
                + "\", " + creditos + ", "
                + dificuldade + ", "
                + periodoPrevisto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Disciplina other = (Disciplina) obj;
		if (creditos != other.creditos) {
			return false;
		}
		if (dificuldade != other.dificuldade) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		if (periodoPrevisto != other.periodoPrevisto) {
			return false;
		}
		return true;
	}
}
