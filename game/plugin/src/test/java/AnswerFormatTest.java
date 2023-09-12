import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.artformgames.plugin.votepass.game.conf.PluginConfig;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AnswerFormatTest {

    @Test
    public void onTest() {

        System.out.println(formatAnswersLore(List.of(
                "answer1answer1answer1answer1answer1answer1",
                "answer2answer1answer1answer1answer1answer12",
                " ",
                "%%%%player%%%%",
                "",
                "answer3 %player_name% %%player%%"
        )));

    }

    public static List<String> formatAnswersLore(List<String> answers) {

        int lettersPreLine = 35;

        String prefix = "-->";

        List<String> lore = new ArrayList<>();
        for (String answer : answers) {
            String cleared = answer
                    .replaceAll("%+([一-龥_a-zA-Z0-9-]+)%+", "$1")
                    .replaceAll("&", "&&").replaceAll(Pattern.quote("§"), "&&")
                    .replaceAll("^&+$", "");// Prevent color problems
            if (cleared.isBlank()) continue;

            int length = cleared.length();
            int lines = length / lettersPreLine + (length % lettersPreLine == 0 ? 0 : 1);
            for (int i = 0; i < lines; i++) {
                int start = i * lettersPreLine;
                int end = Math.min((i + 1) * lettersPreLine, length);
                lore.add(prefix + cleared.substring(start, end));
            }
        }
        return lore;
    }

}
