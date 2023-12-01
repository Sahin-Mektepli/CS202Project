import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Product {
    public final int productId;
    public final String name;

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

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


    public static boolean removeProduct(){
        boolean s = false;

        try{
            System.out.println("Enter Product ID: ");
            int id= Integer.parseInt(br.readLine());

            Statement  stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select id from product where id="+id);
                if (rs.next()){//istenilen product mevcutsa tabloda

                    stmt.executeUpdate("delete from listing where product_id = "+id);//bu sanırım gereksiz ama şimdilik kalsın sağlam kafayla test edip ona göre sileriz
                    stmt.executeUpdate("delete from  product where id="+id);

                    s=true;
                }




        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }


    return s;
    }
}
