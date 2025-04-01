import java.io.*;
import java.sql.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class StudentServlet3 extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String Seat_no = request.getParameter("Seat_no");
        String Name = request.getParameter("Name");
        String ans1 = request.getParameter("group1");
        String ans2 = request.getParameter("group2");
        String ans3 = request.getParameter("group3");
        String ans4 = request.getParameter("group4");
        String ans5 = request.getParameter("group5");

        int Total = 0;
        if ("True".equals(ans1))
            Total += 2;
        if ("False".equals(ans2))
            Total += 2;
        if ("True".equals(ans3))
            Total += 2;
        if ("False".equals(ans4))
            Total += 2;
        if ("False".equals(ans5))
            Total += 2;

        Connection connect = null;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body bgcolor=cyan><center>");
        out.println("<h1>Thank you for participating in the online exam</h1>");
        out.println("<h3>Your results are stored in our database</h3>");
        out.println("<br><br>");
        out.println("<b>Participants and their Marks</b>");
        out.println("<table border=5>");
        out.println("<tr><th>Seat_no</th><th>Name</th><th>Marks</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/studentdb", "root", "password");

            // Insert student data
            String query = "INSERT INTO student (Seat_no, Name, Total) VALUES (?, ?, ?)";
            pstmt = connect.prepareStatement(query);
            pstmt.setString(1, Seat_no);
            pstmt.setString(2, Name);
            pstmt.setInt(3, Total);
            pstmt.executeUpdate();
            pstmt.close(); // Close the prepared statement

            // Open a new statement for retrieving data
            stmt = connect.createStatement();
            rs = stmt.executeQuery("SELECT * FROM student");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getString("Seat_no") + "</td>");
                out.println("<td>" + rs.getString("Name") + "</td>");
                out.println("<td>" + rs.getInt("Total") + "</td>");
                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p>Error: " + e.getMessage() + "</p>");
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (connect != null)
                    connect.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        out.println("</table>");
        out.println("</center></body></html>");
    }
}
