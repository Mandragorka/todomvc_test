package com.myfirsttest.separate.pageobjects;

import org.junit.After;
import org.junit.Before;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class OpenTodoMVCWithClearedDataBeforeEachTest extends WithReporteScreenshotsPerTest {

    @Before
    public void setUp() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }

    @After
    public void tearDown() {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com/");
    }

}
