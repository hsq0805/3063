package 学生选课系统;

import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.sun.javafx.image.impl.ByteBgr;
import javax.swing.JTextField;
import 学生选课系统.StudentUI;

/*
 * login登录面板，根据登录信息分别调用StudentUI或TeaccherUI
 * 包含denglu（）和chongzhi（）和 compare（）三个函数
 *
 * */

public class Login extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5252772035964963789L;
	private JPanel pan = new JPanel();
	private JLabel t = new JLabel("教务系统登录");
	private JLabel namelab = new JLabel("用户名：");
	private JLabel passlab = new JLabel("密码：");

	// 把按钮加到同一个按钮组
	ButtonGroup bg;
	JRadioButton op1;
	JRadioButton op2;
	{
		bg = new ButtonGroup();
		op1 = new JRadioButton("学生");
		op2 = new JRadioButton("教师");
		bg.add(op1);
		bg.add(op2);
	}
	private JTextField nametext = new JTextField();
	private JPasswordField passtext = new JPasswordField();
	public JButton denglu = new JButton("登录");
	public JButton chongzhi = new JButton("重置");
	ImageIcon image;

	public Login() {
		this.setLocation(1000, 500);
		Font font1 = new Font("黑体", Font.BOLD, 35);
		Font font2 = new Font("黑体", Font.BOLD, 20);
		super.setTitle("欢迎登录教务系统");
		pan.setLayout(null);

		t.setBounds(150, 20, 300, 60);
		namelab.setBounds(100, 100, 100, 50);
		nametext.setBounds(200, 100, 200, 50);
		passlab.setBounds(100, 160, 100, 50);
		passtext.setBounds(200, 160, 200, 50);
		op1.setBounds(130, 220, 100, 50);
		op2.setBounds(300, 220, 100, 50);
		denglu.setBounds(130, 280, 90, 50);
		chongzhi.setBounds(300, 280, 90, 50);

		pan.add(t);
		pan.add(namelab);
		pan.add(nametext);
		pan.add(passlab);
		pan.add(passtext);
		pan.add(op1);
		pan.add(op2);
		pan.add(denglu);
		pan.add(chongzhi);
		t.setFont(font1);
		namelab.setFont(font2);
		passlab.setFont(font2);
		op1.setFont(font2);
		op2.setFont(font2);
		denglu.setFont(font2);
		chongzhi.setFont(font2);
		denglu.addActionListener(this);
		chongzhi.addActionListener(this);
		super.add(pan);
		super.setSize(600, 400);
		super.setVisible(true);
		passtext.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e2) {
				if (e2.getKeyChar() == KeyEvent.VK_ENTER) {// 如果密码是enter键
					denglu.doClick();// 点击登录按钮
				}
			}
		});
	}

	public static void main(String[] args) {

		new Login();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == denglu) {
			try {
				denglu();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (arg0.getSource() == chongzhi) {
			chongzhi();
		}
	}

	// 登录按钮的事件处理函数     && username.contains("s") && username.contains("Tea")
	public void denglu() throws HeadlessException, Exception {
		String username = nametext.getText();
		char[] password = passtext.getPassword();
		if (op1.isSelected()) {
			if (compare(username, password)) {
				JOptionPane.showMessageDialog(null, "登陆成功！");
				//dispose();
				chongzhi();
				StudentUI.getStudentUI(username);
			}
		} else if (op2.isSelected()) {
			if (compare(username, password)) {
				JOptionPane.showMessageDialog(null, "登陆成功！");
				//dispose();
				chongzhi();
				TeacherUI.getTeacherUI(username);
			}
		}else {
				JOptionPane.showMessageDialog(null, "输入有误！");
			}
	}

	// 重置按钮触发后的事件处理函数
	public void chongzhi() {
		nametext.setText("");
		passtext.setText("");
		op1.setSelected(false);
		op2.setSelected(false);
	}

	// 对比用户名和密码是不匹配
	public boolean compare(String username, char[] password) throws Exception {
		String pwd = String.valueOf(password);
		boolean m = false;
		Connection con = new DbUtil().getCon();
		Statement statement = con.createStatement();

		String sql = "select userpass from userpass where username='" + username + "'";
		try {

			ResultSet res = statement.executeQuery(sql);
			if (res.next()) {
				String pa = res.getString(1);
				System.out.println(pa + " " + pwd);
				if (pa.equals(pwd)) {
					m = true;
				} else {
					JOptionPane.showMessageDialog(null, "密码错误！");
				}
			} else {
				JOptionPane.showMessageDialog(null, "用户名不存在！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}
}
