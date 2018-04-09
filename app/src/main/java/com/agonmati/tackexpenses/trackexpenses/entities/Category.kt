package com.agonmati.tackexpenses.trackexpenses.entities

import ninja.sakib.pultusorm.annotations.AutoIncrement
import ninja.sakib.pultusorm.annotations.Ignore
import ninja.sakib.pultusorm.annotations.PrimaryKey

/**
 * Created by agonmati on 2/10/18.
 */
class Category() {
    @PrimaryKey
    @AutoIncrement
    var id:Int = 0
    var name:String? = null
    @Ignore
    var isExpandable = false

}

