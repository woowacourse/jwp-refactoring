package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.domain.ProductRepository;
import kitchenpos.domain.repository.MenuGroupRepository;
import kitchenpos.domain.repository.MenuRepository;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductCreateRequest;
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
@Import(MenuService.class)
class MenuServiceTest extends ServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    private MenuGroup 두마리메뉴;
    private MenuProductCreateRequest 후라이드_한마리;
    private MenuProductCreateRequest 양념치킨_한마리;
    private MenuProductCreateRequest 간장치킨_한마리;

    @BeforeEach
    void setUp() {
        두마리메뉴 = MenuGroup.create("두마리메뉴");
        menuGroupRepository.save(두마리메뉴);

        Product 후라이드 = Product.create("후라이드", BigDecimal.valueOf(16000));
        Product 양념치킨 = Product.create("양념치킨", BigDecimal.valueOf(16000));
        Product 간장치킨 = Product.create("간장치킨", BigDecimal.valueOf(16000));

        productRepository.save(후라이드);
        productRepository.save(양념치킨);
        productRepository.save(간장치킨);

        후라이드_한마리 = new MenuProductCreateRequest(후라이드.getId(), 1L);
        양념치킨_한마리 = new MenuProductCreateRequest(양념치킨.getId(), 1L);
        간장치킨_한마리 = new MenuProductCreateRequest(간장치킨.getId(), 1L);
    }

    @DisplayName("메뉴를 정상적으로 등록할 수 있다.")
    @Test
    void create() {
        // given
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드1+양념1",
                32000L,
                두마리메뉴.getId(),
                List.of(후라이드_한마리, 양념치킨_한마리)
        );

        // when
        Menu actual = menuService.create(request);

        // then
        assertSoftly(softly -> {
            softly.assertThat(menuRepository.findById(actual.getId())).isPresent();
            softly.assertThat(actual.getName()).isEqualTo(request.getName());
            softly.assertThat(actual.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(request.getPrice()));
            softly.assertThat(actual.getMenuGroup().getId()).isEqualTo(request.getMenuGroupId());
            softly.assertThat(actual.getMenuProducts().size()).isEqualTo(request.getMenuProducts().size());
        });
    }

    @DisplayName("메뉴 등록 시 메뉴 가격이 음수인 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -100L})
    void create_FailWithNegativeMenuPrice(long invalidMenuPrice) {
        // given
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드1+양념1",
                invalidMenuPrice,
                두마리메뉴.getId(),
                List.of(후라이드_한마리, 양념치킨_한마리)
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 가격은 0원 이상이어야 합니다.");
    }

    @DisplayName("메뉴 등록 시 메뉴 가격이 상품 총 가격보다 큰 경우 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(longs = {33000L, Long.MAX_VALUE})
    void create_FailWithInvalidMenuPrice(long invalidMenuPrice) {
        // given
        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드1+양념1",
                invalidMenuPrice,
                두마리메뉴.getId(),
                List.of(후라이드_한마리, 양념치킨_한마리)
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 등록 시 메뉴 그룹이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidMenuGroupId() {
        // given
        long invalidMenuGroupId = 1000L;

        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드1+양념1",
                32000L,
                invalidMenuGroupId,
                List.of(후라이드_한마리, 양념치킨_한마리)
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청한 menuGroupId에 해당하는 MenuGroup이 존재하지 않습니다.");
    }

    @DisplayName("메뉴 등록 시 상품이 존재하지 않는 경우 예외가 발생한다.")
    @Test
    void create_FailWithInvalidProducts() {
        // given
        long invalidProductId = 1000L;
        MenuProductCreateRequest invalidRequest1 = new MenuProductCreateRequest(invalidProductId, 1L);
        MenuProductCreateRequest invalidRequest2 = new MenuProductCreateRequest(invalidProductId, 1L);

        MenuCreateRequest request = new MenuCreateRequest(
                "후라이드1+양념1",
                32000L,
                두마리메뉴.getId(),
                List.of(invalidRequest1, invalidRequest2)
        );

        // when & then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("요청한 상품들 중 존재하지 않는 상품이 존재합니다.");
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        MenuCreateRequest request1 = new MenuCreateRequest(
                "후라이드1+양념1",
                32000L,
                두마리메뉴.getId(),
                List.of(후라이드_한마리, 양념치킨_한마리)
        );

        MenuCreateRequest request2 = new MenuCreateRequest(
                "간장1+양념1",
                32000L,
                두마리메뉴.getId(),
                List.of(간장치킨_한마리, 양념치킨_한마리)
        );

        Menu 후1양1_메뉴 = menuService.create(request1);
        Menu 간1양1_메뉴 = menuService.create(request2);

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
