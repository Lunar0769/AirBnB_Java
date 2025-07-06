import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class User {
    static Scanner sc = new Scanner(System.in);
    static Connection custcon = null;
    static Host host;
    Booking booking;

    String name;
    String gender;
    String mailaddress;
    String phnno;
    String govid;

    public User(String name, String gender, String mailaddress, String phnno, String govid) {
        this.name = name;
        this.gender = gender;
        this.mailaddress = mailaddress;
        this.phnno = phnno;
        this.govid = govid;
    }

    User() throws Exception {
        host = new Host();
        custcon = DriverManager.getConnection("jdbc:mysql://localhost:3306/airbnb",
                "root", "");
        booking = new Booking();
    }

    User(String temp) {
    }

    void addUser() throws Exception {

        System.out.print("Enter your Name : ");
        String username = sc.next();
        System.out.print("Enter Your Gender : ");
        String usergender = sc.next();
        System.out.print("Enter Your Mail Address : ");
        String usermail = sc.next();
        System.out.print("Enter Your Phone Number : ");
        String userphnno = sc.next();
        int flag = 0;
        while (flag == 0) {
            if (userphnno.length() == 10) {
                flag = 1;
                break;
            } else {
                System.out.println("Invalid Phone Number");
                System.out.print("Enter Your Phone Number : ");
                userphnno = sc.next();
            }
        }

        System.out.print("Enter Your Govermnet Id (Driver's Lic/Passport/Adhar Card No) : ");
        String usergovid = sc.next();

        String sql = "INSERT INTO user_details VALUES (null,?,?,?,?,?)";
        PreparedStatement pst = custcon.prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, usergender);
        pst.setString(3, usermail);
        pst.setString(4, userphnno);
        pst.setString(5, usergovid);

        pst.executeUpdate();

        User data = new User(username, usergender, usermail, userphnno, usergovid);

        insert_linkedList_user(data);

    }

    class Node {
        Node next;
        Node left;
        Node right;
        Airbnbdetails airbnb;
        User data;

        public Node(User data) {
            this.data = data;
        }

        public Node(Airbnbdetails airbnbdetails) {
            this.airbnb = airbnbdetails;
        }
    }

    Node root;
    Node head;

    public synchronized void insert_linkedList_user(User data) {
        Node n = new Node(data);
        if (head == null) {
            head = n;
        } else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = n;
        }
    }

    public synchronized void checkUser(String username, int userid) throws Exception {

        String query = "SELECT * FROM user_booking_details WHERE User_id=" + userid;
        PreparedStatement pst = custcon.prepareCall(query);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            do {
                String status = rs.getString(9);
                if (status.equalsIgnoreCase("CHECKED-IN")) {
                    existingUser_Functions(username, userid);
                } else if (status.equalsIgnoreCase("CHECKED-OUT")) {
                    newUser_Functions(username, userid);
                }else{
                    existingUser_Functions(username, userid);
                }
            } while (rs.next());
        } else {
            newUser_Functions(username, userid);
        }

    }

    public synchronized void newUser_Functions(String username, int userid) throws Exception {

        int choice;
        do {
            System.out
                    .println("-----------------------------------------------------------------------------------");
            System.out.println("\t\t\t \033[1mUSER FUNCTIONALITY\033[0m");
            System.out
                    .println("-----------------------------------------------------------------------------------");

            System.out.println("\nWelcome " + username + " To Airbnb");

            System.out.println("\n1. List All The Airbnb");
            System.out.println("2. List Airbnb Based On Price");
            System.out.println("3. Book Airbnb");
            System.out.println("4. View Booked Airbnb");
            System.out.println("5. Cancel Airbnb");
            System.out.println("6. Your Booked History");
            System.out.println("7. Exit");
            System.out.print("\nEnter Your Choice : ");
            choice = sc.nextInt();
            switch (choice) {
                case 1 -> {
                    System.out.println("All The Airbnb Present In Database Are : ");
                    listAirbnb();
                }
                case 2 -> listAirbnbByPrice();

                case 3 -> booking.search_Airbnb(username, userid);

                case 4 -> booking.viewBookedAirbnb(userid);

                case 5 -> booking.cancelBooking(userid);

                case 6 -> booking.history(userid);

                case 7 -> System.out.println("Thank You..");

                default -> System.out.println("Invalid Choice\nEnter The Choice From The Given Choices.");
            }
        } while (choice != 6);

    }

    public synchronized void existingUser_Functions(String username, int userid) throws Exception {

        int choice;
        do {
            System.out
                    .println("-----------------------------------------------------------------------------------");
            System.out.println("\t\t\t \033[1mUSER FUNCTIONALITY\033[0m");
            System.out
                    .println("-----------------------------------------------------------------------------------");
            System.out.println("\nWelcome " + username + " To Airbnb");
            System.out.println("\n1. Checkout & Payment");
            System.out.println("2. Book Another Airbnb");
            System.out.println("3. Give Rating");
            System.out.println("4. View Booked Airbnb");
            System.out.println("5. Cancel Your Booking");
            System.out.println("6. Exit");
            System.out.print("\nEnter Your Choice : ");
            choice = sc.nextInt();
            switch (choice) {
                case 1 -> booking.payment_gateway(username, userid);

                case 2 -> booking.search_Airbnb(username, userid);

                case 3 -> booking.giveRating(userid);

                case 4 -> booking.viewBookedAirbnb(userid);

                case 5 -> booking.cancelBooking(userid);

                case 6 -> System.out.println("Thank You..");

                default -> System.out.println("Invalid Choice\nEnter The Choice From The Given Choices.");
            }

        } while (choice != 6);

    }

    public synchronized void listAirbnb() throws Exception {

        try {

            String sql = "SELECT * FROM airbnb_details WHERE Airbnb_availability=true";
            PreparedStatement pst = custcon.prepareCall(sql);
            ResultSet rs = pst.executeQuery();

            host.printAirbnb(rs);

        } catch (InputMismatchException e) {
            System.out.println("Invalid Input");
        } catch (SQLException e) {
            System.out.println("SQL Error");
        } catch (Exception e) {
            System.out.println("Error");
        }
    }

    // DATA STRUCTURE

    public synchronized void insertBST(Airbnbdetails airbnbdetails, Node temp) {
        if (temp == null) {
            temp = new Node(airbnbdetails);
            root = temp;
        } else if (temp.airbnb.Airbnb_price > airbnbdetails.Airbnb_price && temp.left == null) {
            temp.left = new Node(airbnbdetails);
        } else if (temp.airbnb.Airbnb_price > airbnbdetails.Airbnb_price) {
            insertBST(airbnbdetails, temp.left);
        } else if (temp.airbnb.Airbnb_price < airbnbdetails.Airbnb_price && temp.right == null) {
            temp.right = new Node(airbnbdetails);
        } else {
            insertBST(airbnbdetails, temp.right);
        }
    }

    public synchronized void inOrder(Node t) {
        if (t != null) {
            inOrder(t.left);
            System.out.println(t.airbnb);
            inOrder(t.right);
        }
    }

    public synchronized void listAirbnbByPrice() {
        System.out.println("List of Airbnb by Price");
        inOrder(root);
    }

    @Override
    public String toString() {
        return "User booking=" + booking + ", name=" + name + ", gender=" + gender + ", mailaddress=" + mailaddress
                + ", phnno=" + phnno + ", govid=" + govid;
    }

}
