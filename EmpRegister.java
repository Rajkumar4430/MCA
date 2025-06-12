/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package QRAttendance.QR;

import QRAttendance.SQLconnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author NARESH
 */
@MultipartConfig(maxFileSize = 16177215)   
public class EmpRegister extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
           String ename = request.getParameter("empname");
            String fname = request.getParameter("fathername");
            String email = request.getParameter("email");
            String dob = request.getParameter("dob");
            String phone = request.getParameter("phone");
            String address = request.getParameter("address");
            String pass = request.getParameter("pass");
            String dept = request.getParameter("dept");
            String designation = request.getParameter("designation");

            System.out.println("Employee Details :" + ename + fname + email + dob + phone + address + pass + dept);
            InputStream inputStream = null;

              Part filePart = request.getPart("ephoto");
            if (filePart != null) {
                System.out.println(filePart.getName());
                System.out.println(filePart.getSize());
                System.out.println(filePart.getContentType());

                inputStream = filePart.getInputStream();
            }
            //Tandom
            Random RANDOM = new SecureRandom();
            int PIN = 7;
            String letters = "123456789";
            String empid = "";
            for (int i = 0; i < PIN; i++) {
                int index = (int) (RANDOM.nextDouble() * letters.length());
                empid += letters.substring(index, index + 1);
            }
            String empId = "EMP"+empid;
           
            Connection conn= SQLconnection.getconnection();
            Statement st1 = conn.createStatement();
                ResultSet rs = st1.executeQuery("select * from emp_details where email='" + email + "'");
            int count = 0;
            while (rs.next()) {

                count++;
            }
            if (count > 0) {
                response.sendRedirect("Register.jsp?mailid");
            } else {

            try {

               
                String sql = "INSERT INTO emp_details (empid, ename, fname, email, dob, phone, address, pass, ephoto, dept, designation) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, empId);
                statement.setString(2, ename);
                statement.setString(3, fname);
                statement.setString(4, email);
                statement.setString(5, dob);
                statement.setString(6, phone);
                statement.setString(7, address);
                statement.setString(8, pass);

                if (inputStream != null) {
                    statement.setBlob(9, inputStream);
                }
                statement.setString(10, dept);
                statement.setString(11, designation);
                int row = statement.executeUpdate();
                if (row > 0) {
                    // Create and write to the file
                    response.sendRedirect("Register.jsp?Success");
                } else {
                    response.sendRedirect("Register.jsp?Failed");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmpRegister.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
