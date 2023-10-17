package kitchenpos.application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.exception.ProductNotFoundException;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.MenuRepository;
import kitchenpos.persistence.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
            ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(CreateMenuRequest request) {
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        setupMenuProducts(request, menu);
        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    private void setupMenuProducts(CreateMenuRequest request, Menu menu) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : request.getMenuProducts()) {
            Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(ProductNotFoundException::new);

            menuProducts.add(new MenuProduct(menu, product, menuProductRequest.getQuantity()));
        }
        menu.setupMenuProduct(menuProducts);
    }

    public List<MenuResponse> findAll() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
