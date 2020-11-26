package kitchenpos.product.application;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kitchenpos.product.application.dto.ProductCreateRequest;
import kitchenpos.product.application.dto.ProductResponse;
import kitchenpos.product.domain.Product;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ProductRestController {
    private final ProductService productService;

    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody final ProductCreateRequest productCreateRequest) {
        final Product created = productService.create(productCreateRequest.toRequestEntity());
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
            .body(ProductResponse.from(created));
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<ProductResponse> productResponses = productService.list()
            .stream()
            .map(ProductResponse::from)
            .collect(Collectors.toList());
        return ResponseEntity.ok()
            .body(productResponses);
    }
}
