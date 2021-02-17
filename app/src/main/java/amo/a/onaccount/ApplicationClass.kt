package amo.a.onaccount

import amo.a.onaccount.data.AccountsRepository
import android.app.Application

class ApplicationClass:Application() {

    override fun onCreate() {
        super.onCreate()
        AccountsRepository.initialize(applicationContext)
    }
}