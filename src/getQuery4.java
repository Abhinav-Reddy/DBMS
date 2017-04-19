

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


    String getOutput(ResultSet rs) throws SQLException{
    	String startHtml = "<!DOCTYPE html ><html lang=\"en\"><head>	<meta charset=\"utf-8\">	<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">	<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">	<meta name=\"author\" content=\"Magdalena Myka\">	<title>Flight Search</title>	<!-- Latest compiled and minified CSS -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css\">	<!-- Optional theme -->	<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap-theme.min.css\">	<link rel=\"stylesheet\" href=\"http://code.jquery.com/ui/1.11.2/themes/smoothness/jquery-ui.css\">	<style>	body {	background:url('http://www.beautiful-views.net/views/clouds-plane-sky-flight.jpg') no-repeat 0px 0px;	background-size: cover;	font-family: 'Open Sans', sans-serif;	background-attachment: fixed;    background-position: center;}</style></head><body>	<div class=\"container\">		<div class=\"row\">			<div class=\"col-md-3\">				<!-- Refine price -->			</div>			<!-- Search results -->			<div class=\"col-sm-12 col-md-9\">				<!-- First row with header and dates -->				<div class=\"row\" id=\"results-header\">					<div class=\"col-md-6\">"
    			+ "	</div>	</div>	<!-- All results (these are sample static data)-->				<div class=\"row\">					<div class=\"col-md-8\" id=\"results\">";

    	String endHtml = "</div> <!-- End of search results -->				</div>			</div>		</div>	</div> <!-- end of container -->";
    	String res = "<div class=\"panel panel-default\"> "
				+ "<div class=\"panel-body\"> "
				+ "<div class=\"col-sm-2 col-md-12 vertically-centered\"> "
				+ "<ul class=\"list-unstyled\"> "
				+ "<li>"+ "<b>Probability of flight getting cancelled"+" </b> </li> "
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

    		res = res + "<div class=\"panel panel-default\"> "
					+ "<div class=\"panel-body\"> "
					+ "<div class=\"col-sm-4 col-md-6 vertically-centered\"> "
					+ "<ul class=\"list-unstyled\"> "
					+ "<li> Probability: "+ numberFormat.format(d) +" </li> "
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



			String query = "Select 100*avg(nvl(Cancelled,0)) as FlightCancelledProbability"
							+ " from pastflightschedule p "
							+ " where p.operatedby like '"+ carrier + "' and p.DepartedATAIRPORTID = " + src
							+ " and p.ARRIVEDATAIRPORTID = "+ dest;

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
