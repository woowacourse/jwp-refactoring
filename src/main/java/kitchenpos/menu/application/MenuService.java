package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.product.exception.ProductNotFoundException;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    @Transactional
    public MenuResponse create(MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
        Menu menu = menuRepository.save(Menu.of(request.getName(), request.getPrice(), menuGroup.getId(),
                Collections.emptyList()));

        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(dto -> menuProductRepository.save(new MenuProduct(menu, productRepository.findById(dto.getProductId())
                        .orElseThrow(ProductNotFoundException::new), dto.getQuantity())))
                .collect(toList());

        menu.changeMenuProducts(menuProducts);

        return MenuResponse.from(menu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAllMenusFetchMenuGroup();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
