import java.io.IOException;
import java.sql.SQLException;

public class Main {
    public static void main (String[]args) throws SQLException, IOException {

        //User.addSeller();
        //User.addCustomer();
       /*for (String p: User.getPaymentMethodsOfUser()){
           System.out.println(p);
       }*/
       // System.out.println(User.addRemovePaymentMethod());

       /* for(User u :User.listAllUsers()){
            System.out.println(u.getUserId()+" ,"+u.getName()+" ,"+u.type+" ,"+u.getAddress());
        }*/
        /*for( Category c :Category.getCategories()) {
            System.out.println(c.getCategoryId() + " ," + c.getName() );

        }*/
        //System.out.println(Product.removeProduct());
        System.out.println(Listing.createListing(3,1,40,1));

    }
}
