package com.product.app.service;

import com.product.app.exceptions.NotFoundException;
import com.product.app.model.Product;
import com.product.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ProductService {

    ProductRepository repository;
    CurrencyService currencyService;

    public Product create(Product product) {
        return repository.save(product);
    }

    public Product findById(UUID id) {

        Product entity = repository.findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Product for the given id : %s  was not found.", id)));

        updateUsdPrice(entity);

        return repository.save(entity);
    }

    private void updateUsdPrice(Product entity) {
        BigDecimal usdPrice = currencyService.convertEurToUsd(entity.getPriceEur());
        entity.setPriceUsd(usdPrice);
    }

    public Page<Product> findAll(int pageNum, int pageSize, String sortField, String sortDirection) {
        Sort sort = handlePageSort(sortField, sortDirection);
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        return repository.findAll(pageable);
    }

    private Sort handlePageSort(String sortField, String sortDirection) {
        return sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
    }
}
