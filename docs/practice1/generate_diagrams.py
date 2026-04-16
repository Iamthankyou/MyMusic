"""
Practice 1 v2: Multi-Agent Structured Ideation (BMAD-inspired)
Diagrams showing real 4-agent pipeline + comparison of 3 methods.
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
        patch = FancyBboxPatch((x, y), w, h, boxstyle="round,pad=0.02",
                                facecolor=fc, edgecolor=ec, lw=lw)
        ax.add_patch(patch)
        ax.text(x + w/2, y + h/2, text, ha='center', va='center',
                fontsize=fs, color=tc, weight='bold' if bold else 'normal',
                fontfamily='sans-serif', linespacing=1.3)
    @property
    def cx(self): return self.x + self.w / 2
    @property
    def cy(self): return self.y + self.h / 2
    @property
    def top(self): return (self.cx, self.y + self.h)
    @property
    def bot(self): return (self.cx, self.y)
    @property
    def left(self): return (self.x, self.cy)
    @property
    def right(self): return (self.x + self.w, self.cy)

def arr(ax, p1, p2, c=DGREY, lw=1.2):
    ax.annotate('', xy=p2, xytext=p1, arrowprops=dict(arrowstyle='->', color=c, lw=lw))

def arr_curved(ax, p1, p2, c=DGREY, lw=1.0, rad=0.3):
    ax.annotate('', xy=p2, xytext=p1,
                arrowprops=dict(arrowstyle='->', color=c, lw=lw,
                                connectionstyle=f'arc3,rad={rad}'))

# ══════════════════════════════════════════════════════════════
# 1. 4-AGENT PIPELINE OVERVIEW
# ══════════════════════════════════════════════════════════════
def diagram_pipeline():
    fig, ax = plt.subplots(figsize=(13, 5.5), facecolor=WHITE)
    ax.set_xlim(-0.5, 13.5); ax.set_ylim(-1.5, 5.5); ax.axis('off')
    ax.text(6.5, 5.0, 'Multi-Agent Ideation Pipeline  —  4 Agents, 4 Documents', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    # User seed
    b_user = Box(ax, 0.0, 1.5, 2.0, 2.0,
        'USER\n\nShort idea:\n"Music app for\nindie discovery\nusing Flutter"', fs=7, fc='#FFF8E1', bold=True)

    # Agent 1
    b_a1 = Box(ax, 2.8, 1.5, 2.3, 2.0,
        'Agent 1\nBusiness Analyst\n\nAsks 10-15\nquestions\n(WHO/WHAT/WHY\nHOW/CONSTRAINTS)', fs=6, fc='#E3F2FD', bold=True)

    # Output 1
    b_o1 = Box(ax, 2.8, 0.0, 2.3, 0.9, 'Requirements\nBrief', fs=7.5, fc='#E8F5E9', ec='#2E7D32')

    # Agent 2
    b_a2 = Box(ax, 5.8, 1.5, 2.3, 2.0,
        'Agent 2\nMarket Researcher\n\nCompetitors\nSCAMPER variants\nRisk analysis', fs=6, fc='#FFF3E0', bold=True)

    # Output 2
    b_o2 = Box(ax, 5.8, 0.0, 2.3, 0.9, 'Market Research\nReport', fs=7.5, fc='#E8F5E9', ec='#2E7D32')

    # Agent 3
    b_a3 = Box(ax, 8.8, 1.5, 2.3, 2.0,
        'Agent 3\nIdea Evaluator\n\nScores 4-5 ideas\non 6 criteria\nPicks winner', fs=6, fc='#FCE4EC', bold=True)

    # Output 3
    b_o3 = Box(ax, 8.8, 0.0, 2.3, 0.9, 'Evaluation\nReport', fs=7.5, fc='#E8F5E9', ec='#2E7D32')

    # Agent 4
    b_a4 = Box(ax, 11.5, 1.5, 1.8, 2.0,
        'Agent 4\nCrystallizer\n\nCompiles all\ninto final\nIdea Spec', fs=6, fc='#E8EAF6', bold=True)

    # Final output
    b_final = Box(ax, 11.0, -0.3, 2.5, 1.0,
        'Idea Specification\nDocument\n(12 sections)', fs=7, fc='#C8E6C9', ec='#2E7D32', bold=True)

    # Arrows: User -> A1
    arr(ax, b_user.right, b_a1.left)
    # A1 -> output 1
    arr(ax, b_a1.bot, b_o1.top, c='#2E7D32')
    # A1 -> A2
    arr(ax, b_a1.right, b_a2.left)
    # Output 1 -> A2 (feeds in)
    arr_curved(ax, b_o1.right, (b_a2.x, b_a2.y + 0.3), c='#2E7D32', rad=-0.2)
    # A2 -> output 2
    arr(ax, b_a2.bot, b_o2.top, c='#2E7D32')
    # A2 -> A3
    arr(ax, b_a2.right, b_a3.left)
    # Output 1,2 -> A3
    arr_curved(ax, b_o2.right, (b_a3.x, b_a3.y + 0.3), c='#2E7D32', rad=-0.2)
    # A3 -> output 3
    arr(ax, b_a3.bot, b_o3.top, c='#2E7D32')
    # A3 -> A4
    arr(ax, b_a3.right, b_a4.left)
    # All outputs -> A4
    arr_curved(ax, b_o3.right, (b_a4.x, b_a4.y + 0.3), c='#2E7D32', rad=-0.2)
    # A4 -> final
    arr(ax, b_a4.bot, b_final.top, c='#2E7D32')

    # Labels
    ax.text(6.5, 4.3, 'Each agent has ONE job  |  Each produces ONE document  |  Documents flow forward', ha='center',
            fontsize=8, color=DGREY, style='italic')

    # "User answers" loop at Agent 1
    arr_curved(ax, (b_a1.x + 0.3, b_a1.y + b_a1.h), (b_user.x + b_user.w, b_user.y + b_user.h - 0.3), c='#1565C0', rad=0.25)
    ax.text(1.8, 4.0, 'asks questions', fontsize=5.5, color='#1565C0', style='italic')
    arr_curved(ax, (b_user.x + b_user.w, b_user.y + 0.5), (b_a1.x, b_a1.y + 0.5), c='#1565C0', rad=0.25)
    ax.text(1.8, 1.2, 'user answers', fontsize=5.5, color='#1565C0', style='italic')

    # Next step arrow
    ax.text(12.2, -0.9, 'Input for\nPractice 2', fontsize=7, color=DGREY, ha='center', weight='bold')

    save(fig, 'multi_agent_pipeline')

# ══════════════════════════════════════════════════════════════
# 2. COMPARISON: 3 METHODS
# ══════════════════════════════════════════════════════════════
def diagram_3methods():
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(14, 5.5), facecolor=WHITE)
    for ax in (ax1, ax2, ax3):
        ax.set_xlim(0, 4.8); ax.set_ylim(0, 5.5); ax.axis('off')
    fig.suptitle('3 Approaches to AI Brainstorming  —  Comparison', fontsize=14,
                 color=NAVY, weight='bold', fontfamily='sans-serif', y=0.97)

    # Method 1: Ad-hoc
    bg1 = FancyBboxPatch((0.1, 0.1), 4.6, 5.0, boxstyle="round,pad=0.04",
                          facecolor='#FDF2F2', edgecolor='#E0E0E0', lw=1)
    ax1.add_patch(bg1)
    ax1.text(2.4, 4.8, 'Method 1\nAd-hoc Prompting', ha='center', fontsize=9,
             color='#999', weight='bold')
    ax1.plot([0.3, 4.5], [4.4, 4.4], color='#E0E0E0', lw=0.8)
    for i, t in enumerate([
        'X  "Make me a music app"',
        'X  AI answers immediately',
        'X  No questions asked',
        'X  1 generic idea produced',
        'X  No competitive analysis',
        'X  No alternatives explored',
        'X  No scoring or evidence',
        'X  Same as everyone else',
        '',
        'Quality: LOW',
        'Time: 5 minutes',
    ]):
        c = '#C62828' if t.startswith('Quality') else '#999'
        ax1.text(0.3, 4.0 - i*0.37, t, fontsize=6.5, color=c, va='center',
                 weight='bold' if 'Quality' in t or 'Time' in t else 'normal')

    # Method 2: Structured single-agent
    bg2 = FancyBboxPatch((0.1, 0.1), 4.6, 5.0, boxstyle="round,pad=0.04",
                          facecolor='#FFFFF0', edgecolor='#E0E0E0', lw=1)
    ax2.add_patch(bg2)
    ax2.text(2.4, 4.8, 'Method 2\nStructured Single Agent', ha='center', fontsize=9,
             color='#F57F17', weight='bold')
    ax2.plot([0.3, 4.5], [4.4, 4.4], color='#E0E0E0', lw=0.8)
    for i, t in enumerate([
        '~  Long prompt with instructions',
        '~  AI asks some questions',
        '~  One agent does everything',
        '~  Context window fills up fast',
        '~  Quality drops as session grows',
        '~  No separation of concerns',
        '~  Bias: creator = judge',
        '~  Better than ad-hoc but fragile',
        '',
        'Quality: MEDIUM',
        'Time: 30-60 minutes',
    ]):
        c = '#F57F17' if t.startswith('Quality') else '#666'
        ax2.text(0.3, 4.0 - i*0.37, t, fontsize=6.5, color=c, va='center',
                 weight='bold' if 'Quality' in t or 'Time' in t else 'normal')

    # Method 3: Multi-agent (THIS)
    bg3 = FancyBboxPatch((0.1, 0.1), 4.6, 5.0, boxstyle="round,pad=0.04",
                          facecolor='#F2F8F2', edgecolor='#E0E0E0', lw=1)
    ax3.add_patch(bg3)
    ax3.text(2.4, 4.8, 'Method 3\nMulti-Agent (This Practice)', ha='center', fontsize=9,
             color=NAVY, weight='bold')
    ax3.plot([0.3, 4.5], [4.4, 4.4], color='#E0E0E0', lw=0.8)
    for i, t in enumerate([
        'V  4 specialized agents',
        'V  Each has ONE clear job',
        'V  Each produces ONE document',
        'V  Fresh context per agent',
        'V  Creator is not the judge',
        'V  Agent 3 plays devil\'s advocate',
        'V  Evidence-backed scoring',
        'V  Reproducible process',
        '',
        'Quality: HIGH',
        'Time: 45-90 minutes',
    ]):
        c = '#2E7D32' if t.startswith('Quality') else BLUE
        ax3.text(0.3, 4.0 - i*0.37, t, fontsize=6.5, color=c, va='center',
                 weight='bold' if 'Quality' in t or 'Time' in t else 'normal')

    fig.subplots_adjust(wspace=0.06)
    save(fig, 'three_methods')

# ══════════════════════════════════════════════════════════════
# 3. EACH AGENT'S JOB
# ══════════════════════════════════════════════════════════════
def diagram_agents_detail():
    fig, ax = plt.subplots(figsize=(12, 7), facecolor=WHITE)
    ax.set_xlim(-0.5, 12.5); ax.set_ylim(-0.5, 7.5); ax.axis('off')
    ax.text(6.0, 7.1, 'What Each Agent Does  —  Separation of Concerns', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    agents = [
        (0.2, 4.0, 'Agent 1: Business Analyst', '#E3F2FD',
         'INPUT: User\'s short idea',
         'JOB: Ask 10-15 questions\n(WHO / WHAT / WHY / HOW / CONSTRAINTS)\nMulti-round dialogue until clear',
         'OUTPUT: Requirements Brief\n(problem, target user, features,\nconstraints, success metrics)',
         'RULE: NEVER propose solutions'),
        (3.2, 4.0, 'Agent 2: Market Researcher', '#FFF3E0',
         'INPUT: Requirements Brief',
         'JOB: Find 4-6 competitor apps\nRun SCAMPER analysis (7 variants)\nAssess risks (tech/market/legal)',
         'OUTPUT: Market Research Report\n(competitor table, SCAMPER,\ngap analysis, risk matrix)',
         'RULE: Use REAL app names'),
        (6.2, 4.0, 'Agent 3: Idea Evaluator', '#FCE4EC',
         'INPUT: Brief + Research Report',
         'JOB: Score top 4-5 ideas\non 6 criteria (1-5 each)\nPlay devil\'s advocate',
         'OUTPUT: Evaluation Report\n(scoring matrix, evidence\nper score, winner + why)',
         'RULE: NEVER give all 5s'),
        (9.2, 4.0, 'Agent 4: Crystallizer', '#E8EAF6',
         'INPUT: All 3 documents',
         'JOB: Compile into ONE\nIdea Specification Document\nwith 12 structured sections',
         'OUTPUT: Idea Spec Document\n(ready for Practice 2:\nIdea -> Detailed Plan)',
         'RULE: Do NOT add new info'),
    ]

    for x, y, title, fc, inp, job, out, rule in agents:
        # Title
        Box(ax, x, y + 2.0, 2.7, 0.6, title, fs=7.5, fc=fc, bold=True)
        # Input
        Box(ax, x, y + 1.3, 2.7, 0.6, inp, fs=6, fc='#F5F5F5')
        # Job
        Box(ax, x, y, 2.7, 1.2, job, fs=5.5, fc=WHITE)
        # Output
        Box(ax, x, y - 1.2, 2.7, 0.6, out, fs=5.5, fc='#E8F5E9', ec='#2E7D32')
        # Rule
        Box(ax, x, y - 2.2, 2.7, 0.5, rule, fs=5.5, fc='#FFEBEE', ec='#C62828', tc='#C62828')

        # Arrows
        arr(ax, (x+1.35, y+2.0), (x+1.35, y+1.9))
        arr(ax, (x+1.35, y+1.3), (x+1.35, y+1.2))
        arr(ax, (x+1.35, y), (x+1.35, y-0.1), c='#2E7D32')

    # Horizontal flow arrows
    for i in range(3):
        x1 = agents[i][0] + 2.7
        x2 = agents[i+1][0]
        y_mid = 5.3
        arr(ax, (x1, y_mid), (x2, y_mid), c=NAVY)

    save(fig, 'agents_detail')

# ══════════════════════════════════════════════════════════════
# 4. CURSOR SETUP
# ══════════════════════════════════════════════════════════════
def diagram_cursor_setup():
    fig, ax = plt.subplots(figsize=(10, 6), facecolor=WHITE)
    ax.set_xlim(-0.5, 10.5); ax.set_ylim(-0.5, 6.5); ax.axis('off')
    ax.text(5.0, 6.1, 'How to Set Up in Cursor IDE', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    # Project structure
    Box(ax, 0.3, 3.5, 4.5, 2.2,
        'Project folder structure:\n\n'
        '.cursor/rules/\n'
        '  01-business-analyst.mdc\n'
        '  02-market-researcher.mdc\n'
        '  03-idea-evaluator.mdc\n'
        '  04-idea-crystallizer.mdc', fs=7, fc='#F5F5F5', tc=BLUE)

    # Steps
    steps = [
        (5.5, 5.0, 'Step 1: Copy 4 agent .mdc files\ninto .cursor/rules/', '#E3F2FD'),
        (5.5, 3.8, 'Step 2: Open Cursor Agent Mode\nSelect "01-business-analyst" rule', '#FFF3E0'),
        (5.5, 2.6, 'Step 3: Give your idea, answer\nquestions, get Requirements Brief', '#FCE4EC'),
        (5.5, 1.4, 'Step 4: New chat, select next\nagent rule, paste previous output', '#E8EAF6'),
        (5.5, 0.2, 'Step 5: Repeat until Agent 4\nproduces final Idea Spec', '#E8F5E9'),
    ]
    sboxes = []
    for x, y, text, fc in steps:
        b = Box(ax, x, y, 4.5, 0.8, text, fs=7, fc=fc)
        sboxes.append(b)
    for i in range(len(sboxes)-1):
        arr(ax, sboxes[i].bot, sboxes[i+1].top)

    # Key insight
    Box(ax, 0.3, 0.2, 4.5, 1.5,
        'WHY separate chats?\n\n'
        '- Fresh context window per agent\n'
        '- No role confusion\n'
        '- Each agent stays focused\n'
        '- Documents are the "handoff"', fs=6.5, fc='#FFF8E1', bold=True)

    # Arrow from structure to step 1
    arr(ax, (4.8, 4.6), (5.5, 5.4))

    save(fig, 'cursor_setup')

# ══════════════════════════════════════════════════════════════
# 5. WHY MULTI-AGENT WORKS
# ══════════════════════════════════════════════════════════════
def diagram_why_multi():
    fig, ax = plt.subplots(figsize=(10, 5), facecolor=WHITE)
    ax.set_xlim(-0.5, 10.5); ax.set_ylim(-0.5, 5.5); ax.axis('off')
    ax.text(5.0, 5.1, 'Why Multi-Agent Beats Single Agent', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    problems = [
        ('Context Window\nOverflow', 'Single agent fills up\ncontext with Q&A +\nresearch + scoring', 'Each agent gets a\nfresh context window\nwith only its inputs', '#FFEBEE', '#E8F5E9'),
        ('Creator = Judge\nBias', 'Same agent creates\nideas AND evaluates\nthem (self-bias)', 'Agent 2 creates,\nAgent 3 evaluates\n(separation of roles)', '#FFEBEE', '#E8F5E9'),
        ('Role Confusion', 'AI mixes research\nwith coding with\ndesign in one session', 'Each agent has\nONE job and strict\nrules about scope', '#FFEBEE', '#E8F5E9'),
        ('Reproducibility', 'Different prompts\n= different results\neach time', '4 fixed agent configs\n= same process\nevery project', '#FFEBEE', '#E8F5E9'),
    ]

    for i, (title, prob, sol, pcol, scol) in enumerate(problems):
        x = 0.2 + i * 2.6
        Box(ax, x, 3.5, 2.2, 0.8, title, fs=7, fc='#F5F5F5', bold=True)
        Box(ax, x, 2.0, 2.2, 1.2, prob, fs=6, fc=pcol, tc='#C62828')
        Box(ax, x, 0.3, 2.2, 1.2, sol, fs=6, fc=scol, tc='#2E7D32')
        arr(ax, (x+1.1, 2.0), (x+1.1, 1.5), c='#2E7D32')
        ax.text(x+1.1, 1.75, 'Fixed by', fontsize=5, color=DGREY, ha='center')

    save(fig, 'why_multi_agent')

# ══════════════════════════════════════════════════════════════
# 6. OUTPUT DOCUMENT
# ══════════════════════════════════════════════════════════════
def diagram_output():
    fig, ax = plt.subplots(figsize=(10, 6), facecolor=WHITE)
    ax.set_xlim(-0.5, 10.5); ax.set_ylim(-0.5, 6.5); ax.axis('off')
    ax.text(5.0, 6.1, 'Final Output: Idea Specification Document (12 Sections)', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    sections = [
        (0.3, 5.0, '1. Problem Statement', '#E8EDF2', 'BA'),
        (0.3, 4.1, '2. Core Idea', '#FFF8E1', 'Cryst.'),
        (0.3, 3.2, '3. Why This Idea Won', '#FCE4EC', 'Eval.'),
        (0.3, 2.3, '4. Target User', '#E8EDF2', 'BA'),
        (0.3, 1.4, '5. Feature Map (must/nice/out)', '#FFF8E1', 'BA'),
        (0.3, 0.5, '6. User Journey', '#E8EDF2', 'Cryst.'),
        (5.5, 5.0, '7. Competitive Landscape', '#FFF3E0', 'MR'),
        (5.5, 4.1, '8. Tech Stack + Rationale', '#E8EDF2', 'Cryst.'),
        (5.5, 3.2, '9. SCAMPER Variants Explored', '#FFF3E0', 'MR'),
        (5.5, 2.3, '10. Risk Analysis', '#FFF3E0', 'MR'),
        (5.5, 1.4, '11. Assumptions to Validate', '#FCE4EC', 'Eval.'),
        (5.5, 0.5, '12. Next: Practice 2 (Planning)', '#C8E6C9', 'Cryst.'),
    ]
    for x, y, text, fc, src in sections:
        Box(ax, x, y, 4.0, 0.7, text, fs=7.5, fc=fc,
            bold=('Won' in text or 'Next' in text))
        ax.text(x + 4.2, y + 0.35, f'[{src}]', fontsize=5.5, color=DGREY, va='center')

    ax.text(5.0, -0.2, 'Each section traced back to its source agent (BA / MR / Eval. / Cryst.)',
            ha='center', fontsize=7.5, color=DGREY, style='italic')

    save(fig, 'output_doc')

# ══════════════════════════════════════════════════════════════
# 7. 3-PRACTICE SERIES OVERVIEW
# ══════════════════════════════════════════════════════════════
def diagram_series():
    fig, ax = plt.subplots(figsize=(12, 4), facecolor=WHITE)
    ax.set_xlim(-0.5, 12.5); ax.set_ylim(-0.5, 4); ax.axis('off')
    ax.text(6.0, 3.6, '3-Practice Series  —  From Idea to Working Code', ha='center',
            fontsize=14, color=NAVY, weight='bold', fontfamily='sans-serif')

    b1 = Box(ax, 0.0, 0.5, 3.5, 2.2,
        'Practice 1 (THIS)\nStructured Ideation\n\nInput: Short idea\nOutput: Idea Spec Doc\nAgents: BA, MR, Eval, Cryst',
        fs=7, fc='#E3F2FD', bold=True, ec='#1565C0')
    b2 = Box(ax, 4.3, 0.5, 3.5, 2.2,
        'Practice 2\nIdea to Plan\n\nInput: Idea Spec Doc\nOutput: Implementation Plan\nAgents: Architect, PM, UX',
        fs=7, fc='#FFF3E0')
    b3 = Box(ax, 8.6, 0.5, 3.5, 2.2,
        'Practice 3\nPlan to Code\n\nInput: Implementation Plan\nOutput: Working code\nAgents: Dev, QA, Reviewer',
        fs=7, fc='#E8F5E9')

    arr(ax, b1.right, b2.left, c=NAVY)
    arr(ax, b2.right, b3.left, c=NAVY)

    ax.text(3.0, 0.1, 'Idea Spec Doc', fontsize=6.5, color='#2E7D32', ha='center', weight='bold')
    ax.text(7.3, 0.1, 'Impl. Plan', fontsize=6.5, color='#2E7D32', ha='center', weight='bold')

    save(fig, 'practice_series')

print('Generating Practice 1 v2 diagrams (multi-agent)...')
diagram_pipeline()
diagram_3methods()
diagram_agents_detail()
diagram_cursor_setup()
diagram_why_multi()
diagram_output()
diagram_series()
print(f'All 7 diagrams saved to: {OUT}')
