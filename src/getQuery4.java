

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
 * Servlet implementation class Query4Result
 */
@WebServlet("/getQuery4")
public class getQuery4 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery4() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
PrintWriter out = response.getWriter();
		
		//start = request.getParameter("startDate");
		//end = request.getParameter("endDate");
		
		try{
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");  
			  
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			String src = request.getParameter("source");
			String dest = request.getParameter("destination");
			String carrier = request.getParameter("carrier");
			String[] startTime = request.getParameter("startTime").split(":");
			String[] endTime = request.getParameter("endTime").split(":");
			
			
			String query = "Select 100*avg(nvl(Cancelled,0)) as FlightCancelledProbability"
							+ " from pastflightschedule p "
							+ " where p.ArrivalTime >= " + startTime[0] + " and p.DepartureTime <= " + endTime[0] 
							+ " and p.operatedby like "+ carrier + " and p.DepartedATAIRPORTID = " + src
							+ " and p.ARRIVEDATAIRPORTID = "+ dest;
			
			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);  
			while(rs.next()){
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
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
