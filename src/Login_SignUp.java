import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Login_SignUp {

    static Connection con;
    static Scanner sc = new Scanner(System.in);
    static Host host;
    static User users;
    Admin admin;

    public Login_SignUp() throws Exception {

        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airbnb", "root", "");
        host = new Host();
        users = new User();
        admin = new Admin();
    }

    public synchronized void hostlogin() {
        try {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("\t\t\t \033[1mHOST LOGIN\033[0m");
            System.out.println("-----------------------------------------------------------------------------------");

            boolean flag = true;
            while (flag) {
                System.out.print("\nHost User Name: ");
                String user = sc.nextLine();
                System.out.print("Password : ");
                String pass = sc.nextLine();

                String sql = "{call getUsername_Host(?,?,?,?)}";
                CallableStatement cst = con.prepareCall(sql);
                cst.setString(1, user);
                cst.setString(4, pass);
                cst.executeQuery();
                String username = cst.getString(2);
                String password = cst.getString(3);
                if (pass.equals(password) && user.equals(username)) {
                    System.out.println("Login Succesfull");
                    flag = false;

                    host.HostFunctions(username);

                } else {
                    System.out.println("Username Or Password may be incorrect");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public synchronized void hostSignUp() {

        try {

            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("\t\t\t \033[1mHOST SIGNUP\033[0m");
            System.out.println("-----------------------------------------------------------------------------------");
            host.addHost();
            System.out.print("\nSet Username : ");
            String hostname = sc.nextLine();
            System.out.print("Set Password : ");
            String password = sc.nextLine();

            String sql = "INSERT INTO host_login_details VALUES (null,?,?)";
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, hostname);
            pst.setString(2, password);

            System.out.println((pst.executeUpdate() > 0) ? "Host SignUp Succesfully" : "Error");

            hostlogin();
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public synchronized void userSignUp() throws Exception {

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("\t\t\t \033[1mUSER SIGNUP\033[0m");
        System.out.println("-----------------------------------------------------------------------------------");

        users.addUser();

        System.out.print("\nSet Your User Name : ");
        String username = sc.nextLine();
        System.out.print("\nSet Your Password : ");
        String password = sc.nextLine();

        String sql = "INSERT INTO user_login_details VALUES (null,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, password);

        System.out.println((pst.executeUpdate() > 0) ? "User SignUp Succesfully." : "Error");

        userlogin();

    }

    public synchronized void userlogin() throws Exception {

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("\t\t\t \033[1mUSER LOGIN\033[0m");
        System.out.println("-----------------------------------------------------------------------------------");

        boolean flag = true;
        while (flag) {
            System.out.print("\nUser Name: ");
            String user = sc.nextLine();
            System.out.print("Password : ");
            String pass = sc.nextLine();

            String sql = "{call getUsername_user(?,?,?,?,?)}";
            CallableStatement cst = con.prepareCall(sql);
            cst.setString(1, user);
            cst.setString(2, pass);
            cst.executeQuery();
            String username = cst.getString(3);
            String password = cst.getString(4);
            int userid = cst.getInt(5);
            if (pass.equals(password) && user.equals(username)) {
                System.out.println("Login Succesfull");
                flag = false;

                users.checkUser(username, userid);

            } else {
                System.out.println("Username Or Password may be incorrect");
            }
        }

    }

    public synchronized void adminlogin() {

        try {
            System.out.println("-----------------------------------------------------------------------------------");
            System.out.println("\t\t\t \033[1mADMIN LOGIN\033[0m");
            System.out.println("-----------------------------------------------------------------------------------");

            boolean flag = true;
            while (flag) {
                System.out.print("\nEnter The Admin Username : ");
                String username = sc.nextLine();
                System.out.print("Enter The Password : ");
                String password = sc.nextLine();
                String sql = "SELECT * FROM admin_login_details WHERE username = ? AND password = ?";
                PreparedStatement pst = con.prepareStatement(sql);
                pst.setString(1, username);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();
                String adminusername = rs.getString(1);
                String adminpassword = rs.getString(2);
                if (username.equals(adminusername) && password.equals(adminpassword)) {
                    System.out.println("Login Succesfull");
                    flag = false;

                    admin.AdminFunctions();

                } else {
                    System.out.println("Username Or Password may be incorrect");
                }
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

}