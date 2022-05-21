package ai.elimu.crowdsource.util;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;

import timber.log.Timber;

public class EthersUtils {
    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static boolean verifyMessage(String address, String message, String signature) {
        String recovered = EthersUtils.recoverAddress(EthersUtils.hashMessage(message), signature);
        Timber.tag("ethers-utils").i("recovered address: %s", recovered);
        return address.equalsIgnoreCase(recovered);
    }

    public static byte[] hashMessage(String message) {
        // Note: The label prefix is part of the standard
        String label = "\u0019Ethereum Signed Message:\n" + String.valueOf(message.getBytes().length) + message;
        // Get message hash using SHA-3
        return Hash.sha3((label).getBytes());
    }

    public static String recoverAddress(byte[] digest, String signature) {
        SignatureData sd = EthersUtils.getSignatureData(signature);
        for (int i = 0; i < 4; i++) {
            final BigInteger publicKey = Sign.recoverFromSignature(
                    (byte) i,
                    new ECDSASignature(
                            new BigInteger(1, sd.getR()),
                            new BigInteger(1, sd.getS())
                    ),
                    digest
            );

            if (publicKey != null) {
                return  "0x" + Keys.getAddress(publicKey);
            }
        }
        return null;
    }

    private static SignatureData getSignatureData(String signature) {
        byte[] signatureBytes = Numeric.hexStringToByteArray(signature);
        byte v = signatureBytes[64];
        if (v < 27) {
            v += 27;
        }
        byte[] r = (byte[]) Arrays.copyOfRange(signatureBytes, 0, 32);
        byte[] s = (byte[]) Arrays.copyOfRange(signatureBytes, 32, 64);
        return new SignatureData(v, r, s);
    }
}