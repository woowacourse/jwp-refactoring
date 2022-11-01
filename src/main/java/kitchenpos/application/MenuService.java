package kitchenpos.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.Product;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.repository.menu.MenuGroupRepository;
import kitchenpos.repository.menu.MenuProductRepository;
import kitchenpos.repository.menu.MenuRepository;
import kitchenpos.repository.menu.ProductRepository;
import kitchenpos.specification.MenuSpecification;
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
        menu.mapMenuProducts(menuProducts);
        menu.mapMenuGroup(menuGroup);

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
