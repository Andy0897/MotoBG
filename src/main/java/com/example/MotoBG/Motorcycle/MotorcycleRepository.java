package com.example.MotoBG.Motorcycle;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MotorcycleRepository extends CrudRepository<Motorcycle, Long> {
    @Query(nativeQuery = true, value = "SELECT motorcycle_id, category, cubic_capacity, engine, gearbox, is_offer, main_image_index, mileage, motorcycle_status, offer_price, power, price, brand_id, model_id FROM motorcycles " +
            "WHERE motorcycle_status = 'AVAILABLE'")
    public List<Motorcycle> findAllAvailable();

    @Query(nativeQuery = true, value = "SELECT motorcycle_id, category, cubic_capacity, engine, gearbox, is_offer, main_image_index, mileage, motorcycle_status, offer_price, power, price, brand_id, model_id FROM motorcycles " +
            "WHERE motorcycle_status = 'SOLD';")
    public List<Motorcycle> findAllSold();
}