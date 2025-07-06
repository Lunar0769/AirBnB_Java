import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Host {
    static Scanner sc = new Scanner(System.in);
    static HashMap<Integer, Airbnbdetails> airbnb = new HashMap<>();
    static Connection airbnbcon;
    static User users;

    int hostid;
    String hostname;
    String hostgender;
    String hostage;
    String hostmail;
    String hostphnno;
    String hostgovid;

    public Host(int hostid, String hostname, String hostgender, String hostage, String hostmail, String hostphnno,
            String hostgovid) {
        this.hostid = hostid;
        this.hostname = hostname;
        this.hostgender = hostgender;
        this.hostage = hostage;
        this.hostmail = hostmail;
        this.hostphnno = hostphnno;
        this.hostgovid = hostgovid;
    }

    public Host() throws Exception {
        airbnbcon = DriverManager.getConnection("jdbc:mysql://localhost:3306/airbnb", "root", "");
        users = new User("");
    }

    void addHost() throws Exception {

        try {

            System.out.print("Enter Your Name : ");
            String name = sc.next();
            System.out.print("Enter Your Gender ");
            String gender = sc.next();
            System.out.print("Enter Your Age : ");
            String age = sc.next();
            System.out.print("Enter Your mail Address : ");
            String mail = sc.next();
            System.out.print("Enter Your Phone No : ");
            String phnno = sc.next();
            System.out.print("Enter Your Governmnent Id (Passport/Driver's Lic) : ");
            String govid = sc.next();

            String sql = "INSERT INTO host_details VALUES (null,?,?,?,?,?,?)";
            PreparedStatement pstmt = airbnbcon.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, gender);
            pstmt.setString(3, age);
            pstmt.setString(4, mail);
            pstmt.setString(5, phnno);
            pstmt.setString(6, govid);

            pstmt.executeUpdate();

            Host data = new Host(hostid, name, gender, age, mail, phnno, govid);
            add_linkedlist(data);

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    // DATA STRUCTURE
    class Node {
        Host data;
        Node next;

        public Node(Host data) {
            this.data = data;
        }
    }

    Node head;

    void add_linkedlist(Host data) {
        Node n = new Node(data);
        if (head == null) {
            head = n;
        } else {
            Node last = head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = n;
        }
    }

    void printHostLinkedList() {
        Node temp = head;
        while (temp != null) {
            System.out.println(temp.data);
            temp = temp.next;
        }
    }

    void HostFunctions(String username) throws Exception {
        int choice;

        try {

            do {

                System.out
                        .println("-----------------------------------------------------------------------------------");
                System.out.println("\t\t\t HOST FUNCTIONALITY");
                System.out
                        .println("-----------------------------------------------------------------------------------");

                System.out.println("\n1. Add New Airbnb.");
                System.out.println("2. Update The Airbnb Details.");
                System.out.println("3. Remove Existing Airbnb.");
                System.out.println("4. View Available Airbnb.");
                System.out.println("5. List All The Airbnbs");
                System.out.println("6. View Due Payments");
                System.out.println("7. Exit");
                System.out.print("\nEnter Your Desire Choice : ");
                choice = sc.nextInt();
                switch (choice) {
                    case 2 -> updateAirbnb(username);
                    case 3 -> deleteAirbnb(username);
                    case 4 -> availableAirbnb(username);
                    case 5 -> listAirbnb(username);
                    case 6 -> due_payment();
                    case 7 -> System.out.println("Thank You......");

                    default -> System.out.println("Select From The Given Options.");
                }
            } while (choice != 7);

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public synchronized static void addAirbnb(String username) throws Exception {

        try {
            String hostname = username;
            System.out.print("\nEnter The Airbnb Id : ");
            int id = sc.nextInt();
            sc.nextLine();
            System.out.print("Enter The Airbnb Name : ");
            String name = sc.nextLine();
            System.out.print("Enter The Airbnb Location : ");
            String location = sc.nextLine();
            System.out.print("Enter The Type Of Airbnb (Beach, Countryside, Beachfront, TreeHouse, HistoricalHomes): ");
            String type = sc.nextLine();
            System.out.print("Enter The Airbnb Availability : ");
            boolean availability = sc.nextBoolean();
            System.out.print("Enter The Airbnb Price : ");
            int price = sc.nextInt();
            System.out.print("Enter If Wifi Available At Airbnb : ");
            boolean wifiavailable = sc.nextBoolean();
            System.out.print("Enter The BHK Of Airbnb : ");
            int bhk = sc.nextInt();
            System.out.print("Enter The Airbnb Capacity : ");
            int capacity = sc.nextInt();
            System.out.print("Enter The Airbnb Rating : ");
            int rating = sc.nextInt();
            airbnb.put(id,
                    new Airbnbdetails(id, name, location, username, type, availability, price, wifiavailable, bhk,
                            rating, capacity));

            String sql = "{call insertDetails(?,?,?,?,?,?,?,?,?,?)}";
            CallableStatement cst = airbnbcon.prepareCall(sql);
            cst.setInt(1, id);
            cst.setString(2, name);
            cst.setString(3, location);
            cst.setString(4, hostname);
            cst.setInt(5, price);
            cst.setInt(6, bhk);
            cst.setInt(7, capacity);
            cst.setInt(8, rating);
            cst.setString(9, hostname);
            cst.setString(10, type);

            System.out.println((cst.executeUpdate() > 0) ? "Airbnb Added Successfully." : "Airbnb Not Added");

            users.insertBST(
                    new Airbnbdetails(id, name, location, username, type, availability, price, wifiavailable, bhk,
                            rating, capacity),
                    users.root);

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }

    }

    public synchronized static void updateAirbnb(String username) throws Exception {

        try {
            System.out.print("\nEnter The Airbnb Id : ");
            int searchid = sc.nextInt();
            sc.nextLine();
            if (airbnb.containsKey(searchid)) {
                System.out.print("Enter The Updated Airbnb Name : ");
                String newname = sc.nextLine();
                System.out.print("Enter The Updated Airbnb Location : ");
                String newlocation = sc.nextLine();
                System.out.print("Enter The Updated Type : ");
                String newtype = sc.nextLine();
                System.out.print("Enter The Updated Airbnb Availability : ");
                boolean newavailability = sc.nextBoolean();
                System.out.print("Enter The Updated Airbnb Price : ");
                int newprice = sc.nextInt();
                System.out.print("Enter If Wifi Available At Airbnb : ");
                boolean newwifiavailable = sc.nextBoolean();
                System.out.print("Enter The BHK Of Airbnb : ");
                int newbhk = sc.nextInt();
                System.out.print("Enter The Airbnb Rating : ");
                int newrating = sc.nextInt();
                System.out.print("Enter The Airbnb Capacity : ");
                int newcapacity = sc.nextInt();
                airbnb.put(searchid,
                        new Airbnbdetails(searchid, newname, newlocation, username, newtype, newavailability, newprice,
                                newwifiavailable, newbhk, newrating, newcapacity));

                String sql1 = "{call updateDetails(?,?,?,?,?,?,?,?.?)}";
                CallableStatement cst1 = airbnbcon.prepareCall(sql1);
                cst1.setInt(1, searchid);
                cst1.setString(2, newname);
                cst1.setString(3, newlocation);
                cst1.setString(4, username);
                cst1.setInt(5, newprice);
                cst1.setInt(6, newbhk);
                cst1.setInt(7, newcapacity);
                cst1.setInt(8, newrating);
                cst1.setString(9, newtype);

                System.out.println((cst1.executeUpdate() > 0) ? "Update Done Succesfull" : "Error Occured");

            } else {
                System.out.println("Airbnb Not Found");
            }

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public synchronized static void deleteAirbnb(String username) throws Exception {
        try {
            System.out.print("\nEnter The Id Of Airbnb You want To Delete : ");
            int deleteid = sc.nextInt();
            if (airbnb.containsKey(deleteid)) {
                airbnb.remove(deleteid);

                String sql3 = "{call deleteAirbnb(?)}";
                CallableStatement cst2 = airbnbcon.prepareCall(sql3);
                cst2.setInt(1, deleteid);
                System.out.println((cst2.executeUpdate() > 0) ? "Airbnb Deleted Succesfully" : "Error Ocurred");

            } else {
                System.out.println("Airbnb Not Found");
            }
        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public synchronized void availableAirbnb(String username) throws Exception { // Particular Owner's Airbnb which are
                                                                                 // not occupied
        try {
            System.out.println("Available Airbnb Are : ");

            String s = "SELECT * FROM airbnb_details WHERE Airbnb_availability=true AND Airbnb_Owner=?";
            PreparedStatement pst = airbnbcon.prepareStatement(s);
            pst.setString(1, username);
            ResultSet rs = pst.executeQuery();
            printAirbnb(rs);
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    public synchronized void printAirbnb(ResultSet rs) throws Exception {
        try {
            while (rs.next()) {
                System.out.println("------------------------------------");
                System.out.println("Airbnb Id : " + rs.getInt(1));
                System.out.println("Airbnb Name : " + rs.getString(2));
                System.out.println("Airbnb Location : " + rs.getString(3));
                System.out.println("Airbnb Owner Name : " + rs.getString(4));
                System.out.println("Airbnb Type : " + rs.getString(5));
                System.out.println("Airbnb Price : " + rs.getInt(6));
                System.out.println("Is Airbnb Avaliable : " + rs.getBoolean(7));
                System.out.println("Is Wifi Available At Airbnb : " + rs.getBoolean(8));
                System.out.println("Airbnb BHK : " + rs.getInt(9));
                System.out.println("Airbnb Capacity : " + rs.getInt(10));
                System.out.println("Airbnb Rating : " + rs.getInt(11));
                System.out.println("------------------------------------");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error");
        }
    }

    public synchronized void listAirbnb(String username) throws Exception {
        System.out.println("All The Airbnb Present In Database Are : ");

        String sql4 = "SELECT * FROM airbnb_details WHERE Airbnb_Owner='" + username + "'";
        CallableStatement cst3 = airbnbcon.prepareCall(sql4);
        ResultSet rs = cst3.executeQuery();

        printAirbnb(rs);

    }

    public synchronized void due_payment() throws Exception {
        String sql = "SELECT * FROM user_booking_details WHERE User_payment_status=null";
        PreparedStatement pst = airbnbcon.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            do {
                System.out.println("\nDue Payment Details : ");
                System.out.println("\nUser Name : " + rs.getString("User_name"));
                System.out.println("User Booking Id : " + rs.getInt("User_booking_id"));
                System.out.println("User Id : " + rs.getInt("User_Id"));
                System.out.println("Airbnb Id : " + rs.getInt("Airbnb_id"));
            } while (rs.next());
        } else {
            System.out.println("NO PAYMENTS ARE DUE...");
        }

    }

    @Override
    public String toString() {
        return "Host hostid=" + hostid + ", hostname=" + hostname + ", hostgender=" + hostgender + ", hostage="
                + hostage + ", hostmail=" + hostmail + ", hostphnno=" + hostphnno + ", hostgovid=" + hostgovid;
    }

}