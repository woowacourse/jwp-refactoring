package kitchenpos;

import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.menugroup.fixture.MenuGroupFixture;
import kitchenpos.order.fixture.OrderFixture;
import kitchenpos.order.fixture.OrderLineItemFixture;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.fixture.OrderTableFixture;
import kitchenpos.table.fixture.TableGroupFixture;
import kitchenpos.testtool.RequestBuilder;
import kitchenpos.testtool.request.RequestApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AcceptanceTest {

    @Autowired
    private RequestBuilder request;
    @Autowired
    protected MenuFixture menuFixture;
    @Autowired
    protected OrderFixture orderFixture;
    @Autowired
    protected OrderLineItemFixture orderLineItemFixture;
    @Autowired
    protected OrderTableFixture orderTableFixture;
    @Autowired
    protected ProductFixture productFixture;
    @Autowired
    protected TableGroupFixture tableGroupFixture;
    @Autowired
    protected MenuGroupFixture menuGroupFixture;
    @Autowired
    protected MenuProductFixture menuProductFixture;

    protected RequestApi request() {
        return request.builder();
    }
}
