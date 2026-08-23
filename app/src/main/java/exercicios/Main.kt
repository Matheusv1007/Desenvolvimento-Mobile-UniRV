package exercicios

fun main() {

    val turma = Turma("Desenvolvimento Mobile")

    val aluno1 = Aluno("Matheus", 101, 5.0)
    val aluno2 = Aluno("Simon", 102, 6.0)
    val aluno3 = Aluno("Waldecy", 103, 8.5)

    turma.matricularAluno(aluno1)
    turma.matricularAluno(aluno2)
    turma.matricularAluno(aluno3)

    println("Média da turma: ${turma.calcularMediaDaTurma()}")

    println("Alunos aprovados:")
    turma.listarAprovados()
}