package sur.snapps.jetta.data.meta;

import javax.persistence.Entity;

/**
 * @author sur
 * @since 28/02/2015
 */
public class JettaColumn {

    private Class<?> type;
    private String name;
    private boolean primaryKey;

    public JettaColumn(Class<?> type, String name, boolean primaryKey) {
        this.type = type;
        this.name = name;
        this.primaryKey = primaryKey;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public String name() {
        return name;
    }

    public Class<?> type() {
        return type;
    }

    public boolean isEntity() {
        return type.getAnnotation(Entity.class) != null;
    }
}
