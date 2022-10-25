package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;

@Service
@Transactional(readOnly = true)
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
    public MenuResponse create(MenuRequest menuRequest) {
        BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("0원 미만으로 메뉴를 등록할 수 없습니다.");
        }

        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다.");
        }

        List<MenuProductRequest> menuProductRequests = menuRequest.getMenuProductRequests();

        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProductRequest menuProduct : menuProductRequests) {
            Product product = productDao.findById(menuProduct.getProductId())
                .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("가격은 상품들 가격 합보다 클 수 없습니다.");
        }

        Menu savedMenu = menuDao.save(menuRequest.toEntity());

        Long menuId = savedMenu.getId();
        List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (MenuProductRequest menuProductRequest : menuRequest.getMenuProductRequests()) {
            savedMenuProducts.add(menuProductDao.save(menuProductRequest.toEntity(menuId)));
        }
        savedMenu.addMenuProducts(savedMenuProducts);

        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.addMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());
    }
}
