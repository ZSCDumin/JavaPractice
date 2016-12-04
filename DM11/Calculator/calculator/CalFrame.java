/**
 * 
 */
package calculator;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import zsc.dumin_fivechess.com.GameFiveChess;
import zsc.dumin_gamesnack.com.GameSnake;
import zsc_dumin_game2048.com.Game2048;

/**
 * @author 杜敏
 *
 */
public class CalFrame extends JFrame {

	private JTextField txtResult; // 显示计算结果框
	private JPanel panel1, panel2, panel3, panel4; // 四个面板
	private JMenuBar myMenuBar; // 水平菜单栏
	private JMenu menuGame, menuFraction; // 游戏菜单、分数计算菜单
	private JMenuItem game2048_item, gameFiveChess_item, gameSnack_item, fraction_item;// 2048、网络五子棋、贪食蛇、分数计算
	private DecimalFormat df; // 将数字格式化
	private String oper = "=";// 运算操作符
	private double result = 0;// 保存计算结果
	private boolean IfResult = true, flag = false;
	private Num numActionListener; // 数字监听类对象
	private ActionListener actionListener;// 定义监听器
	// 菜单按钮字体
	private Font menuFont = new Font("宋体", Font.PLAIN, 12);
	private Font buttonFont = new Font("宋体", Font.PLAIN, 12);

	/**
	 * 构造函数
	 */
	public CalFrame() {
		// TODO Auto-generated constructor stub
		super("DM11科学计算器");// 设置标题栏（调用超类的构造函数）
		df = new DecimalFormat("#.####");// 保留四位小数
		this.setLayout(new BorderLayout(10, 5));// 按钮水平、纵向间距分别为10,5
		panel1 = new JPanel(new GridLayout(1, 3, 10, 10));
		panel2 = new JPanel(new GridLayout(5, 6, 5, 5));// 5行6列 ,按钮水平、纵向间距均为5
		panel3 = new JPanel(new GridLayout(4, 1, 5, 5));
		panel4 = new JPanel(new BorderLayout(5, 5));

		myMenuBar = new JMenuBar(); // 创建水平菜单栏

		/**
		 * 创建菜单按钮
		 */
		menuGame = new JMenu("小游戏");
		menuFraction = new JMenu("分数");
		menuGame.setFont(menuFont);
		menuFraction.setFont(menuFont);

		/**
		 * 创建游戏菜单子菜单
		 */
		game2048_item = new JMenuItem("2048小游戏");
		gameFiveChess_item = new JMenuItem("网络五子棋");
		gameSnack_item = new JMenuItem("贪食蛇小游戏");

		// 向游戏菜单中添加子菜单
		menuGame.add(game2048_item);
		menuGame.addSeparator();// 添加分隔符
		menuGame.add(gameFiveChess_item);
		menuGame.addSeparator();// 添加分隔符
		menuGame.add(gameSnack_item);

		/**
		 * 创建分数菜单子菜单
		 */
		fraction_item = new JMenuItem("分数计算");

		// 向分数菜单中添加子菜单
		menuFraction.add(fraction_item);

		// 向水平菜单栏添加菜单项
		myMenuBar.add(menuGame);
		myMenuBar.add(menuFraction);
		this.setJMenuBar(myMenuBar);

		/**
		 * 添加菜单按钮监听事件
		 */
		game2048_item.addActionListener(getActionListener());
		gameFiveChess_item.addActionListener(getActionListener());
		gameSnack_item.addActionListener(getActionListener());
		fraction_item.addActionListener(getActionListener());

		/**
		 * 文本域，即为计算器的屏幕显示区域
		 */
		txtResult = new JTextField();
		txtResult.setEditable(false);// 文本区域不可编辑
		txtResult.setBackground(Color.white);// 文本区域的背景色
		txtResult.setHorizontalAlignment(JTextField.RIGHT);// 文字右对齐
		txtResult.setText("0");
		txtResult.setBorder(BorderFactory.createLoweredBevelBorder());
		init();// 对计算器进行初始化
	}

	/**
	 * 
	 * @param panel
	 * @param name
	 * @param action
	 * @param color
	 */
	private void addButton(JPanel panel, String name, ActionListener action, Color color) {
		JButton bt = new JButton(name);
		panel.add(bt);// 在面板上增加按钮
		bt.setForeground(color);// 设置前景（字体）颜色
		bt.addActionListener(action);// 增加监听事件
	}

	/**
	 * 计算器的基础操作（+ - × ÷）
	 * 
	 * @param x
	 */
	private void getResult(double x) {
		if (oper == "+") {
			result += x;
		} else if (oper == "-") {
			result -= x;
		} else if (oper == "×") {
			result *= x;
		} else if (oper == "÷") {
			result /= x;
		} else if (oper == "=") {
			result = x;
		}
		txtResult.setText(df.format(result));
	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub
		addButton(panel1, "Backspace", new Clear(), Color.red);
		addButton(panel1, "CE", new Clear(), Color.red);
		addButton(panel1, "C", new Clear(), Color.red);
		addButton(panel2, "-/+", new Clear(), Color.blue);
		
		addButton(panel2, "7", numActionListener, Color.blue);
		addButton(panel2, "8", numActionListener, Color.blue);
		addButton(panel2, "9", numActionListener, Color.blue);
		addButton(panel2, "4", numActionListener, Color.blue);
		addButton(panel2, "5", numActionListener, Color.blue);
		addButton(panel2, "6", numActionListener, Color.blue);	
		addButton(panel2, "1", numActionListener, Color.blue);
		addButton(panel2, "2", numActionListener, Color.blue);
		addButton(panel2, "3", numActionListener, Color.blue);
		addButton(panel2, "0", numActionListener, Color.blue);
		addButton(panel2, "π", numActionListener, Color.orange);
		addButton(panel2, "e", numActionListener, Color.orange);
		
		addButton(panel2, ".", new Dot(), Color.blue);
		
		addButton(panel2, "÷", new Signs(), Color.red);
		addButton(panel2, "n!", new Signs(), Color.magenta);
		addButton(panel2, "sqrt", new Signs(), Color.magenta);
		addButton(panel2, "+", new Signs(), Color.red);
		addButton(panel2, "tan", new Signs(), Color.magenta);
		addButton(panel2, "%", new Signs(), Color.magenta);
		addButton(panel2, "′″", new Signs(), Color.orange);
		addButton(panel2, "=", new Signs(), Color.red);
		addButton(panel2, "-", new Signs(), Color.red);
		addButton(panel2, "cos", new Signs(), Color.magenta);
		addButton(panel2, "x^3", new Signs(), Color.magenta);
		addButton(panel2, "×", new Signs(), Color.red);
		addButton(panel2, "sin", new Signs(), Color.magenta);
		addButton(panel2, "x^2", new Signs(), Color.magenta);
		addButton(panel2, "1/x", new Signs(), Color.magenta);
		addButton(panel2, "log", new Signs(), Color.magenta);
		
		panel4.add(panel1, BorderLayout.NORTH);
		panel4.add(panel2, BorderLayout.CENTER);
		this.add(txtResult, BorderLayout.NORTH);
		this.add(panel4);
	}

	public ActionListener getActionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
                    String cmd=e.toString();
                    if(cmd.equals("2048小游戏"))
                    {
                    	Game2048 game2048=new Game2048();
                    	game2048.runGame();
                    }
                    else if(cmd.equals("网络五子棋")){
						GameFiveChess gameFiveChess=new GameFiveChess();
					}
                    else if(cmd.equals("贪食蛇小游戏"))
                    {
                    	GameSnake gameSnake=new GameSnake();
                    }
                    else if(cmd.equals("分数计算"))
                    {
                    	
                    }
				}
			};
		}
		return actionListener;
	}

	/**
	 * 运算符号的事件监听
	 */
	class Signs implements ActionListener {
		public void actionPerformed(ActionEvent e) {	
			String cmd = e.getActionCommand();
			if (cmd.equals("sqrt")) {
				double num = Double.parseDouble(txtResult.getText());
				if (num >= 0) {
					txtResult.setText(String.valueOf(MathCal.sqrt(num))); 
				} else {
					txtResult.setText("负数不能开平方根");
				}
			}
			else if(cmd.equals("log")) {
				double num = Double.parseDouble(txtResult.getText());
				if (num > 0) {
					txtResult.setText(String.valueOf(MathCal.log(num)));
				} else {
					txtResult.setText("负数不能求对数");
				}
			}

			/* %求百分比 */
			else if (cmd.equals("%")) {
				double num = Double.parseDouble(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.percent(num)));
			}

			/* 1/x求倒数 */
			else if (str.equals("1/x")) {
				if (Double.parseDouble(txtResult.getText()) == 0) {
					txtResult.setText("除数不能为零");
				} else {
					txtResult.setText(
							txtResult.getText() + "的倒数为： " + df.format(1 / Double.parseDouble(txtResult.getText())));
				}
			}

			/* sin求正弦函数 */
			else if (str.equals("sin")) {
				double i = Double.parseDouble(txtResult.getText());
				txtResult.setText("sin(" + txtResult.getText() + ") = " + String.valueOf(df.format(Math.sin(i))));
			}

			/* cos求余弦函数 */
			else if (str.equals("cos")) {
				double i = Double.parseDouble(txtResult.getText());
				txtResult.setText("cos(" + txtResult.getText() + ") = " + String.valueOf(df.format(Math.cos(i))));
			}

			/* tan求正切函数 */
			else if (str.equals("tan")) {
				double i = Double.parseDouble(txtResult.getText());
				txtResult.setText("tan(" + txtResult.getText() + ") = " + String.valueOf(df.format(Math.tan(i))));
			}

			/* n!求阶乘 */
			else if (str.equals("n!")) {
				double i = Double.parseDouble(txtResult.getText());
				if ((i % 2 == 0) || (i % 2 == 1))// 判断为整数放进行阶乘操作
				{
					int j = (int) i;// 强制类型转换
					int result = 1;
					for (int k = 1; k <= j; k++)
						result *= k;
					txtResult.setText(txtResult.getText() + "的阶乘为：" + String.valueOf(result));
				} else {
					txtResult.setText("无法进行阶乘");
				}
			}

			/* x^2求平方 */
			else if (str.equals("x^2")) {
				double i = Double.parseDouble(txtResult.getText());
				txtResult.setText(txtResult.getText() + "的平方为：" + String.valueOf(df.format(i * i)));
			}

			/* x^3求立方 */
			else if (str.equals("x^3")) {
				double i = Double.parseDouble(txtResult.getText());
				txtResult.setText(txtResult.getText() + "的立方为：" + String.valueOf(df.format(i * i * i)));
			}

			/* ′″角度转换 */
			/**
			 * 将角度值转换成弧度值，方便三角函数的计算
			 */
			else if (str.equals("′″")) {
				double i = Double.parseDouble(txtResult.getText());
				txtResult.setText(txtResult.getText() + "的弧度为：" + String.valueOf(i / 180 * Math.PI));
			}

			else {
				if (flag) {
					IfResult = false;
				}
				if (IfResult) {
					oper = str;
				} else {
					getResult(Double.parseDouble(txtResult.getText()));
					oper = str;
					IfResult = true;
				}
			}
		}
	}

	/**
	 * 清除按钮的事件监听
	 */
	class Clear implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/*
			 * 用ActionEvent对象的getActionCommand()方法 取得与引发事件对象相关的字符串
			 */
			String str = e.getActionCommand();
			if (str == "C") {
				txtResult.setText("0");
				IfResult = true;
				result = 0;
			} else if (str == "-/+") {
				double i = 0 - Double.parseDouble(txtResult.getText().trim());
				txtResult.setText(df.format(i));
			} else if (str == "Backspace") {
				if (Double.parseDouble(txtResult.getText()) > 0) {
					if (txtResult.getText().length() > 1) {
						txtResult.setText(txtResult.getText().substring(0, txtResult.getText().length() - 1));
						// 使用退格删除最后一位字符
					} else {
						txtResult.setText("0");
						IfResult = true;
					}
				} else {
					if (txtResult.getText().length() > 2) {
						txtResult.setText(txtResult.getText().substring(0, txtResult.getText().length() - 1));
					} else {
						txtResult.setText("0");
						IfResult = true;
					}
				}
			} else if (str == "CE") {
				txtResult.setText("0");
				IfResult = true;
			}
		}
	}

	/**
	 * 数字输入的事件监听
	 */
	class Num implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String str = e.getActionCommand();
			if (IfResult) {
				txtResult.setText("");
				IfResult = false;
			}
			if (str == "π") {
				txtResult.setText("π= " + String.valueOf(Math.PI));
			} else if (str == "e") {
				txtResult.setText("e= " + String.valueOf(Math.E));
			} else {
				txtResult.setText(txtResult.getText().trim() + str);
				if (txtResult.getText().equals("0")) {
					txtResult.setText("0");
					IfResult = true;
					flag = true;

				}
			}
		}
	}

	/**
	 * 小数点的事件监听
	 */
	class Dot implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			IfResult = false;
			if (txtResult.getText().trim().indexOf(".") == -1) {
				txtResult.setText(txtResult.getText() + ".");
			}
		}
	}
}
