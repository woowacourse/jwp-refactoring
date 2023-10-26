package kitchenpos.support;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.request.MenuProductRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
public class MenuSupportController {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;

    public MenuSupportController(MenuRepository menuRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
    }

    @PutMapping("/api/menus/{menuId}")
    public ResponseEntity<Void> updateMenu(@PathVariable Long menuId, @RequestBody CreateMenuRequest request) {
        Menu menu = Menu.builder()
                .id(menuId)
                .name(request.getName())
                .price(request.getPrice())
                .menuGroupId(request.getMenuGroupId())
                .menuProducts(request.getMenuProducts()
                        .stream()
                        .map(this::toMenuProduct)
                        .collect(Collectors.toSet()))
                .build();
        menuRepository.save(menu);
        return ResponseEntity.ok().build();
    }

    private MenuProduct toMenuProduct(MenuProductRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));

        return MenuProduct.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }
}
