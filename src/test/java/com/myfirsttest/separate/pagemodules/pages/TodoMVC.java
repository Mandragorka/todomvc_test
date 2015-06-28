package com.myfirsttest.separate.pagemodules.pages;

import com.codeborne.selenide.ElementsCollection;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMVC {

    public static ElementsCollection tasks = $$("#todo-list>li");

    @Step
    public static void filterAll() {
        $("[href='#/']").click();
    }

    @Step
    public static void filterActive() {
        $("[href='#/active']").click();
    }

    @Step
    public static void filterCompleted() {
        $("[href='#/completed']").click();
    }

    @Step
    public static void add(String text) {
        $("#new-todo").val(text).pressEnter();
    }

    @Step
    public static void deleteTask(String text) {
        tasks.find(exactText(text)).hover().find(".destroy").click();
    }

    @Step
    public static void toggleTask(String text) {
        tasks.findBy(exactText(text)).find(".toggle").click();
    }

    @Step
    public static void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public static void clearCompleted(){
        $("#clear-completed").click();
    }

    @Step
    public static void editTask(String textToEdit, String newText) {
        tasks.findBy(exactText(textToEdit)).find("label").doubleClick();
        tasks.find(cssClass("editing")).find(".edit").val(newText).pressEnter();
    }

    public static void assertVisibleTasks(String... visibleTaskText) {
        tasks.filter(visible).shouldHave(exactTexts(visibleTaskText));
    }

    public static void assertTasks(String... taskText) {
        TodoMVC.tasks.shouldHave(exactTexts(taskText));
    }

    public static void assertItemsLeftCounter(int n) {
        $("#todo-count>strong").shouldHave(exactText(Integer.toString(n)));
    }
}
