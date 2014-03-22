package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

import play.db.ebean.Model;

public class Grade extends Model {
	private static final long serialVersionUID = 1543790286461L;

	private static final int MAXIMO_DE_PERIODOS = 12;
	
	@Id
	private int id;
	
	private int periodoCursando;
	
	@Transient
	private TipoDeGrade tipoDeGrade = TipoDeGrade.FLUXOGRAMA_OFICIAL;
	
	// CREATOR: Grade contem uma lista com as disciplinas
	// Lista com todas as disciplinas da grade
	@Transient
	private List<Disciplina> disciplinas;

	// CREATOR: Grade contem uma lista de períodos
	// Lista com todos os periodos da grade
	private List<Periodo> periodos;

	/**
	 * Constructor
	 * Inicializa as variaveis e reseta o sistema (aloca primeiro periodo)
	 * @throws InvalidOperationException 
	 */
	public Grade() {
		setPeriodos(new ArrayList<Periodo>());
		disciplinas = CarregadorDeDisciplinas.carregaDisciplinas(tipoDeGrade);
		periodoCursando = 1;
		
		for (int i = 0; i < MAXIMO_DE_PERIODOS; i++) {
			periodos.add(new Periodo());
		}
		
		resetar();
	}

	/**
	 * Retorna Disciplina com id passado como argumento, ou null caso nao possua
	 * disciplina com o dado id.
	 * 
	 * @param id
	 *            da disciplina a ser retornada
	 * @return Disciplina cujo ID é o passado como argumento
	 * @throws InvalidOperationException 
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
	 * Verifica se a disciplina esta alocada.
	 * 
	 * @param disciplina Disciplina
	 * @return se a disciplina esta alocada ou nao
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas e periodos
	private boolean estaAlocado(Disciplina disciplina) {
		return getPeriodoDaDisciplina(disciplina) != 0;
	}

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
		for (int i = 1; i <= periodos.size(); i++) {
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
	
	private void moverDisciplina(Disciplina disciplina, int indexPeriodoNovo) throws InvalidOperationException {
		int indexPeriodoAntigo = getPeriodoDaDisciplina(disciplina);
		Periodo periodoAntigo = getPeriodo(indexPeriodoAntigo);
		
		int ultimoPeriodo = obterUltimoPeriodo();
		
		Periodo periodoNovo = getPeriodo(indexPeriodoNovo);
		periodoNovo.alocarDisciplina(disciplina, ultimoPeriodo<=indexPeriodoNovo);
		
		try {
			periodoAntigo.desalocarDisciplina(disciplina, periodoCursando>indexPeriodoAntigo);
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
	 * @param force 
	 * 			forcar a desalocacao, mesmo que tenha pos-requisitos (neste caso, desaloca tudo)
	 * @return Lista com todas as disciplinas desalocadas
	 * @throws InvalidOperationException caso nao faca sentido desalocar (ja desalocada, ou primeiro periodo)
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public void desalocarDisciplina(Disciplina disciplina) throws InvalidOperationException {
		int indexPeriodo = getPeriodoDaDisciplina(disciplina);
		Periodo periodo = getPeriodo(indexPeriodo);
		
        if (indexPeriodo == 0) {
            throw new InvalidOperationException("Esta disciplina já está desalocada.");
        }
        
        List<Disciplina> posRequisitosAlocados = posRequisitosAlocados(disciplina);

       	for (Disciplina i : posRequisitosAlocados) {
       		indexPeriodo = getPeriodoDaDisciplina(i);
            periodo = getPeriodo(indexPeriodo);
            periodo.desalocarDisciplina(i, periodoCursando>indexPeriodo);
        }
	}
	
	/**
	 * Verifica todas os pos-requisitos diretos e indiretos, da disciplina, que estao alocados
	 * 
	 * @param disciplina 
	 * 			que se quer saber que pos-requisitos diretos e indiretos estao alocados
	 * @return lista com todas os pos-requisitos diretos e indiretos da disciplina que estao alocados
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas
	public List<Disciplina> posRequisitosAlocados(Disciplina disciplina) {
		List<Disciplina> posRequisitosAlocados = new ArrayList<Disciplina>();
		int periodo = getPeriodoDaDisciplina(disciplina);
		
		for (Disciplina i : disciplina.getPosRequisitos()) {
			if (periodo < getPeriodoDaDisciplina(i) && !posRequisitosAlocados.contains(i)) {
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
	 * @return lista com todos os pre-requisitos diretos que nao estao sendo respeitados
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
	 * Converte a lista de disciplinas da grade para uma string
	 * @return String no formato JSON
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
	 * @throws InvalidOperationException 
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public void resetar() {
		limpar();
		setar();
	}
	
	private void limpar() {
		for (Periodo periodo : getPeriodos()) {
			periodo.resetar();
		}
	}
	
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
		
		for (int periodo = 1; periodo <= MAXIMO_DE_PERIODOS; periodo++) {
			Periodo p = getPeriodo(periodo);
			if (p == null) {
				// a grade tem menos períodos do que o máximo
				break;
			}
			List<Disciplina> disciplinas = p.getDisciplinas();
			
			for (Disciplina disciplina : disciplinas) {
				List<Disciplina> preRequisitos = disciplina.getPreRequisitos();
				
				boolean disciplinaRegular = true;
				for (Disciplina preRequisito : preRequisitos) {
					if (!estaAlocado(preRequisito) || getPeriodoDaDisciplina(preRequisito) >= periodo) {
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPeriodoCursando() {
		return periodoCursando;
	}

	public void setPeriodoCursando(int periodoCursando) {
		this.periodoCursando = periodoCursando;
	}

	public List<Periodo> getPeriodos() {
		return periodos;
	}

	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
	}
}
