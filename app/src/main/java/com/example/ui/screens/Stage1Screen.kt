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
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.LocationCard
import com.example.ui.components.MovementTimeline
import com.example.ui.components.PulsingDot
import com.example.ui.components.StageTag
import com.example.ui.theme.*

@Composable
fun Stage1Screen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.safetySession.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val isSessionActive = session.stage == SafetyStage.STAGE_1_SHIELD && session.isRunning

    var selectedMinutes by remember { mutableIntStateOf(15) }
    var showConfirmSafeDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNavy)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 28.dp)
    ) {
        // TOP APP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.navigateTo(AppScreen.HOME) },
                modifier = Modifier.testTag("stage_1_back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = TextWhite
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                StageTag(text = "STAGE 1", containerColor = Stage1EmeraldContainer, contentColor = Stage1Emerald)
                Spacer(modifier = Modifier.width(8.dp))
                DemoModeBadge()
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // HEADER
        Text(
            text = "I DON'T FEEL SAFE",
            color = TextWhite,
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
        Text(
            text = "For isolated, dark, or suspicious areas ahead. Activates background live telemetry and notifies your circle.",
            color = TextSlate,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(18.dp))

        // STATUS OR ACTIVE TIMER CARD
        if (isSessionActive) {
            // CONFIRMATION BANNER & LIVE COUNTDOWN
            Surface(
                color = Stage1EmeraldContainer.copy(alpha = 0.8f),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Stage1Emerald),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("shield_active_confirmation_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        PulsingDot(color = Stage1Emerald, sizeDp = 12)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SHIELD MODE ACTIVATED",
                            color = Stage1Emerald,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    val minutes = session.remainingSeconds / 60
                    val seconds = session.remainingSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        color = TextWhite,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )

                    Text(
                        text = "Safety session countdown in progress",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Surface(
                        color = CanvasNavy.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Stage1Emerald,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Your trusted contacts have been notified with your live broadcast location (Simulated).",
                                color = TextWhite,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ACTION CONTROLS WHEN ACTIVE
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = { showConfirmSafeDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("mark_safe_button")
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("I'M SAFE / END", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.STAGE_2) },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Stage2Amber),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Stage2Amber),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("escalate_to_stage_2_button")
                ) {
                    Icon(imageVector = Icons.Default.PriorityHigh, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("ESCALATE", fontWeight = FontWeight.Bold)
                }
            }
        } else {
            // ACTIVATION SECTION
            Surface(
                color = CardNavy,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "SAFETY SESSION DURATION",
                        color = TextSlateMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(10, 15, 20, 30).forEach { mins ->
                            val isSelected = selectedMinutes == mins
                            Surface(
                                color = if (isSelected) Stage1EmeraldContainer else SurfaceElevated,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Stage1Emerald) else null,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedMinutes = mins }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "${mins}m",
                                        color = if (isSelected) Stage1Emerald else TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // LARGE GREEN ACTIVATE SHIELD BUTTON
                    Button(
                        onClick = { viewModel.activateStage1(selectedMinutes) },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("start_shield_session_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "ACTIVATE SHIELD (${selectedMinutes} MINS)",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // LOCATION BROADCAST PREVIEW
        LocationCard(
            address = session.locationAddress,
            lat = session.latitude,
            lng = session.longitude
        )

        Spacer(modifier = Modifier.height(18.dp))

        // SELECT TRUSTED CONTACTS TO NOTIFY
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "NOTIFY TRUSTED CIRCLE",
                            color = TextSlateMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "Select who receives simulated safety pings",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = "${session.selectedContactIds.size}/${contacts.size} Selected",
                        color = Stage1Emerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                contacts.forEach { contact ->
                    val isSelected = session.selectedContactIds.contains(contact.id)
                    Surface(
                        color = if (isSelected) SurfaceElevated else CanvasNavy,
                        shape = RoundedCornerShape(14.dp),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, Stage1Emerald.copy(alpha = 0.5f)) else androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.repository.toggleContactSelection(contact.id) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(Stage1EmeraldContainer)
                                ) {
                                    Text(
                                        text = contact.avatarLetter,
                                        color = Stage1Emerald,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = contact.name,
                                            color = TextWhite,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        if (contact.isPrimaryEmergency) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Surface(
                                                color = Stage3RoseContainer,
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = "PRIMARY",
                                                    color = Stage3Rose,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(
                                        text = "${contact.relationship} • ${contact.phone}",
                                        color = TextSlateMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = { viewModel.repository.toggleContactSelection(contact.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Stage1Emerald,
                                    uncheckedColor = TextSlateMuted,
                                    checkmarkColor = CanvasNavy
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // MOVEMENT TRAIL BREADCRUMBS
        MovementTimeline(breadcrumbs = session.breadcrumbs)

        // CONFIRM SAFE DIALOG
        if (showConfirmSafeDialog) {
            AlertDialog(
                onDismissRequest = { showConfirmSafeDialog = false },
                containerColor = CardNavy,
                title = {
                    Text("Confirm You Are Safe", color = TextWhite, fontWeight = FontWeight.Bold)
                },
                text = {
                    Text(
                        "This will deactivate Shield Mode and notify your trusted contacts that you have arrived safely.",
                        color = TextSlate
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.endStage1Safe()
                            showConfirmSafeDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald)
                    ) {
                        Text("Yes, I'm Safe", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirmSafeDialog = false }) {
                        Text("Keep Shield Active", color = TextSlate)
                    }
                }
            )
        }
    }
}
