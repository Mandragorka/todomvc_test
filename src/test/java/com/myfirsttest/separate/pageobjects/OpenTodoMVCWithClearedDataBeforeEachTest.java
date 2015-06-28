package com.myfirsttest.separate.pageobjects;

import org.junit.After;
import org.junit.BeforeClass;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class OpenTodoMVCWithClearedDataBeforeEachTest extends WithReporteScreenshotsPerTest {

    @BeforeClass
    public static void openTodoMVCPage() {
        openTodoMVC();
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com/");
        openTodoMVC();
    }

    public static void openTodoMVC() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }
}
