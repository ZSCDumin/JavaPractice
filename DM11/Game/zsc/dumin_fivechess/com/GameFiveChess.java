package zsc.dumin_fivechess.com;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author 杜敏
 */
public class GameFiveChess extends JFrame {

	private javax.swing.JTextArea chatArea;
	private javax.swing.JTextField chatTextField;
	private zsc.dumin_fivechess.com.ChessPanel chessPanel1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private zsc.dumin_fivechess.com.LoginPanel loginPanel1;
	private javax.swing.JButton sendButton;
	protected javax.swing.JTable userInfoTable;
	private javax.swing.JTextArea userInfoTextArea;
	private Socket socket;
	private ObjectOutputStream objout;
	private UserBean towardsUser;// 对家
	protected UserBean user;
	Socket serverSocket;

	public Socket getServerSocket() {
		return serverSocket;
	}

	public Socket getSocket() {
		return socket;
	}

	/**
	 * 向对家发送信息的方法
	 * 
	 * @param message
	 *            - 要发送的文本或其他类型的对象
	 */
	public void send(Object message) {
		try {
			objout.writeObject(message); // 向对象输出流添加对象
			objout.flush();
		} catch (IOException ex) {
			Logger.getLogger(GameFiveChess.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public UserBean getUser() {
		return user;
	}

	/**
	 * 设置用户信息的方法
	 * 
	 * @param user
	 *            - 本地用户对象
	 */
	public void setUser(UserBean user) {
		this.user = user;
		// 向用户信息面板添加昵称
		userInfoTextArea.setText("昵称：" + user.getName() + "\n");
		// 添加IP信息
		userInfoTextArea.append("ＩＰ：" + user.getHost().getHostAddress() + "\n");
		// 获取用户信息表格组件的数据模型对象
		DefaultTableModel model = (DefaultTableModel) userInfoTable.getModel();
		Vector dataVector = model.getDataVector();
		Vector row = new Vector(); // 使用用户信息创建单行数据的向量
		row.add(user.getName());
		row.add(user.getHost().getHostName());
		row.add(user.getTime());
		if (!dataVector.contains(row)) {
			model.getDataVector().add(row); // 把用户信息添加到表格组件中
		}
		// 设置本地用户的昵称
		chessPanel1.leftInfoLabel.setText(user.getName());
		userInfoTable.revalidate();
	}

	/**
	 * 设置Socket连接和初始化对象输出流的方法
	 * 
	 * @param chatSocketArg
	 *            - Socket对象
	 */
	public void setSocket(Socket chatSocketArg) {
		try {
			socket = chatSocketArg;
			OutputStream os = socket.getOutputStream(); // 获取Socket的输出流
			objout = new ObjectOutputStream(os); // 创建对象输出流
		} catch (IOException ex) {
			Logger.getLogger(GameFiveChess.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	/**
	 * 主窗体的构造方法
	 */
	public GameFiveChess() {
		initComponents(); // 初始化窗体界面
		setGlassPane(loginPanel1); // 设置登录面板为玻璃面板
		loginPanel1.setVisible(true); // 显示登录面板
	}

	/**
	 * 设置对家用户信息的方法
	 * 
	 * @param user
	 *            - 对家通过网络发送来的用户对象
	 */
	public void setTowardsUser(UserBean user) {
		this.towardsUser = user; // 对家用户对象
		// 获取用户信息列表的表格数据模型
		DefaultTableModel model = (DefaultTableModel) userInfoTable.getModel();
		Vector row = new Vector(); // 创建承载表格单行数据的向量集合对象
		row.add(towardsUser.getName()); // 添加用户姓名
		row.add(towardsUser.getHost().getHostName());// 添加主机名称
		row.add(towardsUser.getTime()); // 添加用户登录时间
		Vector dataVector = model.getDataVector();
		if (!dataVector.contains(row)) {
			model.getDataVector().add(row); // 添加用户信息到表格中
		}
		// 设置对家用户头像的昵称
		chessPanel1.rightInfoLabel.setText(towardsUser.getName());
		userInfoTable.revalidate();
	}

	public UserBean getTowardsUser() {
		return towardsUser;
	}

	/**
	 * 初始化主窗体界面的方法
	 */
	private void initComponents() {
		loginPanel1 = new zsc.dumin_fivechess.com.LoginPanel();
		chessPanel1 = new zsc.dumin_fivechess.com.ChessPanel();
		jPanel1 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel1.setOpaque(true);
		jLabel1.setBackground(Color.WHITE);
		jScrollPane2 = new javax.swing.JScrollPane();
		userInfoTextArea = new javax.swing.JTextArea();
		jPanel4 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		userInfoTable = new javax.swing.JTable();
		jPanel2 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		chatTextField = new javax.swing.JTextField();
		sendButton = new javax.swing.JButton();
		jScrollPane3 = new javax.swing.JScrollPane();
		chatArea = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("小小五子棋");
		getContentPane().add(chessPanel1, java.awt.BorderLayout.CENTER);

		jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1,
				javax.swing.BoxLayout.PAGE_AXIS));

		jPanel3.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel3.setPreferredSize(new java.awt.Dimension(225, 50));
		jPanel3.setLayout(new java.awt.BorderLayout());

		jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/res/infoPanelLeft.png"))); // NOI18N
		jPanel3.add(jLabel1, java.awt.BorderLayout.WEST);

		userInfoTextArea.setColumns(20);
		userInfoTextArea.setEditable(false);
		userInfoTextArea.setLineWrap(true);
		userInfoTextArea.setRows(5);
		jScrollPane2.setViewportView(userInfoTextArea);

		jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel3);

		jPanel4.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel4.setPreferredSize(new java.awt.Dimension(100, 20));
		jPanel4.setLayout(new java.awt.BorderLayout());

		jScrollPane1.setMaximumSize(new java.awt.Dimension(32767, 30));
		jScrollPane1.setPreferredSize(new java.awt.Dimension(241, 30));

		userInfoTable.setModel(new javax.swing.table.DefaultTableModel(
				new Object[][] {

				}, new String[] { "昵称", "主机", "联机时间" }) {
			boolean[] canEdit = new boolean[] { false, false, false };

			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		userInfoTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jScrollPane1.setViewportView(userInfoTable);

		jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel4);

		jPanel2.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(0, 0, 0)));
		jPanel2.setPreferredSize(new java.awt.Dimension(100, 300));
		jPanel2.setLayout(new java.awt.BorderLayout());

		jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5,
				javax.swing.BoxLayout.LINE_AXIS));

		chatTextField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				chatTextFieldKeyPressed(evt);
			}
		});
		jPanel5.add(chatTextField);

		sendButton.setText("发送");
		sendButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				sendButtonActionPerformed(evt);
			}
		});
		jPanel5.add(sendButton);

		jPanel2.add(jPanel5, java.awt.BorderLayout.PAGE_END);

		chatArea.setColumns(20);
		chatArea.setEditable(false);
		chatArea.setLineWrap(true);
		chatArea.setTabSize(4);
		jScrollPane3.setViewportView(chatArea);

		jPanel2.add(jScrollPane3, java.awt.BorderLayout.CENTER);

		jPanel1.add(jPanel2);

		getContentPane().add(jPanel1, java.awt.BorderLayout.EAST);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit()
				.getScreenSize();
		setBounds((screenSize.width - 1000) / 2, (screenSize.height - 700) / 2,
				1000, 700);
	}

	/**
	 * 聊天窗体的发送按钮事件处理方法
	 * 
	 * @param evt
	 *            - 事件对象
	 */
	private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {
		String message = (String) chatTextField.getText(); // 获取文本信息
		if (message == null || message.isEmpty()) {
			return;
		}
		chatTextField.setText(""); // 清空文本框内容
		appendMessage(user.getName() + "：" + message); // 将发送的信息添加到聊天记录
		send(message); // 发送信息
	}

	private void chatTextFieldKeyPressed(java.awt.event.KeyEvent evt) {
		if (evt.getKeyChar() == '\n') {
			sendButton.doClick();
		}
	}

	/**
	 * 添加聊天信息的方法
	 * 
	 * @param message
	 *            - 聊天信息文本
	 */
	protected void appendMessage(final String message) {
		Runnable runnable = new Runnable() { // 创建线程对象
			@Override
			public void run() {
				chatArea.append("\n" + message); // 向聊天文本区域组件追加换行文本
			}
		};
		if (SwingUtilities.isEventDispatchThread()) {
			runnable.run(); // 在事件队列线程中执行该线程对象
		} else {
			SwingUtilities.invokeLater(runnable);
		}
	}

	/**
	 * 启动Socket服务器
	 */
	public void startServer() {
		try {
			// 创建Socket服务器对象
			final ServerSocket chatSocketServer = new ServerSocket(9528);
			// 创建接收信息的线程
			new ReceiveThread(chatSocketServer, this).start();
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(this, "本程序禁止重复运行，只能同时存在一个实例。",
					"你敢重复运行？", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			Logger.getLogger(GameFiveChess.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public ChessPanel getChessPanel1() {
		return chessPanel1;
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager
							.setLookAndFeel(new NimbusLookAndFeel());
					GameFiveChess frame = new GameFiveChess();
					frame.startServer();
					frame.setVisible(true);
				} catch (UnsupportedLookAndFeelException ex) {
					Logger.getLogger(GameFiveChess.class.getName()).log(
							Level.SEVERE, null, ex);
				}
			}
		});
	}
}
