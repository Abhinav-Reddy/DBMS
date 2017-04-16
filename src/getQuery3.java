

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query3Result
 */
@WebServlet("/getQuery3")
public class getQuery3 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery3() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		PrintWriter out = response.getWriter();
		String start, end;
		
		start = request.getParameter("startDate");
		end = request.getParameter("endDate");
		
		try{
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");  
			  
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			  
			//step4 execute query
			String query = "Select * from"
			+ "(Select CarrierName, UniqueCarrierCode, avg(LateAircraftDelay+CarrierDelay) as TotalDelay"
			+ " from PASTFLIGHTSCHEDULE p, CARRIER c "
			+ " Where p.OperatedBy = c. UniquecarrierCode and "
			+ " FlightDate  <= to_date('"+ start+"', 'yyyy-mm-dd') and FlightDate  >= to_date('"+ end +"', 'yyyy-mm-dd') "
			+ "Group by UniqueCarrierCode, CarrierName "
			+ "Order by TotalDelay desc) "
			+ "Where rownum < 10+1";
			
			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);  
			while(rs.next())  {
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			out.println("<br>");
			}
			
			String queryTwo = "Select * from"
			+ "(Select CarrierName, UniqueCarrierCode, avg(LateAircraftDelay+CarrierDelay) as TotalDelay"
			+ " from PASTFLIGHTSCHEDULE p, CARRIER c "
			+ " Where p.OperatedBy = c. UniquecarrierCode and "
			+ " FlightDate  <= to_date('"+ start+"', 'yyyy-mm-dd') and FlightDate  >= to_date('"+ end +"', 'yyyy-mm-dd') "
			+ "Group by UniqueCarrierCode, CarrierName "
			+ "Order by TotalDelay asc) "
			+ "Where rownum < 10+1";
					
			System.out.println(queryTwo);
			ResultSet rsTwo=stmt.executeQuery(queryTwo);  
			while(rsTwo.next())  {
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			out.println(rsTwo.getString(2)+" "+rsTwo.getString(3)+" "+rsTwo.getString(4)+"\n");
			out.println("<br>");
			}
			
			System.out.println("Done");
			
			
			//step5 close the connection object  
			con.close();  
			  
			}catch(Exception e){ System.out.println(e);}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
