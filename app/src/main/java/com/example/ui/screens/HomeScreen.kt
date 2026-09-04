package com.example.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.SafetyStage
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.components.PulsingDot
import com.example.ui.components.StageTag
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.safetySession.collectAsState()
    val journey by viewModel.safetyJourney.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val eventLogs by viewModel.eventLogs.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNavy)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        // TOP HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = stringResource(R.string.app_name),
                    color = TextWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.5).sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SAFETY COMPANION • ",
                        color = TextSlate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "DEMO MODE",
                        color = Stage1Emerald,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            Surface(
                color = CardNavy,
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                modifier = Modifier
                    .size(42.dp)
                    .clickable { viewModel.openOnboarding() }
                    .testTag("onboarding_info_icon")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    PulsingDot(
                        color = when (session.stage) {
                            SafetyStage.STAGE_3_EMERGENCY -> Stage3Rose
                            SafetyStage.STAGE_2_DANGER -> Stage2Amber
                            SafetyStage.STAGE_1_SHIELD -> Stage1Emerald
                            else -> Stage1Emerald
                        },
                        sizeDp = 8
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // STATUS SUMMARY BAR
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("status_summary_card")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(
                                when (session.stage) {
                                    SafetyStage.STAGE_3_EMERGENCY -> Stage3RoseContainer
                                    SafetyStage.STAGE_2_DANGER -> Stage2AmberContainer
                                    SafetyStage.STAGE_1_SHIELD -> Stage1EmeraldContainer
                                    else -> Stage1Emerald.copy(alpha = 0.12f)
                                }
                            )
                    ) {
                        Icon(
                            imageVector = when (session.stage) {
                                SafetyStage.STAGE_3_EMERGENCY -> Icons.Default.Warning
                                SafetyStage.STAGE_2_DANGER -> Icons.Default.PriorityHigh
                                SafetyStage.STAGE_1_SHIELD -> Icons.Default.Shield
                                else -> Icons.Default.Check
                            },
                            contentDescription = null,
                            tint = when (session.stage) {
                                SafetyStage.STAGE_3_EMERGENCY -> Stage3Rose
                                SafetyStage.STAGE_2_DANGER -> Stage2Amber
                                else -> Stage1Emerald
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "CURRENT STATUS",
                            color = TextSlateMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = when (session.stage) {
                                SafetyStage.STAGE_3_EMERGENCY -> "Emergency Mode Active!"
                                SafetyStage.STAGE_2_DANGER -> "Danger Alert Sent"
                                SafetyStage.STAGE_1_SHIELD -> {
                                    val m = session.remainingSeconds / 60
                                    val s = session.remainingSeconds % 60
                                    "Shield Active (${String.format("%02d:%02d", m, s)})"
                                }
                                else -> "System Standby"
                            },
                            color = when (session.stage) {
                                SafetyStage.STAGE_3_EMERGENCY -> Stage3Rose
                                SafetyStage.STAGE_2_DANGER -> Stage2Amber
                                SafetyStage.STAGE_1_SHIELD -> Stage1Emerald
                                else -> TextWhite
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.clickable { viewModel.navigateTo(AppScreen.CONTACTS) }
                ) {
                    Text(
                        text = "TRUSTED CIRCLE",
                        color = TextSlateMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "${contacts.size} Active",
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // THREE STAGE CARDS
        // 1. GREEN CARD: STAGE 1 — "I DON'T FEEL SAFE"
        Surface(
            color = Stage1EmeraldDark,
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.STAGE_1) }
                .testTag("stage_1_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StageTag(text = "STAGE 1")
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "I DON'T FEEL SAFE",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "Start a discreet 15-min safety session with live contact ping.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.STAGE_1) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Stage1EmeraldDark
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("activate_shield_button")
                    ) {
                        Text(
                            text = if (session.stage == SafetyStage.STAGE_1_SHIELD && session.isRunning) "Manage Shield" else "Activate Shield",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. AMBER CARD: STAGE 2 — "MAY BE IN DANGER"
        Surface(
            color = Stage2Amber,
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.STAGE_2) }
                .testTag("stage_2_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StageTag(text = "STAGE 2")
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PriorityHigh,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "MAY BE IN DANGER",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "Notify contacts: Followed, harassed, threatened, lost, or medical.",
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.STAGE_2) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("send_alert_button")
                    ) {
                        Text(
                            text = "Send Alert",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. RED CARD: STAGE 3 — "EMERGENCY"
        Surface(
            color = Stage3Rose,
            shape = RoundedCornerShape(26.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.STAGE_3) }
                .testTag("stage_3_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StageTag(text = "STAGE 3")
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "EMERGENCY",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-0.3).sp
                )
                Text(
                    text = "Immediate SOS, Campus Security & live voice relay simulation.",
                    color = Color.White.copy(alpha = 0.95f),
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Inverse Interaction: Zero Navigation",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Button(
                        onClick = { viewModel.navigateTo(AppScreen.STAGE_3) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White,
                            contentColor = Stage3Rose
                        ),
                        shape = RoundedCornerShape(16.dp),
                        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 10.dp),
                        modifier = Modifier.testTag("test_emergency_trigger_button")
                    ) {
                        Text(
                            text = "TEST TRIGGER",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ACTIVE SAFETY JOURNEY STRIP
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { viewModel.navigateTo(AppScreen.JOURNEY) }
                .testTag("active_journey_strip")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (journey.isActive) AccentBlue else TextSlateMuted)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (journey.isActive) "ACTIVE JOURNEY" else "SAFETY JOURNEY",
                            color = if (journey.isActive) AccentBlue else TextSlateMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = if (journey.isActive) {
                                val m = journey.remainingSeconds / 60
                                "To ${journey.destination} (${m}m left)"
                            } else {
                                "Walking somewhere alone? Start journey tracking."
                            },
                            color = TextWhite,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Text(
                    text = if (journey.isActive) "MANAGE" else "START",
                    color = AccentBlue,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // LATEST EVENT LOG SNIPPET
        eventLogs.firstOrNull()?.let { latestLog ->
            Surface(
                color = CardNavy,
                shape = RoundedCornerShape(20.dp),
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
                            text = "LATEST SAFETY EVENT",
                            color = TextSlateMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = latestLog.timeStr,
                            color = TextSlate,
                            fontSize = 10.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = latestLog.title,
                        color = TextWhite,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = latestLog.detail,
                        color = TextSlate,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // FAST PROTOTYPE DEMO CALLOUT FOR JURY
        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Startup Pitch Prototype: All safety notifications, calls, and emergency pings are realistically simulated for judging demonstration.",
                    color = TextSlate,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }
    }
}
