package com.michaelpessoni.mapdesafiofordiel.data.local


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.michaelpessoni.mapdesafiofordiel.data.Pin
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PinsDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: PinsDatabase

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            PinsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun insertPinAndObservePins() = runBlockingTest{
        val pin = Pin(15.0,15.0)
        database.pinsDatabaseDAO.insert(pin)

        val loadedPin = database.pinsDatabaseDAO.getAllPins()

        assertThat(loadedPin, notNullValue())
        assertThat(loadedPin[0].longitude, `is`(pin.longitude))
        assertThat(loadedPin[0].latitude, `is`(pin.latitude))

    }
}