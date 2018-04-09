package com.agonmati.tackexpenses.trackexpenses.entities

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.PrimaryKey
import java.util.*

/**
 * Created by agonmati on 2/10/18.
 */
class Expense {
    @PrimaryKey
    @AutoIncrement
    var id:Int = 0
    var category:Int = 0
    var name:String? = null
    var price:Double = 0.0
    var date:String? = null

}