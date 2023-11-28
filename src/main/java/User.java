import java.io.BufferedReader; //kina like scanner but can read anything.
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class User {
    public final int userId;
    public final String name;
    public final String type;
    public final String address;

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//deneme için yazdım bu constructoru

    public User(){userId=0;name=null;type=null;address=null;};
    public User(int userId, String name, String type, String address) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public void addSeller(String name,String address) throws SQLException {
//bunu denemek için yazdım databaseden veri alabiliyoruz

         // Execute a SELECT query
        Statement stmt = DBConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select * from seller");

        try {
             while ( rs.next () ) {
                 int id = rs.getInt ("id") ;
                 String contact = rs.getString ("contact_info") ;


                 System.out.println ("ID: " + id + ", Contact info : " + contact) ;
                 }
            } catch ( SQLException e ) {
             e.printStackTrace () ;
             }

    }


}
