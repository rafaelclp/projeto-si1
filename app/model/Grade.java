package model;

import java.util.ArrayList;
import java.util.List;

public class Grade {

	final int MAXIMO_DE_PERIODOS = 12;
	
	// CREATOR: Grade contem uma lista com as disciplinas
	// Lista com todas as disciplinas da grade
	private List<Disciplina> disciplinas;

	// CREATOR: Grade contem uma lista de períodos
	// Lista com todos os periodos da grade
	private List<Periodo> periodos;

	/**
	 * Constructor
	 * Inicializa as variaveis e reseta o sistema (aloca primeiro periodo)
	 */
	public Grade() {
		periodos = new ArrayList<Periodo>();
		disciplinas = new ArrayList<Disciplina>();
		this.preencherDisciplinas();
		
		for (int i = 0; i < 12; i++) {
			periodos.add(new Periodo());
		}
		
		resetar();
	}

	/**
	 * preenche a lista com todas as disciplinas do curso, apartir de um arquivo
	 * XML, que é manipulado por uma objeto do tipo LeitorArquivo
	 * 
	 */
	// PURE FABRICATION: Tinha que haver um modo das disciplinas entrarem na grade,
	//					resolvemos fazer por leitura de arquivo.
	private void preencherDisciplinas() {
		// cria classe para leitura de dados apartir de arquivo.
		LeitorArquivo loader = new LeitorArquivo();
		disciplinas = loader.carregaDisciplinas();
	}

	/**
	 * Retorna Disciplina com id passado como argumento, ou null caso nao possua
	 * disciplina com o dado id.
	 * 
	 * @param id
	 *            da disciplina a ser retornada
	 * @return Disciplina cujo ID é o passado como argumento
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas
	public Disciplina getDisciplinaPorID(int id) {
		Disciplina retorno = null;

		for (Disciplina i : disciplinas) {
			if (id == i.getID()) {
				retorno = i;
				break;
			}
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
		return periodos.get(index-1);
	}

	/**
	 * Retorna List com todas disciplinas presentes na grade do curso
	 * 
	 * @return Lista das disciplinas presentes na grade do curso
	 */
	// INFORMATION EXPERT: Grade contem uma lista com todas as disciplinas
	public List<Disciplina> getTodasDisciplinas() {
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
	 * @throws InvalidOperationException caso nao faca sentido alocar (primeiro periodo, >28 creditos)
	 */
	// INFORMATION EXPERT: Grade contem todas as disciplinas e periodos
	public boolean associarDisciplinaAoPeriodo(Disciplina disciplina, int periodo) throws InvalidOperationException {
		boolean resposta = true;
		if (periodo == 1) {
			throw new InvalidOperationException("Não podem ser alocadas disciplinas para o primeiro período.");
		}

		if (periodo < 1 || periodo > 12) {
			throw new InvalidOperationException("Não podem ser alocadas disciplinas para períodos que não existem.");
		}

		Periodo p = getPeriodo(periodo);
		if (!p.podeAlocar(disciplina)) {
            throw new InvalidOperationException("O período não pode ter mais de 28 créditos.");
		}

        List<Disciplina> preRequisitosFaltando = preRequisitosFaltando(disciplina, periodo);
        if (!preRequisitosFaltando.isEmpty()) {
    	    resposta = false;
        } else {
        	p.addDisciplina(disciplina);
        }

        return resposta;
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
	public List<Disciplina> desalocarDisciplina(Disciplina disciplina, boolean force) throws InvalidOperationException {
		int periodo = getPeriodoDaDisciplina(disciplina);
		
        if (periodo == 0) {
            throw new InvalidOperationException("Esta disciplina já está desalocada.");
        }
        
        if (periodo == 1) {
            throw new InvalidOperationException("Disciplinas do primeiro período não podem ser desalocadas.");
        }

        List<Disciplina> posRequisitosAlocados = posRequisitosAlocados(disciplina);
        posRequisitosAlocados.add(disciplina);

        if (force || posRequisitosAlocados.size() == 1) {
        	for (Disciplina i : posRequisitosAlocados) {
                int periodoIndex = getPeriodoDaDisciplina(i);
                Periodo p = getPeriodo(periodoIndex);
                p.removeDisciplina(i);
            }
        } else {
        	posRequisitosAlocados = new ArrayList<Disciplina>();
        }
        return posRequisitosAlocados;
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
	public String disciplinasParaString() {
        String result = "[";
        for (int i = 0; i < disciplinas.size(); i++) {
            if (i > 0) {
                result += ", ";
            }
            Disciplina disciplina = disciplinas.get(i);
            result += "[" + disciplina.toString();
            result += ", " + getPeriodoDaDisciplina(disciplina) + "]";
        }
        result += "]";
        return result;
	}
	
	/**
	 * Reseta a grade
	 */
	// INFORMATION EXPERT: Grade contem todos os periodos
	public void resetar() {
		for (Periodo periodo : periodos) {
			periodo.resetar();
		}
		for (Disciplina disciplina : disciplinas) {
			if (disciplina.getPeriodoPrevisto() == 1) {
				Periodo primeiroPeriodo = getPeriodo(1);
				primeiroPeriodo.addDisciplina(disciplina);
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
	private List<Disciplina> obterDisciplinasIrregulares() {
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
}
