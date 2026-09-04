package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ShieldUpRepository
import com.example.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppScreen {
    HOME,
    STAGE_1,
    STAGE_2,
    STAGE_3,
    CONTACTS,
    JOURNEY,
    SETTINGS,
    RESPONDERS
}

class ShieldUpViewModel(
    val repository: ShieldUpRepository = ShieldUpRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding: StateFlow<Boolean> = _showOnboarding.asStateFlow()

    // Contact Form Dialog State
    private val _editingContact = MutableStateFlow<Contact?>(null)
    val editingContact: StateFlow<Contact?> = _editingContact.asStateFlow()

    private val _showAddContactDialog = MutableStateFlow(false)
    val showAddContactDialog: StateFlow<Boolean> = _showAddContactDialog.asStateFlow()

    // Fast info snackbar or message
    private val _infoMessage = MutableStateFlow<String?>(null)
    val infoMessage: StateFlow<String?> = _infoMessage.asStateFlow()

    val contacts = repository.contacts
    val safetySession = repository.safetySession
    val safetyJourney = repository.safetyJourney
    val emergencySettings = repository.settings
    val eventLogs = repository.eventLogs
    val responderApp = repository.responderApp
    val simulatedCallActive = repository.simulatedCallActive

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun openOnboarding() {
        _showOnboarding.value = true
    }

    fun closeOnboarding() {
        _showOnboarding.value = false
    }

    fun showAddContact() {
        _editingContact.value = null
        _showAddContactDialog.value = true
    }

    fun showEditContact(contact: Contact) {
        _editingContact.value = contact
        _showAddContactDialog.value = true
    }

    fun dismissContactDialog() {
        _showAddContactDialog.value = false
        _editingContact.value = null
    }

    fun showNotice(msg: String) {
        _infoMessage.value = msg
    }

    fun clearNotice() {
        _infoMessage.value = null
    }

    // Helper functions delegating to repository
    fun activateStage1(durationMinutes: Int = 15) {
        repository.activateStage1(durationMinutes)
    }

    fun endStage1Safe() {
        repository.endStage1Safe()
    }

    fun selectSituation(situation: SituationType) {
        repository.selectSituation(situation)
    }

    fun startStage2Countdown(situation: SituationType, note: String) {
        repository.startStage2EscalationCountdown(situation, note)
    }

    fun fastHelpTrigger() {
        repository.fastHelpTrigger()
    }

    fun cancelStage2Countdown() {
        repository.cancelStage2Countdown()
    }

    fun markStage2Safe() {
        repository.markStage2Safe()
    }

    fun triggerStage3Emergency(source: String = "Demo Trigger") {
        repository.triggerStage3Emergency(source)
    }

    fun deactivateEmergency() {
        repository.deactivateStage3Emergency()
    }

    fun simulateCall(name: String) {
        repository.triggerSimulatedEmergencyCall(name)
    }

    fun dismissCall() {
        repository.dismissSimulatedCall()
    }

    fun startJourney(destination: String, minutes: Int) {
        repository.startJourney(destination, minutes)
    }

    fun extendJourney() {
        repository.extendJourneyTime(5)
    }

    fun completeJourney() {
        repository.completeJourney()
    }

    fun simulateJourneyExpiry() {
        repository.simulateJourneyExpiry()
    }
}
