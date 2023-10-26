package kitchenpos.product.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        ProductResponse product = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + product.getId())).body(product);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> products = productService.list();
        return ResponseEntity.ok().body(products);
    }
}
