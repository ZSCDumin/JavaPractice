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

import fraction.FractionMain;
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
	private String oper = "=";// 运算操作符
	private double result = 0;// 保存计算结果
	private boolean IfResult = true, flag = false;
	private Num numActionListener; // 数字监听类对象
	private ActionListener actionListener;
	private Font menuFont = new Font("宋体", Font.PLAIN, 12);// 菜单按钮字体

	/**
	 * 构造函数
	 */
	public CalFrame() {
		// TODO Auto-generated constructor stub
		super("DM11科学计算器");// 设置标题栏
		this.setLayout(new BorderLayout(10, 5));// 水平、纵向间距分别为10,5
		panel1 = new JPanel(new GridLayout(1, 3, 10, 10));
		panel2 = new JPanel(new GridLayout(5, 6, 5, 5));// 5行6列 ,按钮水平、纵向间距均为5
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

		// 初始化监听器
		numActionListener = new Num();

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
		txtResult.setText(String.valueOf(result));
	}

	/**
	 * 
	 */
	private void init() {
		// TODO Auto-generated method stub
		addButton(panel1, "Backspace", new Clear(), Color.red);
		addButton(panel1, "CE", new Clear(), Color.red);
		addButton(panel1, "C", new Clear(), Color.red);

		addButton(panel2, "1/x", new Signs(), Color.magenta);
		addButton(panel2, "log", new Signs(), Color.magenta);
		addButton(panel2, "7", numActionListener, Color.blue);
		addButton(panel2, "8", numActionListener, Color.blue);
		addButton(panel2, "9", numActionListener, Color.blue);
		addButton(panel2, "÷", new Signs(), Color.red);

		addButton(panel2, "n!", new Signs(), Color.magenta);
		addButton(panel2, "sqrt", new Signs(), Color.magenta);
		addButton(panel2, "4", numActionListener, Color.blue);
		addButton(panel2, "5", numActionListener, Color.blue);
		addButton(panel2, "6", numActionListener, Color.blue);
		addButton(panel2, "×", new Signs(), Color.red);

		addButton(panel2, "sin", new Signs(), Color.magenta);
		addButton(panel2, "x^2", new Signs(), Color.magenta);
		addButton(panel2, "1", numActionListener, Color.blue);
		addButton(panel2, "2", numActionListener, Color.blue);
		addButton(panel2, "3", numActionListener, Color.blue);
		addButton(panel2, "-", new Signs(), Color.red);

		addButton(panel2, "cos", new Signs(), Color.magenta);
		addButton(panel2, "x^3", new Signs(), Color.magenta);
		addButton(panel2, "0", numActionListener, Color.blue);
		addButton(panel2, "-/+", new Clear(), Color.blue);
		addButton(panel2, ".", new Dot(), Color.blue);
		addButton(panel2, "+", new Signs(), Color.red);

		addButton(panel2, "tan", new Signs(), Color.magenta);
		addButton(panel2, "%", new Signs(), Color.magenta);
		addButton(panel2, "π", numActionListener, Color.orange);
		addButton(panel2, "e", numActionListener, Color.orange);
		addButton(panel2, "′″", new Signs(), Color.orange);
		addButton(panel2, "=", new Signs(), Color.red);

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
					String cmd = e.getActionCommand();
					if (cmd.equals("2048小游戏")) {
						Game2048 game2048 = new Game2048();
						game2048.runGame();
					} else if (cmd.equals("网络五子棋")) {
						GameFiveChess gameFiveChess = new GameFiveChess();
						gameFiveChess.runGame();
					} else if (cmd.equals("贪食蛇小游戏")) {
						GameSnake gameSnake = new GameSnake();
					} else if (cmd.equals("分数计算")) {
                        FractionMain fractionMain=new FractionMain();
                        fractionMain.run();
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
					txtResult.setText("负数不能开平方根！");
				}
			} else if (cmd.equals("log")) {
				double num = Double.parseDouble(txtResult.getText());
				if (num > 0) {
					txtResult.setText(String.valueOf(MathCal.log(num)));
				} else {
					txtResult.setText("负数不能求对数！");
				}
			}

			/* %求百分比 */
			else if (cmd.equals("%")) {
				double num = Double.parseDouble(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.percent(num)));
			}

			/* 1/x求倒数 */
			else if (cmd.equals("1/x")) {
				double num = Double.parseDouble(txtResult.getText());
				if (num == 0) {
					txtResult.setText("0没有倒数！");
				} else {
					txtResult.setText(String.valueOf(MathCal.daoShu(num)));
				}
			}

			/* 求正弦函数 */
			else if (cmd.equals("sin")) {
				double num = Double.parseDouble(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.sin(num)));
			}

			/* 求余弦函数 */
			else if (cmd.equals("cos")) {
				double num = Double.parseDouble(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.cos(num)));
			}

			/* 求正切函数 */
			else if (cmd.equals("tan")) {
				double num = Double.parseDouble(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.tan(num)));
			}

			/* 求阶乘 */
			else if (cmd.equals("n!")) {
				int num = Integer.parseInt(txtResult.getText());
				if (num < 0) {
					txtResult.setText("负数无法进行阶乘");
				} else if (num == 0) {
					txtResult.setText("0");
				} else {
					txtResult.setText(String.valueOf(MathCal.jieCheng(num)));
				}
			}

			/* 求平方 */
			else if (cmd.equals("x^2")) {
				int num = Integer.parseInt(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.pow(num, 2)));
			}

			/* 求立方 */
			else if (cmd.equals("x^3")) {
				int num = Integer.parseInt(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.pow(num, 3)));
			}

			/* ′″角度转换 */
			else if (cmd.equals("′″")) {
				double num = Double.parseDouble(txtResult.getText());
				txtResult.setText(String.valueOf(MathCal.angle(num)));
			}

			else {
				if (flag) {
					IfResult = false;
				}
				if (IfResult) {
					oper = cmd;
				} else {
					getResult(Double.parseDouble(txtResult.getText()));
					oper = cmd;
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
			String cmd = e.getActionCommand();
			if (cmd == "C") {
				txtResult.setText("0");
				IfResult = true;
				result = 0;
			} else if (cmd == "-/+") {
				double num = 0 - Double.parseDouble(txtResult.getText().trim());
				txtResult.setText(String.valueOf(num));
			} else if (cmd == "Backspace") {
				if (Double.parseDouble(txtResult.getText()) > 0) {
					if (txtResult.getText().length() > 1) {
						txtResult.setText(txtResult.getText().substring(0, txtResult.getText().length() - 1));
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
			} else if (cmd == "CE") {
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
			String cmd = e.getActionCommand();
			if (IfResult) {
				txtResult.setText("");
				IfResult = false;
			}
			if (cmd == "π") {
				txtResult.setText(String.valueOf(Math.PI));
			} else if (cmd == "e") {
				txtResult.setText(String.valueOf(Math.E));
			} else {
				txtResult.setText(txtResult.getText().trim() + cmd);
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
