package com.agonmati.tackexpenses.trackexpenses.activities

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.View
import com.agonmati.tackexpenses.trackexpenses.R
import com.agonmati.tackexpenses.trackexpenses.adapters.CategoryAdapter
import com.agonmati.tackexpenses.trackexpenses.adapters.ExpenseAdapter
import com.agonmati.tackexpenses.trackexpenses.entities.Category
import com.agonmati.tackexpenses.trackexpenses.entities.Expense
import com.agonmati.tackexpenses.trackexpenses.helpers.SwipeToDeleteCallback
import com.agonmati.tackexpenses.trackexpenses.repositories.CategoryRepository
import com.agonmati.tackexpenses.trackexpenses.repositories.ExpenseRepository
import kotlinx.android.synthetic.main.activity_category_details.*
import java.text.FieldPosition
import java.util.*

class CategoryDetailsActivity : AppCompatActivity() {
    private var expenses: ArrayList<Expense> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter:ExpenseAdapter
    private lateinit var categoryRepository:CategoryRepository
    private lateinit var expenseRepository:ExpenseRepository
    private lateinit var category:Category
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_details)
        expenseRepository = ExpenseRepository()
        categoryRepository = CategoryRepository()

        val categoryId:Int = intent.getIntExtra("category_id",1)
        category = categoryRepository.get(categoryId);

        activity_title.text = category.name

        getData()


        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = ExpenseAdapter(expenses)
        recyclerView.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletePressed(expenses[viewHolder.adapterPosition],viewHolder.adapterPosition)


            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    fun deletePressed(expense: Expense,position: Int){
        val dialogBuilder = AlertDialog.Builder(this)
//        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
//        dialogBuilder.setView(dialogView)
//
//        val categoryname = dialogView.findViewById<EditText>(R.id.category_name)

        dialogBuilder.setTitle("Are you sure you want to delete expense: "+ expense.name)
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, whichButton ->
            expenses.remove(expense)
            expenseRepository.deleteExpenses(expense)
            adapter.expenses = expenses
            adapter.notifyItemRemoved(position)

            getData()
        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
            adapter.notifyDataSetChanged()
        })
        val b = dialogBuilder.create()
        b.show()


    }



    fun getData(){
        expenses = expenseRepository.getAllForMonthAndCategory(category,CategoryAdapter.date);
        if (expenses.isEmpty()){
            recyclerView.visibility = View.GONE
            noExpense.visibility = View.VISIBLE
        }
        else {
            recyclerView.visibility = View.VISIBLE
            noExpense.visibility = View.GONE
        }
        var sum = 0.0
        for (exp in expenses) {
            sum += exp.price
        }
        total.text = sum.toString() + "$"

    }
}
