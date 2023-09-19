import com.artformgames.plugin.votepass.game.ui.GUIUtils;
import org.junit.Test;

import java.util.List;

public class AnswerFormatTest {

    @Test
    public void onTest() {

        List<String> contents = List.of(
                "This is the first line of the answer",
                "And this is the second line",
                "Of course the third line",
                " ",
                "%%%%player%%%%",
                "",
                "answer3 %player_name% %%player%%"
        );

        List<String> formatted = GUIUtils.formatAnswersLore(contents, "-->", 35);

        System.out.println("Formatted ");
        formatted.forEach(System.out::println);

        System.out.println(" ");
        System.out.println("Limited x3");
        formatted.subList(0, 3).forEach(System.out::println);

    }

}
