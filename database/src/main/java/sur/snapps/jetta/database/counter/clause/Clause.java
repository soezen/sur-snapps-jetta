package sur.snapps.jetta.database.counter.clause;


import sur.snapps.jetta.database.DatabaseDialect;

public interface Clause {

    String get(DatabaseDialect dialect);
}
