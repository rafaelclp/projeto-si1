package model;

import java.util.ArrayList;
import java.util.List;

public class Grade {

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
disciplinas.add(new Disciplina("Programação I", 4, 4, 1, 1));
		disciplinas.add(new Disciplina("Leitura e Prod. de Textos", 4, 2, 1, 2));
		disciplinas.add(new Disciplina("Cálculo I", 4, 7, 1, 3));
		disciplinas.add(new Disciplina("Algebra Vetorial", 4, 3, 1, 4));
		disciplinas.add(new Disciplina("Int. à Computacação", 4, 5, 1, 5));
		disciplinas.add(new Disciplina("Lab. de Programação I", 4, 4, 1, 6));
		disciplinas.add(new Disciplina("Programação II", 4, 5, 2, 7));
		disciplinas.add(new Disciplina("Lab. de Programação II", 4, 5, 2, 8));
		disciplinas.add(new Disciplina("Matemática Discreta", 4, 5, 2, 9));
		disciplinas.add(new Disciplina("Metodologia Científica", 4, 4, 2, 10));
		disciplinas.add(new Disciplina("Teoria dos Grafos", 2, 3, 2, 11));
		disciplinas.add(new Disciplina("Fund. de Física Clássica", 4, 8, 2, 12));
		disciplinas.add(new Disciplina("Cálculo II", 4, 7, 2, 13));
		disciplinas.add(new Disciplina("Estrutura de Dados e Algoritmos", 4, 7, 3, 14));
		disciplinas.add(new Disciplina("Lab. de Estrutura de Dados e Algoritmos", 4, 7, 3, 15));
		disciplinas.add(new Disciplina("Algebra Linear", 4, 9, 3, 16));
		disciplinas.add(new Disciplina("Probabilidade e Est.", 4, 9, 3, 17));
		disciplinas.add(new Disciplina("Teoria da Computação", 4, 6, 3, 18));
		disciplinas.add(new Disciplina("Fund. de Física Moderna", 4, 7, 3, 19));
		disciplinas.add(new Disciplina("Gerência da Informação", 4, 5, 3, 20));
		disciplinas.add(new Disciplina("Métodos Estatísticos", 4, 5, 4, 21));
		disciplinas.add(new Disciplina("Paradigmas de Linguagens de Programação", 2, 5, 4, 22));
		disciplinas.add(new Disciplina("Org. e Arquitetura de Computadores I", 4, 5, 4, 23));
		disciplinas.add(new Disciplina("Lab. de Org. e Arquitetura de Computadores", 4, 5, 4, 24));
		disciplinas.add(new Disciplina("Lógica Matemática", 4, 5, 4, 25));
		disciplinas.add(new Disciplina("Sistemas de Informação I", 4, 5, 4, 26));
		disciplinas.add(new Disciplina("Engenharia de Software I", 4, 5, 4, 27));
		disciplinas.add(new Disciplina("Análise e Técnicas de Algoritmos", 4, 9, 5, 28));
		disciplinas.add(new Disciplina("Redes de Computadores", 4, 5, 5, 29));
		disciplinas.add(new Disciplina("Compiladores", 4, 9, 5, 30));
		disciplinas.add(new Disciplina("Banco de Dados I", 4, 5, 5, 31));
		disciplinas.add(new Disciplina("Sistemas de Informação II", 4, 5, 5, 32));
		disciplinas.add(new Disciplina("Laboratório de Engenharia de Software", 4, 5, 5, 33));
		disciplinas.add(new Disciplina("Informática e Sociedade", 2, 5, 5, 34));
		disciplinas.add(new Disciplina("Sistemas Operacionais", 4, 5, 6, 35));
		disciplinas.add(new Disciplina("Interconexão de Redes de Computadores", 4, 5, 6, 36));
		disciplinas.add(new Disciplina("Lab. de Interconexão de Redes de Computadores", 4, 5, 6, 37));
		disciplinas.add(new Disciplina("Inteligência Artificial I", 4, 5, 6, 38));
		disciplinas.add(new Disciplina("Banco de Dados II", 4, 5, 6, 39));
		disciplinas.add(new Disciplina("Direito e Cidadania", 4, 5, 6, 40));
		disciplinas.add(new Disciplina("Métodos e Software Númericos", 4, 5, 7, 41));
		disciplinas.add(new Disciplina("Aval. de Desempenho de Sist. Discretos", 4, 5, 7, 42));
		disciplinas.add(new Disciplina("Projeto em Computação I", 4, 5, 7, 43));
		disciplinas.add(new Disciplina("Projeto em Computação II", 6, 5, 8, 44));
		disciplinas.add(new Disciplina("Administração", 4, 5, 0, 45));
		disciplinas.add(new Disciplina("TECC (Administração de SGBDs)", 4, 5, 0, 46));
		disciplinas.add(new Disciplina("TECC (Computação e Música)", 4, 3, 0, 47));
		disciplinas.add(new Disciplina("Criatividade", 4, 2, 0, 48));
		disciplinas.add(new Disciplina("Ergonomia", 4, 3, 0, 49));
		disciplinas.add(new Disciplina("Gestão da Qualidade", 4, 5, 0, 50));
		disciplinas.add(new Disciplina("TECC (Inteligência Artificial II)", 4, 8, 0, 51));
		disciplinas.add(new Disciplina("Marketing para Informática", 4, 5, 0, 52));
		disciplinas.add(new Disciplina("Métodos Avançados de Programação", 4, 7, 0, 53));
		disciplinas.add(new Disciplina("Organização e Arquitetura de Computadores II", 4, 8, 0, 54));
		disciplinas.add(new Disciplina("Percepção da Forma", 4, 3, 0, 55));
		disciplinas.add(new Disciplina("Prática Desportiva", 2, 1, 0, 56));
		disciplinas.add(new Disciplina("Processo Decisório", 4, 5, 0, 57));
		disciplinas.add(new Disciplina("Psicologia da Aprendizagem", 4, 3, 0, 58));
		disciplinas.add(new Disciplina("Relações Humanas", 4, 3, 0, 59));
		disciplinas.add(new Disciplina("Sociologia Industrial I", 4, 5, 0, 60));
		disciplinas.add(new Disciplina(" Teoria e Prática da Cor", 4, 2, 0, 61));
		disciplinas.add(new Disciplina("TECC (Algoritmos Avançados I)", 2, 9, 0, 62));
		disciplinas.add(new Disciplina("TECC (Redes Ad Hoc sem fio)", 4, 5, 0, 63));
		disciplinas.add(new Disciplina("TECC (Jogos digitais)", 4, 5, 0, 64));
		// cria classe para leitura de dados apartir de arquivo.
		//LeitorArquivo loader = new LeitorArquivo();
		//disciplinas = loader.carregaDisciplinas();
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
}
