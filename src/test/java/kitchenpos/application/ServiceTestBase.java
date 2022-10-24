package kitchenpos.application;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
public class ServiceTestBase {

    @Autowired
    public ProductService productService;

    @Autowired
    public MenuGroupService menuGroupService;

    @Autowired
    public MenuService menuService;

    @Autowired
    public OrderService orderService;

    @Autowired
    public TableService tableService;

    @Autowired
    public TableGroupService tableGroupService;

    @Autowired
    public ProductDao productDao;

    @Autowired
    public MenuGroupDao menuGroupDao;

    @Autowired
    public MenuDao menuDao;

    @Autowired
    public OrderDao orderDao;

    @Autowired
    public OrderTableDao orderTableDao;

    @Autowired
    public MenuProductDao menuProductDao;

    public Product 상품_생성(final ProductFixture productFixture) {
        return productDao.save(productFixture.toEntity());
    }

    public List<MenuGroup> 메뉴_그룹_목록_생성() {
        return Arrays.stream(MenuGroupFixture.values())
                .map(it -> menuGroupDao.save(it.toEntity()))
                .collect(Collectors.toList());
    }

    public List<MenuProduct> 메뉴_상품_목록(final Product... products) {
        return Arrays.stream(products)
                .map(this::메뉴_상품)
                .collect(Collectors.toList());
    }

    public MenuProduct 메뉴_상품(final Product product) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(null);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(3);
        return menuProduct;
    }

    public List<MenuProduct> 메뉴_상품_목록_생성(final Product... products) {
        return 메뉴_상품_목록_생성(3, products);
    }

    public List<MenuProduct> 메뉴_상품_목록_생성(final long quantity, final Product... products) {
        return Arrays.stream(products)
                .map(it -> 메뉴_상품_생성(it, quantity))
                .collect(Collectors.toList());
    }

    public MenuProduct 메뉴_상품_생성(final Product product, final long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(null);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }

    public OrderLineItem 주문_항목(final Long menuId) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(menuId);
        orderLineItem.setQuantity(1);
        return orderLineItem;
    }

    public OrderTable 주문_테이블_생성() {
        OrderTable orderTable = new OrderTable();
        orderTable.setEmpty(false);
        orderTable.setNumberOfGuests(3);
        return orderTableDao.save(orderTable);
    }
}
