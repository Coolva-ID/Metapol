package id.coolva.metapol.ui.form.simreg

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.coolva.metapol.core.domain.model.SIMRegsitration
import id.coolva.metapol.core.domain.usecase.MetapolUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SimRegViewModel @Inject constructor(private val metapolUseCase: MetapolUseCase) :
    ViewModel() {

    fun getSIMRegistration(): LiveData<List<SIMRegsitration>> {
        return metapolUseCase.getSIMRegistration().asLiveData()
    }

    fun insertSIMRegistration(simRegsitration: SIMRegsitration){
        viewModelScope.launch {
            metapolUseCase.insertSIMRegistration(simRegsitration)
        }
    }
}