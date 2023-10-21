package kitchenpos.domain;

import static kitchenpos.domain.OrderStatus.COOKING;
import static kitchenpos.fixture.MenuFixture.치킨_피자_세트_치킨_8000_1개_피자_8000_1개;
import static kitchenpos.fixture.OrderFixture.주문_생성_메뉴_당_1개씩_상태_설정;
import static kitchenpos.fixture.OrderTableFixture.빈_테이블_생성;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTableTest {

    @Test
    @DisplayName("특정 테이블의 방문한 손님 수는 0 이상이어야 한다.")
    void 주문_테이블_방문한_손님_수_변경_실패_음수() {
        // given
        final OrderTable table = 주문_테이블_생성();

        // expected
        assertThatThrownBy(() -> table.changeNumberOfGuests(-10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블의 방문한 손님 수를 변경할 수 없다.")
    void 주문_테이블_방문한_손님_수_변경_실패_빈_테이블() {
        // given
        final OrderTable table = 빈_테이블_생성();

        // expected
        assertThatThrownBy(() -> table.changeNumberOfGuests(10))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("식사 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_식사_상태() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();

        // when
        /// TODO: 2023/10/20 auditing 
        final Order order = new Order(orderTable, LocalDateTime.now());
        order.startMeal();

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("조리 상태인 테이블을 비울 수 없다.")
    void 주문_테이블_비우기_실패_조리_상태() {
        // given
        final OrderTable orderTable = 주문_테이블_생성();

        // when
        주문_생성_메뉴_당_1개씩_상태_설정(orderTable, COOKING, List.of(치킨_피자_세트_치킨_8000_1개_피자_8000_1개(new MenuGroup("양식"))));

        // then
        assertThatThrownBy(() -> orderTable.changeEmpty(true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("소속된 단체가 없는 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_이미_소속_단체가_있는_테이블() {
        // given
        final List<OrderTable> tablesInGroup = List.of(빈_테이블_생성(), 빈_테이블_생성());

        // when
        new TableGroup().addOrderTables(tablesInGroup);

        // then
        assertThatThrownBy(() -> new TableGroup().addOrderTables(tablesInGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블만 단체로 지정할 수 있다.")
    void 단체_테이블_지정_실패_주문_테이블() {
        // given
        final List<OrderTable> tablesInGroup = List.of(주문_테이블_생성(), 빈_테이블_생성());

        // when
        final TableGroup tableGroup = new TableGroup();

        // then
        assertThatThrownBy(() -> tableGroup.addOrderTables(tablesInGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
