package exercicios

class Turma (val nomeDisciplina: String){
    val alunos: MutableList<Aluno> = mutableListOf();

    fun matricularAluno(aluno: Aluno){
        alunos.add(aluno)
    }

    fun calcularMediaDaTurma(): Double{
        if (alunos.isEmpty()){
            return 0.0;
        }

        var soma = 0.0;

        for(aluno in alunos){
            soma += aluno.notaFinal;
        }

        val media = soma / alunos.size;

        return media;
    }

    fun listarAprovados(notaMinima: Double = 6.0){
        for (aluno in alunos){
            if (aluno.notaFinal >= notaMinima){
                println(aluno.nome);
            }
        }
    }
}