package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.application.dto.request.MenuProductRequest;
import kitchenpos.application.dto.request.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuRepository menuRepository, ProductRepository productRepository, MenuGroupRepository menuGroupRepository,
                       final MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        validateMenuGroupIsNotNull(menuRequest);
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                        .orElseThrow(IllegalArgumentException::new);

        final Menu menu = menuRepository.save(
                new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup)
        );

        final List<MenuProduct> savedMenuProducts = menuRequest.getMenuProducts().stream()
                .map(menuProductRequest -> toMenuProduct(menu, menuProductRequest))
                .collect(Collectors.toList());

        validateMenuProductPriceSumIsLessThanMenu(menu, savedMenuProducts);
        return menu;
    }

    private void validateMenuProductPriceSumIsLessThanMenu(final Menu menu, final List<MenuProduct> savedMenuProducts) {
        final BigDecimal menuProductPriceSum = savedMenuProducts.stream()
                .map(MenuProduct::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (menu.isMoreThanPrice(menuProductPriceSum)) {
            throw new IllegalArgumentException();
        }
    }

    private void validateMenuGroupIsNotNull(MenuRequest menuRequest) {
        if (menuRequest.getMenuGroupId() == null) {
            throw new IllegalArgumentException();
        }
    }

    private MenuProduct toMenuProduct(final Menu menu, MenuProductRequest menuProductRequest) {
        Product product = productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        final MenuProduct menuProduct = new MenuProduct(menu, product, menuProductRequest.getQuantity());
        return menuProductRepository.save(menuProduct);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
