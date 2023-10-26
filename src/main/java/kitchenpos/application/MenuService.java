package kitchenpos.application;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.product.Product;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import kitchenpos.ui.dto.MenuProductRequest;
import kitchenpos.ui.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest menu) {
        MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        Price menuPrice = new Price(menu.getPrice());
        List<MenuProduct> menuProducts = toMenuProducts(menu.getMenuProducts());
        validateNotExceeded(menuPrice, menuProducts);
        return menuRepository.save(new Menu(
                menu.getName(),
                menuPrice,
                menu.getMenuGroupId(),
                menuProducts
        ));
    }

    private void validateNotExceeded(Price menuPrice, List<MenuProduct> menuProducts) {
        Price menuProductsTotal = menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productRepository.findById(menuProduct.getProductId()).orElseThrow(IllegalArgumentException::new);
                    return product.getPrice().multiply(menuProduct.getQuantity());
                })
                .reduce(Price.ZERO, Price::add);

        if (menuPrice.isGreaterThan(menuProductsTotal)) {
            throw new IllegalArgumentException("메뉴 가격은 메뉴 상품 합계를 넘을 수 없습니다");
        }
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProduct) {
        productRepository.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
        return new MenuProduct(
                menuProduct.getProductId(),
                menuProduct.getQuantity()
        );
    }
}
