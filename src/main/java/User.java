import java.io.BufferedReader; //kina like scanner but can read anything.
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class User {
    public final int userId;
    public final String name;
    public final String type;
    public final String address;

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));



    public User(int userId, String name, String type, String address) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }


    public static void addSeller() throws SQLException, IOException {
//bunu denemek için yazdım databaseden veri alabiliyoruz

        try {
        Statement stmt = DBConnection.getConnection().createStatement();
        ResultSet rs = stmt.executeQuery("select max(id) from seller");
        int lastUserId=0;
        System.out.println("Enter User Name: ");
        String name= br.readLine();
        //System.out.println(name);
        System.out.println("Enter address: ");
        String address=br.readLine();

             while ( rs.next () ) {
                 lastUserId = rs.getInt (1) ;

                 }
             lastUserId++;
             try{

                 stmt.executeUpdate("insert into seller values ("+lastUserId+", '"+address+"' , '"+name+"')");
                  System.out.println ("ID: " + lastUserId ) ;

             }catch(SQLException e1){

                 e1.printStackTrace();
            }

            } catch ( SQLException e ) {
             e.printStackTrace () ;
             }

    }

    public static void addCustomer() throws SQLException, IOException {
//User id yerine customer ve seller id kullanıyoruz bu verdikleri tabloya uygun değil ama bizim implemantasyonumuz böyle

try {
    Statement stmt = DBConnection.getConnection().createStatement();
    ResultSet rs = stmt.executeQuery("select max(id) from customer");


        int lastUserId=0;
        System.out.println("Enter User Name: ");
        String name= br.readLine();
        //System.out.println(name);
        System.out.println("Enter address: ");
        String address=br.readLine();

            while ( rs.next () ) {
                lastUserId = rs.getInt (1) ;

            }
            lastUserId++;
            try{

                stmt.executeUpdate("insert into customer values ("+lastUserId+", '"+name+"' , '"+address+"')");
                System.out.println ("ID: " + lastUserId ) ;

            }catch(SQLException e1){

                e1.printStackTrace();
            }

        } catch ( SQLException e ) {
            e.printStackTrace () ;
        }

    }/**
     Soru: Payment method kart numarası mı olmalı yoksa kredi kartı, nakit,hediye kartı gibi şeyler mi olmalı???
     Ben ikincisine göre yaptım ama ikisi için de çalışır bu metod sorunun cevabına göre tabloyu güncellememiz gerekiyor
     yeni bir tablo oluşturdum müşterilerin ödeme yöntemlerini içinde tutuyor galiba doğrusu böyle olmalı ödeme yaparken kayıtlı yöntemlerden birini kullanmış oluyor böylece
     **/

     public static ArrayList<String> getPaymentMethodsOfUser() throws SQLException, IOException {
        ArrayList<String> paymentMethods=new ArrayList<>();
         int id;
         try{System.out.println("Enter User ID: ");
             try {
                  id= Integer.parseInt(br.readLine());
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
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

    /**
     * yeni eklediğim pays_with tablosunu kullanacağım burada
     * ufak bir sorunumuz var burada seller_id ve customer_id aynı olabiliyor ve bu bir sıkıntı bence buna baklım bir daha
     *
     */
    public static boolean addRemovePaymentMethod(){
         boolean success=false;
         String type = null;
        try{
        System.out.println("Type (write add or remove): ");
        type=br.readLine();
        System.out.println("Enter User ID: ");
        int id= Integer.parseInt(br.readLine());
        System.out.println("Enter Card Number: ");
        String cardNumber=br.readLine();

        if(type.equals("add")){

            Statement stmt = DBConnection.getConnection().createStatement();
            stmt.executeUpdate("insert into payment_method values ("+"'"+ cardNumber +"')");
            stmt.executeUpdate("insert into pays_with values ("+id+","+"'"+ cardNumber +"')");

            success=true;

        } else if (type.equals("remove")) {
//burada bir hata var
            Statement  stmt = DBConnection.getConnection().createStatement();
             stmt.executeUpdate("delete from pays_with p where p.customer_id="+id+"and p.payment_name ="+"'"+cardNumber+"'");
             stmt.executeUpdate("delete from payment_method where pname="+"'"+cardNumber+"'");


        }
        } catch (IOException|SQLException e) {
        e.printStackTrace();
    }

        return success;
        }
    }




