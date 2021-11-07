package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.createMenuProduct;
import static kitchenpos.fixture.MenuFixture.createMenuRequest;
import static kitchenpos.fixture.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixture.ProductFixture.createProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@Sql("classpath:db/test/truncate.sql")
@ActiveProfiles("test")
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MenuService menuService;
    private Long menuGroupId;
    private List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        menuGroupId = menuGroupDao.save(createMenuGroup()).getId();
        Product product = productDao.save(createProduct(1L));
        menuProducts = Arrays.asList(
                createMenuProduct(1L, null, product.getId(), 1),
                createMenuProduct(2L, null, product.getId(), 1)
        );
    }

    @DisplayName("메뉴 생성")
    @Nested
    class CreateMenu {

        @DisplayName("메뉴를 생성한다.")
        @Test
        void create() {
            MenuRequest menu = createMenuRequest(menuGroupId, menuProducts);
            MenuResponse savedMenu = menuService.create(menu);
            assertAll(
                    () -> assertThat(savedMenu.getId()).isNotNull(),
                    () -> assertThat(savedMenu.getName()).isEqualTo(menu.getName()),
                    () -> assertThat(savedMenu.getPrice().compareTo(menu.getPrice())).isEqualTo(0),
                    () -> assertThat(savedMenu.getMenuGroupId()).isEqualTo(menu.getMenuGroupId()),
                    () -> assertThat(savedMenu.getMenuProducts()).hasSize(menu.getMenuProducts().size())
            );
        }

        @DisplayName("메뉴 가격은 음수일 수 없다.")
        @Test
        void createWithInvalidPrice1() {
            BigDecimal price = BigDecimal.valueOf(-1);
            MenuRequest menu = createMenuRequest(price, menuGroupId, menuProducts);
            assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 상품 가격의 합보다 큰 가격으로 메뉴를 생성할 수 없다.")
        @Test
        void createWithInvalidPrice2() {
            MenuRequest menu = createMenuRequest(menuGroupId, menuProducts);
            BigDecimal invalidPrice = sumOfProductPrice(menu.getMenuProducts()).add(BigDecimal.ONE);
            assertThatThrownBy(() -> menuService.create(createMenuRequest(invalidPrice, menuGroupId, menuProducts)))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("존재하지 않는 메뉴 그룹에 메뉴를 생성할 수 없다.")
        @Test
        void createWithInvalidMenuGroup() {
            Long invalidMenuGroupId = Long.MAX_VALUE;
            MenuRequest menu = createMenuRequest(invalidMenuGroupId, menuProducts);
            assertThatThrownBy(() -> menuService.create(menu)).isInstanceOf(IllegalArgumentException.class);
        }

        private BigDecimal sumOfProductPrice(List<MenuProduct> menuProducts) {
            BigDecimal sum = BigDecimal.ZERO;
            for (MenuProduct menuProduct : menuProducts) {
                Product product = productDao.findById(menuProduct.getProductId()).get();
                sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
            }
            return sum;
        }
    }

    @DisplayName("메뉴 목록을 출력한다.")
    @Test
    void list() {
        MenuResponse menu1 = menuService.create(createMenuRequest(menuGroupId, menuProducts));
        MenuResponse menu2 = menuService.create(createMenuRequest(menuGroupId, menuProducts));

        List<MenuResponse> list = menuService.list();
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).usingRecursiveComparison().isEqualTo(Arrays.asList(menu1, menu2))
        );
    }
}
