import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Category {
    public final int categoryId;
    public final String name;

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public static ArrayList<Category> getCategories(){
        ArrayList<Category> category=new ArrayList<>();
        try{
            Statement stmt = DBConnection.getConnection().createStatement();

            ResultSet rs = stmt.executeQuery("select * from category");

            while(rs.next()){
                Category c=new Category(rs.getInt(1),rs.getString(2));
                category.add(c);

            }



        }
        catch(SQLException e){

            e.printStackTrace();
        }




        return category;

    }



}
