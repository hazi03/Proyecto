public class Arbol 
{
    private final Nodo root;
    private final TablaSimbolos tablaSimbolos;
    private Boolean state;
    
    public Arbol(Nodo root)
    {
        this.root = root;
        this.tablaSimbolos = new TablaSimbolos();
    }

    public void recorrer()
    {
        for(Nodo n : root.getChildren())
        {
            Token t = n.getValue();
            switch (t.tipo) 
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
                    Aritmetico solver = new Aritmetico(n, this.tablaSimbolos);
                    Object res = solver.solve();
                    System.out.println("Resultado de la operacion: " + res);
                    break;

                case VAR:
                    Nodo identifier = n.getChildren().get(0);
                    if(tablaSimbolos.existeIdentificador(identifier.getValue().lexema))
                    {
                        System.out.println("ERROR. Variable duplicada.");
                        throw new RuntimeException("Variable ya definida: " + identifier.getValue().lexema);
                    }
                    if(n.getChildren().size() == 1)
                        tablaSimbolos.asignar(identifier.getValue().lexema, 0);
                    else
                    {
                        for(int i = 1 ; i < n.getChildren().size() ; i++)
                        {
                            Nodo value = n.getChildren().get(i);
                            if(value.getValue().tipo == TipoToken.NUMBER)
                                tablaSimbolos.asignar(identifier.getValue().lexema, value.getValue().literal);
                            else if(value.getValue().tipo == TipoToken.STRING)
                                tablaSimbolos.asignar(identifier.getValue().lexema, value.getValue().lexema);
                            else if(value.getValue().tipo == TipoToken.IDENTIFIER)
                                tablaSimbolos.asignar(identifier.getValue().lexema, tablaSimbolos.obtener(value.getValue().lexema));
                            else if(value.getValue().tipo == TipoToken.TRUE || value.getValue().tipo == TipoToken.FALSE)
                                tablaSimbolos.asignar(identifier.getValue().lexema, value.getValue().lexema);
                            else if(value.getValue().tipo == TipoToken.PLUS || value.getValue().tipo == TipoToken.MINUS ||
                                    value.getValue().tipo == TipoToken.STAR || value.getValue().tipo == TipoToken.SLASH || 
                                    value.getValue().tipo == TipoToken.EQUAL || value.getValue().tipo == TipoToken.EQUAL_EQUAL ||
                                    value.getValue().tipo == TipoToken.LESS || value.getValue().tipo == TipoToken.LESS_EQUAL ||
                                    value.getValue().tipo == TipoToken.GREATER || value.getValue().tipo == TipoToken.GREATER_EQUAL ||
                                    value.getValue().tipo == TipoToken.BANG_EQUAL || value.getValue().tipo == TipoToken.OR ||
                                    value.getValue().tipo == TipoToken.AND)
                            {
                                Aritmetico solverVariable = new Aritmetico(value, this.tablaSimbolos);
                                Object resVariable = solverVariable.solve();
                                tablaSimbolos.asignar(identifier.getValue().lexema, resVariable);
                            }
                            else
                                throw new RuntimeException("Tipo de dato no valido: " + value.getValue().lexema);
                        }
                    }
                    break;
                
                case IF:
                    Nodo condicion = n.getChildren().get(0);
                    Aritmetico solverIf = new Aritmetico(condicion, this.tablaSimbolos);
                    Object resIf = solverIf.solve();

                    if(!(resIf instanceof Boolean))
                        throw new RuntimeException("Condicion no booleana: " + resIf);

                    state = (Boolean) resIf;
                    if(state)
                    {
                        Nodo bloque = n.getChildren().get(1);
                        switch (bloque.getValue().tipo) 
                        {
                            case PRINT:
                                for(Nodo hijo : bloque.getChildren())
                                {
                                    Aritmetico solvePrint = new Aritmetico(hijo, this.tablaSimbolos);
                                    Object resultado = solvePrint.solve();
                                    System.out.println("Resultado de print: " + resultado);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                    else
                    {
                        if(n.getChildren().size() == 3)
                        {
                            Nodo bloque = n.getChildren().get(2);
                            for(Nodo hijo : bloque.getChildren())
                            {
                                switch (hijo.getValue().tipo) 
                                {
                                    case PRINT:
                                        for(Nodo hijo2 : bloque.getChildren())
                                        {
                                            Nodo aux = hijo2.getChildren().get(0);
                                            Aritmetico solvePrint = new Aritmetico(aux, this.tablaSimbolos);
                                            Object resultado = solvePrint.solve();
                                            System.out.println("Resultado de print: " + resultado);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                break;
                            }
                        }
                    }
                    break;
                
                case PRINT:
                    for(Nodo hijo2 : n.getChildren())
                    {
                        Aritmetico solvePrint = new Aritmetico(hijo2, this.tablaSimbolos);
                        Object resultado = solvePrint.solve();
                        System.out.println("Resultado de print: " + resultado);
                    }
                    break;

                case FOR:
                    /* Inicializacion */
                    Nodo inicio = n.getChildren().get(0);
                    Nodo name = inicio.getChildren().get(0);
                    Nodo value = inicio.getChildren().get(1);
                    tablaSimbolos.asignar(name.getValue().lexema, value.getValue().literal);

                    /* Condicion */
                    Nodo con = n.getChildren().get(1);
                    Aritmetico solveCon = new Aritmetico(con, this.tablaSimbolos);

                    /* Actualizacion */
                    Nodo update = n.getChildren().get(2);
                    Aritmetico solveUpdate = new Aritmetico(update, this.tablaSimbolos);
                    Object resCon = solveCon.solve();

                    do
                    {
                        if(!(resCon instanceof Boolean))
                            throw new RuntimeException("Condicion no booleana: " + resCon);
                        if((Boolean) resCon)
                        {
                            for(int i = 3 ; i < n.getChildren().size() ; i++)
                            {
                                Nodo bloque = n.getChildren().get(i);
                                switch (bloque.getValue().tipo) 
                                {
                                    case PRINT:
                                        for(Nodo nieto : bloque.getChildren())
                                        {
                                            Aritmetico solvePrint = new Aritmetico(nieto, this.tablaSimbolos);
                                            Object resultado = solvePrint.solve();
                                            System.out.println("Resultado de print: " + resultado);
                                        }
                                        break;

                                    case EQUAL:
                                    case EQUAL_EQUAL:
                                    case LESS:
                                    case LESS_EQUAL:
                                    case GREATER:
                                    case GREATER_EQUAL:
                                    case BANG_EQUAL:
                                        Aritmetico solveOperation = new Aritmetico(bloque, this.tablaSimbolos);
                                        Object resultado = solveOperation.solve();
                                        tablaSimbolos.asignar(bloque.getChildren().get(0).getValue().lexema, resultado);
                                        System.out.println("Resultado: " + resultado);
                                        break;
                                
                                    case IF:
                                        Nodo conP2 = bloque.getChildren().get(0);
                                        Aritmetico solveConP2 = new Aritmetico(conP2, this.tablaSimbolos);
                                        Object resIfP2 = solveConP2.solve();

                                        if(!(resIfP2 instanceof Boolean))
                                            throw new RuntimeException("Condicion no booleana: " + resIfP2);
                                        
                                        state = (Boolean) resIfP2;
                                        if(state)
                                        {
                                            Nodo bloqueP2 = bloque.getChildren().get(1);
                                            switch (bloqueP2.getValue().tipo) 
                                            {
                                                case PRINT:
                                                    for(Nodo hijo : bloqueP2.getChildren())
                                                    {
                                                        Aritmetico solvePrint = new Aritmetico(hijo, this.tablaSimbolos);
                                                        Object resultadoP2 = solvePrint.solve();
                                                        System.out.println("Resultado de print: " + resultadoP2);
                                                    }
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        else
                                        {
                                            if(n.getChildren().size() == 3)
                                            {
                                                Nodo bloqueP2 = bloque.getChildren().get(2);
                                                for(Nodo hijo : bloqueP2.getChildren())
                                                {
                                                    switch (hijo.getValue().tipo) 
                                                    {
                                                        case PRINT:
                                                            for(Nodo hijo2 : bloqueP2.getChildren())
                                                            {
                                                                Nodo aux = hijo2.getChildren().get(0);
                                                                Aritmetico solvePrint = new Aritmetico(aux, this.tablaSimbolos);
                                                                Object resultadoP2 = solvePrint.solve();
                                                                System.out.println("Resultado de print: " + resultadoP2);
                                                            }
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                        i = i + 3;
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                        Object u1 = solveUpdate.solve();
                        tablaSimbolos.asignar(name.getValue().lexema, u1);
                        resCon = solveCon.solve();
                    } while((Boolean) resCon);
                    break;

                case WHILE:
                    Nodo condicionWhile = n.getChildren().get(0);
                    Object resWhile;
                    Aritmetico solveWhile;

                    do
                    {
                        solveWhile = new Aritmetico(condicionWhile, this.tablaSimbolos);
                        resWhile = solveWhile.solve();

                        if(!(resWhile instanceof Boolean))
                            throw new RuntimeException("Condicion no booleana: " + resWhile);

                        if((Boolean) resWhile)
                        {
                            for(int i = 1 ; i < n.getChildren().size() ; i++)
                            {
                                Nodo bloque = n.getChildren().get(i);
                                switch (bloque.getValue().tipo) 
                                {
                                    case PRINT:
                                        for(Nodo nieto : bloque.getChildren())
                                        {
                                            Aritmetico solvePrint = new Aritmetico(nieto, this.tablaSimbolos);
                                            Object resultado = solvePrint.solve();
                                            System.out.println("Resultado de print: " + resultado);
                                        }
                                        break;

                                    case EQUAL:
                                    case EQUAL_EQUAL:
                                    case LESS:
                                    case LESS_EQUAL:
                                    case GREATER:
                                    case GREATER_EQUAL:
                                    case BANG_EQUAL:
                                        Aritmetico solveOperation = new Aritmetico(bloque, this.tablaSimbolos);
                                        Object resultado = solveOperation.solve();
                                        tablaSimbolos.asignar(bloque.getChildren().get(0).getValue().lexema, resultado);
                                        System.out.println("Resultado: " + resultado);
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    } while ((Boolean) resWhile);
                    break;

                default:
                    break;
            }
        }
    }
}
