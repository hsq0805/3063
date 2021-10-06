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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;

/* TeaCourse选课类，由TeacherUI创建实例
 * 功能：完成所教课程面板内容
 * 包含函数：
 * searchCourse（） --查看选课学生成绩，通过TeacherUI框架里的“查看该课程学生成绩”按钮动作调用
 * refreshSelCou1（）--刷新table3，即所教课程主面板
 * */

public class TeaCourse {
	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	String currentTno;
	Vector<Object> columnNames;
	// 定义数据库需要的全局变量
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table3 = null;
	JTable table5; // 学生成绩表格

	public TeaCourse(String tno) {
		currentTno = tno;
		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// 设置列名
		columnNames.add("课程号");
		columnNames.add("课程名");
		// rowData = new Vector<Object>();
		// rowData可以存放多行,开始从数据库里取

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement("select section.SecID,course.CourseName\r\n"
			        +"from section,teacher,course\r\n"
					+ "where section.TeacherNum = '" + tno
					+ "' and section.CourseNum = course.CourseNum"
					+" and teacher.TeacherNum = section.TeacherNum ;");

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData可以存放多行
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
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

	public void searchCourse(String tno) {
		int row = table3.getSelectedRow();
		String string[] = new String[2];
		string[0] = String.valueOf(table3.getValueAt(row, 0));
		string[1] = String.valueOf(table3.getValueAt(row, 1));
		String currentSno = tno;
		JFrame jf = new JFrame("选择该课程的学生");
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
		// 设置列名
		columnNames.add("序号");
		columnNames.add("学生学号");
		columnNames.add("学生姓名");
		columnNames.add("成绩");
		// rowData可以存放多行,开始从数据库里取

		try {
			ct =new DbUtil().getCon();
			String sql = "select student.StudentNum,student.StudentName,grade\r\n"
			        + "from stucourse,student\r\n"
					+ "where stucourse.SecID = '" + string[0]
					+ "' and stucourse.StudentNum = student.StudentNum;";
			ps = ct.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData可以存放多行
				Vector<Object> hang = new Vector<Object>();
				hang.add(rank);
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getDouble(3));
				rank++;
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

		table5 = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = -8736911822989769111L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// 表格不允许被编辑
		};
		table5.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只允许选中一行
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(JLabel.CENTER);// 这句和上句作用一样
		table5.setDefaultRenderer(Object.class, tcr);
		js.setViewportView(table5);
	}

	public void refreshCourse() {
		// TODO 自动生成的方法存根
		new TeaCourse(currentTno);
		TeacherUI.scrollPane3.setViewportView(TeaCourse.table3);
	}

}
