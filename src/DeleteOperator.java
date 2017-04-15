

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

import com.sun.org.apache.bcel.internal.generic.Select;

/**
 * Servlet implementation class DeleteOperator
 */
@WebServlet("/DeleteOperator")
public class DeleteOperator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteOperator() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		
		//step1 load the driver class  
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		  
			//step2 create  the connection object  
			Connection con;
			
			con = DriverManager.getConnection(  
						"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");
			    
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			
			
			Cookie[] cookies = request.getCookies();
			//step4 execute query  
			for(Cookie c : cookies){
				if (c.getName().equals("LoginDetails")){
					String carrierID = c.getValue();
					stmt.executeQuery("delete from carrieroperator"
							+ " where carrierid like " + carrierID);
					c.setValue(null);
					c.setMaxAge(0);
					response.addCookie(c);
					out.println("Deleted carrier operator successfully");
					break;
				}
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
