package kitchenpos.application.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderTable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class OrderServiceFixture {

    protected Order 요청된_주문;
    protected Order 저장한_주문;
    protected OrderTable 주문_생성시_사용하는_주문_테이블;
    protected OrderLineItem 저장한_첫번째_주문항목;
    protected OrderLineItem 저장한_두번째_주문항목;
    protected Order 주문항목이_1개_미만인_주문;
    protected Order 주문항목이_2개인_주문;
    protected Order 유효하지_않은_주문_테이블_아이디를_갖는_주문;
    protected OrderTable empty가_true인_주문_테이블;
    protected List<Order> 저장된_주문_리스트;
    protected Order 상태를_변경할_식사중인_주문;
    protected Order 식사중에서_완료로_상태변경된_주문;
    protected Order 완료_상태인_주문;
    protected Order 잘못된_상태로_수정하고자_하는_주문;
    protected long 잘못된_상태로_수정하고자_하는_주문_아이디;
    protected long 유효하지_않은_주문_아이디;
    protected long 유효하지_않은_주문_테이블_아이디;

    protected void 메뉴를_주문한다_픽스처_생성() {
        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        저장한_주문 = new Order();
        저장한_주문.setOrderTableId(요청된_주문.getOrderTableId());
        저장한_주문.setOrderedTime(LocalDateTime.now());
        저장한_주문.setOrderLineItems(요청된_주문.getOrderLineItems());
    }

    protected void 주문_항목이_1개_미만이면_예외가_발생한다_픽스처_생성() {
        주문항목이_1개_미만인_주문 = new Order();
        주문항목이_1개_미만인_주문.setOrderLineItems(Collections.EMPTY_LIST);
    }

    protected void 주문_항목에서_입력받은_메뉴가_올바른_메뉴가_아니라면_예외가_발생한다_픽스처_생성() {
        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        주문항목이_2개인_주문 = new Order();
        주문항목이_2개인_주문.setOrderLineItems(요청된_주문.getOrderLineItems());
    }

    protected void 유효하지_않은_주문_테이블_아이디라면_예외가_발생한다_픽스처_생성() {
        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        유효하지_않은_주문_테이블_아이디 = -999L;
        유효하지_않은_주문_테이블_아이디를_갖는_주문 = new Order();
        유효하지_않은_주문_테이블_아이디를_갖는_주문.setOrderTableId(유효하지_않은_주문_테이블_아이디);
        유효하지_않은_주문_테이블_아이디를_갖는_주문.setOrderLineItems(요청된_주문.getOrderLineItems());
    }

    protected void 주문_테이블_아이디에_해당하는_주문_테이블이_empty_table이라면_예외가_발생한다_픽스처_생성() {
        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        empty가_true인_주문_테이블 = new OrderTable();
        empty가_true인_주문_테이블.setId(1L);
        empty가_true인_주문_테이블.setEmpty(true);
        유효하지_않은_주문_테이블_아이디를_갖는_주문 = new Order();
        유효하지_않은_주문_테이블_아이디를_갖는_주문.setOrderTableId(empty가_true인_주문_테이블.getId());
        유효하지_않은_주문_테이블_아이디를_갖는_주문.setOrderLineItems(요청된_주문.getOrderLineItems());
    }

    protected void 모든_주문내역을_조회한다_픽스처_생성() {
        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        저장한_주문 = new Order();
        저장한_주문.setOrderTableId(요청된_주문.getOrderTableId());
        저장한_주문.setOrderedTime(LocalDateTime.now());
        저장한_주문.setOrderLineItems(요청된_주문.getOrderLineItems());

        저장된_주문_리스트 = List.of(저장한_주문);
    }

    protected void 주문_상태를_변경한다_픽스처_생성() {
        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        상태를_변경할_식사중인_주문 = new Order();
        상태를_변경할_식사중인_주문.setId(1L);
        상태를_변경할_식사중인_주문.setOrderStatus("MEAL");
        상태를_변경할_식사중인_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        상태를_변경할_식사중인_주문.setOrderedTime(LocalDateTime.now());
        상태를_변경할_식사중인_주문.setOrderLineItems(요청된_주문.getOrderLineItems());

        식사중에서_완료로_상태변경된_주문 = new Order();

        식사중에서_완료로_상태변경된_주문.setOrderStatus("COMPLETION");
        식사중에서_완료로_상태변경된_주문.setId(상태를_변경할_식사중인_주문.getId());
        식사중에서_완료로_상태변경된_주문.setOrderTableId(상태를_변경할_식사중인_주문.getOrderTableId());
        식사중에서_완료로_상태변경된_주문.setOrderedTime(상태를_변경할_식사중인_주문.getOrderedTime());
        식사중에서_완료로_상태변경된_주문.setOrderLineItems(상태를_변경할_식사중인_주문.getOrderLineItems());
    }

    protected void 유효하지_않은_주문_번호를_입력한_경우_예외가_발생한다_픽스처_생성() {
        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        유효하지_않은_주문_아이디 = -999L;
        완료_상태인_주문 = new Order();
        완료_상태인_주문.setId(1L);
        완료_상태인_주문.setOrderStatus("COMPLETION");
        완료_상태인_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        완료_상태인_주문.setOrderedTime(LocalDateTime.now());
        완료_상태인_주문.setOrderLineItems(요청된_주문.getOrderLineItems());
    }

    protected void order_status가_잘못_입력된_경우_예외가_발생한다_픽스처_생성() {
        주문_생성시_사용하는_주문_테이블 = new OrderTable();
        주문_생성시_사용하는_주문_테이블.setId(1L);
        주문_생성시_사용하는_주문_테이블.setEmpty(false);

        final Menu 메뉴 = new Menu();
        메뉴.setId(1L);

        저장한_첫번째_주문항목 = new OrderLineItem();
        저장한_두번째_주문항목 = new OrderLineItem();
        저장한_첫번째_주문항목.setMenuId(메뉴.getId());
        저장한_두번째_주문항목.setMenuId(메뉴.getId());

        요청된_주문 = new Order();
        요청된_주문.setOrderTableId(주문_생성시_사용하는_주문_테이블.getId());
        요청된_주문.setOrderedTime(LocalDateTime.now());
        요청된_주문.setOrderLineItems(List.of(저장한_첫번째_주문항목, 저장한_두번째_주문항목));

        잘못된_상태로_수정하고자_하는_주문_아이디 = 1L;
        잘못된_상태로_수정하고자_하는_주문 = new Order();
        잘못된_상태로_수정하고자_하는_주문.setOrderTableId(잘못된_상태로_수정하고자_하는_주문_아이디);
        잘못된_상태로_수정하고자_하는_주문.setOrderStatus("INVALID STATUS");
        잘못된_상태로_수정하고자_하는_주문.setOrderedTime(LocalDateTime.now());
        잘못된_상태로_수정하고자_하는_주문.setOrderLineItems(요청된_주문.getOrderLineItems());
    }
}
