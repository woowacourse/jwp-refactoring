package kitchenpos.product.ui;

import java.net.URI;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductInformationRequest;
import kitchenpos.product.application.dto.ProductRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.application.dto.ProductResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
        final ProductResponse response = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<ProductResponses> list() {
        final ProductResponses responses = productService.list();
        return ResponseEntity.ok()
                .body(responses);
    }

    @PutMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponse> update(@PathVariable final Long productId,
                                                  @RequestBody final ProductInformationRequest request) {
        return ResponseEntity.ok(productService.update(productId, request));
    }
}
