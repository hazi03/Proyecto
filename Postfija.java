import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Postfija 
{
    private final List<Token> infija;
    private final Stack<Token> pila;
    private final List<Token> postfija;

    public Postfija(List<Token> infija)
    {
        this.infija = infija;
        this.pila = new Stack<>();
        this.postfija = new ArrayList<>();
    }

    public List<Token> convertir()
    {
        boolean controlStrucuture = false;
        Stack<Token> controlStructureStack = new Stack<>();

        for(int i = 0; i < infija.size(); i++)
        {
            Token t = infija.get(i);

            if(t.tipo == TipoToken.EOF)
            {
                break;
            }

            if(t.isKeyword())
            {
                postfija.add(t);
                if(t.isControlStructure())
                {
                    controlStrucuture = true;
                    controlStructureStack.push(t);
                }
            }
            else if(t.isExpression())
            {
                postfija.add(t);
            }
            else if(t.tipo == TipoToken.LEFT_PAREN)
            {
                pila.push(t);
            }
            else if(t.tipo == TipoToken.RIGHT_PAREN)
            {
                while(!pila.isEmpty() && pila.peek().tipo != TipoToken.LEFT_PAREN)
                {
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                if(controlStrucuture)
                {
                    postfija.add(new Token(TipoToken.SEMICOLON,";",null,0));
                }
                if(!pila.isEmpty() && pila.peek().tipo == TipoToken.LEFT_PAREN)
                {
                    pila.pop();
                }
            }
            else if(t.isOperator())
            {
                while(!pila.isEmpty() && pila.peek().precedenceGreaterEqual(t))
                {
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                pila.push(t);
            }
            else if(t.tipo == TipoToken.SEMICOLON)
            {
                while(!pila.isEmpty() && pila.peek().tipo != TipoToken.LEFT_BRACE)
                {
                    Token temp = pila.pop();
                    postfija.add(temp);
                }
                postfija.add(t);
            }
            else if(t.tipo == TipoToken.LEFT_BRACE)
            {
                pila.push(t);
            }
            else if(t.tipo == TipoToken.RIGHT_BRACE && controlStrucuture)
            {
                // Verificar si hay un ELSE
                if(infija.get(i + 1).tipo == TipoToken.ELSE)
                {
                    // Se saca '}' de la pila
                    pila.pop();
                }
                else
                {
                    pila.pop();
                    postfija.add(new Token(TipoToken.SEMICOLON,";",null,0));
                    controlStructureStack.pop();
                    
                    if(controlStructureStack.isEmpty())
                    {
                        controlStrucuture = false;
                    }
                }
            }
        }
        while(!pila.isEmpty())
        {
            Token temp = pila.pop();
            postfija.add(temp);
        }
        while(!controlStructureStack.isEmpty())
        {
            controlStructureStack.pop();
            postfija.add(new Token(TipoToken.SEMICOLON,";",null,0));
        }
        return postfija;
    }
}
