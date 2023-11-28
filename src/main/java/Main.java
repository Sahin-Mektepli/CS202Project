import java.sql.SQLException;

public class Main {
    public static void main (String[]args) throws SQLException {
        User user =new User(12,"a","b","c");
        user.addSeller("abc","bcd");
    }
}
