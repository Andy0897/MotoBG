package com.example.MotoBG.Motorcycle;

import com.example.MotoBG.CarBrand.BrandRepository;
import com.example.MotoBG.CarModel.ModelRepository;
import com.example.MotoBG.ImageEncoder;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/motorcycles")
public class MotorcycleController {
    private MotorcycleRepository motorcycleRepository;
    private MotorcycleService motorcycleService;
    private BrandRepository brandRepository;
    private ModelRepository modelRepository;

    public MotorcycleController(MotorcycleRepository motorcycleRepository, MotorcycleService motorcycleService, BrandRepository brandRepository, ModelRepository modelRepository) {
        this.motorcycleRepository = motorcycleRepository;
        this.motorcycleService = motorcycleService;
        this.brandRepository = brandRepository;
        this.modelRepository = modelRepository;
    }

    @GetMapping("")
    public String getShowMotorcycles(Model model) {
        List<Motorcycle> motorcycles = (List<Motorcycle>) motorcycleRepository.findAll();
        model.addAttribute("motorcycles", motorcycles);
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("encoder", new ImageEncoder());
        return "motorcycle/show";
    }

    @GetMapping("/filter")
    public String filterMotorcycles(
            @RequestParam(value = "brand", required = false) Long brandId,
            @RequestParam(value = "model", required = false) Long modelId,
            @RequestParam(value = "minPrice", required = false) Integer minPrice,
            @RequestParam(value = "maxPrice", required = false) Integer maxPrice,
            @RequestParam(value = "mileage", required = false) Integer maxMileage,
            @RequestParam(value = "isOffer", required = false) Boolean isOffer,
            Model model) {

        List<Motorcycle> filteredMotorcycles = ((List<Motorcycle>) motorcycleRepository.findAll()).stream()
                .filter(motorcycle -> (brandId == null || motorcycle.getBrand().getId().equals(brandId)))
                .filter(motorcycle -> (modelId == null || motorcycle.getModel().getId().equals(modelId)))
                .filter(motorcycle -> (minPrice == null || motorcycle.getPrice() >= minPrice))
                .filter(motorcycle -> (maxPrice == null || motorcycle.getPrice() <= maxPrice))
                .filter(motorcycle -> (maxMileage == null || motorcycle.getMileage() <= maxMileage))
                .filter(motorcycle -> (isOffer == null || motorcycle.isOffer() == isOffer))
                .collect(Collectors.toList());

        model.addAttribute("cars", filteredMotorcycles);
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("models", modelRepository.findAll());
        model.addAttribute("encoder", new ImageEncoder());
        return "motorcycle/show";
    }


    @GetMapping("/{Id}")
    public String getShowMotorcycle(@PathVariable("Id") Long id, Model model) {
        Motorcycle motorcycle = motorcycleRepository.findById(id).get();
        model.addAttribute("motorcycle", motorcycle);
        model.addAttribute("encoder", new ImageEncoder());
        return "motorcycle/showSingle";
    }


    @GetMapping("/add")
    public String getAddMotorcycle(Model model) {
        Motorcycle motorcycle = new Motorcycle();
        model.addAttribute("motorcycle", motorcycle);
        model.addAttribute("brands", brandRepository.findAll());
        model.addAttribute("isBrandSelected", true);
        model.addAttribute("isModelSelected", true);
        model.addAttribute("areImagesSelected", true);
        model.addAttribute("isEngineSelected", true);
        model.addAttribute("isGearboxSelected", true);
        model.addAttribute("isCategorySelected", true);
        model.addAttribute("hasUploadError", false);
        return "motorcycle/add";
    }

    @PostMapping("/submit")
    public String submitMotorcycle(@Valid Motorcycle motorcycle, BindingResult bindingResult, @RequestParam("images") MultipartFile[] images, @RequestParam(value = "mainImageIndex", required = false) Integer mainImageIndex, Model model) {
        if (mainImageIndex == null || mainImageIndex < 0 || mainImageIndex >= images.length) {
            mainImageIndex = 0;
        }
        return motorcycleService.submitMotorcycle(motorcycle, bindingResult, images, mainImageIndex, model);
    }

    @PostMapping("/delete/{id}")
    public String getSubmitDeleteMotorcycle(@PathVariable("id") Long id) {
        return motorcycleService.submitDeleteMotorcycle(id);
    }

    @GetMapping("/offers/add/{id}")
    public String getAddOffer(@PathVariable("id") Long id, Model model) {
        int offerPrice = 1000;
        model.addAttribute("motorcycleId", id);
        model.addAttribute("offerPrice", offerPrice);
        model.addAttribute("invalidPrice", false);
        return "motorcycle/addOffer";
    }

    @PostMapping("/offers/submit")
    public String getSubmitOffer(@RequestParam("motorcycleId") Long id, @RequestParam("offerPrice") int offerPrice, Model model) {
        return motorcycleService.submitOffer(id, offerPrice, model);
    }

    @PostMapping("/offers/delete/{id}")
    public String getSubmitDeleteOffer(@PathVariable("id") Long id) {
        return motorcycleService.submitDeleteOffer(id);
    }
}