package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
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
    public ResponseEntity<ProductResponse> create(
        @RequestBody final ProductRequest productRequest) {
        final ProductResponse createdProduct = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + createdProduct.getId());

        return ResponseEntity.created(uri)
            .body(createdProduct);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> products = productService.list();

        return ResponseEntity.ok()
            .body(products);
    }
}
