package amo.a.onaccount.ui.fragments

import amo.a.onaccount.R
import amo.a.onaccount.data.models.Account
import amo.a.onaccount.showMessage
import amo.a.onaccount.ui.dialogs.AddAccountDialog
import amo.a.onaccount.viewmodels.MainViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class MainFragment : Fragment() {


    private lateinit var mainViewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var callbacks: Callbacks
    private var accountAdapter = AccountAdapter(emptyList())
    private var accountsList: List<Account> = emptyList()
    private lateinit var numView: TextView
    private lateinit var totalView: TextView


    override fun onStart() {
        super.onStart()
        setHasOptionsMenu(true)
        callbacks = (context as Callbacks)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.loadAccountsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        setViews(view)
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = accountAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainViewModel.accountsListLiveData.observe(
            viewLifecycleOwner,
            Observer { accountsList ->
                updateUI(accountsList)
            }
        )
    }

    private fun updateUI(accounts: List<Account>) {
        accountAdapter = AccountAdapter(accounts)
        recyclerView.adapter = accountAdapter
        updateMainCard(accounts)
    }

    private fun updateMainCard(accounts: List<Account>) {
        numView.text = accounts.size.toString()
        totalView.text = Account.getTotalAmount(accounts).toString()
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
        fun newInstance() = MainFragment()
    }

    interface Callbacks {
        fun onItemSelected(accountId: UUID, accountName: String)
    }


    //------------------------------------------------------------------------------------------------


    private inner class AccountHolder(view: View) : RecyclerView.ViewHolder(view)
        , View.OnClickListener {
        val nameView: TextView = view.findViewById(R.id.name_et)
        val amountView: TextView = view.findViewById(R.id.amount_et)

        fun bind(account: Account) {
            nameView.text = account.name
            amountView.text = account.amount.toString()
        }

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            callbacks.onItemSelected(
                accountsList[adapterPosition].id,
                accountsList[adapterPosition].name
            )
        }

    }

    private inner class AccountAdapter(val accounts: List<Account>) :
        RecyclerView.Adapter<AccountHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHolder {
            val view = layoutInflater.inflate(
                R.layout.main_item_list,
                parent,
                false
            )
            return AccountHolder(view)
        }

        override fun getItemCount(): Int = accounts.size

        override fun onBindViewHolder(holder: AccountHolder, position: Int) {
            accountsList = accounts
            holder.bind(accounts[position])
        }

    }


    private fun setViews(view: View) {
        recyclerView = view.findViewById(R.id.recycler_view)
        numView = view.findViewById(R.id.num_tv)
        totalView = view.findViewById(R.id.total_tv)

        //pupUp menu
        view.findViewById<ImageButton>(R.id.menu_btn).setOnClickListener {
            showPopup(it)
        }
        //add button
        view.findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            AddAccountDialog.newInstance(mainViewModel, null).apply {
                show(this@MainFragment.parentFragmentManager, tag)
            }
        }
    }

}