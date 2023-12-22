public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int posicion;

    public Token(TipoToken tipo, String lexema, Object literal, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.posicion = posicion;
    }

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.posicion = 0;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.posicion = 0;
    }

    public Token(TipoToken tipo, String lexema, int posicion) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        this.posicion = posicion;
    }

    /* Métodos auxiliares para el AST */
    public boolean isExpression()
    {
        switch(this.tipo)
        {
            case IDENTIFIER:
            case NUMBER:
            case STRING:
            case TRUE:
            case FALSE:
            case NULL:
                return true;
            default:
                return false;
        }
    }

    public boolean isOperator()
    {
        switch(this.tipo)
        {
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
            case EQUAL:
            case EQUAL_EQUAL:
            case LESS:
            case LESS_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case BANG_EQUAL:
            case OR:
            case AND:
                return true;
            default:
                return false;
        }
    }

    public boolean isKeyword()
    {
        switch(this.tipo)
        {
            case PRINT:
            case RETURN:
            case VAR:
            case NULL:
            case FUN:
            case IF:
            case ELSE:
            case WHILE:
            case FOR:
                return true;
            default:
                return false;
        }
    }

    public boolean isControlStructure()
    {
        switch(this.tipo)
        {
            case IF:
            case ELSE:
            case WHILE:
            case FOR:
                return true;
            default:
                return false;
        }
    }

    public boolean precedenceGreaterEqual(Token token)
    {
        return this.getPrecedence() >= token.getPrecedence();
    }

    private int getPrecedence()
    {
        switch(this.tipo)
        {
            case PLUS:
            case MINUS:
                return 6;
            case STAR:
            case SLASH:
                return 7;
            case LESS:
            case LESS_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
                return 5;
            case EQUAL_EQUAL:
                return 4;
            case EQUAL:
                return 1;
            case OR:
                return 2;
            case AND:
                return 3;
            default:
                return 0;
        }
    }

    public int arity()
    {
        switch(this.tipo)
        {
            case PLUS:
            case MINUS:
            case STAR:
            case SLASH:
            case EQUAL:
            case EQUAL_EQUAL:
            case LESS:
            case LESS_EQUAL:
            case GREATER:
            case GREATER_EQUAL:
            case BANG_EQUAL:
            case OR:
            case AND:
                return 2;
            default:
                return 0;
        }
    }

    /*******************************************************************************************************/

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Token))
        {
            return false;
        }

        if(this.tipo == ((Token)o).tipo)
        {
            return true;
        }
        return false;
    }

    public String toString() {return "<" + tipo + " " + lexema + " " + literal + ">";}
}
