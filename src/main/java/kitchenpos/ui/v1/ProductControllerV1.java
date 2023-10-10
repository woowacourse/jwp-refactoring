package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductControllerV1 {

    private final ProductService productService;

    public ProductControllerV1(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        Product created = productService.create(product);
        URI uri = URI.create("/api/v1/products/" + created.getId());
        return ResponseEntity.created(uri)
            .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        return ResponseEntity.ok()
            .body(productService.list());
    }
}
