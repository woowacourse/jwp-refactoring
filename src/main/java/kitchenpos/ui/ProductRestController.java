package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.product.application.ProductService;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.product.domain.Product;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductCreateRequest product) {
        final Product created = productService.create(product);
        final ProductResponse response = ProductResponse.from(created);
        final URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        final List<ProductResponse> response = productService.list().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(response);
    }
}
