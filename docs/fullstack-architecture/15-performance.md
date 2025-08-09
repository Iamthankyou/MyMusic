# 15) Performance
- Paging prefetch distance 2–3; `cachedIn(viewModelScope)`
- Image sizes constrained; Coil crossfade + placeholders
- Avoid heavy recomposition; use item keys; snapshotFlow for scroll-derived state
- Player off main thread; decode/render handled by Media3
