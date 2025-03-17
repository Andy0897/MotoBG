package com.example.MotoBG.Motorcycle;

import com.example.MotoBG.CarBrand.*;
import com.example.MotoBG.CarModel.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "motorcycles")
public class Motorcycle {
    @Column(name = "motorcycle_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @ElementCollection
    @Lob
    private List<byte[]> images = new ArrayList<>();

    private Integer mainImageIndex;

    private String engine;

    @Min(value = 10, message = "Кубатура трябва да бъде по-голяма или равна на 10 куб.см")
    @Max(value = 2000, message = "Кубатура не трябва да надвишава 2000 куб.см")
    private int cubicCapacity;

    @Min(value = 1, message = "Мощността трябва да бъде по-голяма или равна на 1 к.с.")
    @Max(value = 1000, message = "Мощността не трябва да надвишава 1000 к.с.")
    private int power;

    private String gearbox;

    private String category;

    @Min(value = 1, message = "Пробегът трябва да бъде поне 1 км.")
    private int mileage;

    @Min(value = 1000, message = "Цената трябва да бъде поне 200 лв.")
    private int price;

    private boolean isOffer;

    private int offerPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public List<byte[]> getImages() {
        return images;
    }

    public void setImages(List<byte[]> images) {
        this.images = images;
    }

    public Integer getMainImageIndex() {
        return mainImageIndex;
    }

    public void setMainImageIndex(Integer mainImageIndex) {
        this.mainImageIndex = mainImageIndex;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getCubicCapacity() {
        return cubicCapacity;
    }

    public void setCubicCapacity(int cubicCapacity) {
        this.cubicCapacity = cubicCapacity;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getGearbox() {
        return gearbox;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isOffer() {
        return isOffer;
    }

    public void setOffer(boolean offer) {
        isOffer = offer;
    }

    public int getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(int offerPrice) {
        this.offerPrice = offerPrice;
    }
}