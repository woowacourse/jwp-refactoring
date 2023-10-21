package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ProductService;
import kitchenpos.application.dto.CreateProductCommand;
import kitchenpos.application.dto.domain.ProductDto;
import kitchenpos.domain.product.Product;
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
    public ResponseEntity<ProductDto> create(@RequestBody final CreateProductCommand request) {
        final Product created = productService.create(request);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                .body(ProductDto.from(created))
                ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        final List<ProductDto> productDtos = productService.list().stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(productDtos)
                ;
    }

}
