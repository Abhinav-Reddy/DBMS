

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class OperatorLogin
 */
@WebServlet("/OperatorLogin")
public class OperatorLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OperatorLogin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		System.out.println(email + " " + password);
		//step1 load the driver class  
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		  
			//step2 create  the connection object  
			Connection con;
			
			con = DriverManager.getConnection(  
						"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");
			    
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			  
			//step4 execute query  
			ResultSet rs=stmt.executeQuery("select * from carrieroperator where email like '"+ email + "' and password like '" + password +"'");
			if (rs.next()){
				Cookie cookie = new Cookie("LoginDetails", rs.getString(3));
				response.addCookie(cookie);
				out.println("Logged In succeffully");
			}
			else{
				out.println("Invalid email ID and password");
			}
			  
			con.close();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
