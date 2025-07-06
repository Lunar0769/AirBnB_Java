public class Airbnbdetails {

    int Airbnb_id;
    String Airbnb_name, Airbnb_location, Airbnb_Owner, Airbnb_type;
    boolean Airbnb_availablity;
    int Airbnb_price;
    boolean Airbnb_wifi;
    int Airbnb_bhk;
    int Airbnb_rating;
    int Airbnb_capacity;

    public Airbnbdetails(int airbnb_id, String airbnb_name, String airbnb_location, String airbnb_Owner,
            String airbnb_type, boolean airbnb_availablity, int airbnb_price, boolean airbnb_wifi, int airbnb_bhk,
            int airbnb_rating, int airbnb_capacity) {
        Airbnb_id = airbnb_id;
        Airbnb_name = airbnb_name;
        Airbnb_location = airbnb_location;
        Airbnb_Owner = airbnb_Owner;
        Airbnb_type = airbnb_type;
        Airbnb_availablity = airbnb_availablity;
        Airbnb_price = airbnb_price;
        Airbnb_wifi = airbnb_wifi;
        Airbnb_bhk = airbnb_bhk;
        Airbnb_rating = airbnb_rating;
        Airbnb_capacity = airbnb_capacity;
    }

    public String getAirbnb_type() {
        return Airbnb_type;
    }

    public void setAirbnb_type(String airbnb_type) {
        Airbnb_type = airbnb_type;
    }

    public int getAirbnb_id() {
        return Airbnb_id;
    }

    public void setAirbnb_id(int Airbnb_id) {
        this.Airbnb_id = Airbnb_id;
    }

    public String getAirbnb_name() {
        return Airbnb_name;
    }

    public void setAirbnb_name(String Airbnb_name) {
        this.Airbnb_name = Airbnb_name;
    }

    public String getAirbnb_location() {
        return Airbnb_location;
    }

    public void setAirbnb_location(String Airbnb_location) {
        this.Airbnb_location = Airbnb_location;
    }

    public String getAirbnb_Owner() {
        return Airbnb_Owner;
    }

    public void setAirbnb_Owner(String Airbnb_Owner) {
        this.Airbnb_Owner = Airbnb_Owner;
    }

    public boolean isAirbnb_availablity() {
        return Airbnb_availablity;
    }

    public void setAirbnb_availablity(boolean Airbnb_availablity) {
        this.Airbnb_availablity = Airbnb_availablity;
    }

    public int getAirbnb_price() {
        return Airbnb_price;
    }

    public void setAirbnb_price(int Airbnb_price) {
        this.Airbnb_price = Airbnb_price;
    }

    public boolean isAirbnb_wifi() {
        return Airbnb_wifi;
    }

    public void setAirbnb_wifi(boolean Airbnb_wifi) {
        this.Airbnb_wifi = Airbnb_wifi;
    }

    public int getAirbnb_bhk() {
        return Airbnb_bhk;
    }

    public void setAirbnb_bhk(int Airbnb_bhk) {
        this.Airbnb_bhk = Airbnb_bhk;
    }

    public int getAirbnb_rating() {
        return Airbnb_rating;
    }

    public void setAirbnb_rating(int Airbnb_rating) {
        this.Airbnb_rating = Airbnb_rating;
    }

    public int getAirbnb_capacity() {
        return Airbnb_capacity;
    }

    public void setAirbnb_capacity(int Airbnb_capacity) {
        this.Airbnb_capacity = Airbnb_capacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Airbnbdeatils{");
        sb.append("Airbnb_id=").append(Airbnb_id);
        sb.append(", Airbnb_name=").append(Airbnb_name);
        sb.append(", Airbnb_location=").append(Airbnb_location);
        sb.append(", Airbnb_Owner=").append(Airbnb_Owner);
        sb.append(", Airbnb_availablity=").append(Airbnb_availablity);
        sb.append(", Airbnb_price=").append(Airbnb_price);
        sb.append(", Airbnb_wifi=").append(Airbnb_wifi);
        sb.append(", Airbnb_bhk=").append(Airbnb_bhk);
        sb.append(", Airbnb_rating=").append(Airbnb_rating);
        sb.append(", Airbnb_capacity=").append(Airbnb_capacity);
        sb.append('}');
        return sb.toString();
    }

    // DATA STRUCTURE
    class Node {
        Node left;
        Node right;
        Airbnbdetails airbnb;

        public Node(Airbnbdetails airbnbdetails) {
            this.airbnb = airbnbdetails;
        }

    }

    Node root;

    void insertBST(Airbnbdetails airbnbdetails, Node temp) {
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

    void inOrder(Node t) {
        if (t != null) {
            inOrder(t.left);
            System.out.println(t.airbnb);
            inOrder(t.right);
        }
    }

}