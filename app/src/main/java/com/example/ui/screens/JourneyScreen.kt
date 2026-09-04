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
import com.example.model.SafetyJourney
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.PulsingDot
import com.example.ui.theme.*

@Composable
fun JourneyScreen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val journey by viewModel.safetyJourney.collectAsState()

    var destinationInput by remember { mutableStateOf(journey.destination) }
    var selectedDuration by remember { mutableIntStateOf(15) }
    var showEscalationPreviewModal by remember { mutableStateOf(false) }

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
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("journey_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Text(
                    text = "Safety Journey",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            DemoModeBadge()
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Automated arrival monitor for solo walks at night. Escalates if check-in is missed.",
            color = TextSlate,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )

        Spacer(modifier = Modifier.height(18.dp))

        if (journey.isActive) {
            // ACTIVE JOURNEY TRACKER CARD
            Surface(
                color = CardNavy,
                shape = RoundedCornerShape(26.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.5.dp,
                    if (journey.isExpired) Stage3Rose else AccentBlue
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("active_journey_card")
            ) {
                Column(
                    modifier = Modifier.padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            PulsingDot(color = if (journey.isExpired) Stage3Rose else AccentBlue, sizeDp = 10)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (journey.isExpired) "CHECK-IN MISSED" else "JOURNEY IN PROGRESS",
                                color = if (journey.isExpired) Stage3Rose else AccentBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                        Surface(
                            color = SurfaceElevated,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "MONITORED",
                                color = TextSlate,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Destination",
                        color = TextSlateMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = journey.destination,
                        color = TextWhite,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // COUNTDOWN DISPLAY
                    val m = journey.remainingSeconds / 60
                    val s = journey.remainingSeconds % 60
                    Text(
                        text = String.format("%02d:%02d", m, s),
                        color = if (journey.isExpired) Stage3Rose else TextWhite,
                        fontSize = 44.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = if (journey.isExpired) "Expected arrival elapsed" else "Remaining to expected arrival",
                        color = TextSlate,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // MISSED CHECK-IN ESCALATION WARNING BANNER
                    if (journey.isExpired) {
                        Surface(
                            color = Stage3RoseContainer,
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Stage3Rose),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Stage3Rose,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "MISSED SAFETY CHECK-IN!",
                                        color = Stage3Rose,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 0.8.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Timer expired without arrival confirmation. Escalation warning generated for your safety circle.",
                                    color = TextWhite,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { showEscalationPreviewModal = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Stage3Rose),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("View Escalation Preview", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // PRIMARY "I'M SAFE" ARRIVAL BUTTON (Prompt requirement)
                    Button(
                        onClick = { viewModel.completeJourney() },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("journey_im_safe_button")
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("I'M SAFE / I HAVE ARRIVED", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.extendJourney() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentBlue),
                            border = androidx.compose.foundation.BorderStroke(1.dp, AccentBlue),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                        ) {
                            Text("+5 Mins Delay", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }

                        // SIMULATE MISSED CHECK-IN DEMO BUTTON (Prompt requirement: "If the timer expires, simulate a missed safety check")
                        Button(
                            onClick = { viewModel.simulateJourneyExpiry() },
                            colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated, contentColor = Stage2Amber),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("simulate_journey_expiry_button")
                        ) {
                            Text("Simulate Expiry", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        } else {
            // SETUP JOURNEY CARD
            Surface(
                color = CardNavy,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "WHERE ARE YOU WALKING?",
                        color = TextSlateMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = destinationInput,
                        onValueChange = { destinationInput = it },
                        label = { Text("Destination", color = TextSlate) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = AccentBlue,
                            unfocusedBorderColor = BorderNavy,
                            focusedContainerColor = SurfaceElevated,
                            unfocusedContainerColor = SurfaceElevated
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("journey_destination_input")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick destination presets
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Campus Dorms", "Main Library", "Metro Station").forEach { preset ->
                            Surface(
                                color = SurfaceElevated,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.clickable { destinationInput = preset }
                            ) {
                                Text(
                                    text = preset,
                                    color = AccentBlue,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "ESTIMATED WALKING TIME",
                        color = TextSlateMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(8, 12, 15, 25).forEach { mins ->
                            val isSelected = selectedDuration == mins
                            Surface(
                                color = if (isSelected) AccentBlueContainer else SurfaceElevated,
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.dp, AccentBlue) else null,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDuration = mins }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "${mins}m",
                                        color = if (isSelected) AccentBlue else TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = {
                            if (destinationInput.isNotBlank()) {
                                viewModel.startJourney(destinationInput, selectedDuration)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = CanvasNavy),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("start_journey_button"),
                        enabled = destinationInput.isNotBlank()
                    ) {
                        Icon(imageVector = Icons.Default.Navigation, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("START SAFETY JOURNEY", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // ESCALATION PREVIEW CARD (Prompt requirement: "Show an escalation preview")
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
                    Text(
                        text = "SAFETY JOURNEY ESCALATION PREVIEW",
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
                            text = "AUTOMATED PROTOCOL ON MISSED ARRIVAL:",
                            color = Stage2Amber,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "1. +0m: Grace vibration alert sent to user device.",
                            color = TextWhite,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "2. +1m: Silent check-in prompt asking for PIN.",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "3. +2m: Escalation dispatch to 4 trusted circle contacts.",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "4. +3m: Live last-known GPS coordinates and breadcrumb route transmitted.",
                            color = AccentBlue,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // ESCALATION MODAL DIALOG
        if (showEscalationPreviewModal) {
            AlertDialog(
                onDismissRequest = { showEscalationPreviewModal = false },
                containerColor = CardNavy,
                title = {
                    Text("Escalation Preview (Simulated)", color = Stage3Rose, fontWeight = FontWeight.Bold)
                },
                text = {
                    Column {
                        Text(
                            "In a production system, a missed check-in triggers automated escalation to campus escorts or trusted circles. For this demonstration, you can verify this behavior safely.",
                            color = TextSlate,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            "Would you like to test escalating directly to Stage 2 Alert?",
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showEscalationPreviewModal = false
                            viewModel.navigateTo(AppScreen.STAGE_2)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage2Amber, contentColor = Color.Black)
                    ) {
                        Text("Open Stage 2 Alert", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEscalationPreviewModal = false }) {
                        Text("Dismiss", color = TextSlate)
                    }
                }
            )
        }
    }
}
