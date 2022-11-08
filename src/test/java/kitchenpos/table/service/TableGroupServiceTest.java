package kitchenpos.table.service;

import static kitchenpos.common.Constants.사용가능_테이블;
import static kitchenpos.common.Constants.사용중인_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import kitchenpos.common.annotation.ServiceTest;
import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.common.builder.TableGroupBuilder;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    private final TableGroupService tableGroupService;
    private final TableGroupRepository tableGroupRepository;
    private final OrderTableRepository orderTableRepository;

    private OrderTable 야채곱창_주문_테이블;
    private OrderTable 치킨_주문_테이블;
    private OrderTable 피자_주문_테이블;

    @Autowired
    TableGroupServiceTest(final TableGroupService tableGroupService,
                          final TableGroupRepository tableGroupRepository,
                          final OrderTableRepository orderTableRepository) {
        this.tableGroupService = tableGroupService;
        this.tableGroupRepository = tableGroupRepository;
        this.orderTableRepository = orderTableRepository;
    }

    @BeforeEach
    void setUp() {
        야채곱창_주문_테이블 = 주문_테이블_생성(4, 사용가능_테이블);
        치킨_주문_테이블 = 주문_테이블_생성(3, 사용가능_테이블);
        피자_주문_테이블 = 주문_테이블_생성(5, 사용가능_테이블);

        야채곱창_주문_테이블 = orderTableRepository.save(야채곱창_주문_테이블);
        치킨_주문_테이블 = orderTableRepository.save(치킨_주문_테이블);
        피자_주문_테이블 = orderTableRepository.save(피자_주문_테이블);
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void 단체_지정을_등록한다() {
        // given
        List<Long> 주문_테이블들_아아디 = List.of(야채곱창_주문_테이블.getId(), 치킨_주문_테이블.getId(), 피자_주문_테이블.getId());
        TableGroupCreateRequest 단체_테이블_생성_요청 = new TableGroupCreateRequest(주문_테이블들_아아디);

        // when
        TableGroupResponse actual = tableGroupService.create(단체_테이블_생성_요청);

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
        List<Long> 주문_테이블들_아아디 = List.of(야채곱창_주문_테이블.getId());
        TableGroupCreateRequest 단체_테이블_생성_요청 = new TableGroupCreateRequest(주문_테이블들_아아디);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_생성_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정은 3개 이상의 정수로 입력해주세요.");
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 없으면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_없으면_예외가_발생한다() {
        // given
        List<Long> 주문_테이블들_아아디 = new ArrayList<>();
        TableGroupCreateRequest 단체_테이블_생성_요청 = new TableGroupCreateRequest(주문_테이블들_아아디);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_생성_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("단체 지정은 3개 이상의 정수로 입력해주세요.");
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_빈_테이블이_아니면_예외가_발생한다() {
        // given
        OrderTable 이미_사용중인_테이블 = 주문_테이블_생성(3, 사용중인_테이블);
        이미_사용중인_테이블 = orderTableRepository.save(이미_사용중인_테이블);
        List<Long> 주문_테이블들_아아디 = List.of(이미_사용중인_테이블.getId(), 피자_주문_테이블.getId());
        TableGroupCreateRequest 단체_테이블_생성_요청 = new TableGroupCreateRequest(주문_테이블들_아아디);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_생성_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 테이블 입니다.");
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 이미 단체 지정되어 있으면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_이미_단체_지정되어_있으면_예외가_발생한다() {
        // given
        List<Long> 주문_테이블들_아아디 = List.of(야채곱창_주문_테이블.getId(), 치킨_주문_테이블.getId(), 피자_주문_테이블.getId());
        TableGroupCreateRequest 단체_테이블_생성_요청 = new TableGroupCreateRequest(주문_테이블들_아아디);
        tableGroupService.create(단체_테이블_생성_요청);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블_생성_요청))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 사용중인 테이블 입니다.");
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정을_해제한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);
        TableGroup 단체_테이블 = 테이블_그룹_생성(주문_테이블들);
        단체_테이블 = tableGroupRepository.save(단체_테이블);

        // when
        tableGroupService.ungroup(단체_테이블.getId());

        // then
        Long 단체_테이블_아이디 = 단체_테이블.getId();
        assertAll(
                () -> assertThat(orderTableRepository.findAllByTableGroupId(단체_테이블_아이디)).isEmpty()
        );
    }

    private TableGroup 테이블_그룹_생성(final List<OrderTable> orderTables) {
        return new TableGroupBuilder()
                .orderTables(orderTables)
                .build();
    }

    private OrderTable 주문_테이블_생성(final int numberOfGuests, final boolean empty) {
        return new OrderTableBuilder()
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }
}
