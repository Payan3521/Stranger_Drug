package com.desarrollox.backend_stranger_drug.api_publicaciones.controller.webMapper;

import java.util.List;

import org.springframework.stereotype.Component;
import com.desarrollox.backend_stranger_drug.api_publicaciones.controller.PriceDto;
import com.desarrollox.backend_stranger_drug.api_publicaciones.models.PricePost;

@Component
public class PriceWebMapper {
    public PriceDto toPriceDto(PricePost pricePost){
        PriceDto priceDto = new PriceDto();
        priceDto.setCodeCountry(pricePost.getCodeCountry());
        priceDto.setCountry(pricePost.getCountry());
        priceDto.setAmount(pricePost.getAmount());
        priceDto.setCurrency(pricePost.getCurrency());
        return priceDto;
    }

    public List<PriceDto> toPriceDtoList(List<PricePost> precios) {
        return precios.stream()
            .map(this::toPriceDto)
            .toList();
    }
}
