package com.myfirsttest.separate.pageobjects;

import com.myfirsttest.separate.pageobjects.pages.TodoMVC;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Condition.visible;

public class TodomvcTest extends OpenTodoMVCWithClearedData {

    private TodoMVC page = new TodoMVC();

    @Test
    public void testAtAllFilter() {
        // Create tasks
        page.add("a");
        page.add("b");
        page.add("c");
        page.add("d");
        page.assertTasks("a", "b", "c", "d");
        page.assertItemsLeftCounter(4);

        // Task editing
        page.editTask("a", "a edited");
        page.assertTasks("a edited", "b", "c", "d");

        // Delete task
        page.deleteTask("b");
        page.assertTasks("a edited", "c", "d");
        page.assertItemsLeftCounter(3);

        // Mark tasks as completed
        page.toggleTask("d").toggleTask("c");
        page.assertItemsLeftCounter(1);

        // Mark task as reopened
        page.toggleTask("c");
        page.assertItemsLeftCounter(2);

        // Delete completed tasks
        page.clearCompleted();
        page.assertTasks("a edited", "c");

        // Mark all left tasks as completed
        page.toggleAll();
        page.assertItemsLeftCounter(0);

        // Delete completed tasks
        page.clearCompleted();
        page.tasks.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Given: create and toggle tasks at ALL filter
        page.add("a");
        page.add("b");
        page.toggleTask("b");
        page.add("c");
        page.toggleTask("c");
        page.toggleTask("c");

        // Switch to Active filter
        page.filterActive();
        page.assertItemsLeftCounter(2);
        page.assertVisibleTasks("a", "c");

        // Create task under Active filter
        page.add("d");
        page.assertVisibleTasks("a", "c", "d");
        page.assertItemsLeftCounter(3);
        page.filterAll();
        page.assertTasks("a", "b", "c", "d");
        page.filterActive();

        // ETask editing
        page.editTask("a", "a edited from active");
        page.assertVisibleTasks("a edited from active", "c", "d");

        // Delete task
        page.deleteTask("a edited from active");
        page.assertItemsLeftCounter(2);
        page.assertVisibleTasks("c", "d");

        // Mark tasks as completed
        page.toggleTask("d");
        page.assertItemsLeftCounter(1);
        page.assertVisibleTasks("c");

        // Check tasks at completed filter
        page.filterCompleted();
        page.assertVisibleTasks("b", "d");

        // Mark task as reopened
        page.filterAll();
        page.toggleTask("b");
        page.filterActive();
        page.assertVisibleTasks("b", "c");
        page.assertItemsLeftCounter(2);

        // Mark all active tasks as completed
        page.toggleAll();
        page.tasks.filter(visible).shouldBe(empty);
    }

    @Test
    public void testAtCompletedFilter() {
        // Given: create and toggle tasks at ALL filter
        page.add("a");
        page.add("b");
        page.add("c");
        page.toggleAll();
        page.add("d");

        // Switch to Completed filter
        page.filterCompleted();
        page.assertVisibleTasks("a", "b", "c");
        page.assertItemsLeftCounter(1);

        // Mark task as reopened
        page.toggleTask("c");
        page.assertItemsLeftCounter(2);
        page.assertVisibleTasks("a", "b");
        page.filterAll();
        page.assertVisibleTasks("a", "b", "c", "d");
        page.filterActive();
        page.assertVisibleTasks("c", "d");
        page.filterCompleted();

        // Editing of existing task
        page.editTask("b", "b edited from completed");
        page.assertVisibleTasks("a", "b edited from completed");

        // Delete edited task
        page.deleteTask("b edited from completed");
        page.assertItemsLeftCounter(2);
        page.assertVisibleTasks("a");

        // Removing all completed tasks
        page.filterCompleted();
        page.clearCompleted();
        page.tasks.filter(visible).shouldBe(empty);
    }
}
