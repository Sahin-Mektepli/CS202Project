import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.PrimitiveIterator;

public class Product {
    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public final int productId;
    public final String name;

    public Product(int productId, String name) {
        this.productId = productId;
        this.name = name;
    }

    public int getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }


    public static int addProduct() throws SQLException, IOException {
        try{
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select max(id) from product");
            int lastProductId = 0;
            System.out.println("Enter product name");
            String name = br.readLine();
            while (rs.next()){lastProductId = rs.getInt(1);}
            lastProductId++;
            try{
                stmt.execute("insert into product values("+lastProductId+", '"+name+"')");
                return lastProductId;
            }catch(SQLException e1){e1.printStackTrace();}
        }
        catch (SQLException e ) {e.printStackTrace () ;}
        return -1; //sth went wrong
    }
    public static ArrayList<Product> listProducts(){
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
}
