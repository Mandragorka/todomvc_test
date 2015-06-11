package com.myfirsttest.separate.pagemodules.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMVC {


    public static final ElementsCollection TASKS = $$("#todo-list>li");
    public static final SelenideElement NEW_TASK = $("#new-todo");
    public static final SelenideElement ITEMS_LEFT_COUNT = $("#todo-count");
    public static final SelenideElement CLEAR_COMPLETED = $("#clear-completed");

    public static final SelenideElement FILTER_ACTIVE = $("[href='#/active']");
    public static final SelenideElement FILTER_ALL = $("[href='#/']");
    public static final SelenideElement FILTER_COMPLETED = $("[href='#/completed']");

    @Step
    public static void addTask(String text) {
        NEW_TASK.val(text).pressEnter();
    }

    @Step
    public static void destroyTask(String text) {
        TASKS.find(exactText(text)).hover().find(".destroy").click();
    }

    @Step
    public static void toggleTask(String text) {
        TASKS.findBy(exactText(text)).find(".toggle").click();
    }

    @Step
    public static void clearCompleted(){
        CLEAR_COMPLETED.click();
    }

    @Step
    public static void editTask(String textToEdit, String newText) {
        TASKS.findBy(exactText(textToEdit)).find("label").doubleClick();
        TASKS.find(cssClass("editing")).find(".edit").val(newText).pressEnter();
    }

    public static void assertShownTasks(String... visibleTasks) {
        TASKS.filter(visible).shouldHave(exactTexts(visibleTasks));
    }

    public static void assertActiveCount(int n) {
        ITEMS_LEFT_COUNT.find("strong").shouldHave(exactText(Integer.toString(n)));
    }

    public static void assertCompletedCount(int n) {
        CLEAR_COMPLETED.shouldHave(text("(" + n + ")"));
    }

}
