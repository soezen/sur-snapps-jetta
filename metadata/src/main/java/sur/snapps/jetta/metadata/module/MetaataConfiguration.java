package sur.snapps.jetta.metadata.module;

import sur.snapps.jetta.core.config.JettaConfiguration;
import sur.snapps.jetta.core.config.JettaProperty;

/**
 * User: SUR
 * Date: 23/08/2014
 * Time: 18:57
 */
@JettaConfiguration("sur.snapps.jetta.metadata")
public class MetaataConfiguration {

    @JettaProperty(property = "output.directory", required = false, defaultValue = "target/output/metadata")
    private String directory;
    @JettaProperty(property = "output.filename", required = false, defaultValue = "report.xml")
    private String filename;

    public String filename() {
        return filename;
    }

    public String directory() {
        return directory;
    }
}
