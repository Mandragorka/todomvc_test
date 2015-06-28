package com.myfirsttest.separate.pagemodules;

import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.visible;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.add;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertItemsLeftCounter;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertTasks;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.assertVisibleTasks;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.clearCompleted;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.deleteTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.editTask;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.filterActive;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.filterAll;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.filterCompleted;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.tasks;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.toggleAll;
import static com.myfirsttest.separate.pagemodules.pages.TodoMVC.toggleTask;


public class TodomvcTest extends OpenTodoMVCWithClearedDataBeforeEachTest {

    @Test
    public void testAtAllFilter() {
        // Create tasks
        add("a");
        add("b");
        add("c");
        add("d");
        assertTasks("a", "b", "c", "d");
        assertItemsLeftCounter(4);

        // Editing of existing task
        editTask("a", "a edited");
        assertTasks("a edited", "b", "c", "d");

        // Delete task
        deleteTask("b");
        assertTasks("a edited", "c", "d");
        assertItemsLeftCounter(3);

        // Mark tasks as completed
        toggleTask("d");
        toggleTask("c");
        assertItemsLeftCounter(1);

        // Mark task as reopened
        toggleTask("c");
        assertItemsLeftCounter(2);

        clearCompleted();
        assertTasks("a edited", "c");

        // Mark all left tasks as completed and then their removing
        toggleAll();
        assertItemsLeftCounter(0);
        clearCompleted();
        tasks.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Given
        add("a");
        add("b");
        toggleTask("b");
        add("c");
        toggleTask("c");
        toggleTask("c");
        filterActive();
        assertItemsLeftCounter(2);
        assertVisibleTasks("a", "c");

        // Create task under Active filter
        add("d");
        assertVisibleTasks("a", "c", "d");
        assertItemsLeftCounter(3);
        filterAll();
        assertTasks("a", "b", "c", "d");
        filterActive();

        // Editing of existing task
        editTask("a", "a edited from active");
        assertVisibleTasks("a edited from active", "c", "d");

        // Delete task
        deleteTask("a edited from active");
        assertItemsLeftCounter(2);
        assertVisibleTasks("c", "d");

        // Mark tasks as completed
        toggleTask("d");
        assertItemsLeftCounter(1);
        assertVisibleTasks("c");

        filterCompleted();
        assertVisibleTasks("b", "d");
        filterAll();

        // Mark task as reopened
        toggleTask("b");
        assertItemsLeftCounter(2);
        assertVisibleTasks("b", "c", "d");
        filterActive();
        assertVisibleTasks("b", "c");

        toggleAll();
        tasks.filter(visible).shouldBe(empty);
    }

    @Test
    public void testAtCompletedFilter() {
        // Given
        add("a");
        add("b");
        add("c");
        toggleAll();
        add("d");
        filterCompleted();
        assertVisibleTasks("a", "b", "c");
        assertItemsLeftCounter(1);

        // Mark task as reopened
        toggleTask("c");
        assertItemsLeftCounter(2);
        assertVisibleTasks("a", "b");
        filterAll();
        assertVisibleTasks("a", "b", "c", "d");
        filterActive();
        assertVisibleTasks("c", "d");
        filterCompleted();

        // Editing of existing task
        editTask("b", "b edited from completed");
        assertVisibleTasks("a", "b edited from completed");

        // Delete edited task and then removing all completed tasks
        deleteTask("b edited from completed");
        assertItemsLeftCounter(2);
        assertVisibleTasks("a");

        filterCompleted();
        clearCompleted();
        tasks.filter(visible).shouldBe(empty);
    }

}
