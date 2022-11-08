package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.product.repository.ProductRepository;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupRepository.findById(menuRequest.getMenuGroupId());
        final Menu menu = menuRepository.save(toMenu(menuRequest));
        return MenuResponse.from(menu, MenuProductResponse.from(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(menu -> MenuResponse.from(menu, MenuProductResponse.from(menu)))
                .collect(Collectors.toList());
    }

    private Menu toMenu(MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                new MenuProducts(toMenuProducts(menuRequest.getMenuProducts())));
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest request) {
        final Long productId = request.getProductId();
        final long quantity = request.getQuantity();
        final Price price = productRepository.findById(productId).getPrice();
        return new MenuProduct(productId, quantity, price);
    }
}
