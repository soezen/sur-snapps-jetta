package sur.snapps.jetta.data.dbunit;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author sur
 * @since 08/03/2015
 */
@Entity
@Table(name = "FAMILIES")
public class Family {

    @Id
    private String id;
    private String name;
}
