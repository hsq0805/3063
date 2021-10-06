package 学生选课系统;

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

/* Course课程类，由StudentUI创建实例
 * 功能：完成全部课程信息面板内容
 * 包含函数：
 * refreshSelCou1（）--刷新table3，即全部课程信息面板 
 * */

public class Course {
	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	Vector<Object> columnNames;
	// 定义数据库需要的全局变量
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table3 = null;

	public Course() {
		columnNames = new Vector<Object>();
		// 设置列名
		columnNames.add("课程号");
		columnNames.add("课程名");
		columnNames.add("学分");
		columnNames.add("学时");
		columnNames.add("所属专业号码");
		// rowData = new Vector<Object>();
		// rowData可以存放多行,开始从数据库里取

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement("select * from Course");

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData可以存放多行
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				hang.add(rs.getInt(4));
				hang.add(rs.getString(5));
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
		table3 = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = 6843971351064905400L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// 表格不允许被编辑
		};
		table3.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只允许选中一行
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(JLabel.CENTER);// 这句和上句作用一样
		table3.setDefaultRenderer(Object.class, tcr);
	}

	public void refreshCourse() {
		// TODO 自动生成的方法存根
		new Course();
		StudentUI.scrollPane3.setViewportView(Course.table3);
	}

}
