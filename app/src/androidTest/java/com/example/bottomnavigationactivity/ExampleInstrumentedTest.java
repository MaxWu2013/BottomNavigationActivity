package com.example.bottomnavigationactivity;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<>(MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.bottomnavigationactivity", appContext.getPackageName());
    }

    @Test
    public void ensure_BottomNavigationView_exist(){

        onView(withId(R.id.navigation_home)).perform(ViewActions.click());

        onView(withText("BottomNavigation"));


        onView(withId(R.id.navigation_dashboard)).perform(ViewActions.click());
        onView(withText("DashBoard"));

        onView(withId(R.id.navigation_notifications)).perform(ViewActions.click());

        onView(withText("Notification"));

    }

    @Test
    public void ensure_pullToRefreshView_exit(){
        onView(withId(R.id.pullToRefresh)).perform(ViewActions.swipeUp());
    }

    @Test
    public void ensure_ImageView_exit_and_perform_click(){


        onView(allOf(withId(R.id.iv_icon), hasSibling(withText("2012-decrease"))))
                .perform(ViewActions.click());
    }



}
