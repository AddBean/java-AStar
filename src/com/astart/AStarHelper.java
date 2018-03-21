package com.astart;

import java.util.*;

/**
 * Created by Administrator on 2018/3/20.
  1. 把起点加入openlist
 2. 遍历openlist，找到F值最小的节点，把它作为当前处理的节点，并把该节点加入closelist中
 3. 对该节点的8个相邻格子进行判断，如果格子是不可抵达的或者在closelist中，则忽略它，否则如下操作：
 a. 如果相邻格子不在openlist中，把它加入，并将parent设置为该节点和计算f,g,h值
 b.如果相邻格子已在openlist中，并且新的G值比旧的G值小（此处也可为了减少计算量），则把相邻格子的parent设置为该节点，并且重新计算f值。
 4. 重复2,3步，直到终点加入了openlist中，表示找到路径；或者openlist空了，表示没有路径。
 */

public class AStarHelper {
    private  Queue<Node> mOpenList = new PriorityQueue<>();
    private  Stack<Node> mCloseList = new Stack<>();
    public static final int NORMAL_VALUE = 10;
    public static final int CONER_VALUE = 14;
    public Node mCurNode;
    public Node mStartNode;
    public Node mTargetNode;
    private NodeMap mNodeMap;

    public ArrayList<Node> start(NodeMap map) {
        mCloseList.clear();
        mOpenList.clear();
        mNodeMap = map;
        mStartNode = map.mStartNode;
        mTargetNode = map.mTargetNode;
        mOpenList.add(mStartNode);

        while (!mOpenList.isEmpty()) {
            Node node = getNodeFromOpenList(mTargetNode.x, mTargetNode.y);
            if (node != null) {//已经到终点；
                return getPathNodes(node);
            }
            mCurNode = mOpenList.poll();
            mCloseList.push(mCurNode);
            addOpenListByPos(NORMAL_VALUE, mCurNode.x + 1, mCurNode.y);
            addOpenListByPos(NORMAL_VALUE, mCurNode.x, mCurNode.y + 1);
            addOpenListByPos(NORMAL_VALUE, mCurNode.x - 1, mCurNode.y);
            addOpenListByPos(NORMAL_VALUE, mCurNode.x, mCurNode.y - 1);

            addOpenListByPos(CONER_VALUE, mCurNode.x + 1, mCurNode.y + 1);
            addOpenListByPos(CONER_VALUE, mCurNode.x - 1, mCurNode.y - 1);
            addOpenListByPos(CONER_VALUE, mCurNode.x - 1, mCurNode.y + 1);
            addOpenListByPos(CONER_VALUE, mCurNode.x + 1, mCurNode.y - 1);

        }

        return null;
    }

    private ArrayList<Node> getPathNodes(Node node) {
        ArrayList<Node> path = new ArrayList<>();
        path.add(node);
        while (node.mParent!=null){
            path.add(node.mParent);
            node=node.mParent;
        }
        return path;
    }

    private Node getNodeFromOpenList(int x, int y) {
        for (Node node : mOpenList) {
            if (x == node.x && y == node.y) {
                return node;
            }
        }
        return null;
    }

    private void addOpenListByPos(int value, int x, int y) {
        if (x < 0 || y < 0||x>=mNodeMap.getMap().length||y>=mNodeMap.getMap()[0].length) return;
        if (!canAddToOpenList(x, y)) return;
        Node node = getNodeFromOpenList(x, y);
        if (node == null) {
            node = new Node(x, y);
            setNodeValue(value, node);
            node.mParent = mCurNode;//父节点
            mOpenList.offer(node);
        } else{
            setNodeValue(value, node);
            node.mParent = mCurNode;//父节点
        }
    }

    private boolean canAddToOpenList(int x, int y) {
        if (closeListContains(x, y) != null) return false;
        if (mNodeMap.getMap()[x][y] == NodeMap.Type.BLOCK) return false;//不能过墙
        if ( mNodeMap.getMap()[x][mCurNode.y]== NodeMap.Type.BLOCK) return false;//不能过拐角
        if ( mNodeMap.getMap()[mCurNode.x][y]== NodeMap.Type.BLOCK) return false;
        return true;
    }

    private Node closeListContains(int x, int y) {
        for (Node node : mCloseList) {
            if (x == node.x && y == node.y) {
                return node;
            }
        }
        return null;
    }


    public int calculH(Node node) {
        return (Math.abs(node.x - mTargetNode.x) + Math.abs(node.y - mTargetNode.y)) * NORMAL_VALUE;
    }

    public void setNodeValue(int value, Node node) {
        if (mCurNode == null || mTargetNode == null) return;
        node.g = mCurNode.g + value;
        node.h = calculH(node);
        node.v = node.g + node.h;
    }
}
