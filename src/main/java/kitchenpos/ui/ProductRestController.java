package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.ProductCreateRequest;
import kitchenpos.dto.response.ProductCreateResponse;
import kitchenpos.dto.response.ProductResponse;
import kitchenpos.dto.mapper.ProductDtoMapper;
import kitchenpos.dto.mapper.ProductMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductRestController {

    private final ProductMapper productMapper;
    private final ProductDtoMapper productDtoMapper;
    private final ProductService productService;

    public ProductRestController(final ProductMapper productMapper, final ProductDtoMapper productDtoMapper,
                                 final ProductService productService) {
        this.productMapper = productMapper;
        this.productDtoMapper = productDtoMapper;
        this.productService = productService;
    }

    @PostMapping("/api/products")
    public ResponseEntity<ProductCreateResponse> create(@RequestBody final ProductCreateRequest productCreateRequest) {
        Product product = productMapper.toProduct(productCreateRequest);
        Product product1 = productService.create(product);
        ProductCreateResponse created = productDtoMapper.toProductCreateResponse(product1);
        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponse>> list() {
        List<Product> products = productService.list();
        return ResponseEntity.ok().body(productDtoMapper.toProductResponses(products));
    }
}
