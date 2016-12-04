package calculator;//包名

import javax.swing.JFrame;

public class Calucator{

	public static void main(String[] args) {
		CalFrame calFrame=new CalFrame();
		calFrame.pack();
		calFrame.setVisible(true);
		calFrame.setResizable(false);// 设置窗口是否可改变大小
		calFrame.setLocation(300, 200);
		calFrame.setSize(600,300);
		calFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
