package org.adam.framework.aspect.transaction;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.adam.framework.aspect.transaction.annotations.Transaction;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TransactionAspect {

    private static final String TRANSACTION_ANNOTATION = Transaction.class.getCanonicalName();

    public static void pointcutFor(Method objectMethod) {
        Arrays.stream(objectMethod.getAnnotations()).filter(
                annotation -> annotation.toString().contains(TRANSACTION_ANNOTATION)
        ).forEach(
                annotation ->
                        log.info("Found TRANSACTION annotation: Doing custom logic here.")
        );
    }
}
