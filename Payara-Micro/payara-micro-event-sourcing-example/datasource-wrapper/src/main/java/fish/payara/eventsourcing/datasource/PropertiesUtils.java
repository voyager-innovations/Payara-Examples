package fish.payara.eventsourcing.datasource;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import static java.util.Collections.unmodifiableMap;
import static java.util.logging.Level.SEVERE;

public class PropertiesUtils {
    private static final Logger logger = Logger.getLogger(PropertiesUtils.class.getName());

    public static Map<String, String> getFromBase(String base) {
        /*String earBaseUrl = getEarBaseUrl();
        String stage = getProperty("example.staging");
        if (stage == null) {
            throw new IllegalStateException("example.staging property not found. Please add it, e.g. -Dexample.staging=dev");
        }*/
        String earBaseUrl = "";

        Map<String, String> settings = new HashMap<>();

        loadXMLFromUrl(earBaseUrl + "/conf/" + base, settings);
        //loadXMLFromUrl(earBaseUrl + "/conf/" + stage + "/" + base, settings);

        return unmodifiableMap(settings);
    }

    public static String getEarBaseUrl() {
        URL dummyUrl = Thread.currentThread().getContextClassLoader().getResource("META-INF/dummy.txt");
        String dummyExternalForm = dummyUrl.toExternalForm();

        int ejbJarPos = dummyExternalForm.lastIndexOf(".jar");
        if (ejbJarPos != -1) {

            String withoutJar = dummyExternalForm.substring(0, ejbJarPos);
            int lastSlash = withoutJar.lastIndexOf('/');

            return withoutJar.substring(0, lastSlash);
        }

        throw new IllegalStateException("Can't derive EAR root from: " + dummyExternalForm);
    }

    public static void loadXMLFromUrl(String url, Map<String, String> settings) {
        // Get from classpath for now
        String CONFIG_FILE2 = "datasource-settings.xml";

        try {
            Properties properties = new Properties();
            URL newUrl = PropertiesUtils.class.getClassLoader().getResource(CONFIG_FILE2);
            //properties.loadFromXML(new URL(url).openStream());
            logger.info(String.format("Check path %d .", newUrl.getPath().toString()));
            properties.loadFromXML(newUrl.openStream());

            logger.info(String.format("Loaded %d settings from %s.", properties.size(), url));

            settings.putAll(asMap(properties));

        } catch (Exception e) {
            logger.log(SEVERE, "Error while loading settings.", e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static Map<String, String> asMap(Properties properties) {
        return (Map<String, String>)( (Map) properties);
    }
}
