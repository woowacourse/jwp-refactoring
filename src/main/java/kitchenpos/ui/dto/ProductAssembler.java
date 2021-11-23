package kitchenpos.ui.dto;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.application.dto.request.ProductRequestDto;
import kitchenpos.application.dto.response.ProductResponseDto;
import kitchenpos.ui.dto.request.ProductRequest;
import kitchenpos.ui.dto.response.ProductResponse;

public class ProductAssembler {

    private ProductAssembler() {
    }

    public static ProductRequestDto productRequestDto(ProductRequest request) {
        return new ProductRequestDto(request.getName(), request.getPrice());
    }

    public static ProductResponse productResponse(ProductResponseDto responseDto) {
        return new ProductResponse(
            responseDto.getId(),
            responseDto.getName(),
            responseDto.getPrice()
        );
    }

    public static List<ProductResponse> productsResponse(List<ProductResponseDto> responseDtos) {
        return responseDtos.stream()
            .map(ProductAssembler::productResponse)
            .collect(toList());
    }
}
