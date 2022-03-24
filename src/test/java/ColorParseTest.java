import me.yurinan.fwtool.server.universal.utils.ColorParser;
import org.junit.jupiter.api.Test;

/**
 * @author CarmJos, Yurinan
 * @since 2022/1/3 11:48
 */

public class ColorParseTest {

    @Test
    public void onTest() {
        String testString = "&fI send my thoughts from time immemorial to eternity to Ghost_You. I had flipped, completely.";
        String testHexString = "&(#FFFFFF)You can do you want cause I love you, baby. --- << Indulge >>";
        System.out.println(ColorParser.parse(testString));
        System.out.println(ColorParser.parse(testHexString));
    }

}
