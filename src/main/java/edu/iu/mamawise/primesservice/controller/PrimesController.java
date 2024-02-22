package edu.iu.mamawise.primesservice.controller;
import edu.iu.mamawise.primesservice.service.IPrimesService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/primes")
public class PrimesController {
    IPrimesService primesService;

    public PrimesController(IPrimesService primesService){
        this.primesService = primesService;
    }
    @GetMapping("/{n}")
    public boolean isPrimes(@PathVariable int n){
        return primesService.isPrime(n);
    }
    @GetMapping
    public String greetings(){
        return "Welcome to the primes service";
    }
}
