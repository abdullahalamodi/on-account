package amo.a.onaccount.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Operation(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val amount: Double,
    val date: String = Date().toString(),
    val details: String,
    val type: Int = 1, //1  add, discount 0
    val edited: Boolean = false,
    val account_id: UUID
) {

    companion object {
        fun getAccountAmount(operations: List<Operation>): Double {
            var accountAmount = 0.0
            operations.forEach { transaction ->
                accountAmount += transaction.amount
            }
            return accountAmount
        }
    }
}