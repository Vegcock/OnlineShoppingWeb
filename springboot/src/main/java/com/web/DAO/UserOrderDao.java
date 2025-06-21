package com.web.DAO;

import com.web.Utils.JdbcUtil;
import com.web.entity.AjaxResult;
import com.web.entity.UserOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserOrderDao {
    public List<Integer> findOrderIdsByUserId(String userId) {
        List<Integer> orderIds = new ArrayList<>();
        String sql = "SELECT order_id FROM user_orders WHERE user_id = ?";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    orderIds.add(rs.getInt("order_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderIds;
    }

    // 根据购物车ID查询所属用户ID
    public String findUserIdByOrderId(int orderId) {
        String sql = "SELECT user_id FROM user_orders WHERE order_id = ?";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, orderId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("user_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 查询完整的关联记录
    public List<UserOrder> findAllRelations() {
        List<UserOrder> relations = new ArrayList<>();
        String sql = "SELECT user_id, order_id FROM user_orders";

        try (Connection conn = JdbcUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UserOrder relation = new UserOrder();
                relation.setUserId(rs.getString("user_id"));
                relation.setOrderId(rs.getInt("order_id"));
                relations.add(relation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relations;
    }

    public AjaxResult addOrderRelation(String userId, Integer orderId) {
        String sql = "INSERT INTO user_orders (user_id, order_id) VALUES (?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, orderId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("订单关联成功") : AjaxResult.error("插入失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    public AjaxResult deleteOrderRelation(String userId, Integer orderId) {
        String sql = "DELETE FROM user_orders WHERE user_id = ? AND order_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, orderId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    public AjaxResult deleteOrderOnly(Integer orderId){
        String sql = "DELETE FROM user_orders WHERE order_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, orderId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    public AjaxResult deleteUserOnly(String userId){
        String sql = "DELETE FROM user_orders WHERE user_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    // 更新订单编号
    public AjaxResult updateOrderRelation(String userId, Integer oldOrderId, Integer newOrderId) {
        String sql = "UPDATE user_orders SET order_id = ? WHERE user_id = ? AND order_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newOrderId);
            pstmt.setString(2, userId);
            pstmt.setInt(3, oldOrderId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }
}
