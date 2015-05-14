package sur.snapps.jetta.dummy.module;

import sur.snapps.jetta.core.config.JettaConfiguration;
import sur.snapps.jetta.core.config.JettaProperty;

/**
 * Created by sur on 19/02/2015.
 */
@JettaConfiguration("sur.snapps.jetta.dummy")
public class DummyConfiguration {

    @JettaProperty(property = "dummies_file", required = true)
    private String dummiesFile;

    public String dummiesFile() {
        return dummiesFile;
    }
}
