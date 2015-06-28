package com.myfirsttest.separate.pageobjects;

import com.myfirsttest.separate.pageobjects.pages.TodoMVC;
import org.junit.Test;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class TodomvcTest extends OpenTodoMVCWithClearedData {

    private TodoMVC page = new TodoMVC();

    @Test
    public void testAtAllFilter() {
        // Create tasks
        page.add("a").add("b").add("c").add("d");
        assertTasks("a", "b", "c", "d");
        assertItemsLeftCounter(4);

        // Editing of existing task
        page.editTask("a", "a edited");
        assertTasks("a edited", "b", "c", "d");

        // Delete task
        page.deleteTask("b");
        assertTasks("a edited", "c", "d");
        assertItemsLeftCounter(3);

        // Mark tasks as completed
        page.toggleTask("d").toggleTask("c");
        assertItemsLeftCounter(1);

        // Mark task as reopened
        page.toggleTask("c");
        assertItemsLeftCounter(2);

        page.clearCompleted();
        assertTasks("a edited", "c");

        // Mark all left tasks as completed and then their removing
        page.toggleAll();
        assertItemsLeftCounter(0);
        page.clearCompleted();
        page.tasks.shouldBe(empty);
    }

    @Test
    public void testAtActiveFilter() {
        // Given
        page.add("a").add("b");
        page.toggleTask("b");
        page.add("c");
        page.toggleTask("c");
        page.toggleTask("c");
        page.filterActive();
        assertItemsLeftCounter(2);
        assertVisibleTasks("a", "c");

        // Create task under Active filter
        page.add("d");
        assertVisibleTasks("a", "c", "d");
        assertItemsLeftCounter(3);
        page.filterAll();
        assertTasks("a", "b", "c", "d");
        page.filterActive();

        // Editing of existing task
        page.editTask("a", "a edited from active");
        assertVisibleTasks("a edited from active", "c", "d");

        // Delete task
        page.deleteTask("a edited from active");
        assertItemsLeftCounter(2);
        assertVisibleTasks("c", "d");

        // Mark tasks as completed
        page.toggleTask("d");
        assertItemsLeftCounter(1);
        assertVisibleTasks("c");

        page.filterCompleted();
        assertVisibleTasks("b", "d");
        page.filterAll();

        // Mark task as reopened
        page.toggleTask("b");
        assertItemsLeftCounter(2);
        assertVisibleTasks("b", "c", "d");
        page.filterActive();
        assertVisibleTasks("b", "c");

        page.toggleAll();
        page.tasks.filter(visible).shouldBe(empty);
    }

    @Test
    public void testAtCompletedFilter() {
        // Given
        page.add("a").add("b").add("c");
        page.toggleAll();
        page.add("d");
        page.filterCompleted();
        assertVisibleTasks("a", "b", "c");
        assertItemsLeftCounter(1);

        // Mark task as reopened
        page.toggleTask("c");
        assertItemsLeftCounter(2);
        assertVisibleTasks("a", "b");
        page.filterAll();
        assertVisibleTasks("a", "b", "c", "d");
        page.filterActive();
        assertVisibleTasks("c", "d");
        page.filterCompleted();

        // Editing of existing task
        page.editTask("b", "b edited from completed");
        assertVisibleTasks("a", "b edited from completed");

        // Delete edited task and then removing all completed tasks
        page.deleteTask("b edited from completed");
        assertItemsLeftCounter(2);
        assertVisibleTasks("a");

        page.filterCompleted();
        page.clearCompleted();
        page.tasks.filter(visible).shouldBe(empty);
    }

    public void assertVisibleTasks(String... visibleTaskText) {
        page.tasks.filter(visible).shouldHave(exactTexts(visibleTaskText));
    }

    public void assertTasks(String... taskText) {
        page.tasks.shouldHave(exactTexts(taskText));
    }

    public void assertItemsLeftCounter(int n) {
        page.getTodoCount().shouldHave(exactText(Integer.toString(n)));
    }
}
