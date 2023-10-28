package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.vo.Price;
import kitchenpos.dto.request.CreateOrderRequest;
import kitchenpos.dto.request.OrderLineItemDto;
import kitchenpos.dto.request.PutOrderStatusRequest;
import kitchenpos.infrastructure.persistence.JpaMenuGroupRepository;
import kitchenpos.infrastructure.persistence.JpaMenuRepository;
import kitchenpos.infrastructure.persistence.JpaOrderRepository;
import kitchenpos.infrastructure.persistence.JpaOrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class OrderServiceFixture {

    @Autowired
    private JpaOrderRepository orderRepository;

    @Autowired
    private JpaOrderTableRepository orderTableRepository;

    @Autowired
    private JpaMenuGroupRepository menuGroupRepository;

    @Autowired
    private JpaMenuRepository menuRepository;

    protected long 유효하지_않은_주문_아이디;
    protected CreateOrderRequest 주문_생성_요청_dto;
    protected CreateOrderRequest 주문항목이_1개_미만인_주문_생성_요청_dto;
    protected CreateOrderRequest 주문항목이_2개인_주문_생성_요청_dto;
    protected CreateOrderRequest 유효하지_않은_주문_테이블_아이디를_갖는_주문_생성_요청_dto;
    protected CreateOrderRequest 주문_불가능_상태의_주문_테이블_생성_요청_dto;
    protected Order 조회할_주문_1;
    protected Order 조회할_주문_2;
    protected Order 식사중에서_완료로_상태를_변경할_주문;
    protected PutOrderStatusRequest 주문_상태_변경_요청_dto;
    protected PutOrderStatusRequest 완료_상태인_주문_변경_요청_dto;
    protected PutOrderStatusRequest 잘못된_상태로_수정하고자_하는_주문_변경_요청_dto;
    protected Order COMPLETION_상태의_주문;

    protected void 메뉴를_주문한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹_1 = new MenuGroup("메뉴 그룹 1");
        menuGroupRepository.save(메뉴_그룹_1);

        final Menu 메뉴 = Menu.of(메뉴_그룹_1, "치킨", BigDecimal.valueOf(10_000));
        menuRepository.save(메뉴);

        final OrderTable 주문_테이블 = new OrderTable(3, false);
        orderTableRepository.save(주문_테이블);

        final OrderLineItemDto 주문_항목_아이템_생성_dto = new OrderLineItemDto(메뉴.getId(), 3);

        주문_생성_요청_dto = new CreateOrderRequest(주문_테이블.getId(), List.of(주문_항목_아이템_생성_dto));
    }

    protected void 주문_항목이_1개_미만이면_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹_1 = new MenuGroup("메뉴 그룹 1");
        menuGroupRepository.save(메뉴_그룹_1);

        final Menu 메뉴 = Menu.of(메뉴_그룹_1, "치킨", BigDecimal.valueOf(10_000));
        menuRepository.save(메뉴);

        final OrderTable 주문_테이블 = new OrderTable(3, false);
        orderTableRepository.save(주문_테이블);

        주문항목이_1개_미만인_주문_생성_요청_dto = new CreateOrderRequest(주문_테이블.getId(), Collections.EMPTY_LIST);
    }

    protected void 주문_항목에서_입력받은_메뉴가_올바른_메뉴가_아니라면_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_테이블 = new OrderTable(3, false);
        orderTableRepository.save(주문_테이블);

        final OrderLineItemDto 주문_항목_아이템_생성_dto = new OrderLineItemDto(-999L, 3);

        주문항목이_2개인_주문_생성_요청_dto = new CreateOrderRequest(주문_테이블.getId(), List.of(주문_항목_아이템_생성_dto));
    }

    protected void 유효하지_않은_주문_테이블_아이디라면_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹_1 = new MenuGroup("메뉴 그룹 1");
        menuGroupRepository.save(메뉴_그룹_1);

        final Menu 메뉴 = Menu.of(메뉴_그룹_1, "치킨", BigDecimal.valueOf(10_000));
        menuRepository.save(메뉴);

        final OrderLineItemDto 주문_항목_아이템_생성_dto = new OrderLineItemDto(메뉴.getId(), 3);

        유효하지_않은_주문_테이블_아이디를_갖는_주문_생성_요청_dto = new CreateOrderRequest(-999L, List.of(주문_항목_아이템_생성_dto));
    }

    protected void 주문_테이블_아이디에_해당하는_주문_테이블이_empty_table이라면_예외가_발생한다_픽스처_생성() {
        final MenuGroup 메뉴_그룹_1 = new MenuGroup("메뉴 그룹 1");
        menuGroupRepository.save(메뉴_그룹_1);

        final Menu 메뉴 = Menu.of(메뉴_그룹_1, "치킨", BigDecimal.valueOf(10_000));
        menuRepository.save(메뉴);

        final OrderTable 주문_테이블 = new OrderTable(3, true);
        orderTableRepository.save(주문_테이블);

        final OrderLineItemDto 주문_항목_아이템_생성_dto = new OrderLineItemDto(메뉴.getId(), 3);

        주문_불가능_상태의_주문_테이블_생성_요청_dto = new CreateOrderRequest(주문_테이블.getId(), List.of(주문_항목_아이템_생성_dto));
    }

    protected void 모든_주문내역을_조회한다_픽스처_생성() {
        final OrderTable 주문_테이블_1 = new OrderTable(3, true);
        final OrderTable 주문_테이블_2 = new OrderTable(2, true);
        orderTableRepository.saveAll(List.of(주문_테이블_1, 주문_테이블_2));

        조회할_주문_1 = new Order(주문_테이블_1.getId(), OrderStatus.COOKING);
        조회할_주문_2 = new Order(주문_테이블_2.getId(), OrderStatus.COOKING);
        orderRepository.saveAll(List.of(조회할_주문_1, 조회할_주문_2));
    }

    protected void 주문_상태를_변경한다_픽스처_생성() {
        final OrderTable 주문_상태를_변경할_주문_테이블 = new OrderTable(3, true);
        orderTableRepository.save(주문_상태를_변경할_주문_테이블);

        식사중에서_완료로_상태를_변경할_주문 = new Order(주문_상태를_변경할_주문_테이블.getId(), OrderStatus.MEAL);
        orderRepository.save(식사중에서_완료로_상태를_변경할_주문);

        주문_상태_변경_요청_dto = new PutOrderStatusRequest(OrderStatus.COMPLETION.name());
    }

    protected void 완료된_상태의_주문을_완료된_상태로_변경하는_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_상태를_변경할_주문_테이블 = new OrderTable(3, true);
        orderTableRepository.save(주문_상태를_변경할_주문_테이블);

        COMPLETION_상태의_주문 = new Order(주문_상태를_변경할_주문_테이블.getId(), OrderStatus.COMPLETION);
        orderRepository.save(COMPLETION_상태의_주문);

        주문_상태_변경_요청_dto = new PutOrderStatusRequest(OrderStatus.COMPLETION.name());
    }

    protected void 유효하지_않은_주문_번호를_입력한_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_상태를_변경할_주문_테이블 = new OrderTable(3, true);
        orderTableRepository.save(주문_상태를_변경할_주문_테이블);

        식사중에서_완료로_상태를_변경할_주문 = new Order(주문_상태를_변경할_주문_테이블.getId(), OrderStatus.MEAL);
        orderRepository.save(식사중에서_완료로_상태를_변경할_주문);

        완료_상태인_주문_변경_요청_dto = new PutOrderStatusRequest(OrderStatus.COMPLETION.name());
    }

    protected void order_status가_잘못_입력된_경우_예외가_발생한다_픽스처_생성() {
        final OrderTable 주문_상태를_변경할_주문_테이블 = new OrderTable(3, true);
        orderTableRepository.save(주문_상태를_변경할_주문_테이블);

        식사중에서_완료로_상태를_변경할_주문 = new Order(주문_상태를_변경할_주문_테이블.getId(), OrderStatus.MEAL);
        orderRepository.save(식사중에서_완료로_상태를_변경할_주문);

        잘못된_상태로_수정하고자_하는_주문_변경_요청_dto = new PutOrderStatusRequest("INVALID");
    }
}
