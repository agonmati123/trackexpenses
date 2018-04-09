package com.agonmati.tackexpenses.trackexpenses.repositories

import com.agonmati.tackexpenses.trackexpenses.helpers.DateHelper
import com.agonmati.tackexpenses.trackexpenses.entities.Category
import com.agonmati.tackexpenses.trackexpenses.entities.Expense
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMCondition
import java.util.Date;

/**
 * Created by agonmati on 2/11/18.
 */
class ExpenseRepository {
    val appInstance = com.agonmati.tackexpenses.trackexpenses.application.AppController.instance
    var pultusORM:PultusORM ?= null

    fun insert(expense: Expense){
        openConnection()
        pultusORM?.save(expense)
        closeConnection()
    }

    fun deleteExpensesForCategory(category: Category){
        openConnection()
        val condition: PultusORMCondition = PultusORMCondition.Builder()
                .eq("category", category.id)
                .build()
        pultusORM?.delete(Expense(),condition)

        closeConnection()

    }

    fun deleteExpenses(expense: Expense){
        openConnection()
        val condition: PultusORMCondition = PultusORMCondition.Builder()
                .eq("id", expense.id)
                .build()
        pultusORM?.delete(Expense(),condition)

        closeConnection()

    }


    fun getAllForMonth(date: Date):ArrayList<Expense>{
        openConnection()
        var expenses = ArrayList<Expense>()
        var expensesORM = pultusORM?.find(Expense())

        for (it in expensesORM.orEmpty()) {
            val expense = it as Expense

            if (DateHelper.isSameMonth(expense.date,date.toString())){
                expenses.add(expense)
            }
        }
        closeConnection()
        return expenses
    }

    fun getAllForMonthAndCategory(category:Category,date:Date):ArrayList<Expense>{
        openConnection()
        var expenses = ArrayList<Expense>()
        val condition: PultusORMCondition = PultusORMCondition.Builder()
                .eq("category", category.id)
                .build()
        val expensesORM = pultusORM?.find(Expense(), condition)
        for (it in expensesORM.orEmpty()) {
            val expense = it as Expense
        if (DateHelper.isSameMonth(expense.date,date.toString())){
                expenses.add(expense)
            }
        }
        closeConnection()
        return expenses
    }

    fun openConnection(){
        var appPath = appInstance.getApplicationContext().getFilesDir().getAbsolutePath()  // Output : /data/data/application_package_name/files/
        pultusORM = PultusORM("expenses.db", appPath)

    }

    fun closeConnection(){
        pultusORM?.close()
    }

}