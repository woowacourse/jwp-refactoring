package kitchenpos.ui;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.application.ProductService;
import kitchenpos.dto.product.ProductCreateRequest;
import kitchenpos.dto.product.ProductCreateResponse;
import kitchenpos.dto.product.ProductFindAllResponses;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductCreateResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        final ProductCreateResponse created = productService.create(productCreateRequest);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created)
            ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductFindAllResponses> findAll() {
        return ResponseEntity.ok()
            .body(productService.findAll())
            ;
    }
}
