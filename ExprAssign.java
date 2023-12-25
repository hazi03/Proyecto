package mx.ipn.escom.k.parser;

import mx.ipn.escom.k.tools.Token;

public class ExprAssign extends Expression{
    final Token name;
    final Expression value;

    ExprAssign(Token name, Expression value) {
        this.name = name;
        this.value = value;
    }
}
