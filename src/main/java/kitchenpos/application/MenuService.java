package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.domain.Price;
import kitchenpos.persistence.ProductRepository;
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
    public Menu create(final MenuRequest menu) {
        MenuGroup menuGroup = menuGroupRepository.findById(menu.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        return menuRepository.save(new Menu(
                menu.getName(),
                new Price(menu.getPrice()),
                menuGroup,
                toMenuProducts(menu.getMenuProducts())
        ));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProducts) {
        return menuProducts.stream()
                .map(it -> new MenuProduct(
                        productRepository.findById(it.getProductId())
                                .orElseThrow(IllegalArgumentException::new),
                        it.getQuantity()
                ))
                .collect(Collectors.toList());
    }
}
