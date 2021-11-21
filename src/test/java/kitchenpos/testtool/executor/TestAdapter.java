package kitchenpos.testtool.executor;


import kitchenpos.testtool.response.HttpResponse;
import kitchenpos.testtool.util.RequestDto;

public interface TestAdapter {

    boolean isAssignable(RequestDto requestDto);

    HttpResponse execute(RequestDto requestDto);
}
