package kitchenpos.test;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TestTransactional {

    @Transactional
    public void invoke(Runnable runnable) {
        runnable.run();
    }
}
