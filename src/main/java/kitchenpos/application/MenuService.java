package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuProductCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuVerifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;
    private final MenuVerifier menuVerifier;

    public MenuService(
        MenuDao menuDao,
        MenuGroupDao menuGroupDao,
        MenuProductDao menuProductDao,
        ProductDao productDao,
        MenuVerifier menuVerifier
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
        this.menuVerifier = menuVerifier;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest menuCreateRequest) {
        List<MenuProduct> menuProducts = menuCreateRequest.getMenuProducts()
            .stream()
            .map(MenuProductCreateRequest::toEntity)
            .collect(Collectors.toList());

        Menu menu = menuDao.save(menuVerifier.toMenu(
            menuCreateRequest.getName(),
            menuCreateRequest.getPrice(),
            menuCreateRequest.getMenuGroupId(),
            menuProducts
        ));

        List<MenuProduct> savedMenuProducts = menuProducts.stream()
            .peek(it -> it.changeMenuId(menu.getId()))
            .map(menuProductDao::save)
            .collect(Collectors.toList());

        return MenuResponse.of(menu, savedMenuProducts);
    }

    public List<MenuResponse> list() {
        return menuDao.findAll()
            .stream()
            .map(it -> MenuResponse.of(it, menuProductDao.findAllByMenuId(it.getId())))
            .collect(Collectors.toList());
    }
}
