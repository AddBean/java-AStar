package com;

import com.astart.AStarHelper;
import com.astart.NodeMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {

    public static void main(String[] args) {
        AStarHelper mHelper=new AStarHelper();
        GameLayout frame = new GameLayout();
        frame.setup(800, 800, 20);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Container contentPane = frame.getContentPane();
        Button btn = new Button("Start");
        contentPane.add(btn);// 登陆按钮
        btn.setBounds(0, 0, 40, 30);
        btn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.loadPath(mHelper.start(frame.getmNodeMap()));
            }
        });

        frame.loadMap(new NodeMap(new int[20][20]));
    }

}
