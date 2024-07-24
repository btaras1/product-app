package com.product.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static java.util.Objects.requireNonNull;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "code", nullable = false, unique = true, length = 10)
    @Size(min = 10, max = 10)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Min(value = 0)
    @Column(name = "price_eur", nullable = false)
    private BigDecimal priceEur;

    @Min(value = 0)
    @Column(name = "price_usd", nullable = false)
    private BigDecimal priceUsd;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder(toBuilder = true)
    public Product(
            String code,
            String name,
            BigDecimal priceEur,
            BigDecimal priceUsd,
            boolean isAvailable
    ) {

        this.code = requireNonNull(code, "Product code must be supplied.");;
        this.name = requireNonNull(name, "Product name must be supplied.");;
        this.priceEur = requireNonNull(priceEur, "EUR price of a product must be supplied.");;
        this.priceUsd = requireNonNull(priceUsd, "USD price of a product must be supplied.");
        this.isAvailable = isAvailable;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
}
