package com.myfirsttest;

import com.codeborne.selenide.SelenideElement;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Mandragorka on 4/26/15.
 */
public class TodomvcTest {

    @Test
    public void enterTasks() {
        open("http://todomvc.com/examples/troopjs_require/#");

        String t1 = "1. Practice kindness",
               t2 = "=(^.^)=",
               t3 = "- be productive yet calm ;)",
               t4 = " Don't forget to smile! @#$%^&*() and yaaaaaaaaaaaaaaaaaaaaaaz";

        List<String> taskList = Arrays.asList(t1, t2, t3, t4);
        for (String task : taskList) {
           createTask(task);
        }

        // Вирішила перевірити не красиво :)
        for (int i = 0; i < taskList.size(); i++) {
            assertEquals(taskList.get(i).trim(), $$("#todo-list li.active").get(i).getText());
        }

        // Delete second task
        SelenideElement task = $$("#todo-list li.active").get(1);
        task.hover();
        task.find(".destroy").click();

        // Make sure that task was deleted
        assertFalse($$("#todo-list li.active").get(1).getText().startsWith(t2));

        // Mark 4th task as a completed
        $$("#todo-list li.active").get(2).shouldHave(text(t4)).find(".toggle").click();

        filterActive(2);
        filterCompleted(1);
        backToAll();

        // Clear 4th task
        clearCompleted();

        // Make sure that 4th task was deleted
        assertFalse($("#todo-list").getText().contains(t4));
        // потипу, можна ще ось так
        //assertEquals(2, $$("#todo-list li.active").size());

        // Mark ALL tasks as completed
        $("#toggle-all").shouldHave(type("checkbox")).click();

        // Clear all completed tasks
        clearCompleted();

        assertEquals(0, $$("#todo-list li.active").size());
    }

    // Method for creating new task
    private void createTask(String taskName) {
        $("#new-todo").val(taskName).pressEnter();
    }

    // Method for deleting completed tasks
    private void clearCompleted() {
        $("#clear-completed").click();
    }

    // Method for Active filter check
    private void filterActive(int activeCount) {
        $$("#filters a").find(text("Active")).click();
        assertTrue($$("#filters a").find(text("Active")).has(cssClass("selected")));
        assertTrue($$(".completed").filter(visible).isEmpty());
        assertEquals(activeCount, $$(".active").size());
    }

    // Method for Completed filter check
    private void filterCompleted(int completedCount) {
        $$("#filters a").find(text("Completed")).click();
        assertTrue($$("#filters a").find(text("Completed")).has(cssClass("selected")));
        assertTrue($$(".active").filter(visible).isEmpty());
        assertEquals(completedCount, $$(".completed").size());
    }

    // Method for returning to full list
    private void backToAll() {
        $$("#filters a").find(text("All")).click();
        assertTrue($$("#filters a").find(text("All")).has(cssClass("selected")));
    }
}
