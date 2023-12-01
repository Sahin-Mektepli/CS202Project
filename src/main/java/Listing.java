import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Listing {
    public final int listingId;
    public final int sellerId;
    public final int productId;
    public final int price;
    public final int stock;

    public Listing(int listingId, int sellerId, int productId, int price, int stock) {
        this.listingId = listingId;
        this.sellerId = sellerId;
        this.productId = productId;
        this.price = price;
        this.stock = stock;
    }

    public int getListingId() {
        return listingId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getProductId() {
        return productId;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public static int createListing(int productId,int sellerId,int price,int stock){
        int lastUserId=-1;
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(listing_id) from listing");

            ResultSet rs1 = stmt.executeQuery("select listing_id from listing where seller_id = "+sellerId+" and product_id = "+productId);
            if (rs1.next()){
                int id= rs1.getInt(1);
                stmt.executeUpdate("update listing set price = " +price+" where listing_id= "+id);
                return id;
            }
            else{
                while ( rs.next () ) {
                    lastUserId = rs.getInt (1) ;

                }
                lastUserId++;

                stmt.executeUpdate("insert into listing values ("+lastUserId+", "+productId+" , "+sellerId+", "+stock+", "+price+" )");
                System.out.println ("ID: " + lastUserId ) ;
            }






            while ( rs.next () ) {
                lastUserId = rs.getInt (1) ;

            }
            lastUserId++;

            stmt.executeUpdate("insert into listing values ("+lastUserId+", "+productId+" , "+sellerId+", "+stock+", "+price+" )");
            System.out.println ("ID: " + lastUserId ) ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastUserId;
    }

    public static Listing getListing(int listingId){
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
}
