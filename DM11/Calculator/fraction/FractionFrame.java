/**
 * 
 */
package fraction;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

/**
 * @author 杜敏
 *
 */
public class FractionFrame extends JFrame {
	private JPanel jPanel1;// 复杂算术表达式面板
	private JPanel jPanel2;// 分数计算面板
	private JTextField txtResult; // 显示计算结果框
	private JLabel lblFenZi1, lblFenZi2;// 分子
	private JLabel lblOpration;// 操作符
	private JComboBox<String> listOpration;// 操作符
	private JLabel lblFenMu1, lblFenMu2;// 分母
	private JTextField txtFenZi1, txtFenZi2;// 分子
	private JTextField txtFenMu1, txtFenMu2;// 分母
	private JButton btnExpressionCal;// 复杂算术表达式计算按钮
	private JButton btnFractionCal;// 分数计算按钮
	private ActionListener actionListener;// 事件监听器

	public FractionFrame() {
		// TODO Auto-generated constructor stub
		super("DM11科学计算器之复杂表达式计算");// 设置标题栏
		/**
		 * 初始化面板
		 */
		jPanel1 = new JPanel();
		jPanel2 = new JPanel();
		jPanel1.setLayout(new BorderLayout(0, 5));
		jPanel2.setLayout(new GridLayout(6, 3, 5, 10));
		this.setLayout(new BorderLayout(5, 10));

		/*
		 * 算数表达式面板初始化
		 */
		txtResult = new JTextField();
		txtResult.setFont(new Font("新宋体", Font.BOLD, 20));
		txtResult.setEditable(true);// 文本区域不可编辑
		txtResult.setBackground(Color.white);// 文本区域的背景色
		txtResult.setHorizontalAlignment(JTextField.LEFT);// 文字右对齐
		txtResult.setText("0");
		txtResult.setBorder(BorderFactory.createLoweredBevelBorder());
		btnExpressionCal = new JButton("表达式计算");
		btnExpressionCal.addActionListener(actionListener());
		btnExpressionCal.setFont(new Font("楷体", Font.BOLD, 20));
		btnExpressionCal.setForeground(Color.red);

		/**
		 * 分式计算面板初始化
		 */
		lblFenZi1 = new JLabel("第一个数的分子：");
		lblFenZi1.setFont(new Font("楷体", Font.BOLD, 20));
		lblFenZi1.setForeground(Color.blue);

		lblFenMu1 = new JLabel("第一个数的分母：");
		lblFenMu1.setFont(new Font("楷体", Font.BOLD, 20));
		lblFenMu1.setForeground(Color.blue);

		lblFenZi2 = new JLabel("第二个数的分子：");
		lblFenZi2.setFont(new Font("楷体", Font.BOLD, 20));
		lblFenZi2.setForeground(Color.blue);

		lblFenMu2 = new JLabel("第二个数的分母：");
		lblFenMu2.setFont(new Font("楷体", Font.BOLD, 20));
		lblFenMu2.setForeground(Color.blue);

		txtFenZi1 = new JTextField();
		txtFenZi1.setFont(new Font("楷体", Font.BOLD, 20));
		txtFenZi1.setForeground(Color.blue);

		txtFenZi2 = new JTextField();
		txtFenZi2.setFont(new Font("楷体", Font.BOLD, 20));
		txtFenZi2.setForeground(Color.blue);

		txtFenMu1 = new JTextField();
		txtFenMu1.setFont(new Font("楷体", Font.BOLD, 20));
		txtFenMu1.setForeground(Color.blue);

		txtFenMu2 = new JTextField();
		txtFenMu2.setFont(new Font("楷体", Font.BOLD, 20));
		txtFenMu2.setForeground(Color.blue);

		btnFractionCal = new JButton("分式计算");
		btnFractionCal.addActionListener(actionListener());
		btnFractionCal.setFont(new Font("楷体", Font.BOLD, 20));
		btnFractionCal.setForeground(Color.red);

		lblOpration = new JLabel("运算符:");
		lblOpration.setFont(new Font("楷体", Font.BOLD, 20));
		lblOpration.setForeground(Color.blue);

		listOpration = new JComboBox<String>();
		listOpration.addItem("+");
		listOpration.addItem("-");
		listOpration.addItem("*");
		listOpration.addItem("/");
		listOpration.setFont(new Font("楷体", Font.BOLD, 20));
		listOpration.setForeground(Color.blue);

		jPanel1.add(txtResult, BorderLayout.CENTER);
		jPanel1.add(btnExpressionCal, BorderLayout.EAST);

		jPanel2.add(new JLabel());
		jPanel2.add(lblFenZi1);
		jPanel2.add(txtFenZi1);

		jPanel2.add(new JLabel());
		jPanel2.add(lblFenMu1);
		jPanel2.add(txtFenMu1);

		jPanel2.add(new JLabel());
		jPanel2.add(lblOpration);
		jPanel2.add(listOpration);

		jPanel2.add(new JLabel());
		jPanel2.add(lblFenZi2);
		jPanel2.add(txtFenZi2);

		jPanel2.add(new JLabel());
		jPanel2.add(lblFenMu2);
		jPanel2.add(txtFenMu2);

		jPanel2.add(new JLabel());
		jPanel2.add(btnFractionCal);
		jPanel2.add(new JLabel());

		this.add(jPanel1, BorderLayout.NORTH);
		this.add(jPanel2, BorderLayout.WEST);
	}

	public ActionListener actionListener() {
		if (actionListener == null) {
			actionListener = new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String cmd = e.getActionCommand();
					if (cmd.equals("分式计算")) {
						fractinoCal();
					} else {
						expressionCal();
					}
				}
			};
		}
		return actionListener;
	}

	public void expressionCal() {
		String content = txtResult.getText().trim();
		ScriptEngineManager manager = new ScriptEngineManager();
		ScriptEngine engine = manager.getEngineByName("js");
		Object result;
		try {
			result = engine.eval(content);
			txtResult.setText(result.toString());
		} catch (ScriptException e1) {
			// TODO Auto-generated catch block
			txtResult.setText("表达式出错，请重新输入！");
		}
	}

	public void fractinoCal() {
		try {
			int first_numerator, second_numrator;
			int first_denominator, second_denominator;
			String opration = listOpration.getSelectedItem().toString();
			first_numerator = Integer.parseInt(txtFenZi1.getText().trim());
			second_numrator = Integer.parseInt(txtFenZi2.getText().trim());
			first_denominator = Integer.parseInt(txtFenMu1.getText().trim());
			second_denominator = Integer.parseInt(txtFenMu2.getText().trim());
			if (first_denominator == 0 || second_denominator == 0) {
				txtResult.setText("分母不能为0！");
			} else {
				Fraction fraction = new Fraction();
				switch (opration) {
				case "+":
					fraction = MyMath.fracAdd(first_numerator, first_denominator, second_numrator, second_denominator);
					txtResult.setText(String.valueOf(fraction.getFezi()) + " / " + String.valueOf(fraction.getFenmu()));
					break;
				case "-":
					fraction = MyMath.fracSub(first_numerator, first_denominator, second_numrator, second_denominator);
					txtResult.setText(String.valueOf(fraction.getFezi()) + " / " + String.valueOf(fraction.getFenmu()));
					break;
				case "*":
					fraction = MyMath.fracMul(first_numerator, first_denominator, second_numrator, second_denominator);
					txtResult.setText(String.valueOf(fraction.getFezi()) + " / " + String.valueOf(fraction.getFenmu()));
					break;
				case "/":
					fraction = MyMath.fractDiv(first_numerator, first_denominator, second_numrator, second_denominator);
					txtResult.setText(String.valueOf(fraction.getFezi()) + " / " + String.valueOf(fraction.getFenmu()));
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
