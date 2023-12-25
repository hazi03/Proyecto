import java.util.HashMap;
import java.util.Map;

public class symbolTable {

    private final Map<String, Object> values = new HashMap<>();

    //Existe el identificador de la tabla de simbolos
    boolean existeIdentificador(String identificador){
        return values.containsKey(identificador);
    }

    //Regresa el objeto
    Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        throw new RuntimeException("Variable no definida '" + identificador + "'.");
    }

    //Asigna un valor a la tabla de simbolos.
    void asignar(String identificador, Object valor){
        values.put(identificador, valor);
    }


}