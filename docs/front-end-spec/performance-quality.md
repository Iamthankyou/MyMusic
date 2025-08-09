# Performance & Quality
- Use Paging 3 with `cachedIn(viewModelScope)`; prefetch distance 2–3.
- Limit image sizes; request thumbnail sizes where possible.
- Avoid excessive recomposition; remember derived state; use `LazyList` item keys.
- Handle process death: restore last playing item and position when possible.
