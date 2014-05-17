/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.bus;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author lachlan
 */
public class MessageBus {

    private final List<FilteredMessageHandler> handlers;
    private int count;

    public MessageBus() {
        this.handlers = new CopyOnWriteArrayList<>();
    }

    /**
     * Sends out a message synchronously.
     */
    public void send(Message msg) {
        count++;

        for (FilteredMessageHandler handler : handlers)
            handler.receive(msg);
    }

    public void watchFor(MessageBusListener messageBusListener, Class<? extends Message>... types) {
        handlers.add(new FilteredMessageHandler(messageBusListener, types));
    }

    public int getCountSinceLastFrame() {
        return count;
    }

    public void resetCountSinceLastFrame() {
        count = 0;
    }

    private static class FilteredMessageHandler {

        final MessageBusListener listener;
        final Class<? extends Message>[] types;

        public FilteredMessageHandler(MessageBusListener listener, Class<? extends Message>[] types) {
            this.listener = listener;
            this.types = types;
        }

        public void receive(Message msg) {
            for (Class<? extends Message> t : types) {
                if (t.isInstance(msg)) {
                    listener.receive(msg);
                    return;
                }
            }
        }
    }
}
