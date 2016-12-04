package zsc.dumin_fivechess.com;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 * 登录面板
 * 
 * @author 杜敏
 */
public class LoginPanel extends javax.swing.JPanel {

	private Socket socket;
	private UserBean user;
	protected boolean linked;

	/**
	 * 构造方法
	 */
	public LoginPanel() {
		initComponents(); // 调用初始化界面的方法
	}

	public boolean isLinked() {
		return linked;
	}

	public void setLinked(boolean linked) {
		this.linked = linked;
	}

	void setLinkIp(String ip) {
		ipTextField.setText(ip);
		ipTextField.setEditable(false);
		nameTextField.requestFocus();
	}

	/**
	 * 初始化登录界面的方法
	 */
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jLabel1 = new javax.swing.JLabel();
		nameTextField = new javax.swing.JTextField();
		jLabel2 = new javax.swing.JLabel();
		ipTextField = new javax.swing.JTextField();
		loginButton = new javax.swing.JButton();
		closeButton = new javax.swing.JButton();

		setForeground(java.awt.Color.gray);
		setOpaque(false);
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);
			}
		});
		setLayout(new java.awt.GridBagLayout());

		jLabel1.setFont(new java.awt.Font("楷体_gbk", 2, 24));
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		jLabel1.setText("昵  称：");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		add(jLabel1, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipady = -5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
		add(nameTextField, gridBagConstraints);

		jLabel2.setFont(new java.awt.Font("楷体_gbk", 2, 24));
		jLabel2.setForeground(java.awt.Color.white);
		jLabel2.setText("对方 IP：");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		add(jLabel2, gridBagConstraints);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.ipady = -5;
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.insets = new java.awt.Insets(3, 0, 3, 0);
		add(ipTextField, gridBagConstraints);

		loginButton.setText("登录");
		loginButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				loginButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 40);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		add(loginButton, gridBagConstraints);

		closeButton.setText("关闭");
		closeButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new Insets(0, 0, 0, 55);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 2;
		add(closeButton, gridBagConstraints);
	}

	private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void formMouseClicked(java.awt.event.MouseEvent evt) {
		JOptionPane.showMessageDialog(this, "还没登录呢，往哪点？");
	}

	/**
	 * 登录按钮的事件处理方法
	 * 
	 * @param evt
	 *            - 按钮的事件对象
	 */
	private void loginButtonActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			// 获取主窗体的实例对象
			GameFiveChess mainFrame = (GameFiveChess) getParent().getParent();
			String name = nameTextField.getText(); // 获取用户昵称
			if (name.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "请输入昵称");
				return;
			}
			String ipText = ipTextField.getText(); // 获取对家IP地址
			if(ipText==null||ipText.isEmpty()){
				JOptionPane.showMessageDialog(this, "请输入对家IP地址");
				return;
			}
			ipTextField.setEditable(true);
			InetAddress ip = InetAddress.getByName(ipText);
			if(ip.equals(InetAddress.getLocalHost())){
				JOptionPane.showMessageDialog(this, "不能输入自己的IP地址");
				return;
			}
			socket = new Socket(ip, 9528); // 创建Socket连接对家主机
			if (socket.isConnected()) { // 如果连接成功
				user = new UserBean(); // 创建用户对象
				// 获取当前时间对象
				Time time = new Time(System.currentTimeMillis());
				user.setName(name); // 初始化用户昵称
				user.setHost(InetAddress.getLocalHost()); // 初始化用户IP
				user.setTime(time); // 初始化用户登录时间
				socket.setOOBInline(true); // 启用紧急数据的接收
				mainFrame.setSocket(socket); // 设置主窗体的Socket连接对象
				mainFrame.setUser(user); // 添加本地用户对象到主窗体对象
				mainFrame.send(user); // 发送本地用户对象到对家主机
				setVisible(false); // 隐藏登录窗体
			}
		} catch (UnknownHostException ex) {
			Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE,
					null, ex);
			JOptionPane.showMessageDialog(this, "输入的IP不正确");
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "对方主机无法连接");
		}
	}

	/**
	 * 绘制组件界面的方法
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g; // 获取2D绘图上下文
		Composite composite = g2.getComposite(); // 备份合成模式
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,
				0.8f)); // 设置绘图使用透明合成规则
		g2.fillRect(0, 0, getWidth(), getHeight()); // 使用当前颜色填充矩形空间
		g2.setComposite(composite); // 恢复原有合成模式
		super.paintComponent(g2); // 执行超类的组件绘制方法
	}

	private javax.swing.JButton closeButton;
	private javax.swing.JTextField ipTextField;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JButton loginButton;
	private javax.swing.JTextField nameTextField;
}
