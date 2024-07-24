package com.product.app.rest.controller;

import com.product.app.mapper.ProductMapper;
import com.product.app.model.Product;
import com.product.app.repository.ProductRepository;
import com.product.app.rest.dto.request.ProductRequestDto;
import com.product.app.rest.dto.response.ProductResponseDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.ModelAndView;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    private static final String PRODUCT_CODE = "gtrf54zuio";
    private static final String PRODUCT_NAME = "PRODUCT NAME";
    private static final BigDecimal PRICE_EUR = BigDecimal.ONE;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");


    @BeforeAll
    public static void beforeAll() {
        postgres.withReuse(true);
        postgres.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", () -> postgres.getJdbcUrl());
        registry.add("spring.datasource.username=", () -> postgres.getUsername());
        registry.add("spring.datasource.password=", () -> postgres.getPassword());
    }

    @AfterAll
    public static void afterAll() {
        postgres.stop();
    }

    @Test
    void homePage() throws Exception {
        ModelAndView result = this.mockMvc
                .perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("sortField"))
                .andExpect(model().attributeExists("sortDir"))
                .andExpect(model().attributeExists("reverseSortDir"))
                .andExpect(model().attributeExists("pageSizeList"))
                .andExpect(model().attributeExists("productList"))
                .andReturn().getModelAndView();

        assertEquals(1, result.getModel().get("currentPage"));
        assertEquals("name", result.getModel().get("sortField"));
        assertEquals("asc", result.getModel().get("sortDir"));
        assertEquals(10, result.getModel().get("pageSize"));

        List<ProductResponseDto> actualProductList = (List<ProductResponseDto>) result.getModel().get("productList");

        assertTrue(actualProductList.size() > 1);
    }

    @Test
    void findPage() throws Exception {
        int pageNo = 2;
        int pageSize = 5;
        String sortField = "name";
        String sortDir = "asc";

        Sort sort = Sort.by(sortField).ascending();
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);

        List<ProductResponseDto> expectedProductList = productRepository.findAll(pageable).getContent()
                .stream()
                .map(productMapper::toResponse)
                .toList();

        ModelAndView result = this.mockMvc
                .perform(get("/product/page/{pageNo}", pageNo)
                        .param("sortField", sortField)
                        .param("sortDir", sortDir)
                        .param("pageSize", String.valueOf(pageSize)))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("totalItems"))
                .andExpect(model().attributeExists("pageSize"))
                .andExpect(model().attributeExists("sortField"))
                .andExpect(model().attributeExists("sortDir"))
                .andExpect(model().attributeExists("reverseSortDir"))
                .andExpect(model().attributeExists("pageSizeList"))
                .andExpect(model().attributeExists("productList"))
                .andReturn().getModelAndView();

        assertEquals(pageNo, result.getModel().get("currentPage"));
        assertEquals(sortField, result.getModel().get("sortField"));
        assertEquals(sortDir, result.getModel().get("sortDir"));
        assertEquals(pageSize, result.getModel().get("pageSize"));

        List<ProductResponseDto> actualProductList = (List<ProductResponseDto>) result.getModel().get("productList");

        assertTrue(actualProductList.size() > 1);

        assertEquals(expectedProductList, actualProductList);
    }

    @Test
    void createProductForm() throws Exception {

        this.mockMvc
                .perform(get("/product/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("add_product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void productDetailsForm() throws Exception {
        String id = "f22b8e5e-0e1b-4f78-8d0b-63f67827c8a0";

        ModelAndView result = this.mockMvc
                .perform(get("/product/details/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("product_details"))
                .andExpect(model().attributeExists("product"))
                .andReturn().getModelAndView();

        ProductResponseDto expectedProduct = productMapper.toResponse(productRepository.findById(UUID.fromString(id)).get());

        ProductResponseDto actualProduct = (ProductResponseDto) result.getModel().get("product");

        assertThat(actualProduct)
                .usingRecursiveComparison()
                .ignoringFields("priceUsd")
                .isEqualTo(expectedProduct);
    }

    @Test
    void productDetailsFormProductNotFoundShouldReturnToErrorPage() throws Exception {
        UUID id = UUID.randomUUID();
        while(productRepository.existsById(id)) {
            id = UUID.randomUUID();
        }

        this.mockMvc
                .perform(get("/product/details/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("custom_error"))
                .andExpect(model().attributeExists("exception"))
                .andReturn().getModelAndView();
    }

    @Test
    void createProduct() throws Exception {
        ProductRequestDto requestDto = constructRequestDto(PRODUCT_CODE);
        List<Product> currentProducts = productRepository.findAll();

        this.mockMvc
                .perform(post("/product/save")
                        .flashAttr("product", requestDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        List<Product> newProducts = productRepository.findAll();

        Product actual = newProducts.stream()
                .filter(product -> currentProducts.stream()
                        .noneMatch(currentProduct -> currentProduct.getId().equals(product.getId())))
                .findFirst()
                .get();

        assertEquals(requestDto.getName(), actual.getName());
        assertEquals(requestDto.getCode(), actual.getCode());
        assertEquals(requestDto.getPriceEur().intValue(), actual.getPriceEur().intValue());
        assertNotNull(actual.getPriceUsd());
        assertTrue(actual.isAvailable());
    }

    @Test
    void createProductShouldReturnToFormWhenInvalidRequest() throws Exception {
        ProductRequestDto invalidRequestDto = constructRequestDto(null);

        this.mockMvc
                .perform(post("/product/save")
                        .flashAttr("product", invalidRequestDto))
                .andExpect(status().isOk())
                .andExpect(view().name("add_product"))
                .andExpect(model().attributeHasFieldErrors("product", "code"));
    }

    private ProductRequestDto constructRequestDto(String code) {
        return ProductRequestDto.builder()
                .code(code)
                .available(true)
                .name(PRODUCT_NAME)
                .priceEur(PRICE_EUR)
                .build();
    }
}
