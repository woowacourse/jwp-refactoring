package kitchenpos.ui.v1;

import java.net.URI;
import java.util.List;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.dto.ProductCreateRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.dto.ProductUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        var response = productService.create(request);
        URI uri = URI.create("/api/v1/products/" + response.getId());
        return ResponseEntity.created(uri)
            .body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        return ResponseEntity.ok()
            .body(productService.findAll());
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> update(
        @PathVariable Long productId,
        @RequestBody ProductUpdateRequest request
    ) {
        var response = productService.update(productId, request);
        return ResponseEntity.ok()
            .body(response);
    }
}
