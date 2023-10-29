package kitchenpos.ui;

import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.application.dto.ProductCreateRequest;
import kitchenpos.menu.application.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductCreateRequest request) {
        final Long id = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + id)).build();
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> findAll() {
        final List<ProductResponse> productResponses = productService.findAll();
        return ResponseEntity.ok().body(productResponses);
    }
}
