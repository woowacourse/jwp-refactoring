package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductCreateRequest;
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
    public ResponseEntity<Product> create(@RequestBody ProductCreateRequest request) {
        Product savedProduct = productService.create(request);
        URI uri = URI.create("/api/products/" + savedProduct.getId());

        return ResponseEntity.created(uri)
                .body(savedProduct);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<Product>> list() {
        List<Product> response = productService.list();
        return ResponseEntity.ok()
                .body(response);
    }
}
