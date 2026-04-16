"""
Practice 1 v2: Multi-Agent Structured Ideation
Word report — based on BMAD method, with actual Cursor agent configs.
"""
import os
from docx import Document
from docx.shared import Pt, RGBColor, Inches, Cm
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT, WD_ALIGN_VERTICAL
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

IMG_DIR = r'd:\PROJECT\MyMusic\docs\practice1'
IMG = {k: os.path.join(IMG_DIR, f'{k}.png') for k in
       ['multi_agent_pipeline','three_methods','agents_detail',
        'cursor_setup','why_multi_agent','output_doc','practice_series']}

doc = Document()
sec = doc.sections[0]
sec.page_width = Inches(8.27); sec.page_height = Inches(11.69)
sec.left_margin = Cm(2); sec.right_margin = Cm(2)
sec.top_margin = Cm(2); sec.bottom_margin = Cm(2)

def shade(cell, hx):
    tc = cell._tc; pr = tc.get_or_add_tcPr()
    s = OxmlElement('w:shd')
    s.set(qn('w:val'),'clear'); s.set(qn('w:color'),'auto'); s.set(qn('w:fill'),hx)
    pr.append(s)

def run(p, text, bold=False, italic=False, sz=10, color=None, font='Calibri'):
    r = p.add_run(text); r.bold=bold; r.italic=italic; r.font.size=Pt(sz); r.font.name=font
    if color: r.font.color.rgb = RGBColor(*bytes.fromhex(color))
    return r

def lcell(cell, text):
    shade(cell, '2E4057')
    p = cell.paragraphs[0]; p.alignment = WD_ALIGN_PARAGRAPH.LEFT
    r = p.add_run(text); r.bold=True; r.font.size=Pt(10); r.font.name='Calibri'
    r.font.color.rgb = RGBColor(255,255,255)
    cell.vertical_alignment = WD_ALIGN_VERTICAL.CENTER

def vcell(cell, items):
    first = True
    for it in items:
        p = cell.paragraphs[0] if first else cell.add_paragraph(); first=False
        p.alignment = WD_ALIGN_PARAGRAPH.LEFT
        run(p, it.get('text',''), bold=it.get('bold',False), sz=it.get('size',10),
            color=it.get('color'))

def heading(text, level=1):
    p = doc.add_paragraph(); r = p.add_run(text); r.bold=True; r.font.name='Calibri'
    if level==1: r.font.size=Pt(14); r.font.color.rgb=RGBColor(0x2E,0x40,0x57)
    elif level==2: r.font.size=Pt(12); r.font.color.rgb=RGBColor(0x48,0x6F,0x8C)
    else: r.font.size=Pt(10.5); r.font.color.rgb=RGBColor(0x1A,0x5C,0x8B)

def body(text, bullet=False, bold=False, sz=10):
    p = doc.add_paragraph()
    if bullet: p.style = doc.styles['List Bullet']
    run(p, text, bold=bold, sz=sz); return p

def code(text):
    p = doc.add_paragraph()
    r = p.add_run(text); r.font.name='Courier New'; r.font.size=Pt(8.5)
    return p

def img(key, caption=None, w=6.0):
    path = IMG.get(key)
    if path and os.path.exists(path):
        p = doc.add_paragraph(); p.alignment = WD_ALIGN_PARAGRAPH.CENTER
        p.add_run().add_picture(path, width=Inches(w))
        if caption:
            c = doc.add_paragraph(); c.alignment = WD_ALIGN_PARAGRAPH.CENTER
            r = c.add_run(caption); r.italic=True; r.font.size=Pt(8.5)
            r.font.color.rgb=RGBColor(0x66,0x66,0x66); r.font.name='Calibri'

def info_table(rows):
    t = doc.add_table(rows=len(rows), cols=2); t.style='Table Grid'
    t.alignment = WD_TABLE_ALIGNMENT.CENTER
    for i,(l,c) in enumerate(rows):
        row=t.rows[i]; row.cells[0].width=Cm(4.5); row.cells[1].width=Cm(13)
        lcell(row.cells[0],l); vcell(row.cells[1],c)
    doc.add_paragraph()

def table(headers, data):
    nc=len(headers); t=doc.add_table(rows=1+len(data), cols=nc)
    t.style='Table Grid'; t.alignment=WD_TABLE_ALIGNMENT.CENTER
    for i,h in enumerate(headers):
        shade(t.rows[0].cells[i],'2E4057')
        r=t.rows[0].cells[i].paragraphs[0].add_run(h)
        r.bold=True; r.font.size=Pt(9); r.font.name='Calibri'
        r.font.color.rgb=RGBColor(255,255,255)
    for ri,rd in enumerate(data):
        bg = 'FFFFFF' if ri%2==0 else 'EEF2F7'
        for ci,ct in enumerate(rd):
            shade(t.rows[ri+1].cells[ci], bg)
            r=t.rows[ri+1].cells[ci].paragraphs[0].add_run(ct)
            r.font.size=Pt(9); r.font.name='Calibri'
    doc.add_paragraph()

# ══════════════════════════════════════════════════════════════
#  TITLE PAGE
# ══════════════════════════════════════════════════════════════
doc.add_paragraph(); doc.add_paragraph()
p = doc.add_paragraph(); p.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = p.add_run('AI Practice — Best Practice Guide'); r.bold=True; r.font.size=Pt(20)
r.font.name='Calibri'; r.font.color.rgb=RGBColor(0x2E,0x40,0x57)

p = doc.add_paragraph(); p.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = p.add_run('Practice 1 of 3'); r.font.size=Pt(14); r.font.name='Calibri'
r.font.color.rgb=RGBColor(0x48,0x6F,0x8C); r.bold=True

p = doc.add_paragraph(); p.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = p.add_run('Multi-Agent Structured Ideation\nFrom a Short Idea to a Validated Concept\nUsing 4 Specialized AI Agents in Cursor')
r.font.size=Pt(12); r.font.name='Calibri'; r.font.color.rgb=RGBColor(0x48,0x6F,0x8C)

doc.add_paragraph()
p = doc.add_paragraph(); p.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = p.add_run('Submitted: 2026-04-16  |  Video Editor  |  Duy LQ')
r.font.size=Pt(10); r.font.name='Calibri'; r.font.color.rgb=RGBColor(0x99,0x99,0x99)

doc.add_paragraph()
img('practice_series', 'This is Practice 1 of a 3-part series: Ideation -> Planning -> Code', 5.5)
doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  INFO TABLE
# ══════════════════════════════════════════════════════════════
info_table([
    ("Practice",           [{'text':'1 of 3: Multi-Agent Structured Ideation', 'bold':True, 'size':11}]),
    ("Method Based On",    [{'text':'BMAD Method (Breakthrough Method for Agile AI-Driven Development)', 'bold':True},
                             {'text':'Open-source multi-agent framework — github.com/bmad-code-org/BMAD-METHOD'}]),
    ("Key Innovation",     [{'text':'4 specialized agents, each with ONE job and ONE output document', 'bold':True},
                             {'text':'Separation of concerns: the agent who creates ideas is NOT the one who evaluates them'}]),
    ("Tool",               [{'text':'Cursor IDE (Agent Mode + .cursor/rules/ files)', 'bold':True},
                             {'text':'Also works with: Claude, ChatGPT, Windsurf, any AI with system prompts'}]),
    ("Input",              [{'text':'A short initial idea (1-3 sentences)'}]),
    ("Output",             [{'text':'Idea Specification Document (12 sections, evidence-backed)', 'bold':True}]),
    ("Time",               [{'text':'45-90 minutes total (4 separate AI sessions)'}]),
    ("Files Included",     [{'text':'4 ready-to-use Cursor agent config files (.mdc)'}]),
])

# ══════════════════════════════════════════════════════════════
#  1. THE PROBLEM
# ══════════════════════════════════════════════════════════════
heading('1. Why Normal AI Brainstorming Doesn\'t Work', 1)
body('There are 3 levels of using AI for brainstorming. Most people stop at Level 1:')

img('three_methods', 'Figure 1: Three approaches — Ad-hoc, Structured Single Agent, Multi-Agent', 6.2)

table(
    ['', 'Ad-hoc\n(Level 1)', 'Single Agent\n(Level 2)', 'Multi-Agent\n(Level 3 — This)'],
    [
        ['Process',           '"Make me an app"',    'Long prompt with instructions', '4 agents, 4 sessions, 4 documents'],
        ['Questions asked?',  'None',                'Some (if prompted)',            '10-15 structured questions by Agent 1'],
        ['Competitive analysis?', 'No',              'Maybe (if you ask)',            'Always (Agent 2\'s entire job)'],
        ['Who evaluates?',    'Same agent',          'Same agent (bias)',             'Agent 3 = dedicated critical judge'],
        ['Evidence?',          'None',               'Sometimes',                     'Every score requires 1-sentence proof'],
        ['Reproducible?',     'No',                  'Depends on prompt',             'Yes — same 4 config files every time'],
        ['Context overflow?', 'N/A',                 'Yes (long sessions)',           'No — fresh context per agent'],
        ['Quality',            'Low',                'Medium',                         'High'],
    ]
)

heading('Why Multi-Agent Specifically?', 2)
img('why_multi_agent', 'Figure 2: 4 problems with single-agent approach and how multi-agent fixes each', 5.5)

body('The core insight from BMAD Method: in real software teams, '
     'the analyst is NOT the architect, the architect is NOT the developer, '
     'and the developer is NOT the QA tester. Each role has a specific job. '
     'The same principle applies to AI agents.', bold=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  2. THE 4-AGENT PIPELINE
# ══════════════════════════════════════════════════════════════
heading('2. The 4-Agent Ideation Pipeline', 1)
body('Each agent has ONE job, produces ONE document, and hands it to the next agent.')

img('multi_agent_pipeline', 'Figure 3: The 4-agent pipeline — each agent produces one document that flows forward', 6.2)

img('agents_detail', 'Figure 4: What each agent does — inputs, job description, outputs, and hard rules', 6.0)

table(
    ['Agent', 'Role', 'Input', 'Output', 'Key Rule'],
    [
        ['1. Business Analyst',  'Requirements discovery', 'User\'s short idea', 'Requirements Brief',    'NEVER propose solutions'],
        ['2. Market Researcher', 'Competitive + SCAMPER',  'Requirements Brief',  'Market Research Report', 'Use REAL app names'],
        ['3. Idea Evaluator',    'Critical scoring',       'Brief + Research',    'Evaluation Report',      'NEVER give all 5s'],
        ['4. Idea Crystallizer', 'Compile final spec',     'All 3 documents',     'Idea Specification Doc', 'Do NOT add new info'],
    ]
)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  3. AGENT 1: BUSINESS ANALYST
# ══════════════════════════════════════════════════════════════
heading('3. Agent 1: Business Analyst — "Ask, Don\'t Assume"', 1)

body('This agent\'s only job is to ask questions and produce a Requirements Brief.', bold=True)

heading('What Questions It Asks', 2)
table(
    ['Category', 'Example Questions', 'Why It Matters'],
    [
        ['WHO\n(Target)',  'Who is the target user? Age/demographics?\nTech comfort level? Willing to pay?',
         'Defines UX complexity,\npricing, feature set'],
        ['WHAT\n(Features)', 'Core features? Platform (web/mobile)?\nOffline mode? Social features?',
         'Scopes the MVP vs\nfull product'],
        ['WHY\n(Motivation)', 'What problem does this solve?\nWhat apps do you NOT like? Why?\nWhat makes your idea different?',
         'Validates the idea\nhas a reason to exist'],
        ['HOW\n(Technical)', 'Budget? Timeline? Team size?\nScalability? Data storage needs?',
         'Determines architecture\n+ feasibility'],
        ['CONSTRAINTS', 'Regulatory requirements?\nTech restrictions? Existing infrastructure?',
         'Catches blockers before\ndesign begins'],
    ]
)

heading('Multi-Round Dialogue', 2)
body('The agent doesn\'t stop after one round. It works like a real BA:')
body('Round 1: Asks 10-15 initial questions across all 5 categories', bullet=True)
body('User answers (can be brief)', bullet=True)
body('Round 2: Follows up on vague/broad answers with deeper questions', bullet=True)
body('Round 3: Confirms understanding, asks "anything I missed?"', bullet=True)
body('Only then does it produce the Requirements Brief.', bold=True)

heading('Output: Requirements Brief', 2)
body('A structured markdown document with these sections:')
body('Problem Statement — what pain point, for whom', bullet=True)
body('Target User Profile — demographics, tech level, usage frequency', bullet=True)
body('Must-Have Features (MVP) — what must be in v1', bullet=True)
body('Nice-to-Have Features (v2) — what can wait', bullet=True)
body('Out-of-Scope — explicitly NOT building (prevents scope creep)', bullet=True)
body('Constraints — platform, budget, timeline, team, regulatory', bullet=True)
body('Success Metrics — how do we know this product is successful', bullet=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  4. AGENT 2: MARKET RESEARCHER
# ══════════════════════════════════════════════════════════════
heading('4. Agent 2: Market Researcher — "What Already Exists?"', 1)

body('This agent receives the Requirements Brief and researches the real market.', bold=True)
body('It produces 4 deliverables:')

heading('4.1  Competitive Analysis Table', 2)
body('Finds 4-6 existing apps/products that solve a similar problem:')
table(
    ['App', 'Platform', 'Strengths', 'Weaknesses', 'Pricing', 'Users (est.)'],
    [
        ['Spotify',    'All',   'Huge library, great algo', 'Favors mainstream', 'Free/Premium $10/mo', '600M+'],
        ['Bandcamp',   'Web',   'Artist-first, direct pay', 'Poor UX, no social', 'Free (artist pays %)', '~8M'],
        ['SoundCloud', 'All',   'User uploads, community',  'Cluttered, ads',     'Free/$5/$16',          '~175M'],
        ['Tidal',      'All',   'Hi-fi audio, artist focus', 'High price, small library', '$11/$22',       '~5M'],
    ]
)

heading('4.2  SCAMPER Variants', 2)
body('A creativity framework applied to generate 7 alternative approaches:')
table(
    ['Letter', 'Technique', 'Example Variant'],
    [
        ['S', 'Substitute',  'Replace streaming with podcast-style episodes about indie artists'],
        ['C', 'Combine',     'Music + Instagram-style story feature for artists'],
        ['A', 'Adapt',       'Adapt TikTok\'s algorithm for music discovery'],
        ['M', 'Modify',      'Real-time collaborative playlists (like Google Docs)'],
        ['P', 'Put to other uses', 'Engine also discovers indie film/game soundtracks'],
        ['E', 'Eliminate',   'Remove the feed — only curated weekly "drops"'],
        ['R', 'Reverse',     'Artists pitch to listeners instead of listeners searching'],
    ]
)

heading('4.3  Risk Assessment', 2)
body('Technical, Market, Legal, and Dependency risks — each with mitigation.')

heading('4.4  Market Gap Analysis', 2)
body('The key question: "What do ALL competitors miss that this idea could fill?"')

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  5. AGENT 3: IDEA EVALUATOR
# ══════════════════════════════════════════════════════════════
heading('5. Agent 3: Idea Evaluator — "Devil\'s Advocate"', 1)

body('This agent is the critical judge. Its job is to be harsh, not positive. '
     'It receives both the Requirements Brief AND the Market Research Report.', bold=True)

heading('Why a Separate Evaluator?', 2)
body('If the same agent creates ideas AND judges them, it has creator bias — '
     'it will always favor its own ideas. By making Agent 3 a separate session '
     'with a separate context, it evaluates objectively based only on the documents.', bold=True)

heading('Scoring Matrix', 2)
table(
    ['Criterion', 'Score', 'What It Measures'],
    [
        ['Feasibility',     '1-5', 'Can we build this with available tech/APIs?'],
        ['Market Demand',   '1-5', 'Is there evidence of user need?'],
        ['Uniqueness',      '1-5', 'How different from existing solutions?'],
        ['Time to MVP',     '1-5', 'How quickly can a working v1 be built?'],
        ['Scalability',     '1-5', 'Can this grow beyond initial users?'],
        ['Cost Efficiency', '1-5', 'How expensive to build and operate?'],
    ]
)

heading('Example Output', 2)
table(
    ['Idea', 'Feas.', 'Market', 'Unique', 'Time', 'Scale', 'Cost', 'TOTAL'],
    [
        ['Original: Standard music app',          '5', '4', '1', '4', '4', '4', '22'],
        ['SCAMPER-C: Music + artist stories',     '4', '3', '4', '3', '3', '3', '20'],
        ['SCAMPER-E: Curated weekly drops only',  '5', '3', '4', '5', '4', '5', '26 (Winner)'],
        ['Gap: Artist-pitch-to-listener',         '3', '2', '5', '2', '2', '3', '17'],
    ]
)

body('For every single score, the evaluator must write exactly 1 sentence of evidence.', bold=True)
body('Example: "Feasibility: 5 — No real-time streaming backend needed; weekly batch uploads '
     'reduce both server cost and technical complexity to near-zero for MVP."')

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  6. AGENT 4: CRYSTALLIZER
# ══════════════════════════════════════════════════════════════
heading('6. Agent 4: Idea Crystallizer — "Compile & Finalize"', 1)

body('The final agent receives ALL 3 previous documents and synthesizes them '
     'into ONE Idea Specification Document with 12 sections.', bold=True)

img('output_doc', 'Figure 5: The 12-section Idea Specification Document — each section traced to its source agent', 5.5)

body('Traceability: each section comes from a specific agent:')
table(
    ['Section', 'Source Agent', 'Content'],
    [
        ['1. Problem Statement',         'Business Analyst',    'What pain point, for whom, evidence'],
        ['2. Core Idea',                  'Crystallizer',        'One crisp paragraph describing the concept'],
        ['3. Why This Idea Won',          'Idea Evaluator',      'Scoring summary, why this beat alternatives'],
        ['4. Target User',               'Business Analyst',    'Demographics, tech level, willingness to pay'],
        ['5. Feature Map',                'Business Analyst',    'Must-have (MVP) / nice-to-have / out-of-scope'],
        ['6. User Journey',              'Crystallizer',        'Discovery -> onboard -> core -> retain -> monetize'],
        ['7. Competitive Landscape',      'Market Researcher',   'Comparison table of 4-6 competitors'],
        ['8. Tech Stack + Rationale',     'Crystallizer',        'Frontend/backend/DB/API choices with reasoning'],
        ['9. SCAMPER Variants Explored',  'Market Researcher',   'Alternative approaches, why runner-up wasn\'t chosen'],
        ['10. Risk Analysis',             'Market Researcher',   'Technical, market, legal, dependency risks'],
        ['11. Assumptions to Validate',   'Idea Evaluator',      'Hypotheses that need to be tested'],
        ['12. Next Step',                 'Crystallizer',        '"This doc is the input for Practice 2: Planning"'],
    ]
)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  7. HOW TO SET UP IN CURSOR
# ══════════════════════════════════════════════════════════════
heading('7. How to Set Up in Cursor (Step-by-Step)', 1)

img('cursor_setup', 'Figure 6: Cursor setup — copy 4 .mdc files, then run 4 separate chat sessions', 5.5)

heading('Step 1: Copy Agent Files', 2)
body('Copy the 4 .mdc files from practice1/agents/ into your project\'s .cursor/rules/ folder:')
code('your-project/\n'
     '  .cursor/\n'
     '    rules/\n'
     '      01-business-analyst.mdc\n'
     '      02-market-researcher.mdc\n'
     '      03-idea-evaluator.mdc\n'
     '      04-idea-crystallizer.mdc')

heading('Step 2: Agent 1 — Business Analyst', 2)
body('Open a NEW Cursor Agent Mode chat. Select the "01-business-analyst" rule.')
body('Type your seed idea:')
code('I have an idea for a project. Here it is:\n'
     '"I want to make a music streaming app using Flutter + Firebase\n'
     'that helps young people discover independent artists.\n'
     'Something like Spotify but for indie music."')
body('The agent will ask 10-15 questions. Answer them. '
     'After 2-3 rounds, it produces a Requirements Brief.', bold=True)
body('Save the Requirements Brief to a file (e.g., docs/requirements-brief.md).', bold=True)

heading('Step 3: Agent 2 — Market Researcher', 2)
body('Open a NEW chat. Select "02-market-researcher" rule.')
body('Paste the Requirements Brief and say:')
code('"Here is the Requirements Brief from our Business Analyst.\n'
     'Please run your full analysis: competitors, SCAMPER, risks, gaps."')
body('Save the Market Research Report to a file.', bold=True)

heading('Step 4: Agent 3 — Idea Evaluator', 2)
body('Open a NEW chat. Select "03-idea-evaluator" rule.')
body('Paste BOTH the Requirements Brief AND the Market Research Report:')
code('"Here are 2 documents from previous agents.\n'
     '[Requirements Brief]\n...\n[Market Research Report]\n...\n'
     'Please score the top 4-5 ideas and recommend a winner."')
body('Save the Evaluation Report.', bold=True)

heading('Step 5: Agent 4 — Crystallizer', 2)
body('Open a NEW chat. Select "04-idea-crystallizer" rule.')
body('Paste ALL 3 previous documents:')
code('"Here are 3 documents from the previous agents.\n'
     '[Requirements Brief]\n...\n[Market Research Report]\n...\n[Evaluation Report]\n...\n'
     'Please compile into the final Idea Specification Document."')
body('The output is your final Idea Spec — the input for Practice 2 (Planning).', bold=True)

heading('Why Separate Chats?', 2)
body('Fresh context window: each agent gets only what it needs, not 50 pages of history', bullet=True)
body('No role confusion: Agent 3 can\'t see Agent 1\'s thought process, only its output', bullet=True)
body('Reproducible: same 4 file configs + same process = consistent quality every project', bullet=True)
body('Debuggable: if the evaluation is bad, you only re-run Agent 3, not everything', bullet=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  8. ALSO WORKS WITH
# ══════════════════════════════════════════════════════════════
heading('8. Also Works With Other AI Tools', 1)
body('The 4 agent configs are Cursor .mdc files, but the method works anywhere '
     'that supports system prompts or separate sessions:')
table(
    ['Tool', 'How to Use This Method'],
    [
        ['Cursor IDE',    'Copy .mdc files to .cursor/rules/ (native integration, recommended)'],
        ['ChatGPT',       'Paste the agent\'s system prompt as the first message in each NEW conversation'],
        ['Claude',         'Use Projects feature: create 4 projects, each with one agent prompt'],
        ['Windsurf',      'Use .windsurfrules files (similar to Cursor)'],
        ['Gemini',        'Copy the agent prompt as first message in each new chat'],
        ['Claude Code',   'Use /persona command or CLAUDE.md files'],
    ]
)
body('The key principle is the same everywhere: 4 separate sessions, '
     '4 separate agent prompts, documents as handoff between sessions.', bold=True)

# ══════════════════════════════════════════════════════════════
#  9. TIPS
# ══════════════════════════════════════════════════════════════
heading('9. Tips & Best Practices', 1)

heading('Do', 2)
for tip in [
    'Keep the seed SHORT (1-3 sentences). The BA agent\'s job is to expand it.',
    'Answer Agent 1\'s questions honestly — short answers are fine.',
    'Save each agent\'s output as a separate file before starting the next agent.',
    'If Agent 3\'s scoring seems off, challenge it: "Why did you score X as 4?"',
    'The SCAMPER variants from Agent 2 often produce the winning idea — don\'t skip them.',
    'Use the Idea Spec Document from Agent 4 as the INPUT for Practice 2 (Planning).',
]:
    body(tip, bullet=True)

heading('Don\'t', 2)
for tip in [
    'Don\'t put all 4 agents in ONE chat session (context overflow, role confusion).',
    'Don\'t skip Agent 1 (BA) — it is the most important. Bad requirements = bad everything.',
    'Don\'t accept Agent 3\'s first scoring without reading the evidence sentences.',
    'Don\'t modify Agent 4\'s output — if something is wrong, re-run the responsible agent.',
    'Don\'t use this for trivial tasks — if you already know exactly what to build, skip to Practice 2.',
]:
    body(tip, bullet=True)

# ══════════════════════════════════════════════════════════════
#  10. WHAT'S NEXT
# ══════════════════════════════════════════════════════════════
heading('10. What\'s Next: Practice 2 & 3', 1)
table(
    ['Practice', 'Name', 'Input', 'Output', 'Agents'],
    [
        ['1 (this)', 'Structured Ideation',  'Short idea (1-3 sentences)',   'Idea Specification Document',  'BA, Market Researcher, Evaluator, Crystallizer'],
        ['2 (next)', 'Idea to Plan',         'Idea Specification Document',  'Detailed Implementation Plan', 'Architect, PM, UX Designer'],
        ['3 (last)', 'Plan to Code',          'Implementation Plan',          'Working code',                  'Developer, QA, Code Reviewer'],
    ]
)
body('Each practice uses different specialized agents. '
     'The output of one practice is the input of the next. '
     'This is how BMAD method works: a chain of specialized agents producing '
     'documents that build on each other.', bold=True)

# ══════════════════════════════════════════════════════════════
#  APPENDIX: AGENT FILES
# ══════════════════════════════════════════════════════════════
heading('Appendix: Agent Configuration Files', 1)
body('The 4 .mdc files are included in practice1/agents/. '
     'Each file is a Cursor MDC rule file with these sections:')
table(
    ['File', 'Lines', 'Key Sections'],
    [
        ['01-business-analyst.mdc',   '~55', 'Role, Job (5 question categories), Output format (Requirements Brief), Rules'],
        ['02-market-researcher.mdc',  '~55', 'Role, Job (4 deliverables: competitors, SCAMPER, risks, gaps), Rules'],
        ['03-idea-evaluator.mdc',     '~60', 'Role, Job (scoring matrix, evidence), Output format (Evaluation Report), Rules'],
        ['04-idea-crystallizer.mdc',  '~50', 'Role, Job (compile), Output format (12-section Idea Spec), Rules'],
    ]
)
body('These files are ready to use — copy them to .cursor/rules/ and start immediately.')

# FOOTER
doc.add_paragraph()
ft = doc.add_paragraph(); ft.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = ft.add_run('Report generated: 2026-04-16  |  Practice 1 of 3: Multi-Agent Ideation  |  Video Editor')
r.font.size=Pt(8); r.font.color.rgb=RGBColor(0x99,0x99,0x99); r.font.name='Calibri'

out = r'd:\PROJECT\MyMusic\docs\practice1\Practice-1-Multi-Agent-Ideation.docx'
doc.save(out)
print(f'[OK] Word document saved to: {out}')
