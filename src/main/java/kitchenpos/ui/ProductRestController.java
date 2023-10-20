package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.application.request.ProductCreateRequest;
import kitchenpos.application.response.ProductResponse;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        final ProductResponse productResponse = productService.create(productCreateRequest);
        final URI uri = URI.create("/api/products/" + productResponse.getId());
        return ResponseEntity.created(uri)
                .body(productResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
