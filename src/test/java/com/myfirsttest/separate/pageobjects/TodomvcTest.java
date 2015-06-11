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
        page.assertActiveCount(4);

        // Delete task
        page.destroyTask("b");
        page.assertShownTasks("a", "c", "d");
        page.assertActiveCount(3);

        // Mark task as completed
        page.toggleTask("d");
        page.assertActiveCount(2);
        //page.assertCompletedCount(1);

        //Mark task as completed and then removing completed
        page.toggleTask("c");
        page.assertActiveCount(1);
        //page.assertCompletedCount(2);

        // Displaying tasks at active filter
        page.FILTER_ACTIVE.click();
        page.assertShownTasks("a");

        // Displaying tasks at completed filter
        page.FILTER_COMPLETED.click();
        page.assertShownTasks("c", "d");

        page.FILTER_ALL.click();
        page.clearCompleted();
        page.assertShownTasks("a");

        // Editing of existing task
        page.editTask("a", "all - a");
        page.assertShownTasks("all - a");

        // Mark all left tasks as completed and then their removing
        $("#toggle-all").click();
        page.assertActiveCount(0);
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
        page.assertActiveCount(2);

        page.FILTER_ACTIVE.click();
        // Create task under Active filter
        page.addTask("c");
        page.assertShownTasks("a", "b", "c");
        page.assertActiveCount(3);

        // Editing of existing task
        page.editTask("a", "active - a");
        page.assertShownTasks("active - a", "b", "c");

        // Delete task
        page.destroyTask("active - a");
        page.assertActiveCount(2);
        page.assertShownTasks("b", "c");

        // Mark tasks as completed
        page.toggleTask("c");
        page.assertActiveCount(1);
        //page.assertCompletedCount(1);
        page.assertShownTasks("b");

        // Displaying tasks at active filter
        page.FILTER_ACTIVE.click();
        page.assertShownTasks("b");

        // Displaying tasks at completed filter
        page.FILTER_COMPLETED.click();
        page.assertShownTasks("c");

        page.FILTER_ALL.click();
        // Mark task as reopened
        page.toggleTask("c");
        page.assertActiveCount(2);

        // Displaying tasks at all filter
        page.FILTER_ALL.click();
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

        page.FILTER_COMPLETED.click();
        page.assertShownTasks("a", "b", "c");
        page.assertActiveCount(0);

        // Mark task as reopened
        page.toggleTask("c");
        page.assertActiveCount(1);
        //page.assertCompletedCount(2);
        page.assertShownTasks("a", "b");

        // Editing of existing task
        page.editTask("b", "completed - b");
        page.assertShownTasks("a", "completed - b");
        // Delete edited task and then removing all completed tasks
        page.destroyTask("completed - b");
        //page.assertCompletedCount(1);
        page.assertActiveCount(1);
        page.assertShownTasks("a");

        // Displaying tasks at active filter
        page.FILTER_ACTIVE.click();
        page.assertShownTasks("c");

        // Displaying tasks at all filter
        page.FILTER_ALL.click();
        page.assertShownTasks("a", "c");

        page.FILTER_COMPLETED.click();
        page.clearCompleted();
    }

}
