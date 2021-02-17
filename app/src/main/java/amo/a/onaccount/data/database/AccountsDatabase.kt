package amo.a.onaccount.data.database

import amo.a.onaccount.data.models.Account
import amo.a.onaccount.data.models.Operation
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ Account::class,Operation::class], version=1,exportSchema = false)
@TypeConverters(AccountsTypeConverters::class)
abstract class AccountsDatabase : RoomDatabase() {
    abstract fun accountsDao(): AccountsDao
}

//val migration_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE Task ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
//        )
//    }
//}
//