package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.application.dto.menu.MenuRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.application.dto.menu.ProductQuantityDto;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuDao menuDao;
    private final MenuGroupDao menuGroupDao;
    private final MenuProductDao menuProductDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuDao menuDao,
            final MenuGroupDao menuGroupDao,
            final MenuProductDao menuProductDao,
            final ProductDao productDao
    ) {
        this.menuDao = menuDao;
        this.menuGroupDao = menuGroupDao;
        this.menuProductDao = menuProductDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        // TODO: 2023-10-23 1. request 받도록 수정
        // TODO: 2023-10-23 2. 비즈니스 로직 도메인으로 이동 + 단위테스트 작성
        // TODO: 2023-10-23 3. JPA 적용
        final BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId());

        final List<MenuProduct> menuProducts = getProductsInMenuRequest(menuRequest.getMenuProducts());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu);

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            // TODO: 2023-10-23 request 받아오게 + JPA로 리팩터링하면서 Menu가지는 MenuProduct 만들기
            final MenuProduct menuProductToSave = new MenuProduct(menuId, menuProduct.getProductId(), menuProduct.getQuantity());
            savedMenuProducts.add(menuProductDao.save(menuProductToSave));
        }
        savedMenu.addMenuProducts(savedMenuProducts);

        return MenuResponse.from(menu);
    }

    private List<MenuProduct> getProductsInMenuRequest(final List<ProductQuantityDto> requests) {
        return requests.stream()
            .map(request -> new MenuProduct(null, request.getProductId(), request.getQuantity()))
            .collect(Collectors.toList());
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
