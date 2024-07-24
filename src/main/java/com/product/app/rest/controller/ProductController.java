package com.product.app.rest.controller;

import com.product.app.mapper.ProductMapper;
import com.product.app.model.Product;
import com.product.app.rest.dto.request.ProductRequestDto;
import com.product.app.rest.dto.response.ProductResponseDto;
import com.product.app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Controller
@RequiredArgsConstructor
@RequestMapping
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ProductController {

    ProductService service;
    ProductMapper mapper;

    @GetMapping
    public String homePage(Model model) {
        return findPage(1, "name", "asc", 10, model);
    }

    @GetMapping("/product/page/{pageNo}")
    public String findPage(@PathVariable (value = "pageNo") int pageNo,
                                @RequestParam("sortField") String sortField,
                                @RequestParam("sortDir") String sortDir,
                                @RequestParam("pageSize") int pageSize,
                                Model model) {

        List<Integer> pageSizeList = List.of(5, 10, 15, 20);
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        Page<Product> page = service.findAll(pageNo, pageSize, sortField, sortDir);

        List<ProductResponseDto> productList = page.getContent()
                .stream()
                .map(mapper::toResponse)
                .toList();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("pageSizeList", pageSizeList);
        model.addAttribute("productList", productList);

        return "index";
    }

    @GetMapping("/product/add")
    public String createProductForm(Model model) {
        ProductRequestDto product = ProductRequestDto.builder()
                .build();

        model.addAttribute("product", product);
        return "add_product";
    }

    @PostMapping("/product/save")
    public String createProduct(@Valid @ModelAttribute("product") ProductRequestDto request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add_product";
        } else {
            service.create(mapper.toEntity(request));
            return "redirect:/";
        }
    }

    @GetMapping("/product/details/{id}")
    public String productDetailsForm(@PathVariable( value = "id") UUID id, Model model) {

        ProductResponseDto product = mapper.toResponse(service.findById(id));
        model.addAttribute("product", product);
        return "product_details";
    }
}
