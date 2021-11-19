package kitchenpos.ui;

import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.product.ProductRequestDto;
import kitchenpos.ui.dto.response.product.ProductResponseDto;
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
    public ResponseEntity<ProductResponseDto> create(
        @RequestBody final ProductRequestDto productRequestDto) {
        final Product created = productService.create(new Product(
            productRequestDto.getName(),
            productRequestDto.getPrice()
        ));

        final ProductResponseDto responseDto =
            new ProductResponseDto(created.getId(), created.getName(), created.getPrice());

        final URI uri = URI.create("/api/products/" + created.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponseDto>> list() {
        List<Product> products = productService.list();
        List<ProductResponseDto> responseDtos = products.stream()
            .map(product ->
                new ProductResponseDto(product.getId(), product.getName(), product.getPrice())
            ).collect(toList());

        return ResponseEntity.ok()
            .body(responseDtos)
            ;
    }
}
