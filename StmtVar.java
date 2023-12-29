
public class StmtVar extends Statement {
    final Token name;
    final Expression initializer;

    StmtVar(Token name, Expression init) {
        this.name = name;
        this.initializer = init;
    }
}
