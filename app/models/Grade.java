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
 * Entidade responsável por armazenar a grade de um usuário
 * e realizar operações sobre ela (alocar, desalocar, mover...).
 */
@Entity
public class Grade extends Model {
	private static final long serialVersionUID = 1543790286461L;

	private static final int MAXIMO_DE_PERIODOS = 12;
	
	@Id
	private Long id;
	
	private int periodoCursando;
	
	private TipoDeGrade tipoDeGrade = null;

	@Transient
	private List<Disciplina> disciplinas;

	@ManyToMany(cascade=CascadeType.ALL)
	private List<Periodo> periodos;

	/**
	 * Construtor genérico
	 * Inicializa as variáveis e reseta o sistema (aloca primeiro periodo)
	 */
	public Grade() {
		setPeriodos(new ArrayList<Periodo>());
		setDisciplinas(new ArrayList<Disciplina>());
		setPeriodoCursando(1);

		for (int i = 0; i < MAXIMO_DE_PERIODOS; i++) {
			periodos.add(new Periodo());
		}
	}

	/**
	 * Carrega as disciplinas para a grade de acordo com seu tipo.
	 */
	public void carregarDisciplinas() {
		setDisciplinas(CarregadorDeDisciplinas.carregaDisciplinas(tipoDeGrade));
	}
	
	/**
	 * Tenta associar uma disciplina a um período.
	 * 
	 * @param disciplina Disciplina a ser associada.
	 * @param periodo Período a ser associado.
	 * @throws InvalidOperationException Caso a associação não seja válida.
	 */
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
				throw new InvalidOperationException("Essa disciplina tem pré-requisitos faltando.");
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
	 * Move a disciplina para um outro periodo.
	 *
	 * @param disciplina Disciplina que se quer mover.
	 * @param indexPeriodoNovo Número (1..12) do período para onde se quer mover a disciplina.
	 * @throws InvalidOperationException Caso não seja válido mover para o período especificado.
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
	 * Tenta desalocar uma disciplina. Se tiver pós-requisitos alocados, não permite.
	 * 
	 * @param disciplina Disciplina que se quer desalocar.
	 * @throws InvalidOperationException Caso não seja válido desalocar (já desalocada).
	 */
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
		setPeriodoCursando(obterUltimoPeriodo());
	}
	
	/**
	 * Reseta a grade de disciplinas de acordo com a lista de disciplinas.
	 */
	public void resetar() {
		for (Periodo periodo : getPeriodos()) {
			periodo.resetar();
		}

		for (Disciplina disciplina : disciplinas) {
			Periodo periodo = null;
			if (disciplina.getPeriodoPrevisto() == 0) {
				periodo = getPeriodo(obterUltimoPeriodo());
			} else {
				periodo = getPeriodo(disciplina.getPeriodoPrevisto());
			}
			try {
				periodo.alocarDisciplina(disciplina, true);
			} catch (InvalidOperationException e) {
				// nunca vai entrar aqui...
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Verifica se a disciplina está alocada.
	 * 
	 * @param disciplina Disciplina que se quer saber se está alocada.
	 * @return Se a disciplina esta alocada ou não.
	 */
	private boolean estaAlocado(Disciplina disciplina) {
		return getPeriodoDaDisciplina(disciplina) != 0;
	}

	/**
	 * Obtém o número do último periodo com alguma disciplina alocada.
	 * Em caso de não haver disciplina alocada, considera-se que é o período 1.
	 * 
	 * @return Número (1..12) do último periodo com alguma disciplina alocada.
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
	 * Obtém uma lista das disciplinas irregulares na grade.
	 * Uma disciplina está irregular se está alocada sem que todos os seus
	 * pré-requisitos estejam alocados em períodos inferiores ao dela.
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
	 * Obtém disciplina com id passado como argumento.
	 * 
	 * @param id Identificador da disciplina a ser obtida.
	 * @return Disciplina cujo ID e o passado como argumento
	 * @throws InvalidOperationException Se a disciplina não existe.
	 */
	public Disciplina getDisciplinaPorID(Long id) throws InvalidOperationException {
		Disciplina disciplina = null;

		for (Disciplina d : disciplinas) {
			if ((long)id == (long)d.getId()) {
				disciplina = d;
				break;
			}
		}
		
		if (disciplina == null) {
			throw new InvalidOperationException("A disciplina especificada não existe.");
		}
		
		return disciplina;
	}

	/**
	 * Informa qual é o período da disciplina.
	 *
	 * @param disciplina Disciplina que se quer saber o período.
	 * @return O número (0*..12) do período da disciplina.
	 * 			*Em caso de não estar em nenhum, considera-se que está no 0.
	 */
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
	 * Obtém todos os pós-requisitos (diretos e indiretos) da disciplina que estão alocados.
	 * 
	 * @param disciplina Disciplina da qual se quer obter os pós-requisitos alocados.
	 * @return Lista com os pós-requisitos alocados.
	 */
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
	 * Obtém todos os pré-requisitos (diretos) da disciplina que não estão alocados.
	 * Disciplinas alocadas em período posterior ou igual a periodoDaDisciplina são
	 * consideradas não alocadas.
	 * 
	 * @param disciplina Disciplina da qual se quer os pré-requisitos não alocados.
	 * @param periodoDaDisciplina Período a partir do qual as disciplinas são
	 * 			automaticamente consideradas desalocadas.
	 * @return Lista com os pré-requisitos não alocados.
	 */
	public List<Disciplina> preRequisitosFaltando(Disciplina disciplina, int periodoDaDisciplina) {
		List<Disciplina> preRequisitosFaltando = new ArrayList<Disciplina>();
		for (Disciplina i : disciplina.getPreRequisitos()) {
			if (periodoDaDisciplina <= getPeriodoDaDisciplina(i) || !estaAlocado(i)) {
				preRequisitosFaltando.add(i);
			}
		}
		return preRequisitosFaltando;
	}

	/**
	 * Obtém o período pelo número (ordenado).
	 * 
	 * @param index Número do periodo (1..12)
	 * @return Objeto do período desejado.
	 */
	public Periodo getPeriodo(int index) {
		return getPeriodos().get(index-1);
	}

	/**
	 * Obtém uma lista com todas as disciplinas presentes nesta grade.
	 * 
	 * @return Lista das disciplinas presentes nesta grade.
	 */
	public List<Disciplina> getDisciplinas() {
		return disciplinas;
	}
	
	/**
	 * Altera a lista de disciplinas.
	 * 
	 * @param disciplinas Nova lista de disciplinas.
	 */
	public void setDisciplinas(List<Disciplina> disciplinas) {
		this.disciplinas = disciplinas;
	}

	/**
	 * Retorna o id da grade.
	 * 
	 * @return Identificador numérico da grade.
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribui um id à grade.
	 *
	 * @param id Identificador a ser atribuído.
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém o periodo que está sendo cursado.
	 * 
	 * @return Número do período sendo cursado.
	 */
	public int getPeriodoCursando() {
		return periodoCursando;
	}

	/**
	 * Altera o período que está sendo cursado.
	 * 
	 * @param periodoCursando Número do período que está sendo cursado.
	 */
	public void setPeriodoCursando(int periodoCursando) {
		this.periodoCursando = periodoCursando;
	}

	/**
	 * Obtém lista com os períodos da grade.
	 * 
	 * @return Lista com os períodos.
	 */
	public List<Periodo> getPeriodos() {
		return periodos;
	}

	/**
	 * Atribui uma lista de periodos à grade.
	 * 
	 * @param periodos Lista de períodos a ser atribuída.
	 */
	public void setPeriodos(List<Periodo> periodos) {
		this.periodos = periodos;
	}

	/**
	 * Obtém o tipo da grade.
	 * 
	 * @return Tipo da grade.
	 */
	public TipoDeGrade getTipoDeGrade() {
		return tipoDeGrade;
	}

	/**
	 * Atribui um novo tipo à grade; se for diferente do anterior,
	 * atualiza a lista de disciplinas e limpa a grade.
	 * 
	 * @param tipoDeGrade Novo tipo da grade.
	 */
	public void setTipoDeGrade(TipoDeGrade tipoDeGrade) {
		if (this.tipoDeGrade != tipoDeGrade) {
			this.tipoDeGrade = tipoDeGrade;
			for (Periodo p : periodos) {
				p.resetar();
			}
		}
		carregarDisciplinas();
	}

	/**
	 * Converte a informação da grade em uma string.
	 * @return String com a descrição do que tem na grade.
	 */
	@Override
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
}
