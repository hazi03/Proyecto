import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private static final Map<String, TipoToken> palabrasReservadas;

    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    private final String source;

    // Se crea un ArrayList de tokens llamado "tokens"
    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source + " ";
    }

    // Metodo que escanea sobre un archivo
    // En esta parte definiremos varios automatas que realizaran la tarea de
    // identificar que lexema es

    // ANALIZADOR LEXICO O SCANNER
    public List<Token> scan() {

        // El estado inicial del automata es cero
        // Inicializamos una cadena llamado lexema
        // Definimos un caracter c, que formara parte del lexema
        int estado = 0;
        int flag = 0;
        String lexema = "";
        char c;
        int i = 0;

        // Hasta que recorra toda la cadena
        for (i = 0; i < source.length(); i++) {
            c = source.charAt(i);
            // automata palabras reservadas, identificador y numeros(decimal, exponencial o
            // entero)

            if (Character.isWhitespace(c)) {
                flag = 1;
            }
            switch (estado) {
                case 0:
                    if (Character.isLetter(c)) {
                        flag = 1;
                        estado = 13;
                        lexema += c;
                    } else if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 15;
                        lexema += c;

                    }
                    break;

                case 13:
                    if (Character.isLetterOrDigit(c)) {
                        flag = 1;
                        estado = 13;
                        lexema += c;
                    } else {
                        TipoToken tt = palabrasReservadas.get(lexema);

                        if (tt == null) {
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        } else {
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                        }
                        estado = 90;
                    }
                    break;

                case 15:
                    if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 15;
                        lexema += c;
                    } else if (c == '.') {
                        flag = 1;
                        estado = 16;
                        lexema += c;

                    } else if (c == 'E') {
                        flag = 1;
                        estado = 18;
                        lexema += c;

                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema));
                        tokens.add(t);

                        estado = 90;

                    }
                    break;

                case 16:
                    if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 17;
                        lexema += c;
                    }
                    break;
                case 17:
                    if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 17;
                        lexema += c;
                    } else if (c == 'E') {
                        flag = 1;
                        estado = 18;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema));
                        tokens.add(t);

                        estado = 90;

                    }
                    break;
                case 18:
                    if (c == '+' || c == '-') {
                        flag = 1;
                        estado = 19;
                        lexema += c;
                    } else if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 20;
                        lexema += c;
                    }
                    break;
                case 19:
                    if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 20;
                        lexema += c;
                    }
                    break;
                case 20:
                    if (Character.isDigit(c)) {
                        flag = 1;
                        estado = 20;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema));
                        tokens.add(t);

                        estado = 90;

                    }
                    break;

            }
            /* Automata Comentario */
            switch (estado) {
                case 0:
                    if (c == '/') {
                        flag = 1;
                        estado = 26;
                    }
                    break;
                case 26:
                    if (c == '*') {
                        flag = 1;
                        estado = 27;
                    } else if (c == '/') {
                        flag = 1;
                        estado = 30;
                    } else {
                        estado = 32;
                    }
                    break;
                case 27:
                    if (c == '*') {
                        flag = 1;
                        estado = 28;
                    } else
                        flag = 1;
                    estado = 27;

                    break;
                case 28:
                    if (c == '*') {
                        flag = 1;
                        estado = 28;
                    } else if (c == '/') {
                        estado = 29;
                    } else {
                        flag = 1;
                        estado = 27;

                    }
                    break;
                case 30:
                    if (c == '\n') {
                        flag = 1;
                        estado = 31;
                    } else {
                        flag = 1;
                        estado = 30;
                    }
                    break;
            }

            // Autómata de reconocimiento de cadenas
            switch (estado) {
                case 0:
                    if (c == '"') {
                        flag = 1;
                        estado = 24;
                    }
                    break;

                case 24:
                    if (c == '"') {
                        flag = 1;
                        estado = 25;
                    }

                    else if (c == '\n') {
                        estado = 67;
                    } else {
                        flag = 1;
                        estado = 24;
                        lexema += c;
                    }
                    break;
                case 25:
                    String lexemaConComillas = "\"" + lexema + "\"";
                    Token t = new Token(TipoToken.STRING, lexemaConComillas, lexema);
                    tokens.add(t);
                    /*
                     * Para el caso de las cadenas, el atributo literal
                     * debe contener la cadena detectada, pero sin las comillas inicial y final.
                     */
                    lexemaConComillas = "";
                    estado = 90;

                    break;
            }

            // Autómata de reconocimiento de símbolos
            switch (estado) {
                case 0:
                    if (c == '>') {
                        flag = 1;
                        estado = 1;
                        lexema += c;
                    } else if (c == '<') {
                        flag = 1;
                        estado = 4;
                        lexema += c;
                    } else if (c == '=') {
                        flag = 1;
                        estado = 7;
                        lexema += c;
                    } else if (c == '!') {
                        flag = 1;
                        estado = 10;
                        lexema += c;
                    }
                    break;

                case 1:
                    if (c == '=') {
                        flag = 1;
                        estado = 2;
                        lexema += c;
                    } else {
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);

                        estado = 90;

                    }
                    break;

                case 2:
                    Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                    tokens.add(t);

                    estado = 90;

                    break;

                case 4:
                    if (c == '=') {
                        flag = 1;
                        estado = 5;
                        lexema += c;
                    } else {
                        Token a = new Token(TipoToken.LESS, lexema);
                        tokens.add(a);
                        estado = 90;

                    }
                    break;

                case 5:
                    Token b = new Token(TipoToken.LESS_EQUAL, lexema);
                    tokens.add(b);

                    estado = 90;

                    break;

                case 7:
                    if (c == '=') {
                        flag = 1;
                        estado = 8;
                        lexema += c;
                    } else {
                        Token d = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(d);

                        estado = 90;

                    }
                    break;

                case 8:
                    Token e = new Token(TipoToken.EQUAL_EQUAL, lexema);
                    tokens.add(e);

                    estado = 90;

                    break;

                case 10:
                    if (c == '=') {
                        flag = 1;
                        estado = 11;
                        lexema += c;
                    } else {
                        Token f = new Token(TipoToken.BANG, lexema);
                        tokens.add(f);

                        estado = 90;

                    }
                    break;

                case 11:
                    Token g = new Token(TipoToken.BANG_EQUAL, lexema);
                    tokens.add(g);

                    estado = 90;

                    break;
            }

            // Automata de reconocimiento de un solo caracter
            switch (estado) {
                case 0:
                    if (c == '+') {
                        tokens.add(new Token(TipoToken.PLUS, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '-') {
                        tokens.add(new Token(TipoToken.MINUS, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '*') {
                        tokens.add(new Token(TipoToken.STAR, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '/') {
                        tokens.add(new Token(TipoToken.SLASH, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '{') {
                        tokens.add(new Token(TipoToken.LEFT_BRACE, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '}') {
                        tokens.add(new Token(TipoToken.RIGHT_BRACE, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '(') {
                        tokens.add(new Token(TipoToken.LEFT_PAREN, String.valueOf(c)));
                        estado = 31;
                    } else if (c == ')') {
                        tokens.add(new Token(TipoToken.RIGHT_PAREN, String.valueOf(c)));
                        estado = 31;
                    } else if (c == ',') {
                        tokens.add(new Token(TipoToken.COMMA, String.valueOf(c)));
                        estado = 31;
                    } else if (c == '.') {
                        tokens.add(new Token(TipoToken.DOT, String.valueOf(c)));
                        estado = 31;
                    } else if (c == ';') {
                        tokens.add(new Token(TipoToken.SEMICOLON, String.valueOf(c)));
                        estado = 31;
                    }

                    break;
            }

            if (estado == 90) {
                i--;
                flag = 1;
                lexema = "";
                estado = 0;
            } else if (estado == 31 || estado == 29) {
                flag = 1;
                estado = 0;
                lexema = "";
            } else if (estado == 32) {
                i--;
                tokens.add(new Token(TipoToken.SLASH, String.valueOf(source.charAt(i))));
                flag = 1;
                estado = 0;
                lexema = "";
            }
            if (flag == 0) {
                System.out.println("Caracter no valido:" + "" + c);
                return tokens;

            }
            if (estado == 67) {
                // Comillas no cerradas correctamente
                throw new IllegalArgumentException("Error: Salto de linea entre comillas");
            }
            flag = 0;
        }
        if (estado == 27 || estado == 28)

        {
            // Comentario no cerrado, lanza una excepción
            throw new IllegalArgumentException("Error:Comentario multilinea no cerrado");
        }
        if (estado == 24) {
            // Comillas no cerradas correctamente
            throw new IllegalArgumentException("Error: Comillas no cerradas correctamente");
        }

        return tokens;
    }

}