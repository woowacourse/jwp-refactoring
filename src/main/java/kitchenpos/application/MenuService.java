package kitchenpos.application;

import kitchenpos.application.dto.request.MenuCreateRequest;
import kitchenpos.application.dto.response.MenuResponse;
import kitchenpos.application.mapper.MenuMapper;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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

    public Menu mapToMenu(final MenuCreateRequest menuCreateRequest) {
        final MenuGroup menuGroup = findMenuGroup(menuCreateRequest.getMenuGroupId());
        final MenuProducts menuProducts = new MenuProducts(menuCreateRequest.getMenuProducts()
                .stream()
                .map(it -> new MenuProduct(findProduct(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList()));
        return new Menu(menuCreateRequest.getName(), new Price(menuCreateRequest.getPrice()),
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
