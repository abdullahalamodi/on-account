package amo.a.onaccount.data

import amo.a.onaccount.data.database.AccountsDatabase
import amo.a.onaccount.data.models.Account
import amo.a.onaccount.data.models.Operation
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "on_accounts_db"

class AccountsRepository private constructor(context: Context) {

    private val database: AccountsDatabase = Room.databaseBuilder(
        context.applicationContext,
        AccountsDatabase::class.java,
        DATABASE_NAME
    )
        .build()

    private val accountsDao = database.accountsDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getAccounts(): LiveData<List<Account>> = accountsDao.getAccounts()
    fun getAccount(id: UUID): LiveData<Account> = accountsDao.getAccount(id)

    fun addAccount(account: Account) {
        executor.execute {
            accountsDao.addAccount(account)
        }
    }

    fun updateAccount(account: Account) {
        executor.execute {
            accountsDao.updateAccount(account)
        }
    }

    fun deleteAccount(id: UUID) {
        executor.execute {
            accountsDao.deleteAccount(id)
        }
    }
    //-------------------------------------------------------------------------

    fun getTransactions(): LiveData<List<Operation>> = accountsDao.getTransactions()
    fun getTransaction(id: UUID): LiveData<Operation> = accountsDao.getTransaction(id)

    fun addTransaction(operation: Operation) {
        executor.execute {
            accountsDao.addTransaction(operation)
        }
    }

    fun updateTransaction(operation: Operation) {
        executor.execute {
            accountsDao.updateTransaction(operation)
        }
    }

    fun deleteTransaction(id: UUID) {
        executor.execute {
            accountsDao.deleteTransaction(id)
        }
    }


    //-----------------------------------------------------------------------
    companion object {
        private var INSTANCE: AccountsRepository? = null;

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = AccountsRepository(context)
            }
        }

        fun get(): AccountsRepository {
            return INSTANCE
                ?: throw IllegalStateException("amo.a.onaccount.TasksRepository must be initialized")
        }
    }
}