# Performance and feature roadmap

## Implemented in this branch

- `FeedDiversifier` computes HSV and colorfulness once per wallpaper rather than once per comparison.
- Diversification exits early when the maximum possible similarity score is reached.
- The existing deterministic ordering and cross-page tail behavior are preserved.

## Recommended next features

1. Download wallpapers for offline use, with a bounded disk cache and source attribution.
2. User-created collections and collection-only rotation playlists.
3. Battery/network-aware rotation policies using WorkManager constraints.
4. Color, orientation, brightness, and aspect-ratio filters before pagination.
5. Export/share actions for favorites.

These should be added incrementally with instrumentation tests and Paging/Room benchmarks so feature work does not regress startup time, memory use, or battery consumption.
