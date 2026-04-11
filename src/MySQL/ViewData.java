/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MySQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Lenovo
 */
public class ViewData {
    
    public static Connection con;
    public static Statement stm;
    public static ResultSet rs;
    public static void main(String args[]){
        try {
          
            String url ="jdbc:mysql://localhost/mhs";
            String user="root";
            String pass="";
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,user,pass);
            stm = con.createStatement();
          
            
            // INSERT a record
            String sqlInsert = "insert into datamhs values ('02291', 'Unpam', '3', 'C')";
            System.out.println("The SQL statement is: " + sqlInsert + "\n");  // Echo for debugging
            int countInserted;
            countInserted = stm.executeUpdate(sqlInsert);
            System.out.println(countInserted + " records inserted.\n");
         
            // Update a record 
            String strUpdate;
            strUpdate = "update datamhs set nama = 'Sonasa', kelas = 'Reg B' where nim = '02289'";
            System.out.println("The SQL statement is: " + strUpdate + "\n");  // Echo for debugging
            int countUpdated = stm.executeUpdate(strUpdate);
            System.out.println(countUpdated + " records affected.\n");
            
            // buat query ke database
            String sql = "SELECT * FROM datamhs";
            
            // eksekusi query dan simpan hasilnya di obj ResultSet
            rs = stm.executeQuery(sql);
            
            // tampilkan hasil query
            while(rs.next()){
                System.out.println("NIM: " + rs.getInt("nim"));
                System.out.println("Nama: " + rs.getString("nama"));
                System.out.println("Semester: " + rs.getInt("semester"));
                System.out.println("Kelas: " + rs.getString("kelas"));
            }
            
            stm.close();
            con.close();
            
            
        
        }   
        
        catch (ClassNotFoundException | SQLException e) {
            //e.printStackTrace();
            System.err.println("koneksi gagal" +e.getMessage());
        }
    }   
}
