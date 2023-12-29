
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class asdr implements parser
{
    private int i = 0;
    private Token currentToken;
    private boolean hayErrores = false;
    private final List<Token> tokens;

    public asdr(List<Token> tokens )
    {
        this.tokens = tokens;
        currentToken = tokens.get(i);
    }
  
   @Override
   public boolean parse()
   {
        /* Lista de statements, programa */
        List<Statement> ast = PROGRAM();

        /* Al finalizar todo... */
        if(currentToken.getTipo() == TipoToken.EOF && !hayErrores)
        {
            System.out.println("Works");
            return true;
        }
        else
            System.err.println("Hay error en la sintaxis");
        return false;
   }

   //PROGRAM -> DECLARATION

   private List<Statement> PROGRAM()
   {
        List<Statement> sentencias = new ArrayList<>();

        if(hayErrores) return null;

        return DECLARATION(sentencias);
   }



/*******************Declaraciones*************************/

/*DECLARATION -> FUN_DECL DECLARATION || VAR_DECL DECLARATION || STATEMENT DECLARATION */

private List<Statement> DECLARATION(List<Statement> sentencias){

    if(hayErrores)return null; // no regresa nada

        if(currentToken.getTipo() == TipoToken.FUN){
            Statement sent = FUN_DECL();
            sentencias.add(sent);
            return DECLARATION(sentencias);
        }
        else  if(currentToken.getTipo() == TipoToken.VAR){
            Statement sent = VAR_DECL();
            sentencias.add(sent);
            return DECLARATION(sentencias);
        }
        else if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
                || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN || currentToken.getTipo() ==TipoToken.PRINT
                || currentToken.getTipo() == TipoToken.FOR || currentToken.getTipo() == TipoToken.IF || currentToken.getTipo() == TipoToken.RETURN || currentToken.getTipo() == TipoToken.WHILE || currentToken.getTipo() == TipoToken.LEFT_BRACE){
                    Statement sent = STATEMENT();
                    sentencias.add(sent);
                    return DECLARATION(sentencias);
                }
        return sentencias;
}

/*Declaracion de una funcion
 * FUN_DECL -> fun FUNCTION
 */

 private Statement FUN_DECL(){
        if (hayErrores) return null;

        if(currentToken.getTipo() == TipoToken.FUN){
            match(TipoToken.FUN);//Avanza a la siguiente input
            return FUNCTION();//regresa la sentencia,va a la funcion en otras 
        }
            else {hayErrores = true; 
                return null;
                }
            
 }

 /*VAR_DECL -> var id VAR_INIT; */

 private Statement VAR_DECL(){

    if (hayErrores) {
            return null;}

    if(currentToken.getTipo() == TipoToken.VAR){
        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);

        Token name = previous(); //Para ingresarlo en StmtVar

        Expression init = VAR_INIT();
        match(TipoToken.SEMICOLON);
        return new StmtVar(name,init);
    }else{
        hayErrores=true;
        return null;
    }
 }

 //VAR_INIT -> = EXPRESSION || EPSILON
 //Regresa una expresion
/*Puede no estar inicializada la variable, i*/
 private Expression VAR_INIT(){
        if(hayErrores)return null;

        if(currentToken.getTipo() == TipoToken.EQUAL){
            match(TipoToken.EQUAL);
            return EXPRESSION();
        }

        /*epsilon */
            return null;
 }

 /*****************Sentencias**************/

 /*STATEMENT -> EXPR_STMT||FOR_STMT||IF_STMT||PRINT_STMT||RETURN_STMT||WHILE_STMT||BLOCK */
/*Sentencias expresiones, for, if, print, return...,while,block */
    private Statement STATEMENT(){
        if (hayErrores) {
                return null;
        }

        /*Se va hasta unary  */
        if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
        || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN){
    return EXPR_STMT();
}
/*Si encunetra un for */
else if (currentToken.getTipo() == TipoToken.FOR){
    return FOR_STMT();
} 
/*Si encuentra un if */
else if(currentToken.getTipo() == TipoToken.IF){
    return IF_STMT();
} 
/*Si encuentra un print */
else if (currentToken.getTipo() == TipoToken.PRINT){
    return PRINT_STMT();
} 
/*Si encuentra un return */
else if(currentToken.getTipo() == TipoToken.RETURN){
    return RETURN_STMT();
} 
/*Si encuentra un while */
else if (currentToken.getTipo() == TipoToken.WHILE){
    return WHILE_STMT();
} 
/*Si encuentra un ( */
else if (currentToken.getTipo() == TipoToken.LEFT_BRACE){
    return BLOCK();
} else{
    hayErrores = true;
    return null;
}
    }

    /*EXPR_STMT -> EXPRESSION;*/

        private Statement EXPR_STMT(){

            if(hayErrores)return null;

            if(currentToken.getTipo() == TipoToken.BANG 
            || /*->*/currentToken.getTipo() == TipoToken.MINUS
            || currentToken.getTipo() == TipoToken.FALSE
            || currentToken.getTipo() == TipoToken.TRUE
            || currentToken.getTipo() == TipoToken.NULL
            || currentToken.getTipo() == TipoToken.NUMBER 
            || currentToken.getTipo() == TipoToken.STRING 
            || currentToken.getTipo() == TipoToken.IDENTIFIER 
            || currentToken.getTipo() == TipoToken.LEFT_PAREN){
                Expression expr = EXPRESSION();
                match(TipoToken.SEMICOLON);
                return new StmtExpression(expr);
            }else{
                hayErrores = true;
                return null;
            }
        }

    /*ciclo for 
     * FOR_STMT -> for(FOR_STMT_1 FOR_STMT_2 FOR_STMT_3) STATEMENT
    */
        private Statement FOR_STMT()
        {
            if(hayErrores) return null;

            if(currentToken.getTipo() == TipoToken.FOR)
            {
                match(TipoToken.FOR);
                match(TipoToken.LEFT_PAREN);//AVANZA for(
                
                /*Parte de las 3 declaraciones de los tres parametros de un for(;;) */
                Statement inicializacion = FOR_STMT_1();//i=0, var i = 0 
                Expression condicion = FOR_STMT_2();//i<n
                Expression accion = FOR_STMT_3();//i=i+1

                match(TipoToken.RIGHT_PAREN);

                Statement body = STATEMENT();

                /*si hay expresion de accion */
                    if(accion != null)
                            body = new StmtBlock(Arrays.asList(body,new StmtExpression(accion)));
                /*Si no hay condicion, entonces se establece la condicion en true, es decir que siempre esta activa */      
                if(condicion == null)condicion = new ExprLiteral(true); 

                body = new StmtLoop(condicion, body);
        
                if(inicializacion != null)
                         body = new StmtBlock(Arrays.asList(inicializacion,body));

                    return body;
            }
                else{
                    hayErrores = true;
                    return null;
                }
        }

    // FOR_STMT_1 -> VAR_DECL || EXPR_STMT || ;
    private Statement FOR_STMT_1() 
    {
        if(hayErrores) return null;

        if(currentToken.getTipo() == TipoToken.VAR)
        {
            return VAR_DECL();
        } 
        else if (currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
                || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN)
        {
            return EXPR_STMT();
        } 
        else if(currentToken.getTipo() == TipoToken.SEMICOLON)
        {
            match(TipoToken.SEMICOLON);
            return null;
        } 
        else 
        {
            hayErrores = true;
            return null;
        }
    }

    // FOR_STMT_2 -> EXPRESSION; || ;
    private Expression FOR_STMT_2() 
    {
        if(hayErrores) return null;

        if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
                || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN)
        {
            Expression expr = EXPRESSION();
            match(TipoToken.SEMICOLON);
            return expr;
        } 
        else if(currentToken.getTipo() == TipoToken.SEMICOLON)
        {
            match(TipoToken.SEMICOLON);
            return null;
        } 
        else 
        {
            hayErrores = true;
            return null;
        }
    }

    // FOR_STMT_3 -> EXPRESSION || E
    private Expression FOR_STMT_3()
    {
            if(hayErrores) return null;

            if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
            || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN)
            {
                return EXPRESSION();
            }
        return null;
    }

    /* Sentencia IF
     * IF_STMT -> if ( EXPRESSION ) STATEMENT ELSE_STATEMENT
     */
    private Statement IF_STMT()
    {
        if(hayErrores) return null;

        if(currentToken.getTipo()==TipoToken.IF)
        {
            match(TipoToken.IF);
            match(TipoToken.LEFT_PAREN);

            Expression condicion = EXPRESSION(); //a==1, a>=0, ... ,etc.

            match(TipoToken.RIGHT_PAREN);

            Statement body = STATEMENT();
            Statement elseStatement = ELSE_STMT();

            return new StmtIf(condicion, body, elseStatement);
        }
        else
        {
            hayErrores = true;
            return null;
        }
    }

    // ELSE_STMT -> else STATEMENT || E
    private Statement ELSE_STMT()
    {
        if(hayErrores) return null;

        if(currentToken.getTipo() == TipoToken.ELSE)
        {
            match(TipoToken.ELSE);
            return STATEMENT();
        }  
        return null;
    }

    /* Funcion PRINT
     * PRINT_STMT -> print EXPRESSION ;
     */
    private Statement PRINT_STMT()
    {
        if(hayErrores) return null;

        if(currentToken.getTipo()==TipoToken.PRINT)
        {
            match(TipoToken.PRINT);
            Statement stmtPrint = new StmtPrint(EXPRESSION());
            match(TipoToken.SEMICOLON);

            return stmtPrint;
        }
        else
        {
            hayErrores = true;
            return null;
        }
    }

    /* Sentencia RETURN
     * RETURN_STMT -> return RETURN_EXP_OPC ;
     */
    private Statement RETURN_STMT()
    {
        if(hayErrores) return null;

        if(currentToken.getTipo()==TipoToken.RETURN)
        {
            match(TipoToken.RETURN);
            Expression expression = RETURN_EXP_OPC();
            match(TipoToken.SEMICOLON);
            
            return new StmtReturn(expression);
        }
        else
        {
            hayErrores = true;
            return null;
        }
    }

    // RETURN_EXP_OPC -> EXPRESSION || E
    private Expression RETURN_EXP_OPC()
    {
        if(hayErrores) return null;
        if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
        || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN)
        {
            return EXPRESSION();
        }
        return null;
    }

    /* Ciclo WHILE
     * WHILE_STMT -> while ( EXPRESSION ) STATEMENT
     */
    private Statement WHILE_STMT()
    {
        if(hayErrores) return null;

        if(currentToken.getTipo()==TipoToken.WHILE)
        {
            match(TipoToken.WHILE);
            match(TipoToken.LEFT_PAREN);
            Expression condicion = EXPRESSION(); // a==1, a>=0, ... ,etc.
            match(TipoToken.RIGHT_PAREN);
            Statement body = STATEMENT(); // { ... }

            return new StmtLoop(condicion, body);
        }
        else
        {
            hayErrores = true;
            return null;
        }
    }

    // BLOCK -> { DECLARATION }
    private Statement BLOCK()
    {
        if(hayErrores) return null;

        if(currentToken.getTipo()==TipoToken.LEFT_BRACE)
        {
            match(TipoToken.LEFT_BRACE);

            List<Statement> statements = new ArrayList<>();
            Statement block = new StmtBlock(DECLARATION(statements));

            match(TipoToken.RIGHT_BRACE);

            return block;
        }
        else
        {
            hayErrores = true;
            return null;
        }
    }
    
    /* Expresiones
     * 
     */
    /* EXPRESSION -> ASSIGNMENT */

    private Expression EXPRESSION(){
        if (hayErrores) {
                return null;
        }
        if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
        || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN){
    return ASSIGNMENT();
    } else {
        hayErrores = true;
            return null;
    }
    }

    /*ASSIGNMENT -> LOGICOR ASSIGNMETN_OPC */
        private Expression ASSIGNMENT(){
                if(hayErrores ) return null;

                if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
                || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN){
            return ASSIGNMENT_OPC(LOGIC_OR());
        } else{
                hayErrores = true;
                return null;
        }


        /*ASSIGNMENT_OPC -> = EXPRESSION || EPSILON */

            private Expression ASSIGNMENT_OPC (Expression expresion){
                if(hayErrores)  return null;
/*Si se encunetra un equal = */
                if(currentToken.getTipo() == TipoToken.EQUAL){
                        Token nombre = previous(); //Obtiene el anterior token
                        match(TipoToken.EQUAL);
                        Expression valor = EXPRESSION();//Se vueleve a llamar
                      
                        return new ExprAssign(nombre, valor);
                }
                return expresion;
            
        }

        /*LOGIC OR -> LOGIC AND LOGIC OR 2  */

        private Expression LOGIC_OR(){

            if(hayErrores)return null;
            if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
            || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN){
        return LOGIC_OR_2(LOGIC_AND());
    } else {
        hayErrores = true;
        return null;
    }
        }


            //LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2 || EMPTY
    private Expression LOGIC_OR_2(Expression expr) {

        if(hayErrores)
            return null;

        if(currentToken.getTipo() == TipoToken.OR){
            match(TipoToken.OR);
            Token operator = previous();
            Expression expr2 = LOGIC_AND();
            ExprLogical exprLogical = new ExprLogical(expr,operator,expr2);
            return LOGIC_OR_2(exprLogical);
        }

        return expr;

    }

    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private Expression LOGIC_AND() {

        if(hayErrores)
            return null;
        if(currentToken.getTipo() == TipoToken.BANG || currentToken.getTipo() == TipoToken.MINUS || currentToken.getTipo() == TipoToken.FALSE || currentToken.getTipo() == TipoToken.TRUE|| currentToken.getTipo() == TipoToken.NULL
                || currentToken.getTipo() == TipoToken.NUMBER || currentToken.getTipo() == TipoToken.STRING || currentToken.getTipo() == TipoToken.IDENTIFIER || currentToken.getTipo() == TipoToken.LEFT_PAREN){
            return LOGIC_AND_2(EQUALITY());
        }
        else {
            hayErrores = true;
            return null;
        }

    }

    //LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2 || EMPTY
    private Expression LOGIC_AND_2(Expression expr) {

        if(hayErrores)
            return null;
        if(currentToken.getTipo() == TipoToken.AND){
            match(TipoToken.AND);
            Token operator = previous();
            Expression expr2 = EQUALITY();
            ExprLogical exprLogical = new ExprLogical(expr,operator,expr2);
            return LOGIC_AND_2(exprLogical);
        }

        return expr;

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
        switch (currentToken.getTipo()){
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
        switch (currentToken.getTipo()){
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
        switch (currentToken.getTipo()){
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
        switch (currentToken.getTipo()){
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


    private void match(TipoToken tt) {
        if(currentToken.getTipo() ==  tt){
            i++;
            currentToken = tokens.get(i);
        }
        else{
            hayErrores = true;
        }
    }


    private Token previous() {
        return this.tokens.get(i - 1);
    }
}
