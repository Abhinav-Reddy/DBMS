

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

import java.sql.*;
/**
 * Servlet implementation class Add
 */
@WebServlet("/Add")
public class Add extends HttpServlet {
	private static final long serialVersionUID = 1L;

	int i=0;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Add() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String str = request.getParameter("test");
		PrintWriter out = response.getWriter();
		System.out.println(str);
		// TODO Auto-generated method stub

		try{
			//step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

			//step2 create  the connection object
			Connection con=DriverManager.getConnection(
					"jdbc:oracle:thin:@oracle.cise.ufl.edu:1521:orcl","thota", "jayakrishna");

			//step3 create the statement object
			Statement stmt=con.createStatement();

			//step4 execute query
			String query = "select * from airport";
			ResultSet rs=stmt.executeQuery(query);
			while(rs.next()){
			//System.out.println(rs.getString(2)+" "+rs.getString(3)+" "+rs.getString(4)+"\n");
				out.println("<option value = \""+rs.getString(1)+" - "+rs.getString(2)+"\">");
				System.out.println("<option value = \""+rs.getString(1)+" - "+rs.getString(2)+"\">");

			}

			System.out.println("Done");
			//step5 close the connection object
			con.close();

			}catch(Exception e){ System.out.println(e);}

		i++;

		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		int one = Integer.parseInt(request.getParameter("t1"));
		int two = Integer.parseInt(request.getParameter("t2"));
		PrintWriter out = response.getWriter();
		out.println(one+two);
	}

}
