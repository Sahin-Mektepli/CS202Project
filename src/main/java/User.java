import java.io.BufferedReader; //kina like scanner but can read anything.
import java.io.IOException;
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


    public static void addSeller() throws SQLException, IOException {
//bunu denemek için yazdım databaseden veri alabiliyoruz


        Statement stmt = DBConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select max(id) from seller");
        int lastUserId=0;
        System.out.println("Enter User Name: ");
        String name= br.readLine();
        //System.out.println(name);
        System.out.println("Enter address: ");
        String address=br.readLine();
        try {
             while ( rs.next () ) {
                 lastUserId = rs.getInt (1) ;

                 }
             lastUserId++;
             try{

                 stmt.executeUpdate("insert into seller values ("+lastUserId+", '"+address+"' , '"+name+"')");
                  System.out.println ("ID: " + lastUserId ) ;

             }catch(SQLException e1){

                 e1.printStackTrace();
            }

            } catch ( SQLException e ) {
             e.printStackTrace () ;
             }

    }


}
