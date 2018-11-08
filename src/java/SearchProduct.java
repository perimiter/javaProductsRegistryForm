/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * search for all products that start with value
 *
 * @author 
 */
public class SearchProduct extends HttpServlet {

    DbModal db_modal = null;
    RequestResult reqRslt = new RequestResult();    //store the search result

    @Override
    public void init() {

        ServletContext context = getServletContext();
        String DBdet = (String) context.getInitParameter("serverDet");

        try {
            db_modal = new DbModal(DBdet);

        } catch (SQLException | ClassNotFoundException sqlex) {
            Logger.getLogger(SearchProduct.class.getName()).log(Level.SEVERE, null, sqlex);
            System.exit(1);     //check what we suppose to do here
        }
    }

    /**
     * search for requested products that start with a value.the list of
     * products is displayed on screen.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        String valToSrch = request.getParameter("producttosearch").trim();
        ResultSet res = null;

        if (!valToSrch.isEmpty() && valToSrch.length() > 0) {
            try {
                res = db_modal.search(valToSrch);   //search succeeded
                if (res.isBeforeFirst()) {
                    reqRslt.srchrslt = res;
                }
                else{
                    reqRslt.srchrslt = null;
                    reqRslt.msgType = "danger";
                    reqRslt.msg = "No similar products found";
                }
            } catch (SQLException sqlex) {      //db error
                Logger.getLogger(SearchProduct.class.getName()).log(Level.SEVERE, null, sqlex);
                reqRslt.srchrslt = null;
                reqRslt.msgType = "danger";
                reqRslt.msg = sqlex.getMessage();
                request.setAttribute("reqRslt", reqRslt);
            }
        } else {                            //input field is empty
            reqRslt.msgType = "danger";
            reqRslt.msg = "The search field can not be empty";
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
