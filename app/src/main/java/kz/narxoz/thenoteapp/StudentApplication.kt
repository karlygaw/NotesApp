package kz.narxoz.thenoteapp

import android.app.Application
import kz.narxoz.thenoteapp.database.StudentDatabase

class StudentApplication:Application() {

    val database :StudentDatabase by lazy {
        StudentDatabase.getDataBase(this)
    }

}