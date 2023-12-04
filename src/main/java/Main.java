import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Main {

    //USER Başlangıç
    public int addSeller(String name,String address)  {
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
                return lastUserId;

            }catch(SQLException e1){e1.printStackTrace();}

        } catch ( SQLException e ) {
            e.printStackTrace () ;
        }
        return -1; //something went wrong.
    }

    public  int addCustomer(String name,String address) {
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


                return lastUserId;
            }catch(SQLException e1){

                e1.printStackTrace();
            }

        } catch ( SQLException e ) {
            e.printStackTrace () ;
        }
        return -1; //something went wrong
    }


    public ArrayList<String> getPaymentMethodsOfUser(int id) {
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


    public boolean addRemovePaymentMethod(int id,String type,String cardNumber){

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


    public  ArrayList<User> listAllUsers(){

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
    //Category başlangıç

    public ArrayList<Category> getCategories() {
        ArrayList<Category> category = new ArrayList<>();
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from category");
            while (rs.next()) {
                Category c = new Category(rs.getInt(1), rs.getString(2));
                category.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }




    /**
     * Well named
     *
     * @param name name of the category
     * @param type "add" or "remove", the type of the action
     * @return id of the category processed. return -1 if the action fails.
     */
    public int addRemoveCategory(String name, String type) {

        if (type.equals("add")) {
            //does it exist?
            for (Category cat : getCategories()) {
                if (name.equals(cat.getName())) //yes, it already exists.
                    return -1;
            }
            //it doesn't preexist, add it
            try {
                Statement stmt = DBConnection.getConnection().createStatement();
                ResultSet rs = stmt.executeQuery("select max(id) from category");
                int lastCategoryId = 0;
                while (rs.next()) {
                    lastCategoryId = rs.getInt(1);
                }
                lastCategoryId++;

                stmt.executeUpdate("insert into category values(" + lastCategoryId + ",'" + name + "')");

                return lastCategoryId;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (type.equals("remove")) {
            //is it even there though?
            for (Category cat : getCategories()) {
                if (name.equals(cat.getName())) { //if it exists:
                    try {
                        Statement stmt = DBConnection.getConnection().createStatement();
                        ResultSet rs = stmt.executeQuery("select id from category where cname='" + name + "'"); //store the delendum.id
                        rs.next();
                        int categoryId = rs.getInt(1);
                        stmt.executeUpdate("delete from category where cname='" + name +"'"); //deletes the delendum

                        return categoryId; //returns the id of the category
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else{
            System.out.println("please have the action type as either \"remove\" or \"add\"!");
            return -1;
        }
        return -1;
    }

    public boolean removeProduct(int id){
        boolean s = false;

        try{


            Statement  stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select id from product where id="+id);
            if (rs.next()){//istenilen product mevcutsa tabloda

                stmt.executeUpdate("delete from listing where product_id = "+id);//bu sanırım gereksiz ama şimdilik kalsın sağlam kafayla test edip ona göre sileriz
                stmt.executeUpdate("delete from  product where id="+id);

                s=true;
            }




        } catch ( SQLException e) {
            e.printStackTrace();
        }


        return s;}

    public int addProduct(String name,String description,int categoryId){//!!! category id ve category description da eklenmeli database güncellenmeli
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(id) from product");
            int lastProductId = 0;

            while (rs.next()){lastProductId = rs.getInt(1);}
            lastProductId++;
            try{
                stmt.execute("insert into product values("+lastProductId+", '"+name+"',"+categoryId+")");
                return lastProductId;
            }catch(SQLException e1){e1.printStackTrace();}
        }
        catch (SQLException e ) {e.printStackTrace () ;}
        return -1; //sth went wrong

    }
    public ArrayList<Product> listProducts(){
        ArrayList<Product> products = new ArrayList<Product>();
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from product");
            while(rs.next()){
                products.add(new Product(rs.getInt(1), rs.getString(2)));
            }
        } catch (SQLException e){e.printStackTrace();}
        return products;

    }
    //category bitiş
    public int createListing(int productId,int sellerId,int price,int stock){
        int lastUserId=-1;
        try{
            Statement stmt = DBConnection.getConnection().createStatement();


            ResultSet rs1 = stmt.executeQuery("select listing_id from listing where seller_id = "+sellerId+" and product_id = "+productId);
            if (rs1.next()){
                int id= rs1.getInt(1);
                stmt.executeUpdate("update listing set price = " +price+" where listing_id= "+id);
                return id;
            }
            else{
                ResultSet rs = stmt.executeQuery("select max(listing_id) from listing");
                while ( rs.next () ) {
                    lastUserId = rs.getInt (1) ;
                }
                lastUserId++;

                stmt.executeUpdate("insert into listing values ("+lastUserId+", "+productId+" , "+sellerId+", "+stock+", "+price+" )");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUserId;
    }

    public Listing getListing(int listingId){
        Listing listing=null;
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from listing where listing_id= "+listingId);
            while ( rs.next () ) {
                int productId = rs.getInt (2) ;
                int sellerId = rs.getInt (3) ;
                int stock = rs.getInt (4) ;
                int price = rs.getInt (5) ;
                listing=new Listing(listingId,sellerId, productId,price,stock);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listing;
    }
    public  ArrayList<Listing> getListingsOfSeller(int sellerId){
        ArrayList<Listing> listings = new ArrayList<>();
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from listing where seller_id="+sellerId);

            while(rs.next()){
                listings.add(new Listing(rs.getInt(1),rs.getInt(3),rs.getInt(2), rs.getInt(5),rs.getInt(4)));
            }
        }
        catch (SQLException e){e.printStackTrace();}
        return listings;
    }



    //Statictics başlangıç
    public ArrayList<User> listTopKSellers(int k){
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
             count++;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topSellers;
    }

    public int numberOfOutOfStock(){
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

    public ArrayList<CategoryStats> getAveragePricePerCategory(){
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
    public  ArrayList<User> topSpentK(int k){
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

    public Product topSellingProduct(){
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

    public ArrayList<Product> getKTopSellingProductofSeller(int k, int userId){
        ArrayList<Product> products = new ArrayList<>();
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select count(product_id), seller_id,product_id,p.pname from listing l, orders o, product p, seller s where l.listing_id=o.listing_id and p.id=l.product_id and l.seller_id=s.id AND seller_id="+userId+" group by product_id, seller_id order by count(*) desc;");
            while(rs.next() && products.size()<k){
                products.add(new Product(rs.getInt(3),rs.getString(4)));
            }
        }catch(SQLException e){e.printStackTrace();}


        return products;
    }

    public ArrayList<Order> getOrdersOfUser(int userId){
        ArrayList<Order> orders = new ArrayList<>();
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select o.id, o.listing_id, o.user_id, o.dates from orders o, customer c where o.user_id=c.id and c.id="+userId);
            while(rs.next()){
                orders.add(new Order(rs.getInt(1),rs.getInt(2),rs.getInt(3),rs.getDate(4)));}
        }catch(SQLException e){e.printStackTrace();}

        return orders;
    }

    public boolean buy(int listingId, int userId){
        try {
            Statement stmt = DBConnection.getConnection().createStatement();

            //if the stock in the listing is non-positive, return false:
            ResultSet rs = stmt.executeQuery("select stock,product_id from listing where listing_id="+listingId);
            if(rs.next()){
                int stock = rs.getInt(1);
                if(stock>0){ //nah, we have it. place an order than!
                    ResultSet rs2 = stmt.executeQuery("select max(id) from orders");
                    if(rs2.next()) {
                        int orderId = rs2.getInt(1) + 1;
                        stmt.executeUpdate("insert into orders values("+orderId + "," + listingId + "," + userId + ",current_date())"); //adding the order with given listingId
                        stmt.executeUpdate("update listing set stock="+(stock-1)+" where listing_id="+listingId);
                        return true;
                    }
                }
                else{ //the stock is non-positive!
                    return false;
                }
            }
            else{
                System.out.println("Provided listingId does not correspond to an existing listing");
            }

        }
        catch (SQLException e){e.printStackTrace();}
        return false;
    }

    public Main() throws SQLException {

    }
    public static void main (String[]args) throws SQLException {




    }
}
