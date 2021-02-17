package amo.a.onaccount.ui.fragments

import amo.a.onaccount.R
import amo.a.onaccount.data.models.Operation
import amo.a.onaccount.showMessage
import amo.a.onaccount.ui.dialogs.AddOperationDialog
import amo.a.onaccount.viewmodels.OperationViewModel
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

private const val ACCOUNT_ID = "account_id"
private const val ACCOUNT_NAME = "account_name"

class OperationFragment : Fragment() {

    private lateinit var operationViewModel: OperationViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var callbacks: MainFragment.Callbacks
    private var transactionAdapter = OperationAdapter(emptyList())
    private var operationList: List<Operation> = emptyList()
    private lateinit var nameView: TextView
    private lateinit var numView: TextView
    private lateinit var totalView: TextView
    private var accountId: UUID? = null
    private var accountName: String? = null


    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        callbacks = (context as MainFragment.Callbacks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        operationViewModel = ViewModelProvider(this).get(OperationViewModel::class.java)
        operationViewModel.loadTransactionsList()
        arguments?.let {
            accountId = it.getSerializable(ACCOUNT_ID) as UUID
            accountName = it.getString(ACCOUNT_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transuction, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = transactionAdapter
        nameView = view.findViewById(R.id.name_tv)
        numView = view.findViewById(R.id.num_tv)
        totalView = view.findViewById(R.id.total_tv)

        //pupUp menu
        view.findViewById<ImageButton>(R.id.menu_btn).setOnClickListener {
            showPopup(it)
        }
        //add button
        view.findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            AddOperationDialog.newInstance(
                operationViewModel,
                accountId!!
            ).apply {
                show(this@OperationFragment.parentFragmentManager, tag)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountId?.let {
            operationViewModel.loadTransactionsList()
            operationViewModel.transactionsListLiveData.observe(
                viewLifecycleOwner,
                Observer { transactionsList ->
                    updateUI(transactionsList)
                }
            )
        }
    }


    private fun updateUI(operations: List<Operation>) {
        transactionAdapter = OperationAdapter(operations)
        recyclerView.adapter = transactionAdapter
        updateMainCard(operations)
    }

    private fun updateMainCard(operations: List<Operation>) {
        nameView.text = accountName
        numView.text = operations.size.toString()
        totalView.text = Operation.getAccountAmount(operations).toString()
    }


    private fun showPopup(v: View) {
        PopupMenu(requireContext(), v).apply {
            setOnMenuItemClickListener { item ->
                return@setOnMenuItemClickListener when (item?.itemId) {
                    R.id.about_item -> {
                        context?.showMessage("about")
                        true
                    }
                    else -> false
                }
            }
            inflate(R.menu.main_menu)
            show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(accountId: UUID,accountName:String) = OperationFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ACCOUNT_ID, accountId)
                putString(ACCOUNT_NAME, accountName)
            }
        }
    }

    //------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------


    private inner class OperationHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {
        val nameView: TextView = view.findViewById(R.id.details_tv)
        val priceView: TextView = view.findViewById(R.id.amount_et)
        val dateView: TextView = view.findViewById(R.id.date_tv)
        val editedView: TextView = view.findViewById(R.id.edited_tv)
        val editedImg: ImageView = view.findViewById(R.id.edited_img)
        val stateImg: ImageView = view.findViewById(R.id.state_img)

        fun bind(operation: Operation) {
            nameView.text = operation.details
            priceView.text = operation.amount.toString()
            dateView.text = operation.date
            isEdited(operation.edited)
            setTransactionState(operation.type)

        }

        fun isEdited(edited: Boolean) {
            if (edited) {
                editedImg.visibility = View.VISIBLE
                editedView.visibility = View.VISIBLE
            } else {
                editedImg.visibility = View.INVISIBLE
                editedView.visibility = View.INVISIBLE
            }
        }

        fun setTransactionState(type: Int) {
            if (type == 1) {
                stateImg.setImageResource(R.drawable.ic_transaction)
                priceView.setTextColor(resources.getColor(R.color.accent_color))
            } else {
                stateImg.setImageResource(R.drawable.ic_transaction_n)
                priceView.setTextColor(Color.RED)
            }
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            AddOperationDialog.newInstance(
                operationViewModel,
                accountId!!,
                operationList[adapterPosition].id
            ).apply {
                show(this@OperationFragment.parentFragmentManager, tag)
            }
        }
    }

    private inner class OperationAdapter(val operations: List<Operation>) :
        RecyclerView.Adapter<OperationHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationHolder {
            val view = layoutInflater.inflate(
                R.layout.transaction_item_list,
                parent,
                false
            )
            return OperationHolder(view)
        }

        override fun getItemCount(): Int = operations.size

        override fun onBindViewHolder(holder: OperationHolder, position: Int) {
            operationList = operations
            holder.bind(operations[position])
        }

    }
}