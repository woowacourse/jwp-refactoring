package kitchenpos.presentation;

import kitchenpos.product.application.ProductService;
import kitchenpos.product.application.dto.ProductCreateDto;
import kitchenpos.product.application.dto.ProductDto;
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
    public ResponseEntity<ProductDto> create(@RequestBody final ProductCreateDto request) {
        final ProductDto response = productService.create(request);

        final URI uri = URI.create("/api/products/" + response.getId());
        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        final List<ProductDto> response = productService.list();

        return ResponseEntity.ok().body(response);
    }
}
