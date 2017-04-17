

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
 * Servlet implementation class Query5Result
 */
@WebServlet("/getQuery5")
public class getQuery5 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery5() {
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
			String query = "Select * from " +
							"(Select DEPARTEDATAIRPORTID, ARRIVEDATAIRPORTID , avg(nvl(NASdelay,0))as AverageDelay "
							+ "from PASTFLIGHTSCHEDULE p "
							+ "Where "
							+ "FlightDate >= to_date('"+ start +"', 'yyyy-mm-dd')" + " and "
							+ "FlightDate <= to_date('"+ end +"', 'yyyy-mm-dd')" + " "
							//+ "FlightDate >= '01-01-17' and FlightDate <= '31-01-17' "
							+ "group by DEPARTEDATAIRPORTID, ARRIVEDATAIRPORTID "
							+ "Order by AverageDelay desc) "
							+ "Where rownum < 10+1 ";
			System.out.println(query);

			ResultSet rs=stmt.executeQuery(query);


			String queryTwo = "Select * from " +
					"(Select DEPARTEDATAIRPORTID, ARRIVEDATAIRPORTID , avg(nvl(NASdelay,0))as AverageDelay "
					+ "from PASTFLIGHTSCHEDULE p "
					+ "Where "
					+ "FlightDate >= to_date('"+ start +"', 'yyyy-mm-dd')" + " and "
					+ "FlightDate <= to_date('"+ end +"', 'yyyy-mm-dd')" + " "
					//+ "FlightDate >= '01-01-17' and FlightDate <= '31-01-17' "
					+ "group by DEPARTEDATAIRPORTID, ARRIVEDATAIRPORTID "
					+ "Order by AverageDelay asc) "
					+ "Where rownum < 10+1 ";
			System.out.println(queryTwo);
			ResultSet rsTwo=stmt.executeQuery(queryTwo);

			out.println(getOutput(rs, rsTwo));
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
