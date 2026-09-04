package com.example.model

enum class SafetyStage {
    IDLE,
    STAGE_1_SHIELD,
    STAGE_2_DANGER,
    STAGE_3_EMERGENCY
}

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val relationship: String,
    val isPrimaryEmergency: Boolean = false,
    val isTrusted: Boolean = true,
    val avatarLetter: String = name.take(1).uppercase()
)

data class BreadcrumbPoint(
    val id: String,
    val timeStr: String,
    val label: String,
    val lat: Double,
    val lng: Double,
    val isCurrent: Boolean = false
)

enum class SituationType(
    val title: String,
    val iconEmoji: String,
    val defaultMessage: String
) {
    FOLLOWED(
        title = "Someone is following me",
        iconEmoji = "🚶",
        defaultMessage = "I am being followed by an unknown individual. Please monitor my location."
    ),
    HARASSED(
        title = "I am being harassed",
        iconEmoji = "⚠️",
        defaultMessage = "I am being harassed and feel unsafe in this area."
    ),
    THREATENED(
        title = "I feel threatened",
        iconEmoji = "🚨",
        defaultMessage = "I feel actively threatened. Need check-in or urgent assistance."
    ),
    LOST(
        title = "I am lost",
        iconEmoji = "🧭",
        defaultMessage = "I am disoriented and lost in an unfamiliar dark area."
    ),
    MEDICAL(
        title = "Medical emergency",
        iconEmoji = "🏥",
        defaultMessage = "Experiencing sudden medical distress. Please call campus EMS."
    ),
    OTHER(
        title = "Other",
        iconEmoji = "❓",
        defaultMessage = "Unsafe situation occurring. Please review my live coordinates."
    )
}

data class SafetySession(
    val stage: SafetyStage = SafetyStage.IDLE,
    val isRunning: Boolean = false,
    val remainingSeconds: Int = 15 * 60, // 15 minutes default
    val totalSeconds: Int = 15 * 60,
    val selectedContactIds: Set<String> = emptySet(),
    val situation: SituationType? = null,
    val customNote: String = "",
    val locationAddress: String = "North Campus Walkway, near Science Hall B",
    val latitude: Double = 37.7749,
    val longitude: Double = -122.4194,
    val breadcrumbs: List<BreadcrumbPoint> = emptyList(),
    val isEscalationCountdownActive: Boolean = false,
    val escalationCountdownSeconds: Int = 5,
    val isAlertDispatched: Boolean = false,
    val alertDispatchedTimeStr: String = ""
)

data class SafetyJourney(
    val id: String = "journey_1",
    val destination: String = "Residence Hall Tower C",
    val initialMinutes: Int = 12,
    val remainingSeconds: Int = 12 * 60,
    val isActive: Boolean = false,
    val isExpired: Boolean = false,
    val hasEscalatedWarning: Boolean = false,
    val currentCheckpoint: String = "University Library South Exit"
)

data class EmergencySettings(
    val campusPolice: String = "(555) 019-9110 (Campus Police)",
    val emergencyDispatch: String = "911 (Emergency Dispatch)",
    val campusEscort: String = "(555) 012-9911 (24/7 Safety Escort)",
    val ambulance: String = "(555) 019-9112 (EMS/Ambulance)",
    val womenHelpline: String = "1-800-799-7233 (Helpline)",
    val communityRespondersEnabled: Boolean = true,
    val pinProtectionEnabled: Boolean = false,
    val pinCode: String = "1234"
)

data class SafetyEventLog(
    val id: String,
    val timeStr: String,
    val title: String,
    val detail: String,
    val stage: SafetyStage,
    val isSimulated: Boolean = true
)

data class VerifiedResponderApplication(
    val step: Int = 1, // 1: Register, 2: ID, 3: Background, 4: KYC, 5: Approved
    val fullName: String = "Alex Chen",
    val role: String = "Campus Resident Advisor & Volunteer",
    val studentOrGovId: String = "STD-884920",
    val idUploaded: Boolean = true,
    val backgroundCheckStatus: String = "Cleared (Simulated)",
    val kycStatus: String = "Verified (Simulated)",
    val isApproved: Boolean = false
)
