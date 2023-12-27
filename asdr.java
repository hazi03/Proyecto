
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class asdr implements parser{

    private int i = 0;
    private Token currentToken;
    private boolean hayErrores = false;
    private final List<Token> tokens;

    public asdr(List<Token> tokens ){
        this.tokens = tokens;
        currentToken = tokens.get(i);
    }
  

   @Override
   public boolean parse(){
    /*Lista de statements, programa */
        List<Statement> ast = PROGRAM();
        /*Al finalizar todo */
        if(currentToken.getTipo() == TipoToken.EOF && !hayErrores){
            System.out.println("Works");
            return true;
        }
        else
            System.err.println("Hay error en la sintaxis");
            return false;
   }

   //PROGRAM -> DECLARATION

   private List<Statement> PROGRAM(){

    List<Statement> sentencias = new ArrayList<>();

    if(hayErrores) return null;

    return DECLARATION(sentencias);
   }



/*******************Declaraciones********/

/*DECLARATION -> FUN_DECL DECLARATION || VAR_DECL DECLARATION || STATEMENT DECLARATION */

private List<Statement> DECLARATION(List<Statement> sentencias){

    if(hayErrores)return null; // no regresa nada

        if(currentToken.getTipo() == TipoToken.FUN){
            Statement sent = FUN_DECL();
            sentencias.add(sent);
            return DECLARATION(sentencias);
        }
        if(currentToken.getTipo() == TipoToken.VAR){
            Statement sent = VAR_DECL();
            sentencias.add(sent);
            return DECLARATION(sentencias);
        }
}


    private void term(){
        factor();
        term2();
    }


    private Expression factor(){
        Expression expr = unary();
        expr = factor2(expr);
        return expr;
    }

    private Expression factor2(Expression expr){
        switch (preanalisis.getTipo()){
            case SLASH:
                match(TipoToken.SLASH);
                Token operador = previous();
                Expression expr2 = unary();
                ExprBinary expb = new ExprBinary(expr, operador, expr2);
                return factor2(expb);
            case STAR:
                match(TipoToken.STAR);
                operador = previous();
                expr2 = unary();
                expb = new ExprBinary(expr, operador, expr2);
                return factor2(expb);
        }
        return expr;
    }

    private Expression unary(){
        switch (preanalisis.getTipo()){
            case BANG:
                match(TipoToken.BANG);
                Token operador = previous();
                Expression expr = unary();
                return new ExprUnary(operador, expr);
            case MINUS:
                match(TipoToken.MINUS);
                operador = previous();
                expr = unary();
                return new ExprUnary(operador, expr);
            default:
                return call();
        }
    }

    private Expression call(){
        Expression expr = primary();
        expr = call2(expr);
        return expr;
    }

    private Expression call2(Expression expr){
        switch (preanalisis.getTipo()){
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                List<Expression> lstArguments = argumentsOptional();
                match(TipoToken.RIGHT_PAREN);
                ExprCallFunction ecf = new ExprCallFunction(expr, lstArguments);
                return call2(ecf);
        }
        return expr;
    }

    private Expression primary(){
        switch (preanalisis.getTipo()){
            case TRUE:
                match(TipoToken.TRUE);
                return new ExprLiteral(true);
            case FALSE:
                match(TipoToken.FALSE);
                return new ExprLiteral(false);
            case NULL:
                match(TipoToken.NULL);
                return new ExprLiteral(null);
            case NUMBER:
                match(TipoToken.NUMBER);
                Token numero = previous();
                return new ExprLiteral(numero.getLiteral());
            case STRING:
                match(TipoToken.STRING);
                Token cadena = previous();
                return new ExprLiteral(cadena.getLiteral());
            case IDENTIFIER:
                match(TipoToken.IDENTIFIER);
                Token id = previous();
                return new ExprVariable(id);
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                Expresion expr = expression();
                // Tiene que ser cachado aquello que retorna
                match(TipoToken.RIGHT_PAREN);
                return new ExprGrouping(expr);
        }
        return null;
    }


    private void match(TipoToken tt) throws ParserException {
        if(preanalisis.getTipo() ==  tt){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            String message = "Error en la línea " +
                    preanalisis.getPosition().getLine() +
                    ". Se esperaba " + preanalisis.getTipo() +
                    " pero se encontró " + tt;
            throw new ParserException(message);
        }
    }


    private Token previous() {
        return this.tokens.get(i - 1);
    }
}
