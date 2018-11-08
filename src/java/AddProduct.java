
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * add product to database
 *
 * @author 
 */
public class AddProduct extends HttpServlet {

    DbModal db_modal = null;
    RequestResult reqRslt = new RequestResult();    //store the add result

    @Override
    public void init() {

        ServletContext context = getServletContext();
        String DBdet = (String) context.getInitParameter("serverDet");

        try {
            db_modal = new DbModal(DBdet);

        } catch (SQLException | ClassNotFoundException sqlex) {
            Logger.getLogger(AddProduct.class.getName()).log(Level.SEVERE, null, sqlex);
            System.exit(1);     //check what we suppose to do here
        }
    }

    /**
     * adds a product to the DB . includes checks for empty input field.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        String proToAdd = request.getParameter("product").trim();
        if (!proToAdd.isEmpty() && proToAdd.length() > 0) {
            try {
                db_modal.add(proToAdd);
                reqRslt.msgType = "success";            //add succeeded
                reqRslt.msg = "Product successfully added";
            } catch (SQLException ex) {                 //add not succeeded, db error
                Logger.getLogger(AddProduct.class.getName()).log(Level.SEVERE, null, ex);
                reqRslt.msgType = "danger";
                reqRslt.msg = ex.getMessage();
                request.setAttribute("reqRslt", reqRslt);
            }
        } else {                                        //add field is empty
            reqRslt.msgType = "danger";
            reqRslt.msg = "Add field can not be empty";
        }
        request.setAttribute("reqRslt", reqRslt);
    }

    /**
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        processRequest(request, response);
    }

    /**
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        processRequest(request, response);

    }
}
