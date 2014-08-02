/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.physics;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author lachlan
 */
public class QuadTreeTest {

    @Test
    public void getsOnePointInRange() {
        QuadTree sut = new QuadTree();
        sut.reset(0, 0, 10, 10);
        sut.insert(1, 2, 2);

        int[] tmp = new int[1];
        int count = sut.query(1, 1, 10, tmp);

        assertThat(count, is(1));
        assertThat(tmp[0], is(1));
    }

    @Test
    public void getsNoPointsOutOfRange() {
        QuadTree sut = new QuadTree();
        sut.reset(0, 0, 10, 10);
        sut.insert(1, 2, 2);

        int[] tmp = new int[1];
        int count = sut.query(6, 6, 2, tmp);

        assertThat(count, is(0));
    }

    @Test
    public void getsPointsInRange() {
        QuadTree sut = new QuadTree();
        sut.reset(0, 0, 10, 10);
        sut.insert(1, 2, 2);
        sut.insert(2, 2, 3);
        sut.insert(3, 2, 4);
        sut.insert(4, 2, 5);

        assertThat(sut.getSize(), is(4));

        int[] tmp = new int[5];
        int count = sut.query(2, 3, 1.5f, tmp);

        assertThat(count, is(3));
        assertThat(tmp[0], is(1));
        assertThat(tmp[1], is(2));
        assertThat(tmp[2], is(3));
    }
}
