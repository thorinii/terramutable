/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.lachlanap.terramutable.game.physics;

import com.badlogic.gdx.math.Rectangle;

/**
 *
 * @author lachlan
 */
public class QuadTree {

    private Node root;

    public void reset(Rectangle worldSize) {
        root = new Node(worldSize.x - 1, worldSize.y - 1,
                        worldSize.x + worldSize.width + 2, worldSize.y + worldSize.height + 2);
    }

    public void reset(float left, float top, float right, float bottom) {
        root = new Node(left - 1, top - 1, right + 1, bottom + 1);
    }

    public void insert(int index, float x, float y) {
        if (root == null)
            throw new IllegalStateException("Need to reset the QuadTree before inserting");
        if (x >= root.left && x < root.right
            && y >= root.top && y < root.bottom) {

            Point point = new Point(index, x, y);
            if (!root.insert(point))
                throw new RuntimeException("Refuse to insert " + x + " " + y + "; "
                                           + root.left + " < " + root.right + ", "
                                           + root.top + " < " + root.bottom);
        } else
            throw new RuntimeException("Point " + x + " " + y + " is out of range: "
                                       + root.left + " <= x < " + root.right + ", "
                                       + root.top + " <= y < " + root.bottom);

    }

    public int query(float x, float y, float distance, int[] result) {
        return root.query(x, y, distance, result, 0);
    }

    public int getSize() {
        return root.getSize();
    }

    private static class Point {

        final int index;
        final float x, y;

        public Point(int index, float x, float y) {
            this.index = index;
            this.x = x;
            this.y = y;
        }
    }

    private static class Node {

        final float left, top, right, bottom;
        Point value;
        Node leftTop, leftBottom, rightTop, rightBottom;

        public Node(float left, float top, float right, float bottom) {
            if (left >= right || top >= bottom)
                throw new IllegalArgumentException("Incorrectly defined QuadTree node:"
                                                   + left + " < " + right + ", "
                                                   + top + " < " + bottom);
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }

        private boolean insert(Point point) {
            if (point.x >= left && point.x <= right
                && point.y >= top && point.y <= bottom) {
                if (leftTop == null) {
                    if (value == null) {
                        value = point;
                        return true;
                    } else {
                        float w = right - left;
                        float h = bottom - top;
                        float midX = left + w / 2;
                        float midY = top + h / 2;
                        leftTop = new Node(left, top,
                                           midX, midY);
                        leftBottom = new Node(left, midY,
                                              midX, bottom);
                        rightTop = new Node(midX, top,
                                            right, midY);
                        rightBottom = new Node(midX, midY,
                                               right, bottom);

                        Point tmp = value;
                        value = null;

                        return insert(tmp) && insert(point);
                    }
                } else {
                    boolean inserted = leftTop.insert(point)
                                       || leftBottom.insert(point)
                                       || rightTop.insert(point)
                                       || rightBottom.insert(point);
                    if (inserted)
                        return true;
                    else
                        throw new RuntimeException("Refused to insert " + point);
                }
            } else
                return false;
        }

        private int query(float x, float y, float distance, int[] result, int resultIndex) {
            if (value != null && isInRange(x, y, distance)) {
                if (resultIndex >= result.length) {
                    //throw new ArrayIndexOutOfBoundsException("Too many results: at least " + resultIndex);
                } else {
                    result[resultIndex] = value.index;
                    resultIndex++;
                }
            }

            if (leftTop != null) {
                resultIndex = leftTop.query(x, y, distance, result, resultIndex);
                resultIndex = leftBottom.query(x, y, distance, result, resultIndex);
                resultIndex = rightTop.query(x, y, distance, result, resultIndex);
                resultIndex = rightBottom.query(x, y, distance, result, resultIndex);
            }

            return resultIndex;
        }

        private boolean isInRange(float x, float y, float distance) {
            return value.x > (x - distance) && value.x < (x + distance)
                   && value.y > (y - distance) && value.y < (y + distance);
        }

        private int getSize() {
            if (leftTop != null) {
                return leftTop.getSize()
                       + leftBottom.getSize()
                       + rightTop.getSize()
                       + rightBottom.getSize();
            } else if (value == null)
                return 0;
            else
                return 1;
        }
    }
}
