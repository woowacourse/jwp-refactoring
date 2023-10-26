package kitchenpos.table.application;

import static kitchenpos.support.TestFixtureFactory.새로운_주문;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블;
import static kitchenpos.support.TestFixtureFactory.새로운_주문_테이블_로그;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.order.application.OrderTableChangeEventHandler;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableLogRepository;
import kitchenpos.order.exception.OrderException;
import kitchenpos.support.ServiceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.request.OrderTableRequest;
import kitchenpos.table.dto.request.TableGroupCreateRequest;
import kitchenpos.table.dto.response.OrderTableResponse;
import kitchenpos.table.dto.response.TableGroupResponse;
import kitchenpos.table.exception.OrderTableException;
import kitchenpos.table.exception.TableGroupException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class TableGroupServiceTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableLogRepository orderTableLogRepository;

    @Autowired
    private OrderTableChangeEventHandler orderTableChangeEventHandler;

    @Autowired
    private TableGroupService tableGroupService;

    @Test
    void 테이블들을_단체로_지정한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTableRequest 주문_테이블1_요청 = new OrderTableRequest(주문_테이블1.getId());
        OrderTableRequest 주문_테이블2_요청 = new OrderTableRequest(주문_테이블2.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1_요청, 주문_테이블2_요청));

        TableGroupResponse 등록된_단체_지정_응답 = tableGroupService.create(단체_지정_생성_요청);

        assertSoftly(softly -> {
            softly.assertThat(등록된_단체_지정_응답.getId()).isNotNull();
            softly.assertThat(등록된_단체_지정_응답.getOrderTables())
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(OrderTableResponse.from(주문_테이블1), OrderTableResponse.from(주문_테이블2));
        });
    }

    @Test
    void 단체_지정하려는_테이블은_2개_이상이어야_한다() {
        OrderTable 주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTableRequest 주문_테이블_요청 = new OrderTableRequest(주문_테이블.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블_요청));

        assertThatThrownBy(() -> tableGroupService.create(단체_지정_생성_요청))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("주문 테이블의 수가 유효하지 않습니다.");
    }

    @Test
    void 존재하지_않는_테이블을_단체로_지정할_수_없다() {
        OrderTable 등록된_주문_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 등록되지_않은_주문_테이블 = 새로운_주문_테이블(null, 2, true);
        OrderTableRequest 등록된_주문_테이블_요청 = new OrderTableRequest(등록된_주문_테이블.getId());
        OrderTableRequest 등록되지_않은_주문_테이블_요청 = new OrderTableRequest(등록되지_않은_주문_테이블.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(등록된_주문_테이블_요청, 등록되지_않은_주문_테이블_요청));

        assertThatThrownBy(() -> tableGroupService.create(단체_지정_생성_요청))
                .isInstanceOf(OrderTableException.class)
                .hasMessage("존재하지 않는 주문 테이블의 ID가 포함되어 있습니다.");
    }

    @Test
    void 비어있지_않은_테이블을_단체로_지정할_수_없다() {
        OrderTable 빈_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 비어있지_않은_테이블 = orderTableRepository.save(새로운_주문_테이블(null, 2, false));

        OrderTableRequest 빈_테이블_요청 = new OrderTableRequest(빈_테이블.getId());
        OrderTableRequest 비어있지_않은_테이블_요청 = new OrderTableRequest(비어있지_않은_테이블.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(빈_테이블_요청, 비어있지_않은_테이블_요청));

        assertThatThrownBy(() -> tableGroupService.create(단체_지정_생성_요청))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("주문 테이블의 상태가 유효하지 않습니다.");
    }

    @Test
    void 이미_지정된_테이블을_단체_지정할_수_없다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 주문_테이블3 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));

        OrderTableRequest 주문_테이블1_요청 = new OrderTableRequest(주문_테이블1.getId());
        OrderTableRequest 주문_테이블2_요청 = new OrderTableRequest(주문_테이블2.getId());
        OrderTableRequest 주문_테이블3_요청 = new OrderTableRequest(주문_테이블3.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1_요청, 주문_테이블2_요청));
        tableGroupService.create(단체_지정_생성_요청);

        TableGroupCreateRequest 또다른_단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블2_요청, 주문_테이블3_요청));
        assertThatThrownBy(() -> tableGroupService.create(또다른_단체_지정_생성_요청))
                .isInstanceOf(TableGroupException.class)
                .hasMessage("주문 테이블의 상태가 유효하지 않습니다.");
    }

    @Test
    void 단체_지정을_삭제한다() {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTableRequest 주문_테이블1_요청 = new OrderTableRequest(주문_테이블1.getId());
        OrderTableRequest 주문_테이블2_요청 = new OrderTableRequest(주문_테이블2.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1_요청, 주문_테이블2_요청));
        TableGroupResponse 단체_지정_생성_응답 = tableGroupService.create(단체_지정_생성_요청);

        tableGroupService.ungroup(단체_지정_생성_응답.getId());

        assertSoftly(softly -> {
            softly.assertThat(주문_테이블1.getTableGroupId()).isNull();
            softly.assertThat(주문_테이블2.getTableGroupId()).isNull();
        });
    }

    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = {"COOKING", "MEAL"})
    void 단체_지정된_주문_테이블의_주문_상태가_조리_또는_식사인_경우_단체_지정을_삭제할_수_없다(OrderStatus orderStatus) {
        OrderTable 주문_테이블1 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTable 주문_테이블2 = orderTableRepository.save(새로운_주문_테이블(null, 0, true));
        OrderTableRequest 주문_테이블1_요청 = new OrderTableRequest(주문_테이블1.getId());
        OrderTableRequest 주문_테이블2_요청 = new OrderTableRequest(주문_테이블2.getId());

        TableGroupCreateRequest 단체_지정_생성_요청 = new TableGroupCreateRequest(List.of(주문_테이블1_요청, 주문_테이블2_요청));

        TableGroupResponse 등록된_단체_지정_응답 = tableGroupService.create(단체_지정_생성_요청);

        주문_테이블1.changeEmpty(false);
        주문_테이블1.changeNumberOfGuests(3);

        Order order = orderRepository.save(새로운_주문(orderStatus, LocalDateTime.now()));
        orderTableLogRepository.save(새로운_주문_테이블_로그(order, 주문_테이블1.getId(), 3));

        assertThatThrownBy(() -> tableGroupService.ungroup(등록된_단체_지정_응답.getId()))
                .isInstanceOf(OrderException.class)
                .hasMessage("주문 상태가 주문 완료가 아닙니다.");
    }
}
