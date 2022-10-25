package kitchenpos.ui;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse productResponse = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + productResponse.getId());
        return ResponseEntity.created(uri)
            .body(productResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
            .body(productService.list());
    }
}
