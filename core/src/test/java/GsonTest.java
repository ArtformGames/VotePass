import com.artformgames.plugin.votepass.api.data.request.RequestAnswer;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GsonTest {

    public static Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    @Test
    public void onTest() {
        RequestAnswer answer = new RequestAnswer("This is a test!", List.of("This", "is", "a", "test!"));

        String json = GSON.toJson(answer);
        System.out.println(json);

        RequestAnswer answer2 = GSON.fromJson(json, RequestAnswer.class);
        System.out.println(answer2);

    }

    @Test
    public void onTest2() {
        Map<Integer, RequestAnswer> answers = new TreeMap<>();
        answers.put(1, new RequestAnswer("TEST1", List.of("This", "is", "a", "test!")));
        answers.put(2, new RequestAnswer("TEST2", List.of("This", "is", "a", "test!")));
        answers.put(3, new RequestAnswer("TEST3", List.of("This", "is", "a", "test!")));

        String json = GSON.toJson(answers);
        System.out.println(json);

        Type type = new TypeToken<Map<Integer, RequestAnswer>>() {
        }.getType();

        Map<Integer, RequestAnswer> answers2 = GSON.fromJson(json, type);
        answers2.forEach((id, answer) -> System.out.println(id + ": " + answer.toString()));
    }

}
