package ѧ��ѡ��ϵͳ;

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

/* TeaCourseѡ���࣬��TeacherUI����ʵ��
 * ���ܣ�������̿γ��������
 * ����������
 * searchCourse���� --�鿴ѡ��ѧ���ɼ���ͨ��TeacherUI�����ġ��鿴�ÿγ�ѧ���ɼ�����ť��������
 * refreshSelCou1����--ˢ��table3�������̿γ������
 * */

public class TeaCourse {
	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	String currentTno;
	Vector<Object> columnNames;
	// �������ݿ���Ҫ��ȫ�ֱ���
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table3 = null;
	JTable table5; // ѧ���ɼ����

	public TeaCourse(String tno) {
		currentTno = tno;
		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// ��������
		columnNames.add("�γ̺�");
		columnNames.add("�γ���");
		// rowData = new Vector<Object>();
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement("select section.SecID,course.CourseName\r\n"
			        +"from section,teacher,course\r\n"
					+ "where section.TeacherNum = '" + tno
					+ "' and section.CourseNum = course.CourseNum"
					+" and teacher.TeacherNum = section.TeacherNum ;");

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData���Դ�Ŷ���
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
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
		table3 = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = 6843971351064905400L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// ��������༭
		};
		table3.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// ֻ����ѡ��һ��
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// ����table���ݾ���
		tcr.setHorizontalAlignment(JLabel.CENTER);// �����Ͼ�����һ��
		table3.setDefaultRenderer(Object.class, tcr);
	}

	public void searchCourse(String tno) {
		int row = table3.getSelectedRow();
		String string[] = new String[2];
		string[0] = String.valueOf(table3.getValueAt(row, 0));
		string[1] = String.valueOf(table3.getValueAt(row, 1));
		String currentSno = tno;
		JFrame jf = new JFrame("ѡ��ÿγ̵�ѧ��");
		jf.setVisible(true);
		JPanel panel = new JPanel();
		jf.setSize(550, 550);
		jf.setLocation(500, 300);
		JScrollPane js = new JScrollPane();
		panel.add(js);
		jf.add(panel);

		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		int rank = 1;
		// ��������
		columnNames.add("���");
		columnNames.add("ѧ��ѧ��");
		columnNames.add("ѧ������");
		columnNames.add("�ɼ�");
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {
			ct =new DbUtil().getCon();
			String sql = "select student.StudentNum,student.StudentName,grade\r\n"
			        + "from stucourse,student\r\n"
					+ "where stucourse.SecID = '" + string[0]
					+ "' and stucourse.StudentNum = student.StudentNum;";
			ps = ct.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData���Դ�Ŷ���
				Vector<Object> hang = new Vector<Object>();
				hang.add(rank);
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getDouble(3));
				rank++;
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
		js.setViewportView(table5);
	}

	public void refreshCourse() {
		// TODO �Զ����ɵķ������
		new TeaCourse(currentTno);
		TeacherUI.scrollPane3.setViewportView(TeaCourse.table3);
	}

}
