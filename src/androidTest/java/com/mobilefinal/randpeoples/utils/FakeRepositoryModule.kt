package com.mobilefinal.randpeoples.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mobilefinal.randpeoples.di.RepositoryModule
import com.mobilefinal.randpeoples.model.User
import com.mobilefinal.randpeoples.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
class FakeRepositoryModule {

    @Provides
    @Singleton
    fun userRepository(): UserRepository =
        object : UserRepository {

            private val users = MutableLiveData<List<User>>(listOf());

            override suspend fun getNewUser(): User {
                val userList = users.value!!
                val newUser = User(
                    "Name ${userList.size}",
                    "LastName ${userList.size}",
                    "City",
                    "Image",
                )
                users.postValue(users.value?.toMutableList()?.apply { add(newUser) })
                return newUser
            }

            override suspend fun deleteUser(toDelete: User) {
                users.postValue(users.value?.toMutableList()?.apply { remove(toDelete) })
            }

            override fun getAllUsers(): LiveData<List<User>> = users
        }
}