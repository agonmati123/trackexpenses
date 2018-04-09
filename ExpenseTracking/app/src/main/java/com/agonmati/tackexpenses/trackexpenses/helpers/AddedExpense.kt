package com.agonmati.tackexpenses.trackexpenses.helpers

import com.agonmati.tackexpenses.trackexpenses.entities.Expense

/**
 * Created by agonmati on 2/12/18.
 */
interface AddedExpense {

    fun expenseAdded(expense: Expense)
}