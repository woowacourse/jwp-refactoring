package kitchenpos.menu.ui;

import java.net.URI;
import java.util.List;
import kitchenpos.menu.application.ProductService;
import kitchenpos.menu.ui.dto.request.ProductRequestDto;
import kitchenpos.menu.ui.dto.response.ProductResponseDto;
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
        @RequestBody final ProductRequestDto productRequestDto
    ) {
        final ProductResponseDto responseDto = productService.create(productRequestDto);

        final URI uri = URI.create("/api/products/" + responseDto.getId());
        return ResponseEntity.created(uri)
            .body(responseDto)
            ;
    }

    @GetMapping("/api/products")
    public ResponseEntity<List<ProductResponseDto>> list() {
        List<ProductResponseDto> responseDtos = productService.list();

        return ResponseEntity.ok()
            .body(responseDtos)
            ;
    }
}
