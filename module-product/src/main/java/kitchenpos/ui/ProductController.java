package kitchenpos.ui;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.ProductInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    ResponseEntity<ProductInfo> saveProduct(@RequestBody ProductInfo productInfo) {
        Product product = productService.create(productInfo.getName(), productInfo.getPrice());
        return ResponseEntity.created(URI.create("/api/products/" + product.id())).build();
    }

    @GetMapping
    ResponseEntity<List<ProductInfo>> getProducts() {
        List<Product> list = productService.list();
        List<ProductInfo> result = list.stream()
                .map(ProductInfo::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}
