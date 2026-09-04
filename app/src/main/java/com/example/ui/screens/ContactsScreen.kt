package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Contact
import com.example.ui.AppScreen
import com.example.ui.ShieldUpViewModel
import com.example.ui.components.DemoModeBadge
import com.example.ui.theme.*

@Composable
fun ContactsScreen(
    viewModel: ShieldUpViewModel,
    modifier: Modifier = Modifier
) {
    val contacts by viewModel.contacts.collectAsState()
    val showAddDialog by viewModel.showAddContactDialog.collectAsState()
    val editingContact by viewModel.editingContact.collectAsState()

    var testPingContactName by remember { mutableStateOf<String?>(null) }
    var contactToDelete by remember { mutableStateOf<Contact?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CanvasNavy)
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp)
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.testTag("contacts_back_button")
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }
                Text(
                    text = "Trusted Circles",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            DemoModeBadge()
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Contacts designated to receive live GPS alerts across Stage 1, 2, and 3 triggers.",
            color = TextSlate,
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 8.dp, end = 8.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        // ADD NEW CONTACT ACTION BAR
        Button(
            onClick = { viewModel.showAddContact() },
            colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("add_new_contact_button")
        ) {
            Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Add Trusted Contact", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // CONTACTS LIST
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(contacts, key = { it.id }) { contact ->
                ContactCard(
                    contact = contact,
                    onEdit = { viewModel.showEditContact(contact) },
                    onDelete = { contactToDelete = contact },
                    onTogglePrimary = { viewModel.repository.setPrimaryContact(contact.id) },
                    onTestPing = { testPingContactName = contact.name }
                )
            }
        }
    }

    // CONTACT ADD / EDIT DIALOG
    if (showAddDialog) {
        ContactFormDialog(
            initialContact = editingContact,
            onDismiss = { viewModel.dismissContactDialog() },
            onSave = { name, phone, relationship, isPrimary, isTrusted ->
                if (editingContact != null) {
                    viewModel.repository.updateContact(
                        editingContact!!.copy(
                            name = name,
                            phone = phone,
                            relationship = relationship,
                            isPrimaryEmergency = isPrimary,
                            isTrusted = isTrusted,
                            avatarLetter = name.take(1).uppercase()
                        )
                    )
                } else {
                    viewModel.repository.addContact(name, phone, relationship, isPrimary, isTrusted)
                }
                viewModel.dismissContactDialog()
            }
        )
    }

    // TEST PING CONFIRMATION DIALOG
    testPingContactName?.let { name ->
        AlertDialog(
            onDismissRequest = { testPingContactName = null },
            containerColor = CardNavy,
            title = {
                Text("Test Ping Sent (Simulated)", color = TextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "A sample encrypted telemetry SMS has been simulated for $name. No actual SMS credits consumed.",
                    color = TextSlate
                )
            },
            confirmButton = {
                Button(
                    onClick = { testPingContactName = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald)
                ) {
                    Text("OK")
                }
            }
        )
    }

    // DELETE CONFIRMATION DIALOG
    contactToDelete?.let { contact ->
        AlertDialog(
            onDismissRequest = { contactToDelete = null },
            containerColor = CardNavy,
            title = {
                Text("Delete Contact?", color = TextWhite, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    "Are you sure you want to remove ${contact.name} from your safety circle?",
                    color = TextSlate
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.repository.deleteContact(contact.id)
                        contactToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Stage3Rose)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { contactToDelete = null }) {
                    Text("Cancel", color = TextSlate)
                }
            }
        )
    }
}

@Composable
private fun ContactCard(
    contact: Contact,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onTogglePrimary: () -> Unit,
    onTestPing: () -> Unit
) {
    Surface(
        color = CardNavy,
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (contact.isPrimaryEmergency) Stage3Rose.copy(alpha = 0.5f) else BorderNavy
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(if (contact.isPrimaryEmergency) Stage3RoseContainer else Stage1EmeraldContainer)
                    ) {
                        Text(
                            text = contact.avatarLetter,
                            color = if (contact.isPrimaryEmergency) Stage3Rose else Stage1Emerald,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = contact.name,
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
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
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                        Text(
                            text = "${contact.relationship} • ${contact.phone}",
                            color = TextSlate,
                            fontSize = 12.sp
                        )
                    }
                }

                Row {
                    IconButton(onClick = onEdit) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit", tint = TextSlate, modifier = Modifier.size(18.dp))
                    }
                    IconButton(onClick = onDelete) {
                        Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = "Delete", tint = Stage3Rose.copy(alpha = 0.8f), modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderNavy, thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onTogglePrimary,
                    contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = if (contact.isPrimaryEmergency) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = if (contact.isPrimaryEmergency) Stage2Amber else TextSlateMuted,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (contact.isPrimaryEmergency) "Primary Emergency Contact" else "Make Primary",
                        color = if (contact.isPrimaryEmergency) Stage2Amber else TextSlate,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedButton(
                    onClick = onTestPing,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Stage1Emerald),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Stage1Emerald.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text("Test Ping", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ContactFormDialog(
    initialContact: Contact?,
    onDismiss: () -> Unit,
    onSave: (name: String, phone: String, relationship: String, isPrimary: Boolean, isTrusted: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(initialContact?.name ?: "") }
    var phone by remember { mutableStateOf(initialContact?.phone ?: "") }
    var relationship by remember { mutableStateOf(initialContact?.relationship ?: "") }
    var isPrimary by remember { mutableStateOf(initialContact?.isPrimaryEmergency ?: false) }
    var isTrusted by remember { mutableStateOf(initialContact?.isTrusted ?: true) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CardNavy,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderNavy),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (initialContact != null) "Edit Contact" else "Add Trusted Contact",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", color = TextSlate) },
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
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number", color = TextSlate) },
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
                    value = relationship,
                    onValueChange = { relationship = it },
                    label = { Text("Relationship (e.g. Sister, Roommate)", color = TextSlate) },
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

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Primary Emergency Contact", color = TextWhite, fontSize = 13.sp)
                    Switch(
                        checked = isPrimary,
                        onCheckedChange = { isPrimary = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Stage3Rose,
                            checkedTrackColor = Stage3RoseContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancel", color = TextSlate)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank() && phone.isNotBlank()) {
                                onSave(name, phone, if (relationship.isBlank()) "Friend" else relationship, isPrimary, isTrusted)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Stage1Emerald),
                        shape = RoundedCornerShape(12.dp),
                        enabled = name.isNotBlank() && phone.isNotBlank()
                    ) {
                        Text("Save Contact", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
