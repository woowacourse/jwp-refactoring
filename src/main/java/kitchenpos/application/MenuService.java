package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(this::getMenuProduct)
                .collect(Collectors.toList());

        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);

        return menuRepository.save(menu);
    }

    private MenuProduct getMenuProduct(MenuProductRequest request) {
        return new MenuProduct(productRepository.findById(request.getProductId())
                .orElseThrow(IllegalArgumentException::new), request.getQuantity());
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
