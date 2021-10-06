package ѧ��ѡ��ϵͳ;

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
import ѧ��ѡ��ϵͳ.StudentUI;

/*
 * login��¼��壬���ݵ�¼��Ϣ�ֱ����StudentUI��TeaccherUI
 * ����denglu������chongzhi������ compare������������
 *
 * */

public class Login extends JFrame implements ActionListener {
	private static final long serialVersionUID = 5252772035964963789L;
	private JPanel pan = new JPanel();
	private JLabel t = new JLabel("����ϵͳ��¼");
	private JLabel namelab = new JLabel("�û�����");
	private JLabel passlab = new JLabel("���룺");

	// �Ѱ�ť�ӵ�ͬһ����ť��
	ButtonGroup bg;
	JRadioButton op1;
	JRadioButton op2;
	{
		bg = new ButtonGroup();
		op1 = new JRadioButton("ѧ��");
		op2 = new JRadioButton("��ʦ");
		bg.add(op1);
		bg.add(op2);
	}
	private JTextField nametext = new JTextField();
	private JPasswordField passtext = new JPasswordField();
	public JButton denglu = new JButton("��¼");
	public JButton chongzhi = new JButton("����");
	ImageIcon image;

	public Login() {
		this.setLocation(1000, 500);
		Font font1 = new Font("����", Font.BOLD, 35);
		Font font2 = new Font("����", Font.BOLD, 20);
		super.setTitle("��ӭ��¼����ϵͳ");
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
				if (e2.getKeyChar() == KeyEvent.VK_ENTER) {// ���������enter��
					denglu.doClick();// �����¼��ť
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

	// ��¼��ť���¼�������     && username.contains("s") && username.contains("Tea")
	public void denglu() throws HeadlessException, Exception {
		String username = nametext.getText();
		char[] password = passtext.getPassword();
		if (op1.isSelected()) {
			if (compare(username, password)) {
				JOptionPane.showMessageDialog(null, "��½�ɹ���");
				//dispose();
				chongzhi();
				StudentUI.getStudentUI(username);
			}
		} else if (op2.isSelected()) {
			if (compare(username, password)) {
				JOptionPane.showMessageDialog(null, "��½�ɹ���");
				//dispose();
				chongzhi();
				TeacherUI.getTeacherUI(username);
			}
		}else {
				JOptionPane.showMessageDialog(null, "��������");
			}
	}

	// ���ð�ť��������¼�������
	public void chongzhi() {
		nametext.setText("");
		passtext.setText("");
		op1.setSelected(false);
		op2.setSelected(false);
	}

	// �Ա��û����������ǲ�ƥ��
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
					JOptionPane.showMessageDialog(null, "�������");
				}
			} else {
				JOptionPane.showMessageDialog(null, "�û��������ڣ�");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return m;
	}
}
