package kitchenpos.application;

import kitchenpos.application.dto.MenuRequest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static kitchenpos.application.dto.MenuRequest.MenuProductRequest;

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
    public Menu create(MenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재 해야합니다"));
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        setupMenuProducts(request, menu);
        return menuRepository.save(menu);
    }

    private void setupMenuProducts(MenuRequest request, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
        }
        menu.changeMenuProducts(menuProducts);
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
