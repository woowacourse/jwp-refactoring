package kitchenpos.menu.domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.product.domain.ProductPrice;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.product.domain.Products;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(ProductRepository productRepository,
                         MenuGroupRepository menuGroupRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(long menuGroupId, MenuPrice menuPrice, List<MenuProduct> menuProducts) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        validate(menuPrice, menuProducts);
    }

    public void validate(MenuPrice menuPrice, List<MenuProduct> menuProducts) {
        Products products = new Products(productRepository.findAllByIdIn(collectProductId(menuProducts)));
        validateAmount(menuPrice, products.calculateAmount(toMap(menuProducts)));
    }

    private List<Long> collectProductId(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    private Map<Long, Long> toMap(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .collect(Collectors.toMap(MenuProduct::getProductId, MenuProduct::getQuantity));
    }

    private void validateAmount(MenuPrice menuPrice, ProductPrice amount) {
        if (menuPrice.isGreaterThan(amount)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품의 가격보다 높습니다.");
        }
    }
}
