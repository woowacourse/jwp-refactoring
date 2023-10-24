package kitchenpos.application.fixture;

import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class TableServiceFixture {

    protected Long 존재하지_않는_주문_테이블_아이디 = -999L;
    protected OrderTable 저장된_주문_테이블;
    protected int 방문한_손님_수 = 4;
    protected OrderTable 저장된_주문_테이블1;
    protected OrderTable 저장된_주문_테이블2;
    protected OrderTable 저장된_주문_테이블3;
    protected OrderTable empty가_참인_주문_테이블;
    protected OrderTable empty가_거짓인_주문_테이블;
    protected OrderTable empty가_참인_저장된_주문_테이블;
    protected OrderTable empty가_거짓인_저장된_주문_테이블;
    protected OrderTable 단체_지정_아이디가_있는_주문_테이블;
    protected OrderTable 방문자수가_4인_저장된_주문_테이블;
    protected OrderTable 방문자수가_2인_주문_테이블;
    protected OrderTable 방문자수가_2인_저장된_주문_테이블;
    protected OrderTable 방문자수가_0인_주문_테이블;
    protected List<OrderTable> 저장된_주문_테이블들;

    @BeforeEach
    void setUp() {
        저장된_주문_테이블 = new OrderTable(방문한_손님_수, false);
        저장된_주문_테이블.setId(1L);
        저장된_주문_테이블1 = 저장된_주문_테이블;
        저장된_주문_테이블2 = new OrderTable(5, false);
        저장된_주문_테이블2.setId(2L);
        저장된_주문_테이블3 = new OrderTable(6, false);
        저장된_주문_테이블3.setId(3L);
        empty가_참인_저장된_주문_테이블 = new OrderTable(3, true);
        empty가_참인_저장된_주문_테이블.setId(4L);
        empty가_거짓인_저장된_주문_테이블 = new OrderTable(3, false);
        empty가_거짓인_저장된_주문_테이블.setId(4L);
        empty가_참인_주문_테이블 = new OrderTable(3, true);
        empty가_거짓인_주문_테이블 = new OrderTable(3, false);
        단체_지정_아이디가_있는_주문_테이블 = new OrderTable(3, false);
        단체_지정_아이디가_있는_주문_테이블.setId(5L);
        단체_지정_아이디가_있는_주문_테이블.updateTableGroup(null);
        방문자수가_4인_저장된_주문_테이블 = 저장된_주문_테이블;
        방문자수가_2인_저장된_주문_테이블 = new OrderTable(2, false);
        단체_지정_아이디가_있는_주문_테이블.setId(1L);
        방문자수가_2인_주문_테이블 = new OrderTable(2, false);
        방문자수가_0인_주문_테이블 = new OrderTable(0, false);

        저장된_주문_테이블들 = List.of(저장된_주문_테이블1, 저장된_주문_테이블2, 저장된_주문_테이블3);
    }
}
