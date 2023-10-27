package kitchenpos.product.ui;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.request.ProductCreateRequest;
import kitchenpos.response.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(final ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        Product created = productService.create(request.getName(), request.getPrice());
        URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri).body(ProductResponse.from(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> response = productService.list().stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }
}
