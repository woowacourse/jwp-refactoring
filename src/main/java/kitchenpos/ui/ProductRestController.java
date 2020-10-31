package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.ui.dto.product.ProductRequest;
import kitchenpos.ui.dto.product.ProductResponse;
import kitchenpos.ui.dto.product.ProductResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest request) {
        final ProductResponse created = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + created.getId()))
                .body(created);
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductResponses> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
