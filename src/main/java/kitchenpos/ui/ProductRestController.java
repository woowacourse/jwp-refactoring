package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.product.service.ProductService;
import kitchenpos.product.domain.Product;
import kitchenpos.product.service.ProductDto;
import kitchenpos.product.service.ProductMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductMapper productMapper;
    private final ProductService productService;

    public ProductRestController(ProductMapper productMapper, ProductService productService) {
        this.productMapper = productMapper;
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductDto> create(@RequestBody final ProductDto productDto) {
        final Product created = productService.create(productMapper.toEntity(productDto));
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
                             .body(ProductDto.from(created))
            ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        List<ProductDto> products = productService.list()
                                                  .stream()
                                                  .map(ProductDto::from)
                                                  .collect(toList());
        return ResponseEntity.ok()
                             .body(products)
            ;
    }
}
