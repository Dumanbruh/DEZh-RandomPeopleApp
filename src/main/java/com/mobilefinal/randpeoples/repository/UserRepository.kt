package com.mobilefinal.randpeoples.repository

import androidx.lifecycle.LiveData
import com.mobilefinal.randpeoples.datasource.RestDataSource
import com.mobilefinal.randpeoples.model.User
import com.mobilefinal.randpeoples.model.UserDao
import kotlinx.coroutines.delay
import javax.inject.Inject

interface UserRepository {
    suspend fun getNewUser(): User
    suspend fun deleteUser(toDelete: User)
    fun getAllUsers(): LiveData<List<User>>

}

class UserRepositoryImp @Inject constructor(
    private val dataSource: RestDataSource,
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getNewUser(): User {
        delay(3000)
        val name = dataSource.getUserName().results[0].name!!
        val location = dataSource.getUserLocation().results[0].location!!
        val picture = dataSource.getUserPicture().results[0].picture!!
        val user = User(name.first, name.last, location.city, picture.thumbnail)
        userDao.insert(user)
        return user
    }

    override fun getAllUsers() = userDao.getAll()

    override suspend fun deleteUser(toDelete: User) = userDao.delete(toDelete)

}
