package ai.elimu.crowdsource.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.web3j.crypto.ECDSASignature;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.crypto.Sign.SignatureData;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import timber.log.Timber;

public class EthersUtils {
    private static final String MESSAGE_PREFIX = "\u0019Ethereum Signed Message:\n";

    public static boolean verifyMessage(String address,String message, String signature) {
        String recovered = EthersUtils.recoverAddress(EthersUtils.hashMessage(message), signature);
        Timber.tag("ethers-utils").i("recovered address: %s", recovered);
        return address.equalsIgnoreCase(recovered);
    }

    public static String hashMessage(String message) {
        return Hash.sha3(
                Numeric.toHexStringNoPrefix(
                        (EthersUtils.MESSAGE_PREFIX + message.length() + message).getBytes(StandardCharsets.UTF_8)));
    }

    public static String recoverAddress(String digest, String signature) {
        SignatureData signatureData = EthersUtils.getSignatureData(signature);
        int header = 0;
        for (byte b : signatureData.getV()) {
            header = (header << 8) + (b & 0xFF);
        }
        if (header < 27 || header > 34) {
            return null;
        }
        int recId = header - 27;
        BigInteger key = Sign.recoverFromSignature(
                recId,
                new ECDSASignature(
                        new BigInteger(1, signatureData.getR()), new BigInteger(1, signatureData.getS())),
                Numeric.hexStringToByteArray(digest));
        if (key == null) {
            return null;
        }
        return ("0x" + Keys.getAddress(key)).trim();
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