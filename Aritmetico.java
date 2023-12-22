public class Aritmetico 
{
    private final Nodo nodo;
    private final TablaSimbolos tablaSimbolos;

    public Aritmetico(Nodo nodo, TablaSimbolos tablaSimbolos)
    {
        this.nodo = nodo;
        this.tablaSimbolos = tablaSimbolos;
    }

    public Object solve()
    {
        return solve(nodo);
    }

    public Object solve(Nodo n)
    {
        if(n.getChildren() == null)
        {
            if(n.getValue().tipo == TipoToken.NUMBER || n.getValue().tipo == TipoToken.STRING)
            {
                return n.getValue().literal;
            }
            else if(n.getValue().tipo == TipoToken.IDENTIFIER)
            {
                String nombreVariable = n.getValue().lexema;
                Object valor = tablaSimbolos.obtener(nombreVariable);
                if(valor == null)
                {
                    throw new RuntimeException("ERROR. Variable inexistente.");
                }
                return valor;
            }
        }

        // Se asume por simplicidad que la lista de hijos del nodo solo tiene dos elementos
        Nodo left = n.getChildren().get(0);
        Nodo right = n.getChildren().get(1);

        Object leftRes = solve(left);
        Object rightRes = solve(right); 

        if(leftRes instanceof Double && rightRes instanceof Double)
        {
            switch (n.getValue().tipo) 
            {
                case PLUS:
                    return ((Double) leftRes + (Double) rightRes);
                case MINUS:
                    return ((Double) leftRes - (Double) rightRes);
                case STAR:
                    return ((Double) leftRes * (Double) rightRes);
                case SLASH:
                    if(((Double) rightRes) != 0)
                        return ((Double) leftRes / (Double) rightRes);
                    else
                    {
                        throw new RuntimeException("ERROR. Division entre cero.");
                    }
                /*case EQUAL:
                    return (Double) rightValue;
                case EQUAL_EQUAL:
                    return (Boolean) (leftValue == rightValue);
                case LESS:
                    return (Boolean) (leftValue < rightValue);
                case LESS_EQUAL:
                    return (Boolean) (leftValue <= rightValue);
                case GREATER:
                    return (Boolean) (leftValue > rightValue);
                case GREATER_EQUAL:
                    return (Boolean) (leftValue >= rightValue);
                case BANG_EQUAL:
                    return (Boolean) (leftValue != rightValue);*/
                default:
                    break;
            }
        }
        else if(leftRes instanceof String && rightRes instanceof String)
        {
            if(n.getValue().tipo == TipoToken.PLUS)
            {
                return ((String) leftRes + (String) rightRes);
            }
        }
        else if(leftRes instanceof Boolean && rightRes instanceof Boolean)
        {
            if(n.getValue().tipo == TipoToken.OR)
            {
                return ((Boolean) leftRes || (Boolean) rightRes);
            }
            if(n.getValue().tipo == TipoToken.AND)
            {
                return ((Boolean) leftRes && (Boolean) rightRes);
            } 
        }
        else
        {
            throw new RuntimeException("ERROR. Los operandos " + leftRes + " y " + rightRes + " no son del mismo tipo de dato.");
        }
        throw new RuntimeException("ERROR. Operacion no valida.");
    }
}
