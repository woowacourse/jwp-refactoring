package kitchenpos.product.presentation;

import java.net.URI;
import java.util.List;
import kitchenpos.product.dto.reqeust.ProductCreateRequest;
import kitchenpos.product.dto.response.ProductResponse;
import kitchenpos.product.dto.response.ProductsResponse;
import kitchenpos.product.service.ProductService;
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
        ProductResponse response = productService.create(request);
        URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        ProductsResponse response = productService.list();
        return ResponseEntity.ok().body(response.getProductResponses());
    }
}
