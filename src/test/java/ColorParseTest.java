import me.yurinan.fwtool.server.universal.utils.ColorParser;
import org.junit.jupiter.api.Test;

/**
 * @author CarmJos, Yurinan
 * @since 2022/1/3 11:48
 */

public class ColorParseTest {

    @Test
    public void onTest() {
        String testString = "&fI send the love of the whole universe and my thoughts" +
                " from the beginning of time to the end of time to my beloved, Ghost You.";
        String testHexString = "&(#FFFFFF)将全宇宙的爱与自太古至永劫的思念寄给我的爱人, 魂优宝贝。";
        System.out.println(ColorParser.parse(testString));
        System.out.println(ColorParser.parse(testHexString));
    }

}
