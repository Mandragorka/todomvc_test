package com.myfirsttest.separate.pageobjects.pages;

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


    public final ElementsCollection TASKS = $$("#todo-list>li");
    public final SelenideElement NEW_TASK = $("#new-todo");
    public final SelenideElement ITEMS_LEFT_COUNT = $("#todo-count");
    public final SelenideElement CLEAR_COMPLETED = $("#clear-completed");

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
    public void addTask(String text) {
        NEW_TASK.val(text).pressEnter();
    }

    @Step
    public void destroyTask(String text) {
        TASKS.find(exactText(text)).hover().find(".destroy").click();
    }

    @Step
    public void toggleTask(String text) {
        TASKS.findBy(exactText(text)).find(".toggle").click();
    }

    @Step
    public void clearCompleted(){
        CLEAR_COMPLETED.click();
    }

    @Step
    public void editTask(String textToEdit, String newText) {
        TASKS.findBy(exactText(textToEdit)).find("label").doubleClick();
        TASKS.find(cssClass("editing")).find(".edit").val(newText).pressEnter();
    }

    public void assertShownTasks(String... visibleTasks) {
        TASKS.filter(visible).shouldHave(exactTexts(visibleTasks));
    }

    public void assertItemsLeftCounter(int n) {
        ITEMS_LEFT_COUNT.find("strong").shouldHave(exactText(Integer.toString(n)));
    }

    public void assertCompletedCount(int n) {
        CLEAR_COMPLETED.shouldHave(text("(" + n + ")"));
    }

}
