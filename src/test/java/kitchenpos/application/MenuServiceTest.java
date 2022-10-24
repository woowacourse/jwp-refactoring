package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;

@Transactional
@SpringBootTest
class MenuServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    MenuGroup menuGroup;
    Product product1;
    Product product2;

    @BeforeEach
    void setUp() {
        MenuGroup newMenuGroup = new MenuGroup("두마리메뉴");
        menuGroup = menuGroupDao.save(newMenuGroup);

        Product newProduct1 = new Product("후라이드", new BigDecimal(16_000));
        product1 = productDao.save(newProduct1);
        Product newProduct2 = new Product("양념치킨", new BigDecimal(16_000));
        product2 = productDao.save(newProduct2);
    }

    @DisplayName("메뉴를 생성한다")
    @Nested
    class CreateTest {

        @DisplayName("메뉴를 생성하면 ID를 할당된 Menu객체가 반환된다")
        @Test
        void create() {
            BigDecimal price = new BigDecimal(30_000);
            Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, menuGroup.getId(),
                    List.of(new MenuProduct(1L, product1.getId(), 1), new MenuProduct(1L, product2.getId(), 1)));

            Menu actual = menuService.create(menu);
            assertThat(actual.getId()).isNotNull();
        }

        @DisplayName("가격이 Null일 경우 예외가 발생한다")
        @Test
        void throwExceptionWithNullPrice() {
            BigDecimal price = null;
            Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, 1L,
                    List.of(new MenuProduct(1L, 1L, 1), new MenuProduct(1L, 2L, 1)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("가격이 음수인 경우 예외가 발생한다")
        @Test
        void throwExceptionWithNegativePrice() {
            BigDecimal price = new BigDecimal(-1);
            Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, 1L,
                    List.of(new MenuProduct(1L, 1L, 1), new MenuProduct(1L, 2L, 1)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다")
        @Test
        void throwExceptionWithNotExistMenuGroup() {
            Long notExistMenuGroupId = 0L;
            BigDecimal price = new BigDecimal(30_000);
            Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, notExistMenuGroupId,
                    List.of(new MenuProduct(1L, 1L, 1), new MenuProduct(1L, 2L, 1)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 그룹이 존재하지 않을 경우 예외가 발생한다")
        @Test
        void throwExceptionWithNotExistProduct() {
            Long notExistProductId = 0L;
            BigDecimal price = new BigDecimal(30_000);
            Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, 1L,
                    List.of(new MenuProduct(1L, notExistProductId, 2)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("메뉴 가격이 메뉴를 이루는 가격보다 크면 예외가 발생한다")
        @Test
        void throwExceptionWithTooBigPrice() {
            // product1: 후라이드 16000원, product2: 양념치킨 16000원 => 단일 상품 가격 총합: 32000원
            BigDecimal price = new BigDecimal(40_000);
            Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, 1L,
                    List.of(new MenuProduct(1L, 1L, 1), new MenuProduct(1L, 2L, 1)));

            assertThatThrownBy(() -> menuService.create(menu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @DisplayName("존재하는 모든 메뉴 목록을 조회한다")
    @Test
    void list() {
        BigDecimal price = new BigDecimal(30_000);
        Menu menu = new Menu("후라이드, 양념치킨 2마리 세트", price, menuGroup.getId(),
                List.of(new MenuProduct(1L, product1.getId(), 1), new MenuProduct(1L, product2.getId(), 1)));
        menuService.create(menu);

        List<Menu> list = menuService.list();

        assertThat(list).hasSize(1);
    }
}
