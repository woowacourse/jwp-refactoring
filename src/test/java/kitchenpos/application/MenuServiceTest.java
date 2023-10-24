package kitchenpos.application;

import static kitchenpos.exception.MenuExceptionType.SUM_OF_MENU_PRODUCTS_PRICE_MUST_BE_LESS_THAN_PRICE;
import static kitchenpos.exception.MenuGroupExceptionType.MENU_GROUP_NOT_FOUND;
import static kitchenpos.exception.PriceExceptionType.PRICE_CAN_NOT_NEGATIVE;
import static kitchenpos.exception.PriceExceptionType.PRICE_CAN_NOT_NULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.application.dto.menu.CreateMenuCommand;
import kitchenpos.application.dto.menu.CreateMenuResponse;
import kitchenpos.application.dto.menu.SearchMenuResponse;
import kitchenpos.application.dto.menuproduct.MenuProductCommand;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import kitchenpos.exception.BaseException;
import kitchenpos.exception.BaseExceptionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

class MenuServiceTest extends IntegrationTest {

    private MenuGroup 메뉴그룹;
    private Product 가격이_1인_상품;
    private Product 가격이_3인_상품;
    private String 메뉴이름;

    @BeforeEach
    void setUp() {
        메뉴그룹 = 메뉴그룹저장(메뉴그룹("추천메뉴"));
        가격이_1인_상품 = 상품저장(상품("가격이 1인 상품", 가격(1)));
        가격이_3인_상품 = 상품저장(상품("가격이 3인 상품", 가격(3)));
        메뉴이름 = "메뉴";
    }

    @Test
    void 메뉴_그룹이_존재하지_않으면_예외가_발생한다() {
        // given
        Long noExistMenuGroupId = -1L;
        CreateMenuCommand command = new CreateMenuCommand(메뉴이름, BigDecimal.ZERO, noExistMenuGroupId, List.of());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                menuService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(MENU_GROUP_NOT_FOUND);
    }

    @Test
    void 메뉴_상품이_없어도_예외가_발생하지_않는다() {
        // given
        CreateMenuCommand command = new CreateMenuCommand(메뉴이름, BigDecimal.ZERO, 메뉴그룹.id(), List.of());

        // when & then
        assertThatCode(() -> menuService.create(command))
                .doesNotThrowAnyException();
    }

    @Test
    void 메뉴의_가격이_null이면_예외가_발생한다() {
        // given
        CreateMenuCommand command = new CreateMenuCommand(메뉴이름, null, 메뉴그룹.id(), List.of());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                menuService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NULL);
    }

    @Test
    void 메뉴의_가격이_0보다_작으면_예외가_발생한다() {
        // given
        BigDecimal negativePrice = BigDecimal.valueOf(-1);
        CreateMenuCommand command = new CreateMenuCommand(메뉴이름, negativePrice, 메뉴그룹.id(), List.of());

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                menuService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(PRICE_CAN_NOT_NEGATIVE);
    }

    @Test
    void 메뉴_이름이_없으면_DB에서_예외가_발생한다() {
        // given
        CreateMenuCommand command = new CreateMenuCommand(null, BigDecimal.ZERO, 메뉴그룹.id(), List.of());

        // when & then
        assertThatThrownBy(() -> menuService.create(command))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 메뉴의_가격이_메뉴_상품_가격들의_합보다_크면_예외가_발생한다() {
        // given
        CreateMenuCommand command = new CreateMenuCommand(메뉴이름, BigDecimal.valueOf(12), 메뉴그룹.id(), List.of(
                new MenuProductCommand(가격이_1인_상품.id(), 2),
                new MenuProductCommand(가격이_3인_상품.id(), 3)
        ));

        // when
        BaseExceptionType exceptionType = assertThrows(BaseException.class, () ->
                menuService.create(command)
        ).exceptionType();

        // then
        assertThat(exceptionType).isEqualTo(SUM_OF_MENU_PRODUCTS_PRICE_MUST_BE_LESS_THAN_PRICE);
    }

    @Test
    void 메뉴를_저장한다() {
        // given
        CreateMenuCommand command = new CreateMenuCommand(메뉴이름, BigDecimal.valueOf(11), 메뉴그룹.id(), List.of(
                new MenuProductCommand(가격이_1인_상품.id(), 2),
                new MenuProductCommand(가격이_3인_상품.id(), 3)
        ));

        // when
        CreateMenuResponse result = menuService.create(command);

        // then
        assertAll(
                () -> assertThat(result.id()).isPositive(),
                () -> assertThat(result.name()).isEqualTo(메뉴이름),
                () -> assertThat(result.price()).isEqualByComparingTo(BigDecimal.valueOf(11)),
                () -> assertThat(result.menuGroupResponse().id()).isEqualByComparingTo(메뉴그룹.id()),
                () -> assertThat(result.menuProductResponses()).hasSize(2)
        );
    }

    @Test
    void 메뉴들을_조회한다() {
        // given
        메뉴저장(메뉴(메뉴이름, 가격(11), 메뉴그룹, 메뉴상품(가격이_1인_상품, 2), 메뉴상품(가격이_3인_상품, 3)));
        메뉴저장(메뉴(메뉴이름, 가격(9), 메뉴그룹, 메뉴상품(가격이_1인_상품, 3), 메뉴상품(가격이_3인_상품, 2)));

        // when
        List<SearchMenuResponse> result = menuService.list();

        // then
        assertThat(result).hasSize(2);
    }
}
