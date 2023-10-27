package kitchenpos.menu;

import kitchenpos.common.Price;
import kitchenpos.menugroup.MenuGroupRepository;
import kitchenpos.product.ProductRepository;
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
    public Menu create(final MenuRequest menuProductRequests) {
        menuGroupRepository.validateContains(menuProductRequests.getMenuGroupId());

        return menuRepository.save(new Menu(
                menuProductRequests.getName(),
                new Price(menuProductRequests.getPrice()),
                menuProductRequests.getMenuGroupId(),
                toMenuProducts(menuProductRequests.getMenuProducts())
        ));
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest menuProductRequest) {
        return new MenuProduct(
                productRepository.getBy(menuProductRequest.getProductId()),
                menuProductRequest.getQuantity()
        );
    }
}
