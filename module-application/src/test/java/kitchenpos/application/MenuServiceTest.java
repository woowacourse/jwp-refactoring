package kitchenpos.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.application.dto.MenuResponse.MenuProductResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.menu.application.MenuService;
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
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    private MenuGroup testMenuGroup;
    private Product testProduct;

    @BeforeEach
    void setup() {
        testMenuGroup = menuGroupRepository.save(TEST_GROUP());
        testProduct = productRepository.save(PIZZA());
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
        final Menu menu = new Menu(MENU_NAME, new Price(BigDecimal.valueOf(1900)), testMenuGroup, List.of(new MenuProduct(testMenuGroup.getId(), 2L)));
        final Menu savedMenu = menuRepository.save(menu);
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
