package kitchenpos.product.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.ui.request.ProductCreateRequest;
import kitchenpos.product.ui.response.ProductResponse;
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
    public ResponseEntity<ProductResponse> create(
            @Valid @RequestBody final ProductCreateRequest request
    ) {
        final var response = productService.create(request);
        return ResponseEntity.created(URI.create("/api/products/" + response.getId())).body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        final var response = productService.list();
        return ResponseEntity.ok(response);
    }
}
