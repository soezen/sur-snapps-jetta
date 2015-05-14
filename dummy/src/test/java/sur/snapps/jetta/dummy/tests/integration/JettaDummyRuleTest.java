package sur.snapps.jetta.dummy.tests.integration;

import org.apache.commons.lang.time.DateUtils;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import sur.snapps.jetta.dummy.JettaDummyRule;
import sur.snapps.jetta.dummy.annotation.Dummies;
import sur.snapps.jetta.dummy.annotation.Dummy;
import sur.snapps.jetta.dummy.annotation.DummyConfig;
import sur.snapps.jetta.dummy.domain.Address;
import sur.snapps.jetta.dummy.domain.Country;
import sur.snapps.jetta.dummy.domain.Person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author sur
 * @since 20/02/2015
 */
// TODO add domain object that overwrites default identifier
@Dummies({
    @DummyConfig(type = Person.class, path = "//dummies/persons/person"),
    @DummyConfig(type = Address.class, path = "//dummies/persons/address")
})
public class JettaDummyRuleTest {

    @Rule
    public JettaDummyRule dummyRule = new JettaDummyRule(this);

    @Dummy("HOMER_SIMPSON")
    private Person homer;

    @Dummy("MARGE_BOUVIER")
    private Person marge;

    @Dummy(value = "Lisa", identifier = "firstName")
    private Person lisa;

    @Dummy("BART_SIMPSON")
    private Person bart;

    @Dummy("MAGGIE_SIMPSON")
    private Person maggie;

    @Test
    public void dummyReferencesSameObjects() {
        assertSame(homer.getAddress(), lisa.getAddress());
    }

    @Test
    public void dummyInjectedWithOtherIdentifier() {
        assertNotNull(lisa);
        assertEquals("Lisa", lisa.getFirstName());
    }

    @Test
    public void dummyInjectedWithSimpleChild() {
        assertNotNull(marge.getCharacteristics());
    }

    /**
     * Dummy uses identifier that is not present in java object
     * but only in xml.
     */
    @Test
    @Ignore("TODO later")
    public void dummyIdentifiedWithTransientIdentifier() {
        fail("not yet implemented");
    }

    @Test
    public void dummyInjected() {
        assertNotNull(homer);
    }

    @Test
    public void dummyStringInjected() {
        assertEquals("HOMER_SIMPSON", homer.getId());
    }

    @Test
    public void dummyDateInjected() throws Exception {
        assertEquals(DateUtils.parseDate("01-01-2015", new String[] {"dd-MM-yyyy"}), homer.getBirthDate());
    }

    @Test
    public void dummyEnumInjected() {
        assertEquals(Country.BELGIUM, homer.getAddress().getCountry());
    }

    @Test
    public void dummyListOfOtherDummiesInjected() {
        assertNotNull(bart.getContactInfos());
        assertEquals(2, bart.getContactInfos().size());
        assertNotNull(bart.getContactInfos().get(0));
        assertNotNull(bart.getContactInfos().get(1));
    }

    @Test
    public void dummyListOfOwnReferencesInjected() {
        assertNotNull(lisa.getParents());
        assertEquals(2, lisa.getParents().size());
        assertTrue(lisa.getParents().contains(homer));
        assertTrue(lisa.getParents().contains(marge));
    }

    @Test
    public void dummyListOfNonDummiesInjected() {
        assertNotNull(lisa.getNicknames());
        assertEquals(3, lisa.getNicknames().size());
        assertTrue(lisa.getNicknames().contains("Lisa"));
        assertTrue(lisa.getNicknames().contains("Other nickname"));
        assertTrue(lisa.getNicknames().contains("Lisa's Nickname"));
    }

    @Test
    public void dummyOtherDummyInjected() {
        assertNotNull(homer.getAddress());
    }

    @Test
    public void dummyIntegerInjected() {
        assertEquals(20, homer.getAge());
    }

    @Test
    public void dummyFieldWithCamelCaseInjected() {
        assertEquals("Simpson", homer.getLastName());
    }

    @Test
    public void dummyInjectedWithOtherDummyWithIdentifierInXml() {
        assertNotNull(maggie.getAddress());
    }
}
