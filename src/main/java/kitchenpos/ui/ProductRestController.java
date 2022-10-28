package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.ui.apiservice.ProductApiService;
import kitchenpos.ui.dto.ProductRequest;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductApiService productApiService;

    public ProductRestController(final ProductApiService productApiService) {
        this.productApiService = productApiService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@RequestBody ProductRequest productRequest) {
        ProductResponse response = productApiService.create(productRequest);
        URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri)
                .body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok()
                .body(productApiService.list());
    }
}
