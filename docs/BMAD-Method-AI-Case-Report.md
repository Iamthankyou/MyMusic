# AI Utilization Case Report — BMAD Method for MyMusic App

---

| Field | Details |
|---|---|
| **Date of Submission** | 2026.04.15 |
| **Name (Department)** | Duy LQ (Multimedia 1P) |

---

## Case Title

**Building MyMusic Android App Using BMAD (Breakthrough Method for Agile AI-Driven Development)**

---

## Utilization Scale

- ✅ **Full transformation of development workflow**
- From manual planning + ad-hoc AI prompting → structured, agent-orchestrated, documentation-first AI development lifecycle

---

## Case Category

- **Productivity tool development (Agent-based development workflow)**
- **Design & Documentation**
  - General design (architecture, data model/schema, API/interface definition)
  - Design document generation (class diagram, requirement specification)
  - Code-based summarization or explanation
- **Coding**
  - Code generation (app, business logic implementation)
  - Refactoring (modularization, Clean Architecture separation)
- **Review**
  - Static analysis / potential defect inspection
  - Code change summary
- **Testing**
  - Unit test generation (planned)
  - Test automation environment setup (planned)

---

## Tools Used

Cursor IDE, Cline (VS Code), Antigravity (Google DeepMind)

## Models Used

Claude-4.5-Sonnet, Claude Opus 4.6, GPT-5

---

## Case Details

### 1. What is BMAD Method?

BMAD (**Breakthrough Method for Agile AI-Driven Development**) is an open-source framework that brings structure, governance, and repeatability to AI-assisted software development. Instead of "vibe coding" (ad-hoc prompting), it treats AI as a **disciplined team member** following an agile process with clear roles.

#### 1.1 BMAD vs. Traditional "Vibe Coding" — Why It Matters

```
┌─────────────────────────────────────────────────────────────────────┐
│                    THE PROBLEM WITH VIBE CODING                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   Developer ──prompt──> AI ──code──> ???                            │
│                                                                     │
│   ⚠️ No requirements doc    ⚠️ No architecture review              │
│   ⚠️ Context lost between    ⚠️ Inconsistent quality               │
│      sessions                ⚠️ "Black box" codebase               │
│   ⚠️ No traceability        ⚠️ Hard to onboard new members        │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────────┐
│                     BMAD METHOD SOLUTION                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   Human + AI Agents → PRD → Architecture → Stories → Code → QA     │
│                                                                     │
│   ✅ Full documentation     ✅ Architecture-first design            │
│   ✅ Persistent context     ✅ Consistent, auditable output         │
│   ✅ Version-controlled     ✅ Easy onboarding & handoff            │
│      artifacts              ✅ Traceable decisions                  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

#### 1.2 Core Concept — Specialized AI Agent Team (C4 Context Diagram)

```mermaid
C4Context
    title BMAD Method — System Context (C4 Level 1)

    Person(dev, "Developer", "Human developer who guides the process")

    System_Boundary(bmad, "BMAD Agent Team") {
        System(analyst, "Analyst Agent", "Brainstorming, research, scoping")
        System(pm, "Product Manager Agent", "PRD creation, requirements")
        System(architect, "Architect Agent", "System design, tech decisions")
        System(sm, "Scrum Master Agent", "User stories, acceptance criteria")
        System(developer, "Developer Agent", "Implementation by stories")
        System(qa, "QA Agent", "Testing, code review")
    }

    System_Ext(ide, "AI IDE", "Cursor / Cline / Claude Code")
    System_Ext(llm, "LLM Provider", "Claude / GPT / Gauss")
    System_Ext(repo, "Version Control", "Git Repository")

    Rel(dev, bmad, "Guides & reviews")
    Rel(bmad, ide, "Integrated into")
    Rel(bmad, llm, "Powered by")
    Rel(bmad, repo, "Artifacts stored in")
```

#### 1.3 BMAD Workflow — Phase-Based Development (C4 Container Diagram)

```mermaid
flowchart LR
    subgraph Phase1["📋 Phase 1: ANALYSIS"]
        A1["🧠 Brainstorming<br/>Research & Scoping"]
        A2["📝 Project Brief<br/>Goals & Constraints"]
    end

    subgraph Phase2["📐 Phase 2: PLANNING"]
        B1["📄 PRD Creation<br/>Requirements (FR/NFR)"]
        B2["🎨 UX Specification<br/>Screens & Flows"]
        B3["✅ PRD Review<br/>Checklist Validation"]
    end

    subgraph Phase3["🏗️ Phase 3: SOLUTIONING"]
        C1["🏛️ Architecture Design<br/>Layers, Modules, DI"]
        C2["📊 Data Model<br/>Entities & APIs"]
        C3["📋 Epic Breakdown<br/>→ User Stories"]
    end

    subgraph Phase4["⚙️ Phase 4: IMPLEMENTATION"]
        D1["🔨 Story-by-Story<br/>Development"]
        D2["🔍 Code Review<br/>Per Story"]
        D3["🧪 Testing<br/>Unit + Integration"]
    end

    Phase1 --> Phase2 --> Phase3 --> Phase4

    style Phase1 fill:#4A90D9,color:#fff
    style Phase2 fill:#7B68EE,color:#fff
    style Phase3 fill:#E67E22,color:#fff
    style Phase4 fill:#27AE60,color:#fff
```

#### 1.4 Agent Roles — Detailed Responsibilities

```mermaid
mindmap
  root((BMAD<br/>Agent Team))
    🔍 Analyst
      Brainstorming session
      Market research
      Feature scoping
      Risk identification
    📋 Product Manager
      PRD generation
      Requirements (FR/NFR)
      Acceptance criteria
      Stakeholder alignment
    🏛️ Architect
      Technology selection
      Module structure
      API design
      Data flow design
      C4 diagrams
    📝 Scrum Master
      Epic decomposition
      User story creation
      Sprint planning
      Task prioritization
    💻 Developer
      Code generation
      Clean Architecture
      Best practices
      Refactoring
    🧪 QA
      Test generation
      Code review
      Bug identification
      Quality gates
```

---

### 2. How We Applied BMAD to Build MyMusic App

#### 2.1 Project Overview

MyMusic is a **modern Android music app** built 100% in Kotlin with Jetpack Compose, Hilt DI, MVVM + Clean Architecture, powered by the Jamendo API.

#### 2.2 BMAD Artifact Flow for MyMusic (C4 Component Diagram)

```mermaid
flowchart TB
    subgraph Artifacts["📁 BMAD Output Artifacts (docs/)"]
        brief["📝 project-brief.md<br/>Goals, constraints, risks"]
        brainstorm["🧠 brain-stoming-documentation.md<br/>Tech stack, data flow, models"]
        prd["📄 prd.md<br/>16 FRs + 13 NFRs + Epics"]
        ux["🎨 front-end-spec.md<br/>Screens, nav, interactions"]
        arch["🏛️ fullstack-architecture.md<br/>21 sections, DI graph, media"]
        stories["📋 stories/<br/>22 user stories across 3 epics"]
        diagrams["📊 diagrams/<br/>Mermaid architecture overview"]
    end

    subgraph Source["💻 Generated Source Code"]
        domain["domain/<br/>5 models, 3 repos, 12 use cases"]
        data["data/<br/>6 services, 7 repos, mappers, paging"]
        presentation["presentation/<br/>Screens, ViewModels, components"]
        playback["playback/<br/>Media3, controller, service"]
        di["di/<br/>Hilt modules"]
    end

    brief --> brainstorm --> prd --> ux --> arch --> stories --> Source

    style Artifacts fill:#1a1a2e,color:#e0e0e0,stroke:#4A90D9
    style Source fill:#16213e,color:#e0e0e0,stroke:#27AE60
```

#### 2.3 MyMusic App Architecture (C4 Container Level)

```mermaid
graph TB
    subgraph Android["📱 MyMusic Android App"]
        subgraph PresentationLayer["Presentation Layer"]
            Compose["Jetpack Compose<br/>Material 3 UI"]
            ViewModels["ViewModels<br/>(Hilt-injected)"]
            Navigation["Compose Navigation<br/>NavGraph + BottomNav"]
        end

        subgraph DomainLayer["Domain Layer (Pure Kotlin)"]
            UseCases["Use Cases<br/>12 interactors"]
            Models["Domain Models<br/>Track, Album, Playlist, Artist"]
            RepoInterfaces["Repository Interfaces<br/>3 contracts"]
        end

        subgraph DataLayer["Data Layer"]
            RepoImpls["Repository Implementations<br/>7 classes"]
            Retrofit["Retrofit Services<br/>6 API endpoints"]
            Mappers["DTO → Domain Mappers"]
            Paging["Paging 3 Sources"]
            LocalDB["Room / DataStore<br/>(search history)"]
        end

        subgraph MediaLayer["Media / Playback"]
            ExoPlayer["Media3 ExoPlayer"]
            PlaybackCtrl["PlaybackController"]
            MediaSession["MediaSessionService"]
        end
    end

    subgraph External["External Systems"]
        JamendoAPI["🌐 Jamendo API<br/>api.jamendo.com/v3.0"]
    end

    Compose --> ViewModels --> UseCases --> RepoInterfaces
    RepoInterfaces -. binds .-> RepoImpls
    RepoImpls --> Retrofit --> JamendoAPI
    RepoImpls --> Paging
    RepoImpls --> Mappers
    ViewModels --> PlaybackCtrl --> ExoPlayer
    PlaybackCtrl --> MediaSession

    style PresentationLayer fill:#4A90D9,color:#fff
    style DomainLayer fill:#7B68EE,color:#fff
    style DataLayer fill:#E67E22,color:#fff
    style MediaLayer fill:#E74C3C,color:#fff
    style External fill:#95A5A6,color:#fff
```

#### 2.4 Detailed Data Flow (C4 Component Level)

```mermaid
sequenceDiagram
    actor User
    participant UI as Compose Screen
    participant VM as ViewModel
    participant UC as UseCase
    participant Repo as Repository
    participant API as Jamendo API
    participant Player as PlaybackController

    User->>UI: Opens Trending tab
    UI->>VM: collectAsStateWithLifecycle()
    VM->>UC: GetTrendingTracksUseCase()
    UC->>Repo: getTrendingTracks()
    Repo->>API: GET /tracks?order=popularity_week
    API-->>Repo: JSON Response (DTOs)
    Repo->>Repo: MapperDTO → Domain Model
    Repo-->>VM: Flow<PagingData<Track>>
    VM-->>UI: UiState (tracks list)
    UI-->>User: Display track cards

    User->>UI: Taps "Play" on track
    UI->>VM: onTrackClicked(track)
    VM->>Player: play(track.audioUrl)
    Player->>Player: ExoPlayer.setMediaItem()
    Player-->>VM: NowPlayingState (StateFlow)
    VM-->>UI: MiniPlayer appears
```

#### 2.5 Codebase Statistics

| Metric | Value |
|---|---|
| Total Kotlin source files | 102 |
| Documentation files (docs/) | 80 |
| User stories written | 22 (across 3+ epics) |
| Domain models | 5 (Track, Album, Artist, Playlist, SearchFilter) |
| Repository interfaces | 3 |
| Use cases | 12 |
| Retrofit services | 6 |
| Repository implementations | 7 |
| Presentation screens | 8+ |
| Architecture layers | 4 (Presentation, Domain, Data, Media) |
| **Test files** | **0** ⚠️ |

---

### 3. Why Should You Use BMAD? — Evidence & Proof

#### 3.1 Comparison: Vibe Coding vs. BMAD for Same Project

```mermaid
graph LR
    subgraph Vibe["❌ Vibe Coding Approach"]
        V1["Prompt: 'make me a music app'"]
        V2["Get messy code dump"]
        V3["Manually fix architecture"]
        V4["Lose context next session"]
        V5["Start over or patch"]
    end

    subgraph BMAD_Approach["✅ BMAD Approach"]
        B1["Analyst: scope & research"]
        B2["PM: PRD with 16 FRs"]
        B3["Architect: 21-section design"]
        B4["SM: 22 traceable stories"]
        B5["Dev: implement per story"]
    end

    V1 --> V2 --> V3 --> V4 --> V5
    B1 --> B2 --> B3 --> B4 --> B5

    style Vibe fill:#c0392b,color:#fff
    style BMAD_Approach fill:#27ae60,color:#fff
```

#### 3.2 Concrete Benefits Proven by MyMusic

| Benefit | Evidence from MyMusic |
|---|---|
| **Clean Architecture from Day 1** | 4-layer separation (presentation/domain/data/media) enforced by architecture doc before coding |
| **No Context Loss** | 80 docs serve as persistent context; AI agents always know project state |
| **Traceable Decisions** | Every feature maps to: PRD FR → Architecture Section → Epic → Story → Code |
| **Consistent Quality** | All 102 source files follow the same patterns (Hilt DI, StateFlow, Mapper pattern) |
| **Easy Onboarding** | New developer reads project-brief.md → PRD → architecture → starts coding any story |
| **Scalable Complexity** | Started with Trending Tracks MVP, expanded to 3 epics + 22 stories without architectural churn |

#### 3.3 Documentation-First Traceability Map

```mermaid
flowchart LR
    FR1["FR1: Trending Tracks"] --> Arch["Architecture §7: Data Layer<br/>§8: Playback Architecture"]
    Arch --> Epic1["Epic 1: Foundation & MVP"]
    Epic1 --> S1["Story 1.1: Setup"]
    Epic1 --> S2["Story 1.2: Trending List"]
    Epic1 --> S3["Story 1.3: Tap to Play"]
    S2 --> Code1["TrendingScreen.kt<br/>TrendingViewModel.kt<br/>TrendingTracksPagingSource.kt"]
    S3 --> Code2["PlayerRoute.kt<br/>PlayerViewModel.kt<br/>PlaybackController.kt"]

    style FR1 fill:#3498db,color:#fff
    style Arch fill:#9b59b6,color:#fff
    style Epic1 fill:#e67e22,color:#fff
    style S1 fill:#2ecc71,color:#fff
    style S2 fill:#2ecc71,color:#fff
    style S3 fill:#2ecc71,color:#fff
    style Code1 fill:#1abc9c,color:#fff
    style Code2 fill:#1abc9c,color:#fff
```

---

### 4. Weaknesses & Limitations of BMAD (Honest Assessment)

#### 4.1 Summary of Weaknesses

```mermaid
mindmap
  root((BMAD<br/>Weaknesses))
    ⏰ Overhead
      Heavy ceremony for small tasks
      PRD + Architecture + Stories before a single line of code
      Not suitable for quick bug fixes
    💰 Token Cost
      Comprehensive docs consume many tokens
      Multi-agent conversations multiply cost
      Each phase generates thousands of tokens
    🔗 Cascading Errors
      Analyst mistake propagates to PM → Architect → Dev
      Wrong requirement = wrong architecture = wrong code
      Requires human verification at each gate
    📦 Rigidity
      "Plan everything first" can slow exploration
      Updating formal docs before iterating on code
      Harder to pivot rapidly
    🎯 Forced Quality Metrics
      Adversarial review may create artificial nitpicking
      Minimum-issue requirements in code review = developer fatigue
    📏 Scale Limitations
      Best for single-repo projects
      Struggles with multi-service / cross-repo architectures
      Heavy for simple features
```

#### 4.2 Detailed Weakness Analysis for MyMusic

| Weakness | Impact on MyMusic | Mitigation |
|---|---|---|
| **Overhead for simple tasks** | Adding a simple color change requires checking PRD/Architecture alignment | Use BMAD "Quick Flow" track for minor fixes |
| **No test files generated** | Despite QA agent role, **0 test files exist** ⚠️ | BMAD guides test *strategy* but doesn't enforce test *creation* — needs manual discipline |
| **Token consumption** | ~80 docs generated = significant API cost | Use local/cheaper models for doc generation, premium models for architecture decisions |
| **Cascading errors** | If Jamendo API endpoint assumed incorrectly in PRD → wrong architecture → wrong code | Human review gate at each phase transition is critical |
| **Single-repo focus** | Works well for MyMusic (monorepo), but wouldn't scale if we split into microservices | Use BMAD Enterprise track for multi-repo scenarios |

---

### 5. Current Testing Status — Critical Gap ⚠️

```
┌─────────────────────────────────────────────────────────────────────┐
│                      TESTING STATUS: MyMusic                        │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   📁 app/src/test/       → ❌ Directory does NOT exist              │
│   📁 app/src/androidTest/ → ❌ Directory does NOT exist             │
│   📄 *Test.kt files       → ❌ ZERO test files found                │
│                                                                     │
│   Unit Test Coverage:        0%                                     │
│   Integration Test Coverage: 0%                                     │
│   UI Test Coverage:          0%                                     │
│                                                                     │
│   ⚠️  BMAD produced architecture-doc-level test strategy            │
│       (docs/fullstack-architecture.md §17) but NO actual            │
│       test implementation was generated.                            │
│                                                                     │
│   Documented Test Strategy (from Architecture §17):                 │
│   • Unit tests: mappers, use cases, repos with fakes               │
│   • Instrumented: ViewModels with coroutine testing                 │
│   • UI tests: Compose key flows (Trending → Play, Download)        │
│                                                                     │
│   → This is a known BMAD limitation: method produces excellent      │
│     test plans but relies on human discipline to execute them.      │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

**Recommendation**: Prioritize generating tests using AI for:
1. `TrackMapper` (DTO → Domain) — easiest, highest value
2. `GetTrendingTracksUseCase` — business logic validation
3. `TrendingViewModel` — UI state management

---

### 6. BMAD Best Practice Guide — For Your Next App

#### 6.1 Quick Start Checklist

```mermaid
flowchart TB
    Start["🚀 Start New Project"] --> Q1{"Project size?"}
    Q1 -->|"Bug fix / small feature"| Quick["⚡ Quick Flow<br/>Tech-spec only → Code"]
    Q1 -->|"MVP / new product"| Standard["📋 Standard BMAD<br/>PRD → Architecture → Stories → Code"]
    Q1 -->|"Enterprise / regulated"| Enterprise["🏢 Enterprise BMAD<br/>PRD → Arch → Security → DevOps → Stories"]

    Standard --> Step1["1. Run Analyst Agent<br/>→ project-brief.md"]
    Step1 --> Step2["2. Run PM Agent<br/>→ prd.md (FRs, NFRs)"]
    Step2 --> Gate1{"👤 Human Review<br/>Requirements OK?"}
    Gate1 -->|No| Step2
    Gate1 -->|Yes| Step3["3. Run Architect Agent<br/>→ architecture.md"]
    Step3 --> Gate2{"👤 Human Review<br/>Architecture OK?"}
    Gate2 -->|No| Step3
    Gate2 -->|Yes| Step4["4. Run Scrum Master Agent<br/>→ stories/"]
    Step4 --> Step5["5. Implement Story by Story<br/>(Developer + QA Agents)"]
    Step5 --> Step6["6. Review & Test<br/>Per Story Completion"]
    Step6 --> Done["✅ Ship"]

    style Start fill:#2ecc71,color:#fff
    style Quick fill:#f39c12,color:#fff
    style Standard fill:#3498db,color:#fff
    style Enterprise fill:#8e44ad,color:#fff
    style Gate1 fill:#e74c3c,color:#fff
    style Gate2 fill:#e74c3c,color:#fff
```

#### 6.2 Key Rules for Success

1. **Always review at phase gates** — Don't let AI auto-proceed from PRD → Architecture without human approval
2. **Keep artifacts in version control** — All docs in `docs/` folder, committed to Git
3. **Use stories as implementation units** — One story = one PR = one review cycle
4. **Enforce test creation per story** — Don't wait until the end (lesson from MyMusic!)
5. **Choose the right track** — Don't use full BMAD for a 10-line bug fix

#### 6.3 Recommended Folder Structure

```
your-project/
├── docs/                          # BMAD artifacts
│   ├── project-brief.md           # Phase 1 output
│   ├── brainstorming.md           # Phase 1 output
│   ├── prd.md                     # Phase 2 output
│   ├── front-end-spec.md          # Phase 2 output
│   ├── fullstack-architecture.md  # Phase 3 output
│   ├── diagrams/                  # Architecture diagrams
│   └── stories/                   # Phase 3 output (User stories)
│       ├── epic-1-story-1-xxx.md
│       ├── epic-1-story-2-xxx.md
│       └── ...
├── app/src/                       # Phase 4 output (Code)
│   ├── main/java/...
│   └── test/java/...              # ⚠️ Don't forget tests!
└── README.md                      # Updated with each phase
```

---

## Time Saved by Using AI

### Estimated Time Comparison

1) **Task: Requirements & PRD Creation**
   - Before (manual): 3 days (24 hours)
   - After (BMAD + AI): 3 hours
   - **Savings: ~87%**

2) **Task: Architecture Design & Documentation**
   - Before (manual): 2 days (16 hours)
   - After (BMAD + AI): 2 hours
   - **Savings: ~87%**

3) **Task: User Story Decomposition (22 stories)**
   - Before (manual): 2 days (16 hours)
   - After (BMAD + AI): 1.5 hours
   - **Savings: ~91%**

4) **Task: Code Generation (102 Kotlin files, Clean Architecture)**
   - Before (manual): 15 days (120 hours)
   - After (BMAD + AI): 3 days (24 hours)
   - **Savings: ~80%**

5) **Task: Architecture Diagrams & Visual Documentation**
   - Before (manual): 1 day (8 hours)
   - After (BMAD + AI): 30 minutes
   - **Savings: ~94%**

| Phase | Before (Manual) | After (BMAD + AI) | Time Saved |
|---|---|---|---|
| Requirements & PRD | 24 hours | 3 hours | 21 hours (87%) |
| Architecture Design | 16 hours | 2 hours | 14 hours (87%) |
| Story Decomposition | 16 hours | 1.5 hours | 14.5 hours (91%) |
| Code Generation | 120 hours | 24 hours | 96 hours (80%) |
| Diagrams & Docs | 8 hours | 0.5 hours | 7.5 hours (94%) |
| **TOTAL** | **184 hours (~23 days)** | **31 hours (~4 days)** | **153 hours (83%)** |

---

## Improvements or Benefits from AI Use

- **Architecture Quality**: Clean 4-layer separation enforced from Day 1 — no "spaghetti refactor" needed later
- **Documentation Completeness**: 80 docs generated covering PRD, architecture, UX spec, and 22 user stories — vs. typical projects with 0-3 docs
- **Consistency**: All 102 Kotlin files follow identical patterns (Hilt injection, StateFlow, Mapper pattern, Paging setup)
- **Onboarding Speed**: Any new developer can read `project-brief.md` → `prd.md` → `architecture.md` and start contributing within 30 minutes
- **Traceability**: Every line of code traces back to: Story → Epic → Architecture Section → FR in PRD
- **Decision Documentation**: "Why Kotlinx Serialization over Moshi?", "Why single-module?", "Why Media3 over ExoPlayer directly?" — all answered in architecture doc

---

## Limitations or Challenges

- **No test files generated**: Despite a well-documented test strategy in architecture §17, BMAD did not enforce actual test creation. 0% test coverage is a critical gap.
- **Token cost**: Generating 80 documentation files + 102 source files consumed significant API tokens (~$8-15 USD for the full project with Claude Sonnet)
- **Overhead for small changes**: Making a minor UI tweak requires checking alignment with PRD/Architecture, which can feel heavy for trivial changes
- **Cascading risk**: An incorrect assumption about Jamendo API endpoints in Phase 2 would propagate through all subsequent phases
- **Human discipline required**: BMAD provides the framework, but the developer must still review each artifact critically. Blind trust in AI output = blind spots in production code.

---

## Output / Deliverable Link

| Deliverable | Path |
|---|---|
| Project Brief | `docs/project-brief.md` |
| Brainstorming | `docs/brain-stoming-documentation.md` |
| PRD | `docs/prd.md` |
| Front-End Spec | `docs/front-end-spec.md` |
| Architecture | `docs/fullstack-architecture.md` |
| User Stories (22) | `docs/stories/*.md` |
| Architecture Diagram | `docs/diagrams/architecture-overview.mmd` |
| Source Code (102 files) | `app/src/main/java/com/example/mymusic/` |
| Built APK | `app-debug.apk` |

---

## Appendix

### A. Useful Prompts for BMAD Workflow

```
# Phase 1 — Analyst
"Brainstorm a [type] app with these features: [list]. 
 Research API options, identify risks, and create a project-brief.md."

# Phase 2 — Product Manager  
"Based on the project brief, create a PRD with functional requirements (FR1-FRn), 
 non-functional requirements (NFR1-NFRn), epic list, and UI/UX goals."

# Phase 3 — Architect
"Create a full-stack architecture document for [project] based on this PRD.
 Include: module structure, DI graph, data flow, API design, error handling, 
 testing strategy. Use Clean Architecture + MVVM."

# Phase 3 — Scrum Master
"Decompose Epic [n] into user stories with acceptance criteria. 
 Each story should be independently implementable and testable.
 Include technical notes referencing the architecture document."

# Phase 4 — Developer
"Implement Story [n.m] following the architecture document.
 Use [tech stack]. Include unit tests for new logic.
 Ensure code follows patterns established in existing codebase."
```

### B. Cursor Rules for BMAD (`.cursorrules` or `.cursor/rules`)

```markdown
# Project: MyMusic
# Method: BMAD (Breakthrough Method for Agile AI-Driven Development)

## Context Files (Always Read First)
- docs/project-brief.md — Project scope and goals
- docs/prd.md — Requirements (FR/NFR)  
- docs/fullstack-architecture.md — Architecture decisions
- docs/stories/ — Current stories and acceptance criteria

## Architecture Rules
- Follow MVVM + Clean Architecture (4 layers)
- All DI via Hilt (@Singleton, @ViewModelScoped)
- DTOs stay in data layer, domain models in domain layer
- Mappers convert DTO ↔ Domain at repository boundary
- UI state via StateFlow + collectAsStateWithLifecycle()
- Paging via Paging 3 PagingSource + cachedIn(viewModelScope)

## Code Patterns
- One UseCase per file, single `invoke()` operator
- Repository interface in domain/, implementation in data/
- ViewModels expose UiState sealed class
- Compose screens are stateless (state hoisted to ViewModel)

## Testing Requirements
- Every new UseCase must have a corresponding unit test
- Every new Mapper must have a corresponding unit test
- Every new ViewModel must have basic state transition tests
```

### C. BMAD Workflow Summary Diagram

```mermaid
graph TB
    subgraph Workflow["🔄 BMAD Complete Workflow"]
        direction TB
        
        I["💡 Idea / Feature Request"]
        
        I --> A["📋 Phase 1: Analysis<br/>Analyst Agent → project-brief.md"]
        A --> HG1["👤 Human Gate 1<br/>Scope Review"]
        
        HG1 --> P["📄 Phase 2: Planning<br/>PM Agent → prd.md + front-end-spec.md"]
        P --> HG2["👤 Human Gate 2<br/>Requirements Review"]
        
        HG2 --> S["🏛️ Phase 3: Solutioning<br/>Architect → architecture.md<br/>SM → stories/*.md"]
        S --> HG3["👤 Human Gate 3<br/>Architecture Review"]
        
        HG3 --> Impl["⚙️ Phase 4: Implementation<br/>Developer Agent → Code files<br/>QA Agent → Tests"]
        Impl --> HG4["👤 Human Gate 4<br/>Code Review per Story"]
        
        HG4 --> Ship["🚀 Ship"]
    end

    style I fill:#f1c40f,color:#000
    style A fill:#3498db,color:#fff
    style P fill:#9b59b6,color:#fff
    style S fill:#e67e22,color:#fff
    style Impl fill:#27ae60,color:#fff
    style Ship fill:#2ecc71,color:#fff
    style HG1 fill:#e74c3c,color:#fff
    style HG2 fill:#e74c3c,color:#fff
    style HG3 fill:#e74c3c,color:#fff
    style HG4 fill:#e74c3c,color:#fff
```

### D. Decision Matrix: When to Use BMAD

| Scenario | Recommended Track | Why |
|---|---|---|
| Quick bug fix (< 1 hour) | ❌ Don't use BMAD | Overhead not justified |
| Small feature (1-3 days) | ⚡ Quick Flow | Tech-spec + implement |
| New MVP app (1-4 weeks) | ✅ Standard BMAD | Full workflow, best ROI |
| Enterprise system | 🏢 Enterprise BMAD | Need security, DevOps, compliance docs |
| Exploratory prototype | ⚠️ Use with caution | "Plan first" may slow discovery; consider hybrid |
| Refactoring existing app | ✅ Standard BMAD | Architecture doc prevents regression |

---

*Report generated on 2026-04-15 using research from the MyMusic project repository and BMAD method documentation.*
