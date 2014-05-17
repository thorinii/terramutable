/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.messages;

import me.lachlanap.terramutable.game.bus.Message;

/**
 *
 * @author lachlan
 */
public class DebugCycleStatsMessage extends Message {

    public enum Direction {

        Left, Right
    }

    public final Direction direction;

    public DebugCycleStatsMessage(Direction direction) {
        this.direction = direction;
    }

}
