package kitchenpos.menu.application;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuQuantity;
import kitchenpos.menu.exception.InvalidMenuPriceException;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.exception.ProductNotFoundException;
import kitchenpos.menu.ui.request.MenuProductRequest;
import kitchenpos.menu.ui.request.MenuRequest;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(
        MenuGroupRepository menuGroupRepository,
        ProductRepository productRepository
    ) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateMenu(MenuRequest menuRequest) {
        validateExistMenuGroup(menuRequest.getMenuGroupId());
        validateMenuPrice(new MenuPrice(menuRequest.getPrice()), menuRequest.getMenuProducts());
    }

    private void validateExistMenuGroup(Long menuGroupId) {
        if (menuGroupRepository.existsById(menuGroupId)) {
            return;
        }

        throw new MenuGroupNotFoundException(String.format("%d ID를 가진 MenuGroup이 존재하지 않습니다.", menuGroupId));
    }

    private void validateMenuPrice(MenuPrice menuPrice, List<MenuProductRequest> menuProductRequests) {
        MenuPrice totalPrice = new MenuPrice(BigDecimal.ZERO);

        for (MenuProductRequest request : menuProductRequests) {
            Product product = findProductById(request.getProductId());
            MenuQuantity menuQuantity = new MenuQuantity(request.getQuantity());
            totalPrice = totalPrice.add(menuQuantity.multiplyPrice(product.getPrice()));
        }

        if (menuPrice.isBiggerThan(totalPrice)) {
            throw new InvalidMenuPriceException(
                String.format("메뉴의 가격 %s이 상품 가격의 합 %s보다 큽니다.", menuPrice, totalPrice)
            );
        }
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new ProductNotFoundException(
                String.format("%d ID를 가진 Product가 존재하지 않습니다.", productId))
            );
    }
}
