package com.fooddelivery.dao;

import com.fooddelivery.model.Coupon;
import java.sql.SQLException;
import java.util.List;

public interface CouponDAO {
    Coupon getCouponByCode(String code) throws SQLException;
    List<Coupon> getAllActiveCoupons() throws SQLException;
}
