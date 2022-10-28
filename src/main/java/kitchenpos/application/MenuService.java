package kitchenpos.application;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.dto.menu.CreateMenuRequest;

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
    public Menu create(final CreateMenuRequest request) {

        final MenuGroup menuGroup = findMenuGroupById(request.getMenuGroupId());

        final Map<Product, Long> menuProducts = request.getMenuProducts().stream()
            .collect(Collectors.toMap(
                menuProduct -> findProductById(menuProduct.getProductId()),
                menuProduct -> menuProduct.getQuantity())
            );

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);
        return menuDao.save(menu);
    }

    public List<Menu> list() {
        return menuDao.findAll();
    }

    private MenuGroup findMenuGroupById(Long id) {
        return menuGroupDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

    private Product findProductById(Long id) {
        return productDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다."));
    }
}
