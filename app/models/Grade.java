package models;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import play.db.ebean.Model;


/**
 * 
 * Classe que representa a grade do aluno
 *
 */
@Entity
public class Grade extends Model {
	private static final long serialVersionUID = 1543790286461L;

	private static final int MAXIMO_DE_PERIODOS = 12;
	
	@Id
	private Long id;
	
	private int periodoCursando = 1;
	
	@Transient
	private TipoDeGrade tipoDeGrade = TipoDeGrade.FLUXOGRAMA_OFICIAL;
	
	// CREATOR: Grade contem uma lista com as disciplinas
	// Lista com todas as disciplinas da grade
	@Transient
	private List<Disciplina> disciplinas;

	// CREATOR: Grade contem uma lista de períodos
	// Lista com todos os periodos da grade
	@ManyToMany(cascade=CascadeType.ALL)
	private List<Periodo> periodos;

	/**
	 * Constructor para o Ebean
	 * Inicializa as variaveis e reseta o sistema (aloca primeiro periodo)
	 * @throws InvalidOperationException 
	 */
	public Grade() {
		setPeriodos(new ArrayList<Periodo>());
		this.disciplinas = new ArrayList<Disciplina>();
		//this.disciplinas = CarregadorDeDisciplinas.carregaDisciplinas(tipoDeGrade);
		periodoCursando = 1;

		for (int i = 0; i < MAXIMO_DE_PERIODOS; i++) {
			periodos.add(new Periodo());
		}
	}

	/**
	 * Constructor
	 * Inicializa as variaveis e reseta o sistema (aloca primeiro periodo)
	 * @throws InvalidOperationException 
	 */
	// TODO: refatorar (dois construtores quase idênticos)
	public Grade(List<Disciplina> disciplinas) {
		setPeriodos(new ArrayList<Periodo>());
		this.disciplinas = disciplinas;
		periodoCursando = 1;

		for (int i = 0; i < MAXIMO_DE_PERIODOS; i++) {
			periodos.add(new Periodo());
		}
		resetar();
	}
	
	/**
	 * Gera um plano (grade) aleatório.
	 */
	public void gerarPlanoAleatorio() {
		List<Periodo> periodos = new ArrayList<Periodo>();
		for (int i = 0; i < MAXIMO_DE_PERIODOS; i++) {
			periodos.add(new Periodo());
		}
		this.periodos = periodos;
		for (Disciplina disciplina : disciplinas) {
			boolean adicionado = false;
			do {
				int id = (Math.abs((new Random()).nextInt()) % MAXIMO_DE_PERIODOS) + 1;
				try {
					getPeriodo(id).alocarDisciplina(disciplina, false);
					adicionado = true;
				} catch (InvalidOperationException e) {
				}
			} while (!adicionado);
		}
	}

	/**
	 * Retorna Disciplina com id passado como argumento, ou null caso nao possua
	 * disciplina com o dado id.
	 * 
	 * @param id
	 *            da disciplina a ser retornada
	 * @return Disciplina cujo ID e o passado como argumento
	 * @throws InvalidOperationException se a disciplina nao existe
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas
	public Disciplina getDisciplinaPorID(int id) throws InvalidOperationException {
		Disciplina retorno = null;

		for (Disciplina i : disciplinas) {
			if (id == i.getId()) {
				retorno = i;
				break;
			}
		}
		
		if (retorno == null) {
			throw new InvalidOperationException("Você não pode alocar disciplinas que não existem.");
		}
		
		return retorno;
	}
	
	/**
	 * Pega o periodo pelo numero dele
	 * 
	 * @param index 
	 * 			numero do periodo (1..12)
	 * @return Periodo desejado
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public Periodo getPeriodo(int index) {
		return getPeriodos().get(index-1);
	}

	/**
	 * Retorna List com todas disciplinas presentes na grade do curso
	 * 
	 * @return Lista das disciplinas presentes na grade do curso
	 */
	// INFORMATION EXPERT: Grade contem uma lista com todas as disciplinas
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}
	
	/**
	 * Altera a lista de disciplinas
	 * 
	 * @param disciplinas Nova lista
	 */
	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}
	
	/**
	 * Verifica se a disciplina esta alocada.
	 * 
	 * @param disciplina Disciplina que se quer saber se esta alocada
	 * @return se a disciplina esta alocada ou nao
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas e periodos
	private boolean estaAlocado(Disciplina disciplina) {
		return getPeriodoDaDisciplina(disciplina) != 0;
	}

	/**
	 * Verifica qual e o ultimo periodo com alguma disciplina alocada
	 * 
	 * @return indice do ultimo periodo com alguma disciplina alocada
	 */
	private int obterUltimoPeriodo() {
		int ultimoPeriodo = 1;
		for (int i = 1; i <= MAXIMO_DE_PERIODOS; i++) {
			Periodo p = getPeriodo(i);
			if (p.totalDeCreditos() > 0) {
				ultimoPeriodo = i;
			}
		}
		return ultimoPeriodo;
	}

	/**
	 * Informa qual e o periodo da disciplina
	 * @param disciplina 
	 * 			que se quer saber o periodo
	 * @return o indice do periodo que contem a disciplina
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public int getPeriodoDaDisciplina(Disciplina disciplina) {
		int ultimoPeriodo = obterUltimoPeriodo();
		for (int i = 1; i <= ultimoPeriodo; i++) {
			if (getPeriodo(i).contains(disciplina)) {
				return i;
			}
		}
		return 0;
	}
	
	/**
	 * Tenta associar uma disciplina a um periodo
	 * 
	 * @param disciplina 
	 * 			a ser associada
	 * @param periodo 
	 * 			a ser associado
	 * @return se foi possivel alocar ou nao
	 * @throws InvalidOperationException caso nao faca sentido alocar
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas e periodos
	public void associarDisciplinaAoPeriodo(Disciplina disciplina, int periodo) throws InvalidOperationException {
		if (periodo < 1 || periodo > 12) {
			throw new InvalidOperationException("Não podem ser alocadas disciplinas para períodos que não existem.");
		}

		int indexUltimoPeriodo = obterUltimoPeriodo();
		
		if (indexUltimoPeriodo < periodo) {
            Periodo ultimoPeriodo = getPeriodo(indexUltimoPeriodo);
            if (ultimoPeriodo.passouDoLimiteDeCreditos())
            	throw new InvalidOperationException("O período " + indexUltimoPeriodo + " não pode ficar irregular.");
		}
		
		if (estaAlocado(disciplina)) {
			moverDisciplina(disciplina, periodo);
		}
		
		else {
			if (preRequisitosFaltando(disciplina, periodo).size() > 0) {
				throw new InvalidOperationException("Essa disciplina tem pre-requisitos faltando.");
			}
			
			Periodo p = getPeriodo(periodo);
			boolean ignorarCreditos = false;
			
			if (indexUltimoPeriodo == periodo) {
	            ignorarCreditos = true;
			}
			
			p.alocarDisciplina(disciplina, ignorarCreditos);
		}
	}
	
	/**
	 * Move a disciplina para um outro periodo
	 *
	 * @param disciplina que se quer mover
	 * @param indexPeriodoNovo periodo para onde se quer mover a disciplina
	 * @throws InvalidOperationException caso nao possa mover a disciplina para o periodo especificado
	 */
	private void moverDisciplina(Disciplina disciplina, int indexPeriodoNovo) throws InvalidOperationException {
		int indexPeriodoAntigo = getPeriodoDaDisciplina(disciplina);
		Periodo periodoAntigo = getPeriodo(indexPeriodoAntigo);
		
		int ultimoPeriodo = obterUltimoPeriodo();
		
		Periodo periodoNovo = getPeriodo(indexPeriodoNovo);
		periodoNovo.alocarDisciplina(disciplina, ultimoPeriodo<=indexPeriodoNovo);
		
		try {
			periodoAntigo.desalocarDisciplina(disciplina);
		} catch (InvalidOperationException e) {
			// nunca entra aqui...
			e.printStackTrace();
		}
	}
	
	/**
	 * Tenta desalocar uma disciplina. Se tiver pos-requisitos alocados, nao permite.
	 * 
	 * @param disciplina 
	 * 			que se quer desalocar
	 * @throws InvalidOperationException caso nao faca sentido desalocar (ja desalocada, ou primeiro periodo)
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public void desalocarDisciplina(Disciplina disciplina) throws InvalidOperationException {
		int indexPeriodo = getPeriodoDaDisciplina(disciplina);
		
        if (indexPeriodo == 0) {
            throw new InvalidOperationException("Esta disciplina já está desalocada.");
        }
        
		Periodo periodo = getPeriodo(indexPeriodo);
		
        List<Disciplina> posRequisitosAlocados = posRequisitosAlocados(disciplina);

       	for (Disciplina disc : posRequisitosAlocados) {
       		indexPeriodo = getPeriodoDaDisciplina(disc);
            Periodo p = getPeriodo(indexPeriodo);
            p.desalocarDisciplina(disc);
        }
       	
       	periodo.desalocarDisciplina(disciplina);
	}
	
	/**
	 * Verifica todas os pos-requisitos diretos e indiretos, da disciplina, que estao alocados
	 * 
	 * @param disciplina 
	 * 			que se quer saber que pos-requisitos diretos e indiretos estao alocados
	 * @return list com todas os pos-requisitos diretos e indiretos da disciplina que estao alocados
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas
	public List<Disciplina> posRequisitosAlocados(Disciplina disciplina) {
		List<Disciplina> posRequisitosAlocados = new ArrayList<Disciplina>();
		
		for (Disciplina i : disciplina.getPosRequisitos()) {
			if (getPeriodoDaDisciplina(i) != 0 && !posRequisitosAlocados.contains(i)) {
				for (Disciplina j : posRequisitosAlocados(i)) {
					if (!posRequisitosAlocados.contains(j)) {
						posRequisitosAlocados.add(j);
					}
				}
				posRequisitosAlocados.add(i);
			}
		}
		
		return posRequisitosAlocados;
	}
	
	/**
	 * Verifica que pre-requisitos diretos da disciplina nao estao sendo respeitados
	 * 
	 * @param disciplina 
	 * 			que se quer saber que pre-requisitos diretos nao estao sendo respeitados
	 * @return list com todos os pre-requisitos diretos que nao estao sendo respeitados
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas e periodos
	public List<Disciplina> preRequisitosFaltando(Disciplina disciplina, int periodo) {
		List<Disciplina> preRequisitosFaltando = new ArrayList<Disciplina>();
		for (Disciplina i : disciplina.getPreRequisitos()) {
			if (periodo <= getPeriodoDaDisciplina(i) || !estaAlocado(i)) {
				preRequisitosFaltando.add(i);
			}
		}
		return preRequisitosFaltando;
	}

	/**
	 * Converte a informacao da grade em uma string
	 * @return String com a descricao do que tem na grade
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas e periodos
	public String toString() {
        String result = "[";
        List<Disciplina> disciplinasIrregulares = obterDisciplinasIrregulares();
        for (int i = 0; i < disciplinas.size(); i++) {
            if (i > 0) {
                result += ", ";
            }
            Disciplina disciplina = disciplinas.get(i);
            result += "[" + disciplina.toString();
            result += ", " + getPeriodoDaDisciplina(disciplina);
            result += ", " + (disciplinasIrregulares.contains(disciplina) ? "1" : "0") + "]";
        }
        result += "]";
        return result;
	}
	
	/**
	 * Reseta a grade
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public void resetar() {
		limpar();
		setar();
	}
	
	/**
	 * Limpa a grade
	 */
	private void limpar() {
		for (Periodo periodo : getPeriodos()) {
			periodo.resetar();
		}
	}
	
	/**
	 * Aloca as disciplinas em seus periodos previstos, as optativas vao para o ultimo periodo com alguma disciplina
	 */
	private void setar() {
		for (Disciplina disciplina : disciplinas) {
			if (disciplina.getPeriodoPrevisto() == 0) {
				try {
					associarDisciplinaAoPeriodo(disciplina, obterUltimoPeriodo());
				} catch (InvalidOperationException e) {
					e.printStackTrace();
				}
			}
			else {
				try {
					associarDisciplinaAoPeriodo(disciplina, disciplina.getPeriodoPrevisto());
				} catch (InvalidOperationException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Obtém uma lista das disciplinas irregulares na grade.
	 * Uma disciplina está irregular se está alocada sem que todos os seus
	 * pré-requisitos estejam alocadas em períodos inferiores ao dela.
	 * 
	 * @return Lista das disciplinas irregulares.
	 */
	public List<Disciplina> obterDisciplinasIrregulares() {
		List<Disciplina> disciplinasIrregulares = new ArrayList<Disciplina>();
		int ultimoPeriodo = obterUltimoPeriodo();
		
		for (int periodo = 1; periodo <= ultimoPeriodo; periodo++) {
			Periodo p = getPeriodo(periodo);
			List<Disciplina> disciplinas = p.getDisciplinas();
			
			for (Disciplina disciplina : disciplinas) {
				List<Disciplina> preRequisitos = disciplina.getPreRequisitos();
				
				boolean disciplinaRegular = true;
				for (Disciplina preRequisito : preRequisitos) {
					if (!estaAlocado(preRequisito) || getPeriodoDaDisciplina(preRequisito) >= periodo || disciplinasIrregulares.contains(preRequisito)) {
						disciplinaRegular = false;
						break;
					}
				}
				if (!disciplinaRegular) {
					disciplinasIrregulares.add(disciplina);
				}
			}
		}
		return disciplinasIrregulares;
	}

	/**
	 * Retorna o id unico da grade
	 * 
	 * @return id da grade
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribui um id ao periodo
	 *
	 * @param id atribuido
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Retorna o periodo que esta sendo cursado
	 * 
	 * @return inteiro correspondente ao periodo que o usuario esta cursando
	 */
	public int getPeriodoCursando() {
		return periodoCursando;
	}

	/**
	 * Atribui um valor ao periodo cursando
	 * 
	 * @param periodoCursando valor a ser atribuido
	 */
	public void setPeriodoCursando(int periodoCursando) {
		this.periodoCursando = periodoCursando;
	}

	/**
	 * Retorna lista com os periodos
	 * 
	 * @return lista com os periodos
	 */
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	/**
	 * Atribui uma lista de periodos a grade
	 * 
	 * @param periodos lista a ser atribuida a grade
	 */
	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
	}
}
