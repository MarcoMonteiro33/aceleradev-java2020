package challenge;

public class CriptografiaCesariana implements Criptografia {

    @Override
    public String criptografar(String texto) {
        return processaTexto(texto, 3);
    }

    @Override
    public String descriptografar(String texto) {
        return processaTexto(texto,-3);
    }

    public boolean textoEValido(String texto) {
        if (texto == null) throw new NullPointerException();
        if (texto.isEmpty()) throw new IllegalArgumentException();
        return true;
    }

    public String processaTexto(String texto, int chave){
        StringBuilder textoDescriptografado = new StringBuilder();
        if(textoEValido(texto)){
            String textoMinusculo = texto.toLowerCase();
            for (int i = 0; i < textoMinusculo.length(); i++) {
                if (Character.isLetter(textoMinusculo.charAt(i))) {
                    char letra = (char) (textoMinusculo.charAt(i) + chave);
                    textoDescriptografado.append(String.valueOf(letra));
                } else {
                    textoDescriptografado.append(String.valueOf((char) (textoMinusculo.charAt(i))));
                }
            }
        }
        return textoDescriptografado.toString();
    }
}
