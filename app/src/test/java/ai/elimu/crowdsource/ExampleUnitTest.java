package ai.elimu.crowdsource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ai.elimu.crowdsource.ui.authentication.SignInActivity;
import ai.elimu.crowdsource.util.EthersUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void web3recover_SanityCheck() {
        String signature = "0xb2f0b0xb2f0b719e5239f46b0c66779bec901c2396574ac4a20xb2f0b719e5239f46b0c66779bec901c2396574ac4a2af6598c6b675caee4bdd60fa2415ca889879b540a9d024c1d7b95948d9dab1a3153fc7b9b7b799b3179e01caf6598c6b675caee4bdd60fa2415ca889879b540a9d024c1d7b95948d9dab1a3153fc7b9b7b799b3179e01c719e5239f46b0c66779bec901c2396574ac4a2af6598c6b675caee4bdd60fa2415ca889879b540a9d024c1d7b95948d9dab1a3153fc7b9b7b799b3179e01c";
        String message = SignInActivity.W3_SIGN_MESSAGE;
//        String digest = "0xf2a9b02db259ce83b5a1c0ff9f0445247ba4e1c21bcd475e6b7e1d203bc2257a";
//        assertEquals(EthersUtils.hashMessage(message), digest);
        assertEquals(EthersUtils.recoverAddress(EthersUtils.hashMessage(message), signature), "0x11e20cf4eb8f34eebc37475e171c5e2daa43f5ea");
    }
}