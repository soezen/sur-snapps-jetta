package sur.snapps.jetta.database.module;

import sur.snapps.jetta.core.config.JettaConfiguration;
import sur.snapps.jetta.core.config.JettaProperty;

/**
 * User: SUR
 * Date: 4/05/14
 * Time: 18:57
 */
@JettaConfiguration("sur.snapps.jetta.database")
public class DatabaseConfiguration {

    @JettaProperty(property = "script.location", required = true)
    private String scriptLocation;
    @JettaProperty(property = "script.clear", required = false)
    private String clearScript;
    @JettaProperty(property = "datasource.driver", required = true)
    private String dataSourceDriver;
    @JettaProperty(property = "datasource.url", required = true)
    private String dataSourceUrl;
    @JettaProperty(property = "datasource.username", required = true)
    private String dataSourceUsername;
    @JettaProperty(property = "datasource.password", required = true)
    private String dataSourcePassword;


    public DataSource dataSource() {
        return new DataSource();
    }

    public String scriptLocation() {
        return scriptLocation;
    }

    public String clearScript() {
        return clearScript;
    }

    public class DataSource {

        public String driver() {
            return dataSourceDriver;
        }

        public String url() {
            return dataSourceUrl;
        }

        public String username() {
            return dataSourceUsername;
        }

        public String password() {
            return dataSourcePassword;
        }
    }
}
