package com.myfirsttest.separate.pagemodules;

import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Selenide.$;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.TASKS;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.addTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertItemsLeftCounter;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertShownTasks;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.clearCompleted;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.destroyTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.editTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.filterActive;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.filterAll;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.filterCompleted;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.toggleTask;


public class TodomvcTest extends OpenTodoMVCWithClearedDataBeforeEachTest {

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

}
