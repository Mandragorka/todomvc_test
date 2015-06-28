package com.myfirsttest.prettify;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Screenshots;
import com.google.common.io.Files;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

import java.io.File;
import java.io.IOException;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;

public class TodomvcTest extends OpenTodoMVCWithClearedData {

    public static ElementsCollection tasks = $$("#todo-list>li");

    @Before
    public void setUp() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }

    @After
    public void clearData() {
        executeJavaScript("localStorage.clear()");
        open("http://todomvc.com/");
    }

    @Test
    public void testAtAllFilter() {
        // Create tasks
        add("a");
        add("b");
        add("c");
        add("d");
        assertTasks("a", "b", "c", "d");
        assertItemsLeftCounter(4);

        // Editing of existing task
        editTask("a", "a edited");
        assertTasks("a edited", "b", "c", "d");

        // Delete task
        deleteTask("b");
        assertTasks("a edited", "c", "d");
        assertItemsLeftCounter(3);

        // Mark tasks as completed
        toggleTask("d");
        toggleTask("c");
        assertItemsLeftCounter(1);

        // Mark task as reopened
        toggleTask("c");
        assertItemsLeftCounter(2);

        clearCompleted();
        assertTasks("a edited", "c");

        // Mark all left tasks as completed and then their removing
        toggleAll();
        assertItemsLeftCounter(0);
        clearCompleted();
        tasks.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Given
        add("a");
        add("b");
        toggleTask("b");
        add("c");
        toggleTask("c");
        toggleTask("c");
        filterActive();
        assertItemsLeftCounter(2);
        assertVisibleTasks("a", "c");

        // Create task under Active filter
        add("d");
        assertVisibleTasks("a", "c", "d");
        assertItemsLeftCounter(3);
        filterAll();
        assertTasks("a", "b", "c", "d");
        filterActive();

        // Editing of existing task
        editTask("a", "a edited from active");
        assertVisibleTasks("a edited from active", "c", "d");

        // Delete task
        deleteTask("a edited from active");
        assertItemsLeftCounter(2);
        assertVisibleTasks("c", "d");

        // Mark tasks as completed
        toggleTask("d");
        assertItemsLeftCounter(1);
        assertVisibleTasks("c");

        filterCompleted();
        assertVisibleTasks("b", "d");
        filterAll();

        // Mark task as reopened
        toggleTask("b");
        assertItemsLeftCounter(2);
        assertVisibleTasks("b", "c", "d");
        filterActive();
        assertVisibleTasks("b", "c");

        toggleAll();
        tasks.filter(visible).shouldBe(empty);
    }

    @Test
    public void testAtCompletedFilter() {

        // Given
        add("a");
        add("b");
        add("c");
        toggleAll();
        add("d");
        filterCompleted();
        assertVisibleTasks("a", "b", "c");
        assertItemsLeftCounter(1);

        // Mark task as reopened
        toggleTask("c");
        assertItemsLeftCounter(2);
        assertVisibleTasks("a", "b");
        filterAll();
        assertVisibleTasks("a", "b", "c", "d");
        filterActive();
        assertVisibleTasks("c", "d");
        filterCompleted();

        // Editing of existing task
        editTask("b", "b edited from completed");
        assertVisibleTasks("a", "b edited from completed");

        // Delete edited task and then removing all completed tasks
        deleteTask("b edited from completed");
        assertItemsLeftCounter(2);
        assertVisibleTasks("a");

        filterCompleted();
        clearCompleted();
        tasks.filter(visible).shouldBe(empty);
    }


    @Step
    public void filterAll() {
        $("[href='#/']").click();
    }

    @Step
    public void filterActive() {
        $("[href='#/active']").click();
    }

    @Step
    public void filterCompleted() {
        $("[href='#/completed']").click();
    }
    @Step
    private void add(String text) {
        $("#new-todo").val(text).pressEnter();
    }

    @Step
    private void deleteTask(String text) {
        tasks.find(exactText(text)).hover().find(".destroy").click();
    }

    @Step
    private void toggleTask(String text) {
        tasks.findBy(exactText(text)).find(".toggle").click();
    }

    @Step
    public static void toggleAll () {
        $("#toggle-all").click();
    }

    @Step
    private void clearCompleted(){
        $("#clear-completed").click();
    }

    @Step
    private void editTask(String textToEdit, String newText) {
        tasks.findBy(exactText(textToEdit)).find("label").doubleClick();
        tasks.find(cssClass("editing")).find(".edit").val(newText).pressEnter();
    }

    private void assertVisibleTasks(String... visibleTaskText) {
        tasks.filter(visible).shouldHave(exactTexts(visibleTaskText));
    }

    private void assertTasks(String... taskText) {
        tasks.shouldHave(exactTexts(taskText));
    }

    private void assertItemsLeftCounter(int n) {
        $("#todo-count").find("strong").shouldHave(exactText(Integer.toString(n)));
    }

    @After
    public void tearDown() throws IOException {
        screenshot();
    }

    @Attachment(type = "image/png")
    public byte[] screenshot() throws IOException {
        File screenshot = Screenshots.getScreenShotAsFile();
        return Files.toByteArray(screenshot);
    }
}
