package com.agonmati.tackexpenses.trackexpenses.adapters

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.agonmati.tackexpenses.trackexpenses.R
import com.agonmati.tackexpenses.trackexpenses.activities.CategoryDetailsActivity
import com.agonmati.tackexpenses.trackexpenses.activities.ExpenseActivity
import com.agonmati.tackexpenses.trackexpenses.entities.Category
import com.agonmati.tackexpenses.trackexpenses.entities.Expense
import com.agonmati.tackexpenses.trackexpenses.repositories.ExpenseRepository
import kotlinx.android.synthetic.main.category_item.view.*
import java.util.Date
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.agonmati.tackexpenses.trackexpenses.helpers.AddedExpense


/**
 * Created by agonmati on 2/10/18.
 */
class CategoryAdapter(categories:ArrayList<Category>,activity:ExpenseActivity) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>()  {
    var categories = categories
    val expenseActivity = activity
    val expenseAdded: AddedExpense = activity

    companion object {
        lateinit var date: Date
    }


    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent?.context).inflate(R.layout.category_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindView(categories[position],expenseActivity,expenseAdded)
        holder?.view?.setOnClickListener{
            val intent = Intent(expenseActivity, CategoryDetailsActivity::class.java)
            intent.putExtra("category_id", categories[position].id)
            intent.putExtra("date", CategoryAdapter.date)
            expenseActivity.startActivity(intent)
        }
    }

    class ViewHolder(v:View): RecyclerView.ViewHolder(v) {
        var view: View = v

        fun bindView(category:Category,activity: ExpenseActivity,expenseAdded: AddedExpense) {
            view.categoryName.text = category.name
            view.addExpenseBtn.setOnClickListener {
                if (view.fields.visibility == View.VISIBLE) {
                    view.fields.visibility = View.GONE
                }
                else {
                    view.fields.visibility = View.VISIBLE
                }
            }

            view.save.setOnClickListener{
                if (view.expensePrice.text.isEmpty()) {
                    view.expensePrice.setError("Price cannot be empty")
                }
                else {
                    var name = "no name"
                    if (!view.expenseName.text.isEmpty()){
                        name = view.expenseName.text.toString()
                    }
                    saveExpense(name,view.expensePrice.text.toString().toDouble(),category,expenseAdded,activity)

                }
            }

            view.expensePrice.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (view.expensePrice.text.isEmpty()) {
                        view.expensePrice.setError("Price cannot be empty")
                    }
                    else {
                        var name = "no name"
                        if (!view.expenseName.text.isEmpty()){
                            name = view.expenseName.text.toString()
                        }
                        saveExpense(name,view.expensePrice.text.toString().toDouble(),category,expenseAdded,activity)

                    }

                    return@OnKeyListener true
                }
                false
            })

        }

        fun saveExpense(name:String,price:Double,category: Category,expenseAdded: AddedExpense,activity: ExpenseActivity){
            var expense = Expense()
            expense.name = name
            expense.price = price
            expense.category = category.id
            expense.date = date.toString()
            val expenseRepository = ExpenseRepository()
            expenseRepository.insert(expense)

            expenseAdded.expenseAdded(expense)

            val g = activity.getCurrentFocus()
            if (view != null) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(g!!.getWindowToken(), 0)
            }
            view.expenseName.setText("")
            view.expensePrice.setText("")
            view.fields.visibility = View.GONE

        }
    }

}
