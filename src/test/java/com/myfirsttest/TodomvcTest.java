package com.myfirsttest;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class TodomvcTest {

    public static final ElementsCollection TASKS = $$("#todo-list li");
    public static final SelenideElement NEW_TASK = $("#new-todo");
    public static final SelenideElement ITEMS_LEFT_COUNT = $("#todo-count");
    public static final SelenideElement CLEAR_COMPLETED = $("#clear-completed");

    public static final SelenideElement FILTER_ACTIVE = $("[href='#/active']");
    public static final SelenideElement FILTER_ALL = $("[href='#/']");
    public static final SelenideElement FILTER_COMPLETED = $("[href='#/completed']");

    @Test
    public void todoMvcE2e() {
        open("http://todomvc.com/examples/troopjs_require/#");

        addTask("1");
        addTask("2");
        addTask("3");
        addTask("4");
        assertVisibleTasks("1", "2", "3", "4");
        assertActiveCount(4);

        destroyTask("2");
        assertVisibleTasks("1", "3", "4");
        assertActiveCount(3);

        toggleTask("4");
        assertActiveCount(2);
        assertCompletedCount(1);


        FILTER_ACTIVE.click();
        addTask("5");
        addTask("6");
        assertVisibleTasks("1", "3", "5", "6");

        editTask("5", "5 edited");
        assertActiveCount(4);
        assertCompletedCount(1);
        assertVisibleTasks("1", "3", "5 edited", "6");

        destroyTask("5 edited");
        assertActiveCount(3);
        assertVisibleTasks("1", "3", "6");

        toggleTask("3");
        assertActiveCount(2);
        assertCompletedCount(2);
        assertVisibleTasks("1", "6");


        toggleTask("6");
        FILTER_COMPLETED.click();
        assertCompletedCount(3);
        assertActiveCount(1);
        assertVisibleTasks("3", "4", "6");

        toggleTask("3");
        assertActiveCount(2);
        assertCompletedCount(2);
        assertVisibleTasks("4", "6");

        editTask("6", "6 edited");
        assertVisibleTasks("4", "6 edited");
        destroyTask("6 edited");
        assertCompletedCount(1);
        assertActiveCount(2);
        assertVisibleTasks("4");
        CLEAR_COMPLETED.click();


        FILTER_ALL.click();
        assertVisibleTasks("1", "3");
        toggleTask("3");
        assertActiveCount(1);
        assertCompletedCount(1);
        CLEAR_COMPLETED.click();
        assertVisibleTasks("1");

        editTask("1", "1 edited");
        assertVisibleTasks("1 edited");

        $("#toggle-all").click();
        assertActiveCount(0);
        assertCompletedCount(1);
        CLEAR_COMPLETED.click();
        TASKS.shouldBe(empty);
    }

    private void addTask(String task) {
        NEW_TASK.val(task).pressEnter();
    }

    private void destroyTask(String task) {
        TASKS.find(exactText(task)).hover();
        TASKS.find(exactText(task)).find(".destroy").click();
    }

    private void toggleTask(String task) {
        TASKS.findBy(exactText(task)).find(".toggle").click();
    }

    private void editTask(String taskToEdit, String newTask) {
        TASKS.findBy(exactText(taskToEdit)).find("label").doubleClick();
        TASKS.find(cssClass("editing")).find(".edit").val(newTask).pressEnter();
    }

    private void assertVisibleTasks(String ... visibleTasks) {
        TASKS.filter(visible).shouldHave(exactTexts(visibleTasks));
    }

    private void assertActiveCount(int n) {
        ITEMS_LEFT_COUNT.find("strong").shouldHave(exactText(Integer.toString(n)));
    }

    private void assertCompletedCount(int n) {
        CLEAR_COMPLETED.shouldHave(text("(" + n + ")"));
    }
}
