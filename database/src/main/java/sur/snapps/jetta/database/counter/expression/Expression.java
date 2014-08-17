package sur.snapps.jetta.database.counter.expression;


import sur.snapps.jetta.database.DatabaseDialect;

public interface Expression {

    String expression(DatabaseDialect dialect);
}
