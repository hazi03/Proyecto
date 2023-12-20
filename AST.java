import java.util.List;
import java.util.Stack;

public class AST 
{
    private final List<Token> postfija;
    private final Stack<Nodo> stack;

    public AST(List<Token> postfija)
    {
        this.postfija = postfija;
        this.stack = new Stack<>();
    }

    public Arbol generarAST()
    {
        Stack<Nodo> stackFathers = new Stack<>();
        Nodo root = new Nodo(null);
        stackFathers.push(root);
        Nodo father = root;

        for(Token t : postfija)
        {
            if(t.tipo == TipoToken.EOF)
                break;
            if(t.isKeyword())
            {
                Nodo n = new Nodo(t);
                father = stackFathers.peek();
                father.insertarSiguienteHijo(n);
                stackFathers.push(n);
                father = n;
            }
            else if(t.isExpression())
            {
                Nodo n = new Nodo(t);
                stack.push(n);
            }
            else if(t.isOperator())
            {
                int arity = t.arity();
                Nodo n = new Nodo(t);
                for(int i = 1 ; i <= arity ; i++)
                {
                    Nodo aux = stack.pop();
                    n.insertarHijo(aux);
                }
                stack.push(n);
            }
            else if(t.tipo == TipoToken.SEMICOLON)
            {
                if(stack.isEmpty())
                {
                    stackFathers.pop();
                    father = stackFathers.peek();
                }
                else
                {
                    Nodo n = stack.pop();
                    if(father.getValue() != null && father.getValue().tipo == TipoToken.VAR)
                    {
                        if(n.getValue().lexema == "=")
                            father.insertarHijos(n.getChildren());
                        else
                            father.insertarSiguienteHijo(n);

                        stackFathers.pop();
                        father = stackFathers.peek();
                    }
                    else if(father.getValue() != null && father.getValue().tipo == TipoToken.PRINT)
                    {
                        father.insertarSiguienteHijo(n);
                        stackFathers.pop();
                        father = stackFathers.peek();
                    }
                    else
                        father.insertarSiguienteHijo(n);
                }
            }
        }

        Arbol last = new Arbol(root);
        return last;
    }
}
