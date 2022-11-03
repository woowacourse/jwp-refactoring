package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuProducts;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuProductResponse;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productRepository;

    public MenuService(final MenuDao menuDao,
                       final MenuGroupDao menuGroupDao,
                       final ProductDao productRepository) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        menuGroupDao.findById(menuRequest.getMenuGroupId());

        final Menu menu = menuDao.save(toMenu(menuRequest));
        final MenuProducts menuProducts = menu.getMenuProducts()
                .changeAllMenuId(menu.getId());

        menu.placeMenuProducts(menuProducts);
        return MenuResponse.from(menu, MenuProductResponse.from(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        return menus.stream()
                .map(menu -> MenuResponse.from(menu, MenuProductResponse.from(menu)))
                .collect(Collectors.toList());
    }

    private Menu toMenu(MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(), menuRequest.getPrice(),
                menuRequest.getMenuGroupId(),
                new MenuProducts(toMenuProducts(menuRequest.getMenuProducts())));
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductRequest> menuProductRequests) {
        return menuProductRequests.stream()
                .map(this::toMenuProduct)
                .collect(Collectors.toList());
    }

    private MenuProduct toMenuProduct(MenuProductRequest request) {
        final Long productId = request.getProductId();
        final long quantity = request.getQuantity();
        final Price price = productRepository.findById(productId).getPrice();
        return new MenuProduct(productId, quantity, price);
    }
}
