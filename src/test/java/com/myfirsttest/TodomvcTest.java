package com.myfirsttest;

import org.junit.Test;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TodomvcTest {

    public static ElementsCollection tasks = $$("#todo-list li");
    public static SelenideElement itemsLeftCounter = $("#todo-count");
    public static SelenideElement clearCompleted = $("#clear-completed");

    public static SelenideElement filterActive = $("[href='#/active']");
    public static SelenideElement filterAll = $("[href='#/']");
    public static SelenideElement filterCompleted = $("[href='#/completed']");


    private void addTask(String task) {
        $("#new-todo").val(task).pressEnter();
    }

    private void destroyTask(String task) {
        tasks.find(text(task)).hover();
        tasks.find(text(task)).find(".destroy").click();
    }

    private void toggleTask(String task) {
        tasks.findBy(text(task)).find(".toggle").click();
    }

    private void editTask(String taskToEdit, String newTask) {
        actions().doubleClick(tasks.findBy(text(taskToEdit)).find("label")).perform();
        tasks.find(cssClass("editing")).find(".edit").val(newTask).pressEnter();
    }

    private void assertActiveCount(int n) {
        itemsLeftCounter.find("strong").shouldHave(text(Integer.toString(n)));
    }

    private void assertCompletedCount(int n) {
        clearCompleted.shouldHave(text("(" + n + ")"));
    }

    @Test
    public void enterTasks() {
        open("http://todomvc.com/examples/troopjs_require/#");

        addTask("1");
        addTask("2");
        addTask("3");
        addTask("4");
        tasks.shouldHave(exactTexts("1", "2", "3", "4"));
        assertActiveCount(4);

        destroyTask("2");
        tasks.shouldHave(exactTexts("1", "3", "4"));
        assertActiveCount(3);

        toggleTask("4");
        assertActiveCount(2);
        assertCompletedCount(1);

        filterActive.click();
        addTask("5");
        tasks.filter(visible).shouldHave(texts("1", "3", "5"));
        editTask("5", "5 edited");
        assertActiveCount(3);
        assertCompletedCount(1);
        tasks.filter(visible).shouldHave(texts("1", "3", "5 edited"));
        destroyTask("5 edited");
        assertActiveCount(2);
        tasks.filter(visible).shouldHave(texts("1", "3"));
        toggleTask("3");
        assertActiveCount(1);
        assertCompletedCount(2);
        tasks.filter(visible).shouldHave(texts("1"));

        addTask("6");
        toggleTask("6");
        filterCompleted.click();
        assertCompletedCount(3);
        assertActiveCount(1);
        tasks.filter(visible).shouldHave(texts("3", "4", "6"));
        toggleTask("3");
        assertActiveCount(2);
        assertCompletedCount(2);
        tasks.filter(visible).shouldHave(texts("4", "6"));
        editTask("6", "6 edited");
        tasks.filter(visible).shouldHave(texts("4", "6 edited"));
        destroyTask("6");
        assertCompletedCount(1);
        assertActiveCount(2);
        tasks.filter(visible).shouldHave(texts("4"));
        clearCompleted.click();

        filterAll.click();
        tasks.shouldHave(exactTexts("1", "3"));
        toggleTask("3");
        assertActiveCount(1);
        assertCompletedCount(1);
        clearCompleted.click();
        tasks.shouldHave(exactTexts("1"));
        editTask("1", "1 edited");
        tasks.shouldHave(exactTexts("1 edited"));

        $("#toggle-all").click();
        assertActiveCount(0);
        assertCompletedCount(1);

        clearCompleted.click();
        tasks.shouldBe(empty);
    }
}
