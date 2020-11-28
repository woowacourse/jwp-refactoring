package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductCreateRequestDto;
import kitchenpos.product.application.dto.ProductResponseDto;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponseDto> create2(
        @RequestBody final ProductCreateRequestDto productCreateRequestDto) {
        final ProductResponseDto created = productService.create(productCreateRequestDto);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponseDto>> list() {
        return ResponseEntity.ok()
            .body(productService.list());
    }
}
