"""
Practice 1: Multi-Agent Ideation Pipeline — Diagram Generation
Clean diagrams focused on Traditional vs This Method, no version history
"""
import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
from matplotlib.patches import FancyBboxPatch
import os

OUT = os.path.dirname(os.path.abspath(__file__))

NAVY  = '#2C3E50'
BLUE  = '#34495E'
LGREY = '#ECF0F1'
DGREY = '#7F8C8D'
WHITE = '#FFFFFF'

def save(fig, name):
    p = os.path.join(OUT, f'{name}.png')
    fig.savefig(p, dpi=200, bbox_inches='tight', facecolor=WHITE, pad_inches=0.3)
    plt.close(fig)
    print(f'  [OK] {name}.png')

class Box:
    def __init__(self, ax, x, y, w, h, text, fc=LGREY, ec=NAVY, fs=8, tc=NAVY, lw=1.0, bold=False):
        self.x, self.y, self.w, self.h = x, y, w, h
        p = FancyBboxPatch((x,y), w, h, boxstyle="round,pad=0.02", facecolor=fc, edgecolor=ec, lw=lw)
        ax.add_patch(p)
        ax.text(x+w/2, y+h/2, text, ha='center', va='center', fontsize=fs, color=tc,
                weight='bold' if bold else 'normal', fontfamily='sans-serif', linespacing=1.3)
    @property
    def cx(self): return self.x + self.w/2
    @property
    def cy(self): return self.y + self.h/2
    @property
    def top(self): return (self.cx, self.y+self.h)
    @property
    def bot(self): return (self.cx, self.y)
    @property
    def left(self): return (self.x, self.cy)
    @property
    def right(self): return (self.x+self.w, self.cy)

def arr(ax, p1, p2, c=DGREY, lw=1.2):
    ax.annotate('', xy=p2, xytext=p1, arrowprops=dict(arrowstyle='->', color=c, lw=lw))

# ══════════════════════════════════════════════════════════════
# 1. TRADITIONAL vs THIS METHOD — Side by Side
# ══════════════════════════════════════════════════════════════
def diagram_comparison():
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(14, 8), facecolor=WHITE)
    for ax in (ax1, ax2):
        ax.set_xlim(0, 7); ax.set_ylim(-0.5, 9); ax.axis('off')
    fig.suptitle('Traditional AI Brainstorming vs Multi-Agent Ideation', fontsize=14,
                 color=NAVY, weight='bold', fontfamily='sans-serif', y=0.97)

    # LEFT — Traditional
    box = FancyBboxPatch((0.2, 0.0), 6.6, 8.5, boxstyle="round,pad=0.04",
                          facecolor='#FDF2F2', edgecolor='#FFCDD2', lw=1.5)
    ax1.add_patch(box)
    ax1.text(3.5, 8.1, 'X  Traditional: 1 Chat, 1 AI', ha='center', fontsize=10, color='#C62828', weight='bold')
    ax1.plot([0.5, 6.5], [7.7, 7.7], color='#FFCDD2', lw=0.8)
    trad = [
        ('You type:', '"Hey AI, I want to make a music app"', '#C62828'),
        ('AI response:', 'Wall of generic advice (sounds good!)', '#666'),
        ('Competitors?', 'Names them but no URLs, maybe wrong', '#C62828'),
        ('Market size?', '"About $30 billion" — no source, unverifiable', '#C62828'),
        ('Challenges you?', 'Never. Agrees with everything you say.', '#C62828'),
        ('APIs checked?', 'Mentions APIs but never checks pricing', '#C62828'),
        ('Legal research?', 'None. Doesn\'t search regulations.', '#C62828'),
        ('Confidence level?', 'Sounds 100% confident on everything', '#C62828'),
        ('Can you verify?', 'No URLs. No way to fact-check.', '#C62828'),
        ('', '', '#fff'),
        ('Output:', 'A chat transcript. That\'s it.', '#C62828'),
        ('Evidence:', 'ZERO verifiable sources.', '#C62828'),
        ('Time:', '15 min.  Value: LOW.', '#C62828'),
    ]
    for i, (label, text, c) in enumerate(trad):
        if label:
            ax1.text(0.5, 7.3 - i*0.55, label, fontsize=6, color='#999', va='center', weight='bold')
            ax1.text(2.3, 7.3 - i*0.55, text, fontsize=6, color=c, va='center')

    # RIGHT — This Method
    box2 = FancyBboxPatch((0.2, 0.0), 6.6, 8.5, boxstyle="round,pad=0.04",
                           facecolor='#F2F8F2', edgecolor='#C8E6C9', lw=1.5)
    ax2.add_patch(box2)
    ax2.text(3.5, 8.1, 'V  This Method: 8 Agents, Evidence-Based', ha='center', fontsize=10, color='#2E7D32', weight='bold')
    ax2.plot([0.5, 6.5], [7.7, 7.7], color='#C8E6C9', lw=0.8)
    ours = [
        ('You type:', '1 sentence. Then answer 10-15 Q\'s.', '#2E7D32'),
        ('AI response:', '8 specialists work for you in parallel', '#2E7D32'),
        ('Competitors?', '5+ real apps with URLs, pricing, reviews', '#2E7D32'),
        ('Market size?', 'TAM $28.6B (Grand View Research) — URL', '#2E7D32'),
        ('Challenges you?', 'Yes. Agent 5 is a dedicated devil\'s advocate.', '#2E7D32'),
        ('APIs checked?', 'Real pricing pages + rate limits verified', '#2E7D32'),
        ('Legal research?', 'Searches GDPR, CCPA, licensing laws', '#2E7D32'),
        ('Confidence level?', '90% / 60% / Low / Unvalidated per claim', '#2E7D32'),
        ('Can you verify?', 'Every URL is clickable. Full audit trail.', '#2E7D32'),
        ('', '', '#fff'),
        ('Output:', '13-section Idea Spec + evidence trail.', '#2E7D32'),
        ('Evidence:', '30+ verifiable source URLs.', '#2E7D32'),
        ('Time:', '20 min.  Value: HIGH.', '#2E7D32'),
    ]
    for i, (label, text, c) in enumerate(ours):
        if label:
            ax2.text(0.5, 7.3 - i*0.55, label, fontsize=6, color='#999', va='center', weight='bold')
            ax2.text(2.3, 7.3 - i*0.55, text, fontsize=6, color=c, va='center')

    fig.subplots_adjust(wspace=0.08)
    save(fig, 'comparison')

# ══════════════════════════════════════════════════════════════
# 2. THE PIPELINE — How It Works
# ══════════════════════════════════════════════════════════════
def diagram_pipeline():
    fig, ax = plt.subplots(figsize=(14, 10), facecolor=WHITE)
    ax.set_xlim(-0.5, 14); ax.set_ylim(-2, 10); ax.axis('off')
    ax.text(7, 9.5, 'How The Pipeline Works', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')
    ax.text(7, 9.0, 'You type 1 prompt → 8 agents research for you → you receive evidence-backed Idea Spec',
            ha='center', fontsize=9, color=DGREY, fontfamily='sans-serif')

    # Internet
    b_web = Box(ax, 4.0, 8.0, 6.0, 0.6,
        'INTERNET — agents search the web for real data', fs=7, fc='#E1F5FE', ec='#0288D1', bold=True)

    # User + Orchestrator
    b_user = Box(ax, 0.2, 6.2, 2.3, 1.2,
        'YOU\n\n"I want to make\na music app..."', fs=7, fc='#FFF8E1', bold=True)
    b_orch = Box(ax, 3.3, 6.0, 3.0, 1.5,
        'ORCHESTRATOR\n\nManages all agents:\n• delegates tasks\n• checks quality\n• passes data', fs=6, fc='#D1C4E9', ec='#5E35B1', bold=True)
    arr(ax, b_user.right, b_orch.left, c=NAVY, lw=2)

    # Phase 1
    ax.text(9, 7.2, 'Phase 1: Understand', fontsize=7, color='#1565C0', weight='bold')
    b_req = Box(ax, 8, 6.0, 3.0, 1.5,
        'Agent 1:\nRequirements Analyst\n\nAsks you 10-15 questions\nThen searches web to verify\nyour answers', fs=5.5, fc='#E3F2FD', bold=True)
    arr(ax, b_orch.right, b_req.left, c='#1565C0', lw=1.5)
    arr(ax, (b_req.cx, b_req.y+b_req.h), (b_web.cx+1, b_web.y), c='#0288D1', lw=0.8)

    # Phase 2 — Parallel
    ax.text(0.5, 5.2, 'Phase 2: Research (3 agents run AT THE SAME TIME)', fontsize=7, color='#E65100', weight='bold')
    p2 = [
        (0.3, 'Agent 2: Market Researcher\n\nSearches for:\n• Market size + growth\n• User demographics\n• Pricing benchmarks', '#FFF3E0'),
        (4.0, 'Agent 3: Tech Scout\n\nSearches for:\n• APIs + real pricing\n• Rate limits + quotas\n• Framework comparison', '#F3E5F5'),
        (7.7, 'Agent 4: Competitor Analyst\n\nSearches for:\n• 5+ real competitors\n• App Store reviews\n• User complaints on Reddit', '#FBE9E7'),
    ]
    p2b = []
    for x, text, fc in p2:
        b = Box(ax, x, 3.3, 3.2, 1.6, text, fs=5.5, fc=fc, bold=True)
        p2b.append(b)
    for b in p2b:
        arr(ax, (b.cx, b.y+b.h), (b_web.cx, b_web.y), c='#0288D1', lw=0.5)

    # Quality Gate
    b_qg = Box(ax, 11.3, 4.0, 2.5, 0.5,
        'QUALITY CHECK\n✓ 5+ URLs, all sections filled', fs=5, fc='#FFCDD2', ec='#C62828', bold=True)

    # Phase 3 — Parallel
    ax.text(0.5, 2.6, 'Phase 3: Evaluate (3 agents run AT THE SAME TIME)', fontsize=7, color='#AD1457', weight='bold')
    p3 = [
        (0.3, 'Agent 5: Idea Evaluator\n\nScores idea on 8 dimensions\nEach score needs evidence\nGO / PIVOT / KILL verdict', '#FCE4EC'),
        (4.0, 'Agent 6: UX Strategist\n\nDesigns user journeys\nOnboarding + retention\nBased on UX research', '#E0F7FA'),
        (7.7, 'Agent 7: Risk Assessor\n\n"How to GUARANTEE fail?"\nSearches legal requirements\nStress-tests at 1000x scale', '#FFF9C4'),
    ]
    p3b = []
    for x, text, fc in p3:
        b = Box(ax, x, 0.8, 3.2, 1.6, text, fs=5.5, fc=fc, bold=True)
        p3b.append(b)
    for b2 in p2b:
        for b3 in p3b:
            arr(ax, (b2.cx, b2.y), (b3.cx, b3.y+b3.h), c='#78909C', lw=0.3)

    # Phase 4
    ax.text(11.8, 1.9, 'Phase 4: Final', fontsize=7, color='#2E7D32', weight='bold')
    b_crystal = Box(ax, 11.3, 0.3, 2.5, 1.4,
        'Agent 8: Crystallizer\n\nCompiles all 7 reports\ninto 1 document\n+ confidence score', fs=5.5, fc='#C8E6C9', ec='#2E7D32', bold=True)
    for b3 in p3b:
        arr(ax, b3.right, b_crystal.left, c='#2E7D32', lw=0.8)

    b_final = Box(ax, 11.3, -1.2, 2.5, 1.0,
        'YOUR OUTPUT\n13-section Idea Spec\nwith evidence trail', fs=5.5, fc='#A5D6A7', ec='#2E7D32', bold=True, lw=2)
    arr(ax, b_crystal.bot, b_final.top, c='#2E7D32', lw=2)

    ax.text(0.2, -0.3, 'You type 1 sentence + answer 10-15 questions. Everything else is automatic.', fontsize=7, color=NAVY, weight='bold')
    ax.text(0.2, -0.7, 'Every agent searches the internet. Every claim has a source URL. Nothing is made up.', fontsize=7, color='#E65100')
    ax.text(0.2, -1.1, 'Quality check between phases — garbage doesn\'t get passed forward.', fontsize=7, color='#C62828')

    save(fig, 'pipeline')

# ══════════════════════════════════════════════════════════════
# 3. WHAT EACH AGENT DOES — Detailed
# ══════════════════════════════════════════════════════════════
def diagram_agents():
    fig, ax = plt.subplots(figsize=(14, 10), facecolor=WHITE)
    ax.set_xlim(-0.3, 14.3); ax.set_ylim(-0.5, 10); ax.axis('off')
    ax.text(7, 9.6, 'The 8 Specialist Agents — What Each One Does', ha='center',
            fontsize=13, color=NAVY, weight='bold', fontfamily='sans-serif')

    agents = [
        (0.0, 6.0, '[1] Requirements\nAnalyst', 'Asks 10-15 questions\nthen challenges your\nassumptions:\n"What if the opposite\nis true?"', '#E3F2FD'),
        (3.6, 6.0, '[2] Market\nResearcher', 'Finds real numbers:\nTAM/SAM/SOM + trends\nThen tests at extremes:\n"What breaks at\n1M users?"', '#FFF3E0'),
        (7.2, 6.0, '[3] Tech\nScout', 'Checks API pricing,\nrate limits, costs.\nFinds patterns:\n"All APIs throttle\nat scale"', '#F3E5F5'),
        (10.8, 6.0, '[4] Competitor\nAnalyst', 'Finds 5+ real apps\nwith URLs + reviews.\nCreates creative\nalternatives\nvia SCAMPER', '#FBE9E7'),
        (0.0, 2.5, '[5] Idea\nEvaluator', 'Scores on 8 criteria.\nEach score needs\nevidence.\nDevil\'s advocate.\nGO/PIVOT/KILL', '#FCE4EC'),
        (3.6, 2.5, '[6] UX\nStrategist', 'Designs onboarding,\ndaily loop, hooks.\nBased on NNGroup/\nBaymard research,\nnot guessing', '#E0F7FA'),
        (7.2, 2.5, '[7] Risk\nAssessor', '"How to GUARANTEE\nfailure?"\nSearches legal reqs.\nStress-tests risks\nat 1000x scale', '#FFF9C4'),
        (10.8, 2.5, '[8] Idea\nCrystallizer', 'Compiles ALL 7\nreports into 1 doc.\nTraces every claim\nto source URL.\nNo new info added', '#C8E6C9'),
    ]

    for x, y, name, job, fc in agents:
        Box(ax, x, y+1.5, 3.3, 0.8, name, fs=7, fc=fc, bold=True)
        Box(ax, x, y+0.0, 3.3, 1.4, job, fs=5.5, fc=WHITE)

    ax.text(5.5, 5.7, '← run at the SAME TIME →', fontsize=8, color='#E65100', weight='bold', ha='center')
    ax.text(5.5, 2.2, '← run at the SAME TIME →', fontsize=8, color='#AD1457', weight='bold', ha='center')
    ax.text(0.5, 5.7, '(you interact)', fontsize=6, color='#1565C0')
    ax.text(11.0, 2.2, '(final step)', fontsize=6, color='#2E7D32')

    ax.text(7, -0.1, 'Each agent has its own context — fresh perspective, no bias from previous agents.',
            ha='center', fontsize=7, color=DGREY, style='italic')
    save(fig, 'agents_detail')

# ══════════════════════════════════════════════════════════════
# 4. EVIDENCE TRAIL — How Claims Are Verified
# ══════════════════════════════════════════════════════════════
def diagram_evidence():
    fig, ax = plt.subplots(figsize=(11, 6), facecolor=WHITE)
    ax.set_xlim(-0.5, 11.5); ax.set_ylim(-0.5, 6); ax.axis('off')
    ax.text(5.5, 5.5, 'How Every Claim Gets Verified', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    # Row 1: Traditional
    ax.text(0.5, 4.7, 'Traditional AI:', fontsize=8, color='#C62828', weight='bold')
    b1a = Box(ax, 0.5, 3.7, 3.0, 0.8,
        'AI says:\n"Market is $30 billion"', fs=7, fc='#FFCDD2', bold=True)
    b1b = Box(ax, 4.3, 3.7, 3.0, 0.8,
        'Source?\nNone. Training data.\nCould be from 2021.', fs=7, fc='#FFCDD2')
    b1c = Box(ax, 8.0, 3.7, 3.0, 0.8,
        'Can you verify?\n❌ NO\nJust trust it (don\'t)', fs=7, fc='#FFCDD2', ec='#C62828')
    arr(ax, b1a.right, b1b.left, c='#C62828')
    arr(ax, b1b.right, b1c.left, c='#C62828')

    # Row 2: This Method
    ax.text(0.5, 2.7, 'This Method:', fontsize=8, color='#2E7D32', weight='bold')
    b2a = Box(ax, 0.5, 1.7, 3.0, 0.8,
        'Agent says:\n"Market is $28.6B"', fs=7, fc='#C8E6C9', bold=True)
    b2b = Box(ax, 4.3, 1.7, 3.0, 0.8,
        'Source:\nGrand View Research\nstatista.com/xxx [2024]', fs=7, fc='#C8E6C9')
    b2c = Box(ax, 8.0, 1.7, 3.0, 0.8,
        'Can you verify?\n✅ YES — click the URL\nConfidence: 90%', fs=7, fc='#C8E6C9', ec='#2E7D32')
    arr(ax, b2a.right, b2b.left, c='#2E7D32')
    arr(ax, b2b.right, b2c.left, c='#2E7D32')

    ax.text(5.5, 1.1, 'Every claim → searched on the web → source URL attached → confidence level assigned.',
            ha='center', fontsize=8, color=NAVY, weight='bold')
    ax.text(5.5, 0.6, 'If no data found → marked as [UNVALIDATED] — never made up.',
            ha='center', fontsize=7, color='#C62828', style='italic')
    save(fig, 'evidence')

# ══════════════════════════════════════════════════════════════
# 5. SETUP — Step by Step
# ══════════════════════════════════════════════════════════════
def diagram_setup():
    fig, ax = plt.subplots(figsize=(11, 7), facecolor=WHITE)
    ax.set_xlim(-0.5, 11); ax.set_ylim(-0.5, 7.5); ax.axis('off')
    ax.text(5.2, 7.1, 'Setup: Copy Files Once, Then Use Forever', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    Box(ax, 0.3, 2.5, 4.5, 4.0,
        '.cursor/\n'
        '  agents/\n'
        '    ideation-orchestrator.md\n'
        '    requirements-analyst.md\n'
        '    market-researcher.md\n'
        '    tech-scout.md\n'
        '    competitor-analyst.md\n'
        '    idea-evaluator.md\n'
        '    ux-strategist.md\n'
        '    risk-assessor.md\n'
        '    idea-crystallizer.md\n'
        '  mcp.json  (optional)', fs=6, fc='#F5F5F5', tc=BLUE)

    steps = [
        (5.5, 5.8, 'Step 1: Copy 9 agent files\n+ 1 MCP config to .cursor/', '#E3F2FD'),
        (5.5, 4.6, 'Step 2: Open Cursor Agent Mode\nType your idea (even 1 sentence)', '#D1C4E9'),
        (5.5, 3.4, 'Step 3: Answer 10-15 questions\n(this is the only manual part)', '#FFF3E0'),
        (5.5, 2.2, 'Step 4: Wait ~15 minutes\n(6 agents research in parallel)', '#E0F7FA'),
        (5.5, 1.0, 'Step 5: Receive 13-section\nIdea Specification with evidence', '#C8E6C9'),
    ]
    sboxes = []
    for x, y, text, fc in steps:
        b = Box(ax, x, y, 4.8, 0.9, text, fs=6.5, fc=fc)
        sboxes.append(b)
    for i in range(len(sboxes)-1):
        arr(ax, sboxes[i].bot, sboxes[i+1].top)

    Box(ax, 0.3, 0.3, 4.5, 1.5,
        'KEY INSIGHT:\n\nYou type 1 sentence.\nYou answer 10-15 questions.\n\nEverything else is automatic.\nAll 8 agents search the web.',
        fs=6.5, fc='#FFF8E1', bold=True)
    save(fig, 'setup')

# ══════════════════════════════════════════════════════════════
# 6. PRACTICE SERIES
# ══════════════════════════════════════════════════════════════
def diagram_series():
    fig, ax = plt.subplots(figsize=(12, 4), facecolor=WHITE)
    ax.set_xlim(-0.5, 12.5); ax.set_ylim(-0.5, 4); ax.axis('off')
    ax.text(6.0, 3.6, '3-Part Series: From Idea to Working Code', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    b1 = Box(ax, 0.0, 0.5, 3.5, 2.2,
        'Practice 1 (THIS)\nMulti-Agent Ideation\n\nInput: 1 sentence\nOutput: 13-section\nIdea Specification',
        fs=7, fc='#E3F2FD', bold=True, ec='#1565C0')
    b2 = Box(ax, 4.3, 0.5, 3.5, 2.2,
        'Practice 2\nIdea to Plan\n\nInput: Idea Spec\nOutput: Architecture\n+ user stories',
        fs=7, fc='#FFF3E0')
    b3 = Box(ax, 8.6, 0.5, 3.5, 2.2,
        'Practice 3\nPlan to Code\n\nInput: Impl Plan\nOutput: Working code\n+ tests',
        fs=7, fc='#E8F5E9')

    arr(ax, b1.right, b2.left, c=NAVY)
    arr(ax, b2.right, b3.left, c=NAVY)

    ax.text(3.0, 0.1, 'Idea Spec', fontsize=7, color='#2E7D32', ha='center', weight='bold')
    ax.text(7.3, 0.1, 'Impl Plan', fontsize=7, color='#2E7D32', ha='center', weight='bold')
    save(fig, 'series')

print('Generating Practice 1 diagrams...')
diagram_comparison()
diagram_pipeline()
diagram_agents()
diagram_evidence()
diagram_setup()
diagram_series()
print(f'All 6 diagrams saved to: {OUT}')
