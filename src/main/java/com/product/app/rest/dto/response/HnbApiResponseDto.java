package com.product.app.rest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class HnbApiResponseDto {

    @JsonProperty("broj_tecajnice")
    Long id;

    @JsonProperty("datum_primjene")
    LocalDate date;

    @JsonProperty("drzava")
    String country;

    @JsonProperty("drzava_iso")
    String countryIso;

    @JsonProperty("valuta")
    String currency;

    @JsonProperty("sifra_valute")
    Long currencyId;

    @JsonProperty("kupovni_tecaj")
    String buyingExchangeRate;

    @JsonProperty("prodajni_teca")
    String sellingExchangeRate;

    @JsonProperty("srednji_tecaj")
    String middleExchangeRate;
}
