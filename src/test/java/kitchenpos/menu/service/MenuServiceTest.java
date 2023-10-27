package kitchenpos.menu.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.event.ValidateMenuGroupExistsEvent;
import kitchenpos.common.vo.Money;
import kitchenpos.common.vo.PriceIsNegativeException;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.request.MenuProductRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.exception.MenuPriceIsBiggerThanActualPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;
import kitchenpos.product.domain.Product;
import kitchenpos.supports.ServiceTestContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
class MenuServiceTest extends ServiceTestContext {

    @Test
    void 메뉴_가격이_0보다_작으면_예외를_던진다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));

        menuGroupRepository.save(menuGroup);
        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(-1),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(PriceIsNegativeException.class);
    }

    @Test
    void 메뉴_그룹_아이디에_해당하는_메뉴_그룹이_없는_경우_예외를_던진다() {
        // given
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));

        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(1000L),
                Long.MAX_VALUE,
                List.of(new MenuProductRequest(product.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(MenuGroupNotFoundException.class);
    }

    @Test
    void 가격이_실제_메뉴_상품들의_총_가격보다_크면_예외를_던진다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));

        menuGroupRepository.save(menuGroup);
        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(2001L),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        // when, then
        assertThatThrownBy(() -> menuService.create(request))
                .isInstanceOf(MenuPriceIsBiggerThanActualPriceException.class);
    }

    @Test
    void 메뉴를_정상적으로_생성하는_경우_생성한_메뉴가_반환된다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));

        menuGroupRepository.save(menuGroup);
        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(999L),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        // when
        MenuResponse response = menuService.create(request);

        // then
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.getId()).isNotNull();
            softly.assertThat(response.getName()).isEqualTo(request.getName());
        });
    }

    @Test
    void 전체_메뉴를_조회할_수_있다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));

        menuGroupRepository.save(menuGroup);
        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(999L),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        menuService.create(request);

        // when
        List<MenuResponse> response = menuService.findAll();

        // then
        assertThat(response).hasSize(1);
    }

    @Test
    void 상품의_가격이_변경되더라도_메뉴_가격은_변하지_않는다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));

        // when
        menuGroupRepository.save(menuGroup);
        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(999L),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        menuService.create(request);

        ReflectionTestUtils.setField(product, "price", new Money(BigDecimal.valueOf(2000L)));

        List<MenuResponse> menuResponses = menuService.findAll();

        // then
        assertThat(menuResponses.get(0).getPrice().doubleValue())
                .isEqualTo(BigDecimal.valueOf(999L).doubleValue());
    }

    @Test
    void 메뉴를_생성할_때_메뉴_그룹이_존재하는지를_검증하기_위한_이벤트가_발행된다() {
        // given
        MenuGroup menuGroup = MenuGroupFixture.from("name");
        Product product = ProductFixture.of("name", BigDecimal.valueOf(1000L));


        // when
        menuGroupRepository.save(menuGroup);
        productRepository.save(product);

        CreateMenuRequest request = new CreateMenuRequest("menuName",
                BigDecimal.valueOf(999L),
                menuGroup.getId(),
                List.of(new MenuProductRequest(product.getId(), 1L)));

        menuService.create(request);

        // then
        long eventOccurredCount = applicationEvents.stream(ValidateMenuGroupExistsEvent.class)
                .count();

        assertThat(eventOccurredCount).isEqualTo(1);
    }
}
