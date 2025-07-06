import java.util.Scanner;

public class AirBnB {

    public static void main(String[] args) throws Exception {

        Login_SignUp ls = new Login_SignUp();
        Scanner sc = new Scanner(System.in);
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("\t\t\t \033[1mWelcome To Airbnb\033[0m");
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("1. Admin Login");
        System.out.println("2. Login For Host");
        System.out.println("3. Become A Host");
        System.out.println("4. Login As Customer");
        System.out.println("5. SignUp As Customer");
        System.out.print("\nEnter Your Choice : ");
        int choice = sc.nextInt();
        switch (choice) {
            case 1 -> ls.adminlogin();

            case 2 -> ls.hostlogin();

            case 3 -> ls.hostSignUp();

            case 4 -> ls.userlogin();

            case 5 -> ls.userSignUp();

            default -> System.out.println("Select Correct Option.");

        }
        sc.close();
    }

}