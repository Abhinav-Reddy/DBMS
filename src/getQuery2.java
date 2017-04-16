

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
 * Servlet implementation class Query2Result
 */
@WebServlet("/getQuery2")
public class getQuery2 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery2() {
        super();
        // TODO Auto-generated constructor stub
    }

    String formQuery(String start, String end, String date){
    	String query;
    	query = "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, "
    			+ "s2.FlightNumber, s2.ArrivalTime, s2.DepartureTime,"
    			+ "s3.FlightNumber, s3.ArrivalTime, s3.DepartureTime, "
    			+ "s4.FlightNumber, s4.ArrivalTime, s4.DepartureTime, "
    			+ "s1.Distance+s2.distance+s3.distance+s4.distance as TotalDistance, "
    			+ "s1.Cost+s2.cost+s3.cost+s4.cost as TotalCost "
    			+ "from SCHEDULEDFLIGHTS S1, SCHEDULEDFLIGHTS S2, SCHEDULEDFLIGHTS S3, SCHEDULEDFLIGHTS S4 "
    			+ "where s1.OriginID = " + start + " and s4.DestinationID = " + end + " "
    			+ "and s1.destinationID = s2.originID "
    			+ "and s2.destinationID = s3.originID "
    			+ "and s3.destinationID = s4.originID "
    			+ "and s1.ArrivalTime <= s2.DepartureTime  "
    			+ "and s2.ArrivalTime <= s3.DepartureTime "
    			+ "and s3.ArrivalTime <= s4.DepartureTime "
    			+ "and s1.arrivaltime >= s1.departureTime and s2.ArrivalTime >= s2.DepartureTime and s3.ArrivalTime >= s3.DepartureTime and s4.ArrivalTime >= s4.DepartureTime "
    			+ "and s1.FlightDate = to_date('"+ start +"', 'yyyy-mm-dd') "
    			+ "and s2.FlightDate = s1.FlightDate and s3.FlightDate = s1.FlightDate and s4.FlightDate = s1.FlightDate  "
    			+ "union "
    			+ "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime,  "
    			+ "s2.FlightNumber, s2.ArrivalTime, s2.DepartureTime, "
    			+ "s3.FlightNumber, s3.ArrivalTime, s3.DepartureTime, Null, Null, Null, "
    			+ "s1.Distance+s2.distance+s3.distance as TotalDistance, s1.Cost+s2.cost+s3.cost as TotalCost "
    			+ "from SCHEDULEDFLIGHTS S1, SCHEDULEDFLIGHTS S2, SCHEDULEDFLIGHTS S3 "
    			+ "where s1.OriginID = " + start + " and s3.DestinationID = " + end + " "
    			+ "and s1.destinationID = s2.originID and s2.destinationID = s3.originID "
    			+ "and s1.ArrivalTime <= s2.DepartureTime  "
    			+ "and s2.ArrivalTime <= s3.DepartureTime "
    			+ "and s1.arrivaltime >= s1.departureTime and s2.ArrivalTime >= s2.DepartureTime and s3.ArrivalTime >= s3.DepartureTime  "
    			+ "and s1.FlightDate = to_date('"+ start +"', 'yyyy-mm-dd') and "
    			+ "s2.FlightDate = s1.FlightDate and s3.FlightDate = s1.FlightDate "
    			+ "union "
    			+ "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, "
    			+ "Null, Null, Null, Null, Null, Null, Null, Null, Null, "
    			+ "s1.Distance, s1.Cost as TotalCost  from SCHEDULEDFLIGHTS S1 "
    			+ "Where OriginID = 11433 and DestinationID = 12889  and "
    			+ "FlightDate = '01-01-17' "
    			+ "union "
    			+ "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, "
    			+ "s2.FlightNumber, s2.ArrivalTime, s2.DepartureTime, "
    			+ "Null, Null, Null, "
    			+ "Null, Null, Null,  "
    			+ "s1.Distance+s2.distance as TotalDistance, s1.Cost+s2.cost as TotalCost "
    			+ "from SCHEDULEDFLIGHTS S1, SCHEDULEDFLIGHTS S2 "
    			+ "where s1.OriginID = " + start + " and s3.DestinationID = " + end + " "
    			+ "and s1.destinationID = s2.originID "
    			+ "and "
    			+ "s1.ArrivalTime <= s2.DepartureTime and s1.arrivaltime >= s1.departureTime and s2.ArrivalTime >= s2.DepartureTime and "
    			+ "s1.FlightDate = and s1.FlightDate = to_date('"+ start +"', 'yyyy-mm-dd')"
    			+ "and s2.FlightDate = s1.FlightDate "
    			+ "Order by TotalCost asc";
    	return query;
    }
    
    
    

    String formQueryTwo(String start, String end, String todate, String fromDate){
    	String query;
    	query = "";
    	return query;
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String start, end, date, check;
		
		start = request.getParameter("source");
		end = request.getParameter("destination");
		date = request.getParameter("date");
		check = request.getParameter("roundTrip");
		
		
		
		try{
			//step1 load the driver class  
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			  
			//step2 create  the connection object  
			Connection con=DriverManager.getConnection(  
					"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");  
			  
			//step3 create the statement object  
			Statement stmt=con.createStatement();  
			  
			//step4 execute query
						
			if (check.equals("true")){
				String dateTwo = request.getParameter("returnDate");
				String query = formQueryTwo(start, end, date, dateTwo);
				System.out.println(query);
				ResultSet rs=stmt.executeQuery(query);  
				while(rs.next()){
				//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
					out.println("\""+rs.getString(1)+"\" : { \" n \" : \""+rs.getString(2)+" \" },");
					out.println("<br>");
				}
				

			}
			else{
				String query = formQuery(start, end, date);
				System.out.println(query);
				ResultSet rs=stmt.executeQuery(query);  
				while(rs.next()){
				//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
					out.println("\""+rs.getString(1)+"\" : { \" n \" : \""+rs.getString(2)+" \" },");
					out.println("<br>");
				}
				

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
