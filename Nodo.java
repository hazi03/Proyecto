import java.util.ArrayList;
import java.util.List;

public class Nodo 
{
    private final Token value;
    private List<Nodo> children;

    public Nodo(Token value) {this.value = value;}

    public void insertarHijo(Nodo n)
    {
        if(children == null)
        {
            children = new ArrayList<>();
            children.add(n);
        }
        else
            children.add(0,n);
    }

    public void insertarSiguienteHijo(Nodo n)
    {
        if(children == null)
        {
            children = new ArrayList<>();
            children.add(n);
        }
        else
            children.add(n);
    }

    public void insertarHijos(List<Nodo> nodosHijos)
    {
        if(children == null)
            children = new ArrayList<>();

        for(Nodo n : nodosHijos)
            children.add(n);
    }

    public Token getValue() {return value;}
    public List<Nodo> getChildren() {return children;}
}
