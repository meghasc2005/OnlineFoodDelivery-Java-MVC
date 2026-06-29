package com.fooddelivery.service;

import com.fooddelivery.model.Coupon;
import java.util.List;

public interface CouponService {
    Coupon getCouponByCode(String code);
    List<Coupon> getAllActiveCoupons();
    double calculateDiscount(String code, double subtotal, double deliveryFee);
}
