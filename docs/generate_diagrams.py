"""
Generate clean, corporate-style diagrams for the BMAD report.
Style: muted navy/grey palette, clean lines, no flashy effects.
"""
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyBboxPatch, FancyArrowPatch
import numpy as np
import os

OUT = r'd:\PROJECT\MyMusic\docs\diagrams'
os.makedirs(OUT, exist_ok=True)

# ── Corporate palette ─────────────────────────────────────────────────────────
NAVY   = '#2C3E50'
BLUE   = '#34495E'
LGREY  = '#ECF0F1'
MGREY  = '#BDC3C7'
DGREY  = '#7F8C8D'
WHITE  = '#FFFFFF'
ACCENT = '#2980B9'   # subtle blue accent
WARN   = '#C0392B'   # red for warnings only
GREEN  = '#27AE60'   # green for positive only
BG     = '#FAFAFA'

def save(fig, name):
    path = os.path.join(OUT, f'{name}.png')
    fig.savefig(path, dpi=200, bbox_inches='tight', facecolor=fig.get_facecolor(),
                pad_inches=0.3)
    plt.close(fig)
    print(f'  [OK] {name}.png')

def rounded_box(ax, x, y, w, h, text, facecolor=LGREY, edgecolor=NAVY,
                fontsize=8, textcolor=NAVY, lw=1.0, bold=False):
    box = FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.02",
                          facecolor=facecolor, edgecolor=edgecolor, lw=lw)
    ax.add_patch(box)
    weight = 'bold' if bold else 'normal'
    ax.text(x + w/2, y + h/2, text, ha='center', va='center',
            fontsize=fontsize, color=textcolor, weight=weight,
            fontfamily='sans-serif', wrap=True)

def arrow(ax, x1, y1, x2, y2, color=DGREY):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=1.2))


# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 1: BMAD Workflow (4 phases + gates)
# ══════════════════════════════════════════════════════════════════════════════
def diagram_workflow():
    fig, ax = plt.subplots(1, 1, figsize=(11, 3.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 11)
    ax.set_ylim(-0.3, 3.0)
    ax.axis('off')

    # Title
    ax.text(5.25, 2.75, 'BMAD Method  -  Development Workflow',
            ha='center', va='center', fontsize=13, color=NAVY,
            weight='bold', fontfamily='sans-serif')

    phases = [
        (0.0,  'Phase 1\nANALYSIS',     'project-brief.md'),
        (2.7,  'Phase 2\nPLANNING',      'prd.md\nfront-end-spec.md'),
        (5.4,  'Phase 3\nSOLUTIONING',   'architecture.md\nstories/*.md'),
        (8.1,  'Phase 4\nIMPLEMENTATION','source code\n+ tests'),
    ]

    fills = [LGREY, LGREY, LGREY, LGREY]

    for i, (x, label, output) in enumerate(phases):
        # Phase box
        rounded_box(ax, x, 1.0, 2.2, 1.2, label,
                    facecolor=fills[i], edgecolor=NAVY,
                    fontsize=9, bold=True)
        # Output label below
        ax.text(x + 1.1, 0.6, output, ha='center', va='top',
                fontsize=6.5, color=DGREY, fontfamily='sans-serif',
                style='italic')

    # Arrows between phases with gate diamonds
    for i in range(3):
        x1 = phases[i][0] + 2.2
        x2 = phases[i+1][0]
        mid = (x1 + x2) / 2
        # Arrow first half
        arrow(ax, x1, 1.6, mid - 0.12, 1.6)
        # Diamond (gate)
        diamond = plt.Polygon([[mid, 1.4], [mid+0.12, 1.6],
                                [mid, 1.8], [mid-0.12, 1.6]],
                               facecolor=WHITE, edgecolor=NAVY, lw=1.0)
        ax.add_patch(diamond)
        ax.text(mid, 1.6, 'H', ha='center', va='center',
                fontsize=5.5, color=NAVY, weight='bold')
        # Arrow second half
        arrow(ax, mid + 0.12, 1.6, x2, 1.6)

    # Final arrow to SHIP
    arrow(ax, 10.3, 1.6, 10.7, 1.6)
    ax.text(10.85, 1.6, 'SHIP', ha='center', va='center',
            fontsize=9, color=NAVY, weight='bold')

    # Legend
    diamond_legend = plt.Polygon([[0.0, 0.05], [0.1, 0.15],
                                   [0.0, 0.25], [-0.1, 0.15]],
                                  facecolor=WHITE, edgecolor=NAVY, lw=1.0)
    ax.add_patch(diamond_legend)
    ax.text(0.25, 0.15, '= Human Review Gate', ha='left', va='center',
            fontsize=6.5, color=DGREY, fontfamily='sans-serif')

    save(fig, 'workflow')

# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 2: Agent Team
# ══════════════════════════════════════════════════════════════════════════════
def diagram_agents():
    fig, ax = plt.subplots(1, 1, figsize=(10, 4.5), facecolor=WHITE)
    ax.set_xlim(-0.3, 10.3)
    ax.set_ylim(-0.5, 4.2)
    ax.axis('off')

    ax.text(5.0, 3.9, 'BMAD Agent Team  -  Roles & Outputs',
            ha='center', va='center', fontsize=13, color=NAVY,
            weight='bold', fontfamily='sans-serif')

    agents = [
        # row 1
        (0.2, 2.2, 'Analyst',         'Brainstorming\nResearch & Scoping',  'project-brief.md'),
        (3.5, 2.2, 'Product Manager', 'Requirements\nPRD Creation',         'prd.md'),
        (6.8, 2.2, 'Architect',       'System Design\nTech Decisions',       'architecture.md'),
        # row 2
        (0.2, 0.2, 'Scrum Master',    'Epic Decomposition\nUser Stories',    'stories/*.md'),
        (3.5, 0.2, 'Developer',       'Code Implementation\nPer Story',      'Source Code'),
        (6.8, 0.2, 'QA',              'Testing\nCode Review',                'Test Files'),
    ]

    for x, y, name, desc, output in agents:
        # Header bar
        header = FancyBboxPatch((x, y+1.1), 3.0, 0.4, boxstyle="round,pad=0.02",
                                 facecolor=NAVY, edgecolor=NAVY, lw=1)
        ax.add_patch(header)
        ax.text(x+1.5, y+1.3, name, ha='center', va='center',
                fontsize=8.5, color=WHITE, weight='bold', fontfamily='sans-serif')
        # Body
        body = FancyBboxPatch((x, y), 3.0, 1.1, boxstyle="round,pad=0.02",
                               facecolor=LGREY, edgecolor=MGREY, lw=0.8)
        ax.add_patch(body)
        ax.text(x+1.5, y+0.7, desc, ha='center', va='center',
                fontsize=7, color=BLUE, fontfamily='sans-serif')
        ax.text(x+1.5, y+0.15, output, ha='center', va='center',
                fontsize=6.5, color=DGREY, fontfamily='sans-serif', style='italic')

    # Flow arrows row 1
    for x in [3.2, 6.5]:
        arrow(ax, x, 2.9, x+0.3, 2.9)
    # Flow arrows row 1 -> row 2
    arrow(ax, 8.3, 2.2, 8.3, 1.9)
    ax.annotate('', xy=(0.2+1.5, 1.7), xytext=(8.3, 1.9),
                arrowprops=dict(arrowstyle='->', color=DGREY, lw=1.0,
                                connectionstyle='arc3,rad=0'))
    # Flow arrows row 2
    for x in [3.2, 6.5]:
        arrow(ax, x, 0.9, x+0.3, 0.9)

    save(fig, 'agents')

# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 3: Architecture (C4 Container)
# ══════════════════════════════════════════════════════════════════════════════
def diagram_architecture():
    fig, ax = plt.subplots(1, 1, figsize=(9, 7), facecolor=WHITE)
    ax.set_xlim(-0.5, 9.5)
    ax.set_ylim(-1.5, 7.5)
    ax.axis('off')

    ax.text(4.5, 7.2, 'MyMusic App  -  Architecture (C4 Container)',
            ha='center', va='center', fontsize=13, color=NAVY,
            weight='bold', fontfamily='sans-serif')

    # Outer boundary
    outer = FancyBboxPatch((0, 0.5), 9, 6.2, boxstyle="round,pad=0.05",
                            facecolor=WHITE, edgecolor=NAVY, lw=1.5, linestyle='--')
    ax.add_patch(outer)
    ax.text(4.5, 6.5, 'MyMusic Android App', ha='center', va='center',
            fontsize=10, color=NAVY, weight='bold', fontfamily='sans-serif')

    layers = [
        (0.3, 5.0, 8.4, 1.2, 'Presentation Layer',
         'Jetpack Compose (8+ Screens)    |    ViewModels (Hilt)    |    Navigation'),
        (0.3, 3.5, 8.4, 1.2, 'Domain Layer  (Pure Kotlin)',
         '12 Use Cases    |    5 Domain Models    |    3 Repository Interfaces'),
        (0.3, 1.8, 5.0, 1.4, 'Data Layer',
         '7 Repository Impl    |    6 Retrofit Services\nDTO Mappers    |    Paging 3 Sources'),
        (5.6, 1.8, 3.1, 1.4, 'Media Layer',
         'Media3 ExoPlayer\nPlaybackController\nMediaSessionService'),
    ]

    fills = ['#E8EDF2', '#E8EDF2', '#E8EDF2', '#E8EDF2']

    for i, (x, y, w, h, title, content) in enumerate(layers):
        box = FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.03",
                              facecolor=fills[i], edgecolor=NAVY, lw=1.0)
        ax.add_patch(box)
        ax.text(x + w/2, y + h - 0.2, title, ha='center', va='center',
                fontsize=8.5, color=NAVY, weight='bold', fontfamily='sans-serif')
        ax.text(x + w/2, y + h/2 - 0.15, content, ha='center', va='center',
                fontsize=6.5, color=BLUE, fontfamily='sans-serif')

    # Arrows between layers
    arrow(ax, 4.5, 5.0, 4.5, 4.75)
    arrow(ax, 4.5, 3.5, 4.5, 3.25)
    arrow(ax, 2.8, 1.8, 2.8, 1.5)

    # External API
    api_box = FancyBboxPatch((2.0, -1.2), 5, 1.0, boxstyle="round,pad=0.03",
                              facecolor='#F5F5F5', edgecolor=DGREY, lw=1.0, linestyle='--')
    ax.add_patch(api_box)
    ax.text(4.5, -0.7, 'Jamendo API\napi.jamendo.com/v3.0  (REST / JSON)',
            ha='center', va='center', fontsize=7.5, color=DGREY,
            fontfamily='sans-serif')
    arrow(ax, 2.8, 1.5, 4.5, -0.15)

    # Dependency labels
    ax.text(5.0, 4.85, 'depends on', ha='center', va='center',
            fontsize=5.5, color=DGREY, style='italic')
    ax.text(5.0, 3.35, 'implements', ha='center', va='center',
            fontsize=5.5, color=DGREY, style='italic')
    ax.text(3.3, 1.4, 'HTTP', ha='center', va='center',
            fontsize=5.5, color=DGREY, style='italic')

    save(fig, 'architecture')

# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 4: Vibe Coding vs BMAD Comparison
# ══════════════════════════════════════════════════════════════════════════════
def diagram_comparison():
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(10, 4.5), facecolor=WHITE)

    for ax in (ax1, ax2):
        ax.set_xlim(0, 5)
        ax.set_ylim(0, 5)
        ax.axis('off')

    fig.suptitle('Development Approach Comparison', fontsize=13,
                 color=NAVY, weight='bold', fontfamily='sans-serif', y=0.97)

    # LEFT: Vibe Coding
    bg1 = FancyBboxPatch((0.1, 0.1), 4.8, 4.5, boxstyle="round,pad=0.05",
                          facecolor='#FDF2F2', edgecolor='#D5D5D5', lw=1)
    ax1.add_patch(bg1)
    ax1.text(2.5, 4.2, 'Ad-hoc AI Prompting', ha='center', fontsize=11,
             color='#666666', weight='bold', fontfamily='sans-serif')
    ax1.plot([0.5, 4.5], [3.95, 3.95], color='#D5D5D5', lw=0.8)

    issues = [
        'No requirements documentation',
        'Context lost between sessions',
        'No architecture design phase',
        'Inconsistent code quality',
        '"Black box" codebase',
        'Difficult team onboarding',
    ]
    for i, txt in enumerate(issues):
        y = 3.5 - i * 0.55
        ax1.text(0.6, y, '\u2013', ha='center', va='center',
                 fontsize=10, color='#999999')
        ax1.text(1.0, y, txt, ha='left', va='center',
                 fontsize=7.5, color='#666666', fontfamily='sans-serif')

    # RIGHT: BMAD
    bg2 = FancyBboxPatch((0.1, 0.1), 4.8, 4.5, boxstyle="round,pad=0.05",
                          facecolor='#F2F8F2', edgecolor='#D5D5D5', lw=1)
    ax2.add_patch(bg2)
    ax2.text(2.5, 4.2, 'BMAD Method', ha='center', fontsize=11,
             color=NAVY, weight='bold', fontfamily='sans-serif')
    ax2.plot([0.5, 4.5], [3.95, 3.95], color='#D5D5D5', lw=0.8)

    benefits = [
        'Full documentation (PRD, Architecture)',
        'Persistent context via versioned docs',
        'Architecture-first design phase',
        'Consistent, auditable code output',
        'Traceable decisions & artifacts',
        'Easy onboarding (read 3 docs)',
    ]
    for i, txt in enumerate(benefits):
        y = 3.5 - i * 0.55
        ax2.text(0.6, y, '+', ha='center', va='center',
                 fontsize=10, color=NAVY, weight='bold')
        ax2.text(1.0, y, txt, ha='left', va='center',
                 fontsize=7.5, color=BLUE, fontfamily='sans-serif')

    fig.subplots_adjust(wspace=0.08)
    save(fig, 'comparison')

# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 5: Data Flow (simplified sequence)
# ══════════════════════════════════════════════════════════════════════════════
def diagram_dataflow():
    fig, ax = plt.subplots(1, 1, figsize=(10, 6), facecolor=WHITE)
    ax.set_xlim(-0.5, 10.5)
    ax.set_ylim(-0.5, 7)
    ax.axis('off')

    ax.text(5.0, 6.7, 'MyMusic  -  Data Flow (Request Lifecycle)',
            ha='center', va='center', fontsize=13, color=NAVY,
            weight='bold', fontfamily='sans-serif')

    # Entities
    entities = [
        (0.5, 'User'),
        (2.3, 'Compose\nScreen'),
        (4.1, 'View\nModel'),
        (5.9, 'Use\nCase'),
        (7.7, 'Repo'),
        (9.3, 'Jamendo\nAPI'),
    ]

    for x, name in entities:
        rounded_box(ax, x-0.45, 5.7, 1.2, 0.7, name,
                    facecolor=LGREY, edgecolor=NAVY,
                    fontsize=7, bold=True, textcolor=NAVY)
        # Lifeline
        ax.plot([x+0.15, x+0.15], [5.7, 0.2], color=MGREY, lw=0.6, linestyle=':')

    # Messages (y positions descending)
    messages = [
        (0.65, 2.45, 5.0, 'Opens Trending tab'),
        (2.45, 4.25, 4.4, 'collectAsState()'),
        (4.25, 6.05, 3.8, 'GetTrendingTracks()'),
        (6.05, 7.85, 3.2, 'getTrendingTracks()'),
        (7.85, 9.45, 2.6, 'GET /tracks'),
        (9.45, 7.85, 2.0, 'JSON Response'),
        (7.85, 4.25, 1.5, 'Flow<PagingData<Track>>'),
        (4.25, 2.45, 1.0, 'UiState (track list)'),
        (2.45, 0.65, 0.5, 'Display track cards'),
    ]

    for x1, x2, y, label in messages:
        ax.annotate('', xy=(x2, y), xytext=(x1, y),
                    arrowprops=dict(arrowstyle='->', color=NAVY, lw=0.9))
        midx = (x1 + x2) / 2
        ax.text(midx, y + 0.15, label, ha='center', va='bottom',
                fontsize=6, color=BLUE, fontfamily='sans-serif')

    save(fig, 'dataflow')

# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 6: Traceability Map
# ══════════════════════════════════════════════════════════════════════════════
def diagram_traceability():
    fig, ax = plt.subplots(1, 1, figsize=(11, 4.5), facecolor=WHITE)
    ax.set_xlim(-0.3, 11.3)
    ax.set_ylim(-0.3, 4.5)
    ax.axis('off')

    ax.text(5.5, 4.2, 'BMAD Traceability  -  From Requirement to Code',
            ha='center', va='center', fontsize=13, color=NAVY,
            weight='bold', fontfamily='sans-serif')

    # Column headers
    cols = [
        (0.0, 'PRD Requirements'),
        (3.0, 'Architecture'),
        (6.0, 'User Stories'),
        (8.8, 'Source Code'),
    ]
    for x, title in cols:
        ax.text(x + 1.1, 3.7, title, ha='center', va='center',
                fontsize=8.5, color=NAVY, weight='bold', fontfamily='sans-serif')
        ax.plot([x, x+2.2], [3.5, 3.5], color=NAVY, lw=1)

    # Rows
    rows = [
        ('FR1: Trending\nTracks',       'S7: Data Layer',       'Epic 1 / Story 1.2',   'TrendingScreen.kt\nTrendingViewModel.kt'),
        ('FR9: Tap to Play',            'S8: Playback\nArchitecture', 'Epic 1 / Story 1.3', 'PlayerRoute.kt\nPlaybackController.kt'),
        ('FR11: Background\nPlayback',  'S8: Media3\nService',  'Epic 1 / Story 1.5',   'PlaybackService.kt\nMediaSessionService.kt'),
        ('FR14: Download\nTrack',       'S9: Download\nArchitecture', 'Epic 5',          'DownloadsScreen.kt\nDownloadRepoImpl.kt'),
    ]

    for i, (r1, r2, r3, r4) in enumerate(rows):
        y = 2.8 - i * 0.85
        items = [(0.0, r1), (3.0, r2), (6.0, r3), (8.8, r4)]
        for x, txt in items:
            rounded_box(ax, x, y - 0.3, 2.2, 0.6, txt,
                        facecolor=LGREY, edgecolor=MGREY,
                        fontsize=5.8, textcolor=BLUE, lw=0.6)
        # Arrows
        for j in range(3):
            x1 = items[j][0] + 2.2
            x2 = items[j+1][0]
            arrow(ax, x1, y, x2, y, color=MGREY)

    save(fig, 'traceability')

# ══════════════════════════════════════════════════════════════════════════════
#  DIAGRAM 7: Strengths vs Weaknesses
# ══════════════════════════════════════════════════════════════════════════════
def diagram_tradeoffs():
    fig, ax = plt.subplots(1, 1, figsize=(10, 5.5), facecolor=WHITE)
    ax.set_xlim(-0.3, 10.3)
    ax.set_ylim(-0.5, 5.5)
    ax.axis('off')

    ax.text(5.0, 5.2, 'BMAD Method  -  Trade-off Analysis',
            ha='center', va='center', fontsize=13, color=NAVY,
            weight='bold', fontfamily='sans-serif')

    # Left: Strengths
    left_bg = FancyBboxPatch((0, 0), 4.8, 4.6, boxstyle="round,pad=0.04",
                              facecolor='#F5F9F5', edgecolor='#C8D6C8', lw=1)
    ax.add_patch(left_bg)
    ax.text(2.4, 4.25, 'Strengths', ha='center', fontsize=11,
            color=NAVY, weight='bold', fontfamily='sans-serif')
    ax.plot([0.3, 4.5], [4.0, 4.0], color='#C8D6C8', lw=0.8)

    strengths = [
        ('Structure', 'Reduces context loss, auditability'),
        ('Architecture-first', 'Prevents churn and inconsistencies'),
        ('Agent specialization', 'Mimics professional team roles'),
        ('Governance', 'Consistent, production-ready output'),
        ('Traceability', 'Requirement to code mapping'),
    ]
    for i, (title, desc) in enumerate(strengths):
        y = 3.5 - i * 0.7
        ax.text(0.4, y, '+', ha='center', va='center',
                fontsize=9, color=NAVY, weight='bold')
        ax.text(0.7, y, title, ha='left', va='center',
                fontsize=7.5, color=NAVY, weight='bold', fontfamily='sans-serif')
        ax.text(0.7, y - 0.25, desc, ha='left', va='center',
                fontsize=6, color=DGREY, fontfamily='sans-serif')

    # Right: Weaknesses
    right_bg = FancyBboxPatch((5.2, 0), 4.8, 4.6, boxstyle="round,pad=0.04",
                               facecolor='#FDF5F5', edgecolor='#D6C8C8', lw=1)
    ax.add_patch(right_bg)
    ax.text(7.6, 4.25, 'Weaknesses', ha='center', fontsize=11,
            color=NAVY, weight='bold', fontfamily='sans-serif')
    ax.plot([5.5, 9.7], [4.0, 4.0], color='#D6C8C8', lw=0.8)

    weaknesses = [
        ('Overhead', 'Heavy ceremony for small tasks'),
        ('Token cost', 'Multi-agent multiplies API cost'),
        ('Cascading errors', 'Mistake propagates through phases'),
        ('Rigidity', '"Plan-first" slows exploration'),
        ('No test enforcement', '0% coverage despite strategy doc'),
    ]
    for i, (title, desc) in enumerate(weaknesses):
        y = 3.5 - i * 0.7
        ax.text(5.6, y, '\u2013', ha='center', va='center',
                fontsize=9, color='#888888')
        ax.text(5.9, y, title, ha='left', va='center',
                fontsize=7.5, color=BLUE, weight='bold', fontfamily='sans-serif')
        ax.text(5.9, y - 0.25, desc, ha='left', va='center',
                fontsize=6, color=DGREY, fontfamily='sans-serif')

    # Bottom summary
    summary_bg = FancyBboxPatch((0.5, -0.4), 9.0, 0.5, boxstyle="round,pad=0.02",
                                 facecolor=LGREY, edgecolor=MGREY, lw=0.6)
    ax.add_patch(summary_bg)
    ax.text(5.0, -0.15, 'Best for: MVP+, production apps, team projects    |    '
            'Avoid for: quick bug fixes, simple prototypes',
            ha='center', va='center', fontsize=6.5, color=DGREY,
            fontfamily='sans-serif')

    save(fig, 'tradeoffs')


# ══════════════════════════════════════════════════════════════════════════════
#  RUN ALL
# ══════════════════════════════════════════════════════════════════════════════
print('Generating corporate-style diagrams...')
diagram_workflow()
diagram_agents()
diagram_architecture()
diagram_comparison()
diagram_dataflow()
diagram_traceability()
diagram_tradeoffs()
print(f'All diagrams saved to: {OUT}')
