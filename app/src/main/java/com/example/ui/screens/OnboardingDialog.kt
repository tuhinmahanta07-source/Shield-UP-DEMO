package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.theme.*

@Composable
fun OnboardingDialog(
    viewModel: ShieldUpViewModel,
    onDismiss: () -> Unit
) {
    var currentSlide by remember { mutableIntStateOf(1) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = CanvasNavy,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(28.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DemoModeBadge()
                    TextButton(onClick = onDismiss) {
                        Text("Skip", color = TextSlate, fontWeight = FontWeight.SemiBold)
                    }
                }

                // SLIDE CONTENT
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    when (currentSlide) {
                        1 -> SlideWhatIsShieldUp()
                        2 -> SlideThreeStageSystem()
                        3 -> SlideTrustedCircles()
                        4 -> SlideSafetyJourney()
                        5 -> SlideEmergencyModeAndPermissions()
                    }
                }

                // BOTTOM CONTROLS & STEP INDICATOR
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Pager dots
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        (1..5).forEach { index ->
                            val isSelected = currentSlide == index
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 24.dp else 8.dp, 8.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) Stage1Emerald else BorderNavy)
                            )
                        }
                    }

                    // Navigation buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (currentSlide > 1) {
                            OutlinedButton(
                                onClick = { currentSlide-- },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                                border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp)
                            ) {
                                Text("Back", fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                if (currentSlide < 5) {
                                    currentSlide++
                                } else {
                                    onDismiss()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(if (currentSlide > 1) 1f else 2f)
                                .height(50.dp)
                                .testTag("onboarding_next_button")
                        ) {
                            Text(
                                text = if (currentSlide == 5) "Get Started" else "Next",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SlideWhatIsShieldUp() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Stage1EmeraldContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = null,
                tint = Stage1Emerald,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Welcome to ShieldUp",
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Your personal safety companion designed specifically for university campuses and late-night commutes.",
            color = TextSlate,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun SlideThreeStageSystem() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Stage1Emerald))
            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Stage2Amber))
            Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Stage3Rose))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "3-Stage Safety System",
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Core Principle: As danger level increases, user interaction requirements drop to zero.",
            color = Stage2Amber,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Stage 1: Subtle walk timer.\nStage 2: 1-tap rapid threat alert.\nStage 3: Instant SOS with automated broadcast.",
            color = TextSlate,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun SlideTrustedCircles() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(AccentBlueContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = null,
                tint = AccentBlue,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Trusted Circles",
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pre-configure your family, roommates, campus advisors, and campus security escorts. They automatically receive your live encrypted telemetry when danger arises.",
            color = TextSlate,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun SlideSafetyJourney() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(Stage1EmeraldContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = null,
                tint = Stage1Emerald,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Safety Journeys",
            color = TextWhite,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Walking to your dorm late at night? Start a timed journey. If you fail to check in upon arrival, ShieldUp automatically alerts your circle.",
            color = TextSlate,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun SlideEmergencyModeAndPermissions() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Stage3RoseContainer)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = Stage3Rose,
                modifier = Modifier.size(44.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Emergency Mode & Permissions",
            color = TextWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Location Permission: ShieldUp requests location access solely to transmit GPS coordinates to your chosen trusted contacts during active safety sessions. No intrusive background recording.",
            color = TextSlate,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "✓ Prototype Mode Enabled: Safe for demo presentation without real emergency dispatch charges.",
                color = Stage1Emerald,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}
