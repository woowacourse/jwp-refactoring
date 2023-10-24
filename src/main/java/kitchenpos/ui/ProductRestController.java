package kitchenpos.ui;

import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.CreateProductRequest;
import kitchenpos.ui.dto.CreateProductResponse;
import kitchenpos.ui.dto.ReadProductResponse;
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
    public ResponseEntity<CreateProductResponse> create(@RequestBody final CreateProductRequest productRequest) {
        final Product created = productService.create(productRequest);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(CreateProductResponse.from(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ReadProductResponse>> list() {
        final List<ReadProductResponse> productResponses = productService.list()
                                                                         .stream()
                                                                         .map(ReadProductResponse::from)
                                                                         .collect(Collectors.toUnmodifiableList());
        return ResponseEntity.ok()
                             .body(productResponses);
    }
}
