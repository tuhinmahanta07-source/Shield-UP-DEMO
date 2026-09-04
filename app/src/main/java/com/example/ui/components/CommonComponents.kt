package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BreadcrumbPoint
import com.example.ui.AppScreen
import com.example.ui.theme.*

@Composable
fun DemoModeBadge(modifier: Modifier = Modifier) {
    Surface(
        color = CardNavy,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Stage1Emerald)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "DEMO PROTOTYPE",
                color = Stage1Emerald,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun PulsingDot(
    color: Color = Stage1Emerald,
    sizeDp: Int = 10,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    Box(contentAlignment = Alignment.Center, modifier = modifier.size((sizeDp * 1.5).dp)) {
        Box(
            modifier = Modifier
                .size((sizeDp * scale).dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.25f))
        )
        Box(
            modifier = Modifier
                .size(sizeDp.dp)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Composable
fun StageTag(
    text: String,
    containerColor: Color = Color.White.copy(alpha = 0.2f),
    contentColor: Color = Color.White
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun LocationCard(
    address: String,
    lat: Double,
    lng: Double,
    modifier: Modifier = Modifier,
    onRefresh: (() -> Unit)? = null
) {
    Surface(
        color = CardNavy,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = AccentBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CURRENT BROADCAST LOCATION",
                        color = TextSlate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 0.8.sp
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Stage1Emerald)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "GPS LOCK",
                        color = Stage1Emerald,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = address,
                color = TextWhite,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Coordinates: ${String.format("%.4f", lat)}° N, ${String.format("%.4f", lng)}° W • University Campus Hub",
                color = TextSlateMuted,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun MovementTimeline(
    breadcrumbs: List<BreadcrumbPoint>,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CardNavy,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = null,
                    tint = AccentBlue,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SESSION MOVEMENT TRAIL",
                    color = TextSlate,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))

            breadcrumbs.forEachIndexed { index, point ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(if (point.isCurrent) Stage1Emerald else TextSlateMuted)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = point.label,
                            color = if (point.isCurrent) TextWhite else TextSlate,
                            fontSize = 13.sp,
                            fontWeight = if (point.isCurrent) FontWeight.Bold else FontWeight.Normal
                        )
                        Text(
                            text = point.timeStr,
                            color = TextSlateMuted,
                            fontSize = 11.sp
                        )
                    }
                    if (point.isCurrent) {
                        Surface(
                            color = Stage1EmeraldContainer,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "LIVE",
                                color = Stage1Emerald,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                if (index < breadcrumbs.size - 1) {
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .width(2.dp)
                            .height(10.dp)
                            .background(BorderLight)
                    )
                }
            }
        }
    }
}

@Composable
fun SleekBottomNavBar(
    currentScreen: AppScreen,
    onNavigate: (AppScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = CanvasNavy,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy.copy(alpha = 0.5f)),
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp)
        ) {
            NavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = currentScreen == AppScreen.HOME,
                onClick = { onNavigate(AppScreen.HOME) },
                testTag = "nav_home"
            )
            NavItem(
                icon = Icons.Default.Group,
                label = "Circles",
                isSelected = currentScreen == AppScreen.CONTACTS,
                onClick = { onNavigate(AppScreen.CONTACTS) },
                testTag = "nav_contacts"
            )
            NavItem(
                icon = Icons.Default.Navigation,
                label = "Journey",
                isSelected = currentScreen == AppScreen.JOURNEY,
                onClick = { onNavigate(AppScreen.JOURNEY) },
                testTag = "nav_journey"
            )
            NavItem(
                icon = Icons.Default.Shield,
                label = "Responders",
                isSelected = currentScreen == AppScreen.RESPONDERS,
                onClick = { onNavigate(AppScreen.RESPONDERS) },
                testTag = "nav_responders"
            )
            NavItem(
                icon = Icons.Default.Settings,
                label = "Setup",
                isSelected = currentScreen == AppScreen.SETTINGS,
                onClick = { onNavigate(AppScreen.SETTINGS) },
                testTag = "nav_settings"
            )
        }
    }
}

@Composable
private fun NavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .testTag(testTag)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) Stage1Emerald else TextWhite.copy(alpha = 0.45f),
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            color = if (isSelected) Stage1Emerald else TextWhite.copy(alpha = 0.45f),
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun SimulatedCallDialog(
    recipientName: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CardNavy,
            border = androidx.compose.foundation.BorderStroke(1.dp, Stage3Rose.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Surface(
                    color = Stage3RoseContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "SIMULATED CALL DEMO",
                        color = Stage3Rose,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                PulsingDot(color = Stage3Rose, sizeDp = 20)
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Calling...",
                    color = TextSlate,
                    fontSize = 14.sp
                )
                Text(
                    text = recipientName,
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Simulating automated hands-free distress call & live dispatch audio broadcast.",
                    color = TextSlateMuted,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Stage3Rose),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("end_simulated_call_button")
                ) {
                    Icon(imageVector = Icons.Default.CallEnd, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("End Simulated Call", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
