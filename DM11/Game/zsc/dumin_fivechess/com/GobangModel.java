package zsc.dumin_fivechess.com;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Properties;

/**
 * 承载棋盘棋子的数据模型JavaBean
 * 
 * @author 杜敏
 */
public class GobangModel extends Object implements Serializable {
	private PropertyChangeSupport propertySupport; // 定义属性工具类
	private static GobangModel model; // 定义自身的变量
	private byte[][] chessmanArray = new byte[15][15]; // 定义棋子数组
	public static final String PROP_CHESSMANARRAY = "chessmanArray"; // 定义属性名称

	/**
	 * 获取本类实例的方法
	 * 
	 * @return
	 */
	public static GobangModel getInstance() {
		if (model == null) {
			model = new GobangModel();
		}
		return model;
	}

	/**
	 * 棋盘模型的构造方法
	 */
	public GobangModel() {
		propertySupport = new PropertyChangeSupport(this);// 初始化属性工具栏
		model = this;
	}

	/**
	 * 获取棋盘的棋子数组的方法
	 * 
	 * @return - 代表棋子的数组
	 */
	public byte[][] getChessmanArray() {
		return chessmanArray; // 返回棋子数组
	}

	/**
	 * 设置棋子数组的方法
	 * 
	 * @param chessmanArray
	 *            - 一个代表棋盘棋子的二维数组
	 */
	public void setChessmanArray(byte[][] chessmanArray) {
		this.chessmanArray = chessmanArray;
		propertySupport.firePropertyChange(PROP_CHESSMANARRAY, null,
				chessmanArray); // 报告所有已注册侦听器的绑定属性更新
	}

	/**
	 * 更新棋子数组的方法，不会产生跟新事件
	 * 
	 * @param chessmanArray
	 */
	public synchronized void updateChessmanArray(byte[][] chessmanArray) {
		this.chessmanArray = chessmanArray;
	}

	/**
	 * 添加事件监听器的方法
	 * 
	 * @param listener
	 *            - 事件监听器
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.addPropertyChangeListener(listener); // 添加事件监听器
	}

	/**
	 * 移除事件监听器的方法
	 * 
	 * @param listener
	 *            - 事件监听器
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		propertySupport.removePropertyChangeListener(listener); // 移除事件监听器
	}

	/**
	 * 获取棋盘上棋子数组的拷贝
	 * 
	 * @return - 棋子数组
	 */
	byte[][] getChessmanArrayCopy() {
		byte[][] newArray = new byte[15][15]; // 创建一个二维数组
		for (int i = 0; i < newArray.length; i++) {
			// 复制数组
			newArray[i] = Arrays.copyOf(chessmanArray[i], newArray[i].length);
		}
		return newArray;
	}
}
