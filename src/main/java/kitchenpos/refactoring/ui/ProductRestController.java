package kitchenpos.refactoring.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.refactoring.application.ProductService;
import kitchenpos.refactoring.application.dto.ProductRequest;
import kitchenpos.refactoring.application.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final ProductResponse created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());

        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {

        return ResponseEntity.ok().body(productService.list());
    }
}
