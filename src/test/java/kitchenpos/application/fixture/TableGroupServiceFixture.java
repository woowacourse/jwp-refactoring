package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableGroupServiceFixture {

    protected OrderTable 주문_테이블1;
    protected OrderTable 주문_테이블2;
    protected OrderTable 빈_주문_테이블;
    protected TableGroup 저장된_단체_지정;
    protected List<OrderTable> 주문_테이블들;
    protected List<OrderTable> 빈_주문_테이블들 = new ArrayList<>();
    protected List<OrderTable> 빈_주문_테이블이_포함된_주문_테이블들;
    protected List<OrderTable> 단체_지정이_null이_아닌_테이블이_포함된_주문_테이블들;

    @BeforeEach
    void setUp() {
        주문_테이블1 = new OrderTable(1, true);
        주문_테이블1.setId(1L);
        주문_테이블2 = new OrderTable(2, true);
        주문_테이블2.setId(2L);
        빈_주문_테이블 = new OrderTable(2, false);
        빈_주문_테이블.setId(3L);
        OrderTable 단체_지정이_null이_아닌_주문_테이블 = new OrderTable(2, true);
        단체_지정이_null이_아닌_주문_테이블.setId(4L);
        단체_지정이_null이_아닌_주문_테이블.updateTableGroup(null);
        주문_테이블들 = List.of(주문_테이블1, 주문_테이블2);
        빈_주문_테이블이_포함된_주문_테이블들 = List.of(빈_주문_테이블, 주문_테이블2);

        저장된_단체_지정 = new TableGroup(LocalDateTime.now(), 주문_테이블들);
        저장된_단체_지정.setId(1L);

        단체_지정이_null이_아닌_테이블이_포함된_주문_테이블들 = List.of(빈_주문_테이블, 단체_지정이_null이_아닌_주문_테이블);
    }
}
