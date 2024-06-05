package com.example.examspring.controller;



import com.example.examspring.model.Vehicle;
import com.example.examspring.repository.VehicleRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleController(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @GetMapping("/list")
    public String getAllVehicles(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }


        String role = (String) session.getAttribute("role");
        if (!role.equals("Admin")) {
            return "redirect:/";
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();
        model.addAttribute("vehicles", vehicles);
        return "vehicle-list";
    }

    @GetMapping("/infor")
    public String getVehicles(Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        if (!role.equals("Admin") && !role.equals("Sale")) {
            return "redirect:/error";
        }

        List<Vehicle> vehicles = vehicleRepository.findAll();
        model.addAttribute("vehicles", vehicles);
        return "infor";
    }


    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {

        if (session.getAttribute("userId") == null) {

            return "redirect:/login";
        }


        model.addAttribute("vehicle", new Vehicle());
        return "vehicle-form";
    }

    @PostMapping("/add")
    public String addVehicle(@ModelAttribute("vehicle") Vehicle vehicle, HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        vehicleRepository.save(vehicle);
        return "redirect:/vehicles/list";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        if (!role.equals("Admin")) {
            return "redirect:/";
        }

        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            model.addAttribute("vehicle", vehicle);
            return "vehicle-update";
        } else {
            return "redirect:/vehicles/list";
        }
    }


    @PostMapping("/edit/{id}")
    public String updateVehicle(@PathVariable("id") Long id, @ModelAttribute("vehicle") Vehicle updatedVehicle, HttpSession session) {

        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        if (!role.equals("Admin")) {
            return "redirect:/";
        }


        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setId(updatedVehicle.getId());
            vehicle.setName(updatedVehicle.getName());
            vehicle.setModel(updatedVehicle.getModel());
            vehicle.setYearOfManufacture(updatedVehicle.getYearOfManufacture());
            vehicle.setColor(updatedVehicle.getColor());
            vehicleRepository.save(vehicle);
        }
        return "redirect:/vehicles/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable("id") Long id, HttpSession session) {
        if (session.getAttribute("userId") == null) {
            return "redirect:/login";
        }

        String role = (String) session.getAttribute("role");
        if (!role.equals("Admin")) {
            return "redirect:/";
        }


        vehicleRepository.deleteById(id);
        return "redirect:/vehicles/list";
    }

}
