package kitchenpos.application;

import kitchenpos.application.dto.MenuCreateRequest;
import kitchenpos.application.dto.MenuResponse;
import kitchenpos.application.dto.MenuResponse.MenuProductResponse;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Price;
import kitchenpos.domain.Product;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixture.MenuGroupFixtures.TEST_GROUP;
import static kitchenpos.fixture.ProductFixtures.PIZZA;

@Transactional
@SpringBootTest
class MenuServiceTest {

    private static final String MENU_NAME = "테스트 메뉴";
    private static final Long WRONG_ID = -1L;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Autowired
    private ProductDao productDao;

    private MenuGroup testMenuGroup;
    private Product testProduct;

    @BeforeEach
    void setup() {
        testMenuGroup = menuGroupDao.save(TEST_GROUP());
        testProduct = productDao.save(PIZZA());
    }

    @Nested
    @DisplayName("메뉴 등록 테스트")
    class CreateTest {

        @Test
        @DisplayName("메뉴 등록에 성공한다.")
        void success() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest(MENU_NAME,
                    testProduct.getPriceValue().multiply(BigDecimal.valueOf(2)),
                    testMenuGroup.getId(),
                    List.of(new MenuCreateRequest.MenuProductRequest(testProduct.getId(), 2L)));

            // when
            final MenuResponse response = menuService.create(request);

            // then
            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(response.getId()).isNotNull();
                final MenuProductResponse menuProductResponse = response.getMenuProducts().get(0);
                softly.assertThat(menuProductResponse.getMenuId()).isEqualTo(response.getId());
                softly.assertThat(menuProductResponse.getSeq()).isNotNull();
            });
        }

        @Test
        @DisplayName("존재하지 않는 메뉴그룹으로 메뉴생성시 예외가 발생한다.")
        void throwExceptionWithWrongMenuGroupId() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest(MENU_NAME,
                    testProduct.getPriceValue().multiply(BigDecimal.valueOf(2)),
                    WRONG_ID,
                    List.of(new MenuCreateRequest.MenuProductRequest(testProduct.getId(), 2L)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 상품으로 메뉴생성시 예외가 발생한다.")
        void throwExceptionWithWrongProductId() {
            // given
            final MenuCreateRequest request = new MenuCreateRequest(MENU_NAME,
                    BigDecimal.ZERO,
                    testMenuGroup.getId(),
                    List.of(new MenuCreateRequest.MenuProductRequest(WRONG_ID, 2L)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "입력값 : {0}")
        @CsvSource(value = {"-1"})
        @NullSource
        @DisplayName("잘못된 가격으로 메뉴생성시 예외를 발생시킨다.")
        void throwExceptionWithWrongPriceValue(final BigDecimal price) {
            // given
            final MenuCreateRequest request = new MenuCreateRequest(MENU_NAME,
                    price,
                    testMenuGroup.getId(),
                    List.of(new MenuCreateRequest.MenuProductRequest(testProduct.getId(), 2L)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("메뉴 가격이 상품들의 가격 합보다 크면 예외를 발생시킨다.")
        void throwExceptionWhenPriceIsOverProductPriceSum() {
            //given
            final MenuCreateRequest request = new MenuCreateRequest(MENU_NAME,
                    testProduct.getPriceValue().multiply(BigDecimal.valueOf(2)).add(BigDecimal.ONE),
                    testMenuGroup.getId(),
                    List.of(new MenuCreateRequest.MenuProductRequest(testProduct.getId(), 2L)));

            // when
            // then
            Assertions.assertThatThrownBy(() -> menuService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @DisplayName("등록됨 상품들 목록을 가져온다.")
    void getMenuList() {
        // given
        final Menu menu = new Menu.MenuFactory(MENU_NAME, new Price(BigDecimal.valueOf(1900)), testMenuGroup)
                .addProduct(testProduct, 2)
                .create();
        final Menu savedMenu = menuDao.save(menu);
        final MenuResponse expectedLastResponse = MenuResponse.from(savedMenu);

        // when
        final List<MenuResponse> response = menuService.list();

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response).isNotEmpty();
            final MenuResponse lastMenu = response.get(response.size() - 1);
            softly.assertThat(lastMenu).usingRecursiveComparison()
                    .isEqualTo(expectedLastResponse);
        });
    }
}
