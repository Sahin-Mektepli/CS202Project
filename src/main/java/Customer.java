import java.io.BufferedReader; //kina like scanner but can read anything.
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.Statement;

public class Customer {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /*public static void addUser(){
        int userId;
        int lastUserId;

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
    //}
}
