package sur.snapps.jetta.core.el.operation;


public interface Operation<T> {

    T perform(T subject);
}
