package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SafetyStage
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun Stage3Screen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.safetySession.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val settings by viewModel.emergencySettings.collectAsState()
    val logs by viewModel.eventLogs.collectAsState()
    val simulatedCallActive by viewModel.simulatedCallActive.collectAsState()

    val isEmergencyActive = session.stage == SafetyStage.STAGE_3_EMERGENCY
    var showDeactivateConfirmDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    // Radar pulse animation for the large emergency indicator
    val infiniteTransition = rememberInfiniteTransition(label = "emergencyRadar")
    val radarScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarScale"
    )
    val radarAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAlpha"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNavy)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 32.dp)
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier.testTag("stage_3_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                StageTag(text = "STAGE 3", containerColor = Stage3RoseContainer, contentColor = Stage3Rose)
                Spacer(modifier = Modifier.width(8.dp))
                DemoModeBadge()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CORE PRINCIPLE BADGE
        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Core Principle: As danger level increases, user interaction requirements drop to zero.",
                color = TextSlate,
                fontSize = 11.sp,
                lineHeight = 15.sp,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // VERY LARGE RED EMERGENCY INDICATOR (Prompt requirement)
        Surface(
            color = if (isEmergencyActive) Stage3RoseContainer.copy(alpha = 0.5f) else CardNavy,
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(
                2.dp,
                if (isEmergencyActive) Stage3Rose else BorderNavy
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("large_emergency_indicator_card")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(150.dp)
                ) {
                    if (isEmergencyActive) {
                        // Expanding radar wave
                        Box(
                            modifier = Modifier
                                .size((120 * radarScale).dp)
                                .clip(CircleShape)
                                .background(Stage3Rose.copy(alpha = radarAlpha))
                        )
                    }

                    // Main central emergency button
                    Surface(
                        color = Stage3Rose,
                        shape = CircleShape,
                        shadowElevation = 12.dp,
                        modifier = Modifier
                            .size(110.dp)
                            .clickable {
                                if (!isEmergencyActive) {
                                    viewModel.triggerStage3Emergency("Emergency Center Button")
                                }
                            }
                            .testTag("emergency_sos_pulsing_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "SOS",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = if (isEmergencyActive) "Emergency Mode Activated" else "Emergency Standby",
                    color = if (isEmergencyActive) Stage3Rose else TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = if (isEmergencyActive)
                        "All safety protocols engaged • Simulated continuous broadcast"
                    else
                        "Press below to test competition trigger simulation",
                    color = TextSlate,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(18.dp))

                // PROMINENT "TEST EMERGENCY TRIGGER" (Prompt requirement)
                Button(
                    onClick = {
                        viewModel.triggerStage3Emergency("Test Emergency Trigger Control")
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isEmergencyActive) SurfaceElevated else Stage3Rose,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("prominent_test_emergency_trigger")
                ) {
                    Icon(imageVector = Icons.Default.Sensors, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isEmergencyActive) "RE-FIRE DEMO TRIGGER" else "TEST EMERGENCY TRIGGER",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp
                    )
                }

                if (isEmergencyActive) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { showDeactivateConfirmDialog = true },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Stage1Emerald),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Stage1Emerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("deactivate_emergency_button")
                    ) {
                        Icon(imageVector = Icons.Default.Shield, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("DEACTIVATE EMERGENCY / I'M SAFE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // EMERGENCY MESSAGE CARD (Prompt requirement: "Emergency message")
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "EMERGENCY BROADCAST MESSAGE",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = CanvasNavy,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Stage3Rose.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "CRITICAL EMERGENCY ALERT (STAGE 3)",
                            color = Stage3Rose,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Automated distress trigger initiated. User cannot interact with device.",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Live GPS Coordinates: Lat 37.7749, Long -122.4194 (North Campus Walkway)",
                            color = AccentBlue,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Dispatched via: ShieldUp High-Priority Mesh Relay (Simulated)",
                            color = TextSlate,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // PRIMARY EMERGENCY CONTACT & SIMULATE CALL (Prompt requirement: "Simulate emergency contact calling")
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "PRIMARY EMERGENCY CONTACT & ESCORT",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                contacts.find { it.isPrimaryEmergency }?.let { primary ->
                    Surface(
                        color = SurfaceElevated,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Stage3RoseContainer)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhoneInTalk,
                                        contentDescription = null,
                                        tint = Stage3Rose,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "${primary.name} (${primary.relationship})",
                                        color = TextWhite,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = primary.phone,
                                        color = TextSlate,
                                        fontSize = 12.sp
                                    )
                                }
                            }

                            Button(
                                onClick = { viewModel.simulateCall(primary.name) },
                                colors = ButtonDefaults.buttonColors(containerColor = Stage3Rose),
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                                modifier = Modifier.testTag("simulate_call_primary_button")
                            ) {
                                Text("Call Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Campus Police quick simulated call
                Surface(
                    color = SurfaceElevated,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(Stage1EmeraldContainer)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalPolice,
                                    contentDescription = null,
                                    tint = Stage1Emerald,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Campus Security Police",
                                    color = TextWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = settings.campusPolice,
                                    color = TextSlate,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Button(
                            onClick = { viewModel.simulateCall("Campus Security Police Dispatch") },
                            colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("simulate_call_police_button")
                        ) {
                            Text("Call Demo", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // TRUSTED CONTACTS LIST (Prompt requirement: "Trusted contacts")
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "TRUSTED CIRCLE NOTIFICATION STATUS (SIMULATED)",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                contacts.forEach { contact ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(SurfaceElevated)
                            ) {
                                Text(
                                    text = contact.avatarLetter,
                                    color = TextWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = contact.name,
                                    color = TextWhite,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = contact.relationship,
                                    color = TextSlateMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(if (isEmergencyActive) Stage3Rose else Stage1Emerald)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isEmergencyActive) "SOS PUSHED" else "STANDBY",
                                color = if (isEmergencyActive) Stage3Rose else TextSlate,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // LOCATION BROADCAST (Prompt requirement: "Location")
        LocationCard(
            address = session.locationAddress,
            lat = session.latitude,
            lng = session.longitude
        )

        Spacer(modifier = Modifier.height(18.dp))

        // EMERGENCY EVENT TIMELINE (Prompt requirement: "Show the full emergency event timeline")
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = AccentBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "EMERGENCY EVENT TIMELINE (DEMO AUDIT)",
                        color = TextSlate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                logs.take(5).forEach { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = log.timeStr,
                            color = TextSlateMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.width(70.dp)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = log.title,
                                color = if (log.stage == SafetyStage.STAGE_3_EMERGENCY) Stage3Rose else TextWhite,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = log.detail,
                                color = TextSlate,
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        // SIMULATED CALL DIALOG
        simulatedCallActive?.let { recipient ->
            SimulatedCallDialog(
                recipientName = recipient,
                onDismiss = { viewModel.dismissCall() }
            )
        }

        // DEACTIVATE CONFIRM DIALOG
        if (showDeactivateConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeactivateConfirmDialog = false },
                containerColor = CardNavy,
                title = {
                    Text("Deactivate Emergency Mode", color = TextWhite, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(
                        "Are you sure you are safe? This will disengage SOS broadcast and notify contacts that the situation is resolved.",
                        color = TextSlate
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deactivateEmergency()
                            showDeactivateConfirmDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald)
                    ) {
                        Text("Confirm Safe", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeactivateConfirmDialog = false }) {
                        Text("Keep SOS Active", color = Stage3Rose)
                    }
                }
            )
        }
    }
}
