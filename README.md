# ShadowRelay

**Transport-agnostic secure message relay SDK**

ShadowRelay is a lightweight encrypted messaging relay SDK that separates secure message handling from the service used to deliver messages.

It is designed so apps can encrypt messages locally, send them through a configurable transport layer, and decrypt them only on the receiving device. The transport can be Firebase, WebSockets, a custom backend, or another service.

## What it does

ShadowRelay handles:

- message wrapping
- message IDs
- timestamps
- client-side encryption
- client-side decryption
- transport abstraction
- simple send and observe APIs

The relay service only moves encrypted payloads. It does not need to understand the message contents.

## Current status

ShadowRelay is currently an Android/Kotlin SDK.

Future versions may explore support for iOS, web, and additional transport adapters.

## Project structure

```text
ShadowRelay
├── sample
├── shadowrelay_core
└── shadowrelay_firebase
```

## Basic usage

```kotlin
val relay = ShadowRelay(
    transport = InMemoryRelayTransport()
)

val channelId = "demo-channel"
val channelKey = ShadowRelay.createChannelKey()

relay.observe(
    channelId = channelId,
    channelKey = channelKey
) { message ->
    println(message.payload)
}

relay.send(
    channelId = channelId,
    channelKey = channelKey,
    payload = "Hello from ShadowRelay"
) { result ->
    when (result) {
        is ShadowRelayResult.Success -> println("Message sent")
        is ShadowRelayResult.Error -> println(result.message)
    }
}
```

## Transport model

ShadowRelay uses a simple transport interface. A transport is responsible for moving encrypted payloads between devices or clients.

```kotlin
interface RelayTransport {
    fun sendMessage(
        channelId: String,
        encryptedPayload: String,
        onComplete: (ShadowRelayResult) -> Unit
    )

    fun observeMessages(
        channelId: String,
        onMessage: (String) -> Unit
    )
}
```

This allows the SDK to support different delivery systems without changing the encryption or message model.

## Included modules

### shadowrelay_core

Core SDK logic:

- ShadowRelay
- ShadowMessage
- encryption and decryption
- message encoding
- transport interface
- in-memory sample transport

### shadowrelay_firebase

Firebase Realtime Database transport adapter.

Apps using this module should provide their own Firebase project configuration.

### sample

A small demo app showing how ShadowRelay works using the in-memory sample transport.

The sample does not require Firebase.

## Encryption

ShadowRelay currently uses:

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
- iOS or web support
- improved background delivery guidance for Android

## License

MIT

## Author

Andy Friedl

Product designer / developer exploring secure communication tooling.

GitHub: https://github.com/andyfriedl
Website: https://andyfriedl.com
