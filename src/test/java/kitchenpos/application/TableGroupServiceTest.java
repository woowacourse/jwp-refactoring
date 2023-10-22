package kitchenpos.application;

import java.time.LocalDateTime;
import java.util.List;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.ui.dto.tablegroup.OrderTableIdRequest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Transactional
@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 단쳬_지정_저장 {

        @Test
        void 단쳬_지정을_저장할_수_있다() {
            // given
            final OrderTable firstOrderTable = orderTableDao.save(new OrderTable(null, 0, true));
            final OrderTable secondOrderTable = orderTableDao.save(new OrderTable(null, 0, true));

            final List<OrderTableIdRequest> orderTableIdRequests = List.of(
                    new OrderTableIdRequest(firstOrderTable.getId()),
                    new OrderTableIdRequest(secondOrderTable.getId())
            );

            // when
            final TableGroup actual = tableGroupService.create(orderTableIdRequests);

            // then
            assertAll(
                    () -> assertThat(actual.getId()).isNotNull(),
                    () -> assertThat(actual.getOrderTables()).hasSize(2)
            );
        }

        @Test
        void 주문_테이블이_없다면_예외가_발생한다() {
            // given
            final List<OrderTableIdRequest> orderTableIdRequests = List.of(new OrderTableIdRequest(null), new OrderTableIdRequest(null));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(orderTableIdRequests))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("단체 지정시 주문 테이블은 둘 이상이여야 합니다");
        }

        @Test
        void 주문_테이블이_2개_미만이면_예외가_발생한다() {
            // given
            final OrderTable orderTable = orderTableDao.save(new OrderTable(null, 0, true));
            final List<OrderTableIdRequest> orderTableIdRequests = List.of(new OrderTableIdRequest(orderTable.getId()));

            // expect
            assertThatThrownBy(() -> tableGroupService.create(orderTableIdRequests))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("단체 지정시 주문 테이블은 둘 이상이여야합니다");
        }


        @Test
        void 주문_테이블이_빈_상태가_아니라면_예외가_발생한다() {
            // given
            final OrderTable firstOrderTable = orderTableDao.save(new OrderTable(null, 3, false));
            final OrderTable secondOrderTable = orderTableDao.save(new OrderTable(null, 3, false));

            final List<OrderTableIdRequest> orderTableIdRequests = List.of(
                    new OrderTableIdRequest(firstOrderTable.getId()),
                    new OrderTableIdRequest(secondOrderTable.getId())
            );

            // expect
            assertThatThrownBy(() -> tableGroupService.create(orderTableIdRequests))
                    .isInstanceOf(IllegalArgumentException.class);
        }

    }

    @Test
    void 단체_지정을_해제할_수_있다() {
        // given
        final OrderTable firstOrderTable = orderTableDao.save(new OrderTable(null, 3, false));
        final OrderTable secondOrderTable = orderTableDao.save(new OrderTable(null, 3, false));

        final TableGroup tableGroup = new TableGroup(List.of(firstOrderTable, secondOrderTable));
        final TableGroup expected = tableGroupRepository.save(tableGroup);

        // expect
        assertDoesNotThrow(() -> tableGroupService.ungroup(expected.getId()));
    }

    @Test
    void 단체_지정_해제시_식사_중인_주문이_있을_경우_예외가_발생한다() {
        // given
        final OrderTable firstOrderTable = orderTableDao.save(new OrderTable(null, 1, true));
        final OrderTable secondOrderTable = orderTableDao.save(new OrderTable(null, 2, true));
        final Order order = new Order(firstOrderTable, OrderStatus.COOKING, LocalDateTime.now(), null);
        orderDao.save(order);

        final List<OrderTableIdRequest> orderTableIdRequests = List.of(
                new OrderTableIdRequest(firstOrderTable.getId()),
                new OrderTableIdRequest(secondOrderTable.getId())
        );

        final TableGroup expected = tableGroupService.create(orderTableIdRequests);

        // expect
        assertThatThrownBy(() -> tableGroupService.ungroup(expected.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료되지 않은 주문입니다");
    }

}
