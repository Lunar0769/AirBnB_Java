import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Scanner;

public class Booking {
    Connection con;
    Scanner sc = new Scanner(System.in);

    Booking() throws Exception {
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/airbnb", "root", "");

        // String s1="SELECT * FROM user_booking_details";

        String getstatus = "SELECT * FROM user_booking_details WHERE User_status='CHECKED-IN'";
        String setstatus = "UPDATE user_booking_details SET User_status='CHECKED-OUT' WHERE User_booking_id=?";
        String setairbnb = "UPDATE airbnb_details SET Airbnb_availability=true WHERE Airbnb_id=?";
        PreparedStatement pst = con.prepareStatement(getstatus);
        PreparedStatement pst2 = con.prepareStatement(setstatus);
        PreparedStatement pst3 = con.prepareStatement(setairbnb);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            LocalDate checkoutdate = rs.getDate(8).toLocalDate();
            int bookingid = rs.getInt(1);
            int airbnbid = rs.getInt(4);
            if (LocalDate.now().isAfter(checkoutdate)) {
                pst2.setInt(1, bookingid);
                pst2.executeUpdate();
                pst3.setInt(1, airbnbid);
                pst3.executeUpdate();
            }
        }

    }

    public synchronized void search_Airbnb(String username, int userid) throws Exception {

        System.out.println("\n\033[1mPLEASE PROVIDE YOUR REQIUREMENTS FOR AIRBNB...\033[0m");
        System.out.print("\nEnter The Country You Want To Stay : ");
        String location = sc.next();
        System.out.print(
                "Enter The Type Of Airbnb You Want (Beach, Countryside, Beachfront, TreeHouse, HistoricalHomes) : ");
        String type = sc.next();
        System.out.print("Enter If You Want Wifi (true/false) : ");
        boolean wifi = sc.nextBoolean();
        System.out.print("Enter The BHK for Airbnb : ");
        int bhk = sc.nextInt();
        System.out.print("Enter Maximum Number Of People Will Stay : ");
        int capacity = sc.nextInt();

        boolean flag = true;

        String sql = "SELECT * FROM airbnb_details WHERE Airbnb_location=? AND Airbnb_type=? AND Airbnb_wifi=? AND Airbnb_bhk>=? AND Airbnb_capacity>=? AND Airbnb_availability=? ";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, location);
        pst.setString(2, type);
        pst.setBoolean(3, wifi);
        pst.setInt(4, bhk);
        pst.setInt(5, capacity);
        pst.setBoolean(6, flag);

        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            do {

                System.out.println("\n\033[1mHERE ARE THE AIRBNB AS PER YOUR REQUIREMENTS : \033[0m");
                printAirbnb(rs);

                System.out.println(
                        "\033[1mYou Want To Book An AirBnB Or Else Search For Other Options (YES/NO) : \033[0m");
                String bookconfirmation = sc.next();

                if (bookconfirmation.equalsIgnoreCase("YES")) {
                    registerUser(username, userid);
                } else {
                    search_Airbnb(username, userid);
                }
            } while (rs.next());
        } else {

            System.out.println("No Such Airbnb Found...");
        }

    }

    public synchronized void registerUser(String username, int userid) throws Exception {
        System.out.print("\nEnter The Airbnb Id : ");
        int airbnbid = sc.nextInt();
        System.out.print("Enter The Total Number Of People Will Stay : ");
        int totalpeople = sc.nextInt();
        System.out.print("Enter The No. Of Days You Want To Stay : ");
        int no_ofdays = sc.nextInt();
        System.out.print("Enter The Check-In Date (YYYY-MM-DD) : ");
        String checkin = sc.next();

        LocalDate checkin_date = LocalDate.parse(checkin);

        boolean flag = true;
        while (flag) {
            if (checkin_date.isBefore(LocalDate.now())) {
                System.out.println("\033[1mCheck-In Date Should Be After Today's Date.\033[m");
                System.out.print("Enter The Check-In Date (YYYY-MM-DD) : ");
                checkin = sc.next();
                checkin_date = LocalDate.parse(checkin);
            } else {
                flag = false;
            }
        }

        LocalDate checkout_date = checkin_date.plusDays(no_ofdays);

        // String sql = "{call book_airbnb(?,?,?,?,?,?,?,?)}";
        String sql = "INSERT INTO user_booking_details(User_name, User_Id, Airbnb_id, No_of_days,No_of_people, Check_In,Check_Out, User_status) VALUES (?,?,?,?,?,?,?,?)";
        // CallableStatement cst = con.prepareCall(sql);
        PreparedStatement cst = con.prepareStatement(sql);
        cst.setString(1, username);
        cst.setInt(2, userid);
        cst.setInt(3, airbnbid);
        cst.setInt(4, no_ofdays);
        cst.setInt(5, totalpeople);

        cst.setDate(6, Date.valueOf(checkin_date));
        cst.setDate(7, Date.valueOf(checkout_date));

        if (checkin_date.isEqual(LocalDate.now())) {
            cst.setString(8, "CHECKED-IN");
        } else {
            cst.setString(8, "YET TO CHECK-IN");
        }

        String setairbnb = "UPDATE airbnb_details SET Airbnb_availability=false WHERE Airbnb_id=" + airbnbid;
        PreparedStatement pst = con.prepareStatement(setairbnb);

        System.out.println(
                ((cst.executeUpdate() > 0) && pst.executeUpdate() > 0)
                        ? "\nPlease Procceed For Payment Gateway"
                        : "Error Occured");

        payment_gateway(username, userid);
    }

    public synchronized void viewBookedAirbnb(int userid) throws Exception {
        String sql = "SELECT * FROM user_booking_details WHERE User_Id=" + userid;
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            System.out.println("\n\033[1mHERE ARE YOUR BOOKED AIRBNB : \033[0m");
            do {
                System.out.println("\nYour Booking Id : " + rs.getInt(1));
                System.out.println("Name : " + rs.getString(2));
                System.out.println("Id : " + rs.getInt(3));
                System.out.println("Airbnb Id : " + rs.getInt(4));
                System.out.println("You Stay For : " + rs.getInt(5));
                System.out.println("No Of People : " + rs.getInt(6));
                System.out.println("Check In Date : " + rs.getDate(7));
                System.out.println("Check Out Date : " + rs.getDate(8));
                System.out.println("Status : " + rs.getString(9));

            } while (rs.next());

        } else {
            System.out.println("We Think you haven't Booked any AirBnB...");
        }

    }

    public synchronized void giveRating(int userid) throws Exception {

        viewBookedAirbnb(userid);

        int id = 0;

        String s = "SELECT User_booking_id FROM user_booking_details WHERE User_id=" + userid;
        PreparedStatement pst = con.prepareStatement(s);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            id = rs.getInt("User_booking_id");
        }
        if (id != 0) {
            Boolean flag = true;
            while (flag) {

                System.out.println("Enter The AirBnB Id You Want To Give Rating Out Of 5 : ");
                int rating = sc.nextInt();

                if (rating > 5 && rating < 0) {
                    System.out.println("Please Enter Rating Between 0-5");
                } else {
                    String sql = "UPDATE airbnb_details INNER JOIN user_booking_details ON airbnb_details.Airbnb_id=user_booking_details.Airbnb_id SET Airbnb_rating="
                            + rating + " WHERE User_booking_id=" + id;
                    PreparedStatement pst1 = con.prepareStatement(sql);

                    System.out.println(pst1.executeUpdate() > 0 ? "RATING SUBMITTED SUCCESSFULLY" : "ERROR OCCURED");
                }
            }
        } else {

            System.out.println("USER ID NOT FOUND");
        }
    }

    public synchronized void cancelBooking(int userid) throws Exception {
        con.setAutoCommit(false);
        LocalDate localDate = LocalDate.now();
        Date date = Date.valueOf(localDate);
        viewBookedAirbnb(userid);
        System.out.print("Enter The Booking Id You Want To Cancel : ");
        int cancelid = sc.nextInt();
        String sql = "SELECT * FROM user_booking_details WHERE User_booking_id=" + cancelid;
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        if (rs.next()) {
            do {
                Date checkout = rs.getDate(8);
                if (checkout == null) {
                    if (date.before(rs.getDate(7))) {
                        String sql2 = "DELETE FROM User_booking_details WHERE User_booking_id=" + cancelid;
                        String sql3 = "UPDATE airbnb_details SET Airbnb_availability=true WHERE Airbnb_id="
                                + rs.getInt(4);
                        PreparedStatement pst2 = con.prepareStatement(sql2);
                        PreparedStatement pst3 = con.prepareStatement(sql3);

                        System.out.print("\n\nAre You Sure, You Want To Cancel The Booking (Yes/No) : ");
                        String ans = sc.next();
                        if (ans.equalsIgnoreCase("yes")) {
                            System.out.println(((pst2.executeUpdate()) > 0 && (pst3.executeUpdate()) > 0)
                                    ? "\n\nBooking Cancelled Successfully"
                                    : "Error Occured");
                            con.commit();
                        } else {
                            con.rollback();
                        }
                    } else {
                        System.out.println("You Can't Cancel Your Booking As Time Period For Cancelation is over..");
                    }
                }
            } while (rs.next());
        } else {
            System.out.println("You Cannot Cancel Your Booking...");
        }
        con.setAutoCommit(true);
    }

    public synchronized void history(int userid) throws Exception {

        viewBookedAirbnb(userid);

    }

    public synchronized void payment_gateway(String username, int userid) throws Exception {
        System.out.print("\n\nSelect The Payment Method : \n1. Credit Card \n2. Cash\n");

        int payment_method = sc.nextInt();

        switch (payment_method) {
            case 1 -> {
                System.out.print("\nEnter The Credit Card Number : ");
                int credit_card_number = sc.nextInt();
                System.out.print("Enter The Credit Card Expiry Date (YYYY-MM-DD) : ");
                String credit_card_expiry_date = sc.next();
                System.out.print("Enter The Credit Card CVV : ");
                int credit_card_cvv = sc.nextInt();
                String sql = "INSERT INTO user_creditcard_details VALUES (?,?,?)";
                PreparedStatement pst = con.prepareCall(sql);
                pst.setInt(1, credit_card_number);
                pst.setString(2, credit_card_expiry_date);
                pst.setInt(3, credit_card_cvv);

                final_payment(username, userid, "Credit Card");
            }
            case 2 -> final_payment(username, userid, "Cash");

            default -> System.out.println("Invalid Input");
        }
    }

    public synchronized void final_payment(String username, int userid, String mode) throws Exception {

        String sql1 = "SELECT * FROM user_booking_details INNER JOIN airbnb_details ON user_booking_details.Airbnb_id=airbnb_details.Airbnb_id WHERE User_id="
                + userid + " AND User_status<>'CHECKED-OUT'";
        PreparedStatement pst1 = con.prepareStatement(sql1);

        ResultSet rs = pst1.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("User_Id");
            int aid = rs.getInt("Airbnb_id");
            int days = rs.getInt("No_of_days");
            int people = rs.getInt("No_of_people");
            String location = rs.getString("Airbnb_location");
            String type = rs.getString("Airbnb_type");
            int price = rs.getInt("Airbnb_price");
            String airbnbname = rs.getString("Airbnb_name");
            Date d = rs.getDate("Check_In");
            Date d1 = rs.getDate("Check_Out");
            LocalDate checkin = d.toLocalDate();
            LocalDate checkout = d1.toLocalDate();

            int totalamount = price * days * people;

            String paymentstatus = "UPDATE user_booking_details SET user_payment_status='Payment-Done'";
            Statement st = con.createStatement();
            st.executeUpdate(paymentstatus);

            String sql2 = "INSERT INTO payment VALUES (?,?,?,?,?,?)";
            PreparedStatement pst2 = con.prepareStatement(sql2);
            pst2.setInt(1, id);
            pst2.setInt(2, aid);
            pst2.setInt(3, price);
            pst2.setInt(4, days);
            pst2.setInt(5, totalamount);
            pst2.setString(6, mode);

            System.out.println("YOU STAYED AT : " + airbnbname);
            System.out.println("NO OF PEOPLE STAYED WERE : " + people);
            System.out.println("CHECK-IN DATE : " + checkin);
            System.out.println("CHECK-OUT DATE : " + checkout);
            System.out.println("PRICE FOR PER DAY WAS : " + price);
            System.out.println("TOTAL AMOUNT TO BE PAID IS : " + totalamount);

            System.out.println("\nBOOKING DONE SUCCESSFULLY..");

            System.out.println("YOUR BILL RECIPT IS GENERATED...");

            File f = new File(username + " Bill.txt");
            FileWriter fw = new FileWriter(f);
            fw.write("----------------------------------------------------------");
            fw.write("\n\t\t\t\t\tRECIPT FOR " + username.toUpperCase());
            fw.write("\n----------------------------------------------------------");
            fw.write("\n USERNAME : " + username);
            fw.write("\n AIRBNB NAME : " + airbnbname);
            fw.write("\n AIRBNB LOCATION : " + location);
            fw.write("\n AIRBNB TYPE : " + type);
            fw.write("\n NO OF PEOPLE STAYED : " + people);
            fw.write("\n PRICE FOR PER DAY : " + price);
            fw.write("\n CHECK-IN DATE : " + checkin);
            fw.write("\n CHECK-OUT DATE : " + checkout);
            fw.write("\n\n TOTAL AMOUNT TO BE PAID : " + totalamount);
            fw.write("\n MODE OF PAYMENT : " + mode);

            fw.flush();
            fw.close();
        }
        System.exit(0);
    }

    public synchronized void printAirbnb(ResultSet rs) throws Exception {
        do {
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
        } while (rs.next());
    }
}