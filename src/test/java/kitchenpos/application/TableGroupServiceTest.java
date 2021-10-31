package kitchenpos.application;

import kitchenpos.CustomParameterizedTest;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.TableGroupFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("TableGroup 비즈니스 흐름 테스트")
@Transactional
@SpringBootTest
class TableGroupServiceTest {
    private static final LocalDateTime NOW = LocalDateTime.now();

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderService orderService;

    private static Stream<Arguments> create() {
        return Stream.of(
                Arguments.of(NOW, TableGroupFixture.FIRST_SECOND_TABLE_BEFORE_SAVE.getOrderTables())
        );
    }

    @DisplayName("단체지정 저장 - 성공")
    @CustomParameterizedTest
    @MethodSource("create")
    void create(@AggregateWith(TableGroupAggregator.class) TableGroup tableGroup) {
        //given
        //when
        final TableGroup actual = tableGroupService.create(tableGroup);
        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getCreatedDate()).isAfter(NOW);
        assertThat(actual.getOrderTables())
                .extracting(OrderTable::isEmpty)
                .containsExactly(false, false);
        assertThat(actual.getOrderTables())
                .extracting(OrderTable::getTableGroupId)
                .contains(actual.getId());
    }

    @DisplayName("단체지정 저장 - 실패 - 단체지정의 OrderTable이 null")
    @Test
    void createFailureWhenTableGroupNull() {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(null);
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 - 단체지정의 OrderTable이 2 이상이 아닐 때")
    @Test
    void createFailureWhenOrderTableLowerThanTwo() {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(OrderTableFixture.FIRST));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 - 저장된 주물테이블 수와 요청했던 주문테이블의 수가 다름")
    @Test
    void createFailureWhenOrderTableDoesNotMatches() {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(OrderTableFixture.FIRST, OrderTableFixture.NINTH));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 - 주문테이블의 empty 값이 false")
    @Test
    void createFailureWhenOrderTableEmptyFalse() {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(OrderTableFixture.FIRST_EMPTY_FALSE, OrderTableFixture.NINTH));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 저장 - 실패 -  주문테이블에 이미 단체지정이 되어있음")
    @Test
    void createFailureWhenOrderTableAssignTableGroup() {
        //given
        final TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(NOW);
        tableGroup.setOrderTables(Arrays.asList(OrderTableFixture.FIRST, OrderTableFixture.SECOND));
        //when
        tableGroupService.create(tableGroup);
        //then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체지정 해제 - 성공 - 단체지정 해제")
    @Test
    void unGroupTableGroup() {
        //given
        //when
        final TableGroup expect = tableGroupService.create(TableGroupFixture.FIRST_SECOND_TABLE_BEFORE_SAVE);
        tableGroupService.ungroup(expect.getId());
        final Optional<OrderTable> foundOrderTable = orderTableDao.findById(expect.getOrderTables().get(0).getId());
        //then
        assertThat(foundOrderTable).isNotEmpty();
        assertThat(foundOrderTable.get().getTableGroupId()).isNull();
        assertThat(foundOrderTable.get().isEmpty()).isFalse();
    }

    @DisplayName("단체지정 해제 - 실패 - 테이블의 주문 상태가 COMPLETION이 아님")
    @Test
    void unGroupTableGroupFailureWhenOrderStatusNotCompletion() {
        //given
        //when
        final TableGroup expect = tableGroupService.create(TableGroupFixture.FIRST_SECOND_TABLE_BEFORE_SAVE);
        orderService.create(OrderFixture.FIRST_TABLE_후라이드치킨_하나);
        //then
        assertThatThrownBy(() -> tableGroupService.ungroup(expect.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private static class TableGroupAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            final TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(accessor.get(0, LocalDateTime.class));
            tableGroup.setOrderTables(accessor.get(1, List.class));
            return tableGroup;
        }
    }
}
