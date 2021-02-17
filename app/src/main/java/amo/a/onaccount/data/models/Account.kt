package amo.a.onaccount.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
@Entity
data class Account(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    val name: String,
    val currency: String = "r_y",
    val limit: Double = -1.0,
    val amount:Double = 0.0,
    val isActive:Boolean = true
) {

    companion object {
        fun getTotalAmount(accounts: List<Account>):Double {
            var totalAmount = 0.0
            accounts.forEach { account ->
                totalAmount += account.amount
            }
            return totalAmount
        }

        const val RIAL_Y = "r_y"
        const val RIAL_S = "r_s"
        const val DOLLAR = "dollar"
    }
}