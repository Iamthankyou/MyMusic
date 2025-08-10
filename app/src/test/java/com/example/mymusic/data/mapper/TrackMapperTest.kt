package com.example.mymusic.data.mapper

import com.example.mymusic.data.remote.TrackDto
import org.junit.Assert.assertEquals
import org.junit.Test

class TrackMapperTest {

    @Test
    fun fromDto_mapsFields_andConvertsDuration() {
        val dto = TrackDto(
            id = "1",
            name = "Song",
            artistName = "Artist",
            durationSec = 123,
            audioUrl = "http://audio",
            imageUrl = "http://image"
        )
        val track = TrackMapper.fromDto(dto)
        assertEquals("1", track.id)
        assertEquals("Song", track.title)
        assertEquals("Artist", track.artist)
        assertEquals(123000L, track.durationMs)
        assertEquals("http://image", track.artworkUrl)
        assertEquals("http://audio", track.audioUrl)
        assertEquals(false, track.isDownloadable)
    }

    @Test
    fun fromDto_handlesNulls() {
        val dto = TrackDto(
            id = "2",
            name = null,
            artistName = null,
            durationSec = null,
            audioUrl = null,
            imageUrl = null
        )
        val track = TrackMapper.fromDto(dto)
        assertEquals("", track.title)
        assertEquals("", track.artist)
        assertEquals(0L, track.durationMs)
        assertEquals(null, track.artworkUrl)
        assertEquals(null, track.audioUrl)
    }
}


