package com.myfirsttest;

        import com.codeborne.selenide.ElementsCollection;
        import com.codeborne.selenide.SelenideElement;
        import org.junit.Test;

        import java.util.List;

        import static com.codeborne.selenide.CollectionCondition.exactTexts;
        import static com.codeborne.selenide.Condition.*;
        import static com.codeborne.selenide.Selenide.*;
        import static java.util.Arrays.asList;

public class TodomvcTest {

    @Test
    public void enterTasks() {
        ElementsCollection tasks = $$("#todo-list li");
        ElementsCollection filters = $$("#filters a");
        SelenideElement activeCount = $("#todo-count");
        SelenideElement clearCompleted = $("#clear-completed");

        String t1 = "1. Practice kindness";
        String t2 = "=(^.^)=";
        String t3 = "- be productive yet calm ;)";
        String t4 = " Don't forget to smile! @#$%^&*() and yaaaaaaaaaaaaaaaaaaaaaaz"; // ось тут text wrapping не працює

        open("http://todomvc.com/examples/troopjs_require/#");

        List<String> taskList = asList(t1, t2, t3, t4);
        for (String t : taskList) {
            $("#new-todo").val(t).pressEnter();
        }

        tasks.shouldHave(exactTexts(t1, t2, t3, t4));

        // Перевіряю одразу кількість активних тасок
        activeCount.shouldHave(text("4"));

        // Delete second task
        tasks.get(1).hover();
        tasks.get(1).find(".destroy").click();

        // Make sure that task was deleted
        tasks.get(1).shouldNotHave(text(t2));
        // Checks count of active items after deleting 2nd task
        activeCount.shouldHave(text("3"));

        // Mark 4th task as a completed
        tasks.get(2).shouldHave(text(t4)).find(".toggle").click();

        // Active filter verification
        filters.find(text("Active")).click();
        filters.find(text("Active")).shouldHave(cssClass("selected"));
        $(".completed").shouldBe(hidden);
        $$(".active").shouldHaveSize(2);

        // Completed filter verification
        filters.find(text("Completed")).click();
        filters.find(text("Completed")).shouldHave(cssClass("selected"));
        $(".active").shouldBe(hidden);
        $$(".completed").shouldHaveSize(1);

        // Back to All filter
        filters.find(text("All")).click();
        filters.find(text("All")).shouldHave(cssClass("selected"));

        // Checks count of active items
        activeCount.shouldHave(text("2"));
        // Checks count of completed items
        clearCompleted.shouldHave("1");

        // Clear 4th task
        clearCompleted.click();

        // Checks clear-completed after deleting completed tasks
        filters.find(text("All")).has(cssClass("selected"));
        clearCompleted.shouldBe(hidden);

        // Make sure that 4th task was deleted
        tasks.shouldHaveSize(2);

        // Task editing - appending
        actions().doubleClick(tasks.get(1).find("label")).perform();
        tasks.get(1).find(".edit").append("appended").pressEnter();
        tasks.get(1).shouldHave(text("appended"));

        // Task editing - replacing
        actions().doubleClick(tasks.get(0).find("label")).perform();
        tasks.get(0).find(".edit").val("new task").pressEnter();
        tasks.get(0).shouldHave(text("new task"));

        // Mark ALL tasks as completed
        $("#toggle-all").click();

        // Checks count of completed items
        clearCompleted.shouldHave("2");

        // Clear all completed tasks
        clearCompleted.click();

        tasks.shouldHaveSize(0);
    }
}
