package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.ordertable.domain.OrderTable;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class OrderTableTest {

    @Test
    void 테이블을_빈_상태로_변경할_수_있다() {
        //given
        OrderTable 단일_계산완료_테이블 = OrderTableFixture.단일_계산완료_테이블();

        //when
        단일_계산완료_테이블.changeToEmptyTable();

        //then
        assertThat(단일_계산완료_테이블.isEmpty()).isTrue();
    }

    @Test
    void 테이블이_그룹핑_된_상태인지_확인할_수_있다() {
        //given
        OrderTable 그룹핑된_조리_테이블 = OrderTableFixture.그룹핑된_조리_테이블();

        //when
        boolean result = 그룹핑된_조리_테이블.isGrouped();

        //then
        assertThat(result).isTrue();
    }

    @Test
    void 테이블이_그룹핑_되지_않은_상태인지_확인할_수_있다() {
        //given
        OrderTable 단일_조리_테이블 = OrderTableFixture.단일_조리_테이블();

        //when
        boolean result = 단일_조리_테이블.isGrouped();

        //then
        assertThat(result).isFalse();
    }

    @Test
    void 테이블이_빈_상태가_아니면_테이블_손님_수를_변경할_수_있다() {
        //given
        int numberOfGuests = 4;
        OrderTable 비지않은_신규_테이블 = OrderTableFixture.비지않은_신규_테이블();

        //when, then
        assertThatNoException().isThrownBy(() -> 비지않은_신규_테이블.changeNumberOfGuests(numberOfGuests));
    }

    @Test
    void 테이블이_빈_상태면_테이블_손님_수를_변경할_수_없다() {
        //given
        int numberOfGuests = 4;
        OrderTable 빈_신규_테이블 = OrderTableFixture.빈_신규_테이블1();

        //when, then
        assertThatThrownBy(() -> 빈_신규_테이블.changeNumberOfGuests(numberOfGuests))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
