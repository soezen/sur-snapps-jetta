package sur.snapps.jetta.dummy.domain;

/**
 * @author sur
 * @since 21/02/2015
 */
public class Persons {

    public static Person homer() {
        return newPerson("Homer", "Simpson");
    }

    public static Person marge() {
        return newPerson("Marge", "Bouvier");
    }

    public static Person bart() {
        return newPerson("Bart", "Simpson");
    }

    public static Person lisa() {
        return newPerson("Lisa", "Simpson");
    }

    public static Person maggie() {
        return newPerson("Maggie", "Simpson");
    }

    public static Person newPerson(String firstName, String lastName) {
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        return person;
    }
}
