/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.bus;

/**
 *
 * @author lachlan
 */
public interface MessageBusListener {

    /**
     * Handles a message. Do not call long running operations!
     */
    public void receive(Message message);
}
