
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * central controlling servlet. controls movment in the site
 * @author 
 */
public class Profile extends HttpServlet {

    String mode = "add";
    RequestResult reqRslt = new RequestResult();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @throws java.sql.SQLException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {

        HttpSession session = request.getSession();
        String name = (String) session.getAttribute("name");
        if (name == null || name.equals("")) {  //check if user is login, else redirect to login
            response.sendRedirect("welcome");
        }

        response.setContentType("text/html;charset=UTF-8");
        String gotosearchbtn = request.getParameter("gotosearchbtn");
        String gotoaddbtn = request.getParameter("gotoaddProductbtn");
        String addbtn = request.getParameter("addbtn");
        String searchbtn = request.getParameter("searchbtn");
        PrintWriter out = response.getWriter();

        if ("search".equals(gotosearchbtn)) {   //check if go to search is clicked
            this.mode = "search";       //search mode
        } else if ("add".equals(gotoaddbtn)) {  //check if go to add is clicked
            this.mode = "add";          //add mode
        }

        response.setContentType("text/html;charset=UTF-8");

        if ("add".equals(mode)) {       //mode = "add", display the addProduct form

            if (addbtn != null) {       //in "add" mode and "add product" is clicked
                request.getRequestDispatcher("AddProduct").include(request, response);
                request.getRequestDispatcher("add1.html").include(request, response);
                out.print(name);
                reqRslt = (RequestResult) request.getAttribute("reqRslt");  //addProduct servlet store it in the request attributes
                out.print("<h2>" + "<div class=\"alert alert-" + reqRslt.msgType + "\">" + reqRslt.msg + "</h2>");          //addProduct servlet store the add mode in the session
            } else {                    //default display of the "add" form      
                request.getRequestDispatcher("add1.html").include(request, response);
                out.print(name);
            }
            out.print(" <h2>Please select a product you would like to add</h2>");
            request.getRequestDispatcher("add2.html").include(request, response);
            request.getRequestDispatcher("add3.html").include(request, response);
        } else {                        // mode = "search, display the searchProduct form

            if (searchbtn != null) {    //in "search" mode and "search product" is clicked 
                request.getRequestDispatcher("SearchProduct").include(request, response);
                String searchResult = "";    
                RequestResult result = (RequestResult) request.getAttribute("reqRslt"); //searchProduct servlet store it in the request attributes

                if (result.srchrslt != null) {
                    searchResult += "<h3>Your products</h3>";
                    searchResult += "<ul>";
                    while (result.srchrslt.next()) {
                        searchResult += "<li>" + result.srchrslt.getString(1) + "</li>";
                    }
                    searchResult += "</ul>";
                } else {
                    searchResult += "<div class=\"alert alert-" + result.msgType + "\">"
                            + result.msg
                            + "</div>";
                }
                request.getRequestDispatcher("search1.html").include(request, response);
                out.print(name);
                out.print(searchResult);

            } else {                    //default display of the "search" form
                request.getRequestDispatcher("search1.html").include(request, response);
                out.print(name);
            }

            out.print(" <h2>Please select a product you would like to search for</h2>");
            request.getRequestDispatcher("search2.html").include(request, response);
            request.getRequestDispatcher("search3.html").include(request, response);
        }
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            request.getRequestDispatcher("error.html").include(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Profile.class.getName()).log(Level.SEVERE, null, ex);
            request.getRequestDispatcher("error.html").include(request, response);
        }
    }

}
