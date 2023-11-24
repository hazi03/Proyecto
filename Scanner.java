import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    // Se crea un ArrayList de tokens llamado "tokens"
    private final List<Token> tokens = new ArrayList<>();

    private final String source;
    private static final Map<String, TipoToken> palabrasReservadas;
    static 
    {
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
    // Metodo que escanea sobre un archivo
    public Scanner(String source) {
        this.source = source + " ";
    }

    // ANALIZADOR LEXICO O SCANNER
    public List<Token> scan() {
        // El estado inicial del automata es cero
        // Declaramos una bandera de control para los mensajes de error
        // Inicializamos una cadena llamada lexema
        // Definimos un caracter c, que formara parte del lexema
        int estado = 0;
        int flag = 0;                                  
        String lexema = "";
        char c = 0;
        int inicioLexema = 0;

        // Hasta que recorra toda la cadena
        for (int i = 0; i < source.length(); i++) {
            // 'c' obtiene el valor del caracter que se esta leyendo
            c = source.charAt(i);
        
            // AUTOMATA PALABRAS RESERVADAS, IDENTIFICADOR Y NUMEROS (decimal, exponencial o entero)
            if (Character.isWhitespace(c)) {
                //Si el caracter es un espacio, la bandera vale 1            
                flag = 1;
            }
            switch (estado) {
                case 0:                                 //Estado inicial 
                    if (Character.isLetter(c)) {
                        //Si es una letra, pasa al estado 13
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 13;
                        lexema += c;
                        inicioLexema = i;
                    } else if (Character.isDigit(c)) {
                        //De lo contrario, si es un digito, pasa al estado 15
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 15;
                        lexema += c;
                        inicioLexema = i;
                    }
                break;

                case 13:
                    if (Character.isLetterOrDigit(c)) {
                        // Si el carácter es digito o letra, se vuelve al mismo estado
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 13;
                        lexema += c;
                    } else {
                        // Se obtiene el lexema de la cadena leida
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if (tt == null) {
                            // Si el lexema es null, es un identificador
                            Token t = new Token(TipoToken.IDENTIFIER, lexema, null, inicioLexema + 1);
                            tokens.add(t);
                        } else {
                            // De lo contrario, es una palabra reservada
                            Token t = new Token(tt, lexema, null, inicioLexema + 1);
                            tokens.add(t);
                        }
                        estado = 90;                        //Estado final
                    }
                break;

                case 15:
                    if (Character.isDigit(c)) {
                        // Permanece en el mismo estado si es un digito
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 15;
                        lexema += c;
                    } else if (c == '.') {
                        // Si lee un punto '.', pasa al estado 16
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 16;
                        lexema += c;
                    } else if (c == 'E') {
                        // Si lee el valor exponencial 'E', pasa al estado 18
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 18;
                        lexema += c;
                    } else {
                        /* 
                        Si lee otra cosa que no sea un numero, un '.', o 'E', entonces 
                        genera el token para un numero ENTERO 
                        */
                        Token t = new Token(TipoToken.NUMBER, lexema, Integer.valueOf(lexema), i + 1);
                        tokens.add(t);
                        estado = 90;                        //Estado final
                    }
                break;

                case 16:
                    if (Character.isDigit(c)) {
                        // Si lee un digito pasa al estado 17
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 17;
                        lexema += c;
                    }
                break;

                case 17:
                    if (Character.isDigit(c)) {
                        // Si lee un digito, permanece en el mismo esatdo
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 17;
                        lexema += c;
                    } else if (c == 'E') {
                        // Si lee el valor exponencial 'E', pasa al estado 18
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 18;
                        lexema += c;
                    } else {
                        /* 
                        Si lee otra cosa que no sea un numero o 'E', entonces 
                        genera el token para un numero DECIMAL
                        */ 
                        Token t = new Token(TipoToken.NUMBER, lexema, Float.valueOf(lexema), i + 1);
                        tokens.add(t);
                        estado = 90;                        //Estado final
                    }
                break;

                case 18:
                    if (c == '+' || c == '-') {
                        // Si lee '+' o '-', pasa al estado 19
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 19;
                        lexema += c;
                    } else if (Character.isDigit(c)) {
                        // Si lee un digito, pasa al estado 20
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 20;
                        lexema += c;
                    }
                break;

                case 19:
                    if (Character.isDigit(c)) {
                        // Si lee un digito, pasa al estado 20
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 20;
                        lexema += c;
                    }
                break;

                case 20:
                    if (Character.isDigit(c)) {
                        // Si lee un digito, permanece en el mismo esatdo
                        // El caracter se añade al lexema
                        flag = 1;
                        estado = 20;
                        lexema += c;
                    } else {
                        /* 
                        Si lee otra cosa que no sea un numero, entonces 
                        genera el token para un numero con valor EXPONENCIAL
                        */
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.parseDouble(lexema), i + 1);
                        tokens.add(t);
                        estado = 90;                            //Estado final
                        }
                break;

            }

            // AUTOMATA COMENTARIOS (no generan token)
            switch (estado) {
                case 0:
                    if (c == '/') {
                        // Si lee '/', pasa al estado 26
                        flag = 1;
                        estado = 26;
                    }
                break;

                case 26:
                    if (c == '*') {
                        // Si lee '*', pasa al estado 27 (COMENTARIO MULTILINEA)
                        flag = 1;
                        estado = 27;
                    } else if (c == '/') {
                        // Si lee '/', pasa al estado 30 (COMENTARIO DE UNA SOLA LINEA)
                        flag = 1;
                        estado = 30;
                    } else {
                        /* 
                        De lo contrario, si solo se leyó un caracter '/' y le sigue un caracter 
                        que no es ni '*', ni '/' pasa al estado final 32
                        (GENERA TOKEN PARA EL SIMBOLO SLASH)
                        */
                        estado = 32;
                    }
                break;

                case 27:
                    if (c == '*') {
                        // Si lee '*', pasa al estado 28
                        flag = 1;
                        estado = 28;
                    } else {
                        // De lo contrario, permanece en el mismo estado
                        flag = 1;
                        estado = 27;
                    } 
                break;

                case 28:
                    if (c == '*') {
                        // Si lee '*', permanece en el mismo estado
                        flag = 1;
                        estado = 28;
                    } else if (c == '/') {
                        // Si lee '/', pasa al estado final 29 (CIERRE COMENTARIO MULTILINEA)
                        estado = 29;
                    } else {
                        // Si lee caracteres diferentes, se regresa al estado 27
                        flag = 1;
                        estado = 27;
                    }
                break;

                case 30:
                    if (c == '\n') {
                        /*
                        Si lee un salto de linea, pasa al estado final 31
                        (FIN DE COMENTARIO DE UNA SOLA LINEA)
                        */ 
                        flag = 1;
                        estado = 31;
                    } else {
                        // De lo contrario, permanece en el mismo estado
                        flag = 1;
                        estado = 30;
                    }
                break;
            }

            // AUTOMATA DE RECONOCIMIENTO DE CADENAS
            switch (estado) {
                // Las cadenas siempre estarán entre comillas '"'
                case 0:
                    if (c == '"') {
                        flag = 1;
                        estado = 24;
                    }
                break;

                case 24:
                    if (c == '"') {
                        // Cierre de comillas
                        flag = 1;
                        estado = 25;
                    }
                    else if (c == '\n') {
                        // Si se lee un salto de linea, se generará un ERROR (estado 67)
                        estado = 67;
                    } else {
                        /*
                        En caso de haber caracteres antes de cerrar comillas, 
                        permanecerá en el mismo estado; si no se cierran comillas, entonces
                        se generará un ERROR
                        */
                        flag = 1;
                        estado = 24;
                        lexema += c;
                    }
                break;

                case 25:
                    /*
                    Para el caso de las cadenas, el atributo literal
                    debe contener la cadena detectada, pero sin las comillas inicial y final.
                     */
                    String lexemaConComillas = "\"" + lexema + "\"";
                    // Se genera el token para 'String'
                    Token t = new Token(TipoToken.STRING, lexemaConComillas, lexema, i + 1);
                    tokens.add(t);
                    lexemaConComillas = "";
                    estado = 90;                                //Estado final
                break;
            }

            // AUTOMATA DE UNO O DOS CARACTERES
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
                        Token t = new Token(TipoToken.GREATER, lexema, null, i + 1);
                        tokens.add(t);
                        estado = 90;
                    }
                break;

                case 2:
                    Token t = new Token(TipoToken.GREATER_EQUAL, lexema, null, i + 1);
                    tokens.add(t);
                    estado = 90;
                break;

                case 4:
                    if (c == '=') {
                        flag = 1;
                        estado = 5;
                        lexema += c;
                    } else {
                        Token a = new Token(TipoToken.LESS, lexema, null, i + 1);
                        tokens.add(a);
                        estado = 90;
                    }
                break;

                case 5:
                    Token b = new Token(TipoToken.LESS_EQUAL, lexema, null, i + 1);
                    tokens.add(b);
                    estado = 90;
                break;

                case 7:
                    if (c == '=') {
                        flag = 1;
                        estado = 8;
                        lexema += c;
                    } else {
                        Token d = new Token(TipoToken.EQUAL, lexema, null, i + 1);
                        tokens.add(d);
                        estado = 90;
                    }
                break;

                case 8:
                    Token e = new Token(TipoToken.EQUAL_EQUAL, lexema, null, i + 1);
                    tokens.add(e);
                    estado = 90;
                break;

                case 10:
                    if (c == '=') {
                        flag = 1;
                        estado = 11;
                        lexema += c;
                    } else {
                        Token f = new Token(TipoToken.BANG, lexema, null, i + 1);
                        tokens.add(f);
                        estado = 90;
                    }
                break;

                case 11:
                    Token g = new Token(TipoToken.BANG_EQUAL, lexema, null, i + 1);
                    tokens.add(g);
                    estado = 90;
                break;
            }

        // AUTOMATA DE UN SOLO CARACTER
            switch (estado) {
                case 0:
                    if (c == '+') {
                        tokens.add(new Token(TipoToken.PLUS, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '-') {
                        tokens.add(new Token(TipoToken.MINUS, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '*') {
                        tokens.add(new Token(TipoToken.STAR, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '/') {
                        tokens.add(new Token(TipoToken.SLASH, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '{') {
                        tokens.add(new Token(TipoToken.LEFT_BRACE, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '}') {
                        tokens.add(new Token(TipoToken.RIGHT_BRACE, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '(') {
                        tokens.add(new Token(TipoToken.LEFT_PAREN, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == ')') {
                        tokens.add(new Token(TipoToken.RIGHT_PAREN, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == ',') {
                        tokens.add(new Token(TipoToken.COMMA, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == '.') {
                        tokens.add(new Token(TipoToken.DOT, String.valueOf(c), null, i + 1));
                        estado = 31;
                    } else if (c == ';') {
                        tokens.add(new Token(TipoToken.SEMICOLON, String.valueOf(c), null, i + 1));
                        estado = 31;
                    }
                break;
            }
        
            // ESTADOS TERMINALES (reinician todo y se regresa un caracter atrás)
            if (estado == 90) {
                i--;
                flag = 1;
                lexema = "";
                estado = 0;
                inicioLexema = 0;
            } else if (estado == 31 || estado == 29) {
                //Para el estado terminal de comentarios, no se regresa una cadena atrás
                flag = 1;
                estado = 0;
                lexema = "";
            } else if (estado == 32) {
                i--;
                // Se genera el token para el simbolo SLASH '/'
                tokens.add(new Token(TipoToken.SLASH, String.valueOf(source.charAt(i)), null, i + 1));
                flag = 1;
                estado = 0;
                lexema = "";
                inicioLexema = 0;
            }
        
            // MENSAJES DE ERROR PARA CARACTERES
            // Se generarán tokens hasta donde se hallo el error, entonces el programa finalizará
            if (flag == 0) {
                System.out.println("Caracter no valido:" + "" + c);
                return tokens;
            }
            // NO se generarán tokens, el programa unicamente arrojará el error detectado
            if (estado == 67)
                throw new IllegalArgumentException("Error: Salto de linea entre comillas");
            flag = 0;
        }
    
        // MENSAJES DE ERROR PARA COMENTARIOS
        // NO se generarán tokens, el programa unicamente arrojará el error detectado
        if (estado == 27 || estado == 28)
            throw new IllegalArgumentException("Error:Comentario multilinea no cerrado");

        // MENSAJE DE ERROR PARA CADENAS
        // NO se generarán tokens, el programa unicamente arrojará el error detectado
        if (estado == 24)
            throw new IllegalArgumentException("Error: Comillas no cerradas correctamente");

        // Al final agregamos el token de fin de archivo
        tokens.add(new Token(TipoToken.EOF, "$",source.length(), source.length()));
        return tokens;
    }
}