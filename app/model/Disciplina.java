package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Disciplina extends Model {
	private static final long serialVersionUID = 4983178414872139L;

	@Id
	private int id;
	
	private String nome;
	private int creditos;
	private int dificuldade;
	private int periodoPrevisto;
	
	private List<Disciplina> preRequisitos;
	private List<Disciplina> posRequisitos;
	
	/**
	 * Cria uma nova disciplina com o nome e creditos dados
	 * 
	 * @param nome da disciplina a ser criada
	 * @param creditos que a disciplina possui
	 * @param dificuldade da disciplina
	 * @param periodo previsto para disciplina ser cursada
	 * @param id numero unico para cada disciplina
	 */
	public Disciplina(String nome, int creditos, int dificuldade, int periodoPrevisto, int id) {
		setNome(nome);
		setCreditos(creditos);
		setDificuldade(dificuldade);
		setPeriodoPrevisto(periodoPrevisto);
		setId(id);
		
		this.preRequisitos = new ArrayList<Disciplina>();
		this.posRequisitos = new ArrayList<Disciplina>();
	}

	/**
	 * Retorna nome da disciplina
	 * 
	 * @return String do nome da disciplina
	 */
	public String getNome() {
		return this.nome;
	}
	
	/**
	 * Atribui um nome a Disciplina
	 * 
	 * @param nome
	 * 			Nome da disciplina
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	/**
	 * Retorna ID da disciplina
	 * 
	 * @return int do codigo da disciplina
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * Atribui um id a Disciplina
	 * 
	 * @param id
	 * 			Id da disciplina
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Retorna dificuldade da disciplina
	 * 
	 * @return int com dificuldade da disciplina
	 */
	public int getDificuldade(){
		return this.dificuldade;
	}
	
	/**
	 * Atribui uma dificuldade a Disciplina
	 * 
	 * @param dificuldade
	 * 			Dificuldade da disciplina
	 */
	public void setDificuldade(int dificuldade) {
		this.dificuldade = dificuldade;
	}

	/**
	 * Retorna quantidade de creditos da disciplina
	 * 
	 * @return int com valor de creditos da disciplina
	 */
	public int getCreditos() {
		return this.creditos;
	}
	
	/**
	 * Atribui os creditos da Disciplina
	 * 
	 * @param creditos
	 * 			Creditos da disciplina
	 */
	public void setCreditos(int creditos) {
		this.creditos = creditos;
	}
	
	/**
	 * periodo previsto para disciplina ser cursada
	 * 
	 * @return o periodo previsto para disciplina ser cursada
	 */
	public int getPeriodoPrevisto() {
		return periodoPrevisto;
	}
	
	/**
	 * Atribui o periodo previsto da Disciplina
	 * 
	 * @param periodoPrevisto
	 * 			Periodo previsto da disciplina
	 */
	public void setPeriodoPrevisto(int periodoPrevisto) {
		this.periodoPrevisto = periodoPrevisto;
	}
	
	/**
	 * Retorna Set com todas as disciplinas  que sao pre-requisitos
	 * para a disciplina
	 * 
	 * @return Set com disciplinas
	 */
	public List<Disciplina> getPreRequisitos() {
		return preRequisitos;
	}

	/**
	 * Adiciona disciplina passada como parametro a lista de requisitos
	 * da disciplina(this)
	 * 
	 * @param disciplina 
	 * 			a ser adicionada a lista de requisitos
	 */
	public void addPreRequisito(Disciplina disciplina) {
		this.preRequisitos.add(disciplina);
	}

	/**
	 * Retorna Set com todas as disciplinas  que sao pos-requisitos
	 * para a disciplina
	 * 
	 * @return Set com disciplinas
	 */
	public List<Disciplina> getPosRequisitos() {
		return posRequisitos;
	}
	
	/**
	 * Adiciona disciplina passada como parametro a lista de pos-requisitos
	 * da disciplina(this), disciplinas as quais essa é pre-requisito
	 * 
	 * @param disciplina 
	 * 			a ser adicionada a lista de requisitos
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

	private static Finder<Long, Disciplina> find = new Finder<Long, Disciplina>(Long.class,
			Disciplina.class);
	public static List<Disciplina> obterTodas() {
		return find.all();
	}
}
