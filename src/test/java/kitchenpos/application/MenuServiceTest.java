package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.fixture.MenuProductFixture.메뉴_상품;
import static kitchenpos.fixture.ProductFixture.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.menu.MenuCreateRequest;
import kitchenpos.application.dto.menu.MenuCreateResponse;
import kitchenpos.application.dto.menu.MenuProductCreateRequest;
import kitchenpos.application.dto.menu.MenuResponse;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuProducts;
import kitchenpos.domain.Product;
import kitchenpos.domain.vo.Price;
import kitchenpos.persistence.MenuGroupRepository;
import kitchenpos.persistence.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
@Sql("classpath:truncate.sql")
@TestConstructor(autowireMode = AutowireMode.ALL)
class MenuServiceTest {

    private final MenuService menuService;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    private Product 저장된_양념_치킨;
    private Product 저장된_후라이드_치킨;
    private MenuProduct 메뉴_상품_1;
    private MenuProduct 메뉴_상품_2;
    private MenuGroup 저장된_메뉴_그룹;

    public MenuServiceTest(
            final MenuService menuService,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuService = menuService;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @BeforeEach
    void setUp() {
        final MenuGroup 메뉴_그룹 = 메뉴_그룹(null, "양념 반 후라이드 반");
        final Product 양념_치킨 = 상품(null, "양념 치킨", BigDecimal.valueOf(12000, 2));
        final Product 후라이드_치킨 = 상품(null, "후라이드 치킨", BigDecimal.valueOf(10000, 2));
        저장된_양념_치킨 = productRepository.save(양념_치킨);
        저장된_후라이드_치킨 = productRepository.save(후라이드_치킨);
        메뉴_상품_1 = 메뉴_상품(null, null, 저장된_양념_치킨, 1);
        메뉴_상품_2 = 메뉴_상품(null, null, 저장된_후라이드_치킨, 1);
        저장된_메뉴_그룹 = menuGroupRepository.save(메뉴_그룹);
    }

    @Nested
    class 메뉴_등록_시 {

        @Test
        void 메뉴를_정상적으로_등록한다() {
            // given
            final MenuProductCreateRequest 메뉴_상품1_요청값 = new MenuProductCreateRequest(메뉴_상품_1.getProduct().getId(),
                    메뉴_상품_1.getQuantity());
            final MenuProductCreateRequest 메뉴_상품2_요청값 = new MenuProductCreateRequest(메뉴_상품_2.getProduct().getId(),
                    메뉴_상품_2.getQuantity());
            final MenuCreateRequest 메뉴 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(),
                    List.of(메뉴_상품1_요청값, 메뉴_상품2_요청값));

            // when
            final MenuCreateResponse 저장된_메뉴 = menuService.create(메뉴);

            // then
            final Menu 예상_메뉴 = new Menu(null, "메뉴", new Price(BigDecimal.valueOf(22000, 2)),
                    new MenuGroup("양념 반 후라이드 반"), null);
            final MenuProduct 예상_메뉴_상품1 = new MenuProduct(예상_메뉴, 메뉴_상품_1.getProduct(), 메뉴_상품_1.getQuantity());
            final MenuProduct 예상_메뉴_상품2 = new MenuProduct(예상_메뉴, 메뉴_상품_2.getProduct(), 메뉴_상품_2.getQuantity());
            final MenuCreateResponse 예상_응답값 = MenuCreateResponse.of(
                    new Menu(null, "메뉴", new Price(BigDecimal.valueOf(22000, 2)), new MenuGroup("양념 반 후라이드 반"),
                            new MenuProducts(List.of(예상_메뉴_상품1, 예상_메뉴_상품2)))
            );

            assertAll(
                    () -> assertThat(저장된_메뉴.getId()).isNotNull(),
                    () -> assertThat(저장된_메뉴).usingRecursiveComparison()
                            .ignoringFields("id", "menuGroupId", "menuProducts.seq", "menuProducts.menuId")
                            .isEqualTo(예상_응답값)
            );
        }

        @Test
        void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
            // given
            final MenuProductCreateRequest 메뉴_상품1_요청값 = new MenuProductCreateRequest(메뉴_상품_1.getProduct().getId(),
                    메뉴_상품_1.getQuantity());
            final MenuProductCreateRequest 메뉴_상품2_요청값 = new MenuProductCreateRequest(메뉴_상품_2.getProduct().getId(),
                    메뉴_상품_2.getQuantity());
            final MenuCreateRequest 메뉴 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(-22000, 2), 저장된_메뉴_그룹.getId(),
                    List.of(메뉴_상품1_요청값, 메뉴_상품2_요청값));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
            // given
            final MenuProductCreateRequest 메뉴_상품1_요청값 = new MenuProductCreateRequest(메뉴_상품_1.getProduct().getId(),
                    메뉴_상품_1.getQuantity());
            final MenuProductCreateRequest 메뉴_상품2_요청값 = new MenuProductCreateRequest(메뉴_상품_2.getProduct().getId(),
                    메뉴_상품_2.getQuantity());
            final MenuCreateRequest 메뉴 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(22000, 2),
                    저장된_메뉴_그룹.getId() + 1, List.of(메뉴_상품1_요청값, 메뉴_상품2_요청값));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 메뉴 그룹입니다. id = ");
        }

        @Test
        void 상품이_존재하지_않으면_예외가_발생한다() {
            // given
            final MenuProductCreateRequest 메뉴_상품1_요청값 = new MenuProductCreateRequest(메뉴_상품_1.getProduct().getId(),
                    메뉴_상품_1.getQuantity());
            final MenuProductCreateRequest 메뉴_상품2_요청값 = new MenuProductCreateRequest(메뉴_상품_2.getProduct().getId() + 1,
                    메뉴_상품_2.getQuantity());
            final MenuCreateRequest 메뉴 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(),
                    List.of(메뉴_상품1_요청값, 메뉴_상품2_요청값));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("존재하지 않는 상품입니다. id = ");
        }

        @Test
        void 입력한_가격이_상품들의_가격합_보다_크면_예외가_발생한다() {
            // given
            final MenuProductCreateRequest 메뉴_상품1_요청값 = new MenuProductCreateRequest(메뉴_상품_1.getProduct().getId(),
                    메뉴_상품_1.getQuantity());
            final MenuProductCreateRequest 메뉴_상품2_요청값 = new MenuProductCreateRequest(메뉴_상품_2.getProduct().getId(),
                    메뉴_상품_2.getQuantity());
            final MenuCreateRequest 메뉴 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(23000, 2), 저장된_메뉴_그룹.getId(),
                    List.of(메뉴_상품1_요청값, 메뉴_상품2_요청값));

            // expected
            assertThatThrownBy(() -> menuService.create(메뉴))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("메뉴의 가격이 메뉴 상품들의 가격의 합보다 클 수 없습니다.");
        }
    }

    @Test
    void 메뉴_목록을_정상적으로_조회한다() {
        // given
        final MenuProductCreateRequest 메뉴_상품1_요청값 = new MenuProductCreateRequest(메뉴_상품_1.getProduct().getId(),
                메뉴_상품_1.getQuantity());
        final MenuProductCreateRequest 메뉴_상품2_요청값 = new MenuProductCreateRequest(메뉴_상품_2.getProduct().getId(),
                메뉴_상품_2.getQuantity());
        final MenuCreateRequest 메뉴1 = new MenuCreateRequest("메뉴", BigDecimal.valueOf(22000, 2), 저장된_메뉴_그룹.getId(),
                List.of(메뉴_상품1_요청값, 메뉴_상품2_요청값));

        final MenuGroup 저장된_메뉴_그룹2 = menuGroupRepository.save(메뉴_그룹(null, "양념 2개"));
        final MenuProductCreateRequest 메뉴2_상품1_요청값 = new MenuProductCreateRequest(저장된_양념_치킨.getId(), 2);
        final MenuCreateRequest 메뉴2 = new MenuCreateRequest("메뉴2", BigDecimal.valueOf(24000, 2), 저장된_메뉴_그룹2.getId(),
                List.of(메뉴2_상품1_요청값));

        menuService.create(메뉴1);
        menuService.create(메뉴2);

        // when
        final List<MenuResponse> 메뉴들 = menuService.list();

        // then
        final Menu 예상_메뉴1 = new Menu(null, "메뉴", new Price(BigDecimal.valueOf(22000, 2)), new MenuGroup("양념 반 후라이드 반"),
                null);
        final MenuProduct 예상_메뉴_상품1 = new MenuProduct(예상_메뉴1, 메뉴_상품_1.getProduct(), 메뉴_상품_1.getQuantity());
        final MenuProduct 예상_메뉴_상품2 = new MenuProduct(예상_메뉴1, 메뉴_상품_2.getProduct(), 메뉴_상품_2.getQuantity());
        final MenuCreateResponse 예상_메뉴1_응답값 = MenuCreateResponse.of(
                new Menu(null, "메뉴", new Price(BigDecimal.valueOf(22000, 2)), new MenuGroup("양념 반 후라이드 반"),
                        new MenuProducts(List.of(예상_메뉴_상품1, 예상_메뉴_상품2)))
        );
        final Menu 예상_메뉴2 = new Menu(null, "메뉴2", new Price(BigDecimal.valueOf(24000, 2)), new MenuGroup("양념 2개"),
                null);
        final MenuProduct 예상_메뉴2_상품1 = new MenuProduct(예상_메뉴2, 저장된_양념_치킨, 2);
        final MenuCreateResponse 예상_메뉴2_응답값 = MenuCreateResponse.of(
                new Menu(null, "메뉴2", new Price(BigDecimal.valueOf(24000, 2)), new MenuGroup("양념 2개"),
                        new MenuProducts(List.of(예상_메뉴2_상품1)))
        );

        assertThat(메뉴들).usingRecursiveComparison()
                .ignoringFields("id", "menuGroupId", "menuProducts.seq", "menuProducts.menuId")
                .isEqualTo(List.of(예상_메뉴1_응답값, 예상_메뉴2_응답값));
    }
}
