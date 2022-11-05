package kitchenpos.product.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.response.ProductResponse;
import kitchenpos.product.presentation.dto.ProductRequest;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest productRequest) {
        ProductResponse productResponse = productService.create(productRequest.toCommand());
        final URI uri = URI.create("/api/products/" + productResponse.id());
        return ResponseEntity.created(uri)
                .body(productResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productService.list());
    }
}
