package com.web.DAO;

import com.web.Utils.JdbcUtil;
import com.web.entity.AjaxResult;
import com.web.entity.UserCart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserCartDao {
    // 根据用户ID查询所有关联的购物车ID
    public List<Integer> findCartIdsByUserId(String userId) {
        List<Integer> cartIds = new ArrayList<>();
        String sql = "SELECT cart_id FROM user_carts WHERE user_id = ?";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    cartIds.add(rs.getInt("cart_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartIds;
    }

    // 根据购物车ID查询所属用户ID
    public String findUserIdByCartId(int cartId) {
        String sql = "SELECT user_id FROM user_carts WHERE cart_id = ?";

        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, cartId);
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
    public List<UserCart> findAllRelations() {
        List<UserCart> relations = new ArrayList<>();
        String sql = "SELECT user_id, cart_id FROM user_carts";

        try (Connection conn = JdbcUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                UserCart relation = new UserCart();
                relation.setUserId(rs.getString("user_id"));
                relation.setCartId(rs.getInt("cart_id"));
                relations.add(relation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return relations;
    }

    public AjaxResult addCartRelation(String userId, Integer cartId) {
        String sql = "INSERT INTO user_carts (user_id, cart_id) VALUES (?, ?)";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, cartId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("添加成功") : AjaxResult.error("添加失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    public AjaxResult deleteCartRelation(String userId, Integer cartId) {
        String sql = "DELETE FROM user_carts WHERE user_id = ? AND cart_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, cartId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    public AjaxResult deleteCartOnly(Integer cartId){
        String sql = "DELETE FROM user_carts WHERE cart_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cartId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("删除成功") : AjaxResult.error("删除失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }

    public AjaxResult deleteUserOnly(String userId){
        String sql = "DELETE FROM user_carts WHERE user_id = ?";
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

    // 更新
    public AjaxResult updateCartRelation(String userId, Integer oldCartId, Integer newCartId) {
        String sql = "UPDATE user_carts SET cart_id = ? WHERE user_id = ? AND cart_id = ?";
        try (Connection conn = JdbcUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newCartId);
            pstmt.setString(2, userId);
            pstmt.setInt(3, oldCartId);
            int rows = pstmt.executeUpdate();
            return rows > 0 ? AjaxResult.success("更新成功") : AjaxResult.error("更新失败");
        } catch (SQLException e) {
            e.printStackTrace();
            return AjaxResult.error("数据库异常: " + e.getMessage());
        }
    }
}