

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


    String getTableCode(ResultSet resOne) throws NumberFormatException, SQLException{
    	String resHtmlOne = "";
    	Double d;
    	DecimalFormat numberFormat = new DecimalFormat("#0.00");
    	while(resOne.next()){
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
			//d = Double.valueOf(rs.getString(1));
    		d = Double.valueOf(resOne.getString(3));
    		resHtmlOne = resHtmlOne + "<div class=\"panel panel-default\" style=\"opacity: 0.7;\"> "
        			+ "<div class=\"panel-body\"> "
        			+ "<div class=\"col-sm-3 col-md-6 vertically-centered\"> "
        			+ "<ul class=\"list-unstyled\" > "
        			+ "<li>"+ resOne.getString(1) +" </li> "
        			+ "</ul> "
        			+ "</div> "
        			+ "<div class=\"col-sm-3 col-md-6 vertically-centered\"> "
        			+ "<ul class=\"list-unstyled\">"
        			+ "<li>"+ numberFormat.format(d) +" Minutes</li> "
        			+ "</ul> "
        			+ "</div> </div> </div> <!-- table1 result2 ends here-->";

		}
    	//System.out.println(resHtmlOne);
    	return resHtmlOne;
    }

    String getOutput(String resOne, String resTwo, String startDate, String endDate) throws NumberFormatException, SQLException{
    	String startHtml = "<!DOCTYPE html > <head> <!-- Latest compiled and minified CSS --> <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\" integrity=\"sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u\" crossorigin=\"anonymous\"> <!-- Optional theme --> <link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\" integrity=\"sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp\" crossorigin=\"anonymous\"> <!-- Latest compiled and minified JavaScript --> <script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\" integrity=\"sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa\" crossorigin=\"anonymous\"></script> </head> <style> body { background:url('http://www.beautiful-views.net/views/clouds-plane-sky-flight.jpg') no-repeat 0px 0px; background-size: cover; font-family: 'Open Sans', sans-serif; background-attachment: fixed; background-position: center; } </style> <body> <div class=\"container\" > <div class=\"row\"> <div class=\"col-md-6\"> <h2 align=\"center\" style=\"color=black;\"><b>Top 10 carriers with highest delay	</b></h2> <div class=\"table1\" align=\"center\">";

    	String midHtml = "</div> <!-- table 1 ends here --> </div> <div class=\"col-md-6\"> <h2 align=\"center\" style=\"color=black;\"> <b>Top 10 carriers with lowest delay </b></h2> <div class=\"table2\" align=\"center\">";

    	String endHtml = "</div> <!-- table 2 ends here --> </div> </body> </html>";
    	return startHtml+resOne+midHtml+resTwo+endHtml;

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
			+ "(Select CarrierName, UniqueCarrierCode, avg(nvl(LateAircraftDelay,0)+nvl(CarrierDelay,0)) as TotalDelay"
			+ " from PASTFLIGHTSCHEDULE p, CARRIER c "
			+ " Where p.OperatedBy = c.UniquecarrierCode and "
			+ " FlightDate  >= to_date('"+ start+"', 'yyyy-mm-dd') and FlightDate  <= to_date('"+ end +"', 'yyyy-mm-dd') "
			+ "Group by UniqueCarrierCode, CarrierName "
			+ "Order by TotalDelay desc) "
			+ "Where rownum < 10+1";

			System.out.println(query);
			ResultSet rs=stmt.executeQuery(query);
			String oneHtml = getTableCode(rs);


			String queryTwo = "Select * from"
			+ "(Select CarrierName, UniqueCarrierCode, avg(nvl(LateAircraftDelay,0)+nvl(CarrierDelay,0)) as TotalDelay"
			+ " from PASTFLIGHTSCHEDULE p, CARRIER c "
			+ " Where p.OperatedBy = c.UniquecarrierCode and "
			+ " FlightDate  >= to_date('"+ start+"', 'yyyy-mm-dd') and FlightDate  <= to_date('"+ end +"', 'yyyy-mm-dd') "
			+ "Group by UniqueCarrierCode, CarrierName "
			+ "Order by TotalDelay asc) "
			+ "Where rownum < 10+1";

			System.out.println(queryTwo);
			ResultSet rsTwo=stmt.executeQuery(queryTwo);
			out.println(getOutput(oneHtml, getTableCode(rsTwo), start, end));


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
