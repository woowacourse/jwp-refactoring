package kitchenpos;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuGroupFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.OrderTableFixture;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.fixture.TableGroupFixture;
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
