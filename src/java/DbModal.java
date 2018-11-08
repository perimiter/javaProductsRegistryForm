
import com.sun.rowset.CachedRowSetImpl;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.rowset.CachedRowSet;

/**
 * servlet handls detabase requests
 *
 * @author 
 */
public class DbModal {

    private Connection con = null;
    private PreparedStatement pstatement = null;
    private String DBdetails = null;

    /**
     * constructor
     *
     * @param DBdet data base details
     * @throws SQLException
     * @throws ClassNotFoundException in case for name failed
     */
    public DbModal(String DBdet) throws SQLException, ClassNotFoundException {

        String a = "com.mysql.jdbc.Driver";
        Class.forName(a);
        DBdetails = DBdet;

    }

    /**
     * insert product into detabase
     *
     * @param product product to be inserted
     * @throws SQLException
     */
    public void add(String product) throws SQLException {
        con = DriverManager.getConnection(DBdetails);
        String query;
        query = "INSERT INTO food (product) VALUES (?)";

        pstatement = con.prepareStatement(query);
        pstatement.setString(1, product);
        try {
            pstatement.executeUpdate();
        } finally {

            if (pstatement != null) {
                try {
                    pstatement.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }

        }
    }

    /**
     * search for products that start with the value product the result is
     * returned to the servlet.
     *
     * @param product value to be searched
     * @return list or products
     * @throws SQLException
     */
    public ResultSet search(String product) throws SQLException {
        ResultSet r = null;
        con = DriverManager.getConnection(DBdetails);

        String query;
        query = "SELECT product FROM food WHERE product LIKE ?";
        pstatement = con.prepareStatement(query);
        pstatement.setString(1, product + "%");
        CachedRowSet rowset = new CachedRowSetImpl();//save data for after disconnect

        try {
            r = pstatement.executeQuery();
            rowset.populate(r);
        } finally {
            if (pstatement != null) {
                try {
                    r.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (pstatement != null) {
                try {
                    pstatement.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    /* ignored */
                }
            }
        }
        return rowset.getOriginal();
    }

}
