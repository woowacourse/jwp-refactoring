package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.request.product.ProductRequestDto;
import kitchenpos.application.dto.response.product.ProductResponseDto;
import kitchenpos.ui.dto.request.product.ProductRequest;
import kitchenpos.ui.dto.response.product.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductRequestDto productRequestDto =
            new ProductRequestDto(productRequest.getName(), productRequest.getPrice());
        ProductResponseDto productResponseDto = productService.create(productRequestDto);
        ProductResponse productResponse = convert(productResponseDto);
        URI uri = URI.create("/api/products/" + productResponseDto.getId());
        return ResponseEntity.created(uri)
            .body(productResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> productResponses = productService.list()
            .stream()
            .map(this::convert)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(productResponses);
    }

    private ProductResponse convert(ProductResponseDto productResponseDto) {
        return new ProductResponse(productResponseDto.getId(), productResponseDto.getName(), productResponseDto.getPrice());
    }
}
