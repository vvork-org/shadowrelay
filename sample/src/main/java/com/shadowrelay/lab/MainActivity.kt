package com.shadowrelay.lab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shadowrelay.core.ShadowRelay
import com.shadowrelay.core.ShadowRelayResult
import com.shadowrelay.core.transport.InMemoryRelayTransport
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val relay = ShadowRelay(
            transport = InMemoryRelayTransport()
        )

        val channelId = "demo-channel"

        setContent {
            MaterialTheme {
                val channelKey = remember { ShadowRelay.createChannelKey() }
                val logItems = remember { mutableStateListOf<String>() }
                var logCounter by remember { mutableIntStateOf(1) }
                var receivedMessage by remember { mutableStateOf("No messages yet") }
                var messageInput by remember { mutableStateOf("") }

                fun addLog(message: String) {
                    if (message.startsWith("MESSAGE:") && logItems.isNotEmpty()) {
                        logItems.add(0, "---DIVIDER---")
                    }
                    logItems.add(0, "$logCounter. $message")
                    logCounter++
                }

                LaunchedEffect(Unit) {
                    relay.observe(
                        channelId = channelId,
                        channelKey = channelKey
                    ) { message ->
                        addLog("Encrypted message received")
                        addLog("Message decrypted locally")
                        receivedMessage = message.payload
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp, vertical = 60.dp),

                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.shadowrelay_logo),
                                contentDescription = "ShadowRelay Logo",
                                modifier = Modifier.size(40.dp)
                            )

                            Text(
                                buildAnnotatedString {
                                    withStyle(
                                        style = SpanStyle(fontWeight = FontWeight.ExtraBold)
                                    ) {
                                        append("SHADOW")
                                    }
                                    append("RELAY")
                                },
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }

                        Card {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Channel ID:")
                                Text(channelId)

                                Spacer(modifier = Modifier.height(8.dp))

                                Text("Channel Key:")
                                Text(channelKey.take(16) + "...")
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Decrypted Output:")
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(receivedMessage)
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text("Activity Log")

                                Spacer(modifier = Modifier.height(8.dp))

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(220.dp)
                                ) {
                                    LazyColumn(
                                        reverseLayout = true,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.55f)),
                                        contentPadding = PaddingValues(12.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                    items(logItems) { logItem ->
                                        Column {
                                            if (logItem == "---DIVIDER---") {
                                                HorizontalDivider(
                                                    modifier = Modifier.padding(vertical = 8.dp)
                                                )
                                            } else {
                                                Text(
                                                    text = logItem,
                                                    fontWeight = if (logItem.contains("MESSAGE:")) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        }
                                    }
                                        }
                                }
                            }
                        }

                        OutlinedTextField(
                            value = messageInput,
                            onValueChange = { messageInput = it },
                            label = { Text("Enter message") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = {
                                if (messageInput.isNotBlank()) {
                                    addLog("MESSAGE: $messageInput")
                                    addLog("Message packaged for secure delivery")
                                    addLog("Message Encrypted locally")
                                    addLog("Sent via sample transport (swap for your backend)")
                                    relay.send(
                                        channelId = channelId,
                                        channelKey = channelKey,
                                        payload = messageInput
                                    ) { result ->
                                        if (result is ShadowRelayResult.Error) {
                                            receivedMessage = result.message
                                        }
                                    }

                                    messageInput = ""
                                }
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Send")
                        }
                    }
                }
            }
        }
    }
}