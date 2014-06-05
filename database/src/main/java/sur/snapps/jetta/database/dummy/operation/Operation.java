package sur.snapps.jetta.database.dummy.operation;


public interface Operation<T> {

    T perform(T subject);
}
