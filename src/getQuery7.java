

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


    String getOutput(ResultSet rs) throws SQLException{
    	String startHtml = "<!DOCTYPE html ><html lang=\"en\"><head>	<meta charset=\"utf-8\">	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">	<meta name=\"author\" content=\"Magdalena Myka\">	<title>Flight Search</title>	<!-- Latest compiled and minified CSS -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">	<!-- Optional theme -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css\">	<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css\">	<style>	body {	background:url('http://www.beautiful-views.net/views/clouds-plane-sky-flight.jpg') no-repeat 0px 0px;	background-size: cover;	font-family: 'Open Sans', sans-serif;	background-attachment: fixed;    background-position: center;}</style></head><body>	<div class=\"container\">		<div class=\"row\">			<div class=\"col-md-3\">				<!-- Refine price -->			</div>			<!-- Search results -->			<div class=\"col-sm-12 col-md-9\">				<!-- First row with header and dates -->				<div class=\"row\" id=\"results-header\">					<div class=\"col-md-6\">"
    			+ "	</div>	</div>	<!-- All results (these are sample static data)-->				<div class=\"row\">					<div class=\"col-md-8\" id=\"results\">";

    	String endHtml = "</div> <!-- End of search results -->				</div>			</div>		</div>	</div> <!-- end of container -->";
    	String res = "<div class=\"panel panel-default\"> "
				+ "<div class=\"panel-body\"> "
				+ "<div class=\"col-sm-2 col-md-12 vertically-centered\"> "
				+ "<ul class=\"list-unstyled\"> "
				+ "<li>"+ "<b>Probability of flight getting rerouted"+" </b> </li> "
				+ "</ul> "
				+ "</div> "
				+ "</div> "
				+ "</div>";
    	Double d;
    	DecimalFormat numberFormat = new DecimalFormat("#0.00");
    	while(rs.next()){
    		//d = Double.valueOf(rs.getString(1));
			if (rs.getString(1) == null)
				d = 0.0;
			else
				d = Double.parseDouble(rs.getString(1));
    		Double d1;
    		if (rs.getString(2) == null)
    			d1 = 0.0;
    		else
    			d1 = Double.parseDouble(rs.getString(2));
    		res = res + "<div class=\"panel panel-default\"> "
					+ "<div class=\"panel-body\"> "
					+ "<div class=\"col-sm-4 col-md-6 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li> Probability"+ numberFormat.format(d) +" </li> "
					+ "</ul> "
					+ "</div> "
					+ "<div class=\"col-sm-4 col-md-6 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li> Expected rerouting Delay: "+ numberFormat.format(d1) +" Minutes</li> "
					+ "</ul> "
					+ "</div> "
					+ "</div> "
					+ "</div>";
		}
    	return startHtml+res+endHtml;
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
			String src = request.getParameter("source").split(" - ")[0];
			String dest = request.getParameter("destination").split(" - ")[0];
			String carrier = request.getParameter("carrier").split(" - ")[0];
			String[] startTime = request.getParameter("startTime").split(":");
			String[] endTime = request.getParameter("endTime").split(":");


			int sTime[], eTime[];
			sTime = new int[2];
			eTime = new int[2];
			//System.out.println("Before stime");

			//System.out.println(startTime[0]);
			Integer tmp = Integer.parseInt(startTime[0]);
			System.out.println(tmp);
			sTime[0] = (int)tmp;

			tmp = Integer.parseInt(startTime[1]);
			sTime[1] = (int)tmp;

			eTime[0] = Integer.parseInt(endTime[0]);
			eTime[1] = Integer.parseInt(endTime[1]);

			int diff = (eTime[0] - sTime[0])*60 + (eTime[1] - sTime[1]);
			diff = diff / 2;

			addTime(sTime, diff);
			addTime(eTime, diff);

			endTime[0] = Integer.toString(eTime[0]) + Integer.toString(eTime[1]);

			startTime[0] = Integer.toString(sTime[0]) + Integer.toString(sTime[1]);

			System.out.println("Before query");
			String query = "Select 100*avg(nvl(diverted,0)) as FlightReroutingProbability, "
							+ " avg(p.DIVERSIONARRIVALDELAY) as AdditionalTime  "
							+ " from pastflightschedule p "
							+ " where p.ArrivalTime >= " + startTime[0] + " and p.DepartureTime <= " + endTime[0]
							+ " and p.operatedby like '"+ carrier + "' and p.DepartedATAIRPORTID = " + src
							+ " and p.ARRIVEDATAIRPORTID = "+ dest;

			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);

			System.out.println("Done");
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
