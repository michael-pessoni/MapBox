package com.michaelpessoni.mapdesafiofordiel

import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.michaelpessoni.mapdesafiofordiel.data.local.PinsDatabase
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var database: PinsDatabase

    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PinsDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDatabase() = database.close()

    @Test
    fun addNewPin() {
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        onView(withId(R.id.add_pins_button)).check(matches(isCompletelyDisplayed()))
        onView(withId(R.id.add_pins_button)).perform(click())

        onView(withId(R.id.longitude_tv)).check(matches(isDisplayed()))
        onView(withId(R.id.latitude_tv)).check(matches(isDisplayed()))

        onView(withId(R.id.save_pin_button)).perform(click())

        activityScenario.close()
    }

}