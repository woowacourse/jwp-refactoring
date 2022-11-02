package kitchenpos.ui.jpa;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.ui.jpa.dto.product.ProductCreateRequest;
import kitchenpos.ui.jpa.dto.product.ProductCreateResponse;
import kitchenpos.ui.jpa.dto.product.ProductListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestControllerJpa {

    private final ProductService productService;

    public ProductRestControllerJpa(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductCreateResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        ProductCreateResponse productCreateResponse = productService.create(productCreateRequest);
        final URI uri = URI.create("/api/products/" + productCreateResponse.getId());
        return ResponseEntity.created(uri).body(productCreateResponse);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductListResponse>> list() {
        return ResponseEntity.ok().body(productService.list());
    }
}
