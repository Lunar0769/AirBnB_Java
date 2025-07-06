import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Admin {
    static Scanner sc = new Scanner(System.in);
    static Connection con;

    public Admin() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airbnb", "root", "");
    }

    void AdminFunctions() {

        try {

            System.out
                    .println("-----------------------------------------------------------------------------------");
            System.out.println("\t\t\t \033[1mADMIN FUNCTIONALITY\033[0m");
            System.out
                    .println("-----------------------------------------------------------------------------------");
            System.out.println("1. List Of All Host");
            System.out.println("2. Remove Host From Website");
            System.out.println("3. Remove A Particular Airbnb");
            System.out.println("4. Exit");
            System.out.print("\nEnter Your Choice : ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("\033[1mList Of All Host\033[0m");
                    view_host();
                }
                case 2 -> remove_host();
                case 3 -> remove_airbnb();
                case 4 -> System.out.println("Thank You....");
                default -> System.out.println("Select The Valid Choice");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    void view_host() {

        try {
            String sql = "SELECT Host_username,Airbnb_name FROM airbnb_details INNER JOIN host_airbnb_details ON host_airbnb_details.Airbnb_id= airbnb_details.Airbnb_id";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                System.out.println("----------------------------------------------------");
                System.out.println("HOST NAME : " + rs.getString(1));
                System.out.println("AIRBNB NAME : " + rs.getString(2));
                System.out.println("----------------------------------------------------");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    void remove_host() {

        try {
            System.out.print("Enter Host Username : ");
            String host = sc.next();
            String sql1 = "DELETE FROM host_airbnb_details WHERE Host_username = '" + host + "'";
            String sql2 = "DELETE FROM airbnb_details WHERE Airbnb_Owner='" + host + "'";
            Statement st = con.createStatement();

            System.out.println(
                    ((st.executeUpdate(sql1)) > 0 && (st.executeUpdate(sql2) > 0)) ? "DELETION COMPLETED." : "ERROR");

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    void remove_airbnb() {

        try {
            System.out.print("Enter Airbnb Name : ");
            String airbnb = sc.next();

            String sql1 = "DELETE FROM airbnb_details WHERE Airbnb_name = '" + airbnb + "'";
            String sql2 = "SELECT Airbnb_id FROM airbnb_details WHERE Airbnb_name='" + airbnb + "'";

            Statement st = con.createStatement();

            ResultSet rs = st.executeQuery(sql2);
            int id = rs.getInt(1);

            String sql3 = "DELETE FROM host_airbnb_details WHERE Airbnb_id=" + id;
            System.out.println(
                    ((st.executeUpdate(sql1)) > 0 && (st.executeUpdate(sql3) > 0)) ? "DELETION OF AIRBNB COMPLETED."
                            : "ERROR");

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
