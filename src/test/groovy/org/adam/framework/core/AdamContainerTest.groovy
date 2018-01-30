package org.adam.framework.core

import beans.IncorrectInterface
import beans.TestBean
import beans.TestInterface
import spock.lang.Specification

class AdamContainerTest extends Specification {

    def "should return only one container instance"() {
        given:
            AdamContainer container1 = AdamContainer.getInstance()
            AdamContainer container2 = AdamContainer.getInstance()
        expect:
            container1 == container2
    }

    def "should find one adam bean"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        when:
            container.init("beans")
        then:
            container.getSingletonProxies().size() == 1
        and:
            container.getPrototypeProxiesDefs().size()
    }

    def "should create new object instance"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        and:
            container.init("beans")
        when:
            TestInterface testInterface1 = container.createPrototype(TestInterface) as TestInterface
            TestInterface testInterface2 = container.createPrototype(TestInterface) as TestInterface
        then:
            testInterface1 != testInterface2
        and:
            testInterface1.sayHello() == testInterface2.sayHello()
    }

    def "should get one singleton instance"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        and:
            container.init("beans")
        when:
            TestInterface testInterface1 = container.getSingleton(TestInterface) as TestInterface
            TestInterface testInterface2 = container.getSingleton(TestInterface) as TestInterface
        then:
            testInterface1 == testInterface2
    }

    def "should have only one common base package"() {
        given:
            AdamContainer container1 = AdamContainer.getInstance()
            AdamContainer container2 = AdamContainer.getInstance()
        when:
            container2.init("beans")
        then:
            container1.getBasePackage() == "beans"
    }

    def "should not scan any bean"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        when:
            container.init("notexisting")
        then:
            container.getSingletonProxies().size() == 0
    }

    def "should throw incorrect class type"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        and:
        container.init("beans")
        when:
            container.getSingleton(TestBean)
        then:
            thrown AdamRuntimeException
    }

    def "should throw null pointer when using null object"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        and:
            container.init("notexisting")
        and:
            TestInterface testInterface = container.getSingleton(TestInterface) as TestInterface
        when:
            testInterface.sayHello()
        then:
            thrown NullPointerException
    }

    def "should throw null pointer when using object not marked as AdamInterface"() {
        given:
            AdamContainer container = AdamContainer.getInstance()
        and:
            container.init("bean")
        and:
            IncorrectInterface incorrectInterface = container.getSingleton(IncorrectInterface) as IncorrectInterface
        when:
            incorrectInterface.sayHello()
        then:
            thrown NullPointerException
    }
}
