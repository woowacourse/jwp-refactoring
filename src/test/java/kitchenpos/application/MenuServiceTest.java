package kitchenpos.application;

import static kitchenpos.application.kitchenposFixture.메뉴그룹만들기;
import static kitchenpos.application.kitchenposFixture.메뉴상품만들기;
import static kitchenpos.application.kitchenposFixture.상품만들기;
import static kitchenpos.application.kitchenposFixture.저장할메뉴만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import javax.sql.DataSource;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

@DataJdbcTest
@Import({MenuGroupService.class, ProductService.class, JdbcTemplateMenuGroupDao.class, JdbcTemplateMenuProductDao.class})
class MenuServiceTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductDao productDao;

    private MenuService menuService;

    @BeforeEach
    void setUp() {
        this.menuService = new MenuService(
                new JdbcTemplateMenuDao(dataSource),
                new JdbcTemplateMenuGroupDao(dataSource),
                new JdbcTemplateMenuProductDao(dataSource),
                productDao
        );
    }

    @Test
    @DisplayName("이름과 가격, 메뉴 그룹의 식별자 그리고 메뉴에서 제공하는 상품들의 이름과 가격을 제공하여 메뉴를 저장할 수 있다.")
    void givenToMakeMenu(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final Menu menu = 저장할메뉴만들기("메뉴!", "4000", menuGroupId, menuProduct, menuProduct2);
        final Menu savedMenu = menuService.create(menu);

        assertThat(savedMenu).isNotNull();
        assertThat(savedMenu.getId())
                .as("식별자는 주어진 값과 무관하게 할당받는다.")
                .isNotEqualTo(menu.getId());
        assertThat(savedMenu.getMenuProducts().get(0).getSeq())
                .as("메뉴 상품의 순서는 주어진 값과 무관하게 할당받는다.")
                .isNotEqualTo(menuProduct.getSeq());
    }

    @Test
    @DisplayName("메뉴의 가격이 비어있거나 음수이면 안된다.")
    void invalidPrice(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final Menu menu = new Menu();
        menu.setId(9987L);
        menu.setName("메뉴!");
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(
                List.of(
                        menuProduct, menuProduct2
                )
        );

        menu.setPrice(null);
        assertThatThrownBy(() -> menuService.create(menu))
                .as("메뉴의 가격이 비어있다면 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);

        menu.setPrice(new BigDecimal("-40000"));
        assertThatThrownBy(() -> menuService.create(menu))
                .as("메뉴의 가격이 음수일 때에도 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴그룹의 식별자는 존재하는 식별자여야 한다.")
    void invalidMenuGroupId(@Autowired ProductService productService) {
        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final Menu menu = 저장할메뉴만들기("메뉴!", "4000", 0L, menuProduct, menuProduct2);

        assertThatThrownBy(() -> menuService.create(menu))
                .as("존재하지 않는 메뉴 그룹에 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("메뉴에서 제공하는 상품의 가격 총 합계가 메뉴 가격보다 작으면 안된다. (메뉴 가격 =< 각 상품의 가격 합계)")
    void invalidMenuPrice(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final Menu menu = 저장할메뉴만들기("메뉴!", "20001", menuGroupId, menuProduct, menuProduct2);

        assertThatThrownBy(() -> menuService.create(menu))
                .as("메뉴에 있는 각 상품의 금액 총합보다 메뉴 금액이 크다면 저장할 수 없다.")
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("조건에 맞다면 메뉴 정보를 저장한 후, 메뉴의 각 상품 정보를 저장한다.")
    void successfullySaved(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService,
            @Autowired MenuProductDao menuProductDao
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final Menu menu = 저장할메뉴만들기("메뉴!", "4000", menuGroupId, menuProduct, menuProduct2);
        final Menu savedManu = menuService.create(menu);

        assertThat(menuProductDao.findAllByMenuId(savedManu.getId()))
                .as("메뉴를 저장하면 메뉴 상품도 따라 저장된다.")
                .hasSize(2);
    }

    @Test
    @DisplayName("이름은 255자까지 표현할 수 있다.")
    void invalidName(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "4000", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "4000", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 4L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1L);

        final Menu menu = new Menu();
        menu.setId(9987L);
        menu.setPrice(new BigDecimal("4000"));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(
                List.of(
                        menuProduct, menuProduct2
                )
        );

        menu.setName("메".repeat(256)); // 255자를 초과하는 이름
        assertThatThrownBy(() -> menuService.create(menu))
                .as("255자를 초과하는 이름의 메뉴를 저장할 수 없다.")
                .isInstanceOf(DataIntegrityViolationException.class);

        menu.setName("메".repeat(255));
        assertThatCode(() -> menuService.create(menu))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("가격은 소수점 2자리를 포함해 총 19자리까지 표현할 수 있다.")
    void invalidPriceSize(
            @Autowired MenuGroupService menuGroupService,
            @Autowired ProductService productService
    ) {
        // given : 메뉴 그룹
        final Long menuGroupId = 메뉴그룹만들기(menuGroupService).getId();

        // given : 개별 상품
        final Product savedProduct = 상품만들기("상품 1", "12345123451234512", productService);
        final Product savedProduct2 = 상품만들기("상품 2", "12345123451234512", productService);

        // given : 메뉴상품
        final MenuProduct menuProduct = 메뉴상품만들기(savedProduct, 1000L);
        final MenuProduct menuProduct2 = 메뉴상품만들기(savedProduct2, 1000L);

        final Menu menu = new Menu();
        menu.setId(9987L);
        menu.setName("메뉴입니다!");
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(List.of(menuProduct, menuProduct2));

        menu.setPrice(new BigDecimal("123451234512345123.12"));
        assertThatThrownBy(() -> menuService.create(menu))
                .as("19자리를 초과하는 가격의 메뉴를 저장할 수 없다.")
                .isInstanceOf(DataIntegrityViolationException.class);

        menu.setPrice(new BigDecimal("12345123451234512.12"));
        assertThatCode(() -> menuService.create(menu))
                .doesNotThrowAnyException();
    }
}
