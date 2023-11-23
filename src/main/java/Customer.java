import java.io.BufferedReader; //kina like scanner but can read anything.
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;

public class Customer extends User {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    public Customer(int userId, String name, String address){
        super(userId, name, "Customer", address);
    }
    public static void addUser(){
        int userId;
        int lastUserId; //-E- this will be fetched from the database (?)

        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(userId) from user"); //just have the query here.
            String userIdOfTable = null;            //
            while(rs.next()){
                userIdOfTable = rs.getString(1);
                lastUserId = rs.getInt(1);
            }
            if(lastUserId == 0) //bu doğru olmayabilir
                userId = 1001;
            else {
                lastUserId++;
                userId = lastUserId;
            }


            System.out.println("Enter user name");
            String userName = br.readLine(); //mesela yani
        } catch (Exception e){}
    }

    public static void deleteUser(User user){
        /*
        find the user first. If exists, delete; if not, show an error.
         */
    }
}
