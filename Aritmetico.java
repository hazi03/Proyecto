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
            if(n.getValue().tipo == TipoToken.NUMBER)
                return n.getValue().literal;
            else if(n.getValue().tipo == TipoToken.STRING)
                return n.getValue().lexema;
            else if(n.getValue().tipo == TipoToken.IDENTIFIER)
                return tablaSimbolos.obtener(n.getValue().lexema);
        }

        Nodo left = n.getChildren().get(0);
        Nodo right = n.getChildren().get(1);

        Object leftRes = solve(left);
        Object rightRes = solve(right); 

        if(leftRes instanceof Double && rightRes instanceof Double)
        {
            double leftValue = (Double) leftRes;
            double rightValue = (Double) rightRes;
            switch (n.getValue().tipo) {
                case PLUS:
                    return leftValue + rightValue;
                case MINUS:
                    return leftValue - rightValue;
                case STAR:
                    return leftValue * rightValue;
                case SLASH:
                    if(rightValue != 0)
                        return leftValue / rightValue;
                    else
                    {
                        System.out.println("ERROR. Division entre 0.");
                        return null;
                    }
                case EQUAL:
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
                    return (Boolean) (leftValue != rightValue);
                default:
                    break;
            }
        }
        else if(leftRes instanceof String && rightRes instanceof String)
        {
            String leftValue = (String) leftRes;
            String rightValue = (String) rightRes;

            if(n.getValue().tipo == TipoToken.PLUS)
                return leftValue + rightValue;
        }
        else if(leftRes instanceof Boolean && rightRes instanceof Boolean)
        {
            Boolean leftValue = (Boolean) leftRes;
            Boolean rightValue = (Boolean) rightRes;

            if(n.getValue().tipo == TipoToken.OR)
                return leftValue || rightValue;
            if(n.getValue().tipo == TipoToken.AND)
                return leftValue && rightValue;
        }
        else
        {
            System.out.println("ERROR. Los operandos "+leftRes+" y "+rightRes+" no son del mismo tipo de dato.");
            return null;
        }
        return null;
    }
}
