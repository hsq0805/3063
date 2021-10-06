package ѧ��ѡ��ϵͳ;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.sun.javafx.font.Disposer;

import javafx.scene.control.Button;

/*SelectCourseѡ���࣬��StudentUI����ʵ��
 * ���ܣ����ѡ�ι����������
 * ����������
 * addSelCou����--���ѡ�Σ�ͨ��StudentUI��ġ�ѡ��ζΡ���ť��������
 * searchSelCou���� --�鿴��ѡ�ζΣ�ͨ��StudentUI�����ġ��鿴��ѡ����Ϣ����ť��������
 * refreshSelCou1����--ˢ��table5����ѡ�ι�������� 
 * */

public class SelectCourse {
	// �������ݿ���Ҫ��ȫ�ֱ���
	static String currentsno;
	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	Vector<Object> columnNames;
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table5; // ѡ�ι��������
	static JTable table; // �鿴��ѡ�ζ����

	public SelectCourse(String Sno) {
		currentsno = Sno;
		columnNames = new Vector<Object>();
		// ��������
		columnNames.add("�γ̶κ�");
		columnNames.add("�γ���");
		columnNames.add("�γ�ѧ��");
		columnNames.add("��ʦ����");
		columnNames.add("ʣ��ѡ������");
		rowData = new Vector<Vector<Object>>();
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement("call proc_getRemainCourse('" + currentsno + "')");
			// ps=ct.prepareStatement("call proc_getRemainCourse('190502001s')");
			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData���Դ�Ŷ���
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				hang.add(rs.getString(4));
				hang.add(rs.getInt(5));
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
		table5 = new JTable(rowData, columnNames) {

			private static final long serialVersionUID = -8736911822989769111L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};
		table5.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ֻ����ѡ��һ��
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(JLabel.CENTER);// �����Ͼ�����һ��
		table5.setDefaultRenderer(Object.class, tcr);
	}

	/* ���ѡ�Σ�ͨ��StudentUI��ġ�ѡ��ζΡ���ť�������� */
	public void addSelCou(String Sno) {
		// TODO �Զ����ɵķ������
		// StudentUI.unclickable();
		int row = table5.getSelectedRow();// ��ȡѡ�ι���������б�ѡ�е���
		if (row >= 0) {
			String string[] = new String[5];
			string[0] = String.valueOf(table5.getValueAt(row, 0));
			string[1] = String.valueOf(table5.getValueAt(row, 1));
			string[2] = String.valueOf(table5.getValueAt(row, 2));
			string[3] = String.valueOf(table5.getValueAt(row, 3));
			string[4] = String.valueOf(table5.getValueAt(row, 4));
			try {
				ct = new DbUtil().getCon();
				ps = ct.prepareStatement("insert into stucourse values('" + currentsno + "','" + string[0] + "',null)");
				ps.executeUpdate();
				JOptionPane.showMessageDialog(null, "��ӳɹ���");
				refreshSelCou1();
				// row=-1;
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "���ʧ�ܣ�");
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
		} else {
			JOptionPane.showMessageDialog(null, "��ѡ��Ҫ��ӵĿγ̣�");
		}

	}

	/* �鿴��ѡ�γ�ִ�к�����ͨ��StudentUI�����ġ��鿴��ѡ����Ϣ����ť�������� */
	public void searchSelCou(String Sno) {
		String currentSno = Sno;
		JFrame jf = new JFrame("��ѡ�γ�");
		jf.setVisible(true);
		JButton de = new JButton("�˿�");
		JPanel panel = new JPanel();
		jf.setSize(550, 550);
		jf.setLocation(500, 300);
		JScrollPane js = new JScrollPane();
		panel.add(js);
		panel.add(de);
		jf.add(panel);

		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// ��������
		columnNames.add("�γ̶κ�");
		columnNames.add("�γ���");
		columnNames.add("ѧ��");
		columnNames.add("��ʦ����");
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {

			ct = new DbUtil().getCon();
			String sql = "call proc_getStudentCourse('" + currentSno + "')";
			ps = ct.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData���Դ�Ŷ���
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				hang.add(rs.getString(4));
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

		table = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = -8736911822989769111L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ֻ����ѡ��һ��
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(JLabel.CENTER);// �����Ͼ�����һ��
		table.setDefaultRenderer(Object.class, tcr);
		js.setViewportView(table);

		de.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row = table.getSelectedRow();
				if (row >= 0) {
					String string[] = new String[4];
					string[0] = String.valueOf(table.getValueAt(row, 0));
					string[1] = String.valueOf(table.getValueAt(row, 1));
					string[2] = String.valueOf(table.getValueAt(row, 2));
					string[3] = String.valueOf(table.getValueAt(row, 3));
					try {
						ct = new DbUtil().getCon();
						ps = ct.prepareStatement("delete from stucourse where StudentNum='" + currentSno
								+ "' and SecID='" + string[0] + "'");
						ps.executeUpdate();
						JOptionPane.showMessageDialog(null, "ɾ���ɹ���");
						jf.setVisible(false);
						refreshSelCou1();
						// row=-1;
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "ɾ��ʧ�ܣ�");
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
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "��ѡ��Ҫɾ�����У�");
				}
			}
		});
	}

	/* ˢ��table5����ѡ�ι�������� */
	public void refreshSelCou1() {
		// TODO �Զ����ɵķ������
		new SelectCourse(currentsno);
		StudentUI.scrollPane5.setViewportView(SelectCourse.table5);
	}

}
