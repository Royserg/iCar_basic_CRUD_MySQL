package dk.kea.dat18i.spring.demo.car;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class CarController {

    @Autowired
    private CarRepository carRepo;

    /* Rest API examples */
    // all cars
    @GetMapping("/api/cars")
    @ResponseBody
    public List<Car> showCars() {
        List<Car> cars = carRepo.findAllCars();
        return cars;
    }

    // one car, by id
    @GetMapping("/api/cars/{id}")
    @ResponseBody
    public Car showCar(@PathVariable int id) {
        Car car = carRepo.findCar(id);
        return car;
    }


    // View All cars
    @GetMapping("/cars")
    public String car(Model model) {
        model.addAttribute("cars", carRepo.findAllCars());
        model.addAttribute("carForm", new Car());

        return "cars";
    }

    // Add car
    @PostMapping("/cars")
    public String handleCarAdd(@ModelAttribute Car car) {
        carRepo.addCar(car);

        return "redirect:/cars";
    }

    // Delete car from array and redirect to cars
    @RequestMapping(value = "/cars/delete/{id}")
    public String handleCarDelete(@PathVariable int id) {
        carRepo.deleteCar(id);
        return "redirect:/cars";
    }

    // Edit view
    @RequestMapping(value="/cars/edit/{id}")
    public String editCarView(@PathVariable int id, Model model) {
        Car car = carRepo.findCar(id);
        model.addAttribute("car", car);

        return "car_edit";
    }

    // Save edits
    @RequestMapping(value="/cars/edit/{id}", method = RequestMethod.POST)
    public String handleCarEdit(@PathVariable int id, @ModelAttribute Car car) {
        carRepo.editCar(id, car);

        return "redirect:/cars";
    }


}
