import com.artformgames.plugin.votepass.core.utils.TimeStringUtils;
import com.artformgames.plugin.votepass.game.ui.RequestIconInfo;
import org.junit.Test;

import java.time.Duration;

public class DurationTest {


    @Test
    public void a() {
        Duration d = TimeStringUtils.parseDuration("12h,32m");
        System.out.println(d);
        System.out.println(TimeStringUtils.serializeDuration(d));
    }

    @Test
    public void b(){
        System.out.println(RequestIconInfo.getPercent(20,100));
        System.out.println(RequestIconInfo.getPercent(110,100));
    }
}
