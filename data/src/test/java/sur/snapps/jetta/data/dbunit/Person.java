package sur.snapps.jetta.data.dbunit;

import com.google.common.collect.Lists;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import java.util.List;

/**
 * @author sur
 * @since 07/03/2015
 */
@Entity
@Table(name = "PERSONS")
public class Person {

    @Id
    private String id;
    private String firstName;
    private String lastName;

    @JoinColumn(name = "FAMILY_ID")
    private Family family;

    @CollectionTable(name = "EMAILS", joinColumns = @JoinColumn(name = "PERSON_ID"))
    @Column(name = "EMAIL")
    private List<String> emails = Lists.newArrayList();

    public String getFirstName() {
        return firstName;
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFamily(Family family) {
        this.family = family;
    }
}
