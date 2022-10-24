package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.common.builder.TableGroupBuilder;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    private OrderTable 야채곱창_주문_테이블;
    private OrderTable 치킨_주문_테이블;
    private OrderTable 피자_주문_테이블;

    @BeforeEach
    void setUp() {
        야채곱창_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(4)
                .empty(true)
                .build();

        치킨_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(true)
                .build();

        피자_주문_테이블 = new OrderTableBuilder()
                .numberOfGuests(5)
                .empty(true)
                .build();

        야채곱창_주문_테이블 = tableService.create(야채곱창_주문_테이블);
        치킨_주문_테이블 = tableService.create(치킨_주문_테이블);
        피자_주문_테이블 = tableService.create(피자_주문_테이블);
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void 단체_지정을_등록한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);

        TableGroup 단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();

        // when
        TableGroup actual = tableGroupService.create(단체_테이블);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTables()).hasSize(3)
        );
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 두개미만 이면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_두개미만_이면_예외가_발생한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(피자_주문_테이블);

        TableGroup 단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 null 이면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_null_이면_예외가_발생한다() {
        // given
        List<OrderTable> 주문_테이블들 = null;

        TableGroup 단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_빈_테이블이_아니면_예외가_발생한다() {
        // given
        OrderTable 이미_사용중인_테이블 = new OrderTableBuilder()
                .numberOfGuests(3)
                .empty(false)
                .build();
        이미_사용중인_테이블 = tableService.create(이미_사용중인_테이블);

        List<OrderTable> 주문_테이블들 = List.of(이미_사용중인_테이블, 야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);

        TableGroup 단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 이미 단체 지정되어 있으면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_이미_단체_지정되어_있으면_예외가_발생한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);

        TableGroup 단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();
        tableGroupService.create(단체_테이블);

        TableGroup 새로운_단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(새로운_단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정을_해제한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);

        TableGroup 단체_테이블 = new TableGroupBuilder()
                .orderTables(주문_테이블들)
                .build();
        단체_테이블 = tableGroupService.create(단체_테이블);

        // when & then
        tableGroupService.ungroup(단체_테이블.getId());
    }
}