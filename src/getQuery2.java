

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

import com.oracle.jrockit.jfr.FlightRecorder;

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
    	query = "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, s1.OriginID, s1.destinationID, "
    			+ "s2.FlightNumber, s2.ArrivalTime, s2.DepartureTime,s2.OriginID, s2.destinationID,"
    			+ "s3.FlightNumber, s3.ArrivalTime, s3.DepartureTime, s3.OriginID, s3.destinationID,"
    			+ "s4.FlightNumber, s4.ArrivalTime, s4.DepartureTime, s4.OriginID, s4.destinationID,"
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
    			+ "and s1.FlightDate = to_date('"+ date +"', 'yyyy-mm-dd') "
    			+ "and s2.FlightDate = s1.FlightDate and s3.FlightDate = s1.FlightDate and s4.FlightDate = s1.FlightDate  "
    			+ "union "
    			+ "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, s1.OriginID, s1.destinationID, "
    			+ "s2.FlightNumber, s2.ArrivalTime, s2.DepartureTime, s2.OriginID, s2.destinationID,"
    			+ "s3.FlightNumber, s3.ArrivalTime, s3.DepartureTime, s3.OriginID, s3.destinationID,"
    			+ "Null, Null, Null, NULL, NULL,"
    			+ "s1.Distance+s2.distance+s3.distance as TotalDistance, s1.Cost+s2.cost+s3.cost as TotalCost "
    			+ "from SCHEDULEDFLIGHTS S1, SCHEDULEDFLIGHTS S2, SCHEDULEDFLIGHTS S3 "
    			+ "where s1.OriginID = " + start + " and s3.DestinationID = " + end + " "
    			+ "and s1.destinationID = s2.originID and s2.destinationID = s3.originID "
    			+ "and s1.ArrivalTime <= s2.DepartureTime  "
    			+ "and s2.ArrivalTime <= s3.DepartureTime "
    			+ "and s1.arrivaltime >= s1.departureTime and s2.ArrivalTime >= s2.DepartureTime and s3.ArrivalTime >= s3.DepartureTime  "
    			+ "and s1.FlightDate = to_date('"+ date +"', 'yyyy-mm-dd') and "
    			+ "s2.FlightDate = s1.FlightDate and s3.FlightDate = s1.FlightDate "
    			+ "union "
    			+ "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, s1.OriginID, s1.destinationID, "
    			+ "Null, Null, Null, Null, Null,"
    			+ "Null, Null, Null, Null, Null,"
    			+ "Null, Null, Null, Null, Null,"
    			+ "s1.Distance, s1.Cost as TotalCost  from SCHEDULEDFLIGHTS S1 "
    			+ "Where OriginID = " + start + "and DestinationID = " + end + " and "
    			+ "FlightDate = to_date('"+ date +"', 'yyyy-mm-dd') "
    			+ "union "
    			+ "Select s1.FlightNumber, s1.ArrivalTime, s1.DepartureTime, s1.OriginID, s1.destinationID,"
    			+ "s2.FlightNumber, s2.ArrivalTime, s2.DepartureTime, s2.OriginID, s2.destinationID,"
    			+ "Null, Null, Null, Null, Null,"
    			+ "Null, Null, Null,  Null, Null,"
    			+ "s1.Distance+s2.distance as TotalDistance, s1.Cost+s2.cost as TotalCost "
    			+ "from SCHEDULEDFLIGHTS S1, SCHEDULEDFLIGHTS S2 "
    			+ "where s1.OriginID = " + start + " and s2.DestinationID = " + end + " "
    			+ "and s1.destinationID = s2.originID "
    			+ "and "
    			+ "s1.ArrivalTime <= s2.DepartureTime and s1.arrivaltime >= s1.departureTime and s2.ArrivalTime >= s2.DepartureTime and "
    			+ "s1.FlightDate = to_date('"+ date +"', 'yyyy-mm-dd')"
    			+ "and s2.FlightDate = s1.FlightDate order by TotalCost asc" ;
    	return query;
    }


    String getOutputOne(ResultSet rs) throws SQLException{

    	String startHtml = "<!DOCTYPE html ><html lang=\"en\"><head>	<meta charset=\"utf-8\">	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">	<meta name=\"author\" content=\"Magdalena Myka\">	<title>Flight Search</title>	<!-- Latest compiled and minified CSS -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">	<!-- Optional theme -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css\">	<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css\">	<style>	body {	background:url('https://static.pexels.com/photos/8394/flight-sky-clouds-fly.jpg') no-repeat 0px 0px;	background-size: cover;	font-family: 'Open Sans', sans-serif;	background-attachment: fixed;    background-position: center;} h2{font-size:40px; color:black; background:white;} .panel-body{color:#1111e0;} </style></head><body>	<div class=\"container\">	<div> <h2 align=\"center\" style=\"color=black;\"><b> Flight suggestions	</b></h2> </div>	<div class=\"row\">			<div class=\"col-md-3\">				<!-- Refine price -->			</div>			<!-- Search results -->			<div class=\"col-sm-12 col-md-9\">				<!-- First row with header and dates -->				<div class=\"row\" id=\"results-header\">					<div class=\"col-md-6\">"
    			+ "	</div>	</div>	<!-- All results (these are sample static data)-->				<div class=\"row\">					<div class=\"col-md-8\" id=\"results\">";

    	String endHtml = "</div> <!-- End of search results -->				</div>			</div>		</div>	</div> <!-- end of container -->";
    	String res = "";

    	System.out.println("Before main while");
    	while(rs.next()){
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			int i=1;
			res = res + "<div class=\"panel panel-default\" style=\"opacity:0.7\"> "
					+ "<div class=\"panel-body\"> ";

			int a,b;
			//System.out.println("Before while loop");
			while(i<20){

				if (rs.getString(i) != null){

					a = Integer.parseInt(rs.getString(i+1));
					b = Integer.parseInt(rs.getString(i+2));
		    		res = res + "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
							+ "<ul class=\"list-unstyled\"> "
							+ "<li>Flight number: "+ rs.getString(i)+"</li> "
							+ "<li>From : "+ rs.getString(i+3)+"</li> "
							+ "<li>To: "+ rs.getString(i+4)+"</li> "
							+ "<li> Arrival Time "+ a/100+":"+ rs.getString(i+1).substring(rs.getString(i+1).length()-2) +"</li> "
							+ "<li> Departure Time "+ b/100+":"+ rs.getString(i+2).substring(rs.getString(i+2).length()-2)+"</li> "
							+ "</ul> "
							+ "</div> ";
				}
				i+=5;
			}
			res = res + "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li> Total cost "+ rs.getString(22)+"$</li> "
					+ "</ul> "
					+ "</div> ";
			res = res + "</div> " + "</div>";

		}
    	return startHtml+res+endHtml;
    }


    String getTableCode(ResultSet rs) throws NumberFormatException, SQLException{
    	String res = "";
    	while(rs.next()){
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			//d = Double.valueOf(rs.getString(1));
    		int i=1;
			res = res + "<div class=\"panel panel-default\" style=\"opacity:0.7\"> "
					+ "<div class=\"panel-body\"> ";

			int a,b;
			//System.out.println("Before while loop");
			while(i<20){

				if (rs.getString(i) != null){

					a = Integer.parseInt(rs.getString(i+1));
					b = Integer.parseInt(rs.getString(i+2));
		    		res = res + "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
							+ "<ul class=\"list-unstyled\"> "
							+ "<li>Flight number: "+ rs.getString(i)+"</li> "
							+ "<li>From : "+ rs.getString(i+3)+"</li> "
							+ "<li>To: "+ rs.getString(i+4)+"</li> "
							+ "<li> Arrival Time "+ a/100+":"+ rs.getString(i+1).substring(rs.getString(i+1).length()-2) +"</li> "
							+ "<li> Departure Time "+ b/100+":"+ rs.getString(i+2).substring(rs.getString(i+2).length()-2)+"</li> "
							+ "</ul> "
							+ "</div> ";
				}
				i+=5;
			}
			res = res + "<div class=\"col-sm-4 col-md-3 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li> Total cost "+ rs.getString(22)+"$</li> "
					+ "</ul> "
					+ "</div> ";
			res = res + "</div> " + "</div>";

		}
    	//System.out.println(resHtmlOne);
    	return res;
    }

    String getOutput(String resOne, String resTwo) throws NumberFormatException, SQLException{
    	String startHtml = "<!DOCTYPE html > <head> <!-- Latest compiled and minified CSS --> <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"> <!-- Optional theme --> <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"> <!-- Latest compiled and minified JavaScript --> <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script> </head> <style> body { background:url('https://static.pexels.com/photos/8394/flight-sky-clouds-fly.jpg') no-repeat 0px 0px; background-size: cover; font-family: 'Open Sans', sans-serif; background-attachment: fixed; background-position: center; } h2{font-size:40px; color:black; background:white;}  .panel-body{color:#1111e0; font-weight:bold;} </style> <body> <div class=\"container\" > <div> <h2 align=\"center\" style=\"color=black;\"><b> Round trip flight suggestions	</b></h2> </div><div class=\"row\"> <div class=\"col-md-6\">  <div class=\"table1\" align=\"center\">";

    	String midHtml = "</div> <!-- table 1 ends here --> </div> <div class=\"col-md-6\"> <div class=\"table2\" align=\"center\">";

    	String endHtml = "</div> <!-- table 2 ends here --> </div> </body> </html>";
    	return startHtml+resOne+midHtml+resTwo+endHtml;

    }


    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out = response.getWriter();
		String start, end, date, check;

		start = request.getParameter("source").split(" - ")[0];
		end = request.getParameter("destination").split(" - ")[0];
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
			String query = formQuery(start, end, date);
			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);

			if (check != null){
				System.out.println(check);
			}
			String queryTwo="";
			if (check != null && check.equals("on")){
				String oneHtml = getTableCode(rs);
				String dateTwo = request.getParameter("returnDate");
				queryTwo = formQuery(end, start, dateTwo);
				ResultSet rsTwo=stmt.executeQuery(queryTwo);
				out.println(getOutput(oneHtml, getTableCode(rsTwo)));
				System.out.println(query);
				rsTwo=stmt.executeQuery(queryTwo);
			}
			else{
				System.out.println("Before out");

				out.println(getOutputOne(rs));

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
