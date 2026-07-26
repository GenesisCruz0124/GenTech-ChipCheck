package com.gentech.chipcheck.data.local.db

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UnmappedCodeDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: UnmappedCodeDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.unmappedCodeDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    private fun entity(rawCode: String) = UnmappedCodeEntity(
        vendorGuess = "SK hynix",
        segmentName = "storage",
        rawCode = rawCode,
        fullPartNumber = "H9TQ${rawCode}ADFTMC",
        timestamp = System.currentTimeMillis(),
        reviewed = false
    )

    @Test
    fun insertAndObserveAll_returnsInsertedRow() = runTest {
        dao.insert(entity("99"))

        val all = dao.observeAll().first()

        assertEquals(1, all.size)
        assertEquals("99", all.first().rawCode)
        assertEquals(false, all.first().reviewed)
    }

    @Test
    fun markReviewed_updatesFlagWithoutRemovingRow() = runTest {
        val id = dao.insert(entity("99"))

        dao.markReviewed(id, true)
        val all = dao.observeAll().first()

        assertEquals(1, all.size)
        assertTrue(all.first().reviewed)
    }

    @Test
    fun deleteById_removesOnlyThatRow() = runTest {
        val keepId = dao.insert(entity("99"))
        val removeId = dao.insert(entity("88"))

        dao.deleteById(removeId)
        val remaining = dao.observeAll().first()

        assertEquals(1, remaining.size)
        assertEquals(keepId, remaining.first().id)
    }
}
