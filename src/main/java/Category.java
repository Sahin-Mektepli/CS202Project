import javax.xml.stream.events.StartElement;
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

    public static ArrayList<Category> listCategories(){// Buna gerek var mı
        ArrayList<Category> categories = new ArrayList<>();
        try {
            Statement stmt = DBConnection.getConnection().createStatement();
            ResultSet rs = stmt.executeQuery("select * from category");
            while(rs.next()){
                categories.add(new Category(rs.getInt(2), rs.getString(1))); //we had it backwars, sorry lads
            }
        } catch(SQLException e){ e.printStackTrace();}
        return categories;
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
        } catch(SQLException e){e.printStackTrace();}
        return category;
    }

    private static boolean categoryDoesntPreexist(Category category){
        for(Category cat : listCategories()){
            if(category.getName().equals(cat.getName()))
                return false;
        }
        return true;
    }
}
