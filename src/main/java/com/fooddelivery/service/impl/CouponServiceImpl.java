package com.fooddelivery.service.impl;

import com.fooddelivery.dao.CouponDAO;
import com.fooddelivery.dao.impl.CouponDAOImpl;
import com.fooddelivery.model.Coupon;
import com.fooddelivery.service.CouponService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CouponServiceImpl implements CouponService {

    private final CouponDAO couponDAO = new CouponDAOImpl();

    @Override
    public Coupon getCouponByCode(String code) {
        if (code == null || code.trim().isEmpty()) return null;
        try {
            return couponDAO.getCouponByCode(code.trim());
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Coupon> getAllActiveCoupons() {
        try {
            return couponDAO.getAllActiveCoupons();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public double calculateDiscount(String code, double subtotal, double deliveryFee) {
        Coupon c = getCouponByCode(code);
        if (c == null) return 0.0;
        return c.calculateDiscount(subtotal, deliveryFee);
    }
}
