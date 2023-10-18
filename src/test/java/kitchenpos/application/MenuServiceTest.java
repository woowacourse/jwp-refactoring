package kitchenpos.application;

import static kitchenpos.test.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.test.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.test.ServiceTest;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuService menuService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Nested
    class 메뉴_목록_조회_시 {

        @Test
        void 모든_메뉴_목록을_조회한다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuRequest menuRequestA = new MenuRequest(
                    "기본 텐동",
                    BigDecimal.valueOf(11000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1))
            );
            MenuRequest menuRequestB = new MenuRequest(
                    "할인 텐동",
                    BigDecimal.valueOf(10000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1))
            );
            MenuResponse menuResponseA = menuService.create(menuRequestA);
            MenuResponse menuResponseB = menuService.create(menuRequestB);

            //when
            List<MenuResponse> menus = menuService.list();

            //then
            assertThat(menus).usingRecursiveComparison()
                    .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
                    .isEqualTo(List.of(menuResponseA, menuResponseB));
        }

        @Test
        void 메뉴가_존재하지_않으면_목록이_비어있다() {
            //given, when
            List<MenuResponse> menus = menuService.list();

            //then
            assertThat(menus).isEmpty();
        }
    }

    @Nested
    class 메뉴_추가_시 {

        @Test
        void 정상적인_메뉴라면_메뉴를_추가한다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuRequest menuRequest = new MenuRequest(
                    "텐동",
                    BigDecimal.valueOf(11000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1))
            );

            //when
            MenuResponse menuResponse = menuService.create(menuRequest);

            //then
            assertSoftly(softly -> {
                softly.assertThat(menuResponse.getId()).isNotNull();
                softly.assertThat(menuResponse.getName()).isEqualTo("텐동");
                softly.assertThat(menuResponse.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(11000));
                softly.assertThat(menuResponse.getMenuGroupResponse().getId()).isEqualTo(menuGroup.getId());
                softly.assertThat(menuResponse.getMenuGroupResponse().getName()).isEqualTo(menuGroup.getName());
                softly.assertThat(menuResponse.getMenuProducts()).hasSize(1);
                softly.assertThat(menuResponse.getMenuProducts().get(0).getProduct().getId())
                        .isEqualTo(product.getId());
                softly.assertThat(menuResponse.getMenuProducts().get(0).getProduct().getName())
                        .isEqualTo(product.getName());
                softly.assertThat(menuResponse.getMenuProducts().get(0).getProduct().getPrice())
                        .isEqualByComparingTo(product.getPrice());
                softly.assertThat(menuResponse.getMenuProducts().get(0).getQuantity()).isOne();
            });
        }

        @Test
        void 가격이_NULL이면_예외를_던진다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuRequest menuRequest = new MenuRequest(
                    "텐동",
                    null,
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1))
            );

            //when, then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(longs = {Integer.MIN_VALUE, -1})
        void 가격이_0보다_작으면_예외를_던진다(long price) {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuRequest menuRequest = new MenuRequest(
                    "텐동",
                    new BigDecimal(price),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1))
            );

            //when, then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_메뉴_그룹이라면_예외를_던진다() {
            //given
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuRequest menuRequest = new MenuRequest(
                    "텐동",
                    BigDecimal.valueOf(11000),
                    -1L,
                    List.of(new MenuProductRequest(product.getId(), 1))
            );

            //when, then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않은_상품이라면_예외를_던진다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            MenuRequest menuRequest = new MenuRequest(
                    "텐동",
                    BigDecimal.valueOf(11000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(-1L, 1))
            );

            //when, then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 총_상품_가격보다_메뉴_가격이_크다면_예외를_던진다() {
            //given
            MenuGroup menuGroup = menuGroupRepository.save(메뉴_그룹("일식"));
            Product product = productRepository.save(상품("텐동", BigDecimal.valueOf(11000)));
            MenuRequest menuRequest = new MenuRequest(
                    "텐동",
                    BigDecimal.valueOf(12000),
                    menuGroup.getId(),
                    List.of(new MenuProductRequest(product.getId(), 1))
            );

            //when, then
            assertThatThrownBy(() -> menuService.create(menuRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
