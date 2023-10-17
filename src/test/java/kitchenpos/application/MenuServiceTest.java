package kitchenpos.application;

import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.JdbcTemplateProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SuppressWarnings("NonAsciiCharacters")
@Import({
        MenuService.class,
        JdbcTemplateMenuDao.class,
        JdbcTemplateMenuGroupDao.class,
        JdbcTemplateMenuProductDao.class,
        JdbcTemplateProductDao.class
})
class MenuServiceTest extends ServiceTest {

    @Autowired
    private JdbcTemplateMenuDao menuDao;

    @Autowired
    private JdbcTemplateMenuGroupDao menuGroupDao;

    @Autowired
    private JdbcTemplateMenuProductDao menuProductDao;

    @Autowired
    private JdbcTemplateProductDao productDao;

    @Autowired
    private MenuService menuService;

    private MenuGroup 두마리메뉴;
    private MenuProduct 후라이드_한마리;
    private MenuProduct 양념치킨_한마리;
    private MenuProduct 간장치킨_한마리;

    @BeforeEach
    void setUp() {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("두마리메뉴");
        두마리메뉴 = menuGroupDao.save(menuGroup);

        Product product1 = new Product();
        product1.setName("후라이드");
        product1.setPrice(BigDecimal.valueOf(16000));
        Product 후라이드 = productDao.save(product1);

        Product product2 = new Product();
        product2.setName("양념치킨");
        product2.setPrice(BigDecimal.valueOf(16000));
        Product 양념치킨 = productDao.save(product2);

        Product product3 = new Product();
        product3.setName("간장치킨");
        product3.setPrice(BigDecimal.valueOf(16000));
        Product 간장치킨 = productDao.save(product3);

        후라이드_한마리 = new MenuProduct();
        후라이드_한마리.setProductId(후라이드.getId());
        후라이드_한마리.setQuantity(1);

        양념치킨_한마리 = new MenuProduct();
        양념치킨_한마리.setProductId(양념치킨.getId());
        양념치킨_한마리.setQuantity(1);

        간장치킨_한마리 = new MenuProduct();
        간장치킨_한마리.setProductId(간장치킨.getId());
        간장치킨_한마리.setQuantity(1);
    }

    @DisplayName("메뉴를 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        Menu expected = new Menu();
        expected.setName("후라이드1+양념1");
        expected.setPrice(BigDecimal.valueOf(32000L));
        expected.setMenuGroupId(두마리메뉴.getId());
        expected.setMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리));

        // when
        Menu actual = menuService.create(expected);

        // then
        assertSoftly(softly -> {
            softly.assertThat(menuDao.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getName()).isEqualTo(expected.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(expected.getPrice());
            softly.assertThat(actual.getMenuGroupId()).isEqualTo(expected.getMenuGroupId());
            softly.assertThat(actual.getMenuProducts()).
                    usingRecursiveComparison()
                    .comparingOnlyFields("productId", "quantity")
                    .isEqualTo(expected.getMenuProducts());
        });
    }

    @DisplayName("메뉴 등록 시 메뉴 가격이 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void create_FailWithNegativeMenuPrice(long invalidMenuPrice) {
        // given
        Menu invalidMenu = new Menu();
        invalidMenu.setName("후라이드1+양념1");
        invalidMenu.setPrice(BigDecimal.valueOf(invalidMenuPrice));
        invalidMenu.setMenuGroupId(두마리메뉴.getId());
        invalidMenu.setMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 가격이 상품 총 가격보다 큰 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {33000L, Long.MAX_VALUE})
    void create_FailWithInvalidMenuPrice(long invalidMenuPrice) {
        // given
        Menu invalidMenu = new Menu();
        invalidMenu.setName("후라이드1+양념1");
        invalidMenu.setPrice(BigDecimal.valueOf(invalidMenuPrice));
        invalidMenu.setMenuGroupId(두마리메뉴.getId());
        invalidMenu.setMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리));

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidMenuGroupId() {
        // given
        long invalidMenuGroupId = 1000L;

        Menu invalidMenu = new Menu();
        invalidMenu.setName("후라이드1+양념1");
        invalidMenu.setPrice(BigDecimal.valueOf(32000L));
        invalidMenu.setMenuGroupId(invalidMenuGroupId);

        // when & then
        assertThatThrownBy(() -> menuService.create(invalidMenu))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        Menu menu1 = new Menu();
        menu1.setName("후라이드1+양념1");
        menu1.setPrice(BigDecimal.valueOf(32000L));
        menu1.setMenuGroupId(두마리메뉴.getId());
        menu1.setMenuProducts(List.of(후라이드_한마리, 양념치킨_한마리));

        Menu menu2 = new Menu();
        menu2.setName("간장1+양념1");
        menu2.setPrice(BigDecimal.valueOf(32000L));
        menu2.setMenuGroupId(두마리메뉴.getId());
        menu2.setMenuProducts(List.of(간장치킨_한마리, 양념치킨_한마리));

        Menu 후1양1_메뉴 = menuService.create(menu1);
        Menu 간1양1_메뉴 = menuService.create(menu2);

        // when
        List<Menu> list = menuService.list();

        // then
        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list).usingRecursiveComparison()
                    .isEqualTo(List.of(후1양1_메뉴, 간1양1_메뉴));
        });
    }
}
