package kitchenpos.ui;

import kitchenpos.application.dtos.ProductRequest;
import kitchenpos.application.ProductService;
import kitchenpos.application.dtos.ProductResponse;
import kitchenpos.application.dtos.ProductResponses;
import kitchenpos.domain.Product;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        final Product created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(new ProductResponse(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductResponses> list() {
        final List<Product> products = productService.list();
        return ResponseEntity.ok()
                .body(new ProductResponses(products));
    }
}
