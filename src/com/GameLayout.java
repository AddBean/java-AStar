package com;

import com.astart.Node;
import com.astart.NodeMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

/**
 * Created by Administrator on 2018/3/20.
 */
public class GameLayout extends JFrame implements Runnable {
    private int mItemCount;
    private int mWidth = 500, mHeight = 500;
    private int mStartX = 0;//小方格宽度
    private int mStartY = 0;//小方格高度
    private int mItemSize;
    private int mRectSize;


    private Graphics mGraphic;


    private Color rectColor = new Color(0xf5f5f5);
    private Point mPoint;
    private NodeMap mNodeMap;
    private int mActionStep = 0;
    private ArrayList<Node> mPathNode;

    public GameLayout() {
    }

    /**
     * DrawSee构造方法
     */
    public void setup(int w, int h, int size) {
        mWidth = w;
        mHeight = h;
        mItemCount = size;
        mStartX = 20;
        mStartY = 40;
        mRectSize = mWidth - 2 * mStartX;
        mItemSize = mRectSize / mItemCount;
        Container pane = getContentPane();
        pane.setBackground(rectColor);
        setBounds(200, 200, (int) (1f * w), (int) (1.1 * h));
        setLayout(null);
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        mGraphic = getGraphics();
        new Thread(this).start();
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int[] p = getPointLoc(mPoint);
                if (p[0] < 0 || p[1] < 0 || p[0] >= mItemCount || p[1] >= mItemCount) return;
                //左击事件
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (mActionStep == 0) {//设置起点；
                        mNodeMap.setStart(p[0], p[1]);
                        mActionStep++;
                    } else if (mActionStep == 1) {
                        mNodeMap.setTarget(p[0], p[1]);
                        mActionStep++;
                    } else {

                        mNodeMap.setBlock(p[0], p[1]);
                    }
                }
                //右击事件
                if (e.getButton() == MouseEvent.BUTTON3) {
                    mActionStep = 0;
                    if(mPathNode!=null)
                    mPathNode.clear();
                    mNodeMap.clearMap();
                }
                paintComponents(mGraphic);
                //滑轮按下事件
                if (e.getButton() == MouseEvent.BUTTON2) return;
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public void loadMap(NodeMap map) {
        mNodeMap = map;
    }

    public NodeMap getmNodeMap() {
        return mNodeMap;
    }

    @Override
    public void paintComponents(Graphics g) {
        try {
            g.clearRect(0, 0, 2 * mWidth, 2 * mWidth);
            drawBackground(g);
            drawCursor(g);
            drawPath(g);
            drawMap(g);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawPath(Graphics g) {
        if (mPathNode == null || mPathNode.isEmpty()) return;
        int x =mStartX+ mPathNode.get(0).x* mItemSize+mItemSize/2;
        int y =mStartY+ mPathNode.get(0).y* mItemSize+mItemSize/2;
        for (int i = 0; i < mPathNode.size(); i++) {
            g.setColor(Color.GRAY);
            drawItem(g, mPathNode.get(i).x,
                    mPathNode.get(i).y);
            g.setColor(Color.RED);
            g.drawLine(x, y, mStartX + mPathNode.get(i).x* mItemSize+mItemSize/2, mStartY + mPathNode.get(i).y* mItemSize+mItemSize/2);
            x = mStartX + mPathNode.get(i).x* mItemSize+mItemSize/2;
            y = mStartY + mPathNode.get(i).y* mItemSize+mItemSize/2;
        }
    }

    private void drawMap(Graphics g) {
        if (mNodeMap == null) return;
        for (int i = 0; i < mNodeMap.getMap().length; i++) {
            for (int j = 0; j < mNodeMap.getMap()[0].length; j++) {
                switch (mNodeMap.getMap()[i][j]) {
                    case NodeMap.Type.EMPTY:
                        break;
                    case NodeMap.Type.BLOCK:
                        g.setColor(Color.BLACK);
                        drawItem(g, i, j);
                        break;
                    case NodeMap.Type.TARGET:
                        g.setColor(Color.BLUE);
                        drawItem(g, i, j);
                        break;
                    case NodeMap.Type.START:
                        g.setColor(Color.GREEN);
                        drawItem(g, i, j);
                        break;
                }
            }
        }
    }

    private void drawItem(Graphics g, int x, int y) {
        g.fillRect(mStartX + x * mItemSize, mStartY + y * mItemSize, mItemSize, mItemSize);
    }

    private void drawCursor(Graphics g) {
        int[] p = getPointLoc(mPoint);
        if (p[0] < 0 || p[1] < 0 || p[0] >= mItemCount || p[1] >= mItemCount) return;
        g.setColor(Color.RED);
        drawItem(g, p[0], p[1]);
    }

    private void drawBackground(Graphics g) {
        g.setColor(Color.RED);
        g.drawRect(mStartX, mStartY, mRectSize, mRectSize);
        for (int i = 0; i < mItemCount + 1; i++) {
//            // 绘制第i条竖直线
            g.drawLine(mStartX + (i * mItemSize), mStartY, mStartX + (i * mItemSize), mStartY + mRectSize);
            // 绘制第i条水平线
            g.drawLine(mStartX, mStartY + (i * mItemSize), mStartX + mRectSize, mStartY + (i * mItemSize));
        }
    }

    private int[] getPointLoc(Point point) {

        int x = (int) (((point.x - this.getX()) / (float) mWidth) * (float) mItemCount);
        int y = (int) (((point.y - this.getY() - 30) / (float) mHeight) * (float) mItemCount);
        return new int[]{x, y};
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(150);
                mPoint = java.awt.MouseInfo.getPointerInfo().getLocation();
                paintComponents(mGraphic);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public void loadPath(ArrayList<Node> path) {
        mPathNode = path;
    }
}
