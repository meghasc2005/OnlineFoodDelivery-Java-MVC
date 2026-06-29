package com.fooddelivery.dao.impl;

import com.fooddelivery.dao.CouponDAO;
import com.fooddelivery.model.Coupon;
import com.fooddelivery.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CouponDAOImpl implements CouponDAO {

    private static final String FIND_BY_CODE = "SELECT * FROM coupons WHERE code = ? AND is_active = 1";
    private static final String FIND_ALL_ACTIVE = "SELECT * FROM coupons WHERE is_active = 1 ORDER BY min_order_amount";

    private Coupon mapRow(ResultSet rs) throws SQLException {
        return new Coupon(
            rs.getInt("coupon_id"),
            rs.getString("code"),
            rs.getString("description"),
            rs.getString("discount_type"),
            rs.getDouble("discount_value"),
            rs.getDouble("min_order_amount"),
            rs.getDouble("max_discount"),
            rs.getBoolean("is_active"),
            rs.getTimestamp("created_at")
        );
    }

    @Override
    public Coupon getCouponByCode(String code) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_BY_CODE)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    @Override
    public List<Coupon> getAllActiveCoupons() throws SQLException {
        List<Coupon> list = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(FIND_ALL_ACTIVE);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }
}
