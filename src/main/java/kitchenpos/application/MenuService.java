package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final MenuGroupService menuGroupService;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductService menuProductService,
        final ProductRepository productRepository,
        final ProductService productService,
        final MenuGroupService menuGroupService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
        this.productRepository = productRepository;
        this.productService = productService;
        this.menuGroupService = menuGroupService;
    }

    @Transactional
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupService.findById(menuRequest.getMenuGroupId());

        Menu menu = new Menu(
            menuRequest.getName(),
            BigDecimal.valueOf(menuRequest.getPrice()),
            menuGroup);

        final List<Product> products = menuRequest.getProductIds().stream()
            .map(productService::findById)
            .collect(Collectors.toList());

        final Menu savedMenu = menuRepository.save(menu);

        savedMenu.addMenuProducts(
            menuProductService.saveAll(savedMenu, products, menuRequest.getQuantity()));

        return savedMenu;
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }

    public void checkCount(List<OrderLineItem> orderLineItems) {
        final List<Menu> menus = orderLineItems.stream()
            .map(OrderLineItem::getMenu)
            .collect(Collectors.toList());

        long count = menus.stream()
            .map(menu -> menuRepository.countById(menu.getId()))
            .reduce(0L, Long::sum);

        if (orderLineItems.size() != count) {
            throw new IllegalArgumentException();
        }
    }
}
