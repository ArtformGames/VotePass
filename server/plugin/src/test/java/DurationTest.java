import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;
import org.junit.Test;

import java.time.Duration;

public class DurationTest {


    @Test
    public void a() {
        Duration d = TimeStringUtils.parseDuration("12h,32m");
        System.out.println(d);
        System.out.println(TimeStringUtils.serializeDuration(d));
    }
}
