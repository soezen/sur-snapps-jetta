package sur.snapps.jetta.data.dbunit;

import org.dbunit.dataset.ITable;
import org.junit.Test;
import sur.snapps.jetta.data.DataSet;

import static org.junit.Assert.assertEquals;

/**
 * @author sur
 * @since 07/03/2015
 */
@DataSet(type = Person.class, value = "Simpson", selector = "lastName")
public class JettaDataSetTest {


    @Test
    public void test() throws Exception {
        DataSet dataAnnotation = this.getClass().getAnnotation(DataSet.class);
        JettaDataSet dataSet = new JettaDataSet(dataAnnotation);
        assertEquals(3, dataSet.getTables().length);

        ITable families = dataSet.getTable("FAMILIES");
        assertEquals(2, families.getTableMetaData().getColumns().length);
        assertEquals(1, families.getRowCount());

        assertEquals("SIMPSONS", families.getValue(0, "id"));
        assertEquals("Simpson", families.getValue(0, "name"));

        ITable persons = dataSet.getTable("PERSONS");
        assertEquals(4, persons.getTableMetaData().getColumns().length);
        assertEquals(1, persons.getTableMetaData().getPrimaryKeys().length);
        assertEquals("id", persons.getTableMetaData().getPrimaryKeys()[0].getColumnName());
        assertEquals(4, persons.getRowCount());

        assertEquals("HOMER_SIMPSON", persons.getValue(0, "id"));
        assertEquals("Homer", persons.getValue(0,"first_name"));
        assertEquals("Simpson", persons.getValue(0, "last_name"));
        assertEquals("SIMPSONS", persons.getValue(0, "family_id"));

        assertEquals("BART_SIMPSON", persons.getValue(1, "id"));
        assertEquals("Bart", persons.getValue(1, "first_name"));
        assertEquals("Simpson", persons.getValue(1, "last_name"));

        assertEquals("LISA_SIMPSON", persons.getValue(2, "id"));
        assertEquals("Lisa", persons.getValue(2,"first_name"));
        assertEquals("Simpson", persons.getValue(2, "last_name"));

        assertEquals("MAGGIE_SIMPSON", persons.getValue(3, "id"));
        assertEquals("Maggie", persons.getValue(3,"first_name"));
        assertEquals("Simpson", persons.getValue(3, "last_name"));

        ITable emails = dataSet.getTable("EMAILS");
        assertEquals(2, emails.getTableMetaData().getColumns().length);
        assertEquals(2, emails.getRowCount());

        assertEquals("homer.simpson@springfield.com", emails.getValue(0, "email"));
        assertEquals("HOMER_SIMPSON", emails.getValue(0, "person_id"));
    }
}
