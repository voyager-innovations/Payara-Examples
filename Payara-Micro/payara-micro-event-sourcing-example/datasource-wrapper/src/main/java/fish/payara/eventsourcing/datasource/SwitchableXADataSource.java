package fish.payara.eventsourcing.datasource;

import org.postgresql.xa.PGXADataSource;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import java.sql.SQLException;

public class SwitchableXADataSource  extends CommonDataSourceWrapper implements XADataSource {

    public XADataSource getWrapped() {
        return (XADataSource) super.getWrapped();
    }


    // ------------------------- XADataSource-----------------------------------

    @Override
    public XAConnection getXAConnection() throws SQLException {
        return getWrapped().getXAConnection();
    }

    @Override
    public XAConnection getXAConnection(String user, String password) throws SQLException {
        return getWrapped().getXAConnection();
    }



}
