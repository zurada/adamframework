package beans;

import org.adam.framework.annotations.AdamBean;
import org.adam.framework.aspect.transaction.annotations.Transaction;

import lombok.extern.slf4j.Slf4j;

@AdamBean
@Slf4j
public class TestBean implements TestInterface {

    @Transaction
    @Override public String sayHello() {
        log.info("Saying hello");
        return "hello";
    }
}
