package kitchenpos.ui;

import java.util.stream.Collectors;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.request.ProductRequest;
import kitchenpos.ui.response.ProductResponse;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest request) {
        final Product created = productService.create(request.getName(), request.getPrice());
        final ProductResponse response = new ProductResponse(created.getId(), created.getName(), created.getPrice());
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> products = productService.list().stream()
                .map(p -> new ProductResponse(p.getId(), p.getName(), p.getPrice()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(products);
    }
}
