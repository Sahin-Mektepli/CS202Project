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

    public static ArrayList<CategoryStats> getAveragePricePerCategory(){
        ArrayList<CategoryStats> categoryStats=new ArrayList<>();
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select p.category_id, avg(listing.price) as averagePrice from product p, listing\n" +
                    "where listing.product_id = p.id\n" +
                    "group by p.category_id;");

            while ( rs.next () ) {
                CategoryStats s=new CategoryStats(rs.getInt(1),rs.getDouble(2));
                categoryStats.add(s);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryStats;
    }

    //Statistics bitiş
    //yıo, daha yeni başladık:

    /**
     * Yazdığım query'yi beğenmedim. Fakat çalışıyor olmalı.
     * @param k kaç tane kullanıcı verdiği
     * @return en fazla alım yapmış alıcılar
     */
    public static ArrayList<User> topSpentK(int k){
        ArrayList<User> users = new ArrayList<>();
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select sum(l.price),c.id,c.username,c.customer_address from customer as c, orders as o, listing as l where c.id=o.user_id and l.listing_id=o.listing_id group by c.id,c.customer_address,c.id, c.username, c.customer_address order by sum(l.price) desc");
            while(rs.next() && users.size()<k){
                users.add(new User(rs.getInt(2),rs.getString(3),"Customer",rs.getString(4)));
            }
        }
        catch(SQLException e){e.printStackTrace();}
        return users;
    }

    public static Product topSellingProduct(){
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select count(*), p.id, p.pname from listing l, orders o, product p where l.listing_id=o.listing_id and p.id=l.product_id group by product_id order by count(*) desc;");
            if(rs.next())
                return new Product(rs.getInt(2),rs.getString(3));
            else
                System.out.println("There seems to be no product that's being sold...");
        }
        catch (SQLException e){e.printStackTrace();}
        return null;
    }


    public static void main (String[]args) throws SQLException, IOException {

        //System.out.println(numberOfOutOfStock());
        //for (CategoryStats c:getAveragePricePerCategory()){System.out.println(c.categoryId+", "+c.averagePrice);}

        System.out.println(topSellingProduct().getProductId());
        System.out.println(topSellingProduct().getName());



    }
}
