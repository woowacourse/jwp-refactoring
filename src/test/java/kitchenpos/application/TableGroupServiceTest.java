package kitchenpos.application;

import static kitchenpos.common.constants.Constants.사용가능_테이블;
import static kitchenpos.common.constants.Constants.사용중인_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.common.builder.OrderTableBuilder;
import kitchenpos.common.builder.TableGroupBuilder;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    private OrderTable 야채곱창_주문_테이블;
    private OrderTable 치킨_주문_테이블;
    private OrderTable 피자_주문_테이블;

    @BeforeEach
    void setUp() {
        야채곱창_주문_테이블 = 주문_테이블_생성(4, 사용가능_테이블);
        치킨_주문_테이블 = 주문_테이블_생성(3, 사용가능_테이블);
        피자_주문_테이블 = 주문_테이블_생성(5, 사용가능_테이블);

        야채곱창_주문_테이블 = orderTableDao.save(야채곱창_주문_테이블);
        치킨_주문_테이블 = orderTableDao.save(치킨_주문_테이블);
        피자_주문_테이블 = orderTableDao.save(피자_주문_테이블);
    }

    @DisplayName("단체 지정을 등록한다.")
    @Test
    void 단체_지정을_등록한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);
        TableGroup 단체_테이블 = 테이블_그룹_생성(주문_테이블들);

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
        TableGroup 단체_테이블 = 테이블_그룹_생성(주문_테이블들);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 null 이면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_null_이면_예외가_발생한다() {
        // given
        List<OrderTable> 주문_테이블들 = null;
        TableGroup 단체_테이블 = 테이블_그룹_생성(주문_테이블들);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_빈_테이블이_아니면_예외가_발생한다() {
        // given
        OrderTable 이미_사용중인_테이블 = 주문_테이블_생성(3, 사용중인_테이블);
        이미_사용중인_테이블 = orderTableDao.save(이미_사용중인_테이블);
        TableGroup 단체_테이블 = 테이블_그룹_생성(List.of(이미_사용중인_테이블));

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 등록할 때, 주문 테이블이 이미 단체 지정되어 있으면 예외가 발생한다.")
    @Test
    void 단체_지정을_등록할_때_주문_테이블이_이미_단체_지정되어_있으면_예외가_발생한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);
        TableGroup 단체_테이블 = 테이블_그룹_생성(주문_테이블들);
        tableGroupService.create(단체_테이블);

        // when & then
        assertThatThrownBy(() -> tableGroupService.create(단체_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정을_해제한다() {
        // given
        List<OrderTable> 주문_테이블들 = List.of(야채곱창_주문_테이블, 치킨_주문_테이블, 피자_주문_테이블);
        TableGroup 단체_테이블 = 테이블_그룹_생성(주문_테이블들);
        단체_테이블 = tableGroupDao.save(단체_테이블);

        // when
        tableGroupService.ungroup(단체_테이블.getId());

        // then
        Long 단체_테이블_아이디 = 단체_테이블.getId();
        assertAll(
                () -> assertThat(orderTableDao.findAllByTableGroupId(단체_테이블_아이디)).isEmpty()
        );
    }

    private TableGroup 테이블_그룹_생성(final List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroupBuilder()
                .orderTables(orderTables)
                .build();
        tableGroup.setCreatedDate(LocalDateTime.now());
        return tableGroup;
    }

    private OrderTable 주문_테이블_생성(final int numberOfGuests, final boolean empty) {
        return new OrderTableBuilder()
                .numberOfGuests(numberOfGuests)
                .empty(empty)
                .build();
    }
}
