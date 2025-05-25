package com.example.DemoSecurity.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/product")
public class ProductController {

    public record Product(Integer productId,String productName,double price) {}

    List<Product> products =new ArrayList<>(
            List.of(
                    new Product(1,"Iphone",1000),
                    new Product(2,"Samsung",800),
                    new Product(3,"OnePlus",600),
                    new Product(4,"Nokia",400)
            )
    );

    @GetMapping
    public List<Product> getProducts() {
        return products;
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        products.add(product);
        return product;
    }
}
