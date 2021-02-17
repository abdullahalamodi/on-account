package amo.a.onaccount.data.database

import amo.a.onaccount.data.models.Account
import amo.a.onaccount.data.models.Operation
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import java.util.*

@Dao
interface AccountsDao {

    //accounts
    @Query("SELECT * FROM Account")
    fun getAccounts(): LiveData<List<Account>>

    @Query("SELECT * FROM Account WHERE id=(:id)")
    fun getAccount(id: UUID): LiveData<Account>

    @Update
    fun updateAccount(account: Account)

    @Insert
    fun addAccount(account: Account)

    @Query("Delete from Account WHERE id=:id")
    fun deleteAccount(id: UUID)

//----------------------------------------------------------------------

    //transactions
    @Query("SELECT * FROM Operation")
    fun getTransactions(): LiveData<List<Operation>>

    @Query("SELECT * FROM Operation WHERE id=(:id)")
    fun getTransaction(id: UUID): LiveData<Operation>

    @Update
    fun updateTransaction(operation: Operation)

    @Insert
    fun addTransaction(operation: Operation)

    @Query("Delete from Operation WHERE id=:id")
    fun deleteTransaction(id: UUID)

    @Query("SELECT SUM(amount) FROM Operation WHERE account_id=(:id)")
    fun getTotalPrice(id: UUID): LiveData<Double>
}