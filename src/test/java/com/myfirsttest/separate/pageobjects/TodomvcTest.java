package com.myfirsttest.separate.pageobjects;

import com.myfirsttest.separate.pageobjects.pages.TodoMVC;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.Selenide.$;


public class TodomvcTest extends OpenTodoMVCWithClearedDataBeforeEachTest {

    TodoMVC page = new TodoMVC();

    @Test
    public void testAtAllFilter() {

        // Precondition
        page.addTask("a");
        page.addTask("b");
        page.addTask("c");
        page.addTask("d");
        page.assertShownTasks("a", "b", "c", "d");
        page.assertItemsLeftCounter(4);

        // Delete task
        page.destroyTask("b");
        page.assertShownTasks("a", "c", "d");
        page.assertItemsLeftCounter(3);

        // Mark tasks as completed
        page.toggleTask("d");
        page.assertItemsLeftCounter(2);
        //page.assertCompletedCount(1);
        page.toggleTask("c");
        page.assertItemsLeftCounter(1);
        //page.assertCompletedCount(2);

        // Displaying tasks at active filter
        page.filterActive();
        page.assertShownTasks("a");

        // Displaying tasks at completed filter
        page.filterCompleted();
        page.assertShownTasks("c", "d");

        page.filterAll();
        page.clearCompleted();
        page.assertShownTasks("a");

        // Editing of existing task
        page.editTask("a", "all: a edited");
        page.assertShownTasks("all: a edited");

        // Mark all left tasks as completed and then their removing
        $("#toggle-all").click();
        page.assertItemsLeftCounter(0);
        //page.assertCompletedCount(1);
        page.clearCompleted();
        page.TASKS.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Precondition
        page.addTask("a");
        page.addTask("b");
        page.assertShownTasks("a", "b");
        page.assertItemsLeftCounter(2);

        page.filterActive();
        // Create task under Active filter
        page.addTask("c");
        page.assertShownTasks("a", "b", "c");
        page.assertItemsLeftCounter(3);

        // Editing of existing task
        page.editTask("a", "active: a edited");
        page.assertShownTasks("active: a edited", "b", "c");

        // Delete task
        page.destroyTask("active: a edited");
        page.assertItemsLeftCounter(2);
        page.assertShownTasks("b", "c");

        // Mark tasks as completed
        page.toggleTask("c");
        page.assertItemsLeftCounter(1);
        //page.assertCompletedCount(1);
        page.assertShownTasks("b");

        // Displaying tasks at completed filter
        page.filterCompleted();
        page.assertShownTasks("c");

        page.filterAll();
        // Mark task as reopened
        page.toggleTask("c");
        page.assertItemsLeftCounter(2);

        // Displaying tasks at all filter
        page.filterActive();
        page.assertShownTasks("b", "c");

    }

    @Test
    public void testAtCompletedFilter() {
        // Precondition
        page.addTask("a");
        page.addTask("b");
        page.addTask("c");
        page.assertShownTasks("a", "b", "c");
        $("#toggle-all").click();

        page.filterCompleted();
        page.assertShownTasks("a", "b", "c");
        page.assertItemsLeftCounter(0);

        // Mark task as reopened
        page.toggleTask("c");
        page.assertItemsLeftCounter(1);
        //page.assertCompletedCount(2);
        page.assertShownTasks("a", "b");

        // Editing of existing task
        page.editTask("b", "completed: b edited");
        page.assertShownTasks("a", "completed: b edited");
        // Delete edited task and then removing all completed tasks
        page.destroyTask("completed: b edited");
        //page.assertCompletedCount(1);
        page.assertItemsLeftCounter(1);
        page.assertShownTasks("a");

        // Displaying tasks at active filter
        page.filterActive();
        page.assertShownTasks("c");

        // Displaying tasks at all filter
        page.filterAll();
        page.assertShownTasks("a", "c");

        page.filterCompleted();
        page.clearCompleted();
    }

}
