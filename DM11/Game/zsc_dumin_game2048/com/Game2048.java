package zsc_dumin_game2048.com;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.*;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.*;
import javax.swing.JTextField;

public class Game2048 extends JFrame {
	private JPanel scoresPane;
	private JPanel mainPane;
	private JLabel labelMaxScores;
	private JLabel labelScores;
	private JLabel tips; // 提示操作标签
	private JTextField textMaxScores;
	private JLabel textScores;
	private JLabel[][] texts;
	private Icon icon2;
	private int times = 16; // 记录剩余空方块数目
	private int scores = 0; // 记录分数
	private int l1, l2, l3, l4, l5; // 用于判断游戏是否失败
	Font font = new Font("", Font.BOLD, 14); // 设置字体类型和大小
	Font font2 = new Font("", Font.BOLD, 30);
	Random random = new Random();

	public static void runGame() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Game2048 frame = new Game2048();
					frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	/**
	 * 构造方法
	 */
	public Game2048() {
		super();
		setResizable(false); // 禁止调整窗体大小
		getContentPane().setLayout(null); // 设置空布局
		setBounds(500, 50, 500, 615);
		setTitle("DM11_2048小游戏"); // 设置窗体标题

		scoresPane = new JPanel(); // 创建分数显示面板
		scoresPane.setBackground(Color.green); // 设置分数显示面板的背景色
		scoresPane.setBounds(20, 20, 460, 40);
		scoresPane.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.YELLOW)); // 设置得分面板的边框
		getContentPane().add(scoresPane); // 将得分面板添加到窗体
		scoresPane.setLayout(null); // 设置面板空布局

		labelMaxScores = new JLabel("最高分:"); // 最高分标签
		labelMaxScores.setFont(font); // 设置字体类型和大小
		labelMaxScores.setBounds(10, 5, 50, 30); // 设置最懂啊分标签的位置尺寸
		scoresPane.add(labelMaxScores); // 将最高分标签添加到得分容器中

		textMaxScores = new JTextField("暂不可用"); // 得分标签
		textMaxScores.setBounds(60, 5, 150, 30);
		textMaxScores.setFont(font);
		textMaxScores.setEditable(false);
		scoresPane.add(textMaxScores); // 将得分标签添加到分数面板中

		labelScores = new JLabel("得    分:");
		labelScores.setFont(font); // 设置字体类型和大小
		labelScores.setBounds(240, 5, 50, 30);
		scoresPane.add(labelScores);

		textScores = new JLabel(String.valueOf(scores));
		textScores.setFont(font);
		textScores.setBounds(290, 5, 150, 30);
		scoresPane.add(textScores);

		mainPane = new JPanel(); // 创建游戏主面板
		mainPane.setBounds(20, 70, 460, 500); // 设置主面板位置尺寸

		this.getContentPane().add(mainPane);
		mainPane.setLayout(null); // 设置空布局

		texts = new JLabel[4][4]; // 创建文本框二维数组
		for (int i = 0; i < 4; i++) { // 遍历数组
			for (int j = 0; j < 4; j++) {
				texts[i][j] = new JLabel(); // 创建标签
				texts[i][j].setFont(font2);
				texts[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				texts[i][j].setText("");
				texts[i][j].setBounds(120 * j, 120 * i, 100, 100); // 设置方块的大小位置
				setColor(i, j, "");
				texts[i][j].setOpaque(true);
				texts[i][j].setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.green));// 设置方块边框颜色
				mainPane.add(texts[i][j]); // 将创建的文本框放在

			}
		}

		tips = new JLabel("Tips：使用上、下、左、右键或者W、S、A、D键控制");
		tips.setFont(font);
		tips.setBounds(60, 480, 400, 20);
		mainPane.add(tips);

		textMaxScores.addKeyListener(new KeyAdapter() { // 为最高分标签添加按键监听器
			public void keyPressed(KeyEvent e) {
				do_label_keyPressed(e); // 调用时间处理方法
			}
		});

		Create2();
		Create2();
	}

	/**
	 * 按键输入事件的处理方法
	 * 
	 * @param e
	 */
	protected void do_label_keyPressed(final KeyEvent e) {
		int code = e.getKeyCode(); // 获取按键代码
		int a; // a 的引入是为了防止连加的情况出现
		String str;
		String str1;
		int num;
		switch (code) {
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A: // 如果按键代码是左方向键或者A键
			for (int i = 0; i < 4; i++) {
				a = 5;
				for (int k = 0; k < 3; k++) {
					for (int j = 1; j < 4; j++) { // 遍历16个方块
						str = texts[i][j].getText(); // 获取当前方块标签文本字符
						str1 = texts[i][j - 1].getText(); // 获取当前左1方块标签文本字符

						if (str1.compareTo("") == 0) { // 如果左1方块文本为空字符
							texts[i][j - 1].setText(str); // 字符左移
							setColor(i, j - 1, str);
							texts[i][j].setText(""); // 当前方块字符置空
							setColor(i, j, "");
						} else if ((str.compareTo(str1) == 0) && (j != a) && (j != a - 1)) { // 如果当前方块和左1方块文本字符相等
							num = Integer.parseInt(str);
							scores += num;
							times++;
							str = String.valueOf(2 * num);
							texts[i][j - 1].setText(str); // 左1方块文本字符变为两方块之和
							setColor(i, j - 1, str);
							texts[i][j].setText(""); // 当前方块字符置空
							setColor(i, j, "");
							a = j;
						}
					}
				}
			}
			l1 = 1; // 用于判断游戏是否失败
			Create2();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			for (int i = 0; i < 4; i++) {
				a = 5;
				for (int k = 0; k < 3; k++) {
					for (int j = 2; j >= 0; j--) {
						str = texts[i][j].getText();
						str1 = texts[i][j + 1].getText();

						if (str1.compareTo("") == 0) {
							texts[i][j + 1].setText(str);
							setColor(i, j + 1, str);
							texts[i][j].setText("");
							setColor(i, j, "");
						} else if (str.compareTo(str1) == 0 && j != a && j != a + 1) {
							num = Integer.parseInt(str);
							scores += num;
							times++;
							str = String.valueOf(2 * num);
							texts[i][j + 1].setText(str);
							setColor(i, j + 1, str);
							texts[i][j].setText("");
							setColor(i, j, "");
							a = j;
						}
					}
				}
			}
			l2 = 1;
			Create2();
			break;
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			for (int j = 0; j < 4; j++) {
				a = 5;
				for (int k = 0; k < 3; k++) {
					for (int i = 1; i < 4; i++) {
						str = texts[i][j].getText();
						str1 = texts[i - 1][j].getText();

						if (str1.compareTo("") == 0) {
							texts[i - 1][j].setText(str);
							setColor(i - 1, j, str);
							texts[i][j].setText("");
							setColor(i, j, "");
						} else if (str.compareTo(str1) == 0 && i != a && i != a - 1) {
							num = Integer.parseInt(str);
							scores += num;
							times++;
							str = String.valueOf(2 * num);
							texts[i - 1][j].setText(str);
							setColor(i - 1, j, str);
							texts[i][j].setText("");
							setColor(i, j, "");
							a = i;
						}
					}
				}
			}
			l3 = 1;
			Create2();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			for (int j = 0; j < 4; j++) {
				a = 5;
				for (int k = 0; k < 5; k++) {
					for (int i = 2; i >= 0; i--) {
						str = texts[i][j].getText();
						str1 = texts[i + 1][j].getText();

						if (str1.compareTo("") == 0) {
							texts[i + 1][j].setText(str);
							setColor(i + 1, j, str);
							texts[i][j].setText("");
							setColor(i, j, "");
						} else if (str.compareTo(str1) == 0 && i != a && i != a + 1) {
							num = Integer.parseInt(str);
							scores += num;
							times++;
							str = String.valueOf(2 * num);
							texts[i + 1][j].setText(str);
							setColor(i + 1, j, str);
							texts[i][j].setText("");
							setColor(i, j, "");
							a = i;
						}
					}
				}
			}
			l4 = 1;
			Create2();
			break;
		default:
			break;
		}
		textScores.setText(String.valueOf(scores));
	}

	/**
	 * 在随机位置产生一个2号方块的方法
	 * 
	 * @param i,j
	 */
	public void Create2() {
		int i, j;
		boolean r = false;
		String str;

		if (times > 0) {
			while (!r) {
				i = random.nextInt(4);
				j = random.nextInt(4);
				str = texts[i][j].getText();
				if ((str.compareTo("") == 0)) {
					texts[i][j].setIcon(icon2);
					texts[i][j].setText("2");
					setColor(i, j, "2");

					times--;
					r = true;
					l1 = l2 = l3 = l4 = 0;
				}
			}
		} else if (l1 > 0 && l2 > 0 && l3 > 0 && l4 > 0) { // l1到l4同时被键盘赋值为1说明任何方向键都不能产生新的数字2，说明游戏失败
			tips.setText("                            GAME　OVER ！");

		}
	}

	/**
	 * 设置标签颜色
	 * 
	 * @param i
	 *            , j ,str
	 */
	public void setColor(int i, int j, String str) {
		switch (str) {
		case "2":
			texts[i][j].setBackground(Color.yellow);
			break;
		case "4":
			texts[i][j].setBackground(Color.red);
			break;
		case "8":
			texts[i][j].setBackground(Color.pink);
			break;
		case "16":
			texts[i][j].setBackground(Color.orange);
			break;
		case "32":
			texts[i][j].setBackground(Color.magenta);
			break;
		case "64":
			texts[i][j].setBackground(Color.LIGHT_GRAY);
			break;
		case "128":
			texts[i][j].setBackground(Color.green);
			break;
		case "256":
			texts[i][j].setBackground(Color.gray);
			break;
		case "512":
			texts[i][j].setBackground(Color.DARK_GRAY);
			break;
		case "1024":
			texts[i][j].setBackground(Color.cyan);
			break;
		case "2048":
			texts[i][j].setBackground(Color.blue);
			break;
		case "":
		case "4096":
			texts[i][j].setBackground(Color.white);
			break;
		default:
			break;
		}

	}

}
