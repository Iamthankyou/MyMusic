"""
Practice 1 v5 FINAL: MAX POWER — Cursor Native Subagents + Full ClaudeKit Integration
8 subagents + 6 ClaudeKit techniques + MCP + Quality Gates + Confidence Scoring
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

def arr_c(ax, p1, p2, c=DGREY, lw=1.0, rad=0.3):
    ax.annotate('', xy=p2, xytext=p1,
                arrowprops=dict(arrowstyle='->', color=c, lw=lw, connectionstyle=f'arc3,rad={rad}'))

# ══════════════════════════════════════════════════════════════
# 1. FULL PIPELINE — MAX POWER
# ══════════════════════════════════════════════════════════════
def diagram_pipeline():
    fig, ax = plt.subplots(figsize=(14, 10), facecolor=WHITE)
    ax.set_xlim(-0.5, 14); ax.set_ylim(-2, 10); ax.axis('off')
    ax.text(7, 9.5, 'MAX POWER Ideation Pipeline', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')
    ax.text(7, 9.0, '1 Orchestrator + 8 Subagents + 6 ClaudeKit Techniques + MCP',
            ha='center', fontsize=9, color=DGREY, fontfamily='sans-serif')

    # Internet + MCP
    b_web = Box(ax, 3.5, 8.0, 3.5, 0.6,
        'INTERNET (Web Search)', fs=7, fc='#E1F5FE', ec='#0288D1', bold=True)
    b_mcp = Box(ax, 7.5, 8.0, 3.5, 0.6,
        'MCP: context7 + seq-thinking', fs=7, fc='#F3E5F5', ec='#7B1FA2', bold=True)

    # User + Orchestrator
    b_user = Box(ax, 0.2, 6.2, 2.3, 1.2,
        'USER\n\n"App idea..."', fs=7, fc='#FFF8E1', bold=True)
    b_orch = Box(ax, 3.3, 6.0, 3.0, 1.5,
        'ORCHESTRATOR\n\n4-Bucket Context:\nWrite | Select\nCompress | Isolate', fs=6, fc='#D1C4E9', ec='#5E35B1', bold=True)
    arr(ax, b_user.right, b_orch.left, c=NAVY, lw=2)

    # Phase 1
    ax.text(9, 7.2, 'Phase 1: Foreground', fontsize=7, color='#1565C0', weight='bold')
    b_req = Box(ax, 8, 6.0, 3.0, 1.5,
        'Sub 1: requirements-analyst\n\nINVERSION EXERCISE\n10-15 questions\n+ flip assumptions\n+ web validate', fs=5.5, fc='#E3F2FD', bold=True)
    arr(ax, b_orch.right, b_req.left, c='#1565C0', lw=1.5)
    arr(ax, (b_req.cx, b_req.y+b_req.h), (b_web.x+b_web.w-0.5, b_web.y), c='#0288D1', lw=0.8)

    # Phase 2 — Parallel
    ax.text(0.5, 5.2, 'Phase 2: PARALLEL x3', fontsize=7, color='#E65100', weight='bold')
    p2 = [
        (0.3, 'Sub 2: market-researcher\n\nSCALE GAME\nTAM/SAM + trends\nTest at 1000x / 0.001x', '#FFF3E0'),
        (4.0, 'Sub 3: tech-scout\n\nMETA-PATTERN + context7\nAPIs, pricing, patterns\nacross 3+ services', '#F3E5F5'),
        (7.7, 'Sub 4: competitor-analyst\n\nSCAMPER +\nCOLLISION-ZONE\n5+ competitors + 3 collisions', '#FBE9E7'),
    ]
    p2b = []
    for x, text, fc in p2:
        b = Box(ax, x, 3.3, 3.2, 1.6, text, fs=5.5, fc=fc, bold=True)
        p2b.append(b)
    # Web connections
    for b in p2b:
        arr(ax, (b.cx, b.y+b.h), (b_web.cx, b_web.y), c='#0288D1', lw=0.5)
    # MCP for tech-scout
    arr(ax, (p2b[1].cx+0.5, p2b[1].y+p2b[1].h), (b_mcp.cx, b_mcp.y), c='#7B1FA2', lw=0.8)

    # Quality Gate 1
    b_qg1 = Box(ax, 11.3, 4.0, 2.5, 0.5,
        'QUALITY GATE\n5+ URLs | sections filled', fs=5, fc='#FFCDD2', ec='#C62828', bold=True)
    arr(ax, (p2b[2].x+p2b[2].w, p2b[2].cy-0.3), b_qg1.left, c='#C62828', lw=1.0)

    # Phase 3 — Parallel
    ax.text(0.5, 2.6, 'Phase 3: PARALLEL x3', fontsize=7, color='#AD1457', weight='bold')
    p3 = [
        (0.3, 'Sub 5: idea-evaluator\n\nSIMPLIFICATION\nCASCADES\n1 insight -> N eliminated', '#FCE4EC'),
        (4.0, 'Sub 6: ux-strategist\n\nCOLLISION-ZONE UX\nBreakthrough interaction\npatterns via metaphors', '#E0F7FA'),
        (7.7, 'Sub 7: risk-assessor\n\nINVERSION + SCALE GAME\n"How to GUARANTEE fail?"\nStress-test at extremes', '#FFF9C4'),
    ]
    p3b = []
    for x, text, fc in p3:
        b = Box(ax, x, 0.8, 3.2, 1.6, text, fs=5.5, fc=fc, bold=True)
        p3b.append(b)
    # Phase 2 -> 3 connections
    for b2 in p2b:
        for b3 in p3b:
            arr(ax, (b2.cx, b2.y), (b3.cx, b3.y+b3.h), c='#78909C', lw=0.3)

    # Quality Gate 2
    b_qg2 = Box(ax, 11.3, 2.6, 2.5, 0.5,
        'QUALITY GATE\nscores + risks + journeys', fs=5, fc='#FFCDD2', ec='#C62828', bold=True)
    arr(ax, (p3b[2].x+p3b[2].w, p3b[2].cy+0.3), b_qg2.left, c='#C62828', lw=1.0)

    # Phase 4
    ax.text(11.8, 1.9, 'Phase 4', fontsize=7, color='#2E7D32', weight='bold')
    b_crystal = Box(ax, 11.3, 0.3, 2.5, 1.4,
        'Sub 8: crystallizer\n\nCOMPRESSION\n13-section Spec\n+ confidence score', fs=5.5, fc='#C8E6C9', ec='#2E7D32', bold=True)
    for b3 in p3b:
        arr(ax, b3.right, b_crystal.left, c='#2E7D32', lw=0.8)

    # Final output
    b_final = Box(ax, 11.3, -1.2, 2.5, 1.0,
        'IDEA SPECIFICATION\n13 sections + confidence\nscore + evidence trail', fs=5.5, fc='#A5D6A7', ec='#2E7D32', bold=True, lw=2)
    arr(ax, b_crystal.bot, b_final.top, c='#2E7D32', lw=2)

    # Legend
    ax.text(0.2, -0.3, 'User types 1 prompt. Orchestrator handles everything. 6 agents run in parallel.', fontsize=7, color=NAVY, weight='bold')
    ax.text(0.2, -0.7, 'Quality Gates validate outputs. Confidence Scoring (90%/60%/low) on every claim.', fontsize=7, color='#C62828')
    ax.text(0.2, -1.1, 'When-Stuck fallback: broaden > alternative sources > flag gap. Never fabricate.', fontsize=7, color='#E65100')

    save(fig, 'pipeline_v5_maxpower')

# ══════════════════════════════════════════════════════════════
# 2. CLAUDEKIT TECHNIQUES MAP
# ══════════════════════════════════════════════════════════════
def diagram_techniques():
    fig, ax = plt.subplots(figsize=(14, 8), facecolor=WHITE)
    ax.set_xlim(-0.5, 14); ax.set_ylim(-0.5, 8); ax.axis('off')
    ax.text(7, 7.6, 'ClaudeKit Techniques — Where Each Is Applied', ha='center',
            fontsize=13, color=NAVY, weight='bold', fontfamily='sans-serif')

    techniques = [
        # (x, y, name, description, applied_to, color)
        (0.2, 5.5, 'Inversion Exercise', 'Flip assumptions:\n"What if opposite is true?"\nReveals hidden constraints', 'requirements-analyst\nrisk-assessor', '#E3F2FD'),
        (3.7, 5.5, 'Scale Game', 'Test at 1000x / 0.001x:\n"What breaks? Survives?"\nReveals fundamental truths', 'market-researcher\nrisk-assessor', '#FFF3E0'),
        (7.2, 5.5, 'Collision-Zone Thinking', 'Force unrelated concepts:\n"What if X was like Y?"\nBreakthrough innovation', 'competitor-analyst\nux-strategist', '#FCE4EC'),
        (10.7, 5.5, 'Meta-Pattern Recognition', 'Spot patterns across 3+\ndomains/APIs/services.\nExtract universal principle', 'tech-scout', '#F3E5F5'),
        (1.8, 2.5, 'Simplification Cascades', 'Find 1 insight that\neliminates N weaknesses.\n"All same problem underneath"', 'idea-evaluator', '#E0F7FA'),
        (5.5, 2.5, '4-Bucket Context Strategy', 'Write: save to files\nSelect: pass only needed\nCompress: reduce tokens\nIsolate: per-agent context', 'orchestrator', '#D1C4E9'),
        (9.2, 2.5, 'docs-seeker + context7', 'context7.com/[org]/[repo]\n/llms.txt?topic=[query]\nReal API docs instantly', 'tech-scout\nrequirements-analyst', '#C8E6C9'),
    ]

    for x, y, name, desc, applied, fc in techniques:
        Box(ax, x, y, 3.2, 1.8, f'{name}\n\n{desc}', fs=5.5, fc=fc, bold=True)
        Box(ax, x, y-0.7, 3.2, 0.6, f'Applied to: {applied}', fs=5, fc=WHITE, ec='#999')

    # Bottom summary
    Box(ax, 1.0, 0.3, 12, 0.8,
        'Total: 6 ClaudeKit techniques + 2 MCP servers = Every agent has a specialized thinking tool',
        fs=8, fc='#E8F5E9', ec='#2E7D32', bold=True)

    save(fig, 'techniques_v5')

# ══════════════════════════════════════════════════════════════
# 3. BEFORE vs AFTER — FULL COMPARISON
# ══════════════════════════════════════════════════════════════
def diagram_comparison():
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(15, 7.5), facecolor=WHITE)
    for ax in (ax1, ax2, ax3):
        ax.set_xlim(0, 5); ax.set_ylim(-0.2, 8); ax.axis('off')
    fig.suptitle('Evolution — From Basic to MAX POWER', fontsize=14,
                 color=NAVY, weight='bold', fontfamily='sans-serif', y=0.97)

    def panel(ax, title, color, bg, items):
        box = FancyBboxPatch((0.1, 0.1), 4.8, 7.4, boxstyle="round,pad=0.04",
                              facecolor=bg, edgecolor='#E0E0E0', lw=1)
        ax.add_patch(box)
        ax.text(2.5, 7.1, title, ha='center', fontsize=9, color=color, weight='bold')
        ax.plot([0.3, 4.7], [6.7, 6.7], color='#E0E0E0', lw=0.8)
        for i, (mark, t, c) in enumerate(items):
            ax.text(0.3, 6.3 - i*0.42, f'{mark}  {t}', fontsize=6, color=c, va='center',
                    weight='bold' if i >= len(items)-2 else 'normal')

    panel(ax1, 'v1: 4 .mdc Rules\n(Manual)', '#C62828', '#FDF2F2', [
        ('X', '4 separate chats, copy-paste', '#C62828'),
        ('X', 'User manages handoff', '#C62828'),
        ('X', '1 model pretending 4 roles', '#999'),
        ('X', 'No parallel execution', '#999'),
        ('X', 'Shared context (polluted)', '#999'),
        ('X', 'Web search optional', '#999'),
        ('X', 'No ClaudeKit techniques', '#999'),
        ('X', 'No MCP tools', '#999'),
        ('X', 'No context management', '#999'),
        ('X', 'No assumption challenging', '#999'),
        ('', '', '#999'),
        ('', 'Power Level: LOW', '#C62828'),
        ('', 'Evidence: PARTIAL', '#C62828'),
    ])
    panel(ax2, 'v4: 8 Native Subagents\n(Auto)', '#F57F17', '#FFFFF0', [
        ('~', '1 prompt, auto-delegation', '#666'),
        ('~', 'Orchestrator handles flow', '#666'),
        ('~', 'Real subagents (isolated)', '#666'),
        ('~', 'Parallel execution (6 at once)', '#666'),
        ('~', 'Context isolation per agent', '#666'),
        ('~', 'Web search mandatory', '#666'),
        ('~', 'No ClaudeKit techniques', '#C62828'),
        ('~', 'No MCP tools', '#C62828'),
        ('~', 'No context compression', '#C62828'),
        ('~', 'No creative thinking tools', '#C62828'),
        ('', '', '#666'),
        ('', 'Power Level: MEDIUM', '#F57F17'),
        ('', 'Evidence: FULL', '#F57F17'),
    ])
    panel(ax3, 'v5: MAX POWER\n(ClaudeKit + MCP)', '#2E7D32', '#F2F8F2', [
        ('V', '1 prompt, full autonomous', '#2E7D32'),
        ('V', '4-bucket context engineering', '#2E7D32'),
        ('V', '8 isolated subagents', BLUE),
        ('V', '6 parallel + compression', BLUE),
        ('V', 'Inversion Exercise (flip)', '#2E7D32'),
        ('V', 'Scale Game (1000x stress)', '#2E7D32'),
        ('V', 'Collision-Zone (breakthrough)', '#2E7D32'),
        ('V', 'Meta-Pattern (3+ domains)', '#2E7D32'),
        ('V', 'Simplification Cascades', '#2E7D32'),
        ('V', 'MCP: context7 + seq-thinking', '#7B1FA2'),
        ('', '', BLUE),
        ('', 'Power Level: MAXIMUM', '#2E7D32'),
        ('', 'Evidence: FULL + CREATIVE', '#2E7D32'),
    ])
    fig.subplots_adjust(wspace=0.06)
    save(fig, 'comparison_v5_evolution')

# ══════════════════════════════════════════════════════════════
# 4. AGENT DETAIL — 8 SUBAGENTS + TECHNIQUES
# ══════════════════════════════════════════════════════════════
def diagram_agents():
    fig, ax = plt.subplots(figsize=(14, 10), facecolor=WHITE)
    ax.set_xlim(-0.3, 14.3); ax.set_ylim(-0.5, 10); ax.axis('off')
    ax.text(7, 9.6, 'The 8 Specialist Subagents + ClaudeKit Techniques', ha='center',
            fontsize=13, color=NAVY, weight='bold', fontfamily='sans-serif')

    agents = [
        # Row 1 (x, y, name, technique, job, model, web, color)
        (0.0, 6.0, 'requirements-analyst', 'INVERSION', 'WHO/WHAT/WHY/HOW\nFlip 3+ assumptions\nWeb validate claims', 'inherit', 'YES', '#E3F2FD'),
        (3.6, 6.0, 'market-researcher', 'SCALE GAME', 'TAM/SAM/SOM\nTest at 1000x/0.001x\nBenchmark pricing', 'fast', 'YES', '#FFF3E0'),
        (7.2, 6.0, 'tech-scout', 'META-PATTERN', 'APIs via context7\nPrice + limits\nPattern across 3+ APIs', 'fast', 'YES+MCP', '#F3E5F5'),
        (10.8, 6.0, 'competitor-analyst', 'COLLISION-ZONE', '5+ competitors\nSCAMPER analysis\n3 creative collisions', 'fast', 'YES', '#FBE9E7'),
        # Row 2
        (0.0, 2.5, 'idea-evaluator', 'SIMPLIFICATION', '8-dim scoring\nDevil\'s advocate\n1 insight -> N eliminated', 'inherit', 'NO', '#FCE4EC'),
        (3.6, 2.5, 'ux-strategist', 'COLLISION-ZONE', 'User journeys\nUX collisions\nEngagement hooks', 'fast', 'SOME', '#E0F7FA'),
        (7.2, 2.5, 'risk-assessor', 'INV + SCALE', 'Guarantee-fail exercise\nStress-test risks\nLegal research', 'fast', 'YES', '#FFF9C4'),
        (10.8, 2.5, 'idea-crystallizer', 'COMPRESSION', 'Synthesize 7 reports\n13-section Spec\nFull evidence trail', 'inherit', 'NO', '#C8E6C9'),
    ]

    for x, y, name, technique, job, model, web, fc in agents:
        Box(ax, x, y+2.0, 3.3, 0.6, name, fs=6.5, fc=fc, bold=True)
        Box(ax, x, y+1.2, 3.3, 0.7, f'ClaudeKit: {technique}', fs=6, fc='#FFECB3', ec='#FF8F00', bold=True)
        Box(ax, x, y+0.0, 3.3, 1.1, job, fs=5.5, fc=WHITE)
        web_fc = '#E1F5FE' if 'YES' in web else '#F5F5F5'
        web_ec = '#0288D1' if 'YES' in web else '#999'
        mcp_label = ' + MCP' if 'MCP' in web else ''
        Box(ax, x, y-0.5, 3.3, 0.4, f'Web: {web} | Model: {model}{mcp_label}', fs=5, fc=web_fc, ec=web_ec)

    ax.text(5.5, 5.7, '<-- PARALLEL -->', fontsize=8, color='#E65100', weight='bold', ha='center')
    ax.text(5.5, 2.2, '<-- PARALLEL -->', fontsize=8, color='#AD1457', weight='bold', ha='center')
    ax.text(0.5, 5.7, '(foreground)', fontsize=6, color='#1565C0')
    ax.text(11.0, 2.2, '(foreground)', fontsize=6, color='#2E7D32')

    ax.text(7, -0.1, 'Sources: Cursor Native Subagents + BMAD Orchestrator + ClaudeKit (6 techniques) + MCP (2 servers)',
            ha='center', fontsize=7, color=DGREY, style='italic')
    save(fig, 'agents_v5_detail')

# ══════════════════════════════════════════════════════════════
# 5. EVIDENCE TRAIL
# ══════════════════════════════════════════════════════════════
def diagram_evidence():
    fig, ax = plt.subplots(figsize=(11, 5.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 11.5); ax.set_ylim(-0.5, 5.5); ax.axis('off')
    ax.text(5.5, 5.1, 'Evidence Trail — From Claim to Source', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    b1 = Box(ax, 0.5, 3.5, 3.0, 1.0,
        'Claim in Final Spec:\n"Market for indie music\napps is growing 15% YoY"', fs=7, fc='#FFF8E1', bold=True)
    b2 = Box(ax, 4.3, 3.5, 3.0, 1.0,
        'market-researcher\nsearched + Scale Game:\n"At 1000x users,\nthis market still holds"', fs=6.5, fc='#E1F5FE')
    b3 = Box(ax, 8.0, 3.5, 3.0, 1.0,
        'Source found:\nStatista + TechCrunch\nURL: statista.com/...\nURL: techcrunch.com/...', fs=6.5, fc='#E8F5E9', ec='#2E7D32')
    arr(ax, b1.right, b2.left, c='#0288D1')
    arr(ax, b2.right, b3.left, c='#2E7D32')

    b4 = Box(ax, 0.5, 1.5, 3.0, 1.0,
        'Innovation in Spec:\n"Daily tasting flight\nof 5 curated songs"', fs=7, fc='#FCE4EC', bold=True)
    b5 = Box(ax, 4.3, 1.5, 3.0, 1.0,
        'competitor-analyst\nCollision-Zone:\nMusic app X Restaurant\n= "tasting menu" concept', fs=6.5, fc='#FFECB3')
    b6 = Box(ax, 8.0, 1.5, 3.0, 1.0,
        'Validated:\nNo competitor has this\nFeasible to build\nUsers want curation', fs=6.5, fc='#E8F5E9', ec='#2E7D32')
    arr(ax, b4.right, b5.left, c='#FF8F00')
    arr(ax, b5.right, b6.left, c='#2E7D32')

    ax.text(5.5, 0.9, 'Every claim: web-validated data + source URL. Every innovation: ClaudeKit technique + feasibility check.',
            ha='center', fontsize=7.5, color=DGREY, style='italic', weight='bold')
    save(fig, 'evidence_v5')

# ══════════════════════════════════════════════════════════════
# 6. SETUP
# ══════════════════════════════════════════════════════════════
def diagram_setup():
    fig, ax = plt.subplots(figsize=(11, 7), facecolor=WHITE)
    ax.set_xlim(-0.5, 11); ax.set_ylim(-0.5, 7.5); ax.axis('off')
    ax.text(5.2, 7.1, 'Setup: One-Time, Then Fully Automatic', ha='center',
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
        '  mcp.json  (context7 + seq-thinking)', fs=6, fc='#F5F5F5', tc=BLUE)

    steps = [
        (5.5, 5.8, 'Step 1: Files in .cursor/agents/\n+ MCP in .cursor/mcp.json', '#E3F2FD'),
        (5.5, 4.6, 'Step 2: Open Agent chat:\n/ideation-orchestrator "My idea..."', '#D1C4E9'),
        (5.5, 3.4, 'Step 3: Answer requirements\nquestions (Phase 1 only)', '#FFF3E0'),
        (5.5, 2.2, 'Step 4: 6 agents run parallel\nwith ClaudeKit techniques', '#E0F7FA'),
        (5.5, 1.0, 'Step 5: Receive 13-section\nIdea Spec with evidence', '#C8E6C9'),
    ]
    sboxes = []
    for x, y, text, fc in steps:
        b = Box(ax, x, y, 4.8, 0.9, text, fs=6.5, fc=fc)
        sboxes.append(b)
    for i in range(len(sboxes)-1):
        arr(ax, sboxes[i].bot, sboxes[i+1].top)

    Box(ax, 0.3, 0.3, 4.5, 1.5,
        'KEY:\n\nOLD: 4 chats, manual, no tools\nNEW: 1 prompt, autonomous,\n6 ClaudeKit techniques + MCP\n\nUser touches keyboard ONCE.',
        fs=6.5, fc='#FFF8E1', bold=True)
    save(fig, 'setup_v5')

# ══════════════════════════════════════════════════════════════
# 7. SOURCES
# ══════════════════════════════════════════════════════════════
def diagram_sources():
    fig, ax = plt.subplots(figsize=(14, 5.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 14.5); ax.set_ylim(-0.5, 5.5); ax.axis('off')
    ax.text(7, 5.1, 'Sources — Evidence-Based Method', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    sources = [
        (0.2, 3.0, 'Cursor Subagents\n\ncursor.com/docs/\nsubagents\n\nParallel execution\nContext isolation', '#D1C4E9'),
        (2.5, 3.0, 'BMAD Method\n\ngithub.com/bmad-\ncode-org/BMAD\n\nMulti-agent\nOrchestrator', '#E3F2FD'),
        (4.8, 3.0, 'ClaudeKit:\nProblem-Solving\n\n5 techniques:\nInversion, Scale,\nCollision, Meta,\nSimplification', '#FFF3E0'),
        (7.1, 3.0, 'ClaudeKit:\nContext Eng.\n\n4-bucket strategy\nCompression\nIsolation', '#FCE4EC'),
        (9.4, 3.0, 'ClaudeKit:\ndocs-seeker\n\ncontext7.com\nllms.txt\nParallel agents', '#E0F7FA'),
        (11.7, 3.0, 'MCP Protocol\n\nsequential-\nthinking\ncontext7-mcp', '#F3E5F5'),
    ]
    for x, y, text, fc in sources:
        Box(ax, x, y, 2.1, 2.0, text, fs=5, fc=fc)

    b_result = Box(ax, 1.5, 0.5, 11, 1.0,
        'MAX POWER = Cursor Subagents + BMAD Orchestrator + ClaudeKit (problem-solving + context-eng + docs-seeker) + MCP',
        fs=8, fc='#E8F5E9', bold=True, ec='#2E7D32')
    for i, (x, y, _, _) in enumerate(sources):
        arr(ax, (x+1.05, y), (b_result.x + 1.0 + i*1.8, b_result.y + b_result.h), c='#2E7D32')
    save(fig, 'sources_v5')

# ══════════════════════════════════════════════════════════════
# 8. PRACTICE SERIES
# ══════════════════════════════════════════════════════════════
def diagram_series():
    fig, ax = plt.subplots(figsize=(12, 4), facecolor=WHITE)
    ax.set_xlim(-0.5, 12.5); ax.set_ylim(-0.5, 4); ax.axis('off')
    ax.text(6.0, 3.6, '3-Practice Series — From Idea to Working Code', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    b1 = Box(ax, 0.0, 0.5, 3.5, 2.2,
        'Practice 1 (THIS)\nMAX POWER Ideation\n\nInput: 1 sentence\nOutput: 13-section Spec\n+ ClaudeKit + evidence',
        fs=7, fc='#E3F2FD', bold=True, ec='#1565C0')
    b2 = Box(ax, 4.3, 0.5, 3.5, 2.2,
        'Practice 2\nIdea to Plan\n\nInput: Idea Spec\nOutput: Implementation\n+ architecture + stories',
        fs=7, fc='#FFF3E0')
    b3 = Box(ax, 8.6, 0.5, 3.5, 2.2,
        'Practice 3\nPlan to Code\n\nInput: Impl Plan\nOutput: Working code\n+ tests + deployment',
        fs=7, fc='#E8F5E9')

    arr(ax, b1.right, b2.left, c=NAVY)
    arr(ax, b2.right, b3.left, c=NAVY)

    ax.text(3.0, 0.1, 'Idea Spec', fontsize=7, color='#2E7D32', ha='center', weight='bold')
    ax.text(7.3, 0.1, 'Impl Plan', fontsize=7, color='#2E7D32', ha='center', weight='bold')
    save(fig, 'series_v5')

print('Generating Practice 1 v5 MAX POWER diagrams...')
diagram_pipeline()
diagram_techniques()
diagram_comparison()
diagram_agents()
diagram_evidence()
diagram_setup()
diagram_sources()
diagram_series()
print(f'All 8 diagrams saved to: {OUT}')
