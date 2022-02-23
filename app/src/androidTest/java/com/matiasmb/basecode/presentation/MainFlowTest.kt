package com.matiasmb.basecode.presentation

import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.matiasmb.basecode.R
import com.matiasmb.basecode.presentation.RecyclerViewMatcher.withItemCount
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.endsWith
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@LargeTest
@RunWith(AndroidJUnit4::class)
class MainFlowTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainFlowTest() {
        val searchAutoComplete = onView(
            allOf(
                withId(R.id.placesSearchView),
                isDisplayed()
            )
        )
        searchAutoComplete.perform(click())

        val searchAutoComplete2 = onView(
            allOf(
                withId(R.id.placesSearchView),
                isDisplayed()
            )
        )
        searchAutoComplete2.perform(typeSearchViewText("Rotterdam"), closeSoftKeyboard()).perform(pressKey(
            KeyEvent.KEYCODE_ENTER))

        //TODO improve this wait
        val endTime = System.currentTimeMillis() + 5000
        while (System.currentTimeMillis() < endTime);

        val recyclerView = onView(
            allOf(
                withId(R.id.placesList),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))
        recyclerView.check(matches(withItemCount(10)))

        val recyclerView2 = onView(
            allOf(
                withId(R.id.placesList),
                childAtPosition(
                    withId(R.id.coordinator_layout),
                    1
                )
            )
        )
        recyclerView2.perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        //TODO improve this wait
        val endTime2 = System.currentTimeMillis() + 5000
        while (System.currentTimeMillis() < endTime2);

        onView(withText(endsWith("Swatch, launched in 1983 by Nicolas G. Hayek, is a leading Swiss watch maker and one of the world's most popular brands"))).check(matches(isDisplayed()))

        onView(withText(endsWith("City: Rotterdam"))).check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }

    private fun typeSearchViewText(text: String?): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView::class.java))
            }

            override fun getDescription(): String {
                return "Change view text"
            }

            override fun perform(uiController: UiController?, view: View) {
                (view as SearchView).setQuery(text, false)
            }
        }
    }
}
