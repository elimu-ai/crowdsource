package ai.elimu.crowdsource;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.web3j.utils.Numeric;

import java.nio.charset.StandardCharsets;

import ai.elimu.crowdsource.ui.authentication.SignInWithWeb3Activity;
import ai.elimu.crowdsource.util.EthersUtils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void web3recover_SanityCheck() {
        String signature = "382a3e04daf88f322730f6a2972475fc5646ea8c4a7f3b5e83a90b10ba08a7364cd2f55348f2b6d210fbed7fc485abf19ecb2f3967e410d6349dd7dd1d4487751b";
        String message = "0x63f9a92d8d61b48a9fff8d58080425a3012d05c8igwyk4r1o7o";
        String digest = "0x438d6abe3d60b1a1f3f1e0c606ba249bc30e221001b9269f96be450c393c2f7c";
        assertEquals(Numeric.toHexString(EthersUtils.hashMessage(message)), digest);
        assertEquals(EthersUtils.recoverAddress(EthersUtils.hashMessage(message), signature), "0x63f9a92d8d61b48a9fff8d58080425a3012d05c8");
    }
}