package id.coolva.metapol.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.coolva.metapol.core.domain.model.User
import id.coolva.metapol.core.domain.usecase.MetapolUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val metapolUseCase: MetapolUseCase): ViewModel() {

    fun getUserList(): LiveData<List<User>> {
        return metapolUseCase.getUserList().asLiveData()
    }

    fun insertUser(user: User) {
        viewModelScope.launch { metapolUseCase.insertUser(user) }
    }

    fun updateUser(user: User) {
        viewModelScope.launch { metapolUseCase.updateUser(user) }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch { metapolUseCase.deleteUser(user) }
    }

    fun deleteAllUser() {
        viewModelScope.launch { metapolUseCase.deleteAllUser() }
    }
}