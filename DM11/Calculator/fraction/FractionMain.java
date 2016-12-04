/**
 * 
 */
package fraction;

import javax.swing.JFrame;

/**
 * @author 杜敏
 *
 */
public class FractionMain {

	/**
	 * @param args
	 */
	public static void run() {
		// TODO Auto-generated method stub
        FractionFrame FractionFrame=new FractionFrame();
        FractionFrame.pack();
        FractionFrame.setVisible(true);
        FractionFrame.setResizable(false);// 设置窗口是否可改变大小
        FractionFrame.setLocation(300, 200);
        FractionFrame.setSize(600,350);
	}
}
