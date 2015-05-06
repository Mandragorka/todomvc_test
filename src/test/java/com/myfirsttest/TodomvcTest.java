package com.myfirsttest;

        import org.junit.Test;

        import java.util.Arrays;
        import java.util.List;


        import static com.codeborne.selenide.Condition.*;
        import static com.codeborne.selenide.Selenide.*;

public class TodomvcTest {

    @Test
    public void enterTasks() {
        String todoListSelector = ("#todo-list li"),
                filterSelector = ("#filters a"),
                activeTaskSelector = ("#todo-count"),
                completedTaskSelector = ("#clear-completed");

        open("http://todomvc.com/examples/troopjs_require/#");

        // Initial checking
        $("#main").shouldBe(hidden);
        $("#footer").shouldBe(hidden);

        String t1 = "1. Practice kindness",
                t2 = "=(^.^)=",
                t3 = "- be productive yet calm ;)",
                t4 = " Don't forget to smile! @#$%^&*() and yaaaaaaaaaaaaaaaaaaaaaaz"; // ось тут text wrapping не працює

        List<String> taskList = Arrays.asList(t1, t2, t3, t4);
        for (String task : taskList) {
            createTask(task);
        }

        for (int i = 0; i < taskList.size(); i++) {
            $$(todoListSelector).get(i).shouldHave(text(taskList.get(i)));
        }

        // Перевіряю одразу кількість активних тасок
        $(activeTaskSelector).shouldHave(text("4"));
        // Потім перевірка для clear-completed коли знаходимось під фільтром All
        $$(filterSelector).find(text("All")).has(cssClass("selected"));
        $(completedTaskSelector).shouldBe(hidden).shouldHave("0"); //???

        // Delete second task
        $$(todoListSelector).get(1).hover();
        $$(todoListSelector).get(1).find(".destroy").click();

        // Make sure that task was deleted
        $$(todoListSelector).get(1).shouldNotHave(text(t2));
        // Checks count of active items after deleting 2nd task
        $(activeTaskSelector).shouldHave(text("3"));

        // Mark 4th task as a completed
        $$(todoListSelector).get(2).shouldHave(text(t4)).find(".toggle").click();

        // Active filter verification
        $$(filterSelector).find(text("Active")).click();
        $$(filterSelector).find(text("Active")).shouldHave(cssClass("selected"));
        $(".completed").shouldBe(hidden);
        $$(".active").shouldHaveSize(2);

        // Completed filter verification
        $$(filterSelector).find(text("Completed")).click();
        $$(filterSelector).find(text("Completed")).shouldHave(cssClass("selected"));
        $(".active").shouldBe(hidden);
        $$(".completed").shouldHaveSize(1);

        // Back to All filter
        $$(filterSelector).find(text("All")).click();
        $$(filterSelector).find(text("All")).shouldHave(cssClass("selected"));

        // Checks count of active items
        $(activeTaskSelector).shouldHave(text("2"));
        // Checks count of completed items
        $(completedTaskSelector).shouldHave("1");

        // Clear 4th task
        $(completedTaskSelector).click();

        // Checks clear-completed after deleting completed tasks
        $$(filterSelector).find(text("All")).has(cssClass("selected"));
        $(completedTaskSelector).shouldBe(hidden).shouldHave("0");

        // Make sure that 4th task was deleted
        $$(todoListSelector).shouldHaveSize(2);

        // Task editing - appending
        actions().doubleClick($$(todoListSelector).get(1).find("label")).perform();
        $$(todoListSelector).get(1).find(".edit").append("appended").pressEnter();
        $$(todoListSelector).get(1).shouldHave(text("appended"));

        // Task editing - replacing
        actions().doubleClick($$(todoListSelector).get(0).find("label")).perform();
        $$(todoListSelector).get(0).find(".edit").val("new task").pressEnter();
        $$(todoListSelector).get(0).shouldHave(text("new task"));

        // Mark ALL tasks as completed
        $("#toggle-all").click();

        // Checks count of completed items
        $(completedTaskSelector).shouldHave("2");

        // Clear all completed tasks
        $(completedTaskSelector).click();

        $$(todoListSelector).shouldHaveSize(0);
    }

    // Method for creating new task
    private void createTask(String taskName) {
        $("#new-todo").val(taskName).pressEnter();
    }
}
