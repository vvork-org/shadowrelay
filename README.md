# MurmurRelay

<img src="https://i.imgur.com/K3yQKtx.png" width="200" alt="MurmurRelay Logo">

**Encrypt locally. Send anywhere.**

MurmurRelay is a lightweight encrypted messaging relay SDK that separates secure message handling from the service used to deliver messages.

It is designed so apps can encrypt messages locally, send them through a configurable transport layer, and decrypt them only in clients that have the shared channel key. The transport can be Firebase, WebSockets, a custom backend, or another service.

## What it does

MurmurRelay handles:

- message wrapping
- message IDs
- timestamps
- client-side encryption
- client-side decryption
- transport abstraction
- simple send and observe APIs

The relay service only moves encrypted payloads. It does not need to understand the message contents.

## Current status

MurmurRelay is currently an Android/Kotlin SDK.

Future versions may explore support for iOS, web, and additional transport adapters.

## Project structure

```text
MurmurRelay
├── sample
├── murmurrelay_core
└── murmurrelay_firebase
```

## Basic usage

```kotlin
val relay = MurmurRelay(
    transport = InMemoryRelayTransport()
)

val channelId = "demo-channel"
val channelKey = MurmurRelay.createChannelKey()

relay.observe(
    channelId = channelId,
    channelKey = channelKey
) { message ->
    println(message.payload)
}

relay.send(
    channelId = channelId,
    channelKey = channelKey,
    payload = "Hello from MurmurRelay"
) { result ->
    when (result) {
        is MurmurRelayResult.Success -> println("Message sent")
        is MurmurRelayResult.Error -> println(result.message)
    }
}
```

## Transport model

MurmurRelay uses a simple transport interface. A transport is responsible for moving encrypted payloads between devices or clients.

```kotlin
interface RelayTransport {
    fun sendMessage(
        channelId: String,
        encryptedPayload: String,
        onComplete: (MurmurRelayResult) -> Unit
    )

    fun observeMessages(
        channelId: String,
        onMessage: (String) -> Unit
    )
}
```

This allows the SDK to support different delivery systems without changing the encryption or message model.

## Included modules

### murmurrelay_core

Core SDK logic:

- MurmurRelay
- MurmurMessage
- encryption and decryption
- message encoding
- transport interface
- in-memory sample transport

### murmurrelay_firebase

Firebase Realtime Database transport adapter for moving encrypted payloads.

Apps using this module should provide their own Firebase project configuration.

### sample

A small demo app showing how MurmurRelay works using the in-memory sample transport.

The sample does not require Firebase.

## Encryption

🟨 MurmurRelay does not currently handle key exchange. Apps are responsible for securely sharing or deriving channel keys.

MurmurRelay currently uses:

- AES-GCM
- PBKDF2WithHmacSHA256
- per-message random salt
- per-message random IV
- authenticated encryption

Messages are encrypted before they are passed to the transport layer.

## Roadmap

Possible future work:

- digital signatures
- sender verification
- ECDH key exchange
- key rotation
- replay protection
- WebSocket transport
- Kotlin Multiplatform, iOS, or web support
- improved background delivery guidance for Android
- WebSocket transport adapter
- local network transport adapter
- custom backend transport examples
- community-contributed transport adapters

## License

MIT

## Author

Andy Friedl

Product designer / developer exploring secure communication tooling.

GitHub: https://github.com/andyfriedl
Website: https://andyfriedl.com
