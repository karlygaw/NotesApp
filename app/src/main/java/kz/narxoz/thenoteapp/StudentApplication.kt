package kz.narxoz.thenoteapp

import android.app.Application
import kz.narxoz.thenoteapp.database.BookDatabase

class StudentApplication:Application() {

    val database :BookDatabase by lazy {
        BookDatabase.getDataBase(this)
    }

}