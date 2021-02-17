package amo.a.onaccount.ui.dialogs

import amo.a.onaccount.R
import amo.a.onaccount.data.models.Account
import amo.a.onaccount.data.models.Operation
import amo.a.onaccount.showMessage
import amo.a.onaccount.viewmodels.MainViewModel
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class AddAccountDialog(
    private val mainViewModel: MainViewModel,
    private val accountId: UUID? = null
) : DialogFragment() {

    //views
    private lateinit var nameView: EditText
    private lateinit var limitAmountView: EditText
    private lateinit var amountView: EditText
    private lateinit var detailsView: EditText
    private lateinit var rialYView: TextView
    private lateinit var rialSView: TextView
    private lateinit var dollarView: TextView
    private lateinit var contactsBtn: FloatingActionButton
    private lateinit var createBtn: TextView

    //    variables
    private var currency: String = Account.RIAL_Y

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = layoutInflater.inflate(R.layout.add_account_dialog, container, false)
        setView(view)
        changeCurrency()

        contactsBtn.setOnClickListener {

        }

        createBtn.setOnClickListener {
            if (validateFields()) {
                createAccountWithTransaction()
                mainViewModel.loadAccountsList()
                this.dismiss()
            } else {
                context?.showMessage("some fields are empty !")
            }
        }
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadAccountIfIsUpdate()
    }

    private fun loadAccountIfIsUpdate() {
        accountId?.let {
            mainViewModel.loadAccount()
            mainViewModel.accountLiveData.observe(
                viewLifecycleOwner,
                Observer { account ->
                    updateUI(account)
                }
            )
        }
    }

    private fun createAccountWithTransaction() {
        val name = nameView.text.toString()
        val limit = limitAmountView.text.toString().toDouble()
        val amount = amountView.text.toString().toDouble()
        val details = detailsView.text.toString()
        val account = Account(
            name = name,
            limit = limit,
            currency = currency,
            amount = amount
        )
        mainViewModel.createAccount(account)
        val transaction = Operation(
            amount = amount,
            details = details,
            account_id = account.id
        )
        mainViewModel.addTransaction(transaction)
    }

    private fun validateFields(): Boolean {
        return (
                nameView.text.isNotEmpty() &&
                        limitAmountView.text.isNotEmpty() &&
                        amountView.text.isNotEmpty() &&
                        detailsView.text.isNotEmpty()
                )
    }


    private fun updateUI(account: Account) {
        nameView.setText(account.name)
        limitAmountView.setText(account.limit.toString())
    }

    private fun changeCurrency() {
        rialYView.setOnClickListener {
            currency = Account.RIAL_Y
            //rial_y
            rialYView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back_active)
            rialYView.setTextColor(Color.WHITE)
            //rial_s
            rialSView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back)
            rialSView.setTextColor(resources.getColor(R.color.accent_color))
            //dollar
            dollarView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back)
            dollarView.setTextColor(resources.getColor(R.color.accent_color))
        }

        rialSView.setOnClickListener {
            currency = Account.RIAL_S
            //rial_s
            rialSView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back_active)
            rialSView.setTextColor(Color.WHITE)
            //rial_y
            rialYView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back)
            rialYView.setTextColor(resources.getColor(R.color.accent_color))
            //dollar
            dollarView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back)
            dollarView.setTextColor(resources.getColor(R.color.accent_color))
        }

        dollarView.setOnClickListener {
            currency = Account.DOLLAR
            //dollar
            dollarView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back_active)
            dollarView.setTextColor(Color.WHITE)
            //rial_s
            rialSView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back)
            rialSView.setTextColor(resources.getColor(R.color.accent_color))
            //rial_y
            rialYView.background = resources.getDrawable(R.drawable.ic_price_cyrcle_back)
            rialYView.setTextColor(resources.getColor(R.color.accent_color))
        }

    }


    companion object {
        fun newInstance(
            mainViewModel: MainViewModel,
            accountId: UUID?
        ) = AddAccountDialog(mainViewModel, accountId)
    }

    private fun setView(view: View) {
        nameView = view.findViewById(R.id.name_et)
        limitAmountView = view.findViewById(R.id.limit_amount_et)
        amountView = view.findViewById(R.id.amount_et)
        detailsView = view.findViewById(R.id.details_et)
        rialYView = view.findViewById(R.id.rial_y_tv)
        rialSView = view.findViewById(R.id.rial_s_tv)
        dollarView = view.findViewById(R.id.dollar_tv)
        contactsBtn = view.findViewById(R.id.contacts_btn)
        createBtn = view.findViewById(R.id.create_btn)
    }
}