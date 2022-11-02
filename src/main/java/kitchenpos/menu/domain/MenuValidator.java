package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;

@Component
public class MenuValidator {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuValidator(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public void validateCreateMenu(
        final String name,
        final BigDecimal price,
        final Long menuGroupId,
        final List<MenuProductDto> menuProducts) {
        validateName(name);
        validatePrice(price);
        validateMenuGroupById(menuGroupId);
        validateProperPrice(price, menuProducts);
    }

    private void validateName(final String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("메뉴의 이름은 비어있을 수 없습니다.");
        }
    }

    private void validatePrice(final BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException("메뉴의 가격은 비어있을 수 없습니다.");
        }

        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격은 0원 미만일 수 없습니다.");
        }
    }

    private void validateMenuGroupById(final Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

    private void validateProperPrice(final BigDecimal price, final List<MenuProductDto> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
            .map(MenuProductDto::getProductId)
            .collect(Collectors.toList());

        final Map<Long, BigDecimal> productsPriceMap = productRepository.findAllByIdIn(productIds).stream()
            .collect(Collectors.toMap(it -> it.getId(), it -> it.getPrice()));

        if (productIds.size() != productsPriceMap.size()) {
            throw new IllegalArgumentException("존재하지 않는 제품입니다.");
        }

        final BigDecimal totalPrice = menuProducts.stream()
            .map(it -> productsPriceMap.get(it.getProductId()).multiply(BigDecimal.valueOf(it.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (price.compareTo(totalPrice) > 0) {
            throw new IllegalArgumentException("각 상품 가격의 합보다 큰 가격을 적용할 수 없습니다.");
        }
    }

}
