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
        List<Product> products = productRepository.findAllById(productIds);
        verifyFindAllProduct(productIds, products);

        PriceValidator priceValidator = PriceValidator.of(products);
        priceValidator.validate(price, menuProductCreateInfos);

        return new Menu(name, price, menuGroupId,
            menuProductCreateInfos.stream().map(MenuProductCreateInfo::toMenuProduct).collect(
                Collectors.toList()));
    }

    private void verifyFindAllProduct(List<Long> productIds, List<Product> products) {
        if (products.size() != productIds.size()) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroupId(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }
}
