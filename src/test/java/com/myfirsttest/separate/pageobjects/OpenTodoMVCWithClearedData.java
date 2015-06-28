package com.myfirsttest.separate.pageobjects;

import org.junit.After;
import org.junit.Before;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class OpenTodoMVCWithClearedData extends WithReporteScreenshotsPerTest {

    @Before
    public void setUp() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        // Because localStorage clears only when browser leaves the page.
        // We need to navigate to separate page, otherwise localStorage will maintain it's data.
        open("http://todomvc.com/");
    }
}
