package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.common.exception.CustomErrorCode;
import kitchenpos.common.exception.DomainLogicException;
import kitchenpos.order.domain.Empty;
import org.junit.jupiter.api.Test;

class EmptyTest {

    @Test
    void 빈테이블_여부를_변경한다() {
        // given
        final var empty = new Empty(true);

        // when
        final var changed = empty.changeTo(false);

        // then
        assertThat(changed).isEqualTo(new Empty(false));
    }
    
    @Test
    void 같은_값으로_변경하려는_경우_예외를_던진다() {
        // given
        final var empty = new Empty(true);

        // when & then
        assertThatThrownBy(() -> empty.changeTo(true))
                .isInstanceOf(DomainLogicException.class)
                .extracting("errorCode")
                .isEqualTo(CustomErrorCode.TABLE_EMPTY_CHANGE_SAME_ERROR);
    }
}
