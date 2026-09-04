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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.theme.*

@Composable
fun ResponderNetworkScreen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val responderApp by viewModel.responderApp.collectAsState()
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
                    modifier = Modifier.testTag("responders_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Text(
                    text = "Responder Network",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            DemoModeBadge()
        }

        Spacer(modifier = Modifier.height(6.dp))

        // PROMINENT FUTURE / PROTOTYPE DISCLAIMER (Prompt requirement: "This must be presented as a FUTURE/PROTOTYPE feature. Do not claim that real KYC or background verification is being performed.")
        Surface(
            color = Stage2AmberContainer.copy(alpha = 0.6f),
            shape = RoundedCornerShape(18.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Stage2Amber),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Handshake,
                    contentDescription = null,
                    tint = Stage2Amber,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "FUTURE ROADMAP CONCEPT • PROTOTYPE DEMO",
                        color = Stage2Amber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Designed for university security partnerships, certified student escorts, ex-service personnel, and approved local NGOs. Simulated verification pipeline.",
                        color = TextWhite,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // MULTI-STEP VERIFICATION PIPELINE FLOW
        // Step 1: Register
        // Step 2: Identity verification
        // Step 3: Background verification
        // Step 4: KYC
        // Step 5: Admin approval -> Verified responder
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "5-STAGE ONBOARDING PIPELINE",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(14.dp))

                val steps = listOf(
                    Triple(1, "Register", "Profile creation & campus affiliation"),
                    Triple(2, "Identity Verification", "Student card or government ID review"),
                    Triple(3, "Background Verification", "Safety records check & dean referral"),
                    Triple(4, "KYC & Code of Conduct", "Safety terms & digital affidavit"),
                    Triple(5, "Admin Approval", "Granted responder credentials & badge")
                )

                steps.forEach { (stepNum, title, desc) ->
                    val isDone = responderApp.step > stepNum || (responderApp.step == 5 && stepNum == 5)
                    val isCurrent = responderApp.step == stepNum && !responderApp.isApproved

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isDone -> Stage1Emerald
                                        isCurrent -> AccentBlue
                                        else -> SurfaceElevated
                                    }
                                )
                        ) {
                            if (isDone) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = CanvasNavy,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Text(
                                    text = "$stepNum",
                                    color = if (isCurrent) CanvasNavy else TextSlate,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                color = if (isCurrent || isDone) TextWhite else TextSlateMuted,
                                fontSize = 13.sp,
                                fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold
                            )
                            Text(
                                text = desc,
                                color = TextSlateMuted,
                                fontSize = 11.sp
                            )
                        }

                        if (isCurrent) {
                            Surface(
                                color = AccentBlueContainer,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "ACTIVE STEP",
                                    color = AccentBlue,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // INTERACTIVE STEP DEMONSTRATION CONTROLS
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "STEP ${responderApp.step} DETAILS (SIMULATED)",
                    color = AccentBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                when (responderApp.step) {
                    1 -> {
                        Text(
                            text = "Applicant: ${responderApp.fullName}",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Proposed Role: ${responderApp.role}",
                            color = TextSlate,
                            fontSize = 13.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.repository.advanceResponderStep(2) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = CanvasNavy),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Simulate Submitting ID Card", fontWeight = FontWeight.Bold)
                        }
                    }
                    2 -> {
                        Text(
                            text = "ID Verification: University ID #${responderApp.studentOrGovId}",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Simulating automated OCR and enrollment ledger check.",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.repository.advanceResponderStep(3) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = CanvasNavy),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Pass ID Check → Proceed to Background", fontWeight = FontWeight.Bold)
                        }
                    }
                    3 -> {
                        Text(
                            text = "Background Screening: ${responderApp.backgroundCheckStatus}",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Simulating campus conduct database query and mentor verification.",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.repository.advanceResponderStep(4) },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue, contentColor = CanvasNavy),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Background Cleared → Proceed to KYC", fontWeight = FontWeight.Bold)
                        }
                    }
                    4 -> {
                        Text(
                            text = "KYC & Ethics Affirmation: ${responderApp.kycStatus}",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Simulating digital signing of Good Samaritan & Escort Code of Conduct.",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = { viewModel.repository.advanceResponderStep(5) },
                            colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Grant Admin Approval", fontWeight = FontWeight.Bold)
                        }
                    }
                    else -> {
                        Surface(
                            color = Stage1EmeraldContainer,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = Stage1Emerald,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "VERIFIED RESPONDER ACTIVE",
                                        color = Stage1Emerald,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "Badge #VRP-492 issued to ${responderApp.fullName}. Authorized for low-latency emergency dispatch.",
                                        color = TextWhite,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedButton(
                            onClick = { viewModel.repository.resetResponderDemo() },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSlate),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Reset Responder Simulation", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
