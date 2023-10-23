package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
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
        Menu menu = menuRepository.save(Menu.of(request.getName(), request.getPrice(), menuGroup,
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
