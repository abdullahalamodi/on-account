package amo.a.onaccount.ui.dialogs

import amo.a.onaccount.R
import amo.a.onaccount.data.models.Operation
import amo.a.onaccount.showMessage
import amo.a.onaccount.viewmodels.OperationViewModel
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
import java.util.*

class AddOperationDialog(
    private val operationViewModel: OperationViewModel,
    private val accountId: UUID,
    private val operationId: UUID?
) : DialogFragment() {


    private lateinit var amountView: EditText
    private lateinit var detailsView: EditText
    private lateinit var positiveView: TextView
    private lateinit var negativeView: TextView
    private lateinit var addBtn: TextView
    private var type = 1

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
        val view = layoutInflater.inflate(R.layout.add_transaction_dialog, container, false)
        setView(view)
        selectType()

        addBtn.setOnClickListener {
            if (validateFields()) {
                addOperation()
                operationViewModel.loadTransactionsList()
                this.dismiss()
            } else {
                context?.showMessage("some fields are empty !")
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadOperationIfIsUpdate()
    }

    private fun loadOperationIfIsUpdate() {
        operationId?.let {
            operationViewModel.loadTransaction(it)
            operationViewModel.transactionLiveData.observe(
                viewLifecycleOwner,
                Observer { operation ->
                    updateUI(operation)
                }
            )
        }
    }

    private fun updateUI(operation: Operation) {
        amountView.setText(operation.amount.toString())
        detailsView.setText(operation.details)
        setType(operation.type)
        addBtn.text = "تعديل"
    }

    private fun setType(type: Int) {
        this.type = type
        if (type == 1) {
            //positive
            positiveView.background = resources.getDrawable(R.drawable.ic_positive_active_back)
            positiveView.setTextColor(Color.WHITE)
            //negative
            negativeView.background = resources.getDrawable(R.drawable.ic_negative_back)
            negativeView.setTextColor(Color.RED)
        } else {
            negativeView.background = resources.getDrawable(R.drawable.ic_negative_active_back)
            negativeView.setTextColor(Color.WHITE)
            //negative
            positiveView.background = resources.getDrawable(R.drawable.ic_positive_back)
            positiveView.setTextColor(resources.getColor(R.color.accent_color))
        }

    }

    private fun selectType() {
        positiveView.setOnClickListener {
            type = 1
            //positive
            positiveView.background = resources.getDrawable(R.drawable.ic_positive_active_back)
            positiveView.setTextColor(Color.WHITE)
            //negative
            negativeView.background = resources.getDrawable(R.drawable.ic_negative_back)
            negativeView.setTextColor(Color.RED)
        }

        negativeView.setOnClickListener {
            type = 0
            //positive
            negativeView.background = resources.getDrawable(R.drawable.ic_negative_active_back)
            negativeView.setTextColor(Color.WHITE)
            //negative
            positiveView.background = resources.getDrawable(R.drawable.ic_positive_back)
            positiveView.setTextColor(resources.getColor(R.color.accent_color))
        }

    }


    private fun addOperation() {
        val operation = Operation(
            amount = amountView.text.toString().toDouble(),
            details = detailsView.text.toString(),
            type = type,
            account_id = accountId
        )
        operationViewModel.addTransaction(operation)
    }

    private fun validateFields(): Boolean {
        return (amountView.text.isNotEmpty() &&
                detailsView.text.isNotEmpty())
    }

    companion object {
        fun newInstance(
            operationViewModel: OperationViewModel,
            accountId: UUID,
            operationId: UUID? = null
        ) =
            AddOperationDialog(operationViewModel,accountId, operationId)
    }

    private fun setView(view: View) {
        amountView = view.findViewById(R.id.amount_et)
        detailsView = view.findViewById(R.id.details_et)
        positiveView = view.findViewById(R.id.positive_tv)
        negativeView = view.findViewById(R.id.negative_tv)
        addBtn = view.findViewById(R.id.add_btn)

    }
}