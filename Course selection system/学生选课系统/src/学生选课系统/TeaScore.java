package ѧ��ѡ��ϵͳ;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.Printable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/* TeaScore�࣬��TeacherUI����ʵ��
 * ���ܣ�������̿γ��������
 * ����������
 * addSorce����  --��ӳɼ���Ϣ
 * searchScore���� --�鿴ĳ��ѧ���ɼ�
 * refreshScore����--ˢ��table4�������̿γ������ 
 * */

public class TeaScore {
	static String currentTno;
	private Vector<Vector<Object>> rowData;
	// Vector<Object> rowData;
	Vector<Object> columnNames;
	// �������ݿ���Ҫ��ȫ�ֱ���
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table4 = null;

	public TeaScore(String tno) {
		currentTno = tno;
		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// ��������
		columnNames.add("�ζκ�");
		columnNames.add("�γ���");
		columnNames.add("�ɼ�");

		// rowData = new Vector<Object>();
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement(
					"select SecID,CourseName,grade from vi_grade where StudentNum='" 
			        + currentTno + "'");

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData���Դ�Ŷ���
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getDouble(3));

				// ���뵽rowData
				rowData.add(hang);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null) {
					rs.close();
				}
				if (ps != null) {
					ps.close();
				}
				if (ct != null) {
					ct.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		table4 = new JTable(rowData, columnNames) {

			private static final long serialVersionUID = 1525421110274312344L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};
		table4.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ֻ����ѡ��һ��
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(JLabel.CENTER);// �����Ͼ�����һ��
		table4.setDefaultRenderer(Object.class, tcr);
	}

	public void addScore() {
		// TODO �Զ����ɵķ������
		// TeacherUI.unclickable();
		JFrame Addsc = new JFrame("��ӳɼ���Ϣ");
		Addsc.setSize(320, 200);
		Addsc.setLocation(600, 300);
		JPanel addsc = new JPanel();
		JLabel scsno = new JLabel("ѧ��");
		JLabel sccno = new JLabel("�γ̺�");
		JLabel score = new JLabel("�ɼ�");
		JTextField scsnotext = new JTextField();
		JTextField sccnotext = new JTextField();
		JTextField scoretext = new JTextField();

		JButton ok = new JButton("ȷ��");
		JButton reset = new JButton("����");

		addsc.setLayout(null);
		scsno.setBounds(5, 5, 70, 20);
		scsnotext.setBounds(80, 5, 120, 20);
		sccno.setBounds(5, 30, 70, 20);
		sccnotext.setBounds(80, 30, 120, 20);
		score.setBounds(5, 60, 70, 20);
		scoretext.setBounds(80, 60, 120, 20);
		ok.setBounds(50, 100, 60, 20);
		reset.setBounds(130, 100, 60, 20);

		addsc.add(scsno);
		addsc.add(scsnotext);
		addsc.add(sccno);
		addsc.add(sccnotext);
		addsc.add(score);
		addsc.add(scoretext);
		addsc.add(ok);
		addsc.add(reset);
		Addsc.add(addsc);
		Addsc.setVisible(true);

		Addsc.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TeacherUI.clickable();
			}
		});

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String sno = scsnotext.getText();
					String secno = sccnotext.getText();
					double score = Integer.valueOf(scoretext.getText()).intValue();
					int count = 0;
					ct = new DbUtil().getCon();
					ps = ct.prepareStatement("select StudentNum\r\n" + "from stucourse\r\n" 
					        + "where SecID = '" + secno
							+ "' and StudentNum ='" + sno + "'");
					rs = ps.executeQuery();
					while (rs.next()) {
						count++;
					}
					if (count == 0) {
						JOptionPane.showMessageDialog(null, "����û��ѡ�˿γ̣�");
					}
					if (count != 0) {
						ps = ct.prepareStatement("call insert_grade('" + secno + "','" 
					                               + sno + "'," + score + ")");
						ps.executeUpdate();
						JOptionPane.showMessageDialog(null, "��ӳɹ���");
						Addsc.dispose();
						TeacherUI.clickable();
						refreshScore();
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "���ʧ�ܣ�");
					TeacherUI.unclickable();
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (ps != null) {
							ps.close();
						}
						if (ct != null) {
							ct.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}

		});
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				scsnotext.setText("");
				sccnotext.setText("");
				scoretext.setText("");
			}
		});
	}

	public void searchScore() {
		// TODO �Զ����ɵķ������
		TeacherUI.unclickable();
		JFrame SeaSC = new JFrame("���ҳɼ���Ϣ");
		SeaSC.setSize(250, 150);
		SeaSC.setLocation(600, 300);
		JPanel seasc = new JPanel();
		JLabel cno = new JLabel("������ζκ�");
		JTextField cnotext = new JTextField();
		JLabel sno = new JLabel("������ѧ��");
		JTextField snotext = new JTextField();
		JButton ok = new JButton("ȷ��");
		JButton reset = new JButton("����");

		seasc.setLayout(null);

		cno.setBounds(5, 5, 80, 20);
		cnotext.setBounds(90, 5, 120, 20);
		sno.setBounds(5, 30, 80, 20);
		snotext.setBounds(90, 30, 120, 20);
		ok.setBounds(50, 70, 60, 20);
		reset.setBounds(130, 70, 60, 20);

		seasc.add(cno);
		seasc.add(cnotext);
		seasc.add(sno);
		seasc.add(snotext);
		seasc.add(ok);
		seasc.add(reset);

		SeaSC.add(seasc);
		SeaSC.setVisible(true);

		SeaSC.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				TeacherUI.clickable();
			}
		});

		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				columnNames = new Vector<Object>();
				rowData = new Vector<Vector<Object>>();
				// ��������
				columnNames.add("ѧ��");
				columnNames.add("����");
				columnNames.add("�ζκ�");
				columnNames.add("�γ���");
				columnNames.add("�ɼ�");
				String str = cnotext.getText();
				String str1 = snotext.getText();
				// rowData = new Vector<Object>();
				int count = 0;
				// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ
				try {
					ct = new DbUtil().getCon();
					ps = ct.prepareStatement("select StudentNum\r\n" 
					        + "from stucourse\r\n" 
							+ "where SecID = '" + str
							+ "' and StudentNum ='" + str1 + "'");
					rs = ps.executeQuery();
					System.out.println(rs);
					while (rs.next()) {
						count++;
					}
					if (count == 0) {
						JOptionPane.showMessageDialog(null, "����û��ѡ�˿γ̣�");
					}
					if (count != 0) {
						ps = ct.prepareStatement("select StudentNum,StudentName,SecID,CourseName,grade\r\n"
								+ "from vi_grade\r\n" 
								+ "where StudentNum ='" + str1 
								+ "' and SecID='" + str + "';");
						rs = ps.executeQuery();
						JOptionPane.showMessageDialog(null, "��ѯ�ɹ���");
						while (rs.next()) {
							// rowData���Դ�Ŷ���
							Vector<Object> hang = new Vector<Object>();
							hang.add(rs.getString(1));
							hang.add(rs.getString(2));
							hang.add(rs.getString(3));
							hang.add(rs.getString(4));
							hang.add(rs.getDouble(5));
							// ���뵽rowData
							rowData.add(hang);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "��ѯʧ�ܣ�");
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
						if (ps != null) {
							ps.close();
						}
						if (ct != null) {
							ct.close();
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				table4 = new JTable(rowData, columnNames) {
					private static final long serialVersionUID = -8736911822989769111L;

					public boolean isCellEditable(int row, int column) {
						return false;
					}// ��������༭
				};

				table4.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ֻ����ѡ��һ��
				DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
				tcr.setHorizontalAlignment(JLabel.CENTER);// �����Ͼ�����һ��
				table4.setDefaultRenderer(Object.class, tcr);
				TeacherUI.scrollPane4.setViewportView(table4);
				SeaSC.dispose();
				TeacherUI.clickable();
			}
		});

		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				cnotext.setText("");
				snotext.setText("");
			}

		});
	}

	public void refreshScore() {
		// TODO �Զ����ɵķ������
		new TeaScore(currentTno);
		TeacherUI.scrollPane4.setViewportView(TeaScore.table4);
	}

}
