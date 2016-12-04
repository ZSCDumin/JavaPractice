package zsc.dumin_fivechess.com;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Arrays;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

/**
 * 下棋面板
 * @author 杜敏
 */
public class ChessPanel extends JPanel {
	static ImageIcon WHITE_CHESS_ICON;  //白棋图标
	static ImageIcon BLACK_CHESS_ICON;  //黑旗图标
	final static int OPRATION_REPENT = 0xEF; // 悔棋命令
	final static int OPRATION_NODE_REPENT = 0xCF; // 接受悔棋命令
	final static int OPRATION_DRAW = 0xFE; // 和棋命令
	final static int OPRATION_NODE_DRAW = 0xEE; // 接受和棋命令
	final static int OPRATION_START = 0xFd; // 开始命令
	final static int OPRATION_ALL_START = 0xEd; // 接受开始命令
	final static int OPRATION_GIVEUP = 0xFc; // 认输命令
	final static int WIN = 88; // 胜利代码
	private boolean towardsStart = false;
	private Image backImg;
	protected JButton backButton;
	private JToggleButton backplayToggleButton;
	private JLabel bannerLabel;
	private JButton giveupButton;
	private GobangPanel gobangPanel1;
	private JButton heqiButton;
	private JLabel jLabel5;
	private JLabel jLabel6;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JPanel jPanel3;
	private JPanel jPanel4;
	protected JLabel leftInfoLabel;
	protected JLabel myChessColorLabel;
	protected JLabel rightInfoLabel;
	private JButton startButton;
	protected JLabel towardsChessColorLabel;
	int backIndex = 1;

	/**
	 * 下棋面板的构造方法
	 */
	public ChessPanel() {
		WHITE_CHESS_ICON = new javax.swing.ImageIcon(getClass().getResource(
				"/res/whiteChess.png")); // 初始化白棋棋盒图片
		BLACK_CHESS_ICON = new javax.swing.ImageIcon(getClass().getResource(
				"/res/blackChess.png")); // 初始化黑棋棋盒图片
		URL url = getClass().getResource("/res/bg/1.jpg");
		backImg = new ImageIcon(url).getImage(); // 初始化背景图片
		initComponents(); // 调用初始化界面的方法
	}

	/**
	 * 重写paintComponent方法，绘制背景图片
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		// 绘制背景图片
		g.drawImage(backImg, 0, 0, getWidth(), getHeight(), null);
	}

	/**
	 * 设置棋子颜色的方法，以棋盒颜色为主
	 * 
	 * @param color
	 *            - 指定颜色的棋盒图片
	 */
	public void setChessColor(ImageIcon color) {
		myChessColorLabel.setIcon(color); // 设置本地用户的棋盒图标
		if (color.equals(WHITE_CHESS_ICON)) { // 设置白棋
			gobangPanel1.setMyColor(GobangPanel.WHITE_CHESSMAN);
			towardsChessColorLabel.setIcon(BLACK_CHESS_ICON);
		} else if (color.equals(BLACK_CHESS_ICON)) {// 设置黑棋
			gobangPanel1.setMyColor(GobangPanel.BLACK_CHESSMAN);
			towardsChessColorLabel.setIcon(WHITE_CHESS_ICON);
		}
		revalidate();
	}

	/**
	 * 设置轮回状态的方法
	 * 
	 * @param turn
	 *            - 是否获得走棋权利
	 */
	public void setTurn(boolean turn) {
		if (turn) { // 如果获得走棋权利
			myChessColorLabel.setVisible(true); // 显示棋盒
			towardsChessColorLabel.setVisible(false); // 隐藏对家棋盒
		} else {// 否则
			myChessColorLabel.setVisible(false); // 隐藏自己的棋盒
			towardsChessColorLabel.setVisible(true); // 显示对家的棋盒
		}
	}

	public boolean isTowardsStart() {
		return towardsStart;
	}

	public void setTowardsStart(boolean towardsStart) {
		this.towardsStart = towardsStart;
	}

	public GobangPanel getGobangPanel1() {
		return gobangPanel1;
	}

	/**
	 * 悔棋的业务处理方法
	 */
	public synchronized void repentOperation() {
		// 获取下棋队列
		Deque<byte[][]> chessQueue = gobangPanel1.getChessQueue();
		if (chessQueue.isEmpty()) {
			return;
		}
		// 获取上两次次走棋的棋谱
		for (int i = 0; i < 2 && !chessQueue.isEmpty(); i++) {
			byte[][] pop = chessQueue.pop(); // 废弃走棋步骤
		}
		if (chessQueue.size() < 1) {
			chessQueue.push(new byte[15][15]);
		}
		byte[][] pop = chessQueue.peek();
		GobangModel.getInstance().updateChessmanArray(pop);// 更新棋盘的棋子布局
		repaint();
	}

	public void send(Object opration) {
		GameFiveChess mainFrame = (GameFiveChess) getRootPane().getParent();
		mainFrame.send(opration); // 发送命令

	}

	/**
	 * 重新初始化游戏状态
	 */
	void reInit() {
		gobangPanel1.oldRec();
		startButton.setEnabled(true);
		giveupButton.setEnabled(false);
		heqiButton.setEnabled(false);
		backButton.setEnabled(false);
		gobangPanel1.setStart(false);
		setTowardsStart(false);
	}

	/**
	 * 为双方玩家分配棋子的方法
	 */
	private void fenqi() {
		GameFiveChess frame = (GameFiveChess) getRootPane().getParent(); // 获取主窗体对象
		// 获取对家开始游戏的时间
		long towardsTime = frame.getTowardsUser().getTime().getTime();
		// 获取自己开始游戏的时间
		long meTime = frame.getUser().getTime().getTime();
		// 根据两个玩家开始游戏时间的先后，分配棋子的颜色
		if (meTime >= towardsTime) {
			frame.getChessPanel1().setChessColor(ChessPanel.WHITE_CHESS_ICON);
			frame.getChessPanel1().getGobangPanel1().setTurn(true);
		} else {
			frame.getChessPanel1().setChessColor(ChessPanel.BLACK_CHESS_ICON);
			frame.getChessPanel1().getGobangPanel1().setTurn(false);
		}
	}

	/**
	 * 清屏 填充棋盘的方法。可以使用1或-1制定填充棋盘的棋子，使用0清除棋盘
	 * 
	 * @param chessman
	 *            - 填充棋盘的棋子的颜色代码
	 */
	private void fillChessBoard(final byte chessman) {
		try {
			Runnable runnable = new Runnable() { // 创建清屏的动画线程
				/**
				 * 线程的主体方法
				 * 
				 * @see java.lang.Runnable#run()
				 */
				public void run() {
					byte[][] chessmanArray = GobangModel.getInstance()
							.getChessmanArray(); // 获取棋盘数组
					for (int i = 0; i < chessmanArray.length; i += 2) {
						try {
							Thread.sleep(10); // 动画间隔时间
						} catch (InterruptedException ex) {
							Logger.getLogger(ChessPanel.class.getName()).log(
									Level.SEVERE, null, ex);
						}
						// 使用指定颜色的棋子填充数组的一列
						Arrays.fill(chessmanArray[i], chessman);
						Arrays.fill(chessmanArray[(i + 1) % 15], chessman);
						GobangModel.getInstance().updateChessmanArray(
								chessmanArray); // 更新棋盘上的棋子
						gobangPanel1.paintImmediately(0, 0, getWidth(),
								getHeight()); // 立即重绘指定区域的棋盘
					}
				}
			};
			// 在事件队列中执行清屏
			if (SwingUtilities.isEventDispatchThread()) {
				runnable.run();
			} else {
				SwingUtilities.invokeAndWait(runnable);
			}
		} catch (Exception ex) {
			Logger.getLogger(ChessPanel.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * 初始化程序界面的方法
	 */
	private void initComponents() {

		jPanel1 = new javax.swing.JPanel();
		jPanel1.setOpaque(false);
		backButton = new javax.swing.JButton();
		heqiButton = new javax.swing.JButton();
		startButton = new javax.swing.JButton();
		giveupButton = new javax.swing.JButton();
		backplayToggleButton = new javax.swing.JToggleButton();
		jPanel2 = new javax.swing.JPanel();
		jPanel2.setOpaque(false);
		jLabel5 = new javax.swing.JLabel();
		leftInfoLabel = new javax.swing.JLabel();
		leftInfoLabel.setForeground(new Color(0, 255, 0));
		leftInfoLabel.setFont(new Font("隶书", Font.PLAIN, 22));
		myChessColorLabel = new javax.swing.JLabel();
		jPanel3 = new javax.swing.JPanel();
		jPanel3.setOpaque(false);
		jLabel6 = new javax.swing.JLabel();
		rightInfoLabel = new javax.swing.JLabel();
		rightInfoLabel.setForeground(Color.GREEN);
		rightInfoLabel.setFont(new Font("隶书", Font.PLAIN, 22));
		towardsChessColorLabel = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		jPanel4.setOpaque(false);
		bannerLabel = new javax.swing.JLabel();
		gobangPanel1 = new zsc.dumin_fivechess.com.GobangPanel();

		setLayout(new java.awt.BorderLayout());
		setOpaque(false);

		backButton.setText("悔棋");
		backButton.setEnabled(false);
		backButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				backButtonActionPerformed(evt);
			}
		});
		jPanel1.add(backButton);

		heqiButton.setText("和棋");
		heqiButton.setEnabled(false);
		heqiButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				heqiButtonActionPerformed(evt);
			}
		});
		jPanel1.add(heqiButton);

		startButton.setText("开始");
		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});
		jPanel1.add(startButton);

		giveupButton.setText("认输");
		giveupButton.setEnabled(false);
		giveupButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				giveupButtonActionPerformed(evt);
			}
		});
		jPanel1.add(giveupButton);

		backplayToggleButton.setText("游戏回放");
		backplayToggleButton
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(java.awt.event.ActionEvent evt) {
						backplayToggleButtonActionPerformed(evt);
					}
				});
		jPanel1.add(backplayToggleButton);

		add(jPanel1, java.awt.BorderLayout.PAGE_END);

		final JButton button = new JButton();
		button.addActionListener(new ButtonActionListener());
		button.setText("更换背景");
		jPanel1.add(button);

		jPanel2.setPreferredSize(new java.awt.Dimension(110, 100));
		jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
				50, 60));

		jLabel5.setPreferredSize(new java.awt.Dimension(42, 55));
		jPanel2.add(jLabel5);

		leftInfoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/res/infoPanelLeft.png")));
		leftInfoLabel
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		leftInfoLabel
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel2.add(leftInfoLabel);

		myChessColorLabel.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/res/whiteChess.png")));
		jPanel2.add(myChessColorLabel);

		add(jPanel2, java.awt.BorderLayout.LINE_START);

		jPanel3.setPreferredSize(new java.awt.Dimension(110, 100));
		jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER,
				50, 60));

		jLabel6.setPreferredSize(new java.awt.Dimension(42, 55));
		jPanel3.add(jLabel6);

		rightInfoLabel.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/res/infoPanel.png")));
		rightInfoLabel
				.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
		rightInfoLabel
				.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
		jPanel3.add(rightInfoLabel);

		towardsChessColorLabel.setIcon(new javax.swing.ImageIcon(getClass()
				.getResource("/res/blackChess.png"))); // NOI18N
		jPanel3.add(towardsChessColorLabel);

		add(jPanel3, java.awt.BorderLayout.LINE_END);

		jPanel4.setLayout(new java.awt.BorderLayout());

		bannerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		bannerLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/res/logo.png"))); // NOI18N
		bannerLabel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1,
				5, 1));
		bannerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		bannerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				bannerLabelMouseClicked(evt);
			}
		});
		jPanel4.add(bannerLabel, java.awt.BorderLayout.CENTER);

		add(jPanel4, java.awt.BorderLayout.PAGE_START);

		add(gobangPanel1, java.awt.BorderLayout.CENTER);

		javax.swing.GroupLayout gobangPanel1Layout = new javax.swing.GroupLayout(
				gobangPanel1);
		gobangPanel1Layout.setHorizontalGroup(gobangPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						280, Short.MAX_VALUE));
		gobangPanel1Layout.setVerticalGroup(gobangPanel1Layout
				.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0,
						248, Short.MAX_VALUE));
		gobangPanel1.setLayout(gobangPanel1Layout);
	}

	/**
	 * 开始按钮的事件处理方法
	 * 
	 * @param evt
	 *            - 事件对象
	 */
	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// 获取主窗体对象
		GameFiveChess mainFrame = (GameFiveChess) getRootPane().getParent();
		if (mainFrame.serverSocket == null) {
			JOptionPane.showMessageDialog(this, "请等待对方连接。");
			return;
		}
		if (gobangPanel1.isStart()) {
			return;
		}
		// 设置各个按钮的可用状态
		startButton.setEnabled(false);
		giveupButton.setEnabled(true);
		heqiButton.setEnabled(true);
		backButton.setEnabled(true);
		gobangPanel1.setStart(true); // 设置游戏的开始状态
		gobangPanel1.setTowardsWin(false); // 设置对家胜利状态
		gobangPanel1.setWin(false); // 设置自己胜利状态
		gobangPanel1.setDraw(false); // 设置和棋状态
		send(OPRATION_START);// 发送开始指令
		fenqi(); // 分配双方棋子
		fillChessBoard(gobangPanel1.getMyColor());// 使用自己的棋子颜色清屏
		fillChessBoard((byte) 0); // 使用空棋子清屏
		byte[][] data = new byte[15][15]; // 创建一个空的棋盘布局
		GobangModel.getInstance().setChessmanArray(data);// 设置棋盘使用空布局
	}

	/**
	 * 认输按钮的事件处理方法
	 * 
	 * @param evt
	 *            - 按钮的事件对象
	 */
	private void giveupButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// 如果没到自己走棋，提示用户等待
		if (!gobangPanel1.isTurn()) {
			JOptionPane.showMessageDialog(this, "没到你走棋呢。", "请等待...",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		send(OPRATION_GIVEUP);// 发送认输指令
		// 启动一个新的线程使认输按钮5秒不可用
		new Thread() {
			@Override
			public void run() {
				try {
					giveupButton.setEnabled(false);
					sleep(5000);
					giveupButton.setEnabled(true);
				} catch (InterruptedException ex) {
					Logger.getLogger(ChessPanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}.start();
	}

	/**
	 * 悔棋按钮的事件处理方法
	 * 
	 * @param evt
	 */
	private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
		// 如果没到自己走棋，提示用户
		if (!gobangPanel1.isTurn()) {
			JOptionPane.showMessageDialog(this, "没到你走棋呢。", "请等待...",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		send(OPRATION_REPENT);// 发送悔棋命令
		new Thread() { // 开启新的线程，使悔棋按钮禁用5秒
			@Override
			public void run() {
				try {
					backButton.setEnabled(false);
					sleep(5000);
					backButton.setEnabled(true);
				} catch (InterruptedException ex) {
					Logger.getLogger(ChessPanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}.start();
	}

	/**
	 * 和棋按钮的事件处理方法
	 * 
	 * @param evt
	 *            - 按钮的action事件对象
	 */
	private void heqiButtonActionPerformed(java.awt.event.ActionEvent evt) {
		send(OPRATION_DRAW);// 发送和棋指令
		new Thread() { // 开启新的线程使和棋按钮5秒不可用
			@Override
			public void run() {
				try {
					heqiButton.setEnabled(false);
					sleep(5000);
					heqiButton.setEnabled(true);
				} catch (InterruptedException ex) {
					Logger.getLogger(ChessPanel.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		}.start();
	}

	/**
	 * 广告图片的鼠标单击事件处理方法
	 * 
	 * @param evt
	 *            - 鼠标事件对象
	 */
	private void bannerLabelMouseClicked(java.awt.event.MouseEvent evt) {
		try {
			// 调用Desktop类的browse方法浏览编程词典首页
			if (Desktop.isDesktopSupported()) {
				Desktop.getDesktop().browse(
						new URL("http://www.mrbccd.com").toURI());
			} else {
				JOptionPane.showMessageDialog(this, "当前系统不支持该操作");
			}
		} catch (Exception ex) {
			Logger.getLogger(ChessPanel.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	/**
	 * 游戏回放按钮的事件处理方法
	 * 
	 * @param evt
	 *            - 事件对象
	 */
	private void backplayToggleButtonActionPerformed(
			java.awt.event.ActionEvent evt) {
		// 如果游戏进行中，提示用户游戏结束后在观看游戏回放
		if (gobangPanel1.isStart()) {
			JOptionPane.showMessageDialog(this, "请在游戏结束后，观看游戏回放。");
			backplayToggleButton.setSelected(false);
			return;
		}
		if (!backplayToggleButton.isSelected()) {
			backplayToggleButton.setText("游戏回放");
		} else {
			backplayToggleButton.setText("终止回放");
			new Thread() { // 开启新的线程播放游戏记录
				public void run() {
					Object[] toArray = gobangPanel1.getOldRec();
					if (toArray == null) {
						JOptionPane.showMessageDialog(ChessPanel.this,
								"没有游戏记录", "游戏回放", JOptionPane.WARNING_MESSAGE);
						backplayToggleButton.setText("游戏回放");
						backplayToggleButton.setSelected(false);
						return;
					}
					// 清除界面的结局文字，包括对方胜利、你胜利了、此战平局
					gobangPanel1.setTowardsWin(false);
					gobangPanel1.setWin(false);
					gobangPanel1.setDraw(false);
					for (int i = toArray.length - 1; !gobangPanel1.isStart()
							&& backplayToggleButton.isSelected() && i >= 0; i--) {
						try {
							Thread.sleep(1000); // 线程休眠1秒
						} catch (InterruptedException ex) {
							Logger.getLogger(ChessPanel.class.getName()).log(
									Level.SEVERE, null, ex);
						}
						GobangModel.getInstance().updateChessmanArray(
								(byte[][]) toArray[i]); // 根据游戏记录跟换每一布游戏的棋谱
						gobangPanel1.repaint(); // 重绘棋盘
					}
					backplayToggleButton.setSelected(false);
					backplayToggleButton.setText("游戏回放");
				}
			}.start();
		}
	}

	/**
	 * 更换背景图片的按钮事件监听器
	 * 
	 * @author Li Zhong Wei
	 */
	private class ButtonActionListener implements ActionListener {
		public void actionPerformed(final ActionEvent e) {
			backIndex = backIndex % 9 + 1; // 获取9张背景图片的索引的递增
			URL url = getClass().getResource("/res/bg/" + backIndex + ".jpg");
			backImg = new ImageIcon(url).getImage(); // 初始化棋盘图片
			repaint(); // 重新绘制下棋面板
		}
	}
}
