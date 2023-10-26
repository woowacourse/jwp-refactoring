package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductDto;
import kitchenpos.mapper.ProductMapper;
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
    public ResponseEntity<ProductDto> create(@RequestBody final ProductDto productDto) {
        final Product created = productService.create(ProductMapper.toEntity(productDto));
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(ProductMapper.toDto(created))
            ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        List<ProductDto> products = productService.list()
                                                  .stream()
                                                  .map(ProductMapper::toDto)
                                                  .collect(toList());
        return ResponseEntity.ok()
                             .body(products)
            ;
    }
}
