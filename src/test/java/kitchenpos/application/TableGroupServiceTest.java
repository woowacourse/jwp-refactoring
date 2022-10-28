package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.ui.dto.OrderTableDto;
import kitchenpos.ui.dto.OrderTableRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTestBase {

    @Test
    void 테이블_그룹_정상_생성() {
        // given
        OrderTable orderTable1 = 빈_주문_테이블_생성();
        OrderTable orderTable2 = 빈_주문_테이블_생성();
        OrderTableRequest request = toRequest(orderTable1, orderTable2);

        // when
        TableGroup savedTableGroup = tableGroupService.create(request);

        // then
        Optional<TableGroup> actual = tableGroupDao.findById(savedTableGroup.getId());
        assertThat(actual).isNotEmpty();
    }

    private OrderTableRequest toRequest(final OrderTable... orderTables) {
        List<OrderTableDto> orderTableDtos = Arrays.stream(orderTables)
                .map(it -> new OrderTableDto(it.getId()))
                .collect(Collectors.toList());

        return new OrderTableRequest(orderTableDtos);
    }

    @Test
    void 주문_테이블_1개로_테이블_그룹_생성_시_실패() {
        // given
        OrderTable orderTable = 빈_주문_테이블_생성();
        OrderTableRequest request = toRequest(orderTable);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 비어있지_않은_주문_테이블로_테이블_그룹_생성_시_실패() {
        // given
        OrderTable orderTable1 = 주문_테이블_생성();
        OrderTable orderTable2 = 주문_테이블_생성();
        OrderTableRequest request = toRequest(orderTable1, orderTable2);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 저장되지_않은_주문_테이블로_테이블_그룹_생성_시_실패() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();
        OrderTableRequest request = toRequest(orderTable1, orderTable2);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 단체_지정_정상_해제() {
        // given
        OrderTableRequest request = toRequest(빈_주문_테이블_생성(), 빈_주문_테이블_생성());
        TableGroup savedTableGroup = tableGroupService.create(request);
        Long tableGroupId = savedTableGroup.getId();

        // when
        tableGroupService.ungroup(tableGroupId);

        // then
        List<OrderTable> actual = orderTableDao.findAllByTableGroupId(tableGroupId);
        assertThat(actual).isEmpty();
    }

    @ParameterizedTest(name = "주문 테이블의 현재 상태: {0}")
    @CsvSource(value = {"MEAL", "COOKING"})
    void 요리_또는_식사_중인_주문_테이블이_있으면_단체_지정_해제_불가능(final OrderStatus orderStatus) {
        // given
        List<OrderTable> orderTables = Arrays.asList(주문_테이블_생성(), 주문_테이블_생성());
        주문_생성(분식_메뉴_생성(), orderTables.get(0), orderStatus);
        TableGroup tableGroup = 단체_지정_생성(orderTables);
        Long tableGroupId = tableGroup.getId();

        // when
        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroupId))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
