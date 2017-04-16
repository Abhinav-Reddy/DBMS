

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

/**
 * Servlet implementation class SignUpOperator
 */
@WebServlet("/SignUpOperator")
public class SignUpOperator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SignUpOperator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String carrrierID = request.getParameter("carrierID").split(" - ")[0];
		Pattern pattern = Pattern.compile("^.+@.+\\..+$");
		Matcher matcher = pattern.matcher(email);
		PrintWriter out = response.getWriter();
		
		
		
		if (matcher.matches() == false){
			out.println("Invalid Email address. Please enter a valid Email ID "+email);
		}
		else{
			//step1 load the driver class  
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
			  
				//step2 create  the connection object  
				Connection con;
				
					con = DriverManager.getConnection(  
							"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");
				    
				//step3 create the statement object  
				Statement stmt=con.createStatement();  
				
				System.out.println("select * from carrieroperator"
						+ " where carriercode like '"+ carrrierID + "' or email like '"+email+"'");
				//step4 execute query  
				ResultSet rs=stmt.executeQuery("select * from carrieroperator"
						+ " where carriercode like '"+ carrrierID + "' or email like '"+email+"'");
				if (rs.next()){
					out.println("Operator/Email is already registered for this carrier");
					System.out.println("Operator/Email is already registered for this carrier "+rs.getFetchSize());
				}
				else{
					System.out.println("insert into carrieroperator values ('" + email + "', '" + password + "', '" + carrrierID+"')");
					stmt.executeQuery("insert into carrieroperator values ('" + email + "', '" + password + "', '" + carrrierID+"')");
					out.println("Carrier operator registered successfully");
					System.out.println("Carrier operator registered successfully");
				}
				  
				con.close();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
		//response.getWriter().append("Served at: ").append(request.getContextPath());

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
