package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.presentation.dto.request.MenuRequest;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuProductRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menu.repository.ProductRepository;
import kitchenpos.menu.specification.MenuSpecification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final MenuSpecification menuSpecification;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductRepository menuProductRepository,
                       ProductRepository productRepository,
                       MenuSpecification menuSpecification) {

        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
        this.menuSpecification = menuSpecification;
    }

    @Transactional
    public Menu create(MenuRequest request) {

        Menu menu = createMenu(request);

        menuSpecification.validateCreate(menu, request);

        return menuRepository.save(menu);
    }

    private Menu createMenu(MenuRequest request) {

        List<MenuProduct> menuProducts = createMenuProducts(request);
        Long menuGroupId = request.getMenuGroupId();
        MenuGroup menuGroup = menuGroupRepository.findById(menuGroupId).orElse(null);

        Menu menu = request.toDomain();
        menu.mapMenuGroup(menuGroup);
        menu.mapMenuProducts(menuProducts);

        return menu;
    }

    private List<MenuProduct> createMenuProducts(MenuRequest request) {

        List<Long> productIds = request.productIds();

        List<Product> products = productRepository.findAllByIdIn(productIds);

        return request.getMenuProducts().stream()
                .flatMap(menuPrdReq -> products.stream()
                        .filter(product -> menuPrdReq.getProductId().equals(product.getId()))
                        .map(product -> new MenuProduct(product, menuPrdReq.getQuantity()))
                ).collect(toList());
    }

    public List<Menu> list() {

        return menuRepository.findAllWithMenuProduct();
    }
}
