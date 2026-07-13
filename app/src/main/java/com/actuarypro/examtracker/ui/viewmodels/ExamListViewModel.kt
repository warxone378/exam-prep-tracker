package com.actuarypro.examtracker.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actuarypro.examtracker.data.database.entities.ExamEntity
import com.actuarypro.examtracker.data.repositories.ExamRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExamListViewModel @Inject constructor(
    private val repository: ExamRepository
) : ViewModel() {
    private val _exams = MutableStateFlow<List<ExamEntity>>(emptyList())
    val exams: StateFlow<List<ExamEntity>> = _exams.asStateFlow()

    private val _organizations = MutableStateFlow<List<String>>(emptyList())
    val organizations: StateFlow<List<String>> = _organizations.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedOrganization = MutableStateFlow<String?>(null)
    val selectedOrganization: StateFlow<String?> = _selectedOrganization.asStateFlow()

    init {
        loadExams()
        loadOrganizations()
    }

    private fun loadExams() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getAllExams().collect { exams ->
                _exams.value = exams
                _isLoading.value = false
            }
        }
    }

    private fun loadOrganizations() {
        viewModelScope.launch {
            repository.getAllOrganizations().collect { orgs ->
                _organizations.value = orgs
            }
        }
    }

    fun filterByOrganization(orgCode: String?) {
        _selectedOrganization.value = orgCode
        viewModelScope.launch {
            _isLoading.value = true
            if (orgCode != null) {
                repository.getExamsByOrganization(orgCode).collect { exams ->
                    _exams.value = exams
                    _isLoading.value = false
                }
            } else {
                repository.getAllExams().collect { exams ->
                    _exams.value = exams
                    _isLoading.value = false
                }
            }
        }
    }
}
