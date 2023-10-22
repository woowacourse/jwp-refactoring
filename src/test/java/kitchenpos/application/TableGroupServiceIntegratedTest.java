package kitchenpos.application;

import kitchenpos.application.test.ServiceIntegrateTest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTables;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import kitchenpos.domain.repository.TableGroupRepository;
import kitchenpos.dto.request.TableGroupCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.List;

import static java.time.LocalDateTime.now;
import static kitchenpos.domain.fixture.OrderFixture.주문_생성;
import static kitchenpos.domain.fixture.OrderTableFixture.주문_테이블_생성;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TableGroupServiceIntegratedTest extends ServiceIntegrateTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Nested
    class 통합_계산을_생성한다 {

        @Test
        void 통합_계산을_생성한다() {
            // given
            OrderTable 주문_테이블1 = 주문_테이블_생성();
            OrderTable 주문_테이블2 = 주문_테이블_생성();
            OrderTable orderTable1 = orderTableRepository.save(주문_테이블1);
            OrderTable orderTable2 = orderTableRepository.save(주문_테이블2);
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId()));

            // when, then
            assertDoesNotThrow(() -> tableGroupService.create(request));
        }

        @Test
        void 주문이_한_개면_예외가_발생한다() {
            // given
            OrderTable 주문_테이블1 = 주문_테이블_생성();
            OrderTable orderTable1 = orderTableRepository.save(주문_테이블1);
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable1.getId()));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(request));
        }

        @Test
        void 저장된_주문_테이블_개수와_TableGroups의_주문_개수가_다르면_예외가_발생한다() {
            // given
            OrderTable 주문_테이블1 = 주문_테이블_생성();
            OrderTable 주문_테이블2 = 주문_테이블_생성();
            OrderTable orderTable1 = orderTableRepository.save(주문_테이블1);
            OrderTable orderTable2 = orderTableRepository.save(주문_테이블2);
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable1.getId()));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(request));
        }

        @Test
        void 저장된_주문_테이블이_비어있지_않으면_예외가_발생한다() {
            // given
            OrderTable 주문_테이블1 = 주문_테이블_생성();
            OrderTable 주문_테이블2 = 주문_테이블_생성();
            주문_테이블2.updateEmpty(false);
            OrderTable orderTable1 = orderTableRepository.save(주문_테이블1);
            OrderTable orderTable2 = orderTableRepository.save(주문_테이블2);
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId()));

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(request));
        }

        @Test
        void 주문_테이블의_TableGroupId가_null이_아니라면_예외가_발생한다() {
            // Given
            OrderTable 주문_테이블1 = 주문_테이블_생성();
            OrderTable 주문_테이블2 = 주문_테이블_생성();
            final OrderTables orderTables = new OrderTables(List.of(주문_테이블1, 주문_테이블2));
            주문_테이블2.updateTableGroup(new TableGroup(orderTables, now()));
            OrderTable orderTable1 = orderTableRepository.save(주문_테이블1);
            OrderTable orderTable2 = orderTableRepository.save(주문_테이블2);
            TableGroupCreateRequest request = new TableGroupCreateRequest(List.of(orderTable1.getId(), orderTable2.getId()));

            // When, Then
            assertThrows(InvalidDataAccessApiUsageException.class, () -> {
                // When
                tableGroupService.create(request);
            });
        }

    }

    @Nested
    class 통합_계산을_취소한다 {

        private Long tableGroupId;

        @BeforeEach
        void setUp() {
            OrderTable 주문_테이블1 = 주문_테이블_생성();
            OrderTable 주문_테이블2 = 주문_테이블_생성();
            OrderTable orderTable1 = orderTableRepository.save(주문_테이블1);
            OrderTable orderTable2 = orderTableRepository.save(주문_테이블2);
            final OrderTables orderTables = new OrderTables(List.of(orderTable1, orderTable2));
            TableGroup request = new TableGroup(orderTables, now());
            tableGroupId = tableGroupRepository.save(request).getId();
        }

        @Test
        void 통합_계산을_취소한다() {
            // when, then
            assertDoesNotThrow(() -> tableGroupService.ungroup(tableGroupId));
        }

        @Test
        void 통합_계산_주문들_중_하나라도_COOKING_또는_MEAL이면_예외가_발생한다() {
            // given
            TableGroup tableGroup = tableGroupRepository.getById(tableGroupId);
            Order order1 = 주문_생성(tableGroup.getOrderTables().getValues().get(0));
            Order order2 = 주문_생성(tableGroup.getOrderTables().getValues().get(1));
            orderRepository.save(order1);
            orderRepository.save(order2);

            // when, then
            assertThrows(IllegalArgumentException.class, () -> tableGroupService.ungroup(tableGroupId));
        }

    }

}