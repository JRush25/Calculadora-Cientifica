package evaluator;
/**
 * Creado por: mmonteiro
 * miguelmonteiroclaveri@gmail.com
 * github.com/mmonteiroc
 * Paquete evaluator
 * Proyecto Calculadora
 */

import java.util.ArrayList;
import java.util.List;

public class Token {
    enum Toktype {
        // Tipos de tokens
        NUMBER, OP, PAREN
    }

    private Toktype tokType;
    private double value;
    private char tkOp;

    // Constructor privado para evitar
    // poder instanciar un nuevo token
    // desde fuera de esta clase
    private Token() {
    }

    /**
     * @param value valor del token que crearemos
     * @return devolvemos el token ya creado
     * <p>
     * Este metodo lo que hace es recivir un valor el cual sera un numero integro.
     * Despues instancia un nuevo token, y asignamos un tipo de token "Number"
     * Asignamos el valor que le corresponde y retornamos dicho token
     */
    public static Token tokNumber(double value) {
        Token dev = new Token();
        dev.tokType = Toktype.NUMBER;
        dev.value = value;
        return dev;
    }

    /**
     * @param operador Operador de dicho token
     * @return Retornamos un token con sus cracteristicas que le corresponden
     * <p>
     * Lo que hacemos es recibir el caracter del token que le corresponde
     * Ex: + - * / ^
     * Despues instanciamos un nuevo token y le asignamos los
     * valores que le pertañen
     */
    public static Token tokOp(char operador) {
        Token dev = new Token();
        dev.tokType = Toktype.OP;
        dev.tkOp = operador;
        return dev;
    }

    /**
     * @param paren Parentesis de dicho operador
     * @return Devolvemos el token una vez creado
     * <p>
     * Recibimos el parentesis que queremos crear en token
     * Añadimos a dicho token todos
     * sus valores necesarios y lo retornamos
     */
    public static Token tokParen(char paren) {
        Token devolver = new Token();
        devolver.tokType = Toktype.PAREN;
        devolver.tkOp = paren;
        return devolver;
    }

    @Override
    public String toString() {
        return " " + tkOp + " " + value;
    }

    @Override
    public boolean equals(Object o) {
        return (this.tkOp == ((Token) o).tkOp) && (this.value == ((Token) o).value) && (this.tokType == ((Token) o).tokType);
    }


    /**
     * @param expr String que contiene una expresion matematica la cual hemos de usar
     * @return Lista de tokens generada a partir de dicha expresion
     * <p>
     * Recibimos una expresion matematica la cual separamos los operadores / parentesis con
     * espacios y hacemos un split por los espacios, despues lo que hacemos es ir recorriendo
     * pieza a pieza y dependiendo de que tipo sea generaremos un token el cual añadiremos
     * a la lista de tokens que despues retornaremos
     */
    // A partir d'un String, torna una llista de tokens
    public static Token[] getTokens(String expr) {
        List<Token> devolver = new ArrayList<Token>();
        String[] partes = anadirEspacios(expr).split("\\s+");

        boolean encontradoOperador = true;

        for (int i = 0; i < partes.length; i++) {
            String part = partes[i];
            if (part.length() != 0) {
                if (part.contains("-") && encontradoOperador) {
                    devolver.add(tokOp('_'));
                    encontradoOperador = false;
                } else if (esOp(part.charAt(0))) {
                    encontradoOperador = true;
                    devolver.add(tokOp(part.charAt(0)));
                } else if (esParenIZQ(part.charAt(0))) {
                    devolver.add(tokParen(part.charAt(0)));
                    encontradoOperador = true;
                } else if (esParenDCH(part.charAt(0))) {
                    devolver.add(tokParen(part.charAt(0)));
                    encontradoOperador = false;
                } else if (Character.isDigit(part.charAt(0))) {
                    if (i + 1 >= partes.length) {
                        devolver.add(tokNumber(Double.parseDouble(part)));
                    } else {
                        if (partes[i + 1].charAt(0) == '.') {
                            int index = 1;
                            String ayuda = "";
                            for (int j = i + 2; j < partes.length; j++) {

                                if (Character.isDigit(partes[j].charAt(0))) {
                                    ayuda += partes[j];
                                    index++;
                                } else {
                                    break;
                                }
                            }
                            i += index;
                            devolver.add(tokNumber(Double.parseDouble(part + "." + ayuda)));
                        } else {
                            devolver.add(tokNumber(Double.parseDouble(part)));
                        }
                    }
                    encontradoOperador = false;
                }
            }
        }

        return devolver.toArray(new Token[0]);
    }

    /**
     * @param c caracter que recibimos
     * @return true/false
     * <p>
     * Este metodo nos retorna true si el caracter
     * que recibimos es un operador, false si no lo es
     */
    static private boolean esOp(char c) {
        switch (c) {
            case '+':
            case '-':
            case '*':
            case '/':
            case '^':
                return true;
            default:
                return false;
        }
    }

    static private boolean esParenDCH(char c) {
        return c == ')';
    }

    static private boolean esParenIZQ(char c) {
        return c == '(';
    }

    public Toktype getTokType() {
        return tokType;
    }

    public double getValue() {
        return value;
    }

    public char getTkOp() {
        return tkOp;
    }


    /**
     * @param expresion Espresion que nos pasan
     * @return Expresion que retornamos
     * <p>
     * A la expresion que recibimos lo que hacemos
     * es añadir un espacio entre los numeros y los operadores
     * Ex:  45+(45-23) --> 45 + ( 45 - 23 )
     */
    static private String anadirEspacios(String expresion) {
        StringBuilder devolver = new StringBuilder();
        for (int i = 0; i < expresion.length(); i++) {
            if (Character.isDigit(expresion.charAt(i))) {
                devolver.append(expresion.charAt(i));
            } else {
                devolver.append(" " + expresion.charAt(i) + " ");
            }
        }
        return devolver.toString();
    }
}
