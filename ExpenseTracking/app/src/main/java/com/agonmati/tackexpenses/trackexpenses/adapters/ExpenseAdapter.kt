package com.agonmati.tackexpenses.trackexpenses.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agonmati.tackexpenses.trackexpenses.R
import com.agonmati.tackexpenses.trackexpenses.entities.Expense
import kotlinx.android.synthetic.main.expense_details_item.view.*
import java.util.*

/**
 * Created by agonmati on 2/12/18.
 */
class ExpenseAdapter (expenses:ArrayList<Expense>) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>()  {
    var expenses = expenses
    override fun getItemCount(): Int {

        return expenses.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent?.context).inflate(R.layout.expense_details_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindView(expenses[position])
    }

    class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v

        fun bindView(expenses: Expense) {
            view.name.text = expenses.name
            view.expensePrice.text = expenses.price.toString() + "$"

            val date = expenses.date!!.split(" ")
            view.date.text = date[0] + " " + date[1] +" "+ date[2]
        }

    }


}