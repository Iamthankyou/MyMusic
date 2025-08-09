# Core Screens & Content
1) Trending
   - Layout: Top tabs (ScrollableTabRow), each tab shows a vertical list.
   - Card/List Item: Cover image (1:1), title, subtitle (artist/owner), optional meta (duration/count).
   - Actions: Primary tap to open detail or start playback (Tracks start playback; others open details).
   - States: Shimmer skeletons (Coil + placeholder), error retry, empty.

2) Feeds
   - Layout: Vertical feed of curated collections (horizontal carousels optional for later).
   - Item: Section header + horizontal list or large cards (MVP: vertical lists).
   - State handling as above.

3) Radio
   - Layout: Grid/list of categories; tapping shows stations list.
   - Station row: Logo/cover, name, bitrate (if available), play icon.
   - Tap station → starts playback and opens mini-player; full Player optional.

4) Stream
   - Similar to Radio; verify API distinction. MVP mirrors Radio patterns.

5) Explore
   - Layout: Full-width list with Paging 3 (infinite scroll).
   - Item: Mixed content or tracks list (MVP: tracks focus). Load more on end.

6) Details (Album/Playlist/Podcast)
   - Header: Large artwork, title, owner/artist, meta.
   - Actions: Play/Shuffle; context menu (later).
   - List of tracks/episodes with index, title, duration.

7) Player (Full Screen)
   - Artwork centered (square or 1:1.1), title, artist.
   - Controls: Play/Pause, Next, Previous, Seek bar with elapsed/remaining time.
   - Secondary actions: Repeat/Shuffle (later), Download button (for track).
   - Gesture: Swipe down/back to collapse to mini-player.

8) Mini-player
   - Compact bar above BottomNav: Artwork thumbnail, title, artist, Play/Pause, Next.
   - Tap expands to full Player; swipe dismiss pauses (optional).

9) Downloads
   - List of downloaded tracks with artwork, title, artist, file status.
   - Show progress rows for active downloads; cancel action.
