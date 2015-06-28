package com.myfirsttest.separate.pagemodules;

import org.junit.After;
import org.junit.BeforeClass;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class OpenTodoMVCWithClearedData extends WithReporteScreenshotsPerTest {

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

    private static void openTodoMVC() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }
}
