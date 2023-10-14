import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import static java.util.Base64.getEncoder;

public class CriptosistemaClaveSecreta {

    public static void main(String[] args) {
        try {
            // Generar una clave secreta
            SecretKey claveSecreta = generarClaveSecreta();

            // Mensaje a cifrar
            String mensajeOriginal = "Este es un mensaje secreto";

            // Cifrar el mensaje
            byte[] mensajeCifrado = cifrarMensaje(mensajeOriginal, claveSecreta);

            // Descifrar el mensaje
            String mensajeDescifrado = descifrarMensaje(mensajeCifrado, claveSecreta);

            System.out.println("Mensaje original: " + mensajeOriginal);
            System.out.println("Mensaje cifrado: " + getEncoder().encodeToString(mensajeCifrado));
            System.out.println("Mensaje descifrado: " + mensajeDescifrado);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Generar una clave secreta
    public static SecretKey generarClaveSecreta() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // Tamaño de clave de 128 bits
        return keyGenerator.generateKey();
    }

    // Cifrar un mensaje
    public static byte[] cifrarMensaje(String mensaje, SecretKey clave) throws Exception {
        Cipher cifrador = Cipher.getInstance("AES/GCM/NoPadding");
        cifrador.init(Cipher.ENCRYPT_MODE, clave);

        byte[] iv = cifrador.getIV(); // Obtener el IV generado

        byte[] mensajeCifrado = cifrador.doFinal(mensaje.getBytes("UTF-8"));

        return concatArray(iv, mensajeCifrado);
    }

    // Descifrar un mensaje
    public static String descifrarMensaje(byte[] mensajeCifrado, SecretKey clave) throws Exception {
        Cipher descifrador = Cipher.getInstance("AES/GCM/NoPadding");

        byte[] iv = new byte[12];
        System.arraycopy(mensajeCifrado, 0, iv, 0, 12);

        descifrador.init(Cipher.DECRYPT_MODE, clave, new GCMParameterSpec(128, iv));

        byte[] mensajeDescifrado = descifrador.doFinal(mensajeCifrado, 12, mensajeCifrado.length - 12);

        return new String(mensajeDescifrado, "UTF-8");
    }

    // Concatenar dos arrays de bytes
    public static byte[] concatArray(byte[] a, byte[] b) {
        byte[] result = new byte[a.length + b.length];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }
}
