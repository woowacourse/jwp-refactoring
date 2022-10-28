package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.request.MenuCreateRequest;
import kitchenpos.application.request.MenuCreateRequest.MenuProductCreateRequest;
import kitchenpos.dao.menu.MenuDao;
import kitchenpos.dao.menu.MenuGroupDao;
import kitchenpos.dao.product.ProductDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public Menu create(final MenuCreateRequest request) {
        if (!menuGroupDao.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹의 id입니다.");
        }
        return menuDao.save(new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                mapToMenuProduct(request.getMenuProducts()))
        );
    }

    private List<MenuProduct> mapToMenuProduct(final List<MenuProductCreateRequest> menuProducts) {
        return menuProducts.stream()
                .map(menuProduct -> {
                    Product product = productDao.getById(menuProduct.getProductId());
                    return new MenuProduct(menuProduct.getProductId(), menuProduct.getQuantity(), product.getPrice());
                })
                .collect(Collectors.toList());
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }
}
