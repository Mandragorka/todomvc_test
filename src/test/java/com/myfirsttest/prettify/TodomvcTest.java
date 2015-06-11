package com.myfirsttest.prettify;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Screenshots;
import com.codeborne.selenide.SelenideElement;
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
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.executeJavaScript;
import static com.codeborne.selenide.Selenide.open;


public class TodomvcTest extends OpenTodoMVCWithClearedDataAfterEachTest {

    public static final ElementsCollection TASKS = $$("#todo-list>li");
    public static final SelenideElement NEW_TASK = $("#new-todo");
    public static final SelenideElement ITEMS_LEFT_COUNT = $("#todo-count");
    public static final SelenideElement CLEAR_COMPLETED = $("#clear-completed");

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

        // Precondition
        addTask("a");
        addTask("b");
        addTask("c");
        addTask("d");
        assertShownTasks("a", "b", "c", "d");
        assertItemsLeftCounter(4);

        // Delete task
        destroyTask("b");
        assertShownTasks("a", "c", "d");
        assertItemsLeftCounter(3);

        // Mark tasks as completed
        toggleTask("d");
        assertItemsLeftCounter(2);
        //page.assertCompletedCount(1);
        toggleTask("c");
        assertItemsLeftCounter(1);
        //page.assertCompletedCount(2);

        // Displaying tasks at active filter
        filterActive();
        assertShownTasks("a");

        // Displaying tasks at completed filter
        filterCompleted();
        assertShownTasks("c", "d");

        filterAll();
        clearCompleted();
        assertShownTasks("a");

        // Editing of existing task
        editTask("a", "all: a edited");
        assertShownTasks("all: a edited");

        // Mark all left tasks as completed and then their removing
        $("#toggle-all").click();
        assertItemsLeftCounter(0);
        //page.assertCompletedCount(1);
        clearCompleted();
        TASKS.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Precondition
        addTask("a");
        addTask("b");
        assertShownTasks("a", "b");
        assertItemsLeftCounter(2);

        filterActive();
        // Create task under Active filter
        addTask("c");
        assertShownTasks("a", "b", "c");
        assertItemsLeftCounter(3);

        // Editing of existing task
        editTask("a", "active: a edited");
        assertShownTasks("active: a edited", "b", "c");

        // Delete task
        destroyTask("active: a edited");
        assertItemsLeftCounter(2);
        assertShownTasks("b", "c");

        // Mark tasks as completed
        toggleTask("c");
        assertItemsLeftCounter(1);
        //page.assertCompletedCount(1);
        assertShownTasks("b");

        // Displaying tasks at completed filter
        filterCompleted();
        assertShownTasks("c");

        filterAll();
        // Mark task as reopened
        toggleTask("c");
        assertItemsLeftCounter(2);

        // Displaying tasks at all filter
        filterActive();
        assertShownTasks("b", "c");

    }

    @Test
    public void testAtCompletedFilter() {
        // Precondition
        addTask("a");
        addTask("b");
        addTask("c");
        assertShownTasks("a", "b", "c");
        $("#toggle-all").click();

        filterCompleted();
        assertShownTasks("a", "b", "c");
        assertItemsLeftCounter(0);

        // Mark task as reopened
        toggleTask("c");
        assertItemsLeftCounter(1);
        //page.assertCompletedCount(2);
        assertShownTasks("a", "b");

        // Editing of existing task
        editTask("b", "completed: b edited");
        assertShownTasks("a", "completed: b edited");
        // Delete edited task and then removing all completed tasks
        destroyTask("completed: b edited");
        //page.assertCompletedCount(1);
        assertItemsLeftCounter(1);
        assertShownTasks("a");

        // Displaying tasks at active filter
        filterActive();
        assertShownTasks("c");

        // Displaying tasks at all filter
        filterAll();
        assertShownTasks("a", "c");

        filterCompleted();
        clearCompleted();
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
    private void addTask(String text) {
        NEW_TASK.val(text).pressEnter();
    }

    @Step
    private void destroyTask(String text) {
        TASKS.find(exactText(text)).hover().find(".destroy").click();
    }

    @Step
    private void toggleTask(String text) {
        TASKS.findBy(exactText(text)).find(".toggle").click();
    }

    @Step
    private void clearCompleted(){
        CLEAR_COMPLETED.click();
    }

    @Step
    private void editTask(String textToEdit, String newText) {
        TASKS.findBy(exactText(textToEdit)).find("label").doubleClick();
        TASKS.find(cssClass("editing")).find(".edit").val(newText).pressEnter();
    }

    private void assertShownTasks(String... visibleTasks) {
        TASKS.filter(visible).shouldHave(exactTexts(visibleTasks));
    }

    private void assertItemsLeftCounter(int n) {
        ITEMS_LEFT_COUNT.find("strong").shouldHave(exactText(Integer.toString(n)));
    }

    private void assertCompletedCount(int n) {
        CLEAR_COMPLETED.shouldHave(text("(" + n + ")"));
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
