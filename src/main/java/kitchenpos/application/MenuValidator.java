package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Price;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Products;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final ProductRepository productRepository;

    public MenuValidator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void validate(Price menuPrice, List<MenuProduct> menuProducts) {
        Products products = new Products(productRepository.findAllByIdIn(collectProductId(menuProducts)));
        validateAmount(menuPrice, products.calculateAmount(toMap(menuProducts)));
    }

    private Map<Long, Long> toMap(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .collect(Collectors.toMap(MenuProduct::getProductId, MenuProduct::getQuantity));
    }

    private void validateAmount(Price menuPrice, Price amount) {
        if (menuPrice.isGreaterThan(amount)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품의 가격보다 높습니다.");
        }
    }

    private List<Long> collectProductId(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }
}
