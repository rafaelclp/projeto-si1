/*
	Classe Disciplina
		Armazena os dados de uma única disciplina (relativa ao curso E ao usuário).
		- id: identificador numérico da disciplina
		- nome: nome da disciplina
		- creditos: créditos da disciplina
		- dificuldade: dificuldade prevista para a disciplina
		- periodo_previsto: período em que "deveria" ser cursada ou 0 (optativa)
		- periodo: período que o usuário escolheu para cursá-la ou 0 (não alocada)
*/

/**
 * Cria uma disciplina através de uma lista/array
 * @param lista Lista no formato [id, nome, creditos, dificuldade, periodo_previsto, periodo].
**/
function Disciplina(lista) {
	this.id = lista[0];
	this.nome = lista[1];
	this.creditos = lista[2];
	this.dificuldade = lista[3];
	this.periodo_previsto = lista[4];
	this.periodo = lista[5];
}

/**
 * Aloca uma disciplina para um periodo
 * @param periodo Periodo em que será alocada.
**/
Disciplina.prototype.alocar = function(periodo) {
	this.periodo = periodo;
}

/**
 * Desaloca uma disciplina
**/
Disciplina.prototype.desalocar = function() {
	this.periodo = 0;
}

/**
 * Compara "esta" disciplina a um objeto qualquer
 * Ordena pelo período esperado, e depois pelo nome.
 * @param obj Um objeto a ser comparado
 * @return -1, se this < obj ou obj não é instância de Disciplina;
 *			1, se obj < this;
 *			0, caso contrário.
**/
Disciplina.prototype.compararA = function(obj) {
	if (!(obj instanceof Disciplina)) return -1;

	if (this.periodo_previsto != obj.periodo_previsto) {
		if (obj.periodo_previsto == 0) return -1;
		if (this.periodo_previsto == 0) return 1;
		return this.periodo_previsto < obj.periodo_previsto ? -1 : 1;
	}
	if (this.nome != obj.nome)
		return this.nome < obj.nome ? -1 : 1;
	return 0;
}