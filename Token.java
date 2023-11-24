public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;
    //final int posicion;

    public Token(TipoToken tipo, String lexema/*, int posicion*/) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
        //this.posicion = posicion;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        //this.posicion = 0;
    }

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

    @Override
    public boolean equals(Object o)
    {
        if(!(o instanceof Token))
            return false;
        if(this.tipo == ((Token)o).tipo)
            return true;
        return false;
    }

    public String toString() {return "<" + tipo + " " + lexema + " " + literal + ">";}
}
