package com.agonmati.tackexpenses.trackexpenses.repositories

import android.icu.util.ULocale
import com.agonmati.tackexpenses.trackexpenses.application.AppController
import com.agonmati.tackexpenses.trackexpenses.entities.Category
import ninja.sakib.pultusorm.core.PultusORM
import ninja.sakib.pultusorm.core.PultusORMCondition

/**
 * Created by agonmati on 2/11/18.
 */
class CategoryRepository {
    lateinit var appInstance:AppController
    var pultusORM:PultusORM ?= null


    fun insert(category: Category){
        openConnection()
        pultusORM?.save(category)
        closeConnection()
    }

    fun delete(category:Category){
        openConnection()
        val condition: PultusORMCondition = PultusORMCondition.Builder()
                .eq("id", category.id)
                .build()

        pultusORM?.delete(Category(), condition)
        val expenseRepository = ExpenseRepository()
        expenseRepository.deleteExpensesForCategory(category)
        closeConnection()
    }

    fun get(id:Int):Category{
        openConnection()
        val condition: PultusORMCondition = PultusORMCondition.Builder()
                .eq("id", id)
                .build()

        val categoriesORM = pultusORM?.find(Category(),condition)
        val category = categoriesORM?.get(0) as Category

        closeConnection()
        return category
    }


    fun getAll():ArrayList<Category>{
        openConnection()
        var categories = ArrayList<Category>()
        val categoriesORM = pultusORM?.find(Category())
        for (it in categoriesORM.orEmpty()) {
            val category = it as Category
            categories.add(category)
        }

        closeConnection()
        return categories
    }

    fun openConnection(){
        appInstance = com.agonmati.tackexpenses.trackexpenses.application.AppController.instance
        var appPath = appInstance.getApplicationContext().getFilesDir().getAbsolutePath()  // Output : /data/data/application_package_name/files/
        pultusORM = PultusORM("expenses.db", appPath)
    }

    fun closeConnection(){
        pultusORM?.close()
    }
}