package sur.snapps.jetta.dummy;

import org.junit.Before;
import org.junit.Test;
import sur.snapps.jetta.dummy.domain.Person;

import static org.junit.Assert.assertSame;
import static sur.snapps.jetta.dummy.domain.Persons.bart;
import static sur.snapps.jetta.dummy.domain.Persons.homer;
import static sur.snapps.jetta.dummy.domain.Persons.lisa;
import static sur.snapps.jetta.dummy.domain.Persons.maggie;
import static sur.snapps.jetta.dummy.domain.Persons.marge;

public class DummyFinderTest {

    private DummyFinder<Person> dummyFinder;

    private Person homerSimpson;
    private Person margeBouvier;
    private Person bartSimpson;
    private Person lisaSimpson;
    private Person maggieSimpson;

    @Before
    public void setup() {
        homerSimpson = homer();
        margeBouvier = marge();
        bartSimpson = bart();
        lisaSimpson = lisa();
        maggieSimpson = maggie();

        dummyFinder = new DummyFinder<>("id");
        dummyFinder.add(homerSimpson);
        dummyFinder.add(margeBouvier);
        dummyFinder.add(bartSimpson);
        dummyFinder.add(lisaSimpson);
        dummyFinder.add(maggieSimpson);
        dummyFinder.add(null);
    }

    @Test
    public void whereFirstNameIsLisaReturnsLisa() {
        assertSame(lisaSimpson, dummyFinder.where("firstName").is("Lisa"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void whereIdentifierIsNotPresentInDummyClass() {
        dummyFinder.where("nonExistingField").is("someValue");
    }
}