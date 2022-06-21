package id.coolva.metapol.ui.form.escortreq

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.coolva.metapol.core.domain.model.EscortReq
import id.coolva.metapol.core.domain.usecase.MetapolUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EscortReqViewModel @Inject constructor(private val metapolUseCase: MetapolUseCase) :
    ViewModel() {

    fun getEscortRequestList(): LiveData<List<EscortReq>> {
        return metapolUseCase.getEscortRequestList().asLiveData()
    }

    fun insertEscortReq(escortReq: EscortReq) {
        viewModelScope.launch {
            metapolUseCase.insertEscortReq(escortReq)
        }
    }

    fun deleteAllEscortReq() {
        viewModelScope.launch {
            metapolUseCase.deleteAllEscortReq()
        }
    }

}