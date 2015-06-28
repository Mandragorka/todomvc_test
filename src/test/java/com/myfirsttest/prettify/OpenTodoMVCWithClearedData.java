package com.myfirsttest.prettify;

import org.junit.After;
import org.junit.Before;

import java.io.IOException;

import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class OpenTodoMVCWithClearedData {

    @Before
    public void setUp() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }

    @After
    public void tearDown() throws IOException {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com/");
    }

}
