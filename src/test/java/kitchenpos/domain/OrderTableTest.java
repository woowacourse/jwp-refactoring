package kitchenpos.domain;

import static kitchenpos.support.DomainFixture.빈_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    void 테이블이_비어있는지_여부를_변경한다() {
        // given
        final var table = new OrderTable(new TableStatus(new Empty(true), new GuestNumber(0)));

        // when
        table.changeEmpty(false);

        // then
        assertThat(table.isEmpty()).isFalse();
    }

    @Test
    void 테이블의_방문자수를_변경한다() {
        // given
        final var table = 빈_테이블_생성();
        table.changeEmpty(false);

        // when
        table.changeGuestNumber(5);

        // then
        assertThat(table.getGuestNumber()).isEqualTo(5);
    }
}
