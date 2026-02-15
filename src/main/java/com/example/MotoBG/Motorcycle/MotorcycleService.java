package com.example.MotoBG.Motorcycle;

import com.example.MotoBG.CarBrand.BrandRepository;
import com.example.MotoBG.CarModel.ModelRepository;
import com.example.MotoBG.MotorcycleStatus.MotorcycleStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MotorcycleService {
    private MotorcycleRepository motorcycleRepository;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;

    public MotorcycleService(MotorcycleRepository motorcycleRepository, BrandRepository brandRepository, ModelRepository modelRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @Transactional
    public String submitMotorcycle(Motorcycle motorcycle, BindingResult bindingResult, MultipartFile[] images, Integer mainImageIndex, Model model) {
        List<byte[]> imageList = new ArrayList<>();
        boolean nullImages = true;

        try {
            for (MultipartFile file : images) {
                if (!file.isEmpty()) {
                    imageList.add(file.getBytes());
                    nullImages = false;
                }
            }
        } catch (IOException e) {
            model.addAttribute("motorcycle", motorcycle);
            model.addAttribute("hasUploadError", true);
            return "motorcycle/add";
        }

        if (motorcycle.getBrand() == null || motorcycle.getModel() == null || nullImages ||
                bindingResult.hasFieldErrors("power") || bindingResult.hasFieldErrors("power") ||
                bindingResult.hasFieldErrors("mileage") || bindingResult.hasFieldErrors("price") ||
                motorcycle.getCategory() == null || motorcycle.getEngine() == null ||
                motorcycle.getGearbox() == null) {
            model.addAttribute("motorcycle", motorcycle);
            model.addAttribute("brands", brandRepository.findAll());
            model.addAttribute("models", modelRepository.findAll());
            model.addAttribute("areImagesSelected", !nullImages);
            model.addAttribute("isBrandSelected", motorcycle.getBrand() != null);
            model.addAttribute("isModelSelected", motorcycle.getModel() != null);
            model.addAttribute("isEngineSelected", !motorcycle.getEngine().isEmpty());
            model.addAttribute("isGearboxSelected", !motorcycle.getGearbox().isEmpty());
            model.addAttribute("isCategorySelected", !motorcycle.getCategory().isEmpty());
            return "motorcycle/add";
        }

        motorcycle.setImages(imageList);
        motorcycle.setMainImageIndex(mainImageIndex);
        motorcycle.setMotorcycleStatus(MotorcycleStatus.AVAILABLE);
        motorcycleRepository.save(motorcycle);
        return "redirect:/home";
    }

    public String submitAsSold(Long id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id).get();
        motorcycle.setMotorcycleStatus(MotorcycleStatus.SOLD);
        motorcycleRepository.save(motorcycle);
        return "redirect:/motorcycles";
    }

    public String submitOffer(Long id, int offerPrice, Model model) {
        Motorcycle motorcycle = motorcycleRepository.findById(id).get();
        motorcycle.setOfferPrice(offerPrice);
        if (motorcycle.getOfferPrice() >= motorcycle.getPrice() || motorcycle.getOfferPrice() < 1000) {
            model.addAttribute("motorcycleId", id);
            model.addAttribute("offerPrice", offerPrice);
            model.addAttribute("invalidPrice", true);
            return "motorcycle/addOffer";
        }
        motorcycle.setOffer(true);
        motorcycleRepository.save(motorcycle);
        return "redirect:/motorcycles/" + id;
    }

    public String submitDeleteOffer(Long id) {
        Motorcycle motorcycle = motorcycleRepository.findById(id).get();
        motorcycle.setOffer(false);
        motorcycle.setOfferPrice(0);
        motorcycleRepository.save(motorcycle);
        return "redirect:/motorcycles/" + id;
    }
}