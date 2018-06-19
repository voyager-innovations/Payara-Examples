package fish.payara.eventsourcing.datasource;

import javax.sql.CommonDataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SwitchableCommonDataSource extends CommonDataSourceWrapper {
    private boolean init;
    private String configFile;
    private Map<String, Object> tempValues = new HashMap<>();

    @Override
    public void set(String name, Object value) {
        if (init) {
            super.set(name, value);
        } else {
            tempValues.put(name, value);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String name) {
        if (init) {
            return super.get(name);
        } else {
            return (T) tempValues.get(name);
        }
    }

    public String getConfigFile() {
        return configFile;
    }

    public void setConfigFile(String configFile) {
        this.configFile = configFile;

        // Nasty, but there's not an @PostConstruct equivalent on a DataSource that's called
        // when all properties have been set.
        doInit();
    }

    public void doInit() {

        // Get the properties that were defined separately from the @DataSourceDefinition/data-source element
        Map<String, String> properties = PropertiesUtils.getFromBase(configFile);

        // Get & check the most important property; the class name of the data source that we wrap.
        String className = properties.get("className");
        if (className == null) {
            throw new IllegalStateException("Required parameter 'className' missing.");
        }

        initDataSource(newInstance(className));

        // Set the properties on the wrapped data source that were already set on this class before doInit()
        // was possible.
        for (Entry<String, Object> property : tempValues.entrySet()) {
            super.set(property.getKey(), property.getValue());
        }

        // Set the properties on the wrapped data source that were loaded from the external file.
        for (Entry<String, String> property : properties.entrySet()) {
            if (!property.getKey().equals("className")) {
                setWithConversion(property.getKey(), property.getValue());
            }
        }

        // After this properties will be set directly on the wrapped data source instance.
        init = true;
    }

    private CommonDataSource newInstance(String className) {
        try {
            return (CommonDataSource) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }
}
