package com.myfirsttest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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

public class TodomvcTest {

    public static final ElementsCollection TASKS = $$("#todo-list>li");
    public static final SelenideElement NEW_TASK = $("#new-todo");
    public static final SelenideElement ITEMS_LEFT_COUNT = $("#todo-count");
    public static final SelenideElement CLEAR_COMPLETED = $("#clear-completed");

    public static final SelenideElement FILTER_ACTIVE = $("[href='#/active']");
    public static final SelenideElement FILTER_ALL = $("[href='#/']");
    public static final SelenideElement FILTER_COMPLETED = $("[href='#/completed']");

    @Before
    public void setUp() {
        open("http://todomvc.com/examples/troopjs_require/#");
    }

    @After
    public void tearDown() {
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
        assertActiveCount(4);

        // Delete task
        destroyTask("b");
        assertShownTasks("a", "c", "d");
        assertActiveCount(3);

        // Mark task as completed
        toggleTask("d");
        assertActiveCount(2);
        //assertCompletedCount(1);

        //Mark task as completed and then removing completed
        toggleTask("c");
        assertActiveCount(1);
        //assertCompletedCount(2);

        // Displaying tasks at active filter
        FILTER_ACTIVE.click();
        assertShownTasks("a");

        // Displaying tasks at completed filter
        FILTER_COMPLETED.click();
        assertShownTasks("c", "d");

        FILTER_ALL.click();
        clearCompleted();
        assertShownTasks("a");

        // Editing of existing task
        editTask("a", "all - a");
        assertShownTasks("all - a");

        // Mark all left tasks as completed and then their removing
        $("#toggle-all").click();
        assertActiveCount(0);
        //assertCompletedCount(1);
        clearCompleted();
        TASKS.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Precondition
        addTask("a");
        addTask("b");
        assertShownTasks("a", "b");
        assertActiveCount(2);

        FILTER_ACTIVE.click();
        // Create task under Active filter
        addTask("c");
        assertShownTasks("a", "b", "c");
        assertActiveCount(3);

        // Editing of existing task
        editTask("a", "active - a");
        assertShownTasks("active - a", "b", "c");

        // Delete task
        destroyTask("active - a");
        assertActiveCount(2);
        assertShownTasks("b", "c");

        // Mark tasks as completed
        toggleTask("c");
        assertActiveCount(1);
        //assertCompletedCount(1);
        assertShownTasks("b");

        // Displaying tasks at active filter
        FILTER_ACTIVE.click();
        assertShownTasks("b");

        // Displaying tasks at completed filter
        FILTER_COMPLETED.click();
        assertShownTasks("c");

        FILTER_ALL.click();
        // Mark task as reopened
        toggleTask("c");
        assertActiveCount(2);

        // Displaying tasks at all filter
        FILTER_ALL.click();
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

        FILTER_COMPLETED.click();
        assertShownTasks("a", "b", "c");
        assertActiveCount(0);

        // Mark task as reopened
        toggleTask("c");
        assertActiveCount(1);
        //assertCompletedCount(2);
        assertShownTasks("a", "b");

        // Editing of existing task
        editTask("b", "completed - b");
        assertShownTasks("a", "completed - b");
        // Delete edited task and then removing all completed tasks
        destroyTask("completed - b");
        //assertCompletedCount(1);
        assertActiveCount(1);
        assertShownTasks("a");

        // Displaying tasks at active filter
        FILTER_ACTIVE.click();
        assertShownTasks("c");

        // Displaying tasks at all filter
        FILTER_ALL.click();
        assertShownTasks("a", "c");

        FILTER_COMPLETED.click();
        clearCompleted();
    }

    private void addTask(String task) {
        NEW_TASK.val(task).pressEnter();
    }

    private void destroyTask(String task) {
        TASKS.find(exactText(task)).hover().find(".destroy").click();
    }

    private void toggleTask(String task) {
        TASKS.findBy(exactText(task)).find(".toggle").click();
    }

    private void clearCompleted(){
        CLEAR_COMPLETED.click();
    }

    private void editTask(String taskToEdit, String newTask) {
        TASKS.findBy(exactText(taskToEdit)).find("label").doubleClick();
        TASKS.find(cssClass("editing")).find(".edit").val(newTask).pressEnter();
    }

    private void assertShownTasks(String... visibleTasks) {
        TASKS.filter(visible).shouldHave(exactTexts(visibleTasks));
    }

    private void assertActiveCount(int n) {
        ITEMS_LEFT_COUNT.find("strong").shouldHave(exactText(Integer.toString(n)));
    }

    private void assertCompletedCount(int n) {
        CLEAR_COMPLETED.shouldHave(text("(" + n + ")"));
    }
}
