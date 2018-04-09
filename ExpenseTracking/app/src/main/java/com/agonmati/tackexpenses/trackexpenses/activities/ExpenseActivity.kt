package com.agonmati.tackexpenses.trackexpenses.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.agonmati.tackexpenses.trackexpenses.R
import com.agonmati.tackexpenses.trackexpenses.entities.Category
import java.util.*
import com.agonmati.tackexpenses.trackexpenses.adapters.CategoryAdapter
import com.agonmati.tackexpenses.trackexpenses.entities.Expense
import com.agonmati.tackexpenses.trackexpenses.helpers.AddedExpense
import kotlinx.android.synthetic.main.activity_expense.*
import com.agonmati.tackexpenses.trackexpenses.repositories.CategoryRepository
import com.agonmati.tackexpenses.trackexpenses.repositories.ExpenseRepository
import kotlin.collections.ArrayList
import android.content.DialogInterface
import android.widget.Toast
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.EditText
import android.view.LayoutInflater
import android.view.View
import android.widget.AbsListView
import com.agonmati.tackexpenses.trackexpenses.helpers.SwipeToDeleteCallback
import java.text.SimpleDateFormat


class ExpenseActivity : AppCompatActivity(),AddedExpense {

    private lateinit var expenses:ArrayList<Expense>
    private var categories: ArrayList<Category> = ArrayList()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter:CategoryAdapter
    private lateinit var categoryRepository:CategoryRepository
    private lateinit var expenseRepository:ExpenseRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense)
        categoryRepository = CategoryRepository()
        expenseRepository = ExpenseRepository()
        CategoryAdapter.date = Date()



        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = CategoryAdapter(categories,this)

        recyclerView.adapter = adapter
        getData()
        val swipeHandler = object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                deletePressed(categories[viewHolder.adapterPosition],viewHolder.adapterPosition)


            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        setUpMonthsForFilter()

        filterButton.setOnClickListener{
            filterPressed()
        }


        fab.setOnClickListener{
            addCategoryPressed()
            Log.d("Test","Test")
        }

        sortView.visibility = View.GONE

        firstMonth.setOnClickListener{
            firstMonthPressed()
        }

        secondMonth.setOnClickListener{
            secondMonthPressed()
        }

        thirdMonth.setOnClickListener{
            thirdMonthPressed()
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                sortView.visibility = View.GONE
            }
        })


    }

    fun setUpMonthsForFilter(){
        var dateFormat = SimpleDateFormat("MMMM.dd");
        var fM = Date()
        val c = Calendar.getInstance()
        c.time = fM
        firstMonth.text = dateFormat.format(c.time).split(".")[0]

        c.add(Calendar.MONTH,-1)
        secondMonth.text = dateFormat.format(c.time).split(".")[0]

        c.add(Calendar.MONTH,-1)
        thirdMonth.text = dateFormat.format(c.time).split(".")[0]
        val test = c.time
    }

    fun getData(){
        categories = categoryRepository.getAll()
        if (categories.isEmpty()){
            var utilities = Category()
            utilities.name = "Utilities"
            var food = Category()
            food.name = "Food and Groceries"
            var clothing = Category()
            clothing.name = "Clothing"
            var entertainment = Category()
            entertainment.name = "Entertainment"
            var transportation = Category()
            transportation.name = "Transportation"
            var education = Category()
            education.name = "Education"
            var medical = Category()
            medical.name = "Medical"
            var leisure = Category()
            leisure.name = "Leisure"

            categoryRepository.insert(utilities)
            categoryRepository.insert(food)
            categoryRepository.insert(clothing)
            categoryRepository.insert(entertainment)
            categoryRepository.insert(transportation)
            categoryRepository.insert(education)
            categoryRepository.insert(medical)
            categoryRepository.insert(leisure)

            categories = categoryRepository.getAll()

        }

        expenses = expenseRepository.getAllForMonth(CategoryAdapter.date)
        var sum = 0.0
        for (exp in expenses) {
            sum += exp.price
        }
        total.text = sum.toString() + "$"
        adapter.categories = categories
        adapter.notifyDataSetChanged()
    }

    override fun expenseAdded(expense: Expense) {
        expenses.add(expense)
        var sum = 0.0
        for (exp in expenses) {
            sum += exp.price
        }
        total.text = sum.toString() + "$"
    }

    fun deletePressed(category:Category,position:Int){
        val dialogBuilder = AlertDialog.Builder(this)
//        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
//        dialogBuilder.setView(dialogView)
//
//        val categoryname = dialogView.findViewById<EditText>(R.id.category_name)

        dialogBuilder.setTitle("Are you sure you want to delete "+ category.name+" category?")
        dialogBuilder.setMessage("All the expenses for this category will be deleted.")
        dialogBuilder.setPositiveButton("Delete", DialogInterface.OnClickListener { dialog, whichButton ->
            categories.remove(category)
            categoryRepository.delete(category)
            adapter.categories = categories
            adapter.notifyItemRemoved(position)
        })
        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
            adapter.notifyDataSetChanged()
        })
        val b = dialogBuilder.create()
        b.show()
    }


    fun addCategoryPressed() {

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        dialogBuilder.setView(dialogView)

        val categoryname = dialogView.findViewById<EditText>(R.id.category_name)

//        dialogBuilder.setTitle("Send FeedBack")
//        dialogBuilder.setMessage("please send me to your feedback.")
        dialogBuilder.setPositiveButton("Save", DialogInterface.OnClickListener { dialog, whichButton ->
            val nameStr = categoryname.text.toString()
            if (nameStr.isEmpty()){
                categoryname.setError("Name cannot be empty")
            }
            else {
                val category = Category()
                category.isExpandable = false
                category.name = nameStr
                categoryRepository.insert(category)

                getData()
                adapter.categories = categories
                adapter.notifyDataSetChanged()
            }

        })

        dialogBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, whichButton ->
            //pass
        })

        val b = dialogBuilder.create()
        b.show()
    }

    fun filterPressed(){
        if (sortView.visibility == View.GONE){
            sortView.visibility = View.VISIBLE
        }
        else {
            sortView.visibility = View.GONE
        }
    }

    fun firstMonthPressed(){
        CategoryAdapter.date = Date()
        getData()
        sortView.visibility = View.GONE
        firstCheck.visibility = View.VISIBLE
        secondCheck.visibility = View.INVISIBLE
        thirdCheck.visibility = View.INVISIBLE
    }

    fun secondMonthPressed(){
        var fM = Date()
        val c = Calendar.getInstance()
        c.time = fM
        c.add(Calendar.MONTH,-1)
        val d = c.time
        CategoryAdapter.date = c.time
        getData()
        sortView.visibility = View.GONE

        firstCheck.visibility = View.INVISIBLE
        secondCheck.visibility = View.VISIBLE
        thirdCheck.visibility = View.INVISIBLE
    }

    fun thirdMonthPressed(){
        var fM = Date()
        val c = Calendar.getInstance()
        c.time = fM
        c.add(Calendar.MONTH,-2)
        CategoryAdapter.date = c.time
        getData()
        sortView.visibility = View.GONE
        firstCheck.visibility = View.INVISIBLE
        secondCheck.visibility = View.INVISIBLE
        thirdCheck.visibility = View.VISIBLE
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        getData()
    }


}
