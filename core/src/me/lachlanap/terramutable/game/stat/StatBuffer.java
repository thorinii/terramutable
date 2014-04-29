/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.stat;

/**
 *
 * @author lachlan
 */
public class StatBuffer {

    private final String name;
    private final float[] buffer;
    private int head;

    public StatBuffer(String name, int size) {
        this.name = name;
        buffer = new float[size];
    }

    public String getName() {
        return name;
    }

    public void push(float val) {
        buffer[head] = val;

        head++;
        if (head >= size())
            head = 0;
    }

    public int size() {
        return buffer.length;
    }

    public float getMax() {
        float max = buffer[0];
        for (int i = 1; i < size(); i++)
            if (max < buffer[i])
                max = buffer[i];
        return max;
    }

    public float getMin() {
        float min = buffer[0];
        for (int i = 1; i < size(); i++)
            if (min > buffer[i])
                min = buffer[i];
        return min;
    }

    public void copyTo(float[] destination) {
        int j = 0;
        for (int i = head; i < size(); i++, j++)
            destination[j] = buffer[i];

        for (int i = 0; i < head; i++, j++)
            destination[j] = buffer[i];
    }

    public float getLatest() {
        if (head == 0)
            return buffer[buffer.length - 1];
        else
            return buffer[head - 1];
    }
}
