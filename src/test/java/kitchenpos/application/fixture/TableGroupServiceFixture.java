package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupServiceFixture {

    protected TableGroup 생성한_테이블그룹;
    protected List<OrderTable> 그룹화할_주문테이블_리스트;
    protected List<OrderTable> 주문_가능상태로_변경된_주문테이블_리스트;
    protected TableGroup 생성할_테이블그룹;
    protected TableGroup 주문_테이블이_없는_테이블그룹;
    protected TableGroup 주문_테이블이_1개인_테이블그룹;
    protected List<OrderTable> 사용가능한_테이블을_포함한_주문테이블_리스트;
    protected TableGroup 사용가능한_테이블을_포함한_테이블그룹;
    protected List<OrderTable> 삭제할_주문테이블_리스트;
    protected TableGroup 삭제할_테이블그룹;
    protected long 식사중인_주문_아이디 = 1L;

    @BeforeEach
    void setUp() {
        final OrderTable 주문테이블1 = new OrderTable();
        final OrderTable 주문테이블2 = new OrderTable();
        주문테이블1.setId(1L);
        주문테이블1.setEmpty(false);
        주문테이블2.setId(2L);
        주문테이블2.setEmpty(false);

        그룹화할_주문테이블_리스트 = List.of(주문테이블1, 주문테이블2);

        생성할_테이블그룹 = new TableGroup();
        생성할_테이블그룹.setOrderTables(그룹화할_주문테이블_리스트);

        final OrderTable 주문_가능상태로_변경된_주문테이블1 = 주문테이블1;
        final OrderTable 주문_가능상태로_변경된_주문테이블2 = 주문테이블2;
        주문_가능상태로_변경된_주문테이블1.setEmpty(true);
        주문_가능상태로_변경된_주문테이블2.setEmpty(true);

        주문_가능상태로_변경된_주문테이블_리스트 = List.of(주문_가능상태로_변경된_주문테이블1, 주문_가능상태로_변경된_주문테이블1);

        생성한_테이블그룹 = new TableGroup();
        생성한_테이블그룹.setOrderTables(주문_가능상태로_변경된_주문테이블_리스트);

        // 테이블_아이디가_입력되지_않은_경우_예외가_발생한다
        주문_테이블이_없는_테이블그룹 = new TableGroup();
        주문_테이블이_없는_테이블그룹.setOrderTables(Collections.EMPTY_LIST);

        // 테이블_아이디가_1개인_경우_예외가_발생한다
        주문_테이블이_1개인_테이블그룹 = new TableGroup();
        주문_테이블이_1개인_테이블그룹.setOrderTables(List.of(주문테이블1));

        // 주문테이블이_사용가능한_테이블인_경우_예외가_발생한다
        final OrderTable 사용가능한_주문_테이블1 = new OrderTable();
        final OrderTable 사용가능한_주문_테이블2 = new OrderTable();
        사용가능한_주문_테이블1.setId(1L);
        사용가능한_주문_테이블1.setEmpty(false);
        사용가능한_주문_테이블2.setId(2L);
        사용가능한_주문_테이블2.setEmpty(false);

        사용가능한_테이블을_포함한_주문테이블_리스트 = List.of(사용가능한_주문_테이블1, 사용가능한_주문_테이블2);

        사용가능한_테이블을_포함한_테이블그룹 = new TableGroup();
        사용가능한_테이블을_포함한_테이블그룹.setId(1L);
        사용가능한_테이블을_포함한_테이블그룹.setOrderTables(사용가능한_테이블을_포함한_주문테이블_리스트);

        // 단체_테이블을_삭제할_수_있다
        final OrderTable 삭제할_주문테이블1 = new OrderTable();
        final OrderTable 삭제할_주문테이블2 = new OrderTable();
        삭제할_주문테이블1.setId(1L);
        삭제할_주문테이블1.setEmpty(true);
        삭제할_주문테이블2.setId(2L);
        삭제할_주문테이블2.setEmpty(true);

        삭제할_주문테이블_리스트 = List.of(삭제할_주문테이블1, 삭제할_주문테이블2);
        삭제할_테이블그룹 = new TableGroup();
        삭제할_테이블그룹.setId(1L);
        삭제할_테이블그룹.setOrderTables(삭제할_주문테이블_리스트);
    }
}
