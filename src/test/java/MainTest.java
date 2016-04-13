import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainTest {

	public static void main(String[] args) throws SQLException {

		Connection conn = null;

		Statement stmt = null;

		ResultSet rs = null;

		String sql;

		String url = "jdbc:mysql://anlutec.com:3306/yiwifi?useUnicode=true&characterEncoding=UTF8";

		String username = "yiwifi";

		String password = "yiwifi_19842015";

		try {

			Class.forName("com.mysql.jdbc.Driver");

			System.out.println("成功加载MySQL驱动程序");

			conn = DriverManager.getConnection(url, username, password);

			stmt = conn.createStatement();

			sql = "show tables";

			stmt.executeQuery(sql);

			rs = stmt.executeQuery(sql);// executeQuery会返回结果的集合，否则返回空值

			while (rs.next()) {

				System.out.println(String.format("Table:%s", rs.getString(1)));

			}

		} catch (SQLException e) {

			System.out.println("MySQL操作错误");

			e.printStackTrace();

		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			
			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

}
