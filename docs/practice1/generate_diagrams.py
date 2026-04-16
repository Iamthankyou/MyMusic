"""
Practice 1 v3: Multi-Agent Ideation with REAL Web Research
Diagrams reflect agents that use WebSearch, not "knowledge in a box"
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
# 1. PIPELINE WITH WEB RESEARCH
# ══════════════════════════════════════════════════════════════
def diagram_pipeline():
    fig, ax = plt.subplots(figsize=(13, 7), facecolor=WHITE)
    ax.set_xlim(-0.5, 13.5); ax.set_ylim(-2, 7.5); ax.axis('off')
    ax.text(6.5, 7.0, 'Multi-Agent Ideation Pipeline  —  with Real Web Research', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    # Internet cloud at top
    b_web = Box(ax, 4.5, 5.5, 4.5, 1.0,
        'INTERNET\nWeb Search / WebFetch / APIs', fs=8, fc='#E1F5FE', ec='#0288D1', bold=True)

    # User
    b_user = Box(ax, 0.0, 2.5, 2.0, 1.8,
        'USER\n\nShort idea:\n"Music app for\nindie discovery"', fs=7, fc='#FFF8E1', bold=True)

    # Agent 1
    b_a1 = Box(ax, 2.8, 2.5, 2.3, 1.8,
        'Agent 1\nRequirements\nAnalyst\n\nAsk 10+ questions\n+ validate via\nweb search', fs=6, fc='#E3F2FD', bold=True)

    # Agent 2
    b_a2 = Box(ax, 5.8, 2.5, 2.3, 1.8,
        'Agent 2\nMarket & Tech\nResearcher\n\nSearch competitors\nAPIs, pricing,\nSCAMPER + data', fs=6, fc='#FFF3E0', bold=True)

    # Agent 3
    b_a3 = Box(ax, 8.8, 2.5, 2.3, 1.8,
        'Agent 3\nCritical\nEvaluator\n\nScore vs real data\nDevil\'s advocate\nVerify claims', fs=6, fc='#FCE4EC', bold=True)

    # Agent 4
    b_a4 = Box(ax, 11.5, 2.5, 1.8, 1.8,
        'Agent 4\nIdea\nCompiler\n\nSynthesize\nall docs +\nevidence trail', fs=6, fc='#E8EAF6', bold=True)

    # Outputs (documents)
    b_o1 = Box(ax, 2.8, 0.5, 2.3, 1.0, 'Requirements Brief\n+ existing solutions\n+ market data', fs=5.5, fc='#E8F5E9', ec='#2E7D32')
    b_o2 = Box(ax, 5.8, 0.5, 2.3, 1.0, 'Research Report\n+ source URLs\n+ API pricing', fs=5.5, fc='#E8F5E9', ec='#2E7D32')
    b_o3 = Box(ax, 8.8, 0.5, 2.3, 1.0, 'Evaluation Report\n+ evidence per score\n+ unverified flags', fs=5.5, fc='#E8F5E9', ec='#2E7D32')
    b_final = Box(ax, 11.0, -0.1, 2.5, 1.2,
        'Idea Spec Document\n13 sections\nwith evidence trail\n+ source URLs', fs=6, fc='#C8E6C9', ec='#2E7D32', bold=True)

    # Horizontal flow
    arr(ax, b_user.right, b_a1.left)
    arr(ax, b_a1.right, b_a2.left)
    arr(ax, b_a2.right, b_a3.left)
    arr(ax, b_a3.right, b_a4.left)

    # Agents to outputs
    arr(ax, b_a1.bot, b_o1.top, c='#2E7D32')
    arr(ax, b_a2.bot, b_o2.top, c='#2E7D32')
    arr(ax, b_a3.bot, b_o3.top, c='#2E7D32')
    arr(ax, b_a4.bot, b_final.top, c='#2E7D32')

    # Outputs flow forward
    arr_c(ax, b_o1.right, (b_a2.x, b_a2.y+0.2), c='#2E7D32', rad=-0.15)
    arr_c(ax, b_o2.right, (b_a3.x, b_a3.y+0.2), c='#2E7D32', rad=-0.15)
    arr_c(ax, b_o3.right, (b_a4.x, b_a4.y+0.2), c='#2E7D32', rad=-0.15)

    # Internet connections (agents 1,2,3 search the web)
    arr(ax, (b_a1.cx, b_a1.y+b_a1.h), (b_web.x+0.5, b_web.y), c='#0288D1', lw=1.5)
    arr(ax, (b_a2.cx, b_a2.y+b_a2.h), (b_web.cx, b_web.y), c='#0288D1', lw=1.5)
    arr(ax, (b_a3.cx, b_a3.y+b_a3.h), (b_web.x+b_web.w-0.5, b_web.y), c='#0288D1', lw=1.5)
    # Agent 4 does NOT search (compile only)
    ax.text(12.4, 5.0, 'Agent 4: NO web search\n(compile only)', fontsize=5.5, color=DGREY, ha='center', style='italic')

    # User loop
    arr_c(ax, (b_a1.x+0.3, b_a1.y+b_a1.h), (b_user.x+b_user.w, b_user.y+b_user.h-0.2), c='#1565C0', rad=0.2)
    ax.text(1.8, 4.8, 'asks questions', fontsize=5.5, color='#1565C0', style='italic')
    arr_c(ax, (b_user.x+b_user.w, b_user.y+0.3), (b_a1.x, b_a1.y+0.3), c='#1565C0', rad=0.2)
    ax.text(1.8, 2.2, 'user answers', fontsize=5.5, color='#1565C0', style='italic')

    # Labels
    ax.text(6.5, 6.3, 'Agents 1, 2, 3 actively search the internet — not just training data', ha='center',
            fontsize=8, color='#0288D1', weight='bold')
    ax.text(12.2, -1.0, 'Input for\nPractice 2', fontsize=7, color=DGREY, ha='center', weight='bold')

    save(fig, 'pipeline_v3')

# ══════════════════════════════════════════════════════════════
# 2. COMPARISON: 3 METHODS (updated)
# ══════════════════════════════════════════════════════════════
def diagram_3methods():
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(14, 6), facecolor=WHITE)
    for ax in (ax1, ax2, ax3):
        ax.set_xlim(0, 4.8); ax.set_ylim(-0.2, 6.2); ax.axis('off')
    fig.suptitle('3 Approaches to AI Brainstorming  —  Why Multi-Agent Wins', fontsize=14,
                 color=NAVY, weight='bold', fontfamily='sans-serif', y=0.97)

    def panel(ax, title, color, bg, items):
        box = FancyBboxPatch((0.1, 0.1), 4.6, 5.6, boxstyle="round,pad=0.04",
                              facecolor=bg, edgecolor='#E0E0E0', lw=1)
        ax.add_patch(box)
        ax.text(2.4, 5.4, title, ha='center', fontsize=9, color=color, weight='bold')
        ax.plot([0.3, 4.5], [5.1, 5.1], color='#E0E0E0', lw=0.8)
        for i, (mark, t, c) in enumerate(items):
            ax.text(0.3, 4.7 - i*0.38, f'{mark}  {t}', fontsize=6.2, color=c, va='center',
                    weight='bold' if i >= len(items)-2 else 'normal')

    panel(ax1, 'Method 1\nAd-hoc Prompting', '#999', '#FDF2F2', [
        ('X', '"Make me a music app"', '#999'),
        ('X', 'AI answers in 30 seconds', '#999'),
        ('X', 'No questions asked', '#999'),
        ('X', 'No web research', '#999'),
        ('X', '1 generic idea, no evidence', '#999'),
        ('X', 'No competitors checked', '#999'),
        ('X', 'No API pricing verified', '#999'),
        ('X', 'Same as 1000 other people', '#999'),
        ('X', 'Creator = Judge (bias)', '#999'),
        ('', '', '#999'),
        ('', 'Quality: LOW', '#C62828'),
        ('', 'Evidence: NONE', '#C62828'),
    ])
    panel(ax2, 'Method 2\nSingle Agent + Instructions', '#F57F17', '#FFFFF0', [
        ('~', 'Long prompt with rules', '#666'),
        ('~', 'AI asks some questions', '#666'),
        ('~', 'One agent does everything', '#666'),
        ('~', 'MAY search web (if prompted)', '#666'),
        ('~', 'Context window fills up', '#666'),
        ('~', 'Quality drops across session', '#666'),
        ('~', 'Creator = Judge still', '#666'),
        ('~', 'Some evidence, inconsistent', '#666'),
        ('~', 'Not reproducible', '#666'),
        ('', '', '#666'),
        ('', 'Quality: MEDIUM', '#F57F17'),
        ('', 'Evidence: PARTIAL', '#F57F17'),
    ])
    panel(ax3, 'Method 3\nMulti-Agent + Web Research', NAVY, '#F2F8F2', [
        ('V', '4 agents, each with ONE job', BLUE),
        ('V', 'Agent 1 asks 10-15 questions', BLUE),
        ('V', 'Agents 1-3 search the web', BLUE),
        ('V', 'All data has source URLs', BLUE),
        ('V', 'Fresh context per agent', BLUE),
        ('V', 'Agent 3 = devil\'s advocate', BLUE),
        ('V', 'Evidence per score required', BLUE),
        ('V', 'Reproducible (same 4 configs)', BLUE),
        ('V', 'Sources: ClaudeKit + BMAD', BLUE),
        ('', '', BLUE),
        ('', 'Quality: HIGH', '#2E7D32'),
        ('', 'Evidence: FULL + URLs', '#2E7D32'),
    ])
    fig.subplots_adjust(wspace=0.06)
    save(fig, 'three_methods_v3')

# ══════════════════════════════════════════════════════════════
# 3. WHAT EACH AGENT DOES (with web search highlighted)
# ══════════════════════════════════════════════════════════════
def diagram_agents():
    fig, ax = plt.subplots(figsize=(13, 7.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 13); ax.set_ylim(-1, 7.5); ax.axis('off')
    ax.text(6.2, 7.1, 'Agent Detail  —  What Each Agent Does + Web Search', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    agents = [
        (0.2, 'Agent 1\nRequirements Analyst', '#E3F2FD',
         'Ask 10-15 questions\n(WHO/WHAT/WHY/HOW)',
         'WebSearch:\nmarket size, user data\nexisting solutions',
         'Requirements Brief\n+ market data\n+ existing solutions',
         'NEVER propose solutions'),
        (3.3, 'Agent 2\nMarket & Tech Researcher', '#FFF3E0',
         'Competitor table\nSCAMPER variants\nAPI pricing research',
         'WebSearch:\ncompetitors, Product Hunt\nAPI docs, pricing, G2',
         'Research Report\n+ source URLs\n+ API pricing table',
         'Every claim needs URL'),
        (6.4, 'Agent 3\nCritical Evaluator', '#FCE4EC',
         'Score 4-5 ideas\non 6 criteria\nDevil\'s advocate',
         'WebSearch:\nverify claims from\nAgent 2, re-check data',
         'Evaluation Report\n+ evidence per score\n+ unverified flags',
         'NEVER give all 5s'),
        (9.5, 'Agent 4\nIdea Compiler', '#E8EAF6',
         'Compile all docs\ninto 13-section spec\nwith evidence trail',
         'NO web search\n(compile only —\ndo NOT add new info)',
         'Idea Spec Document\n13 sections\n+ full evidence trail',
         'Do NOT add new info'),
    ]

    for x, title, fc, job, search, output, rule in agents:
        Box(ax, x, 5.5, 2.8, 0.7, title, fs=7.5, fc=fc, bold=True)
        Box(ax, x, 4.2, 2.8, 1.2, f'Job:\n{job}', fs=5.5, fc=WHITE)
        search_fc = '#E1F5FE' if 'NO' not in search else '#F5F5F5'
        search_ec = '#0288D1' if 'NO' not in search else '#999'
        Box(ax, x, 2.8, 2.8, 1.2, f'Web Search:\n{search}', fs=5.5, fc=search_fc, ec=search_ec)
        Box(ax, x, 1.4, 2.8, 1.0, f'Output:\n{output}', fs=5.5, fc='#E8F5E9', ec='#2E7D32')
        Box(ax, x, 0.3, 2.8, 0.5, f'Rule: {rule}', fs=5.5, fc='#FFEBEE', ec='#C62828', tc='#C62828')

        arr(ax, (x+1.4, 5.5), (x+1.4, 5.4))
        arr(ax, (x+1.4, 4.2), (x+1.4, 4.0))
        arr(ax, (x+1.4, 2.8), (x+1.4, 2.4), c='#2E7D32')
        arr(ax, (x+1.4, 1.4), (x+1.4, 0.8), c='#C62828')

    # Flow arrows
    for i in range(3):
        x1 = agents[i][0] + 2.8
        x2 = agents[i+1][0]
        arr(ax, (x1, 5.9), (x2, 5.9), c=NAVY)

    # Based on label
    ax.text(6.2, -0.5, 'Based on: BMAD Method (multi-agent) + ClaudeKit Skills (problem-solving, docs-seeker, context-engineering)',
            ha='center', fontsize=7, color=DGREY, style='italic')

    save(fig, 'agents_v3')

# ══════════════════════════════════════════════════════════════
# 4. EVIDENCE TRAIL
# ══════════════════════════════════════════════════════════════
def diagram_evidence():
    fig, ax = plt.subplots(figsize=(11, 5.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 11.5); ax.set_ylim(-0.5, 5.5); ax.axis('off')
    ax.text(5.5, 5.1, 'The Evidence Trail  —  From Claim to Source', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    # Claim
    b1 = Box(ax, 0.5, 3.5, 3.0, 1.0,
        'Claim in Final Spec:\n"Market for indie music\napps is growing 15% YoY"', fs=7, fc='#FFF8E1', bold=True)

    # Agent 2 search
    b2 = Box(ax, 4.3, 3.5, 3.0, 1.0,
        'Agent 2 searched:\n"indie music streaming\nmarket size 2025"', fs=7, fc='#E1F5FE')

    # Source
    b3 = Box(ax, 8.0, 3.5, 3.0, 1.0,
        'Source found:\nTechCrunch article\nURL: techcrunch.com/...', fs=7, fc='#E8F5E9', ec='#2E7D32')

    arr(ax, b1.right, b2.left, c='#0288D1')
    arr(ax, b2.right, b3.left, c='#2E7D32')

    ax.text(3.0, 3.1, 'traced to', fontsize=6.5, color='#0288D1', ha='center')
    ax.text(7.0, 3.1, 'verified by', fontsize=6.5, color='#2E7D32', ha='center')

    # Bad example
    b4 = Box(ax, 0.5, 1.5, 3.0, 1.0,
        'Claim without evidence:\n"Users love this concept"\n(no data)', fs=7, fc='#FFEBEE', ec='#C62828')

    b5 = Box(ax, 4.3, 1.5, 3.0, 1.0,
        'Agent 3 flags:\n"[UNVALIDATED]\nNo web search result\nfound for this claim"', fs=6.5, fc='#FFEBEE', ec='#C62828')

    b6 = Box(ax, 8.0, 1.5, 3.0, 1.0,
        'Final Spec marks:\n"[NEEDS RESEARCH]\nRe-run Agent 2\nfor this claim"', fs=6.5, fc='#FFF8E1', ec='#F57F17')

    arr(ax, b4.right, b5.left, c='#C62828')
    arr(ax, b5.right, b6.left, c='#F57F17')

    ax.text(5.5, 0.9, 'Every claim must be traceable. Unvalidated claims are explicitly flagged.',
            ha='center', fontsize=8, color=DGREY, style='italic', weight='bold')

    save(fig, 'evidence_trail')

# ══════════════════════════════════════════════════════════════
# 5. CURSOR SETUP (updated)
# ══════════════════════════════════════════════════════════════
def diagram_setup():
    fig, ax = plt.subplots(figsize=(10, 6.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 10.5); ax.set_ylim(-0.5, 7); ax.axis('off')
    ax.text(5.0, 6.6, 'How to Set Up in Cursor  —  Step by Step', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    # File structure
    Box(ax, 0.3, 3.2, 4.5, 3.0,
        '.cursor/rules/\n'
        '  01-requirements-analyst.mdc\n'
        '  02-market-researcher.mdc\n'
        '  03-idea-evaluator.mdc\n'
        '  04-idea-compiler.mdc\n\n'
        'docs/\n'
        '  01-requirements-brief.md\n'
        '  02-research-report.md\n'
        '  03-evaluation-report.md\n'
        '  04-idea-spec.md', fs=6.5, fc='#F5F5F5', tc=BLUE)

    # Steps
    steps = [
        (5.5, 5.5, 'Step 1: Copy 4 .mdc files\nto .cursor/rules/', '#E3F2FD'),
        (5.5, 4.4, 'Step 2: New chat + rule 01\nGive idea, answer questions\nAgent searches web to validate', '#FFF3E0'),
        (5.5, 3.0, 'Step 3: Save output to docs/\nNew chat + rule 02\nAgent searches web heavily', '#FCE4EC'),
        (5.5, 1.8, 'Step 4: New chat + rule 03\nPaste all docs so far\nAgent scores + verifies', '#E8EAF6'),
        (5.5, 0.5, 'Step 5: New chat + rule 04\nPaste all 3 docs\nAgent compiles final spec', '#E8F5E9'),
    ]
    sboxes = []
    for x, y, text, fc in steps:
        b = Box(ax, x, y, 4.5, 0.9, text, fs=6.5, fc=fc)
        sboxes.append(b)
    for i in range(len(sboxes)-1):
        arr(ax, sboxes[i].bot, sboxes[i+1].top)

    Box(ax, 0.3, 0.5, 4.5, 2.0,
        'KEY: Save each agent\'s\noutput to a file before\nstarting the next agent.\n\n'
        'This IS the handoff.\n'
        'Documents = contracts\nbetween agents.', fs=6.5, fc='#FFF8E1', bold=True)

    save(fig, 'setup_v3')

# ══════════════════════════════════════════════════════════════
# 6. SOURCES & METHODOLOGY
# ══════════════════════════════════════════════════════════════
def diagram_sources():
    fig, ax = plt.subplots(figsize=(10, 5), facecolor=WHITE)
    ax.set_xlim(-0.5, 10.5); ax.set_ylim(-0.5, 5.5); ax.axis('off')
    ax.text(5.0, 5.1, 'Sources  —  This Method Is Not Made Up', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    sources = [
        (0.2, 3.0, 'BMAD Method\n(Open Source)\n\ngithub.com/bmad-code-org\n/BMAD-METHOD\n\nMulti-agent personas\n4-phase workflow\nDocument-driven', '#E3F2FD'),
        (2.7, 3.0, 'ClaudeKit Skills\n(Open Source)\n\nproblem-solving:\nInversion, Scale Game\n\ndocs-seeker:\nWebSearch + WebFetch\nparallel agents', '#FFF3E0'),
        (5.2, 3.0, 'Context Engineering\n(ClaudeKit)\n\n4-bucket strategy:\nWrite, Select,\nCompress, Isolate\n\nSub-agent isolation', '#FCE4EC'),
        (7.7, 3.0, 'Sequential Thinking\n(ClaudeKit MCP)\n\nStep-by-step reasoning\nRevision + branching\nDynamic scope', '#E8EAF6'),
    ]
    for x, y, text, fc in sources:
        Box(ax, x, y, 2.3, 2.0, text, fs=5.5, fc=fc)

    # Result
    b_result = Box(ax, 2.0, 0.5, 6.5, 1.0,
        'This Practice = BMAD multi-agent approach + ClaudeKit web research\n'
        '+ ClaudeKit problem-solving + context isolation pattern', fs=7.5, fc='#E8F5E9', bold=True, ec='#2E7D32')

    for i, (x, y, _, _) in enumerate(sources):
        arr(ax, (x+1.15, y), (b_result.x + 0.5 + i*1.5, b_result.y + b_result.h), c='#2E7D32')

    save(fig, 'sources')

# ══════════════════════════════════════════════════════════════
# 7. PRACTICE SERIES
# ══════════════════════════════════════════════════════════════
def diagram_series():
    fig, ax = plt.subplots(figsize=(12, 4), facecolor=WHITE)
    ax.set_xlim(-0.5, 12.5); ax.set_ylim(-0.5, 4); ax.axis('off')
    ax.text(6.0, 3.6, '3-Practice Series  —  From Idea to Working Code', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    b1 = Box(ax, 0.0, 0.5, 3.5, 2.2,
        'Practice 1 (THIS)\nStructured Ideation\n\nInput: Short idea\nOutput: Idea Spec Doc\n+ evidence trail + sources',
        fs=7, fc='#E3F2FD', bold=True, ec='#1565C0')
    b2 = Box(ax, 4.3, 0.5, 3.5, 2.2,
        'Practice 2\nIdea to Plan\n\nInput: Idea Spec Doc\nOutput: Implementation Plan\n+ architecture + stories',
        fs=7, fc='#FFF3E0')
    b3 = Box(ax, 8.6, 0.5, 3.5, 2.2,
        'Practice 3\nPlan to Code\n\nInput: Implementation Plan\nOutput: Working code\n+ tests + deployment',
        fs=7, fc='#E8F5E9')

    arr(ax, b1.right, b2.left, c=NAVY)
    arr(ax, b2.right, b3.left, c=NAVY)

    ax.text(3.0, 0.1, 'Idea Spec Doc', fontsize=6.5, color='#2E7D32', ha='center', weight='bold')
    ax.text(7.3, 0.1, 'Impl. Plan', fontsize=6.5, color='#2E7D32', ha='center', weight='bold')

    save(fig, 'practice_series_v3')

print('Generating Practice 1 v3 diagrams (web research)...')
diagram_pipeline()
diagram_3methods()
diagram_agents()
diagram_evidence()
diagram_setup()
diagram_sources()
diagram_series()
print(f'All 7 diagrams saved to: {OUT}')
