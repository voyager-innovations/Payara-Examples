package fish.payara.eventsourcing.datasource;

import javax.sql.CommonDataSource;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.beans.Introspector.getBeanInfo;
import static java.beans.PropertyEditorManager.findEditor;
import static java.util.Collections.unmodifiableMap;

public class CommonDataSourceWrapper implements CommonDataSource{
    private CommonDataSource commonDataSource;
    private Map<String, PropertyDescriptor> dataSourceProperties;

    public void initDataSource(CommonDataSource dataSource) {
        this.commonDataSource = dataSource;

        try {
            Map<String, PropertyDescriptor> mutableProperties = new HashMap<>();
            for (PropertyDescriptor propertyDescriptor : getBeanInfo(dataSource.getClass()).getPropertyDescriptors()) {
                mutableProperties.put(propertyDescriptor.getName(), propertyDescriptor);
            }

            dataSourceProperties = unmodifiableMap(mutableProperties);

        } catch (IntrospectionException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        try {
            return (T) dataSourceProperties.get(name).getReadMethod().invoke(commonDataSource);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public void set(String name, Object value) {
        try {
            dataSourceProperties.get(name).getWriteMethod().invoke(commonDataSource, value);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }


    public void setWithConversion(String name, String value) {

        PropertyDescriptor property = dataSourceProperties.get(name);

        PropertyEditor editor = findEditor(property.getPropertyType());
        editor.setAsText(value);

        try {
            property.getWriteMethod().invoke(commonDataSource, editor.getValue());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            throw new IllegalStateException(e);
        }
    }

    public CommonDataSource getWrapped() {
        return commonDataSource;
    }


    // ------------------------- CommonDataSource-----------------------------------

    @Override
    public java.io.PrintWriter getLogWriter() throws SQLException {
        return commonDataSource.getLogWriter();
    }

    @Override
    public void setLogWriter(java.io.PrintWriter out) throws SQLException {
        commonDataSource.setLogWriter(out);
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        commonDataSource.setLoginTimeout(seconds);
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return commonDataSource.getLoginTimeout();
    }

    // ------------------------- CommonDataSource JDBC 4.1 -----------------------------------

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return commonDataSource.getParentLogger();
    }


    // ------------------------- Common properties -----------------------------------

    public String getServerName() {
        return get("serverName");
    }

    public void setServerName(String serverName) {
        set("serverName", serverName);
    }

    public String getDatabaseName() {
        return get("databaseName");
    }

    public void setDatabaseName(String databaseName) {
        set("databaseName", databaseName);
    }

    public int getPortNumber() {
        return get("portNumber");
    }

    public void setPortNumber(int portNumber) {
        set("portNumber", portNumber);
    }

    public void setPortNumber(Integer portNumber) {
        set("portNumber", portNumber);
    }

    public String getUser() {
        return get("user");
    }

    public void setUser(String user) {
        set("user", user);
    }

    public String getPassword() {
        return get("password");
    }

    public void setPassword(String password) {
        set("password", password);
    }

    public String getCompatible() {
        return get("compatible");
    }

    public void setCompatible(String compatible) {
        set("compatible", compatible);
    }

    public int getLogLevel() {
        return get("logLevel");
    }

    public void setLogLevel(int logLevel) {
        set("logLevel", logLevel);
    }

    public int getProtocolVersion() {
        return get("protocolVersion");
    }

    public void setProtocolVersion(int protocolVersion) {
        set("protocolVersion", protocolVersion);
    }

    public void setPrepareThreshold(int prepareThreshold) {
        set("prepareThreshold", prepareThreshold);
    }

    public void setReceiveBufferSize(int receiveBufferSize) {
        set("receiveBufferSize", receiveBufferSize);
    }

    public void setSendBufferSize(int sendBufferSize) {
        set("sendBufferSize", sendBufferSize);
    }

    public int getPrepareThreshold() {
        return get("prepareThreshold");
    }

    public void setUnknownLength(int unknownLength) {
        set("unknownLength", unknownLength);
    }

    public int getUnknownLength() {
        return get("unknownLength");
    }

    public void setSocketTimeout(int socketTimeout) {
        set("socketTimeout", socketTimeout);
    }

    public int getSocketTimeout() {
        return get("socketTimeout");
    }

    public void setSsl(boolean ssl) {
        set("ssl", ssl);
    }

    public boolean getSsl() {
        return get("ssl");
    }

    public void setSslfactory(String sslfactory) {
        set("sslfactory", sslfactory);
    }

    public String getSslfactory() {
        return get("sslfactory");
    }

    public void setApplicationName(String applicationName) {
        set("applicationName", applicationName);
    }

    public String getApplicationName() {
        return get("applicationName");
    }

    public void setTcpKeepAlive(boolean tcpKeepAlive) {
        set("tcpKeepAlive", tcpKeepAlive);
    }

    public boolean getTcpKeepAlive() {
        return get("tcpKeepAlive");
    }

    public void setBinaryTransfer(boolean binaryTransfer) {
        set("binaryTransfer", binaryTransfer);
    }

    public boolean getBinaryTransfer() {
        return get("binaryTransfer");
    }

    public void setBinaryTransferEnable(String binaryTransferEnable) {
        set("binaryTransferEnable", binaryTransferEnable);
    }

    public String getBinaryTransferEnable() {
        return get("binaryTransferEnable");
    }

    public void setBinaryTransferDisable(String binaryTransferDisable) {
        set("binaryTransferDisable", binaryTransferDisable);
    }

    public String getBinaryTransferDisable() {
        return get("binaryTransferDisable");
    }
}
