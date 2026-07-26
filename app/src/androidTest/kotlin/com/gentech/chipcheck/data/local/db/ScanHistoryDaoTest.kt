package com.gentech.chipcheck.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScanHistoryDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: ScanHistoryDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.scanHistoryDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun entity(part: String, vendor: String? = "SK hynix") = ScanHistoryEntity(
        decodedSnapshotJson = "{}",
        vendor = vendor,
        normalizedPartNumber = part,
        hasUnknownSegments = false,
        source = "MANUAL",
        timestamp = System.currentTimeMillis()
    )

    @Test
    fun insertAndObserveAll_returnsInsertedRowsNewestFirst() = runTest {
        dao.insert(entity("H9TQ17ADFTMC").copy(timestamp = 1000))
        dao.insert(entity("KLUEG4RHGB", vendor = "Samsung").copy(timestamp = 2000))

        val all = dao.observeAll().first()

        assertEquals(2, all.size)
        assertEquals("KLUEG4RHGB", all.first().normalizedPartNumber)
    }

    @Test
    fun search_matchesByPartNumberOrVendor() = runTest {
        dao.insert(entity("H9TQ17ADFTMC"))
        dao.insert(entity("KLUEG4RHGB", vendor = "Samsung"))

        val byPart = dao.search("TQ17").first()
        val byVendor = dao.search("Samsung").first()

        assertEquals(1, byPart.size)
        assertEquals("H9TQ17ADFTMC", byPart.first().normalizedPartNumber)
        assertEquals(1, byVendor.size)
        assertEquals("Samsung", byVendor.first().vendor)
    }

    @Test
    fun deleteById_removesOnlyThatRow() = runTest {
        val keepId = dao.insert(entity("H9TQ17ADFTMC"))
        val removeId = dao.insert(entity("KLUEG4RHGB", vendor = "Samsung"))

        dao.deleteById(removeId)
        val remaining = dao.observeAll().first()

        assertEquals(1, remaining.size)
        assertEquals(keepId, remaining.first().id)
    }

    @Test
    fun clearAll_removesEveryRow() = runTest {
        dao.insert(entity("H9TQ17ADFTMC"))
        dao.insert(entity("KLUEG4RHGB", vendor = "Samsung"))

        dao.clearAll()

        assertTrue(dao.observeAll().first().isEmpty())
    }

    @Test
    fun insertWithExplicitId_afterDelete_restoresTheSameId() = runTest {
        val id = dao.insert(entity("H9TQ17ADFTMC"))
        dao.deleteById(id)

        val restoredId = dao.insert(entity("H9TQ17ADFTMC").copy(id = id))

        assertEquals(id, restoredId)
        assertNull(dao.observeAll().first().firstOrNull { it.id != id })
    }
}
