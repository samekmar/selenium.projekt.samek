package selenium.projekt.samek;

import org.testng.annotations.Test;

import  java.sql.Connection;
import  java.sql.Statement;
import  java.sql.ResultSet;
import  java.sql.DriverManager;
import  java.sql.SQLException;

public class  SQLConnector {
    @Test
    public static void main(String[] args) throws  ClassNotFoundException, SQLException {

        // dane do połączenie do bazy PZP
        String dbUrl = "jdbc:oracle:thin:@126.193.3.67:1521:PZPWL";
        String username = "tp";
        String password = "!tp!";
        Class.forName("oracle.jdbc.driver.OracleDriver");

        // zapytanie do wykonania
        String query = "select sms.recipient, sms.requesttime, decode(sms.status,'P', 'wysłany', 'D', 'doręczony',NULL,'zarejestrowany do wysłania') status from pzp.tb_zlecenie zl, pzp.tb_smsout sms where zl.numerzlecenia = 'KAW008/N/12998971/2020/1' and zl.id_zlecenie = sms.id_zlecenie order by 2 desc";

        // utwórz połączenie do DB PZP
        Connection con = DriverManager.getConnection(dbUrl,username,password);
        Statement stmt = con.createStatement();

        // wykonaj zapytanie
        ResultSet rs= stmt.executeQuery(query);

        // przekaż wynik zapytania (jesli kilka rekordów zwróć w pętli)
        System.out.println("Status SMS (odbiorca, data wysłania, status) :");
        while (rs.next()){
            String phoneNumber = rs.getString(1);
            String dataSend = rs.getString(2);
            String smsStatus = rs.getString(3);
            System. out.println(phoneNumber+"  "+dataSend+"  "+smsStatus);
        }

        // zamknij połączenie do DB
        con.close();
    }
}
// komentarz końcowy
