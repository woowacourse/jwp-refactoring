package kitchenpos.acceptance;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuProductRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import kitchenpos.order.domain.repository.OrderLineItemRepository;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.Arrays;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("classpath:/database-cleanup.sql")
public abstract class AcceptanceTest {

    @Autowired
    protected TestRestTemplate testRestTemplate;

    @Autowired
    protected OrderRepository orderRepository;

    @Autowired
    protected OrderTableRepository orderTableRepository;

    @Autowired
    protected OrderLineItemRepository orderLineItemRepository;

    @Autowired
    protected MenuRepository menuRepository;

    @Autowired
    protected MenuProductRepository menuProductRepository;

    @Autowired
    protected MenuGroupRepository menuGroupRepository;

    @Autowired
    protected ProductRepository productRepository;

    @Autowired
    protected TableGroupRepository tableGroupRepository;

    protected MenuGroup 한마리메뉴;
    protected MenuGroup 두마리메뉴;

    protected Product 후라이드치킨;
    protected Product 양념치킨;
    protected Product 간장치킨;

    protected Menu 한마리메뉴_중_후라이드치킨;
    protected Menu 두마리메뉴_중_양념치킨_간장치킨;

    protected MenuProduct 한마리메뉴_후라이드치킨;
    protected MenuProduct 두마리메뉴_양념치킨;
    protected MenuProduct 두마리메뉴_간장치킨;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup.Builder()
                .name("한마리메뉴")
                .build();
        menuGroupRepository.save(한마리메뉴);

        두마리메뉴 = new MenuGroup.Builder()
                .name("두마리메뉴")
                .build();
        menuGroupRepository.save(두마리메뉴);

        후라이드치킨 = new Product.Builder()
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(15000))
                .build();
        productRepository.save(후라이드치킨);

        양념치킨 = new Product.Builder()
                .name("양념치킨")
                .price(BigDecimal.valueOf(16000))
                .build();
        productRepository.save(양념치킨);

        간장치킨 = new Product.Builder()
                .name("간장치킨")
                .price(BigDecimal.valueOf(16000))
                .build();
        productRepository.save(간장치킨);

        한마리메뉴_후라이드치킨 = new MenuProduct.Builder()
                .menu(null)
                .productId(후라이드치킨.getId())
                .quantity(1L)
                .build();

        한마리메뉴_중_후라이드치킨 = new Menu.Builder()
                .name("후라이드치킨")
                .price(BigDecimal.valueOf(15000))
                .menuGroup(한마리메뉴)
                .menuProducts(Arrays.asList(한마리메뉴_후라이드치킨))
                .build();
        menuRepository.save(한마리메뉴_중_후라이드치킨);
        menuProductRepository.save(한마리메뉴_후라이드치킨);

        두마리메뉴_양념치킨 = new MenuProduct.Builder()
                .menu(null)
                .productId(양념치킨.getId())
                .quantity(1L)
                .build();

        두마리메뉴_간장치킨 = new MenuProduct.Builder()
                .menu(null)
                .productId(간장치킨.getId())
                .quantity(1L)
                .build();

        두마리메뉴_중_양념치킨_간장치킨 = new Menu.Builder()
                .name("양념+간장치킨")
                .price(BigDecimal.valueOf(32000))
                .menuGroup(두마리메뉴)
                .menuProducts(Arrays.asList(두마리메뉴_양념치킨, 두마리메뉴_간장치킨))
                .build();
        menuRepository.save(두마리메뉴_중_양념치킨_간장치킨);
        menuProductRepository.save(두마리메뉴_양념치킨);
        menuProductRepository.save(두마리메뉴_간장치킨);
    }
}
