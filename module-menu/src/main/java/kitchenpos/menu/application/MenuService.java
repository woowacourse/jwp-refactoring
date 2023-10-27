package kitchenpos.menu.application;

import kitchenpos.common.domain.Price;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final String menuName = request.getName();
        final Price menuPrice = new Price(request.getPrice());
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final List<MenuProduct> menuProducts = extractMenuProducts(request);

        validate(menuPrice, menuProducts);

        final Menu menu = new Menu(menuName, menuPrice, menuGroup, menuProducts);
        return MenuResponse.from(menuRepository.save(menu));
    }

    private List<MenuProduct> extractMenuProducts(final MenuCreateRequest request) {
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();
        final List<MenuProduct> menuProducts = new ArrayList<>();
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            menuProducts.add(new MenuProduct(menuProductRequest.getQuantity(), menuProductRequest.getProductId()));
        }
        return menuProducts;
    }

    private void validate(final Price menuPrice, final List<MenuProduct> menuProducts) {
        Price sum = new Price(BigDecimal.ZERO);
        for (final MenuProduct menuProduct : menuProducts) {
            final Price productPrice = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new)
                    .getPrice();
            final Price menuProductPrice = productPrice.multiply(BigDecimal.valueOf(menuProduct.getQuantity()));
            sum = sum.add(menuProductPrice);
        }
        if (menuPrice.isLargerThan(sum)) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
