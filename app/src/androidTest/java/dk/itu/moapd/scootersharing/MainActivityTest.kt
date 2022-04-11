package dk.itu.moapd.scootersharing

import androidx.lifecycle.Lifecycle.State
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import dk.itu.moapd.scootersharing.activities.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun mainActivityTest_createMainActivity() {
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(State.RESUMED)
    }
}
