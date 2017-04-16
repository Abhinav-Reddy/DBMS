

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
 * Servlet implementation class Query7Result
 */
@WebServlet("/getQuery7")
public class getQuery7 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery7() {
        super();
        // TODO Auto-generated constructor stub
    }

    void addTime(int sTime[], int diff){
    	sTime[1] += diff%60;
		
		if (sTime[0] + diff/60 + sTime[1]/60 > 24){
			sTime[0] = 23;
			sTime[1] = 59;
		}
		else{
			sTime[0] += diff/60 + sTime[1]/60;
			sTime[1] = sTime[1]%60;
		}
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
			
			int sTime[], eTime[];
			sTime = new int[2];
			eTime = new int[2];
			sTime[0] = Integer.getInteger(startTime[0]);
			sTime[1] = Integer.getInteger(startTime[1]);
			
			eTime[0] = Integer.getInteger(endTime[0]);
			eTime[1] = Integer.getInteger(endTime[1]);
			
			int diff = (eTime[0] - sTime[0])*60 + (eTime[1] - sTime[1]);
			diff = diff / 2;
			addTime(sTime, diff);
			addTime(eTime, diff);
			
			endTime[0] = Integer.toString(eTime[0]) + Integer.toString(eTime[1]);
			
			startTime[0] = Integer.toString(sTime[0]) + Integer.toString(sTime[1]);
			
			String query = "Select 100*avg(nvl(diverted,0)) as FlightReroutingProbability, "
							+ " avg(p.DIVERSIONARRIVALDELAY) as AdditionalTime  "
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
