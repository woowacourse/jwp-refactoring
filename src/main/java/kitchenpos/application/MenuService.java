package kitchenpos.application;

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
        final BigDecimal price = menu.getPrice(); // 받은 menu 의 가격을 가져옴, 이것은 request 로 들어오는 것

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) { // 가격이 null 이거나 0 미만으로 들어오는 것은 실패
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menu.getMenuGroupId())) { // 존재하지 않는 menu id 로 들어오는 경우 안 됨
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = menu.getMenuProducts(); // menu product 로 product 와 quantity 를 이용함

        BigDecimal sum = BigDecimal.ZERO; // 0 원으로 설정을 진행해놓고
        for (final MenuProduct menuProduct : menuProducts) {
            final Product product = productDao.findById(menuProduct.getProductId()) // 존재하지 않는 상품이면 안됨
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity()))); // 현재 menu product 의 총 가격을 더해줌
        }

        if (price.compareTo(sum) > 0) { // price 가 더 큰 경우, Illegal Argument Exception 발생, 즉 단일 상품보다 가격이 더 나가서는 안됨
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuDao.save(menu); // 모두다 성공적이면 menuDao 를 통해서 저장을 진행함

        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }
        savedMenu.setMenuProducts(savedMenuProducts); // 이것은 menu products 를 등록하는 과정, 즉 바로 접근이 불가능하니까

        return savedMenu;
    }

    public List<Menu> list() { // list 역시
        final List<Menu> menus = menuDao.findAll();

        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }
}
