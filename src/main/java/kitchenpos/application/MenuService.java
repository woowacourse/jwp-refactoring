package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
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
    public MenuResponse create(final MenuRequest request) {
        MenuGroup menuGroup = getMenuGroup(request);
        List<MenuProduct> menuProducts = getMenuProducts(request);

        Menu menu = new Menu(
                request.getName(),
                request.getPrice(),
                menuGroup,
                menuProducts
        );

        Menu savedMenu = menuRepository.save(menu);
        return new MenuResponse(savedMenu);
    }

    private MenuGroup getMenuGroup(final MenuRequest request) {
        return menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
    }

    private List<MenuProduct> getMenuProducts(final MenuRequest request) {
        return request.getMenuProducts()
                .stream()
                .map(menuProductRequest -> new MenuProduct(
                        getProduct(menuProductRequest),
                        menuProductRequest.getQuantity()
                ))
                .collect(Collectors.toList());
        // TODO: N+1 문제 발생
    }

    private Product getProduct(final MenuProductRequest menuProductRequest) {
        return productRepository.findById(menuProductRequest.getProductId())
                .orElseThrow(IllegalArgumentException::new);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::new)
                .collect(Collectors.toList());
    }
}
