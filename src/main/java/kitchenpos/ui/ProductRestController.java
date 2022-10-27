package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductRequest;
import kitchenpos.dto.response.ProductResponse;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductRequest request) {
        final Product created = productService.create(request);

        final URI uri = URI.create("/api/products/" + created.getId());
        final ProductResponse body = ProductResponse.from(created);
        return ResponseEntity.created(uri)
                .body(body)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        final List<Product> foundProducts = productService.list();

        final List<ProductResponse> body = ProductResponse.from(foundProducts);
        return ResponseEntity.ok()
                .body(body)
                ;
    }
}
