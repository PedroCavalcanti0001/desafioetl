package me.pedroeugenio.ktorsample

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.pedroeugenio.ktorsample.extensions.isKeyword
import me.pedroeugenio.ktorsample.plugins.configureHTTP
import me.pedroeugenio.ktorsample.routes.userRout
import me.pedroeugenio.ktorsample.services.UserService
import me.pedroeugenio.ktorsample.utils.MysqlTableToExposedObject
import org.jetbrains.exposed.sql.transactions.transaction

fun main() {
    Database.connect()

    transaction {
        MysqlTableToExposedObject.run(database = "sqldados", table = "users")
        MysqlTableToExposedObject.run(database = "sqldados", table = "stk")
        MysqlTableToExposedObject.run(database = "sqldados", table = "emp")
        MysqlTableToExposedObject.run(database = "sqldados", table = "prd")
        MysqlTableToExposedObject.run(database = "sqldados", table = "inv")
        MysqlTableToExposedObject.run(database = "sqldados", table = "spc")
        MysqlTableToExposedObject.run(database = "sqldados", table = "custp")
        MysqlTableToExposedObject.run(database = "sqldados", table = "invnfe2")
        MysqlTableToExposedObject.run(database = "sqldados", table = "mblistinv")
        //MysqlTableToExposedObject.run(database = "sqldados", table = "users")
        //StkDao.select { (StkDao.qtty_atacado+ StkDao.qtty_varejo) neq 0 }.forEach {
        //    println("it $it")
        // }
    }

    /*
     transaction {
        val list = arrayOf(
        "CD e Compras foram Informados sobre o (INVENTÁRIO)?",
        "Contagem dos produtos com defeitos foi realizada? (INVENTÁRIO)",
        "Produtos com defeitos estão lançados no sistema?",
        "Os produtos com defeitos estão separados e organizados no estoque da área de produto de revenda?",
        "Há pendências com notas fiscais de devolução de produtos com defeitos?",
        "Os produtos com defeitos sem estoque, foram analisados?",
        "Todas as notas fiscais de devolução estão separadas da area de defeitos e produtos de revenda?",
        "Contagem dos Perfumes (INVENTÁRIO)",
        "Contagem dos relógios (INVENTÁRIO)",
        "Há Relógios em manutenção e/ou enviados para o fornecedor?",
        "Os computadores da unidade, já foram instalados os drivers do coletor?",
        "Estoque limpo e organizado?",
        "Produtos de vitrine com o segundo pé no estoque em sua devida caixa identificada com VT?",
        "Informar ao CD novamente data do (INVENTÁRIO).",
        "Todos os produtos que estão fora da loja, saíram com NFAgência de moda e/ou conserto).",
        "Estoques Negativos no sistema resolvido?",
        "Lançamento de NF´s com origem do fornecedor foram realizadas?",
        "Notas Fiscais em trânsito foram recebidas?",
        "Há notas fiscais em trânsito com mais de 10 dias sem dar entrada?",
        "Há pendências/divergências com notas fiscais em trânsito a serem resolvidas?",
        "Os pés perdidos e trocados foram tratados?",
        "Há pendências de notas fiscais com o Conciliação de Caixa?",
        "Há notas fiscais emitidassaída ou entrada) em duplicidade?",
        "Há notas fiscais que foram emitidas e ocorreu erro na SEFAZ ou SACI?",
        "Bonificação/Brindes foram realizados no sistema, dando saída do estoque?",
        "Os produtos para bonificação e que constam no estoque, estão etiquetados?",
        "Há pendências/divergências com notas fiscais em trânsito a serem resolvidas?",
        "Reforçar informação ao CD sobre data do (INVENTÁRIO) e Efetivação.",
        "Lançamento de NF´s com origem do fornecedor foram realizadas?",
        "Suporte TI informado sobre o (INVENTÁRIO)?",
        "Os computadores da loja possuem excel na versão 2007 ou melhor?",
        "Foi dada a folga durante a semana para o estoquista que vai participar do (INVENTÁRIO)?",
        "Estoque limpo e organizado?",
        "Calçados em prateleiras estão alinhados?",
        "Acessórios e Chinelos estão organizados em seus respectivos expositores para Contagem?",
        "Foi verificado se há caixas de calçados escondidas/caídas por trás  ou em baixo das prateleiras, que não estejam na área de contagem?",
        "Foi realizada coleta geral averiguando a situação das etiquetas dos produtos de salão e estoque?",
        "Resolução dos produtos sem etiquetas, etiquetas rasuradas, e/ou códigos de barras incompletos?,",
        "Análise e resolução dos produtos de revenda sem estoque.",
        "Os produtos de revenda sem estoque estão analisados e separados e organizados para contagem?",
        "Todos os produtos sem códigos foram resolvidos?",
        "Se  não, quantos ainda possui na loja?",
        "Perfumesamostras/testers) estão etiquetados corretamente?",
        "Relógios estão etiquetados corretamente?",
        "Produtos em caixas em cima de prateleiras ou sala da gerência ou sala do gecred?",
        "Há Nfs sem lançamento?",
        "Houve lançamentos de NF's que ainda possui pendências?",
        "Marcação de células dos Defeitos e Estoque.",
        "Há pendências/divergências com notas fiscais em trânsito a serem resolvidas?",
        "Estoque limpo e organizado?",
        "Produtos em prateleiras estão alinhados?",
        "Acessórios e Chinelos estão organizados em seus respectivos expositores para Contagem?",
        "Pré balançao de pilhas de calçados no salão de vendas.",
        "Há Nfs sem lançamento?",
        "Marcação de células do salão de vendas",
        "Todos os pares, com exceção dos pés de vitrine ou quadro visuais e chinelos estão casados em suas respectivas caixas?",
        "As reservas de cliente foram retiradas ou organizadas para contagem?",
        "Os produtos do caixa foram retirados para marcação de células? ",
        "Acessórios retirados das vitrines?",
        "Coletores estão carregados a bateria?",
        "Foi transmitido aos coletores a tabela de estoque?",
        "Os locais para os produtos sem etiqueta e desconhecidos durante o (INVENTÁRIO) já foi definido e está identificado?"
        )
        val conn = TransactionManager.current().connection

        for (s in list) {
           println(s)
           val query = "INSERT INTO app.auditquest_perguntas(texto) VALUES ('${s.replace("'", "")}')"
           println(query)
           val statement = conn.prepareStatement(query, false)
           statement.executeUpdate()
        }

     }

     */


    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(Routing) {
            userRout(UserService())
        }
        install(ContentNegotiation) {
            gson()
        }
        //configureSecurity()
        configureHTTP()
    }.start(wait = true)
}
