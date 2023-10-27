package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.request.MenuCreateRequest;
import kitchenpos.menu.application.dto.response.MenuResponse;
import kitchenpos.menu.application.mapper.MenuMapper;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuPrice;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        final Menu menu = mapToMenu(menuCreateRequest);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuMapper.mapToResponse(savedMenu);
    }

    private Menu mapToMenu(final MenuCreateRequest menuCreateRequest) {
        final MenuGroup menuGroup = findMenuGroup(menuCreateRequest.getMenuGroupId());
        final MenuProducts menuProducts = new MenuProducts(menuCreateRequest.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(findProduct(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList()));
        return new Menu(menuCreateRequest.getName(), MenuPrice.of(menuCreateRequest.getPrice()),
                menuGroup, menuProducts);
    }

    private MenuGroup findMenuGroup(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private Product findProduct(final Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
