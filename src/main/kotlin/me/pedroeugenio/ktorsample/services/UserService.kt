package me.pedroeugenio.ktorsample.services

import me.pedroeugenio.ktorsample.dao.EmpDAO
import me.pedroeugenio.ktorsample.dao.UserDAO
import me.pedroeugenio.ktorsample.models.Person
import org.jetbrains.exposed.sql.innerJoin
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Connection

class UserService {

    fun getAll(): List<Person> {
        return transaction(Connection.TRANSACTION_REPEATABLE_READ, 3) {
            return@transaction EmpDAO
                .innerJoin(UserDAO, { no }, { no })
                .slice(EmpDAO.no, UserDAO.login, UserDAO.name)
                .selectAll()
                .map {
                    Person(it[EmpDAO.no], it[UserDAO.login], it[UserDAO.name])
                }
        }
    }

    fun findByNo(findNo: Int): Person {
        transaction {
            val conn = TransactionManager.current().connection
            val query = """
                SELECT
            inv.invno AS numerointerno
            ,inv.nfname AS nota
            ,(SELECT vend.name FROM sqldados.vend WHERE vend.no = inv.vendno) AS fornecedor
            ,invnfe.nfekey AS chave
            ,inv.storeno AS LojaDest
            ,IF(inv.nfStoreno = 0, 'NAO E TRANSFERENCIA', inv.nfStoreno) AS LojaOrig
            ,(SELECT vend.cgc FROM sqldados.vend WHERE vend.no = inv.vendno) AS CNPJ
            ,(inv.grossamt - inv.discount) AS Valor
            ,iprd.prdno
            ,iprd.grade
            ,LEFT(iprd.grade, (SELECT grade.length FROM sqldados.grade WHERE grade.no = TRIM(LEFT(prd.grade_l,10)))) AS cor
            ,TRIM(LEFT(prd.name,38)) AS descricao
            ,prd.mfno_ref AS referencia
            ,vend.sname AS marca
            ,ROUND(iprd.qtty/1000) AS quantidade
            ,IFNULL(ROUND(((stk.qtty_varejo+stk.qtty_atacado)/1000)),0) AS qtdGrade
            ,IFNULL(ROUND(((devf.qtty_varejo+devf.qtty_atacado)/1000)),0) AS qtdDefeito
            ,GROUP_CONCAT(TRIM(prdbar.barcode)) AS barcode
            ,prp.rplu AS dt_marcacao
            ,IFNULL(prp.refprice,0)                                                                          AS prc_ref_retaguarda
            ,IFNULL(promoprd.price,0)                                                                        AS prc_promocional
            ,IFNULL(prp.sp_atacado,0)                                                                        AS prc_atacado
            ,IFNULL(IF(prp.promo_validate < DATE_FORMAT(CURDATE(), '%Y%m%d'), 0, prp.promo_price),0)    AS prc_neocard_retaguarda
            ,IFNULL(prdstk.price,0) AS prc_ref_pdv
            ,IFNULL(IF(prdstk.sp_promo_date < DATE_FORMAT(CURDATE(), '%Y%m%d'), 0, prdstk.sp_promo),0)AS prc_neocard_pdv
            ,inv.auxShort3
            FROM sqldados.inv
            INNER JOIN sqldados.iprd
            ON inv.storeno = iprd.storeno
            AND inv.invno = iprd.invno
            INNER JOIN sqldados.prd
            ON iprd.prdno = prd.no
            INNER JOIN sqldados.vend
            ON vend.no = prd.mfno
            INNER JOIN sqldados.invnfe
            ON invnfe.invno = inv.invno
            LEFT JOIN sqldados.prdbar
            ON iprd.prdno = prdbar.prdno
            AND iprd.grade = prdbar.grade
            LEFT JOIN sqldados.prp
            ON iprd.storeno = prp.storeno
            AND iprd.prdno = prp.prdno
            LEFT JOIN sqldados.promoprd
            ON promoprd.promono = iprd.storeno
            AND promoprd.prdno = iprd.prdno
            AND promoprd.grade = iprd.grade
            LEFT JOIN sqldados.stk
            ON stk.storeno = iprd.storeno
            AND stk.prdno = iprd.prdno
            AND stk.grade = iprd.grade
            LEFT JOIN sqldados.devf
            ON devf.storeno = iprd.storeno
            AND devf.prdno = iprd.prdno
            AND devf.grade = iprd.grade
            LEFT JOIN sqlpdv.prdstk
            ON stk.storeno = prdstk.storeno
            AND stk.prdno = prdstk.prdno
            AND stk.grade = prdstk.grade
            AND prd.no = prdstk.prdno
            AND prd.mfno_ref = prdstk.mfno_ref
            WHERE inv.date BETWEEN DATE_SUB(CURDATE()+0, INTERVAL 30 DAY) AND CURDATE()+0
            AND inv.nfStoreno <> inv.storeno
            GROUP BY inv.storeno,inv.invno,iprd.prdno,iprd.grade 
            LIMIT 1
            """.trimIndent()
            val statement = conn.prepareStatement(query, false)
            println(statement)

        }
        return transaction(Connection.TRANSACTION_REPEATABLE_READ, 3) {
            return@transaction EmpDAO
                .innerJoin(UserDAO, { no }, { no })
                .slice(EmpDAO.no, UserDAO.login, UserDAO.name)
                .select { EmpDAO.no eq findNo }
                .map {
                    Person(it[EmpDAO.no], it[UserDAO.login], it[UserDAO.name])
                }.first()
        }
    }
}