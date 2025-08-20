package com.example.mymusic.data.repository

import com.example.mymusic.data.mapper.TrackMapper
import com.example.mymusic.data.remote.DeezerService
import com.example.mymusic.data.remote.JamendoTracksService
import com.example.mymusic.domain.model.Track
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Aggregates results from Jamendo and Deezer and maps to unified domain models.
 */
@Singleton
class AggregatedMusicRepository @Inject constructor(
	private val jamendoService: JamendoTracksService,
	private val deezerService: DeezerService,
	private val trackMapper: TrackMapper
) {

	suspend fun getTrendingTracks(limit: Int = 20, offset: Int = 0): List<Track> = coroutineScope {
		val jamendoDeferred = async {
			try {
				val res = jamendoService.getTrendingTracks(limit = limit, offset = offset)
				res.results.map { trackMapper.fromDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val deezerDeferred = async {
			try {
				val res = deezerService.getTrendingTracks(limit = limit, index = offset)
				res.data.map { TrackMapper.fromDeezerDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val jamendo = jamendoDeferred.await()
		val deezer = deezerDeferred.await()
		(jamendo + deezer)
	}

	suspend fun searchTracks(query: String, limit: Int = 20, offset: Int = 0): List<Track> = coroutineScope {
		val jamendoDeferred = async {
			try {
				val res = jamendoService.searchTracks(query = query, limit = limit, offset = offset)
				res.results.map { trackMapper.fromDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val deezerDeferred = async {
			try {
				val res = deezerService.searchTracks(query = query, limit = limit, index = offset)
				res.data.map { TrackMapper.fromDeezerDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val jamendo = jamendoDeferred.await()
		val deezer = deezerDeferred.await()
		(jamendo + deezer)
	}

	// New methods for slides feed using correct Jamendo API endpoints
	suspend fun getTopTracks(limit: Int = 10): List<Track> = coroutineScope {
		val jamendoDeferred = async {
			try {
				val res = jamendoService.getTopTracks(limit = limit)
				res.results.map { trackMapper.fromDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val deezerDeferred = async {
			try {
				val res = deezerService.getTrendingTracks(limit = limit, index = 0)
				res.data.map { TrackMapper.fromDeezerDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val jamendo = jamendoDeferred.await()
		val deezer = deezerDeferred.await()
		(jamendo + deezer).take(limit)
	}

	suspend fun getLatestTracks(limit: Int = 10): List<Track> = coroutineScope {
		val jamendoDeferred = async {
			try {
				val res = jamendoService.getLatestTracks(limit = limit)
				res.results.map { trackMapper.fromDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val deezerDeferred = async {
			try {
				// Try to get latest tracks from Deezer
				val res = deezerService.getLatestTracks(limit = limit, index = 0)
				res.data.map { TrackMapper.fromDeezerDto(it) }
			} catch (t: Throwable) {
				// Fallback to trending tracks if latest fails
				try {
					val res = deezerService.getTrendingTracks(limit = limit, index = 0)
					res.data.map { TrackMapper.fromDeezerDto(it) }
				} catch (t2: Throwable) {
					emptyList()
				}
			}
		}
		val jamendo = jamendoDeferred.await()
		val deezer = deezerDeferred.await()
		(jamendo + deezer).take(limit)
	}

	suspend fun getTracksByMood(mood: String, limit: Int = 10): List<Track> = coroutineScope {
		val jamendoDeferred = async {
			try {
				val res = jamendoService.getTracksByMood(mood = mood, limit = limit)
				res.results.map { trackMapper.fromDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val deezerDeferred = async {
			try {
				val res = deezerService.searchTracks(query = mood, limit = limit)
				res.data.map { TrackMapper.fromDeezerDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val jamendo = jamendoDeferred.await()
		val deezer = deezerDeferred.await()
		(jamendo + deezer).take(limit)
	}

	suspend fun getTracksByGenre(genre: String, limit: Int = 10): List<Track> = coroutineScope {
		val jamendoDeferred = async {
			try {
				val res = jamendoService.getTracksByGenre(genre = genre, limit = limit)
				res.results.map { trackMapper.fromDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val deezerDeferred = async {
			try {
				val res = deezerService.searchTracks(query = genre, limit = limit)
				res.data.map { TrackMapper.fromDeezerDto(it) }
			} catch (t: Throwable) {
				emptyList()
			}
		}
		val jamendo = jamendoDeferred.await()
		val deezer = deezerDeferred.await()
		(jamendo + deezer).take(limit)
	}
}


