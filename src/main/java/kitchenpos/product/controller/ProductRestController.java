package kitchenpos.product.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.service.ProductService;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<Void> create(@RequestBody @Valid ProductCreateRequest request) {
        Long savedId = productService.create(request);
        final URI uri = URI.create("/api/products/" + savedId);
        return ResponseEntity.created(uri)
            .build();
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> list() {
        return ResponseEntity.ok()
            .body(productService.list())
            ;
    }
}
