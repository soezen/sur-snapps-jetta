package sur.snapps.jetta.dummy;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.unitils.util.ReflectionUtils;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * @author sur
 * @since 20/02/2015
 */
public class DummyFinder<D> {

    private List<D> dummies;
    private String defaultIdentifier;

    public DummyFinder(String defaultIdentifier) {
        this.defaultIdentifier = defaultIdentifier;
        this.dummies = Lists.newArrayList();
    }

    public void add(D dummy) {
        if (dummy != null) {
            dummies.add(dummy);
        }
    }

    public DummiesByIdentifier where(String identifier) {
        return new DummiesByIdentifier(identifier);
    }

    public class DummiesByIdentifier {

        private Map<String, D> dummiesByIdentifier;

        public DummiesByIdentifier(String identifier) {
            final String actualIdentifier = isNullOrEmpty(identifier) ? defaultIdentifier : identifier;
            List<D> filteredDummies = FluentIterable.from(dummies)
                .filter(new Predicate<D>() {
                    @Override
                    public boolean apply(D dummy) {
                        Field field = ReflectionUtils.getFieldWithName(dummy.getClass(), actualIdentifier, false);
                        if (field == null) {
                            throw new IllegalArgumentException("Invalid identifier '" + actualIdentifier + "', no such field found in " + dummy.getClass());
                        }
                        return ReflectionUtils.getFieldValue(dummy, field) != null;
                    }
                }).toList();
            dummiesByIdentifier = Maps.uniqueIndex(filteredDummies, new DummyByIdentifierFunction(actualIdentifier));
        }

        public D is(String identifierValue) {
            return dummiesByIdentifier.get(identifierValue);
        }
    }

    class DummyByIdentifierFunction implements Function<D, String> {

        private String identifier;

        public DummyByIdentifierFunction(String identifier) {
            this.identifier = identifier;
        }

        @Nonnull
        @Override
        public String apply(@Nonnull D dummy) {
            Field field = ReflectionUtils.getFieldWithName(dummy.getClass(), identifier, false);
            if (field == null) {
                throw new IllegalArgumentException("Invalid identifier '" + identifier + "', no such field found in " + dummy.getClass());
            }
            return ReflectionUtils.getFieldValue(dummy, field);
        }
    }
}
