package model;

import java.util.ArrayList;

public class Disciplina {

	// Nome
	private String nome;

	// Quantidade de creditos
	private int creditos;

	// ID
	private int id;

	// Dificuldade
	private int dificuldade;

	// Periodo previsto para ser alocada
	private int periodoPrevisto;
	
	// CREATOR: Disciplina contem uma lista dos seus pre-requisitos
	// Lista com todos os pre-requisitos diretos
	private ArrayList<Disciplina> preRequisitos;

	// CREATOR: Disciplina contem uma lista dos seus pos-requisitos
	// Lista com todos os pos-requisitos diretos
	private ArrayList<Disciplina> posRequisitos;
	
	/**
	 * Cria uma nova disciplina com o nome e creditos dados
	 * 
	 * @param nome da disciplina a ser criada
	 * @param creditos que a disciplina possui
	 * @param dificuldade da disciplina
	 * @param periodo previsto para disciplina ser cursada
	 * @param id numero unico para cada disciplina
	 */
	public Disciplina(String nome, int creditos, int dificuldade, int periodo, int id) {
		this.nome = nome;
		this.creditos = creditos;
		this.dificuldade = dificuldade;
		this.periodoPrevisto = periodo;
		this.id = id;

		this.preRequisitos = new ArrayList<Disciplina>();
		this.posRequisitos = new ArrayList<Disciplina>();
	}

	/**
	 * Retorna nome da disciplina
	 * 
	 * @return String do nome da disciplina
	 */
	// INFORMATION EXPERT: Disciplina tem o seu proprio nome
	public String getNome() {
		return this.nome;
	}
	
	/**
	 * Retorna ID da disciplina
	 * 
	 * @return int do codigo da disciplina
	 */
	// INFORMATION EXPERT: Disciplina tem o seu proprio ID
	public int getID(){
		return this.id;
	}
	
	/**
	 * Retorna dificuldade da disciplina
	 * 
	 * @return int com dificuldade da disciplina
	 */
	// INFORMATION EXPERT: Disciplina tem a sua propria dificuldade
	public int getDificuldade(){
		return this.dificuldade;
	}

	/**
	 * Retorna quantidade de creditos da disciplina
	 * 
	 * @return int com valor de creditos da disciplina
	 */
	// INFORMATION EXPERT: Disciplina tem a sua propria quantidade de creditos
	public int getCreditos() {
		return this.creditos;
	}

	/**
	 * Adiciona disciplina passada como parametro a lista de requisitos
	 * da disciplina(this)
	 * 
	 * @param disciplina 
	 * 			a ser adicionada a lista de requisitos
	 */
	// INFORMATION EXPERT: Disciplina contem uma lista com seus pre-requisitos
	public void addPreRequisito(Disciplina disciplina) {
		this.preRequisitos.add(disciplina);
		disciplina.addPosRequisito(this);
	}

	/**
	 * Adiciona disciplina passada como parametro a lista de pos-requisitos
	 * da disciplina(this), disciplinas as quais essa é pre-requisito
	 * 
	 * @param disciplina 
	 * 			a ser adicionada a lista de requisitos
	 */
	// INFORMATION EXPERT: Disciplina contem uma lista com seus pos-requisitos
	public void addPosRequisito(Disciplina disciplina) {
		this.posRequisitos.add(disciplina);
	}

	/**
	 * Retorna Set com todas as disciplinas  que sao pre-requisitos
	 * para a disciplina
	 * 
	 * @return Set com disciplinas
	 */
	// INFORMATION EXPERT: Disciplina contem uma lista com seus pre-requisitos
	public ArrayList<Disciplina> getPreRequisitos() {
		return preRequisitos;

	}

	/**
	 * Retorna Set com todas as disciplinas  que sao pos-requisitos
	 * para a disciplina
	 * 
	 * @return Set com disciplinas
	 */
	// INFORMATION EXPERT: Disciplina contem uma lista com seus pos-requisitos
	public ArrayList<Disciplina> getPosRequisitos() {
		return posRequisitos;

	}
	
	/**
	 * periodo previsto para disciplina ser cursada
	 * 
	 * @return o periodo previsto para disciplina ser cursada
	 */
	// INFORMATION EXPERT: Disciplina tem o seu proprio periodo previsto
	public int getPeriodoPrevisto() {
		return periodoPrevisto;
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
