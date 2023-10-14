package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Transactional
@SpringBootTest
class MenuServiceTest {

    private static final String MENU_NAME = "테스트 메뉴";
    private static final Long WRONG_ID = -1L;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup testMenuGroup;
    private Product testProduct;

    @BeforeEach
    void setup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("테스트 그룹");
        testMenuGroup = menuGroupDao.save(menuGroup);

        final Product product = new Product();
        product.setName("테스트 상품");
        product.setPrice(BigDecimal.valueOf(1000));
        testProduct = productDao.save(product);
    }

    private MenuProduct getMenuProductForRequest() {
        final MenuProduct requestMenuProduct = new MenuProduct();
        requestMenuProduct.setProductId(testProduct.getId());
        requestMenuProduct.setQuantity(2);
        return requestMenuProduct;
    }

    private Menu getMenuForRequest(MenuProduct requestMenuProduct) {
        final Menu reqeustMenu = new Menu();
        reqeustMenu.setName(MENU_NAME);
        reqeustMenu.setPrice(BigDecimal.valueOf(1900));
        reqeustMenu.setMenuGroupId(testMenuGroup.getId());
        reqeustMenu.setMenuProducts(List.of(requestMenuProduct));
        return reqeustMenu;
    }

    @Nested
    @DisplayName("메뉴 등록 테스트")
    class CreateTest {

        @Test
        @DisplayName("메뉴 등록에 성공한다.")
        void success() {
            // given
            final MenuProduct requestMenuProduct = getMenuProductForRequest();

            final Menu reqeustMenu = getMenuForRequest(requestMenuProduct);

            // when
            final Menu createdMenu = menuService.create(reqeustMenu);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(createdMenu.getId()).isNotNull();
                final MenuProduct createdMenuProduct = createdMenu.getMenuProducts().get(0);
                softly.assertThat(createdMenuProduct.getMenuId()).isEqualTo(createdMenu.getId());
                softly.assertThat(createdMenuProduct.getSeq()).isNotNull();
            });
        }

        @Test
        @DisplayName("존재하지 않는 메뉴그룹으로 메뉴생성시 예외가 발생한다.")
        void throwExceptionWithWrongMenuGroupId() {
            // given
            final MenuProduct requestMenuProduct = getMenuProductForRequest();

            final Menu reqeustMenu = getMenuForRequest(requestMenuProduct);
            reqeustMenu.setMenuGroupId(WRONG_ID);

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(reqeustMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 상품으로 메뉴생성시 예외가 발생한다.")
        void throwExceptionWithWrongProductId() {
            // given
            final MenuProduct requestMenuProduct = getMenuProductForRequest();
            requestMenuProduct.setProductId(WRONG_ID);

            final Menu reqeustMenu = getMenuForRequest(requestMenuProduct);

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(reqeustMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "입력값 : {0}")
        @CsvSource(value = {"-1"})
        @NullSource
        @DisplayName("잘못된 가격으로 메뉴생성시 예외를 발생시킨다.")
        void throwExceptionWithWrongPriceValue(final BigDecimal price) {
            // given
            final MenuProduct requestMenuProduct = getMenuProductForRequest();

            final Menu reqeustMenu = getMenuForRequest(requestMenuProduct);
            reqeustMenu.setPrice(price);

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(reqeustMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 가격이 상품들의 가격 합보다 크면 예외를 발생시킨다.")
        void throwExceptionWhenPriceIsOverProductPriceSum() {
            //given
            final MenuProduct requestMenuProduct = getMenuProductForRequest();
            final BigDecimal price = testProduct.getPrice()
                    .multiply(BigDecimal.valueOf(requestMenuProduct.getQuantity()))
                    .add(BigDecimal.ONE);

            final Menu reqeustMenu = getMenuForRequest(requestMenuProduct);
            reqeustMenu.setPrice(price);

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(reqeustMenu))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("등록됨 상품들 목록을 가져온다.")
    void getMenuList() {
        // given
        final MenuProduct requestMenuProduct = getMenuProductForRequest();
        final Menu reqeustMenu = getMenuForRequest(requestMenuProduct);
        menuService.create(reqeustMenu);

        // when
        final List<Menu> actualMenus = menuService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actualMenus).isNotEmpty();
            final Menu createdMenu = actualMenus.get(actualMenus.size() - 1);
            softly.assertThat(createdMenu.getName()).isEqualTo(reqeustMenu.getName());
        });
    }
}
