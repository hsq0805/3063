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

/* Score�࣬��StudentUI����ʵ��
 * ���ܣ����ѧ���γ̳ɼ�������� 
 * */

public class Score {
	static String currentsno;
	private Vector<Vector<Object>> rowData;
	Vector<Object> columnNames;
	// �������ݿ���Ҫ��ȫ�ֱ���
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table4 = null;// �ɼ����

	public Score(String sno) {
		currentsno = sno;
		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// ��������
		columnNames.add("�ζκ�");
		columnNames.add("�γ���");
		columnNames.add("�ɼ�");
		// rowData���Դ�Ŷ���,��ʼ�����ݿ���ȡ

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement(
					"select SecID,CourseName,grade from vi_grade where StudentNum='" + currentsno + "'");

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

}
