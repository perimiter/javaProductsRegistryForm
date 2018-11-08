

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Login to website
 *
 * @author 
 */
public class Login extends HttpServlet {

    String pass;

    @Override
    public void init() {
        pass = this.getServletConfig().getInitParameter("password");
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

            HttpSession session = request.getSession();
            String name = (String) session.getAttribute("name");

            response.setContentType("text/html");

            if (name == null || name.equals("")) {     //check if the user is Login

                request.getRequestDispatcher("login1.html").include(request, response);
                request.getRequestDispatcher("login2.html").include(request, response);

            } else {
                response.sendRedirect("Profile");
            }
        } catch (IOException | ServletException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
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

            HttpSession session = request.getSession(true);

            String name = request.getParameter("name").trim();
            String password = request.getParameter("password");

            PrintWriter toClient = response.getWriter();

            response.setContentType("text/html");

            if (name.length() == 0 || password.length() == 0) {     //login form validation
                request.getRequestDispatcher("login1.html").include(request, response);
                toClient.println("<div class=\"alert alert-danger\">"
                        + "<h2>Name field and Password field can't be emptey</h2>"
                        + "</div>");
                request.getRequestDispatcher("login2.html").include(request, response);
            } else if (password.equals(pass)) {

                session.setAttribute("name", name);
                response.sendRedirect("Profile");
            } else {
                request.getRequestDispatcher("login1.html").include(request, response);
                toClient.println("<div class=\"alert alert-danger\">"
                        + "<h2>Wrong assword, lease ry gain</h2>"
                        + "</div>");
                request.getRequestDispatcher("login2.html").include(request, response);
            }
        } catch (IOException | ServletException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            request.getRequestDispatcher("error.html").include(request, response);
        }
    }
}
