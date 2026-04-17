"""
Practice 1: Multi-Agent Ideation Pipeline
Word report — Sales pitch style: why it's better, how it works, real examples
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
       ['pipeline','comparison','agents_detail','evidence','setup','series']}

doc = Document()
sec = doc.sections[0]
sec.page_width = Inches(8.27); sec.page_height = Inches(11.69)
sec.left_margin = Cm(2); sec.right_margin = Cm(2)
sec.top_margin = Cm(2); sec.bottom_margin = Cm(2)

# ── Helpers ──────────────────────────────────────────────────
def shade(cell, hx):
    tc = cell._tc; pr = tc.get_or_add_tcPr()
    s = OxmlElement('w:shd')
    s.set(qn('w:val'),'clear'); s.set(qn('w:color'),'auto'); s.set(qn('w:fill'),hx)
    pr.append(s)

def run(p, text, bold=False, italic=False, sz=10, color=None, font='Calibri'):
    r = p.add_run(text); r.bold=bold; r.italic=italic; r.font.size=Pt(sz); r.font.name=font
    if color: r.font.color.rgb = RGBColor(*bytes.fromhex(color))
    return r

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

def callout(text, bg='FFF8E1'):
    t = doc.add_table(rows=1, cols=1); t.style='Table Grid'
    t.alignment = WD_TABLE_ALIGNMENT.CENTER
    shade(t.rows[0].cells[0], bg)
    p = t.rows[0].cells[0].paragraphs[0]
    run(p, text, bold=True, sz=10)
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
r = p.add_run('Multi-Agent Ideation Pipeline\nTurn 1 Sentence Into a Validated Product Concept\nUsing 8 AI Agents That Research the Internet For You')
r.font.size=Pt(12); r.font.name='Calibri'; r.font.color.rgb=RGBColor(0x48,0x6F,0x8C)

doc.add_paragraph()
p = doc.add_paragraph(); p.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = p.add_run('Submitted: 2026-04-17  |  Video Editor  |  Duy LQ')
r.font.size=Pt(10); r.font.name='Calibri'; r.font.color.rgb=RGBColor(0x99,0x99,0x99)

doc.add_paragraph()
img('series', 'This is Practice 1 of a 3-part series: Ideation → Planning → Code', 5.5)
doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  1. THE PROBLEM
# ══════════════════════════════════════════════════════════════
heading('1. The Problem: Why Most AI Brainstorming Doesn\'t Work', 1)

body('Most people brainstorm with AI like this:')
callout('💬 "Hey ChatGPT, I want to make a music app for indie artists. What do you think?"')

body('The AI responds with a confident, detailed answer that SOUNDS helpful. '
     'The problem? Almost none of it is verifiable:')

table(
    ['What the AI Says', 'The Real Problem'],
    [
        ['"Competitors include Spotify, SoundCloud,\nand Bandcamp."',
         'Did it actually search? Or just recall from\ntraining data? No URLs to verify.'],
        ['"The music streaming market is\nestimated at $30 billion."',
         'Source? There is no source.\nThis number could be from 2021 or completely made up.'],
        ['"This is a promising idea with\ngreat potential."',
         'Of course it says that — it\'s trained to be positive.\nIt never challenges your assumptions.'],
        ['"You could use React Native\nwith a Spotify API."',
         'Did it check Spotify API pricing? Rate limits?\nTerms of service? No.'],
        ['"There\'s a gap in the market for\nindie music discovery."',
         'Based on what evidence?\nIt doesn\'t know what launched last week.'],
    ]
)

body('The dangerous part is not that the AI is wrong — it\'s that you feel confident '
     'and start building a product based on information nobody verified.', bold=True)

heading('What Would "Good" AI Brainstorming Look Like?', 2)
body('Imagine if instead of 1 generic AI chat, you had:')
body('A requirements analyst who asks you 10-15 structured questions '
     'before making any suggestions — and then SEARCHES the internet to verify your answers.', bullet=True)
body('A market researcher who finds actual market size data WITH clickable source URLs '
     'from Statista, Grand View Research, etc.', bullet=True)
body('A competitor analyst who finds 5+ real competing apps, '
     'reads their 1-star App Store reviews, and finds user complaints on Reddit.', bullet=True)
body('A tech scout who checks actual API pricing pages, rate limits, and terms of service.', bullet=True)
body('A devil\'s advocate evaluator who scores your idea on 8 dimensions '
     'and REQUIRES evidence for each score — no free passes.', bullet=True)
body('A risk assessor who asks "How would I GUARANTEE this project fails?" '
     'and searches for legal requirements you hadn\'t considered.', bullet=True)

body('That\'s exactly what this method does. And it costs nothing extra — '
     'just your existing Cursor subscription.', bold=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  2. SIDE-BY-SIDE COMPARISON
# ══════════════════════════════════════════════════════════════
heading('2. Traditional AI Brainstorming vs This Method', 1)

body('Let\'s use a real example. Same idea, two different approaches:', bold=True)
callout('💡 Idea: "I want to make a music streaming app focused on independent artists, like Spotify but for indie music."')

img('comparison', 'Side-by-side: Traditional gives you opinions. This method gives you evidence.', 6.2)

table(
    ['', 'Traditional\n(1 AI chat)', 'This Method\n(8 agents)'],
    [
        ['You type',               '20+ messages, back-and-forth',     '1 sentence + answer 10-15 questions'],
        ['How many AI "brains"',   '1 (does everything, biased)',      '8 specialists (each has fresh context)'],
        ['Does it search the web?','No — uses training data',          'Yes — every agent searches the internet'],
        ['Real competitors?',      'Maybe 2-3, often wrong names',     '5+ with URLs, pricing, user counts'],
        ['Market size data?',      '"About $30B" — no source',        '"$28.6B" — Grand View Research [URL]'],
        ['API pricing checked?',   'No — just mentions API names',    'Yes — actual pricing pages visited'],
        ['Anyone challenge you?',  'Never — AI agrees with you',      'Yes — Agent 5 is a dedicated critic'],
        ['Legal research?',        'None',                             'GDPR, CCPA, licensing laws searched'],
        ['Evidence per claim?',    'None — 0 source URLs',            'Source URL + confidence level'],
        ['Can you verify?',        'No — just trust it',              'Yes — every URL is clickable'],
        ['Output format',          'A scrollable chat transcript',    '13-section document'],
        ['Time',                   '~15 minutes',                      '~20 minutes'],
        ['Verifiable facts',       '~0',                               '30+'],
    ]
)

callout('💡 The key insight: traditional brainstorming gives you OPINIONS that sound good.\n'
        'This method gives you EVIDENCE you can verify.\n\n'
        'Same amount of time. Completely different quality of output.')

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  3. HOW IT WORKS — STEP BY STEP
# ══════════════════════════════════════════════════════════════
heading('3. How It Works: A Complete Walkthrough', 1)

body('Think of it like a medical checkup. You don\'t see 1 doctor who does everything. '
     'You see specialists — and each only sees your test results, '
     'not what you told the previous doctor. This prevents bias.', bold=True)

img('pipeline', 'The full pipeline: your 1 prompt flows through 4 phases, 8 agents, with quality checks', 6.2)

heading('Phase 1: Understanding Your Idea (2-5 minutes)', 2)
body('You type your idea. Agent 1 (Requirements Analyst) doesn\'t start working — '
     'it asks YOU questions first.', bold=True)
body('This is different from normal AI brainstorming where the AI immediately '
     'starts generating ideas. Here, the AI wants to UNDERSTAND before suggesting anything.')

heading('Example: What the Conversation Looks Like', 3)
table(
    ['Agent Asks', 'You Respond', 'Why It Matters'],
    [
        ['"Who is your target user?\nAge range? Tech-savvy?"',
         '"Vietnamese millennials,\n22-35, use Spotify daily"',
         'Defines the UX complexity\nand pricing strategy'],
        ['"What makes this different\nfrom Spotify or Bandcamp?"',
         '"Focus on Vietnamese\nindie artists specifically"',
         'This is your USP —\nthe agent will verify it'],
        ['"What\'s your budget for MVP?\nTimeline?"',
         '"$5K, 3 months,\n1 developer"',
         'Determines what\'s\nrealistic to build'],
        ['"Any licensing concerns?\nMusic rights?"',
         '"I\'m not sure"',
         'Agent flags this for\ndeep research by Agent 7'],
    ]
)

body('After your answers, the agent doesn\'t just accept them. '
     'It CHALLENGES your assumptions:', bold=True)
callout('🔍 Agent 1: "You said no app focuses on Vietnamese indie artists.\n'
        'Let me search to verify... I found:\n'
        '• NhacCuaTui (14M users) — has indie section\n'
        '• Zing MP3 (now ZingMP3) — Vietnamese focus\n'
        '• Keeng.vn — local streaming service\n'
        'Your assumption may need revision."')

body('This alone is worth more than an entire traditional brainstorming session. '
     'You discovered 3 competitors you didn\'t know about.', bold=True)

doc.add_page_break()

heading('Phase 2: Research (3 agents work at the SAME TIME, ~5 min)', 2)
body('While you wait, 3 specialist agents work simultaneously:')

heading('Agent 2: Market Researcher — What Does the Data Say?', 3)
body('Searches for real market data with source URLs:')
table(
    ['What It Searches', 'Example Result'],
    [
        ['"music streaming market size 2025"', 'TAM: $35.7B globally — Grand View Research [URL]'],
        ['"Vietnam music streaming users"', 'SAM: 40M users in Vietnam — Statista [URL]'],
        ['"indie music market revenue"', '~$4.2B globally, growing 12% YoY — MIDiA [URL]'],
        ['"freemium conversion rate music"', '3-6% average — Revenue Cat report [URL]'],
    ]
)
body('Then it stress-tests at extremes: "At 1M users, API costs would be $X/month. '
     'At only 100 users, does this app even make sense?"', bold=True)

heading('Agent 3: Tech Scout — Can We Actually Build This?', 3)
body('Checks actual API pricing and requirements:')
table(
    ['What It Checks', 'Example Finding'],
    [
        ['Spotify API free tier', '100 requests/min, no streaming rights — [pricing URL]'],
        ['Firebase hosting costs', 'Free up to 50K MAU, then $0.01/MAU — [URL]'],
        ['YouTube API quotas', '10,000 units/day free, $0 for search — [URL]'],
        ['Audio streaming CDN', 'Cloudflare R2: $0.015/GB egress — [URL]'],
    ]
)
body('It also spots patterns: "Every API you need has rate limits at scale. '
     'You need a caching layer from day 1."', bold=True)

heading('Agent 4: Competitor Analyst — Who Already Does This?', 3)
body('Finds 5+ real competitors with evidence:')
table(
    ['Found', 'Details'],
    [
        ['Bandcamp', '8M users, $201M GMV, artist-first — bandcamp.com'],
        ['DistroKid', '$35.99/year distribution — distrokid.com'],
        ['NhacCuaTui', '14M users, Vietnamese focus — nhaccuatui.com'],
        ['Zing MP3', '50M+ users, dominant in Vietnam — zingmp3.vn'],
        ['SoundCloud', '175M users, indie-friendly — soundcloud.com'],
    ]
)
body('Then reads 1-star App Store reviews and Reddit complaints to find what '
     'users actually hate about these apps. This reveals your real opportunities.', bold=True)

doc.add_page_break()

heading('Phase 3: Evaluation (3 agents work at the SAME TIME, ~5 min)', 2)
body('Three evaluation agents work simultaneously on different angles:')

heading('Agent 5: Idea Evaluator — Should You Build This?', 3)
body('Scores your idea on 8 dimensions. Each score MUST have 1 sentence of evidence:', bold=True)
table(
    ['Criterion', 'Score', 'Evidence (required)'],
    [
        ['Feasibility', '4/5', '"APIs exist, Flutter handles cross-platform, $5K sufficient for MVP"'],
        ['Market Demand', '3/5', '"40M users in VN, but 2 dominant players — hard to penetrate"'],
        ['Uniqueness', '2/5', '"NhacCuaTui already has indie section, Zing dominates VN"'],
        ['Time to MVP', '4/5', '"3 months realistic with Flutter + Firebase — not novel tech"'],
        ['Scalability', '4/5', '"Standard cloud architecture, CDN for audio — well understood"'],
        ['Cost Efficiency', '3/5', '"Audio streaming CDN costs grow linearly with users"'],
        ['Legal Risk', '2/5', '"Music licensing in Vietnam is complex — [research needed]"'],
        ['Timing', '3/5', '"Market growing but competitors already established"'],
    ]
)
body('Verdict: CONDITIONAL GO — proceed only if licensing requirements can be met '
     'and a clear differentiator is found vs NhacCuaTui.', bold=True)

heading('Agent 6: UX Strategist — How Will Users Experience This?', 3)
body('Designs user journeys based on UX research (NNGroup, Baymard Institute):')
body('First open → onboarding → "aha moment" must happen in < 2 minutes', bullet=True)
body('Daily loop: notification trigger → explore → save → share', bullet=True)
body('Creative idea: "What if onboarding worked like a game tutorial?"', bullet=True)

heading('Agent 7: Risk Assessor — What Could Kill This Project?', 3)
body('Asks: "How would I GUARANTEE this project FAILS?"', bold=True)
body('"Ignore all music licensing" → MUST research Vietnamese music copyright laws', bullet=True)
body('"Assume one API will always work" → MUST have fallback for every API', bullet=True)
body('"Build everything before launching" → MUST launch MVP in 3 months max', bullet=True)

doc.add_page_break()

heading('Phase 4: Final Document (Agent 8, ~3 minutes)', 2)
body('Agent 8 (Crystallizer) takes ALL 7 reports and compiles them into 1 document.', bold=True)
body('Important: Agent 8 does NOT add new information. It only:')
body('Distills: pulls key findings from each report', bullet=True)
body('Connects: shows where agents agreed or disagreed', bullet=True)
body('Traces: every claim links back to its source agent + URL', bullet=True)
body('Scores: calculates an Overall Confidence Score for the entire spec', bullet=True)

heading('Quality Control: Nothing Gets Passed Without Checking', 2)
body('Between Phase 2 and Phase 3, the orchestrator checks each report:')
body('✓ Does it have at least 5 source URLs?', bullet=True)
body('✓ Are all sections filled (no empty headings)?', bullet=True)
body('✓ Is there real data, not just opinions?', bullet=True)
body('If a report fails → that agent gets re-run. If it fails twice → '
     'the gap is noted in the final document so you know what to research manually.', bold=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  4. WHAT YOU GET
# ══════════════════════════════════════════════════════════════
heading('4. What You Get: The 13-Section Idea Specification', 1)

body('After ~20 minutes, you receive a single, structured document:', bold=True)

table(
    ['Section', 'What It Contains', 'Why It\'s Useful'],
    [
        ['1. Problem Statement', 'Pain point + who + how big', 'Validated by web search'],
        ['2. Target User', 'Persona + segment size', 'Based on real demographics'],
        ['3. Solution Overview', 'MVP features + differentiator', 'Compared against competitors'],
        ['4. Market Opportunity', 'TAM/SAM/SOM + growth + trends', 'Real numbers with URLs'],
        ['5. Competitive Landscape', '5+ competitors + feature matrix', 'Every app has a URL'],
        ['6. Tech Architecture', 'Stack + API pricing + costs', 'Pricing pages verified'],
        ['7. UX & User Journey', 'Onboarding + retention hooks', 'Based on UX research'],
        ['8. Monetization', 'Revenue model + benchmarks', 'Industry data cited'],
        ['9. Risk Analysis', 'Risks ranked + mitigations', 'Legal reqs researched'],
        ['10. Evaluation', '8-dimension scorecard + verdict', 'Evidence per score'],
        ['11. MVP Scope', 'Must-have / nice-to-have', 'Realistic timeline'],
        ['12. Go-to-Market', 'Launch strategy + first users', 'Based on competitor gaps'],
        ['13. Evidence Trail', 'ALL sources + confidence scores', 'Full audit trail'],
    ]
)

heading('How Much Can You Trust the Output?', 2)
body('Every claim has a confidence level so you know what\'s verified and what\'s not:', bold=True)

img('evidence', 'Traditional: no source, can\'t verify. This method: source URL + confidence level.', 5.5)

table(
    ['Level', 'What It Means', 'Example'],
    [
        ['✅ Validated (90%+)', '3+ sources confirm this', '"Spotify has 600M+ users" — Wikipedia + TechCrunch + Statista'],
        ['🟡 Likely (60-89%)', '1-2 sources found', '"VN indie music market ~$200M" — MIDiA Research report'],
        ['🟠 Low Confidence', 'Reasoning only, no source', '"Estimated 5% conversion" — based on similar apps'],
        ['❌ Unvalidated', 'No data found', '"No data on VN indie app market" — NEEDS YOUR RESEARCH'],
    ]
)

body('The document ends with an Overall Confidence Score: "72% of claims are validated." '
     'You know exactly what you can trust and what needs more work.', bold=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  5. HOW TO SET UP
# ══════════════════════════════════════════════════════════════
heading('5. How to Start Using It Today', 1)

img('setup', 'Setup once (5 minutes), then use for every new idea', 5.5)

heading('One-Time Setup (5 minutes)', 2)
body('Copy 10 files into your project. That\'s all.', bold=True)
code('.cursor/\n'
     '  agents/                    <-- Cursor finds these automatically\n'
     '    ideation-orchestrator.md    (manages the pipeline)\n'
     '    requirements-analyst.md    (asks questions + validates)\n'
     '    market-researcher.md       (finds market data)\n'
     '    tech-scout.md              (checks APIs + pricing)\n'
     '    competitor-analyst.md      (finds real competitors)\n'
     '    idea-evaluator.md          (scores + critiques)\n'
     '    ux-strategist.md           (designs experience)\n'
     '    risk-assessor.md           (finds risks + legal)\n'
     '    idea-crystallizer.md       (compiles everything)\n'
     '  mcp.json                     (optional: API docs tool)')

heading('Every Time You Have a New Idea', 2)
body('Open Cursor → Agent Mode → type:', bold=True)
callout('💬 /ideation-orchestrator "I want to make [your idea here]"')

body('Then:')
body('1. Answer 10-15 questions from the Requirements Analyst (2-5 min)', bullet=True)
body('2. Wait while 6 agents research in parallel (~15 min)', bullet=True)
body('3. Receive your 13-section Idea Specification', bullet=True)

body('Total effort: 1 sentence + answering questions. Total time: ~20 minutes.', bold=True)
body('Everything else is fully automatic.', bold=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  6. WHAT MAKES IT SMART
# ══════════════════════════════════════════════════════════════
heading('6. What Makes It Smart: The Techniques Behind Each Agent', 1)

body('Each agent doesn\'t just search — it THINKS using proven techniques:')

table(
    ['Technique', 'What It Does', 'Real Example', 'Used By'],
    [
        ['Assumption\nFlipping',
         'Challenges what you\nbelieve is true.\n"What if the problem\nis the opposite?"',
         '"Users want more features"\n→ "What if they want fewer?"\n→ Simplicity wins\n(proven by Spotify vs iTunes)',
         'Agent 1\n(Requirements)\nAgent 7\n(Risk)'],
        ['Extreme Scale\nTesting',
         'Tests your idea at\n1000x and 0.001x.\n"What breaks?\nWhat survives?"',
         '"At 1M users, API costs =\n$50K/month"\n"At 100 users, does the\napp even make sense?"',
         'Agent 2\n(Market)\nAgent 7\n(Risk)'],
        ['Creative\nCollision',
         'Forces unrelated\nconcepts together to\nfind new ideas.',
         '"Music app × Restaurant =\nDaily tasting flight of\n5 curated songs"\n→ NO competitor has this',
         'Agent 4\n(Competitor)\nAgent 6\n(UX)'],
        ['Pattern\nRecognition',
         'Spots what\'s true\nacross 3+ technologies.\nFinds universal truths.',
         '"ALL your APIs rate-limit\nat scale" → Build caching\nlayer from day 1',
         'Agent 3\n(Tech)'],
        ['Simplification',
         'Finds 1 change that\nfixes multiple problems\nat the same time.',
         '"Don\'t host music files"\n→ Fixes: licensing +\nCDN costs + moderation\nALL with 1 decision',
         'Agent 5\n(Evaluator)'],
    ]
)

body('These techniques come from open-source frameworks (BMAD Method, ClaudeKit by Anthropic). '
     'They\'re not magic — they\'re structured thinking tools that force the AI to go deeper '
     'than a simple search.', bold=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  7. SAFETY
# ══════════════════════════════════════════════════════════════
heading('7. When the AI Can\'t Find Data (And What It Does About It)', 1)

body('Real problem: some searches return nothing. Most AIs will MAKE SOMETHING UP '
     'rather than say "I don\'t know." This method explicitly forbids that.', bold=True)

heading('The When-Stuck Protocol', 2)
body('Every agent follows a 4-step fallback when search fails:')
table(
    ['Step', 'What the Agent Does', 'Example'],
    [
        ['1. Broaden\nsearch', 'Remove specific terms,\ntry synonyms', '"Vietnamese indie music app market"\n→ "Southeast Asia music app market"'],
        ['2. Alternative\nsources', 'Try Reddit, HackerNews,\nindustry reports', 'Official stats failed → search Reddit\nfor user discussions, founder posts'],
        ['3. Adjacent\nmarkets', 'Search parent/sibling\ncategory instead', '"Vietnamese indie music" → "Asian\nstreaming market" or "global indie"'],
        ['4. Accept\nthe gap', 'Mark as [NO DATA].\nNEVER fabricate.', '"Vietnamese indie music market size:\n[NO DATA — needs manual research]"'],
    ]
)

callout('🔒 Critical Rule: An agent will NEVER make up data to fill a gap.\n'
        'It will always say "I couldn\'t find this — here\'s what you need to research manually"\n'
        'rather than invent numbers that sound plausible.')

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  8. ALSO WORKS WITH
# ══════════════════════════════════════════════════════════════
heading('8. Using This With Other AI Tools', 1)
body('This method is designed for Cursor, but the core principles work with any AI tool:')
table(
    ['Tool', 'How to Use This Method'],
    [
        ['Cursor (recommended)', 'Copy agent files to .cursor/agents/\nFully automatic — native subagent support'],
        ['ChatGPT Plus', 'Run 4 separate conversations (or 8 if patient).\nPaste each agent\'s prompt as first message.\nManually copy output between conversations.'],
        ['Claude', 'Use the Projects feature.\nCreate separate projects for each agent.\nPaste outputs as context in next project.'],
        ['Any other AI', 'Core idea: separate sessions, separate roles.\nEach "agent" = a new conversation with a system prompt.\nDocuments are the handoff between sessions.'],
    ]
)

body('The principle is always the same: different specialists, separate contexts, '
     'evidence-based output. Cursor just makes it automated.', bold=True)

# ══════════════════════════════════════════════════════════════
#  9. TIPS
# ══════════════════════════════════════════════════════════════
heading('9. Tips & Common Mistakes', 1)

heading('✅ Do', 2)
for tip in [
    'Keep your initial idea SHORT — even 1 sentence. Agent 1\'s job is to expand it through questions.',
    'Answer questions honestly. Short answers are fine — the agent follows up on vague answers.',
    'Read the confidence scores. Low-confidence claims need YOUR manual verification.',
    'Challenge the evaluator\'s scores if something feels wrong: "Why only 3/5 on uniqueness?"',
    'Pay attention to the SCAMPER variants — they often produce the best idea.',
    'Use this Idea Spec as the INPUT for Practice 2 (Idea → Architecture & Plan).',
]:
    body(tip, bullet=True)

heading('❌ Don\'t', 2)
for tip in [
    'Don\'t skip the questioning phase. Bad requirements = bad everything downstream.',
    'Don\'t accept scores without reading the evidence sentence.',
    'Don\'t edit the final document directly. If something is wrong, re-run that specific agent.',
    'Don\'t use this for trivial ideas you\'ve already validated. Jump to Practice 2 instead.',
]:
    body(tip, bullet=True)

doc.add_page_break()

# ══════════════════════════════════════════════════════════════
#  10. WHAT'S NEXT
# ══════════════════════════════════════════════════════════════
heading('10. What\'s Next: From Idea to Working Code', 1)
body('This document is Practice 1 of 3. Each practice feeds into the next:', bold=True)

img('series', 'From 1 sentence to working code: each practice uses different specialist agents', 5.5)

table(
    ['Practice', 'Name', 'Input', 'Output', 'Agents Used'],
    [
        ['1 (this)', 'Multi-Agent\nIdeation', '1 sentence\nidea', '13-section Idea\nSpecification', '8 research +\nevaluation agents'],
        ['2 (next)', 'Idea to\nPlan', 'Idea\nSpecification', 'Architecture +\nuser stories', 'Architect, PM,\nUX Designer'],
        ['3 (last)', 'Plan to\nCode', 'Implementation\nPlan', 'Working code\n+ tests', 'Developer, QA,\nCode Reviewer'],
    ]
)

body('The 13-section document from this practice becomes the input for Practice 2. '
     'Different specialist agents, same principle: evidence over opinions, '
     'multiple perspectives, quality checks between phases.', bold=True)

# FOOTER
doc.add_paragraph()
ft = doc.add_paragraph(); ft.alignment = WD_ALIGN_PARAGRAPH.CENTER
r = ft.add_run('Report generated: 2026-04-17  |  Practice 1: Multi-Agent Ideation Pipeline  |  Video Editor')
r.font.size=Pt(8); r.font.color.rgb=RGBColor(0x99,0x99,0x99); r.font.name='Calibri'

out = r'd:\PROJECT\MyMusic\docs\practice1\Practice-1-Multi-Agent-Ideation.docx'
doc.save(out)
print(f'[OK] Word document saved to: {out}')
