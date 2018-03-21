package com.astart;

import java.awt.*;

/**
 * Created by Administrator on 2018/3/20.
 */
public class NodeMap {
    public Node mStartNode;
    public Node mTargetNode;
    public static class Type {
        public static final int EMPTY = 0;
        public static final int START = 1;
        public static final int BLOCK = 2;
        public static final int TARGET = 3;
    }


    public  int[][] mMap;

    public int[][] getMap() {
        return mMap;
    }

    public NodeMap(int[][] mMap) {
        this.mMap = mMap;
    }

    public void setBlock(int x, int y) {
        mMap[x][y] = Type.BLOCK;
    }

    public void setStart(int x, int y) {
        clearType(Type.START);
        mMap[x][y] = Type.START;
        mStartNode=new Node(x,y);
    }

    public void setTarget(int x, int y) {
        clearType(Type.TARGET);
        mMap[x][y] = Type.TARGET;
        mTargetNode=new Node(x,y);
    }

    public void clearMap() {
        for (int i = 0; i < mMap.length; i++) {
            for (int j = 0; j < mMap[0].length; j++) {
                mMap[i][j] = Type.EMPTY;
            }
        }
    }

    private void clearType(int type) {
        for (int i = 0; i < mMap.length; i++) {
            for (int j = 0; j < mMap[0].length; j++) {
                if (mMap[i][j] == type) {
                    mMap[i][j] = Type.EMPTY;
                    return;
                }
            }
        }
    }
}
