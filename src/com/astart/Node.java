package com.astart;

import java.util.Comparator;

/**
 * Created by Administrator on 2018/3/20.
 */
public class Node implements Comparable {
    public Node mParent;
    public int x;
    public int y;

    public int g;
    public int h;
    public int v;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }



    @Override
    public int compareTo(Object o) {
        Node current = (Node) o;
        if (current.v < this.v) {
            return 1;
        } else if (current.v == this.v) {
            return 0;
        }
        return -1;
    }
}
