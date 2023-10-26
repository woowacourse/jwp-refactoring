package kitchenpos.product.presentation;

import java.net.URI;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.dto.ProductsResponse;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductCreateRequest request) {
        ProductResponse response = productService.create(request);
        final URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductsResponse> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }
}
