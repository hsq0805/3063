package ѧ��ѡ��ϵͳ;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.Toolkit;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

/* TeacherUI��ʦ��¼����
 * ������������壺���̿γ���Ϣ���ɼ�����
 * CourseInfo��ScoreInfo
 * */

public class TeacherUI extends JFrame {

	private static final long serialVersionUID = 4197017698513729527L;
	String currentTno;
	TeaCourse c;
	TeaScore sc;

	static JTabbedPane tabbedPane;
	static JToolBar toolBar_3;
	static JToolBar toolBar_4;
	static JButton SearchCourse;
	static JButton RefreshCourse;
	static JButton AddScore;
	static JButton SearchScore;
	static JButton RefreshScore;

	static JScrollPane scrollPane3;
	static JScrollPane scrollPane4;

	JPanel CourseInfo = new JPanel();// ���̿γ���Ϣ��壬������ť���鿴�ÿγ�ѧ���ɼ���ˢ��
	JPanel ScoreInfo = new JPanel(); // �ɼ�������壬������ť���޸ĳɼ����鿴�ɼ���ˢ��

	public TeacherUI(String tno) {
		currentTno = tno;
		c = new TeaCourse(currentTno);
		sc = new TeaScore(currentTno);
		setIconImage(Toolkit.getDefaultToolkit().getImage(".jpg"));
		this.setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(565, 500);
		this.setLocation(550, 0);
		this.setVisible(true);
		this.setTitle("��ӭ��������ϵͳ");
		getContentPane().setLayout(new BorderLayout(0, 0));

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.add(CourseInfo, "���̵Ŀγ���Ϣ");
		CourseInfo.setLayout(new BorderLayout(0, 0));
		toolBar_3 = new JToolBar();
		CourseInfo.add(toolBar_3, BorderLayout.NORTH);

		SearchCourse = new JButton("�鿴�ÿγ�ѧ���ɼ�");
		SearchCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.searchCourse(tno);
			}
		});
		toolBar_3.add(SearchCourse);

		RefreshCourse = new JButton("ˢ��");
		RefreshCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				c.refreshCourse();
			}
		});
		toolBar_3.add(RefreshCourse);
		scrollPane3 = new JScrollPane();
		CourseInfo.add(scrollPane3, BorderLayout.CENTER);
		scrollPane3.setViewportView(TeaCourse.table3);

		tabbedPane.add(ScoreInfo, "�ɼ�����");
		ScoreInfo.setLayout(new BorderLayout(0, 0));
		toolBar_4 = new JToolBar();
		ScoreInfo.add(toolBar_4, BorderLayout.NORTH);

		AddScore = new JButton("���/�޸ĳɼ���Ϣ");
		AddScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sc.addScore();
			}
		});
		toolBar_4.add(AddScore);

		SearchScore = new JButton("�����ɼ���Ϣ");
		SearchScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sc.searchScore();
			};
		});
		toolBar_4.add(SearchScore);

		RefreshScore = new JButton("ˢ��");
		RefreshScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sc.refreshScore();
			}
		});
		toolBar_4.add(RefreshScore);

		scrollPane4 = new JScrollPane();
		ScoreInfo.add(scrollPane4, BorderLayout.CENTER);

		scrollPane4.setViewportView(Score.table4);

	}

	public static void clickable() {
		tabbedPane.setEnabled(true);
		AddScore.setEnabled(true);
		RefreshScore.setEnabled(true);
		SearchScore.setEnabled(true);
	}

	public static void unclickable() {
		tabbedPane.setEnabled(false);
		AddScore.setEnabled(false);
		RefreshScore.setEnabled(false);
		SearchScore.setEnabled(false);
	}

	static public void getTeacherUI(String tno) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TeacherUI frame = new TeacherUI(tno);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
