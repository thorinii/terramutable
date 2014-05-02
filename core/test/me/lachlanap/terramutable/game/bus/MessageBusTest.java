/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.bus;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 *
 * @author lachlan
 */
@RunWith(JUnit4.class)
public class MessageBusTest {

    final Mockery context = new JUnit4Mockery();
    final MessageBus bus = new MessageBus();
    final MessageBusListener listener = context.mock(MessageBusListener.class);

    @Test
    public void canListenForAMessage() {
        final Message msg = new TestMessage1();

        context.checking(new Expectations() {
            {
                oneOf(listener).receive(msg);
            }
        });

        bus.watchFor(listener, TestMessage1.class);

        bus.send(msg);
    }

    @Test
    public void doesNotSendMessagesToThoseThatDoNotWantThem() {
        final Message msg = new TestMessage2();

        context.checking(new Expectations() {
            {
                never(listener).receive(with(any(Message.class)));
            }
        });

        bus.watchFor(listener, TestMessage1.class);

        bus.send(msg);
    }

    @Test
    public void hasNoMessagesWhenNoneAreSent() {
        assertThat(bus.getCountSinceLastFrame(), is(0));
    }

    @Test
    public void keepsRecordOfMessageSent() {
        bus.send(new TestMessage1());
        assertThat(bus.getCountSinceLastFrame(), is(1));
    }

    @Test
    public void canClearCount() {
        bus.send(new TestMessage1());

        assertThat(bus.getCountSinceLastFrame(), is(1));

        bus.resetCountSinceLastFrame();

        assertThat(bus.getCountSinceLastFrame(), is(0));
    }

    class TestMessage1 extends Message {
    }

    class TestMessage2 extends Message {
    }
}
