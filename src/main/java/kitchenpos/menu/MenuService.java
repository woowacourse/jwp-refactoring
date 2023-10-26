package kitchenpos.menu;

import kitchenpos.common.Price;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
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
    public Menu create(final MenuRequest menuProductRequests) {
        menuGroupRepository.validateContains(menuProductRequests.getMenuGroupId());

        List<MenuProduct> menuProducts = toMenuProducts(menuProductRequests.getMenuProducts());
        Price menuPrice = new Price(menuProductRequests.getPrice());

        validateNotExceeded(menuPrice, menuProducts);

        return menuRepository.save(new Menu(
                menuProductRequests.getName(),
                menuPrice,
                menuProductRequests.getMenuGroupId(),
                menuProducts
        ));
    }

    private void validateNotExceeded(Price menuPrice, List<MenuProduct> menuProducts) {
        Price menuProductsTotal = menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productRepository.getBy(menuProduct.getProductId());
                    return product.priceFor(menuProduct.getQuantity());
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

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        productRepository.validateContains(menuProductRequest.getProductId());
        return new MenuProduct(
                menuProductRequest.getProductId(),
                menuProductRequest.getQuantity()
        );
    }
}
