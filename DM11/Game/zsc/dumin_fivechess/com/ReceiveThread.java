package zsc.dumin_fivechess.com;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class ReceiveThread extends Thread {
	private final ServerSocket chatSocketServer;
	GameFiveChess frame;
	private String host;

	/**
	 * 线程的主体方法
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				frame.serverSocket = chatSocketServer.accept(); // 接收Socket连接
				Socket serverSocket = frame.serverSocket;
				host = serverSocket.getInetAddress().getHostName(); // 获取对方主机信息
				String ip = serverSocket.getInetAddress().getHostAddress(); // 获取对方IP地址
				int link = JOptionPane.showConfirmDialog(frame, "收到" + host
						+ "的联机请求，是否接受？"); // 询问是否接受联机
				if (link == JOptionPane.YES_OPTION) { // 如果接受联机
					LoginPanel loginPanel = (LoginPanel) frame.getRootPane()
							.getGlassPane(); // 获取登录面板的实例
					loginPanel.setLinkIp(ip); // 设置登录面板的对家IP信息
				}
				serverSocket.setOOBInline(true); // 启用紧急数据的接收
				InputStream is = serverSocket.getInputStream(); // 获取网络输入流
				ObjectInputStream objis = new ObjectInputStream(is);// 创建对象输入流
				while (frame.isVisible()) {
					serverSocket.sendUrgentData(255); // 发送紧急数据
					Object messageObj = objis.readObject(); // 从对象输入流读取Java对象
					if (messageObj instanceof String) { // 如果读取的对象是String类型
						String name = frame.getTowardsUser().getName();// 获取对家昵称
						frame.appendMessage(name + "：" + messageObj); // 将字符串信息添加到通讯面板
					} else if (messageObj instanceof byte[][]) { // 如果读取的是字节数组对象
						GobangModel.getInstance().setChessmanArray( // 将数组对象设置为棋盘模型数据
								(byte[][]) messageObj);
						frame.getChessPanel1().getGobangPanel1().setTurn(true);// 获得走棋权限
						byte myColor = frame.getChessPanel1().getGobangPanel1()
								.getMyColor(); // 获取自己的棋子颜色
						frame.getChessPanel1().getGobangPanel1().zhengliBoard(
								myColor); // 整理棋盘
						frame.getChessPanel1().backButton.setEnabled(true);// 悔棋按钮可用
					} else if (messageObj instanceof Integer) {// 如果是整形对象
						oprationHandler(messageObj);// 命令代码的接收和处理方法
					} else if (messageObj instanceof UserBean) {// 如果是用户实体对象
						UserBean user = (UserBean) messageObj;
						frame.setTowardsUser(user); // 设置对家信息
					}
				}
			} catch (SocketException ex) {
				Logger.getLogger(GameFiveChess.class.getName()).log(Level.SEVERE,
						null, ex);
				JOptionPane.showMessageDialog(frame, "连接中断");
				frame.getChessPanel1().reInit();
				DefaultTableModel model = (DefaultTableModel) frame.userInfoTable.getModel();
				model.setRowCount(0);
				frame.getGlassPane().setVisible(true);
			} catch (IOException ex) {
				Logger.getLogger(GameFiveChess.class.getName()).log(Level.SEVERE,
						null, ex);
			} catch (ClassNotFoundException ex) {
				Logger.getLogger(GameFiveChess.class.getName()).log(Level.SEVERE,
						null, ex);
			}
		}
	}

	/**
	 * 构造方法
	 * 
	 * @param chatSocketServer
	 *            - Socket服务类
	 * @param outer
	 *            - 主窗体对象
	 */
	public ReceiveThread(ServerSocket chatSocketServer, GameFiveChess outer) {
		super();
		this.frame = outer;
		this.chatSocketServer = chatSocketServer;
	}

	/**
	 * 处理远程命令的方法
	 * 
	 * @param messageObj
	 *            - 命令代码
	 */
	private void oprationHandler(Object messageObj) {
		int code = (Integer) messageObj; // 获取命令代码
		String towards = frame.getTowardsUser().getName();// 获取对家昵称
		int option;
		switch (code) {
		case ChessPanel.OPRATION_REPENT: // 如果是悔棋请求
			System.out.println("请求悔棋");
			// 询问玩家是否同意对方悔棋
			option = JOptionPane.showConfirmDialog(frame,
					towards + "要悔棋，是否同意？", "求你了，我走错了，让我悔棋！！！",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
			// 在聊天面板添加悔棋信息
			frame.appendMessage("对方请求悔棋.......");
			if (option == JOptionPane.YES_OPTION) { // 如果同意悔棋
				frame.send(ChessPanel.OPRATION_NODE_REPENT);// 发送同意悔棋的消息
				frame.getChessPanel1().repentOperation();// 执行本地的悔棋操作
				frame.appendMessage("接受对方的悔棋请求。");// 添加悔棋信息到聊天面板
				frame.send(frame.getUser().getName() + "接受悔棋请求");
			} else { // 如果不同意悔棋
				// 添加不同意悔棋的信息到聊天面板
				frame.send(frame.getUser().getName() + "拒绝悔棋请求");
				frame.appendMessage("拒绝了对方的悔棋请求。");
			}
			break;
		case ChessPanel.OPRATION_NODE_REPENT: // 如果是同意悔棋命令
			System.out.println("同意悔棋命令");
			frame.getChessPanel1().repentOperation(); // 执行本地的悔棋操作
			frame.appendMessage("悔棋成功"); // 把悔棋成功信息添加到聊天面板
			break;
		case ChessPanel.OPRATION_NODE_DRAW: // 如果是同意和棋命令
			System.out.println("同意和棋命令");
			// 设置和棋状态为true
			frame.getChessPanel1().getGobangPanel1().setDraw(true);
			frame.getChessPanel1().reInit(); // 初始化游戏状态变量
			frame.appendMessage("此战平局。"); // 将和棋信息添加到聊天面板
			break;
		case ChessPanel.OPRATION_DRAW: // 如果是和棋请求
			System.out.println("请求和棋");
			// 询问玩家是否同意和棋
			option = JOptionPane.showConfirmDialog(frame, towards
					+ "请求和棋，是否同意？", "大哥，和棋吧！！！", JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);
			frame.appendMessage("对方请求和棋......."); // 添加信息到聊天面板
			if (option == JOptionPane.YES_OPTION) { // 如果同意和棋
				frame.send(ChessPanel.OPRATION_NODE_DRAW);// 发送接受和棋的消息
				// 设置和棋状态为true
				frame.getChessPanel1().getGobangPanel1().setDraw(true);
				frame.getChessPanel1().reInit(); // 初始化游戏状态变量
				frame.appendMessage("接受对方的和棋请求。"); // 添加信息到聊天面板
				frame.send(frame.getUser().getName() + "接受和棋请求");
			} else { // 如果不同意和棋
				// 发送拒绝信息
				frame.send(frame.getUser().getName() + "拒绝和棋请求");
				frame.appendMessage("拒绝了对方的和棋请求。");
			}
			break;
		case ChessPanel.OPRATION_GIVEUP: // 如果是对方认输的请求
			System.out.println("对方认输");
			// 询问玩家是否同意对方认输
			option = JOptionPane.showConfirmDialog(frame, towards
					+ "请求认输，是否同意？", "对方认输", JOptionPane.YES_NO_OPTION);
			frame.appendMessage("对方请求认输.......");
			if (option == JOptionPane.YES_OPTION) { // 如果同意对方认输
				frame.send(ChessPanel.WIN);// 发送胜利消息
				// 设置胜利状态为true
				frame.getChessPanel1().getGobangPanel1().setWin(true);
				frame.getChessPanel1().reInit(); // 初始化游戏的状态变量
				frame.appendMessage("接受对方的认输请求。");
			} else {
				frame.send(frame.getUser().getName() + "拒绝认输请求");
				frame.appendMessage("拒绝了对方的认输请求。");
			}
			break;
		case ChessPanel.OPRATION_START: // 如果是开始游戏的请求
			System.out.println("请求开始");
			// 如果自己已经执行游戏开始动作
			if (frame.getChessPanel1().getGobangPanel1().isStart()) {
				frame.send((int) ChessPanel.OPRATION_ALL_START); // 发送全部开始命令
				frame.getChessPanel1().setTowardsStart(true); // 设置对家游戏开始状态为true
			}
			break;
		case ChessPanel.OPRATION_ALL_START: // 如果是回应开始请求
			System.out.println("回应开始请求");
			frame.getChessPanel1().setTowardsStart(true); // 设置对家为开始状态
			break;
		case ChessPanel.WIN: // 如果是胜利的命令代码
			System.out.println("对方胜利");
			// 设置对家胜利状态为true
			frame.getChessPanel1().getGobangPanel1().setTowardsWin(true);
			frame.getChessPanel1().reInit(); // 初始化游戏状态变量
			break;
		default:
			System.out.println("未知操作代码：" + code);
		}
	}
}
