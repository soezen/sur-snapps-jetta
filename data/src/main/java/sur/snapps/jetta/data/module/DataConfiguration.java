package sur.snapps.jetta.data.module;

import sur.snapps.jetta.core.config.JettaConfiguration;
import sur.snapps.jetta.core.config.JettaProperty;

/**
 * Created by sur on 19/02/2015.
 */
@JettaConfiguration("sur.snapps.jetta.data")
public class DataConfiguration {

    public static final String PROPERTY_DATA_SOURCE_BEAN_NAME = "spring.data_source_bean_name";
    private static final String DEFAULT_DATA_SOURCE_BEAN_NAME = "dataSource";

    @JettaProperty(property = "dummies_file", required = true)
    private String dummiesFile;
    @JettaProperty(property = PROPERTY_DATA_SOURCE_BEAN_NAME, required = false, defaultValue = DEFAULT_DATA_SOURCE_BEAN_NAME)
    private String dataSourceBeanName;



    public String dummiesFile() {
        return dummiesFile;
    }

    public String dataSourceBeanName() {
        return dataSourceBeanName;
    }
}
