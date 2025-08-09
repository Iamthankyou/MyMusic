# States & Behavior
- Loading: Shimmer placeholders; initial and page append loaders.
- Error: Inline error with retry; toast/snackbar for transient errors.
- Empty: Friendly illustration/title and CTA to explore.
- Offline: Banner indicator, reduced features (no streaming/download start).
- Permissions: Prompt flow for notifications (API 33+), foreground service rationale, storage access via `MediaStore` (no broad storage perms for 29+).
