
data class Movimento(
    val montante: Int,
    val data: Int,
    val tipo: Char,
    val descricao: String
)

val movimentos = Array(1000) { mutableListOf<Movimento>() }
var minimo = 0

fun inserir(montante: Int, data: Int, periodicidade: Int, tipo: Char, descricao: String) {
    var dia = data

    while (dia <= 999) {
        movimentos[dia].add(Movimento(montante, dia, tipo, descricao))

        if (periodicidade == 0) break
        dia += periodicidade
    }
}

fun listar(data: Int) {
    movimentos[data].forEachIndexed { indice, m ->
        println("$indice ${m.montante} ${m.tipo} ${m.descricao}")
    }
}

fun remover(data: Int, indice: Int) {
    if (indice in movimentos[data].indices) {
        movimentos[data].removeAt(indice)
    }
}

fun saldo(data: Int): Int {
    var total = 0

    for (dia in 0..data) {
        for (m in movimentos[dia]) {
            total += if (m.tipo == 'r') m.montante else -m.montante
        }
    }

    return total
}

fun valido(): Boolean {
    for (dia in 0..999) {
        if (saldo(dia) < minimo) return false
    }
    return true
}

fun dataComportavel(inicio: Int, valor: Int): Int {
    for (dia in inicio..999) {
        val original = movimentos[dia].toList()
        movimentos[dia].add(Movimento(valor, dia, 'd', "Teste"))

        val resultado = valido()

        movimentos[dia].clear()
        movimentos[dia].addAll(original)

        if (resultado) return dia
    }

    return -1
}

fun main() {
    while (true) {
        val linha = readlnOrNull() ?: break
        val partes = linha.trim().split(Regex("\\s+"))
        if (partes.isEmpty() || partes[0].isEmpty()) continue

        when (partes[0]) {
            "i" -> inserir(
                partes[1].toInt(),
                partes[2].toInt(),
                partes[3].toInt(),
                partes[4][0],
                partes[5]
            )

            "l" -> listar(partes[1].toInt())

            "r" -> remover(partes[1].toInt(), partes[2].toInt())

            "b" -> println(saldo(partes[1].toInt()))

            "m" -> minimo = partes[1].toInt()

            "v" -> println(if (valido()) "Valido" else "Invalido")

            "w" -> {
                val dia = dataComportavel(
                    partes[1].toInt(),
                    partes[2].toInt()
                )
                println(if (dia == -1) "Incomportavel" else dia)
            }

            "exit" -> break
        }
    }
}
