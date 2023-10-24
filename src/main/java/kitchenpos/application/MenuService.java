package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.CreateMenuRequest;
import kitchenpos.ui.dto.MenuProductDto;
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
    public Menu create(final CreateMenuRequest menuRequest) {
        final BigDecimal price = menuRequest.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupDao.existsById(menuRequest.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        final List<Product> products = new ArrayList<>();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductDto menuProductDto : menuRequest.getMenuProductDtos()) {
            final Product product = productDao.findById(menuProductDto.getProductId())
                                              .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductDto.getQuantity())));
            products.add(product);
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final MenuGroup menuGroup = menuGroupDao.findById(menuRequest.getMenuGroupId())
                                                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 그룹입니다."));
        final Menu menu = new Menu(menuGroup, menuRequest.getName(), menuRequest.getPrice());
        final Menu savedMenu = menuDao.save(menu);

        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProductDto menuProductDto : menuRequest.getMenuProductDtos()) {
            // TODO: 2023/10/24 일급컬렉션으로 변경 후 일급컬렉션에서 조회하기
            final Product product = products.stream()
                                            .filter(savedProduct -> savedProduct.getId()
                                                                                .equals(menuProductDto.getProductId()))
                                            .findFirst()
                                            .orElseThrow(() -> new IllegalArgumentException("잘못된 상품 아이디입니다."));
            final MenuProduct menuProduct = new MenuProduct(savedMenu, product, menuProductDto.getQuantity());
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
