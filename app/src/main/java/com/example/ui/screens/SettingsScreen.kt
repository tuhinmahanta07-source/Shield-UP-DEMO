package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EmergencySettings
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.theme.*

@Composable
fun SettingsScreen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.emergencySettings.collectAsState()
    val contacts by viewModel.contacts.collectAsState()

    var policeNum by remember(settings) { mutableStateOf(settings.campusPolice) }
    var ambulanceNum by remember(settings) { mutableStateOf(settings.ambulance) }
    var helplineNum by remember(settings) { mutableStateOf(settings.womenHelpline) }
    var escortNum by remember(settings) { mutableStateOf(settings.campusEscort) }
    var communityEnabled by remember(settings) { mutableStateOf(settings.communityRespondersEnabled) }
    var pinEnabled by remember(settings) { mutableStateOf(settings.pinProtectionEnabled) }
    var pinCode by remember(settings) { mutableStateOf(settings.pinCode) }

    var savedSnackbar by remember { mutableStateOf(false) }

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
                    modifier = Modifier.testTag("settings_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Text(
                    text = "Emergency Settings",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            DemoModeBadge()
        }

        Spacer(modifier = Modifier.height(6.dp))

        // DISCLAIMER CARD
        Surface(
            color = SurfaceElevated,
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Stage2Amber,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "PROTOTYPE CONFIGURATION: Configure numbers and mock responders for startup competition demonstration. No real 911 dispatch calls occur.",
                    color = TextSlate,
                    fontSize = 11.sp,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // DISPATCH NUMBERS CONFIGURATION
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "EMERGENCY HOTLINES & ESCORT NUMBERS",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = policeNum,
                    onValueChange = { policeNum = it },
                    label = { Text("Police / Campus Security Dispatch", color = TextSlate) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = Stage1Emerald,
                        unfocusedBorderColor = BorderNavy,
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = ambulanceNum,
                    onValueChange = { ambulanceNum = it },
                    label = { Text("Ambulance / Campus EMS", color = TextSlate) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = Stage1Emerald,
                        unfocusedBorderColor = BorderNavy,
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = helplineNum,
                    onValueChange = { helplineNum = it },
                    label = { Text("Women's / Student Crisis Helpline", color = TextSlate) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = Stage1Emerald,
                        unfocusedBorderColor = BorderNavy,
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = escortNum,
                    onValueChange = { escortNum = it },
                    label = { Text("Other: 24/7 Campus Safety Escort", color = TextSlate) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = Stage1Emerald,
                        unfocusedBorderColor = BorderNavy,
                        focusedContainerColor = SurfaceElevated,
                        unfocusedContainerColor = SurfaceElevated
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // COMMUNITY RESPONDER & PIN PROTECTION
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "ADVANCED PROTOCOLS",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Community Responder Network",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Allow nearby verified student volunteers & campus security escorts to receive emergency pings.",
                            color = TextSlate,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                    Switch(
                        checked = communityEnabled,
                        onCheckedChange = { communityEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Stage1Emerald,
                            checkedTrackColor = Stage1EmeraldContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderNavy, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "PIN Duress Cancellation",
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Require 4-digit PIN to cancel an active SOS to prevent forced dismissal.",
                            color = TextSlate,
                            fontSize = 11.sp,
                            lineHeight = 15.sp
                        )
                    }
                    Switch(
                        checked = pinEnabled,
                        onCheckedChange = { pinEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Stage1Emerald,
                            checkedTrackColor = Stage1EmeraldContainer
                        )
                    )
                }

                if (pinEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = pinCode,
                        onValueChange = { if (it.length <= 4) pinCode = it },
                        label = { Text("Cancellation PIN Code", color = TextSlate) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedBorderColor = Stage1Emerald,
                            unfocusedBorderColor = BorderNavy,
                            focusedContainerColor = SurfaceElevated,
                            unfocusedContainerColor = SurfaceElevated
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // SAVE BUTTON
        Button(
            onClick = {
                viewModel.repository.updateSettings(
                    EmergencySettings(
                        campusPolice = policeNum,
                        emergencyDispatch = settings.emergencyDispatch,
                        campusEscort = escortNum,
                        ambulance = ambulanceNum,
                        womenHelpline = helplineNum,
                        communityRespondersEnabled = communityEnabled,
                        pinProtectionEnabled = pinEnabled,
                        pinCode = pinCode
                    )
                )
                savedSnackbar = true
            },
            colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("save_settings_button")
        ) {
            Icon(imageVector = Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Save Emergency Settings", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // ONBOARDING & PITCH DEMO CONTROLS
        Surface(
            color = CardNavy,
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "DEMONSTRATION TOOLKIT",
                    color = TextSlateMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { viewModel.openOnboarding() },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Replay Pitch Intro", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }

                    OutlinedButton(
                        onClick = { viewModel.navigateTo(AppScreen.RESPONDERS) },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentBlue),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AccentBlue.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Responder KYC Flow", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        if (savedSnackbar) {
            AlertDialog(
                onDismissRequest = { savedSnackbar = false },
                containerColor = CardNavy,
                title = { Text("Settings Saved", color = TextWhite, fontWeight = FontWeight.Bold) },
                text = { Text("Emergency numbers and prototype protocols updated successfully.", color = TextSlate) },
                confirmButton = {
                    Button(
                        onClick = { savedSnackbar = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald)
                    ) {
                        Text("OK")
                    }
                }
            )
        }
    }
}
