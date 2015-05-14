package sur.snapps.jetta.data;

import com.google.common.collect.Maps;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * DataCollection.
 *
 * Created by sur on 19/02/2015.
 */
public final class DataCollection {

    private static Map<Class<?>, DataFinder<?>> dataFindersByType = Maps.newHashMap();

    private DataCollection() {}

    @SuppressWarnings("unchecked")
    public static <D> DataFinder<D> find(Class<D> dataClass) {
        checkNotNull(dataClass, "dataClass is required");

        // find or create dummy finder
        // dummy finder creates config of jpa config
        if (dataFindersByType.containsKey(dataClass)) {
            return (DataFinder<D>) dataFindersByType.get(dataClass);
        }

        DataFinder<D> dataFinder = createDataFinder(dataClass);
        dataFindersByType.put(dataClass, dataFinder);
        return dataFinder;
    }

    public static <D> DataFinder<D> findPersistentObjects(Class<D> dataClass) {
        return new DataFinder<>(dataClass, true);
    }

    private static <D> DataFinder<D> createDataFinder(Class<D> dataClass) {
        DataFinder<D> dataFinder = new DataFinder<>(dataClass);
//        dataFor(dataClass, dataFinder);
        return dataFinder;
    }


}
