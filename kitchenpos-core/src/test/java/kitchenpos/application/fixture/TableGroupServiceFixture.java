package kitchenpos.application.fixture;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.common.dto.request.CreateTableGroupRequest;
import kitchenpos.common.dto.request.OrderTableDto;
import kitchenpos.order.persistence.JpaOrderRepository;
import kitchenpos.ordertable.persistence.JpaOrderTableRepository;
import kitchenpos.tablegroup.persistence.JpaTableGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupServiceFixture {

    @Autowired
    private JpaOrderTableRepository orderTableRepository;

    @Autowired
    private JpaTableGroupRepository tableGroupRepository;

    @Autowired
    private JpaOrderRepository orderRepository;

    protected CreateTableGroupRequest 생성할_테이블그룹_요청_dto;
    protected TableGroup 삭제할_테이블_그룹;
    protected CreateTableGroupRequest 주문_테이블이_없는_테이블그룹_요청_dto;
    protected CreateTableGroupRequest 주문_테이블이_1개인_테이블그룹_요청_dto;
    protected CreateTableGroupRequest 사용가능한_테이블을_포함한_테이블그룹_요청_dto;
    protected List<OrderTable> 삭제할_주문테이블에_포함된_주문_테이블_리스트;
    protected TableGroup 식사중인_단체_테이블;

    protected void 단체_테이블을_등록할_수_있다_픽스처_생성() {
        final OrderTable 주문_테이블1 = new OrderTable(1, false);
        final OrderTable 주문_테이블2 = new OrderTable(2, false);
        orderTableRepository.saveAll(List.of(주문_테이블1, 주문_테이블2));

        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_1 = new OrderTableDto(주문_테이블1.getId());
        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_2 = new OrderTableDto(주문_테이블2.getId());
        생성할_테이블그룹_요청_dto = new CreateTableGroupRequest(List.of(그룹화할_주문_테이블에_포함될_주문_테이블_1, 그룹화할_주문_테이블에_포함될_주문_테이블_2));
    }

    protected void 단체_테이블_등록시_주문_테이블_연관관계_세팅_이벤트가_호출된다_픽스처_생성() {
        final OrderTable 주문_테이블1 = new OrderTable(1, false);
        final OrderTable 주문_테이블2 = new OrderTable(2, false);
        orderTableRepository.saveAll(List.of(주문_테이블1, 주문_테이블2));

        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_1 = new OrderTableDto(주문_테이블1.getId());
        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_2 = new OrderTableDto(주문_테이블2.getId());
        생성할_테이블그룹_요청_dto = new CreateTableGroupRequest(List.of(그룹화할_주문_테이블에_포함될_주문_테이블_1, 그룹화할_주문_테이블에_포함될_주문_테이블_2));
    }

    protected void 주문_테이블_아이디가_입력되지_않은_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_테이블1 = new OrderTable(1, false);
        final OrderTable 주문_테이블2 = new OrderTable(2, false);
        orderTableRepository.saveAll(List.of(주문_테이블1, 주문_테이블2));

        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_1 = new OrderTableDto(주문_테이블1.getId());
        주문_테이블이_없는_테이블그룹_요청_dto = new CreateTableGroupRequest(List.of(그룹화할_주문_테이블에_포함될_주문_테이블_1));
    }

    protected void 주문_테이블_아이디가_1개인_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_테이블1 = new OrderTable(1, false);
        orderTableRepository.save(주문_테이블1);

        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_1 = new OrderTableDto(주문_테이블1.getId());
        주문_테이블이_1개인_테이블그룹_요청_dto = new CreateTableGroupRequest(List.of(그룹화할_주문_테이블에_포함될_주문_테이블_1));
    }

    protected void 주문_테이블이_사용가능한_테이블인_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_테이블1 = new OrderTable(1, false);
        final OrderTable 주문_테이블2 = new OrderTable(2, false);
        주문_테이블2.changeEmptyStatus(true);
        orderTableRepository.saveAll(List.of(주문_테이블1, 주문_테이블2));

        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_1 = new OrderTableDto(주문_테이블1.getId());
        final OrderTableDto 그룹화할_주문_테이블에_포함될_주문_테이블_2 = new OrderTableDto(주문_테이블2.getId());
        사용가능한_테이블을_포함한_테이블그룹_요청_dto = new CreateTableGroupRequest(List.of(그룹화할_주문_테이블에_포함될_주문_테이블_1, 그룹화할_주문_테이블에_포함될_주문_테이블_2));
    }

    protected void 단체_테이블을_삭제할_수_있다_픽스처_생성() {
        final OrderTable 삭제할_주문_테이블_1 = new OrderTable(1, false);
        final OrderTable 삭제할_주문_테이블_2 = new OrderTable(2, false);
        삭제할_주문테이블에_포함된_주문_테이블_리스트 = List.of(삭제할_주문_테이블_1, 삭제할_주문_테이블_2);
        orderTableRepository.saveAll(삭제할_주문테이블에_포함된_주문_테이블_리스트);

        final Order 주문_1 = new Order(삭제할_주문_테이블_1.getId(), OrderStatus.COOKING);
        final Order 주문_2 = new Order(삭제할_주문_테이블_2.getId(), OrderStatus.COOKING);
        orderRepository.saveAll(List.of(주문_1, 주문_2));

        삭제할_테이블_그룹 = new TableGroup(LocalDateTime.now());
        tableGroupRepository.save(삭제할_테이블_그룹);
    }

    protected void 단체_테이블을_삭제하는_경우_이벤트를_호출한다_픽스처_생성() {
        final OrderTable 삭제할_주문_테이블_1 = new OrderTable(1, false);
        final OrderTable 삭제할_주문_테이블_2 = new OrderTable(2, false);
        삭제할_주문테이블에_포함된_주문_테이블_리스트 = List.of(삭제할_주문_테이블_1, 삭제할_주문_테이블_2);
        orderTableRepository.saveAll(삭제할_주문테이블에_포함된_주문_테이블_리스트);

        final Order 주문_1 = new Order(삭제할_주문_테이블_1.getId(), OrderStatus.COOKING);
        final Order 주문_2 = new Order(삭제할_주문_테이블_2.getId(), OrderStatus.COOKING);
        orderRepository.saveAll(List.of(주문_1, 주문_2));

        삭제할_테이블_그룹 = new TableGroup(LocalDateTime.now());
        tableGroupRepository.save(삭제할_테이블_그룹);
    }

    protected void 단체_테이블에_포함된_주문_테이블_중_주문_상태가_조리_또는_식사인_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_테이블1 = new OrderTable(1, false);
        final OrderTable 주문_테이블2 = new OrderTable(2, false);
        orderTableRepository.saveAll(List.of(주문_테이블1, 주문_테이블2));

        final Order 주문_1 = new Order(주문_테이블1.getId(), OrderStatus.COOKING);
        final Order 주문_2 = new Order(주문_테이블2.getId(), OrderStatus.MEAL);
        orderRepository.saveAll(List.of(주문_1, 주문_2));

        식사중인_단체_테이블 = new TableGroup(LocalDateTime.now());
    }
}
