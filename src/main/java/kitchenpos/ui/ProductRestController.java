package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.ProductCreateDto;
import kitchenpos.application.dto.ProductDto;
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
    public ResponseEntity<ProductDto> create(@RequestBody final ProductCreateDto productCreateDto) {
        final ProductDto created = productService.create(productCreateDto);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(created)
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        return ResponseEntity.ok()
                .body(productService.list())
                ;
    }
}
