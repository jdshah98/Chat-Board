import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/validate")
public class validate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public validate() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String pass = request.getParameter("password");
		PrintWriter out = response.getWriter();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con =   DriverManager.getConnection("jdbc:mysql://localhost:3306/chatboard","root","");
			String sql = "select * from user where email = '" + email + "'";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if(rs.next())
			{
				if(rs.getString(4).equals(pass)){
					Cookie[] cookie = new Cookie[2];
					cookie[0] = new Cookie("name",rs.getString(2));
					cookie[1] = new Cookie("email",email);
					for(int i=0;i<cookie.length;i++)
						response.addCookie(cookie[i]);
					response.sendRedirect("index.jsp");
				}
				else{
					out.println("<meta http-equiv='refresh' content='3';URL='login.html'>");
					out.println("<p style='color:red;'>Incorrect username or password!</p>");
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}

	}

}