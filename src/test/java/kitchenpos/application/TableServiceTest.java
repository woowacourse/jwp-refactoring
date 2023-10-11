package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;
import kitchenpos.support.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableService tableService;

    @Test
    void 주문테이블을_저장() {
        // given
        OrderTable expected = new OrderTable(4, true);

        // when
        OrderTable actual = tableService.create(expected);

        // then
        assertAll(
            () -> assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected),
            () -> assertThat(actual.getId()).isPositive()
        );
    }

    @Test
    void 모든_주문_테이블_조회() {
        // given
        List<OrderTable> expected = new ArrayList<>();
        expected.add(orderTableDao.save(new OrderTable(3, true)));
        expected.add(orderTableDao.save(new OrderTable(4, false)));
        expected.add(orderTableDao.save(new OrderTable(2, true)));

        // when
        List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Nested
    class 주문_테이블_상태를_변경 {

        @Test
        void 불가능에서_가능으로_변경() {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, false)).getId();

            // when
            OrderTable actual = tableService.changeEmpty(givenId, true);

            // then
            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        void 가능에서_불가능으로_변경() {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, true)).getId();
            // when
            OrderTable actual = tableService.changeEmpty(givenId, false);

            // then
            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        void 없는_주문테이블이면_예외() {
            // when && then
            //TODO: truncate 변경하고 아이디 1L로 변경
            assertThatThrownBy(() -> tableService.changeEmpty(100L, true))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 테이블그룹_아이디가_존재하면_예외() {
            // given
            tableGroupDao.save(new TableGroup(1L, LocalDateTime.now(), null));
            Long givenId = orderTableDao.save(new OrderTable(1L, 5, true)).getId();

            // when && then
            assertThatThrownBy(() -> tableService.changeEmpty(givenId, false))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_주문테이블의_주문이_예약상태면_예외() {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, true)).getId();
            orderDao.save(new Order(givenId, OrderStatus.COOKING, LocalDateTime.now(), null));

            // when && then
            assertThatThrownBy(() -> tableService.changeEmpty(givenId, true))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_주문테이블의_주문이_조리중이면_예외() {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, true)).getId();
            orderDao.save(new Order(givenId, OrderStatus.MEAL, LocalDateTime.now(), null));

            // when && then
            assertThatThrownBy(() -> tableService.changeEmpty(givenId, true))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 방문자수를_변경 {

        @ParameterizedTest
        @ValueSource(ints = {1,2,3})
        void 성공(int changedNumberOfGuests) {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, false)).getId();

            // when
            OrderTable actual = tableService.changeNumberOfGuests(givenId, changedNumberOfGuests);

            // then
            assertThat(actual.getNumberOfGuests()).isEqualTo(changedNumberOfGuests);
        }

        @ParameterizedTest
        @ValueSource(ints = {-100,-1})
        void 변경할려는_방문자수가_음수면_예외(int wrongValue) {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, false)).getId();

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenId, wrongValue))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 해당하는_주문테이블이_없으면_예외() {
            List<OrderTable> list = tableService.list();
            System.out.println("list = " + list);
            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 1))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문테이블이_비어있으면_예외() {
            // given
            Long givenId = orderTableDao.save(new OrderTable(5, true)).getId();

            // when & then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(givenId, 4))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
