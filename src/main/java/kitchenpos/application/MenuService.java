package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuProductRequest;
import kitchenpos.application.dto.MenuRequest;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuProductResponse;
import kitchenpos.ui.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(MenuDao menuDao, MenuGroupDao menuGroupDao, MenuProductDao menuProductDao, ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    public MenuResponse create(MenuRequest menuRequest) {
        BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProductRequest : menuProductRequests) {
            Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuDao.save(menuRequest.toEntity());

        List<MenuProduct> menuProducts = menuProductRequests.stream()
                .map(menuProductRequest -> new MenuProduct(savedMenu.getId(), menuProductRequest.getProductId(),
                        menuProductRequest.getQuantity()))
                .collect(Collectors.toList());

        List<MenuProduct> savedMenuProducts = menuProducts.stream()
                .map(menuProductDao::save)
                .collect(Collectors.toList());

        return new MenuResponse(savedMenu.getId(), savedMenu.getName(), savedMenu.getPrice(),
                savedMenu.getMenuGroupId(), mapToMenuProductResponses(savedMenuProducts));
    }

    private List<MenuProductResponse> mapToMenuProductResponses(List<MenuProduct> savedMenuProducts) {
        return savedMenuProducts.stream()
                .map(menuProduct -> new MenuProductResponse(menuProduct.getSeq(), menuProduct.getMenuId(),
                        menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuDao.findAll();

        return menus.stream()
                .map(this::mapToMenuResponse)
                .collect(Collectors.toList());
    }

    private MenuResponse mapToMenuResponse(Menu menu) {
        List<MenuProduct> menuProducts = menuProductDao.findAllByMenuId(menu.getId());
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.getMenuGroupId(),
                mapToMenuProductResponses(menuProducts));
    }
}
