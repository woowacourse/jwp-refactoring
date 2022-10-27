package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.dto.response.MenuResponse.MenuProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<MenuProductRequest> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu menu = new Menu();
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        menu.setMenuGroupId(request.getMenuGroupId());
        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProductResponse> menuProductResponses = menuProducts.stream()
                .map(it -> {
                    final MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setMenuId(savedMenu.getId());
                    menuProduct.setQuantity(it.getQuantity());
                    menuProduct.setProductId(it.getProductId());
                    return menuProduct;
                }).map(menuProductDao::save)
                .map(it -> new MenuProductResponse(it.getSeq(), it.getMenuId(), it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuResponse(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getMenuGroupId(),
                menuProductResponses
        );
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        final List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            final List<MenuProductResponse> menuProductResponses = menuProductDao.findAllByMenuId(menu.getId())
                    .stream()
                    .map(it -> new MenuProductResponse(it.getSeq(), it.getMenuId(), it.getProductId(),
                            it.getQuantity()))
                    .collect(Collectors.toList());
            final MenuResponse menuResponse = new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                    menu.getMenuGroupId(), menuProductResponses);
            menuResponses.add(menuResponse);
        }
        return menuResponses;
    }
}
