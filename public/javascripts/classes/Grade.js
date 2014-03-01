/*
	Classe Grade
		Armazena uma lista de disciplinas acessíveis tanto por index quanto
		por id, e realiza operações de alocar/desalocar tais disciplinas.
		- disciplinas: lista das disciplinas na grade.
*/

var Grade = {
	"disciplinas": [],

	/**
	 * Altera a lista de disciplinas.
	 * @param lista_de_disciplinas A nova lista
	**/
	setarDisciplinas: function(lista_de_disciplinas) {
		this.disciplinas = lista_de_disciplinas;
		this.disciplinas.sort(function(a,b) { return a.compararA(b); });
	},

	/**
	 * Procura uma disciplina na lista pelo id.
	 * @param id ID da disciplina a ser buscada.
	 * @return Referência para a disciplina encontrada, ou null se não existir.
	**/
	procurarDisciplina: function(id) {
		for (var i = 0; i < this.disciplinas.length; i++)
			if (this.disciplinas[i].id == id)
				return this.disciplinas[i];
		return null;
	},

	/**
	 * Aloca uma disciplina pelo id a um período.
	 * @param id ID da disciplina a ser alocada.
	 * @param periodo Periodo em que a disciplina deve ser alocada.
	**/
	alocarDisciplina: function(id, periodo) {
		var disciplina = this.procurarDisciplina(id);
		if (disciplina != null)
			disciplina.alocar(periodo);
	},

	/**
	 * Desaloca uma disciplina pelo id.
	 * @param id ID da disciplina a ser desalocada.
	**/
	desalocarDisciplina: function(id) {
		var disciplina = this.procurarDisciplina(id);
		if (disciplina != null)
			disciplina.desalocar();
	},

	/**
	 * Desaloca uma lista de disciplinas pelos ids.
	 * @param lista_de_ids Lista de ids das disciplinas a serem desalocadas.
	**/
	desalocarDisciplinas: function(lista_de_ids) {
		for (var i = 0; i < lista_de_ids.length; i++) {
			var id = lista_de_ids[i];
			this.desalocarDisciplina(id);
		}
	}
};
