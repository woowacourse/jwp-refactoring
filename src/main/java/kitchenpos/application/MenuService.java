package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public Menu create(final Menu menu) {
        final BigDecimal price = menu.getPrice();

        // 메뉴 가격은 null이거나 음수일 수 없다
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        // 요청바디로 전달된 메뉴 그룹 아이디는 반드시 존재해야 한다
        // 즉 메뉴 추가시, 기존 메뉴 그룹에 반드시 속해야 한다
        if (!menuGroupDao.existsById(menu.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        // 메뉴를 이루는 프로덕트를 리스트로 받는다
        final List<MenuProduct> menuProducts = menu.getMenuProducts();

        // TODO MenuProduct 리스트가 null이거나 사이즈 0이면 안된다고 검증해야할 것 같다

        // 프로덕트 리스트를 순회하며 총액을 계산한다
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        // compareTo : -1 less than, 0 equal to, 1 greater than
        // 실제 프로덕트 가격의 총합보다 더 비싸게 받을 순 없다
        // 단, 묶어서 파는거니까 더 싸게 받을 순 있나보다.
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        // 메뉴에 대한 유효성 검증 완료. 저장한다. 메뉴 아이디가 생긴다.
        final Menu savedMenu = menuDao.save(menu);

        // 생성된 메뉴 아이디를 이용해 프로덕트들에 메뉴 아이디를 할당하고 저장해서 반환한다.
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    public List<Menu> list() {
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
