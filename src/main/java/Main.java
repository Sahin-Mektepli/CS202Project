import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main (String[]args) throws SQLException, IOException {

        //User.addSeller();
        //User.addCustomer();
       /*for (String p: User.getPaymentMethodsOfUser()){
           System.out.println(p);
       }*/
        System.out.println(User.addRemovePaymentMethod());
    }
}
