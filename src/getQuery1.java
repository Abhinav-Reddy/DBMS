

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query1Result
 */
@WebServlet("/getQuery1")
public class getQuery1 extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public getQuery1() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */

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


    String getOutput(ResultSet rs) throws SQLException{
    	String startHtml = "<!DOCTYPE html ><html lang=\"en\"><head>	<meta charset=\"utf-8\">	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">	<meta name=\"author\" content=\"Magdalena Myka\">	<title>Flight Search</title>	<!-- Latest compiled and minified CSS -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">	<!-- Optional theme -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css\">	<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css\">	<style>	body {	background:url('http://www.beautiful-views.net/views/clouds-plane-sky-flight.jpg') no-repeat 0px 0px;	background-size: cover;	font-family: 'Open Sans', sans-serif;	background-attachment: fixed;    background-position: center;}</style></head><body>	<div class=\"container\">		<div class=\"row\">			<div class=\"col-md-3\">				<!-- Refine price -->			</div>			<!-- Search results -->			<div class=\"col-sm-12 col-md-9\">				<!-- First row with header and dates -->				<div class=\"row\" id=\"results-header\">					<div class=\"col-md-6\">"
    			+ "	</div>	</div>	<!-- All results (these are sample static data)-->				<div class=\"row\">					<div class=\"col-md-8\" id=\"results\">";

    	String endHtml = "</div> <!-- End of search results -->				</div>			</div>		</div>	</div> <!-- end of container -->";
    	String res = "<div class=\"panel panel-default\" style=\"opacity:0.7\"> "
				+ "<div class=\"panel-body\"> "
				+ "<div class=\"col-sm-2 col-md-12 vertically-centered\"> "
				+ "<ul class=\"list-unstyled\"> "
				+ "<li>"+ "<b>Average delay of the flight </b> </li> "
				+ "</ul> "
				+ "</div> "
				+ "</div> "
				+ "</div>";
    	Double d;
    	DecimalFormat numberFormat = new DecimalFormat("#0.00");
    	while(rs.next()){
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			//d = Double.valueOf(rs.getString(1));
    		if (rs.getString(1) == null)
    			d=0.0;
    		else
    			d = Double.valueOf(rs.getString(1));
    		res = res + "<div class=\"panel panel-default\" style=\"opacity:0.7\"> "
					+ "<div class=\"panel-body\"> "
					+ "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li>"+ numberFormat.format(d) +" Minutes</li> "
					+ "</ul> "
					+ "</div> "
					+ "</div> "
					+ "</div>";
		}
    	return startHtml+res+endHtml;
    }


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
			String src = request.getParameter("source").split(" - ")[0];
			String dest = request.getParameter("destination").split(" - ")[0];
			String carrier = request.getParameter("carrier").split(" - ")[0];
			String[] startTime = request.getParameter("startTime").split(":");
			String[] endTime = request.getParameter("endTime").split(":");

			int sTime[], eTime[];
			sTime = new int[2];
			eTime = new int[2];
			sTime[0] = Integer.parseInt(startTime[0]);
			sTime[1] = Integer.parseInt(startTime[1]);

			eTime[0] = Integer.parseInt(endTime[0]);
			eTime[1] = Integer.parseInt(endTime[1]);

			int diff = (eTime[0] - sTime[0])*60 + (eTime[1] - sTime[1]);
			diff = diff / 2;
			addTime(sTime, diff);
			addTime(eTime, diff);

			endTime[0] = Integer.toString(eTime[0]) + Integer.toString(eTime[1]);

			startTime[0] = Integer.toString(sTime[0]) + Integer.toString(sTime[1]);

			//step4 execute query
			String query = "Select "
			+ "avg(nvl(p.DepartureDelay,0)+ nvl(p.ArrivalDelay,0)+ nvl(p.CarrierDelay,0)+"
			+ "nvl(p.WeatherDelay,0)+ nvl(p.NASDelay,0)+ nvl(p.SecurityDelay,0)+"
			+ "nvl(p.LateAircraftDelay,0)+ nvl(p.DiversionArrivalDelay,0)) as TotalDelay "
			+ "from pastflightschedule p Where "
			+ " p.operatedBy like '" + carrier +"' and p.ArrivalTime >= " + startTime[0] + " And p.DepartureTime <= "+ endTime[0]
			+ " and p.DepartedATAIRPORTID = " + src + " and p.ARRIVEDATAIRPORTID = " + dest;

			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			out.println(getOutput(rs));

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
