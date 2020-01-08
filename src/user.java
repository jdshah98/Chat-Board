import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/user")
public class user extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public user() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String name = request.getParameter("uname");
		String email = request.getParameter("email");
		String pass = request.getParameter("pass");
		String c_pass = request.getParameter("c_pass");
		PrintWriter out = response.getWriter(); 
		if(pass.equals(c_pass))
		{
			try
			{
				Class.forName("com.mysql.jdbc.Driver");
				Connection con =   DriverManager.getConnection("jdbc:mysql://localhost:3306/chatboard","root","");  
				String sql = "insert into user(name,email,password) values (?,?,?)";
				PreparedStatement stmt= con.prepareStatement(sql);
				stmt.setString(1,name);
				stmt.setString(2,email);
				stmt.setString(3,pass);
				
				if(stmt.executeUpdate()>0) {
					Cookie[] cookie = new Cookie[2];
					cookie[0] = new Cookie("name",name);
					cookie[1] = new Cookie("email",email);
					for(int i=0;i<cookie.length;i++)
						response.addCookie(cookie[i]);
					response.sendRedirect("index.jsp");
				}
				con.close();
			}catch(Exception e){
				System.out.print(e);
			}
		}
		else
		{
			out.println("<meta http-equiv='refresh' content='3';URL='signup.html'>");
			out.println("<p style='color:red;'>Passwords doesn't match!</p>");
		}
	}	
}