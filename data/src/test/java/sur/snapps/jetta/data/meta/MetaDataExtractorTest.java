package sur.snapps.jetta.data.meta;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MetaDataExtractorTest {

    @Test
    public void extractPutsCorrectTableName() {
        MetaData result = MetaDataExtractor.extract(User.class);
        assertNotNull(result.tableFor("USERS"));
    }

    @Test
    public void extractPutsCorrectTableNameNotSpecifiedByTableAnnotation() {
        MetaData result = MetaDataExtractor.extract(Family.class);
        assertNotNull(result.tableFor("FAMILY"));
    }

    
}