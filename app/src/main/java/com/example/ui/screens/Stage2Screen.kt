package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import com.example.model.SituationType
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.LocationCard
import com.example.ui.components.MovementTimeline
import com.example.ui.components.PulsingDot
import com.example.ui.components.StageTag
import com.example.ui.theme.*

@Composable
fun Stage2Screen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.safetySession.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    var selectedSituation by remember { mutableStateOf(session.situation ?: SituationType.FOLLOWED) }
    var customNoteText by remember { mutableStateOf(session.customNote) }
    var showSafeConfirmationDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNavy)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 28.dp)
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier.testTag("stage_2_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                StageTag(text = "STAGE 2", containerColor = Stage2AmberContainer, contentColor = Stage2Amber)
                Spacer(modifier = Modifier.width(8.dp))
                DemoModeBadge()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // TITLE & DESCRIPTION
        Text(
            text = "MAY BE IN DANGER",
            color = TextWhite,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "Immediate escalation for threats, stalking, or medical emergencies. Rapidly alerts your trusted circle with precise live tracking.",
            color = TextSlate,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // FAST 1-TAP HELP BUTTON (Prompt requirement: "Provide a fast HELP button that automatically creates a simple emergency message")
        Surface(
            color = Stage2Amber,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.fastHelpTrigger() }
                .testTag("fast_help_emergency_button")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.Black.copy(alpha = 0.2f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "INSTANT 1-TAP HELP",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Bypasses menus & dispatches instant alert",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // CANCELLATION COUNTDOWN ACTIVE BANNER (Prompt requirement: "short cancellation countdown before escalation")
        if (session.isEscalationCountdownActive) {
            Surface(
                color = Stage2AmberContainer,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Stage2Amber),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("stage_2_countdown_banner")
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(18.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PulsingDot(color = Stage2Amber, sizeDp = 10)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "DISPATCHING ALERT IN",
                            color = Stage2Amber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${session.escalationCountdownSeconds}s",
                        color = TextWhite,
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Press cancel if triggered by mistake",
                        color = TextSlate,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.cancelStage2Countdown() },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("cancel_countdown_button")
                    ) {
                        Text("CANCEL ALERT", fontWeight = FontWeight.Black)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // DISPATCHED STATUS BANNER
        if (session.isAlertDispatched) {
            Surface(
                color = CardNavy,
                shape = RoundedCornerShape(22.dp),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Stage2Amber),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("alert_dispatched_banner")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Stage2Amber,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ALERT TRANSMITTED (SIMULATED)",
                                color = Stage2Amber,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Text(
                            text = session.alertDispatchedTimeStr,
                            color = TextSlate,
                            fontSize = 11.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Simulated high-priority push notifications and live GPS telemetry sent to ${contacts.size} circle members.",
                        color = TextWhite,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Button(
                        onClick = { showSafeConfirmationDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("stage_2_im_safe_button")
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("I'M SAFE — CANCEL ALERT", fontWeight = FontWeight.Bold)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // LARGE SITUATION BUTTONS (6 Situations from prompt)
        Text(
            text = "SELECT SITUATION",
            color = TextSlateMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            val situations = listOf(
                SituationType.FOLLOWED,
                SituationType.HARASSED,
                SituationType.THREATENED,
                SituationType.LOST,
                SituationType.MEDICAL,
                SituationType.OTHER
            )

            situations.chunked(2).forEach { rowPair ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    rowPair.forEach { sit ->
                        val isSelected = selectedSituation == sit
                        Surface(
                            color = if (isSelected) Stage2AmberContainer else CardNavy,
                            shape = RoundedCornerShape(18.dp),
                            border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Stage2Amber) else androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    selectedSituation = sit
                                    viewModel.selectSituation(sit)
                                }
                                .testTag("situation_${sit.name.lowercase()}")
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = sit.iconEmoji,
                                    fontSize = 24.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = sit.title,
                                    color = if (isSelected) TextWhite else TextSlate,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // EMERGENCY MESSAGE PREVIEW
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "EMERGENCY MESSAGE PREVIEW",
                        color = TextSlateMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    DemoModeBadge()
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    color = CanvasNavy,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = "SHIELDUP ALERT",
                            color = Stage2Amber,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Situation: ${selectedSituation.title}",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Time: 8:45 PM (Live broadcast)",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Location: ${session.locationAddress}",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Safety Session: Active (${session.breadcrumbs.size} route waypoints)",
                            color = Stage1Emerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (customNoteText.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Note: \"$customNoteText\"",
                                color = AccentBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // OPTIONAL CUSTOM MESSAGE
                Text(
                    text = "ADD CUSTOM DETAILS (OPTIONAL)",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = customNoteText,
                    onValueChange = {
                        customNoteText = it
                        viewModel.repository.setCustomNote(it)
                    },
                    placeholder = {
                        Text(
                            "e.g. Person in dark jacket following near library alley...",
                            color = TextSlateMuted,
                            fontSize = 12.sp
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = Stage2Amber,
                        unfocusedBorderColor = BorderNavy,
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("custom_note_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                // LARGE "SEND ALERT" BUTTON
                Button(
                    onClick = {
                        viewModel.startStage2Countdown(selectedSituation, customNoteText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Stage2Amber),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("send_alert_action_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SEND ALERT (5s GRACE COUNTDOWN)",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // CURRENT LOCATION
        LocationCard(
            address = session.locationAddress,
            lat = session.latitude,
            lng = session.longitude
        )

        Spacer(modifier = Modifier.height(18.dp))

        // ROUTE / MOVEMENT HISTORY
        MovementTimeline(breadcrumbs = session.breadcrumbs)

        // CONFIRM SAFE DIALOG
        if (showSafeConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { showSafeConfirmationDialog = false },
                containerColor = CardNavy,
                title = {
                    Text("Confirm You Are Safe", color = TextWhite, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(
                        "This will terminate the active danger alert and notify your contacts that you are now safe and secure.",
                        color = TextSlate
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.markStage2Safe()
                            showSafeConfirmationDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald)
                    ) {
                        Text("Yes, I'm Safe", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSafeConfirmationDialog = false }) {
                        Text("Cancel", color = TextSlate)
                    }
                }
            )
        }
    }
}
