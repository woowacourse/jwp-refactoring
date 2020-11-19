package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class MenuCreateService {
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuCreateService(MenuGroupRepository menuGroupRepository, ProductRepository productRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    public Menu createMenu(String name, BigDecimal price, Long menuGroupId,
        List<MenuProductCreateInfo> menuProductCreateInfos) {
        validateMenuGroupId(menuGroupId);
        List<Long> productIds = menuProductCreateInfos.stream()
            .map(MenuProductCreateInfo::getProductId)
            .collect(Collectors.toList());
        PriceValidator priceValidator = PriceValidator.of(productRepository.findAllById(productIds));
        priceValidator.validate(price, menuProductCreateInfos);

        return new Menu(name, price, menuGroupId,
            menuProductCreateInfos.stream().map(MenuProductCreateInfo::toMenuProduct).collect(
                Collectors.toList()));
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
