package kitchenpos.ui;

import kitchenpos.application.MenuProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/menu-products")
public class MenuProductsController {

    private final MenuProductService menuProductService;

    public MenuProductsController(MenuProductService menuProductService) {
        this.menuProductService = menuProductService;
    }

    @PostMapping("/{menuId}/{productId}")
    public ResponseEntity<Long> create(
            @PathVariable("menuId") Long menuId,
            @PathVariable("productId") Long productId,
            @RequestParam("quantity") Long quantity
            ) {
        Long menuProductId = menuProductService.create(menuId, productId, quantity);
        return ResponseEntity.ok(menuProductId);
    }

}
