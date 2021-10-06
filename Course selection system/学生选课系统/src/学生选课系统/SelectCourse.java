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

/*SelectCourse选课类，由StudentUI创建实例
 * 功能：完成选课管理面板内容
 * 包含函数：
 * addSelCou（）--添加选课，通过StudentUI里的“选择课段”按钮动作调用
 * searchSelCou（） --查看已选课段，通过StudentUI框架里的“查看已选课信息”按钮动作调用
 * refreshSelCou1（）--刷新table5，即选课管理主面板 
 * */

public class SelectCourse {
	// 定义数据库需要的全局变量
	static String currentsno;
	private Vector<Vector<Object>> rowData = new Vector<Vector<Object>>();
	Vector<Object> columnNames;
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table5; // 选课管理主面板
	static JTable table; // 查看已选课段面板

	public SelectCourse(String Sno) {
		currentsno = Sno;
		columnNames = new Vector<Object>();
		// 设置列名
		columnNames.add("课程段号");
		columnNames.add("课程名");
		columnNames.add("课程学分");
		columnNames.add("教师姓名");
		columnNames.add("剩余选课人数");
		rowData = new Vector<Vector<Object>>();
		// rowData可以存放多行,开始从数据库里取

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement("call proc_getRemainCourse('" + currentsno + "')");
			// ps=ct.prepareStatement("call proc_getRemainCourse('190502001s')");
			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData可以存放多行
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				hang.add(rs.getString(4));
				hang.add(rs.getInt(5));
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
	}

	/* 添加选课，通过StudentUI里的“选择课段”按钮动作调用 */
	public void addSelCou(String Sno) {
		// TODO 自动生成的方法存根
		// StudentUI.unclickable();
		int row = table5.getSelectedRow();// 获取选课管理主面板中被选中的行
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
				JOptionPane.showMessageDialog(null, "添加成功！");
				refreshSelCou1();
				// row=-1;
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "添加失败！");
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
			JOptionPane.showMessageDialog(null, "请选中要添加的课程！");
		}

	}

	/* 查看已选课程执行函数，通过StudentUI框架里的“查看已选课信息”按钮动作调用 */
	public void searchSelCou(String Sno) {
		String currentSno = Sno;
		JFrame jf = new JFrame("已选课程");
		jf.setVisible(true);
		JButton de = new JButton("退课");
		JPanel panel = new JPanel();
		jf.setSize(550, 550);
		jf.setLocation(500, 300);
		JScrollPane js = new JScrollPane();
		panel.add(js);
		panel.add(de);
		jf.add(panel);

		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// 设置列名
		columnNames.add("课程段号");
		columnNames.add("课程名");
		columnNames.add("学分");
		columnNames.add("教师姓名");
		// rowData可以存放多行,开始从数据库里取

		try {

			ct = new DbUtil().getCon();
			String sql = "call proc_getStudentCourse('" + currentSno + "')";
			ps = ct.prepareStatement(sql);

			rs = ps.executeQuery();

			while (rs.next()) {
				// rowData可以存放多行
				Vector<Object> hang = new Vector<Object>();
				hang.add(rs.getString(1));
				hang.add(rs.getString(2));
				hang.add(rs.getInt(3));
				hang.add(rs.getString(4));
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

		table = new JTable(rowData, columnNames) {
			private static final long serialVersionUID = -8736911822989769111L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}// 表格不允许被编辑
		};
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只允许选中一行
		DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
		tcr.setHorizontalAlignment(JLabel.CENTER);// 这句和上句作用一样
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
						JOptionPane.showMessageDialog(null, "删除成功！");
						jf.setVisible(false);
						refreshSelCou1();
						// row=-1;
					} catch (Exception e1) {
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "删除失败！");
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
					JOptionPane.showMessageDialog(null, "请选中要删除的行！");
				}
			}
		});
	}

	/* 刷新table5，即选课管理主面板 */
	public void refreshSelCou1() {
		// TODO 自动生成的方法存根
		new SelectCourse(currentsno);
		StudentUI.scrollPane5.setViewportView(SelectCourse.table5);
	}

}
