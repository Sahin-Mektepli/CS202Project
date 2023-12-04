import javax.swing.plaf.nimbus.State;
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


    /*public static ArrayList<Category> getCategories() {
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
   /* public static int addRemoveCategory(String name, String type) {

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
            System.out.println("please have the action type as either \"remove\n or \"add\"!");
            return -1;
        }
        return -1;
    }
*/

}




