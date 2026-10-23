
package healthymind.seguridad;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class PasswordUtil {

    private static final int ITERACIONES = 600_000;
    private static final int BITS_HASH = 256;
    private static final int BYTES_SALT = 16;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() {
    }

    public static String hash(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException(
                    "La contraseña no puede estar vacía.");
        }

        byte[] salt = new byte[BYTES_SALT];
        RANDOM.nextBytes(salt);

        byte[] hash = derivar(contrasena.toCharArray(), salt, ITERACIONES);

        return "pbkdf2_sha256$" + ITERACIONES + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(salt)
                + "$"
                + Base64.getEncoder().withoutPadding().encodeToString(hash);
    }

    public static boolean verificar(String contrasena, String almacenado) {
        if (contrasena == null || almacenado == null) {
            return false;
        }

        try {
            String[] partes = almacenado.split("\\$");

            if (partes.length != 4
                    || !partes[0].equals("pbkdf2_sha256")) {
                return false;
            }

            int iteraciones = Integer.parseInt(partes[1]);

            if (iteraciones < 1 || iteraciones > 2_000_000) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(partes[2]);
            byte[] hashOriginal = Base64.getDecoder().decode(partes[3]);

            if (salt.length != BYTES_SALT
                    || hashOriginal.length != BITS_HASH / 8) {
                return false;
            }

            byte[] hashCalculado = derivar(
                    contrasena.toCharArray(), salt, iteraciones);

            return MessageDigest.isEqual(hashOriginal, hashCalculado);

        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] derivar(char[] contrasena, byte[] salt,
                                  int iteraciones) {
        PBEKeySpec spec = new PBEKeySpec(
                contrasena, salt, iteraciones, BITS_HASH);

        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec)
                    .getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(
                    "No fue posible proteger la contraseña.", e);
        } finally {
            spec.clearPassword();
            java.util.Arrays.fill(contrasena, '\0');
        }
    }
}
