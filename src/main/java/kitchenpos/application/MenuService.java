package kitchenpos.application;

import java.util.ArrayList;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            MenuDao menuDao,
            MenuGroupDao menuGroupDao,
            MenuProductDao menuProductDao,
            ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(MenuCreateRequest request) {
        BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        List<MenuProductCreateRequest> menuProducts = request.getMenuProducts();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductCreateRequest menuProductRequest : menuProducts) {
            Product product = productDao.findById(menuProductRequest.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        Menu savedMenu = menuDao.save(request.toEntity()); // TODO: Repository

        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductCreateRequest menuProductRequest : menuProducts) {
            MenuProduct menuProduct = menuProductRequest.toEntity(savedMenu.getId());
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }

        return MenuResponse.of(savedMenu, savedMenuProducts);
    }

    public List<MenuResponse> list() {
        List<Menu> menusWithMenuProduct = menuDao.findAll() // TODO: Repository
                .stream()
                .map(menu -> new Menu(
                        menu.getId(),
                        menu.getName(),
                        menu.getPrice(),
                        menu.getMenuGroupId(),
                        menuProductDao.findAllByMenuId(menu.getId())
                )).collect(Collectors.toList());

        return menusWithMenuProduct.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
