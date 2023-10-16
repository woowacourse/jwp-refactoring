package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.supports.OrderFixture;
import kitchenpos.supports.OrderTableFixture;
import kitchenpos.supports.TableGroupFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @Mock
    TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;

    @Nested
    class 단체_지정 {

        @ValueSource(ints = {0, 1})
        @ParameterizedTest
        void 주문_테이블은_2개_이상이어야한다(int orderTableSize) {
            // given
            List<OrderTable> orderTables = LongStream.range(0, orderTableSize).boxed()
                .map(i -> OrderTableFixture.fixture().id(i + 1).build())
                .collect(Collectors.toList());
            TableGroup tableGroup = TableGroupFixture.fixture().orderTables(orderTables).build();

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블은 2개 이상이어야 합니다.");
        }

        @Test
        void 모든_주문_테이블은_DB에_존재해야한다() {
            // given
            int orderTableSize = 3;
            List<OrderTable> orderTables = LongStream.range(0, orderTableSize).boxed()
                .map(i -> OrderTableFixture.fixture().id(i + 1).build())
                .collect(Collectors.toList());
            TableGroup tableGroup = TableGroupFixture.fixture().orderTables(orderTables).build();

            List<OrderTable> savedOrderTables = LongStream.range(0, orderTableSize - 1).boxed()
                .map(i -> OrderTableFixture.fixture().id(i + 1).build())
                .collect(Collectors.toList());

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L)))
                .willReturn(savedOrderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 주문 테이블입니다.");
        }

        @Test
        void 빈_테이블이_아니면_예외발생() {
            // given
            int orderTableSize = 3;
            List<OrderTable> orderTables = LongStream.range(0, orderTableSize).boxed()
                .map(i -> OrderTableFixture.fixture().id(i + 1).empty(false).tableGroupId(null).build())
                .collect(Collectors.toList());
            TableGroup tableGroup = TableGroupFixture.fixture().orderTables(orderTables).build();

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L)))
                .willReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 주문 테이블입니다.");
        }

        @Test
        void 이미_단체_지정된_테이블이면_예외발생() {
            // given
            int orderTableSize = 3;
            List<OrderTable> orderTables = LongStream.range(0, orderTableSize).boxed()
                .map(i -> OrderTableFixture.fixture().id(i + 1).empty(false).tableGroupId(1L).build())
                .collect(Collectors.toList());
            TableGroup tableGroup = TableGroupFixture.fixture().orderTables(orderTables).build();

            given(orderTableDao.findAllByIdIn(List.of(1L, 2L, 3L)))
                .willReturn(orderTables);

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("올바르지 않은 주문 테이블입니다.");
        }
    }

    @Nested
    class 테이블_그룹_해제 {

        @ValueSource(strings = {"COOKING", "MEAL"})
        @ParameterizedTest
        void 조리중이거나_식사중인_주문이_있으면_예외발생(String orderStatus) {
            // given
            Long tableGroupId = 1L;
            List<Order> orders = List.of(OrderFixture.fixture().orderStatus(orderStatus).build());
            List<OrderTable> orderTables = List.of(
                OrderTableFixture.fixture().id(1L).build(),
                OrderTableFixture.fixture().id(2L).build()
            );

            given(orderTableDao.findAllByTableGroupId(tableGroupId))
                .willReturn(orderTables);
            given(orderDao.existsByOrderTableIdInAndOrderStatusIn(List.of(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
