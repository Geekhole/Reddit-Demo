package uk.geekhole.hotreddit.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import io.reactivex.Completable

// Generic DAO class I use everywhere since almost all DAO require insert delete and update methods. Probably not so necessary here with the app's simplicity.
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: T): Completable

    @Delete
    fun delete(vararg entity: T): Completable

    @Update
    fun update(vararg entity: T): Completable
}