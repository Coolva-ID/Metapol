package id.coolva.metapol.ui.form.skckreg

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.coolva.metapol.core.domain.model.SKCKReg
import id.coolva.metapol.core.domain.usecase.MetapolUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SkckRegViewModel @Inject constructor(private val metapolUseCase: MetapolUseCase): ViewModel() {

    fun getSKCKRegList(): LiveData<List<SKCKReg>> {
        return metapolUseCase.getSKCKRegList().asLiveData()
    }

    fun insertSKCKReg(skckReg: SKCKReg) {
        viewModelScope.launch {
            metapolUseCase.insertSKCKReg(skckReg)
        }
    }

    fun deleteAllSKCKReg(){
        viewModelScope.launch {
            metapolUseCase.deleteAllSKCKReg()
        }
    }
}