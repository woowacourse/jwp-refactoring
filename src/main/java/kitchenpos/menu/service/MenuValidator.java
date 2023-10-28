package kitchenpos.menu.service;

import static kitchenpos.exception.ExceptionType.MENU_GROUP_NOT_FOUND;
import static kitchenpos.exception.ExceptionType.PRICE_RANGE;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.exception.CustomException;
import kitchenpos.exception.ExceptionType;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.stereotype.Component;

@Component
public class MenuValidator {

    private final MenuGroupRepository menuGroupRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    public void validate(Menu menu) {
        validateMenuGroupExists(menu.getMenuGroupId());
        validatePrice(menu.getPrice());
        validatePrice(menu.getPrice(), menu.getMenuProducts());
    }

    private void validatePrice(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = calculateProductPriceSum(menuProducts);

        if (price.compareTo(sum) > 0) {
            throw new CustomException(ExceptionType.MENU_PRICE_OVER_SUM);
        }
    }

    private BigDecimal calculateProductPriceSum(List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculatePriceSum());
        }

        return sum;
    }

    private void validatePrice(BigDecimal price) {
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomException(PRICE_RANGE, String.valueOf(price));
        }
    }

    private void validateMenuGroupExists(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new CustomException(MENU_GROUP_NOT_FOUND);
        }
    }
}
