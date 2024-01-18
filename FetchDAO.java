package com.uniq.projectCRUD2;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.management.remote.rmi.RMIConnectorServer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MyServlet")
public class FetchDAO extends HttpServlet {
	
  @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	
	   PrintWriter outPrintWriter= resp.getWriter();
	   //dummy changes 

	   try {
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection=DriverManager.getConnection("jdbc:mysql://localhost:3306/secondproject","root", "asrabali");
		PreparedStatement pStatement=connection.prepareStatement("SELECT * FROM primarytable");
		ResultSet rSet= pStatement.executeQuery();
		
		List <PrimaryTablePOJO>primaryObjectList = new ArrayList<>();
		List <SecondaryTablePOJO> secondryObjectList= new ArrayList<>();
	    List <MergedPOJO> mergedList= new ArrayList<>();
		
     while (rSet.next()) {
    	PrimaryTablePOJO primaryobject= new PrimaryTablePOJO();
    	primaryobject.setMobile(rSet.getString(1));
    	primaryobject.setAddress(rSet.getString(2));
    	primaryObjectList.add(primaryobject);
          
	}
     PreparedStatement pStatement2 = connection.prepareStatement("SELECT * FROM secondarytable");
     ResultSet rSet2= pStatement2.executeQuery();
     while(rSet2.next()) {
    	 
    	 SecondaryTablePOJO secondaryObject= new SecondaryTablePOJO();
    	 secondaryObject.setMobile(rSet2.getString(1));
    	 secondaryObject.setUsername(rSet2.getString(2));
    	 secondryObjectList.add(secondaryObject);
     }    
     for (PrimaryTablePOJO primary : primaryObjectList) {
    	 for (SecondaryTablePOJO secondary : secondryObjectList) {
    	      if (primary.getMobile().equals(secondary.getMobile())) {
    	    	 MergedPOJO merged= new MergedPOJO();
    	    	 merged.setAddress(primary.getAddress());
    	    	 merged.setMobile1(primary.getMobile());
    	    	 merged.setMobile2(secondary.getMobile());
    	    	 merged.setUsername(secondary.getUsername());
    	    	 mergedList.add(merged);
    	    	 break;	
			}
			
		}
		
	}
     req.setAttribute("mergedList",mergedList);
     RequestDispatcher rsDispatcher= req.getRequestDispatcher("/view.jsp");
    rsDispatcher.forward(req, resp);
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  	     
}

}
