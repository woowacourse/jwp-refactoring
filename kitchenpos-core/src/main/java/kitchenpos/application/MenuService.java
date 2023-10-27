package kitchenpos.application;

import kitchenpos.domain.dto.MenuRequest;
import kitchenpos.domain.dto.MenuRequest.MenuProductRequest;
import kitchenpos.domain.dto.MenuResponse;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.domain.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
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
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));

        final MenuProducts menuProducts = convertMenuProducts(request);

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    private MenuProducts convertMenuProducts(final MenuRequest request) {
        final List<MenuProductRequest> menuProductRequests = request.getMenuProducts();

        final List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProduct -> new MenuProduct(productRepository.findById(menuProduct.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다.")),
                        menuProduct.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
