package kitchenpos.ui.product;

import kitchenpos.application.product.ProductService;
import kitchenpos.application.product.dto.ProductCreateRequest;
import kitchenpos.domain.Product;
import kitchenpos.ui.product.dto.ProductResponse;
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
    public ResponseEntity<ProductResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        Product product = productService.create(productCreateRequest);
        URI uri = URI.create("/api/products/" + product.getId());

        return ResponseEntity.created(uri)
                .body(ProductResponse.from(product));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> response = productService.list()
                .stream()
                .map(ProductResponse::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(response);
    }
}
