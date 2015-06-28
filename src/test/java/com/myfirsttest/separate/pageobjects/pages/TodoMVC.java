package com.myfirsttest.separate.pageobjects.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.cssClass;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TodoMVC {

    public ElementsCollection tasks = $$("#todo-list>li");

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
    public TodoMVC add(String text) {
        $("#new-todo").val(text).pressEnter();
        return this;
    }

    @Step
    public void deleteTask(String text) {
        tasks.find(exactText(text)).hover().find(".destroy").click();
    }

    @Step
    public TodoMVC toggleTask(String text) {
        tasks.findBy(exactText(text)).find(".toggle").click();
        return this;
    }

    @Step
    public void toggleAll() {
        $("#toggle-all").click();
    }

    @Step
    public void clearCompleted(){
        $("#clear-completed").click();
    }

    @Step
    public void editTask(String textToEdit, String newText) {
        tasks.findBy(exactText(textToEdit)).find("label").doubleClick();
        tasks.find(cssClass("editing")).find(".edit").val(newText).pressEnter();
    }

    public SelenideElement getTodoCount() {
        return $("#todo-count>strong");
    }

}
