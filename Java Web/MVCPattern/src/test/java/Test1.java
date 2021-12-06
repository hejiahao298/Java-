import com.hjh.entity.Book;
import com.hjh.utils.JDBCTools;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class Test1 {

    @Test
    public void JDBCTest(){
        Connection connection = JDBCTools.getConnection();
        System.out.println(connection);
    }

    @Test
    public void BeanList(){
        Connection connection = JDBCTools.getConnection();
        String sql = "select * from book,bookcase where book.bookcaseid = bookcase.id";
        QueryRunner queryRunner = new QueryRunner();
        try {
            List<Book> list = queryRunner.query(connection,sql,new BeanListHandler<Book>(Book.class));
            System.out.println(list);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
