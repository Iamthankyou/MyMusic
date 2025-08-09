# 12) Error Handling & Resilience
- Central `AppError` (Network, Api, NotFound, Unauthorized, Playback, Download, Unknown)
- Map exceptions to `AppError` at repository boundary
- UI shows error states with retry actions; toasts/snackbars for transient failures
- Backoff/retry strategy for paging loads on network errors
