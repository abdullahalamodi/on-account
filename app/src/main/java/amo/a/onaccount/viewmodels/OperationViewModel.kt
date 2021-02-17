package amo.a.onaccount.viewmodels

import amo.a.onaccount.data.AccountsRepository
import amo.a.onaccount.data.models.Operation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class OperationViewModel : ViewModel() {

    private val accountsRepository = AccountsRepository.get()

    private val transactionsListTrigger = MutableLiveData<Unit>()
    val transactionsListLiveData = Transformations.switchMap(transactionsListTrigger) {
        accountsRepository.getTransactions()
    }
    private val transactionTrigger = MutableLiveData<UUID>()
    val transactionLiveData = Transformations.switchMap(transactionTrigger) {id ->
        accountsRepository.getTransaction(id)
    }

    fun loadTransactionsList() {
        transactionsListTrigger.value = Unit
    }

    fun loadTransaction(id: UUID) {
        transactionTrigger.value = id
    }

    fun addTransaction(operation: Operation) {
        accountsRepository.addTransaction(operation)
    }

    fun updateTransaction(operation: Operation) {
        accountsRepository.updateTransaction(operation)
    }

    fun deleteTransaction(id:UUID) {
        accountsRepository.deleteTransaction(id)
    }


}