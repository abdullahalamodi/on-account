package amo.a.onaccount.viewmodels

import amo.a.onaccount.data.AccountsRepository
import amo.a.onaccount.data.models.Account
import amo.a.onaccount.data.models.Operation
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel : ViewModel() {

    private val accountsRepository = AccountsRepository.get()

    private val accountsTrigger = MutableLiveData<Unit>()
    val accountsListLiveData = Transformations.switchMap(accountsTrigger) {
        accountsRepository.getAccounts()
    }
    private val accountTrigger = MutableLiveData<UUID>()
    val accountLiveData = Transformations.switchMap(accountTrigger) {
        accountTrigger.value?.let { id -> accountsRepository.getAccount(id) }
    }

    fun loadAccountsList() {
        accountsTrigger.value = Unit
    }

    fun loadAccount() {
        accountsTrigger.value = Unit
    }

    fun createAccount(account: Account) {
        accountsRepository.addAccount(account)
    }

    fun updateAccount(account: Account) {
        accountsRepository.updateAccount(account)
    }

    fun deleteAccount(id: UUID) {
        accountsRepository.deleteAccount(id)
    }

    fun addTransaction(operation: Operation) {
        accountsRepository.addTransaction(operation)
    }

}