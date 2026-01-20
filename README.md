# AutoRune Client

The main client for AutoRune - the central orchestration layer ("brain") of the platform. Responsible for running multiple instances of injected gamepacks, executing user-created scripts, simulating human-like interactions, and communicating with the remote dashboard via WebSocket.

## Technology Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin 1.3.61 with Java interoperability |
| JVM Target | Java 11 |
| Build System | Maven 3.8.1 (multi-module project) |
| Async Framework | Kotlinx Coroutines 1.3.3 |
| Event System | Guava 28.1-jre EventBus |
| Networking | Netty Socket.IO 1.7.12 |
| Bytecode | ASM 7.3.1 (tree, analysis, commons, util) |
| Serialization | GSON 2.8.6 |
| UI Framework | Swing with Radiance Substance 3.4 theming |
| Logging | SLF4J 2.0.0-alpha1 |

## Architecture

The client is organized as a **multi-module Maven project** with 5 main modules:

```
Client/
├── autorune-client/           # Core client logic and instance management
├── autorune-client-launcher/  # Application entry point
├── autorune-socket-server/    # Dashboard communication via WebSocket
├── autorune-interaction/      # Human-like mouse/keyboard simulation
└── test-scripts/              # Example script implementations
```

## Core Modules

### 1. autorune-client (Core Module)

The central module managing game instances, accounts, and script execution.

**Key Components:**
- **ClientInstance** - Represents a single running game instance with applet, client reference, account profile, script executors, and game state tracking
- **ClientInstanceRepo** - Singleton repository managing all instances with add/remove/select operations via event-driven pattern
- **ClientInstanceLoader** - Creates client instances from injected gamepack bytecode
- **InjectedInstanceFetcher** - Fetches and loads injected game instances from Jagex resources
- **ClientInstanceClassLoader** - Custom ClassLoader with security permissions for game class loading
- **ClientInstanceCallbacks** - Handles client ticks, graphics rendering, and script execution callbacks

**Game States:**
- `STARTUP`, `LOGIN_SCREEN`, `LOGIN_AUTHENTICATOR`, `LOGGING_IN`
- `GAME_LOADING`, `LOGGED_IN`, `CONNECTION_LOST`, `WORLD_HOPPING`

### 2. autorune-socket-server (Dashboard Communication)

Enables real-time bidirectional communication with the remote dashboard.

**Protocol Events:**
| Event | Direction | Purpose |
|-------|-----------|---------|
| `INSTANCE_CONTROL` | Bidirectional | Instance add/remove/select |
| `ACCOUNT_CONTROL` | Bidirectional | Account login/logout/select |
| `INITIAL_SYNC` | Dashboard → Client | Request full state snapshot |
| `SCREENSHOT` | Dashboard → Client | Capture and transmit PNG |

**Listeners (Incoming):**
- `InitialSyncListener` - Syncs all instances and account states
- `AccountControlListener` - Handles account selection and authentication
- `InstanceControlListener` - Manages instance lifecycle
- `ScreenshotListener` - Captures and broadcasts screenshots

**Emitters (Outgoing):**
- `InstanceControlEmitter` - Broadcasts instance lifecycle events
- `AccountControlEmitter` - Broadcasts account state changes

**Connection:** Socket.IO server on `localhost:5150`

### 3. autorune-interaction (Human Simulation)

Sophisticated mouse movement and interaction simulation.

**Mouse Path Algorithms:**
- **LinearPath** - Straight-line movement
- **PerlinPath** - Perlin noise for natural jitter and acceleration
- **BezierPath** / **KnottedBezierPath** - Smooth curve-based paths
- **HumanPath** - Specialized human-like movement patterns

**Behavioral Characteristics (per account):**
- `viewportMisClickProbability` - Random viewport click miss (1-10%)
- `widgetMisClickProbability` - Widget interface click miss (0.5-5%)
- `mouseDpi` - Simulated DPI (400-2800)
- `mouseAcceleration` - OS acceleration simulation
- `mousePollingRate` - Polling rate (100-1000 Hz)
- `droppingSloppiness` - Item dropping inaccuracy (4-20%)
- `droppingType` - Column/Row/Spiral patterns

Characteristics are persisted as JSON per account and generated using SecureRandom.

### 4. autorune-client-launcher (Entry Point)

Application bootstrap and main frame initialization.

- Initializes UI on Event Dispatch Thread
- Handles frame positioning with screen boundary detection
- Creates AutoRuneFrame with display configuration

## Key Features

### Multi-Instance Management
- Create and manage multiple concurrent game instances
- Load injected gamepacks from Jagex resources
- Track game state transitions across instances
- Account management with credential storage
- World hopping capabilities

### Script Execution
- Scripts inherit from `Script` base class with `@ScriptManifest` annotation
- Override methods: `workersToExecute()`, `onInitialize()`, `onStart()`, `onLoop()`, `onStop()`
- Coroutine-based execution with cooperative multitasking
- Debug script support for development

### Real-time Monitoring
- Live screenshot capture and transmission
- Account state broadcasting
- Instance state synchronization
- Game state event emission

### Graphics Rendering
- Custom rendering pipeline with Graphics2D
- Integration with game raster provider
- Overlay rendering for debug tools
- Anti-aliased rendering support

### Event-Driven Architecture
Events published via Guava EventBus:
- **Account Events**: `AccountSelectedEvent`
- **Instance Events**: `AddInstanceEvent`, `RemovedInstanceEvent`, `SelectedInstanceEvent`
- **Game State Events**: `GameStateUpdateEvent`
- **UI Events**: Navigation, Sidebar panel events

## UI Components

- **AutoRuneFrame** - Undecorated JFrame with custom window decorations
- **Sidebar** - Navigation and tools panel
- **Instance Tabs** - Tabbed interface for multiple game instances
- **Debug Panel** - Script debugging interface
- **Script Management Panel** - Script listing and management
- **Theme** - Obsidian skin with Substance Look & Feel

## Instance Creation Flow

1. Dashboard sends `INSTANCE_ADD` command
2. Client posts `AddInstanceEvent` on EventBus
3. `ClientInstanceRepo` receives event and launches coroutine
4. `InjectedInstanceFetcher.getInjectedInstance()` fetches Jagex resource
5. `JarDownloader` downloads gamepack JAR
6. `JarLoader` loads and deobfuscates bytecode
7. `ClientInstanceLoader` creates instance with custom ClassLoader
8. Applet stub configured with codebase and parameters
9. Client callbacks initialized (tick, render, paint)
10. Instance added to repository and UI

## Security

- Custom ClassLoader with restricted permissions
- AWT, file I/O, socket, and property access controlled
- Cache directory restrictions (`.jagex_cache_32`, `.file_store_32`)
- Network permissions scoped for Socket connections

## Building

```bash
mvn clean install
```

## Dependencies

- `autorune-utilities` - Shared utilities and ASM extensions
- `osrs-api` - Generated OSRS API interfaces
- `script-api` - Script development API
