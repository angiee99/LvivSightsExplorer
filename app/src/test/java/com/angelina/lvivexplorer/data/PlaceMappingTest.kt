package com.angelina.lvivexplorer.data

import com.angelina.lvivexplorer.data.source.AssetPlaceDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlaceMappingTest {
    @Test
    fun `asset dto maps to room entity`() {
        val dto = AssetPlaceDto(
            id = "latin-cathedral",
            name = "Latin Cathedral",
            category = "Cathedral",
            latitude = 49.8419,
            longitude = 24.0315,
            description = "Test description",
            address = "Katedralna Sq, 1",
            imageUrl = null
        )

        val entity = dto.toEntity()

        assertEquals("latin-cathedral", entity.id)
        assertEquals("Latin Cathedral", entity.name)
        assertEquals("Cathedral", entity.category)
        assertEquals(49.8419, entity.latitude, 0.0)
        assertEquals(24.0315, entity.longitude, 0.0)
        assertNull(entity.imageUrl)
    }
}
