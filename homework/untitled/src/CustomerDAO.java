import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    // 添加客户
    public void addCustomer(Customer c) throws Exception {
        String sql = "INSERT INTO customer (user_id, name, phone, email, address, vip) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getUserId());
            ps.setString(2, c.getName());
            ps.setString(3, c.getPhone());
            ps.setString(4, c.getEmail());
            ps.setString(5, c.getAddress());
            ps.setString(6, c.getVip());
            ps.executeUpdate();
        }
    }

    // 查询所有客户
    public List<Customer> getAllCustomers() throws Exception {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getString("user_id"),
                        rs.getString("name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("address"),
                        rs.getString("vip")
                );
                list.add(c);
            }
        }
        return list;
    }

    // 更新客户
    public void updateCustomer(Customer c) throws Exception {
        String sql = "UPDATE customer SET name=?, phone=?, email=?, address=?, vip=? WHERE user_id=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getName());
            ps.setString(2, c.getPhone());
            ps.setString(3, c.getEmail());
            ps.setString(4, c.getAddress());
            ps.setString(5, c.getVip());
            ps.setString(6, c.getUserId());
            ps.executeUpdate();
        }
    }

    // 删除客户
    public void deleteCustomer(String userId) throws Exception {
        String sql = "DELETE FROM customer WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.executeUpdate();
        }
    }
}
