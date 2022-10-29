package kitchenpos.application;

import kitchenpos.application.request.menu.MenuRequest;
import kitchenpos.application.response.ResponseAssembler;
import kitchenpos.application.response.menu.MenuResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;
    private final ResponseAssembler responseAssembler;

    public MenuService(final MenuDao menuDao, final MenuGroupDao menuGroupDao, final MenuProductDao menuProductDao,
                       final ProductDao productDao, final ResponseAssembler responseAssembler) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
        this.responseAssembler = responseAssembler;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        final BigDecimal price = request.getPrice();
        validatePriceNotNegative(price);

        validateMenuGroupExist(request.getMenuGroupId());
        final List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(menuProduct -> new MenuProduct(menuProduct.getProductId(), menuProduct.getQuantity()))
                .collect(Collectors.toUnmodifiableList());
        validatePriceIsNotLowerThanTotalPriceOfProducts(price, menuProducts);

        final var menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts);
        final Menu savedMenu = menuDao.save(menu);
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return responseAssembler.menuResponse(savedMenu);
    }

    private void validatePriceIsNotLowerThanTotalPriceOfProducts(BigDecimal price, List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품 금액 합산보다 클 수 없습니다.");
        }
    }

    private void validateMenuGroupExist(Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException("메뉴 그룹을 찾을 수 없습니다.");
        }
    }

    private void validatePriceNotNegative(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴 가격은 양수여야 합니다.");
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return responseAssembler.menuResponses(menus);
    }
}
