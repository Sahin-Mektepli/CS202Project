import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {

    //USER Başlangıç
    public static int addSeller(String name,String address) throws SQLException, IOException {
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(id) from seller");
            int lastUserId=0;


            while ( rs.next () ) {
                lastUserId = rs.getInt (1) ;
            }
            lastUserId++;
            try{

                stmt.executeUpdate("insert into seller values ("+lastUserId+", '"+address+"' , '"+name+"')");
                System.out.println ("ID: " + lastUserId ) ;
                // users.add(new User(lastUserId,name,"Seller",address));
                return lastUserId;

            }catch(SQLException e1){e1.printStackTrace();}

        } catch ( SQLException e ) {
            e.printStackTrace () ;
        }
        return -1; //something went wrong.
    }

    public static int addCustomer(String name,String address) throws SQLException, IOException {
//User id yerine customer ve seller id kullanıyoruz bu verdikleri tabloya uygun değil ama bizim implemantasyonumuz böyle

        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(id) from customer");


            int lastUserId=0;


            while ( rs.next () ) {
                lastUserId = rs.getInt (1) ;

            }
            lastUserId++;
            try{

                stmt.executeUpdate("insert into customer values ("+lastUserId+", '"+name+"' , '"+address+"')");
                System.out.println ("ID: " + lastUserId ) ;

                return lastUserId;
            }catch(SQLException e1){

                e1.printStackTrace();
            }

        } catch ( SQLException e ) {
            e.printStackTrace () ;
        }
        return -1; //something went wrong
    }


    public static ArrayList<String> getPaymentMethodsOfUser(int id) throws SQLException, IOException {
        ArrayList<String> paymentMethods=new ArrayList<>();

        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select  payment_name from pays_with where pays_with.customer_id="+id +";");

            while ( rs.next () ) {
                paymentMethods.add(rs.getString (1) ) ;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return paymentMethods;
    }


    public static boolean addRemovePaymentMethod(int id,String type,String cardNumber){

        boolean success=false;

        try{

            if(type.equals("add")){

                Statement  stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select id from customer where id= "+id);
                if (rs.next()){//query nin sonucu varsa çalışacak
                    stmt.executeUpdate("insert into payment_method values ("+"'"+ cardNumber +"')");
                    stmt.executeUpdate("insert into pays_with values ("+id+","+"'"+ cardNumber +"')");

                    success=true;
                }


            } else if (type.equals("remove")) {

                Statement  stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select payment_name from pays_with where customer_id="+id+" and payment_name = "+"'"+cardNumber+"'");
                if (rs.next()){

                    stmt.executeUpdate("delete from pays_with p where p.customer_id="+id+" and p.payment_name = "+"'"+cardNumber+"'");
                    stmt.executeUpdate("delete from payment_method where pname= "+"'"+cardNumber+"'");
                    success=true;
                }



            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return success;
    }


    public static ArrayList<User> listAllUsers(){

        ArrayList<User> users=new ArrayList<>();
        try{
            Statement stmt = DBConnection.getConnection().createStatement(); //bu kısım şu an yalnızca print için
            Statement stmt2 = DBConnection.getConnection().createStatement();// pek zekice de değil açıkçası
            ResultSet rs = stmt.executeQuery("select * from customer");
            ResultSet rs2 = stmt2.executeQuery("select * from seller");
            while(rs.next()){
                User user=new User(rs.getInt(1),rs.getString(2),"Customer",rs.getString(3));
                users.add(user);

            }
            while(rs2.next()){
                User user=new User(rs2.getInt(1),rs2.getString(3),"Seller",rs2.getString(2));
                users.add(user);
            }


        }
        catch(SQLException e){

            e.printStackTrace();
        }


        return users;
    }
    //USER bitiş




    //Statictics başlangıç
    public static ArrayList<User> listTopKSellers(int k){
        ArrayList<User> topSellers=new ArrayList<>();


        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("SELECT seller.id  seller_id, SUM(listing.price) AS ordered_revenue, seller.seller_name,seller.contact_info\n" +
                    "FROM seller , orders, listing\n" +
                    "where seller.id = listing.seller_id and orders.listing_id = listing.listing_id\n" +
                    "GROUP BY seller.id\n" +
                    "ORDER BY ordered_revenue DESC;\n");

            int count=0;
            while ( rs.next () && count<k) {
             User seller=new User (rs.getInt (1) ,rs.getString(3),"Seller",rs.getString(4)) ;
             topSellers.add(seller);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topSellers;
    }

    public static int numberOfOutOfStock(){
        int stock=0;
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select count(*) from listing where stock = 0;");

            while ( rs.next () ) {
              stock= rs.getInt (1) ;

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return stock;
    }

    //Statistics bitiş
    public static void main (String[]args) throws SQLException, IOException {

        System.out.println(numberOfOutOfStock());


    }
}
