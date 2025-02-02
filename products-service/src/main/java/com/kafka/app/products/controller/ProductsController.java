package com.kafka.app.products.controller;

import com.kafka.app.core.dto.Product;
import com.kafka.app.products.dto.ProductCreationRequest;
import com.kafka.app.products.dto.ProductCreationResponse;
import com.kafka.app.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductsController
{
    private final ProductService productService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Product> findAll()
    {
        return productService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductCreationResponse save(@RequestBody @Valid ProductCreationRequest request)
    {
        var product = new Product();
        BeanUtils.copyProperties(request, product);
        Product result = productService.save(product);

        var productCreationResponse = new ProductCreationResponse();
        BeanUtils.copyProperties(result, productCreationResponse);
        return productCreationResponse;
    }
}
