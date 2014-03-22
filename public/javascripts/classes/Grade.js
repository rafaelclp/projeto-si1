/*
	Classe Grade
		Armazena uma lista de disciplinas acessíveis tanto por index quanto
		por id, e realiza operações de alocar/desalocar tais disciplinas.
		- disciplinas: lista das disciplinas na grade.
		- periodoCursando: periodo a ser cursado no momento.
*/

var Grade = {
	"disciplinas": [],
	"periodoCursando": 0,

	/**
	 * Informa a quantidade de créditos já cursados.
	 * @return Quantidade de créditos
	**/
	getCreditosCursados: function() {
		var creditos = 0;
		for (var i = 0; i < this.disciplinas.length; i++)
			if (this.disciplinas[i].periodo != 0 && this.disciplinas[i].periodo < this.periodoCursando)
				creditos += this.disciplinas[i].creditos;
		return creditos;
	},

	/**
	 * Informa a quantidade de créditos sendo cursados.
	 * @return Quantidade de créditos
	**/
	getCreditosCursando: function() {
		var creditos = 0;
		for (var i = 0; i < this.disciplinas.length; i++)
			if (this.disciplinas[i].periodo == this.periodoCursando)
				creditos += this.disciplinas[i].creditos;
		return creditos;
	},

	/**
	 * Informa a quantidade de créditos a serem cursados.
	 * @return Quantidade de créditos
	**/
	getCreditosACursar: function() {
		var creditos = 0;
		for (var i = 0; i < this.disciplinas.length; i++)
			if (this.disciplinas[i].periodo > this.periodoCursando)
				creditos += this.disciplinas[i].creditos;
		return creditos;
	},

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
	 * Altera a regularidade das disciplinas da grade.
	 * @param disciplinas_irregulares Lista de ids das disciplinas irregulares.
	**/
	alterarRegularidade: function(disciplinas_irregulares) {
		var esta_irregular = {};
		for (var i = 0; i < disciplinas_irregulares; i++)
			esta_irregular[disciplinas_irregulares[i]] = true;

		for (var i = 0; i < this.disciplinas.length; i++) {
			var id = this.disciplinas[i].id;
			this.disciplinas[i].irregular = esta_irregular[id] ? 1 : 0;
		}
	},

	/**
	 * Aloca uma disciplina pelo id a um período.
	 * @param id ID da disciplina a ser alocada.
	 * @param periodo Periodo em que a disciplina deve ser alocada.
	 * @param esta_irregular Informa se a disciplina alocada está irregular.
	**/
	alocarDisciplina: function(id, periodo, esta_irregular) {
		var disciplina = this.procurarDisciplina(id);
		if (disciplina != null) {
			disciplina.alocar(periodo);
			disciplina.irregular = esta_irregular ? 1 : 0;
		}
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
