package kitchenpos.core.presentation.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.core.application.ProductService;
import kitchenpos.core.domain.product.Product;
import kitchenpos.core.dto.request.ProductCreateRequest;
import kitchenpos.core.dto.response.ProductResponse;
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
        final Product created = productService.create(request);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(ProductResponse.from(created))
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        final List<Product> products = productService.list();
        return ResponseEntity.ok()
                .body(ProductResponse.of(products))
                ;
    }
}
