package zsc.dumin_fivechess.com;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.ImageIcon;

/**
 * 棋盘面板
 * 
 * @author 杜敏
 */
public class GobangPanel extends javax.swing.JPanel {
	// 黑白棋盒的图标对象和星号的图像对象以及背景图片对象
	private Image backImg;
	private Image white_chessman_img;
	private Image black_chessman_img;
	private Image rightTop_img;
	int chessWidth, chessHeight; // 棋子宽度与高度
	public final static byte WHITE_CHESSMAN = 1;
	public final static byte BLACK_CHESSMAN = -1;
	Dimension size; // 棋盘面板的大小
	private boolean start = false; // 开始
	private Object[] oldRec;
	Deque<byte[][]> chessQueue = new LinkedList(); // 游戏的队列记录
	private boolean turn = false; // 是否到自己走棋
	private boolean towardsWin; // 对方胜利
	private boolean win; // 胜利
	private boolean draw; // 和棋
	private ChessPanel chessPanel;

	/**
	 * 棋盘面板的构造方法
	 */
	public GobangPanel() {
		URL white_url = getClass().getResource("/res/whiteChessman.png");
		URL black_url = getClass().getResource("/res/blackChessman.png");
		URL rightTop_url = getClass().getResource("/res/rightTop.gif");
		white_chessman_img = new ImageIcon(white_url).getImage(); // 初始化白旗图片
		black_chessman_img = new ImageIcon(black_url).getImage(); // 初始化黑旗图片
		// 初始化连成线的棋子上的星图
		rightTop_img = new ImageIcon(rightTop_url).getImage();
		size = new Dimension(getWidth(), getHeight());
		setPreferredSize(size);
		initComponents();
	}

	/**
	 * 记录游戏结束前的棋盘记录
	 */
	public void oldRec() {
		oldRec = (Object[]) chessQueue.toArray();
	}

	public Object[] getOldRec() {
		return oldRec;
	}

	public boolean isTurn() {
		return turn;
	}

	protected void setTurn(boolean turn) {
		this.turn = turn;
		chessPanel.setTurn(turn);
	}

	protected boolean isStart() {
		return start;
	}

	public void setStart(boolean start) {
		chessQueue.clear();
		this.start = start;
		if (chessPanel == null) {
			chessPanel = (ChessPanel) getParent();
		}
		repaint();
	}

	public boolean isTowardsWin() {
		return towardsWin;
	}

	public void setTowardsWin(boolean towardsWin) {
		this.towardsWin = towardsWin;
	}

	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}

	public boolean isDraw() {
		return draw;
	}

	public void setDraw(boolean draw) {
		this.draw = draw;
	}

	public Deque<byte[][]> getChessQueue() {
		return chessQueue;
	}

	/**
	 * 重写父类的paint方法，绘制自己的组件界面
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		super.paint(g); // 调用父类的绘图方法
		if (chessPanel != null) {
			chessPanel.setTurn(turn);
		}
		Composite composite = g.getComposite(); // 备份合成模式
		drawPanel(g); // 调用绘制棋盘的方法
		g.translate(4, 4);
		size = new Dimension(getWidth(), getHeight());// 获取棋盘面板的大小
		chessWidth = size.width / 15; // 初始化棋子宽
		chessHeight = size.height / 15; // 初始化棋子高
		byte[][] chessmanArray = gobangModel1.getChessmanArrayCopy();
		for (int i = 0; i < chessmanArray.length; i++) {// 遍历棋盘数据模型绘制棋子
			for (int j = 0; j < chessmanArray[i].length; j++) {
				byte chessman = chessmanArray[i][j];
				int x = i * chessWidth;
				int y = j * chessHeight;
				if (chessman != 0)
					System.out.println("chess is:" + chessman);
				if (chessman == WHITE_CHESSMAN) { // 绘制白旗
					g.drawImage(white_chessman_img, x, y, chessWidth,
							chessHeight, this);
				} else if (chessman == BLACK_CHESSMAN) {// 绘制黑旗
					g.drawImage(black_chessman_img, x, y, chessWidth,
							chessHeight, this);
				} else if (chessman == (WHITE_CHESSMAN ^ 3)) { // 绘制最近的白旗落子
					g.drawImage(white_chessman_img, x, y, chessWidth,
							chessHeight, this);
					g.drawRect(x, y, chessWidth, chessHeight);
				} else if (chessman == (BLACK_CHESSMAN ^ 3)) { // 绘制最近的黑棋落子
					g.drawImage(black_chessman_img, x, y, chessWidth,
							chessHeight, this);
					g.drawRect(x, y, chessWidth, chessHeight);
				} else if (chessman == ((byte) (WHITE_CHESSMAN ^ 8))) {// 绘制导致胜利的连线白旗
					g.drawImage(white_chessman_img, x, y, chessWidth,
							chessHeight, this);
					g.drawImage(rightTop_img, x, y, chessWidth, chessHeight,
							this);
				} else if (chessman == (BLACK_CHESSMAN ^ 8)) {// 绘制导致胜利的连线黑旗
					g.drawImage(black_chessman_img, x, y, chessWidth,
							chessHeight, this);
					g.drawImage(rightTop_img, x, y, chessWidth, chessHeight,
							this);
				}
			}
		}
		if (!isStart()) { // 如果游戏不处于开始状态
			if (towardsWin || win || draw) { // 如果游戏处于胜利或和棋状态，绘制棋盘提示信息
				g.setComposite(AlphaComposite.SrcOver.derive(0.7f)); // 设置70%
				// 透明的合成规则
				String mess = "对方胜利"; // 定义提示信息
				g.setColor(Color.RED); // 设置前景色为红色
				if (win) { // 如果是自己胜利
					mess = "你胜利了"; // 设置胜利提示信息
					g.setColor(new Color(0x007700)); // 设置绿色前景色
				} else if (draw) { // 如果是和棋状态
					mess = "此战平局"; // 定义和棋提示信息
					g.setColor(Color.YELLOW); // 设置和棋信息使用黄色提示
				}
				// 设置提示文本的字体为隶书、粗斜体、大小72
				Font font = new Font("隶书", Font.ITALIC | Font.BOLD, 72);
				g.setFont(font);
				// 获取字体渲染上下文对象
				FontRenderContext context = g.getFontRenderContext();
				// 计算提示信息的文本所占用的像素空间
				Rectangle2D stringBounds = font.getStringBounds(mess, context);
				double fontWidth = stringBounds.getWidth(); // 获取提示文本的宽度
				g.drawString(mess, (int) ((getWidth() - fontWidth) / 2),
						getHeight() / 2); // 居中绘制提示信息
				g.setComposite(composite); // 恢复原有合成规则
			} else { // 如果当前处于其他未开始游戏的状态
				String mess = "等待开始…"; // 定义等他提示信息
				Font font = new Font("隶书", Font.ITALIC | Font.BOLD, 48);
				g.setFont(font); // 设置48号隶书字体
				FontRenderContext context = g.getFontRenderContext();
				Rectangle2D stringBounds = font.getStringBounds(mess, context);
				double fontWidth = stringBounds.getWidth(); // 获取提示文本的宽度
				g.drawString(mess, (int) ((getWidth() - fontWidth) / 2),
						getHeight() / 2); // 居中绘制提示文本
			}
		}
	}

	/**
	 * 绘制棋盘的方法
	 * 
	 * @param g
	 *            - 绘图对象
	 */
	private void drawPanel(Graphics2D g) {
		Composite composite = g.getComposite(); // 备份合成规则
		Color color = g.getColor(); // 备份前景颜色
		g.setComposite(AlphaComposite.SrcOver.derive(0.6f));// 设置透明合成
		g.setColor(new Color(0xAABBAA)); // 设置前景白色
		g.fill3DRect(0, 0, getWidth(), getHeight(), true); // 绘制半透明的矩形
		g.setComposite(composite); // 恢复合成规则
		g.setColor(color); // 恢复原来前景色
		int w = getWidth(); // 棋盘宽度
		int h = getHeight(); // 棋盘高度
		int chessW = w / 15, chessH = h / 15; // 棋子宽度和高度
		int left = chessW / 2 + (w % 15) / 2; // 棋盘左边界
		int right = left + chessW * 14; // 棋盘右边界
		int top = chessH / 2 + (h % 15) / 2; // 棋盘上边界
		int bottom = top + chessH * 14; // 棋盘下边界
		for (int i = 0; i < 15; i++) {
			// 画每条横线
			g.drawLine(left, top + (i * chessH), right, top + (i * chessH));
		}
		for (int i = 0; i < 15; i++) {
			// 画每条竖线
			g.drawLine(left + (i * chessW), top, left + (i * chessW), bottom);
		}
	}

	private void initComponents() {
		gobangModel1 = new GobangModel(); // 创建棋盘模型的实例对象
		// 为棋盘添加事件监听器
		gobangModel1.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(java.beans.PropertyChangeEvent evt) {
				gobangModel1PropertyChange(evt);// 调用事件处理方法
			}
		});

		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				formMouseClicked(evt);
			}
		});
		setOpaque(false);
		setLayout(null);
	}

	public boolean isTowardsStart() {
		ChessPanel panel = (ChessPanel) getParent();
		return panel.isTowardsStart();
	}

	private void formMouseClicked(java.awt.event.MouseEvent evt) {
		// 判断是否开始游戏
		if (!start || !isTowardsStart() || myColor == 0 || !turn) {
			return;
		}
		Point point = evt.getPoint();
		int xindex = point.x / chessWidth;
		int yindex = point.y / chessHeight;
		byte[][] chessmanArray = gobangModel1.getChessmanArray();
		if (chessmanArray[xindex][yindex] == 0) {
			turn = !turn;
			chessmanArray[xindex][yindex] = (byte) (myColor ^ 3);
			gobangModel1.setChessmanArray(chessmanArray);
			chessPanel.backButton.setEnabled(false);
			zhengliBoard((byte) -myColor);
			int winColor = arithmetic(myColor, xindex, yindex);
			chessPanel.send(gobangModel1.getChessmanArrayCopy());
			if (winColor != 0 && winColor == myColor) {
				chessPanel.send(ChessPanel.WIN); // 发送胜利代码
				win = true;
				chessPanel.reInit();
			}
			// 在判断胜负情况后再发送model中的棋盘数组，因为这个数组可能带有标识连线的棋子数据
		}
	}

	/* 整理棋盘，将标识上一步的棋子恢复普通棋子 */
	public void zhengliBoard(byte color) {
		byte[][] chessmanArray = gobangModel1.getChessmanArray();
		for (int i = 0; i < chessmanArray.length; i++) {
			for (int j = 0; j < chessmanArray[i].length; j++) {
				if (chessmanArray[i][j] == (color ^ 3)) {
					chessmanArray[i][j] = color;
				}
			}
		}
		gobangModel1.updateChessmanArray(chessmanArray);
		repaint();
	}

	/**
	 * 五子棋算法
	 * 
	 * @param n
	 *            - 代表棋子颜色的整数
	 * @param Arow
	 *            - 行编号
	 * @param Acolumn
	 *            - 列编号
	 * @return 胜利一方的棋子颜色的整数
	 */
	public int arithmetic(int n, int Arow, int Acolumn) {
		int n3 = n ^ 3;
		byte n8 = (byte) (n ^ 8);
		byte[][] note = gobangModel1.getChessmanArrayCopy();
		int BCount = 1;
		// 纵向查找
		boolean Lbol = true;
		boolean Rbol = true;
		BCount = 1;
		for (int i = 1; i <= 5; i++) {
			if ((Acolumn + i) > 14) {// 如果棋子超出最大列数
				Rbol = false;
			}
			if ((Acolumn - i) < 0) {// 如果棋子超出最小列数
				Lbol = false;
			}
			if (Rbol == true) {
				if (note[Arow][Acolumn + i] == n
						|| note[Arow][Acolumn + i] == n3) {// 如果横向向右有相同的棋子
					++BCount;
					note[Arow][Acolumn + i] = n8;
				} else {
					Rbol = false;
				}
			}
			if (Lbol == true) {
				if (note[Arow][Acolumn - i] == n
						|| note[Arow][Acolumn - i] == n3) {// 如果横向向左有相同的棋子
					++BCount;
					note[Arow][Acolumn - i] = n8;
				} else {
					Lbol = false;
				}
			}
			if (BCount >= 5) {// 如果同类型的棋子数大于等于5个
				note[Arow][Acolumn] = n8;
				gobangModel1.updateChessmanArray(note);
				repaint();
				return n; // 返回胜利一方的棋子
			}
		}

		// 横向查找
		note = gobangModel1.getChessmanArrayCopy();
		boolean Ubol = true;
		boolean Dbol = true;
		BCount = 1;
		for (int i = 1; i <= 5; i++) {
			if ((Arow + i) > 14) {// 如果超出棋盘的最大行数
				Dbol = false;
			}
			if ((Arow - i) < 0) {// 如果超出棋盘的最小行数
				Ubol = false;
			}
			if (Dbol == true) {
				if (note[Arow + i][Acolumn] == n
						|| note[Arow + i][Acolumn] == n3) { // 如果向上有同类型的棋子
					++BCount;
					note[Arow + i][Acolumn] = n8;
				} else {
					Dbol = false;
				}
			}
			if (Ubol == true) {
				if (note[Arow - i][Acolumn] == n
						|| note[Arow - i][Acolumn] == n3) { // 如果向下有同类型的棋子
					++BCount;
					note[Arow - i][Acolumn] = n8;
				} else {
					Ubol = false;
				}
			}
			if (BCount >= 5) { // 如果同类型的棋子大于等于5个
				note[Arow][Acolumn] = n8;
				gobangModel1.updateChessmanArray(note);
				repaint();
				return n; // 返回胜利一方的棋子
			}
		}

		// 正斜查找
		note = gobangModel1.getChessmanArrayCopy();
		boolean LUbol = true;
		boolean RDbol = true;
		BCount = 1;
		for (int i = 1; i <= 5; i++) {
			if ((Arow - i) < 0 || (Acolumn - i < 0)) {// 如果超出左面的斜线
				LUbol = false;
			}
			if ((Arow + i) > 14 || (Acolumn + i > 14)) {// 如果超出右面的斜线
				RDbol = false;
			}
			if (LUbol == true) {
				if (note[Arow - i][Acolumn - i] == n
						|| note[Arow - i][Acolumn - i] == n3) {// 如果左上斜线上有相同类型的棋子
					++BCount;
					note[Arow - i][Acolumn - i] = n8;
				} else {
					LUbol = false;
				}
			}
			if (RDbol == true) {
				if (note[Arow + i][Acolumn + i] == n
						|| note[Arow + i][Acolumn + i] == n3) {// 如果右下斜线上有相同类型的棋子
					++BCount;
					note[Arow + i][Acolumn + i] = n8;
				} else {
					RDbol = false;
				}
			}
			if (BCount >= 5) {// 如果同类型的棋子大于等于5个
				note[Arow][Acolumn] = n8;
				gobangModel1.updateChessmanArray(note);
				repaint();
				return n; // 返回胜利一方的棋子
			}
		}
		// 反斜查找
		note = gobangModel1.getChessmanArrayCopy();
		boolean RUbol = true;
		boolean LDbol = true;
		BCount = 1;
		for (int i = 1; i <= 5; i++) {
			if ((Arow - i) < 0 || (Acolumn + i > 14)) {
				RUbol = false;
			}
			if ((Arow + i) > 14 || (Acolumn - i < 0)) {
				LDbol = false;
			}
			if (RUbol == true) {
				if (note[Arow - i][Acolumn + i] == n
						|| note[Arow - i][Acolumn + i] == n3) {// 如果左下斜线上有相同类型的棋子
					++BCount;
					note[Arow - i][Acolumn + i] = n8;
				} else {
					RUbol = false;
				}
			}
			if (LDbol == true) {
				if (note[Arow + i][Acolumn - i] == n
						|| note[Arow + i][Acolumn - i] == n3) {// 如果右上斜线上有相同类型的棋子
					++BCount;
					note[Arow + i][Acolumn - i] = n8;
				} else {
					LDbol = false;
				}
			}
			if (BCount >= 5) {// 如果同类型的棋子大于等于5个
				note[Arow][Acolumn] = n8;
				gobangModel1.updateChessmanArray(note);
				repaint();
				return n;// 返回胜利一方的棋子
			}
		}
		return 0;
	}

	/**
	 * 棋盘数据模型的事件处理方法
	 * 
	 * @param evt
	 */
	private void gobangModel1PropertyChange(java.beans.PropertyChangeEvent evt) {
		chessQueue.push(gobangModel1.getChessmanArrayCopy()); // 将新的棋盘布局压入队列
		repaint(); // 重回棋盘界面
	}

	public byte getMyColor() {
		return myColor;
	}

	public void setMyColor(byte myColor) {
		this.myColor = myColor;
	}

	public byte myColor = -2;
	private GobangModel gobangModel1;
}
