package com.example.data

import com.example.model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.*

class ShieldUpRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
) {
    private val timeFormatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    private val secondFormatter = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    // Initial contacts list
    private val _contacts = MutableStateFlow<List<Contact>>(
        listOf(
            Contact(
                id = "c1",
                name = "Sarah Jenkins",
                phone = "+1 (555) 234-5678",
                relationship = "Sister",
                isPrimaryEmergency = true,
                isTrusted = true
            ),
            Contact(
                id = "c2",
                name = "Marcus Vance",
                phone = "+1 (555) 876-5432",
                relationship = "Campus Roommate",
                isPrimaryEmergency = false,
                isTrusted = true
            ),
            Contact(
                id = "c3",
                name = "Dr. Elena Rostova",
                phone = "+1 (555) 432-1098",
                relationship = "Campus Advisor",
                isPrimaryEmergency = false,
                isTrusted = true
            ),
            Contact(
                id = "c4",
                name = "Campus Safety Escort Dispatch",
                phone = "+1 (555) 911-0022",
                relationship = "University Security",
                isPrimaryEmergency = false,
                isTrusted = true
            )
        )
    )
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    // Safety Session State
    private val _safetySession = MutableStateFlow(SafetySession())
    val safetySession: StateFlow<SafetySession> = _safetySession.asStateFlow()

    // Safety Journey State
    private val _safetyJourney = MutableStateFlow(SafetyJourney())
    val safetyJourney: StateFlow<SafetyJourney> = _safetyJourney.asStateFlow()

    // Emergency Settings
    private val _settings = MutableStateFlow(EmergencySettings())
    val settings: StateFlow<EmergencySettings> = _settings.asStateFlow()

    // Event Logs
    private val _eventLogs = MutableStateFlow<List<SafetyEventLog>>(
        listOf(
            SafetyEventLog(
                id = "log_0",
                timeStr = "Yesterday 11:20 PM",
                title = "Safety Journey Completed",
                detail = "Arrived safely at Residence Hall Tower C. Session closed.",
                stage = SafetyStage.IDLE,
                isSimulated = true
            ),
            SafetyEventLog(
                id = "log_1",
                timeStr = "3 days ago 9:45 PM",
                title = "Shield Mode (Stage 1) Activated",
                detail = "Discreet 15-min walk timer initiated near North Quad. 4 contacts notified.",
                stage = SafetyStage.STAGE_1_SHIELD,
                isSimulated = true
            )
        )
    )
    val eventLogs: StateFlow<List<SafetyEventLog>> = _eventLogs.asStateFlow()

    // Verified Responder Application
    private val _responderApp = MutableStateFlow(VerifiedResponderApplication())
    val responderApp: StateFlow<VerifiedResponderApplication> = _responderApp.asStateFlow()

    // Active Simulated Call State (for Stage 3 demo)
    private val _simulatedCallActive = MutableStateFlow<String?>(null)
    val simulatedCallActive: StateFlow<String?> = _simulatedCallActive.asStateFlow()

    // Timers
    private var sessionTimerJob: Job? = null
    private var escalationTimerJob: Job? = null
    private var journeyTimerJob: Job? = null

    init {
        // Initialize default selected contacts for safety session
        _safetySession.value = _safetySession.value.copy(
            selectedContactIds = _contacts.value.map { it.id }.toSet(),
            breadcrumbs = defaultBreadcrumbs()
        )
    }

    private fun currentTime(): String = timeFormatter.format(Date())
    private fun currentSecondTime(): String = secondFormatter.format(Date())

    private fun defaultBreadcrumbs(): List<BreadcrumbPoint> = listOf(
        BreadcrumbPoint("b1", "8:38 PM", "Library North Lawn", 37.7745, -122.4190, false),
        BreadcrumbPoint("b2", "8:41 PM", "Chemistry Annex Plaza", 37.7747, -122.4192, false),
        BreadcrumbPoint("b3", "8:44 PM", "North Campus Walkway (Current)", 37.7749, -122.4194, true)
    )

    // STAGE 1 ACTIONS
    fun activateStage1(durationMinutes: Int = 15) {
        sessionTimerJob?.cancel()
        val totalSecs = durationMinutes * 60
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.STAGE_1_SHIELD,
            isRunning = true,
            remainingSeconds = totalSecs,
            totalSeconds = totalSecs,
            breadcrumbs = defaultBreadcrumbs(),
            isAlertDispatched = false
        )

        addLog(
            title = "Stage 1 Shield Activated",
            detail = "Discreet session started for $durationMinutes min. Location shared with ${_safetySession.value.selectedContactIds.size} contacts (Simulated).",
            stage = SafetyStage.STAGE_1_SHIELD
        )

        sessionTimerJob = scope.launch {
            while (_safetySession.value.isRunning && _safetySession.value.remainingSeconds > 0) {
                delay(1000)
                val current = _safetySession.value
                if (current.remainingSeconds > 1) {
                    _safetySession.value = current.copy(remainingSeconds = current.remainingSeconds - 1)
                } else {
                    _safetySession.value = current.copy(remainingSeconds = 0, isRunning = false)
                    addLog(
                        title = "Stage 1 Shield Expired",
                        detail = "Timer reached 0:00. Safety check prompted.",
                        stage = SafetyStage.STAGE_1_SHIELD
                    )
                    break
                }
            }
        }
    }

    fun endStage1Safe() {
        sessionTimerJob?.cancel()
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.IDLE,
            isRunning = false
        )
        addLog(
            title = "Marked Safe - Shield Deactivated",
            detail = "User marked safe. Trusted contacts informed that user is secure.",
            stage = SafetyStage.IDLE
        )
    }

    fun toggleContactSelection(contactId: String) {
        val currentSet = _safetySession.value.selectedContactIds.toMutableSet()
        if (currentSet.contains(contactId)) {
            if (currentSet.size > 1) currentSet.remove(contactId)
        } else {
            currentSet.add(contactId)
        }
        _safetySession.value = _safetySession.value.copy(selectedContactIds = currentSet)
    }

    // STAGE 2 ACTIONS
    fun selectSituation(situation: SituationType) {
        _safetySession.value = _safetySession.value.copy(situation = situation)
    }

    fun setCustomNote(note: String) {
        _safetySession.value = _safetySession.value.copy(customNote = note)
    }

    fun startStage2EscalationCountdown(situation: SituationType, note: String) {
        escalationTimerJob?.cancel()
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.STAGE_2_DANGER,
            situation = situation,
            customNote = note,
            isEscalationCountdownActive = true,
            escalationCountdownSeconds = 5,
            isAlertDispatched = false
        )

        escalationTimerJob = scope.launch {
            for (i in 5 downTo 1) {
                val cur = _safetySession.value
                if (!cur.isEscalationCountdownActive) break
                _safetySession.value = cur.copy(escalationCountdownSeconds = i)
                delay(1000)
            }
            if (_safetySession.value.isEscalationCountdownActive) {
                dispatchStage2Alert()
            }
        }
    }

    fun cancelStage2Countdown() {
        escalationTimerJob?.cancel()
        _safetySession.value = _safetySession.value.copy(
            isEscalationCountdownActive = false,
            escalationCountdownSeconds = 5
        )
        addLog(
            title = "Stage 2 Countdown Cancelled",
            detail = "User cancelled alert before simulated dispatch.",
            stage = SafetyStage.IDLE
        )
    }

    fun dispatchStage2Alert() {
        escalationTimerJob?.cancel()
        val sit = _safetySession.value.situation ?: SituationType.THREATENED
        val timeNow = currentTime()
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.STAGE_2_DANGER,
            isEscalationCountdownActive = false,
            isAlertDispatched = true,
            alertDispatchedTimeStr = timeNow
        )

        addLog(
            title = "SHIELDUP ALERT: ${sit.title}",
            detail = "Dispatched at $timeNow with live location to ${_safetySession.value.selectedContactIds.size} contacts (Simulated).",
            stage = SafetyStage.STAGE_2_DANGER
        )
    }

    fun fastHelpTrigger() {
        escalationTimerJob?.cancel()
        val timeNow = currentTime()
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.STAGE_2_DANGER,
            situation = SituationType.THREATENED,
            customNote = "FAST 1-TAP HELP TRIGGERED: Urgent assistance requested immediately.",
            isEscalationCountdownActive = false,
            isAlertDispatched = true,
            alertDispatchedTimeStr = timeNow
        )

        addLog(
            title = "FAST HELP Alert Dispatched",
            detail = "Immediate danger alert sent without delay. High-priority push sent to contacts (Simulated).",
            stage = SafetyStage.STAGE_2_DANGER
        )
    }

    fun markStage2Safe() {
        escalationTimerJob?.cancel()
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.IDLE,
            isRunning = false,
            isAlertDispatched = false,
            isEscalationCountdownActive = false
        )
        addLog(
            title = "User Confirmed Safe",
            detail = "Stage 2 Alert closed. 'I\'m safe' broadcast dispatched to contacts.",
            stage = SafetyStage.IDLE
        )
    }

    // STAGE 3 ACTIONS (EMERGENCY)
    fun triggerStage3Emergency(source: String = "Test Trigger Button") {
        escalationTimerJob?.cancel()
        sessionTimerJob?.cancel()
        val timeNow = currentSecondTime()
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.STAGE_3_EMERGENCY,
            isRunning = true,
            isAlertDispatched = true
        )

        addLog(
            title = "EMERGENCY ACTIVATED ($source)",
            detail = "$timeNow: SOS signal triggered. Campus security & emergency contacts alerted with live GPS ping (Simulated).",
            stage = SafetyStage.STAGE_3_EMERGENCY
        )
    }

    fun triggerSimulatedEmergencyCall(recipientName: String) {
        _simulatedCallActive.value = recipientName
        addLog(
            title = "Simulated Emergency Call",
            detail = "Calling $recipientName with automated hands-free safety voice relay.",
            stage = SafetyStage.STAGE_3_EMERGENCY
        )
    }

    fun dismissSimulatedCall() {
        _simulatedCallActive.value = null
    }

    fun deactivateStage3Emergency() {
        _safetySession.value = _safetySession.value.copy(
            stage = SafetyStage.IDLE,
            isRunning = false,
            isAlertDispatched = false
        )
        _simulatedCallActive.value = null
        addLog(
            title = "Emergency Deactivated",
            detail = "Stage 3 Emergency disengaged. Standby mode restored.",
            stage = SafetyStage.IDLE
        )
    }

    // CONTACTS MANAGEMENT
    fun addContact(name: String, phone: String, relationship: String, isPrimary: Boolean, isTrusted: Boolean) {
        val newId = "c_${System.currentTimeMillis()}"
        val updatedList = _contacts.value.toMutableList()
        if (isPrimary) {
            // Remove primary status from others
            for (i in updatedList.indices) {
                updatedList[i] = updatedList[i].copy(isPrimaryEmergency = false)
            }
        }
        val contact = Contact(
            id = newId,
            name = name,
            phone = phone,
            relationship = relationship,
            isPrimaryEmergency = isPrimary,
            isTrusted = isTrusted
        )
        updatedList.add(contact)
        _contacts.value = updatedList
        _safetySession.value = _safetySession.value.copy(
            selectedContactIds = _safetySession.value.selectedContactIds + newId
        )
    }

    fun updateContact(contact: Contact) {
        val updatedList = _contacts.value.map {
            if (it.id == contact.id) {
                contact
            } else if (contact.isPrimaryEmergency && it.isPrimaryEmergency) {
                it.copy(isPrimaryEmergency = false)
            } else {
                it
            }
        }
        _contacts.value = updatedList
    }

    fun deleteContact(contactId: String) {
        _contacts.value = _contacts.value.filterNot { it.id == contactId }
        _safetySession.value = _safetySession.value.copy(
            selectedContactIds = _safetySession.value.selectedContactIds - contactId
        )
    }

    fun setPrimaryContact(contactId: String) {
        _contacts.value = _contacts.value.map {
            it.copy(isPrimaryEmergency = (it.id == contactId))
        }
    }

    // SAFETY JOURNEY ACTIONS
    fun startJourney(destination: String, minutes: Int) {
        journeyTimerJob?.cancel()
        val totalSecs = minutes * 60
        _safetyJourney.value = SafetyJourney(
            destination = destination,
            initialMinutes = minutes,
            remainingSeconds = totalSecs,
            isActive = true,
            isExpired = false,
            hasEscalatedWarning = false,
            currentCheckpoint = "Departure point"
        )

        addLog(
            title = "Safety Journey Started",
            detail = "Heading to $destination ($minutes mins expected). Automated arrival monitor active.",
            stage = SafetyStage.IDLE
        )

        journeyTimerJob = scope.launch {
            while (_safetyJourney.value.isActive && _safetyJourney.value.remainingSeconds > 0) {
                delay(1000)
                val cur = _safetyJourney.value
                if (cur.remainingSeconds > 1) {
                    _safetyJourney.value = cur.copy(remainingSeconds = cur.remainingSeconds - 1)
                } else {
                    _safetyJourney.value = cur.copy(
                        remainingSeconds = 0,
                        isActive = true,
                        isExpired = true,
                        hasEscalatedWarning = true
                    )
                    addLog(
                        title = "Missed Safety Journey Check-in!",
                        detail = "Expected arrival time at $destination expired! Escalation warning issued.",
                        stage = SafetyStage.STAGE_2_DANGER
                    )
                    break
                }
            }
        }
    }

    fun extendJourneyTime(additionalMinutes: Int = 5) {
        val cur = _safetyJourney.value
        val newSecs = cur.remainingSeconds + (additionalMinutes * 60)
        _safetyJourney.value = cur.copy(
            remainingSeconds = newSecs,
            isExpired = false,
            hasEscalatedWarning = false
        )
        addLog(
            title = "Journey Extended +${additionalMinutes}m",
            detail = "New estimated arrival time updated for trusted contacts.",
            stage = SafetyStage.IDLE
        )
    }

    fun completeJourney() {
        journeyTimerJob?.cancel()
        val dest = _safetyJourney.value.destination
        _safetyJourney.value = _safetyJourney.value.copy(
            isActive = false,
            isExpired = false,
            hasEscalatedWarning = false
        )
        addLog(
            title = "Journey Arrived Safely",
            detail = "User reached $dest. Safety Journey completed.",
            stage = SafetyStage.IDLE
        )
    }

    fun simulateJourneyExpiry() {
        journeyTimerJob?.cancel()
        _safetyJourney.value = _safetyJourney.value.copy(
            isActive = true,
            remainingSeconds = 0,
            isExpired = true,
            hasEscalatedWarning = true
        )
        addLog(
            title = "DEMO: Simulated Check-in Expiry",
            detail = "Simulated missed arrival for demonstration. Emergency escalation preview active.",
            stage = SafetyStage.STAGE_2_DANGER
        )
    }

    // SETTINGS
    fun updateSettings(newSettings: EmergencySettings) {
        _settings.value = newSettings
    }

    // VERIFIED RESPONDER FLOW
    fun advanceResponderStep(step: Int) {
        val cur = _responderApp.value
        val next = (step).coerceIn(1, 5)
        _responderApp.value = cur.copy(
            step = next,
            isApproved = (next == 5)
        )
    }

    fun resetResponderDemo() {
        _responderApp.value = VerifiedResponderApplication(step = 1, isApproved = false)
    }

    private fun addLog(title: String, detail: String, stage: SafetyStage) {
        val log = SafetyEventLog(
            id = "log_${System.currentTimeMillis()}",
            timeStr = currentTime(),
            title = title,
            detail = detail,
            stage = stage,
            isSimulated = true
        )
        _eventLogs.value = listOf(log) + _eventLogs.value.take(25)
    }
}
