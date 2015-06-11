package com.myfirsttest.separate.pagemodules;

import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Selenide.$;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.addTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertActiveCount;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertShownTasks;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.clearCompleted;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.destroyTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.editTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.FILTER_ACTIVE;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.FILTER_COMPLETED;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.FILTER_ALL;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.TASKS;
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

}
