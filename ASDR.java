import java.util.List;

public class ASDR implements Parser
{
    private int i=0;
    private boolean hayErrores = false;
    private Token preanalisis;
    private final List<Token> tokens;

    public ASDR(List<Token> tokens)
    {
        this.tokens=tokens;
        preanalisis=this.tokens.get(i);
    }

    @Override
    public boolean parse()
    {
        PROGRAM();
        if(!hayErrores && preanalisis.tipo==TipoToken.EOF)
        {
            System.out.println("Consulta correcta");
            return true;
        }
        else
            System.out.println("Se encontraron errores");
        return false;
    }

    //PROGRAM -> DECLARATION
    private void PROGRAM()
    {
        DECLARATION();
    }

    /*
    DECLARATION -> FUN_DECL DECLARATION
                -> VAR_DECL DECLARATION
                -> STATEMENT DECLARATION
                -> E
    */
    private void DECLARATION()
    {
        if(hayErrores) return;

    }

    //FUN_DECL -> fun FUNCTION
    private void FUN_DECL()
    {
        if(hayErrores) return;
    }

    //FUNCTION -> id ( PARAMETERS_OPC ) BLOCK
    private void FUNCTION()
    {
        if(hayErrores) return;
    }

    /*
    PARAMETERS_OPC -> PARAMETERS
                   -> E
    */
    private void PARAMETERS_OPC()
    {
        if(hayErrores) return;
    }

    //PARAMETERS -> id PARAMETERS_2
    private void PARAMETERS()
    {
        if(hayErrores) return;
    }

    /*
    PARAMETERS_2 -> , id PARAMETERS_2
                 -> E
    */
    private void PARAMETERS_2()
    {
        if(hayErrores) return;
    }

    /*****************************************************************************************************/
    //VAR_DECL -> var id VAR_INIT ;
    private void VAR_DECL()
    {
        if(hayErrores) return;
    }

    /*
    VAR_INIT -> = EXPRESSION
             -> E
    */
    private void VAR_INIT()
    {
        if(hayErrores) return;
    }

    //EXPRESSION -> ASSIGNMENT
    private void EXPRESSION()
    {
        if(hayErrores) return;
    }

    //ASSIGNMENT -> LOGIC_OR ASSIGNMENT_OPC
    private void ASSIGNMENT()
    {
        if(hayErrores) return;
    }

    //LOGIC_OR -> LOGIC_AND LOGIC_OR_2
    private void LOGIC_OR()
    {
        if(hayErrores) return;
    }

    //LOGIC_AND -> EQUALITY LOGIC_AND_2
    private void LOGIC_AND()
    {
        if(hayErrores) return;
    }

    //EQUALITY -> COMPARISON EQUALITY_2
    private void EQUALITY()
    {
        if(hayErrores) return;
    }

    //COMPARISON -> TERM COMPARISON_2
    private void COMPARISON()
    {
        if(hayErrores) return;
    }

    //TERM -> FACTOR TERM_2
    private void TERM()
    {
        if(hayErrores) return;
    }

    //FACTOR -> UNARY FACTOR_2
    private void FACTOR()
    {
        if(hayErrores) return;
    }

    /*
    UNARY -> ! UNARY
          -> - UNARY
          -> CALL
    */
    private void UNARY()
    {
        if(hayErrores) return;
    }

    //CALL -> PRIMARY CALL_2
    private void CALL()
    {
        if(hayErrores) return;
    }

    /*
    PRIMARY -> true
            -> false
            -> null
            -> number
            -> string
            -> id
            -> ( EXPRESSION ) 
    */
    private void PRIMARY()
    {
        if(hayErrores) return;
    }

    /*
    CALL_2 -> ( ARGUMENTS_OPC ) CALL_2
           -> E
    */
    private void CALL_2()
    {
        if(hayErrores) return;
    }

    /*
    ARGUMENTS_OPC -> EXPRESSION ARGUMENTS
                  -> E
    */
    private void ARGUMENTS_OPC()
    {
        if(hayErrores) return;
    }

    /*
    ARGUMENTS -> , EXPRESSION ARGUMENTS
              -> E
    */
    private void ARGUMENTS()
    {
        if(hayErrores) return;
    }

    /*
    FACTOR_2 -> / UNARY FACTOR_2
             -> * UNARY FACTOR_2
             -> E
    */
    private void FACTOR_2()
    {
        if(hayErrores) return;
    }

    /*
    TERM_2 -> - FACTOR TERM_2
           -> + FACTOR TERM_2
           -> E
    */
    private void TERM_2()
    {
        if(hayErrores) return;
    }

    /*
    COMPARISON_2 -> > TERM COMPARISON_2
                 -> >= TERM COMPARISON_2
                 -> < TERM COMPARISON_2
                 -> <= TERM COMPARISON_2
                 -> E
    */
    private void COMPARISON_2()
    {
        if(hayErrores) return;
    }

    /*
    EQUALITY_2 -> != COMPARISON EQUALITY_2
               -> == COMPARISON EQUALITY_2
               -> E
    */
    private void EQUALITY_2()
    {
        if(hayErrores) return;
    }

    /*
    LOGIC_AND_2 -> and EQUALITY LOGIC_AND_2
                -> E
    */
    private void LOGIC_AND_2()
    {
        if(hayErrores) return;
    }

    /*
    LOGIC_OR_2 -> or LOGIC_AND LOGIC_OR_2
               -> E
    */
    private void LOGIC_OR_2()
    {
        if(hayErrores) return;
    }

    /*
    ASSIGNMENT_OPC -> = EXPRESSION
                   -> E
    */
    private void ASSIGNMENT_OPC()
    {
        if(hayErrores) return;
    }

    /*****************************************************************************************************/
    /*
    STATEMENT -> EXPR_STMT
              -> FOR_STMT
              -> IF_STMT
              -> PRINT_STMT
              -> RETURN_STMT
              -> WHILE_STMT
              -> BLOCK
    */
    private void STATEMENT()
    {
        if(hayErrores) return;
    }

    //EXPR_STMT -> EXPRESSION ;
    private void EXPR_STMT()
    {
        if(hayErrores) return;
    }

    //FOR_STMT -> for ( FOR_STMT_1 FOR_STMT_2 FOR_STMT_3 ) STATEMENT
    private void FOR_STMT()
    {
        if(hayErrores) return;
    }

    /*
    FOR_STMT_1 -> VAR_DECL
               -> EXPR_STMT
               -> ;
    */
    private void FOR_STMT_1()
    {
        if(hayErrores) return;
    }

    /*
    FOR_STMT_2 -> EXPRESSION ;
               -> ;
    */
    private void FOR_STMT_2()
    {
        if(hayErrores) return;
    }

    /*
    FOR_STMT_3 -> EXPRESSION
               -> E 
    */
    private void FOR_STMT_3()
    {
        if(hayErrores) return;
    }

    //IF_STMT -> if ( EXPRESSION ) STATEMENT ELSE_STATEMENT
    private void IF_STMT()
    {
        if(hayErrores) return;
    }

    /*
    ELSE_STATEMENT -> else STATEMENT
                   -> E
    */
    private void ELSE_STATEMENT()
    {
        if(hayErrores) return;
    }

    //PRINT_STMT -> print EXPRESSION ;
    private void PRINT_STMT()
    {
        if(hayErrores) return;
    }

    //RETURN_STMT -> return RETURN_EXP_OPC ;
    private void RETURN_STMT()
    {
        if(hayErrores) return;
    }

    /*
    RETURN_EXP_OPC -> EXPRESSION
                   -> E
    */
    private void RETURN_EXP_OPC()
    {
        if(hayErrores) return;
    }

    //WHILE_STMT -> while ( EXPRESSION ) STATEMENT
    private void WHILE_STMT()
    {
        if(hayErrores) return;
    }

    //BLOCK -> { DECLARATION }
    private void BLOCK()
    {
        if(hayErrores) return;
    }
    
    /******************************************************************************************************/
    private void match(TipoToken tipoToken)
    {
        if(preanalisis.tipo==tipoToken)
        {
            i++;
            preanalisis=tokens.get(i);
        }
        else
        {
            hayErrores=true;
            System.out.println("Error detectado");
        }
    }
}
