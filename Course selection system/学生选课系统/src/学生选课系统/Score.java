package 学生选课系统;

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

/* Score类，由StudentUI创建实例
 * 功能：完成学生课程成绩面板内容 
 * */

public class Score {
	static String currentsno;
	private Vector<Vector<Object>> rowData;
	Vector<Object> columnNames;
	// 定义数据库需要的全局变量
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table4 = null;// 成绩面板

	public Score(String sno) {
		currentsno = sno;
		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// 设置列名
		columnNames.add("课段号");
		columnNames.add("课程名");
		columnNames.add("成绩");
		// rowData可以存放多行,开始从数据库里取

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement(
					"select SecID,CourseName,grade from vi_grade where StudentNum='" + currentsno + "'");

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData可以存放多行
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getDouble(3));

				// 加入到rowData
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
			}// 表格不允许被编辑
		};
		table4.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只允许选中一行
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(JLabel.CENTER);// 这句和上句作用一样
		table4.setDefaultRenderer(Object.class, tcr);
	}

}
