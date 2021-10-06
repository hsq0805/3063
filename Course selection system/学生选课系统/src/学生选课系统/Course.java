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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

import com.mysql.cj.util.DnsSrv;

/* Course�γ��࣬��StudentUI����ʵ��
 * ���ܣ����ȫ���γ���Ϣ�������
 * ����������
 * refreshSelCou1����--ˢ��table3����ȫ���γ���Ϣ��� 
 * */

public class Course {
	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	Vector<Object> columnNames;
	// �������ݿ���Ҫ��ȫ�ֱ���
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table3 = null;

	public Course() {
		columnNames = new Vector<Object>();
		// ��������
		columnNames.add("�γ̺�");
		columnNames.add("�γ���");
		columnNames.add("ѧ��");
		columnNames.add("ѧʱ");
		columnNames.add("����רҵ����");
		// rowData = new Vector<Object>();
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement("select * from Course");

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData���Դ�Ŷ���
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				hang.add(rs.getInt(4));
				hang.add(rs.getString(5));
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

	public void refreshCourse() {
		// TODO �Զ����ɵķ������
		new Course();
		StudentUI.scrollPane3.setViewportView(Course.table3);
	}

}
