package 学生选课系统;

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

/* TeaScore类，由TeacherUI创建实例
 * 功能：完成所教课程面板内容
 * 包含函数：
 * addSorce（）  --添加成绩信息
 * searchScore（） --查看某个学生成绩
 * refreshScore（）--刷新table4，即所教课程主面板 
 * */

public class TeaScore {
	static String currentTno;
	private Vector<Vector<Object>> rowData;
	// Vector<Object> rowData;
	Vector<Object> columnNames;
	// 定义数据库需要的全局变量
	PreparedStatement ps = null;
	Connection ct = null;
	ResultSet rs = null;
	static JTable table4 = null;

	public TeaScore(String tno) {
		currentTno = tno;
		columnNames = new Vector<Object>();
		rowData = new Vector<Vector<Object>>();
		// 设置列名
		columnNames.add("课段号");
		columnNames.add("课程名");
		columnNames.add("成绩");

		// rowData = new Vector<Object>();
		// rowData可以存放多行,开始从数据库里取

		try {
			ct = new DbUtil().getCon();
			ps = ct.prepareStatement(
					"select SecID,CourseName,grade from vi_grade where StudentNum='" 
			        + currentTno + "'");

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

	public void addScore() {
		// TODO 自动生成的方法存根
		// TeacherUI.unclickable();
		JFrame Addsc = new JFrame("添加成绩信息");
		Addsc.setSize(320, 200);
		Addsc.setLocation(600, 300);
		JPanel addsc = new JPanel();
		JLabel scsno = new JLabel("学号");
		JLabel sccno = new JLabel("课程号");
		JLabel score = new JLabel("成绩");
		JTextField scsnotext = new JTextField();
		JTextField sccnotext = new JTextField();
		JTextField scoretext = new JTextField();

		JButton ok = new JButton("确定");
		JButton reset = new JButton("重置");

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
						JOptionPane.showMessageDialog(null, "此人没有选此课程！");
					}
					if (count != 0) {
						ps = ct.prepareStatement("call insert_grade('" + secno + "','" 
					                               + sno + "'," + score + ")");
						ps.executeUpdate();
						JOptionPane.showMessageDialog(null, "添加成功！");
						Addsc.dispose();
						TeacherUI.clickable();
						refreshScore();
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "添加失败！");
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
		// TODO 自动生成的方法存根
		TeacherUI.unclickable();
		JFrame SeaSC = new JFrame("查找成绩信息");
		SeaSC.setSize(250, 150);
		SeaSC.setLocation(600, 300);
		JPanel seasc = new JPanel();
		JLabel cno = new JLabel("请输入课段号");
		JTextField cnotext = new JTextField();
		JLabel sno = new JLabel("请输入学号");
		JTextField snotext = new JTextField();
		JButton ok = new JButton("确定");
		JButton reset = new JButton("重置");

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
				// 设置列名
				columnNames.add("学号");
				columnNames.add("姓名");
				columnNames.add("课段号");
				columnNames.add("课程名");
				columnNames.add("成绩");
				String str = cnotext.getText();
				String str1 = snotext.getText();
				// rowData = new Vector<Object>();
				int count = 0;
				// rowData可以存放多行,开始从数据库里取
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
						JOptionPane.showMessageDialog(null, "此人没有选此课程！");
					}
					if (count != 0) {
						ps = ct.prepareStatement("select StudentNum,StudentName,SecID,CourseName,grade\r\n"
								+ "from vi_grade\r\n" 
								+ "where StudentNum ='" + str1 
								+ "' and SecID='" + str + "';");
						rs = ps.executeQuery();
						JOptionPane.showMessageDialog(null, "查询成功！");
						while (rs.next()) {
							// rowData可以存放多行
							Vector<Object> hang = new Vector<Object>();
							hang.add(rs.getString(1));
							hang.add(rs.getString(2));
							hang.add(rs.getString(3));
							hang.add(rs.getString(4));
							hang.add(rs.getDouble(5));
							// 加入到rowData
							rowData.add(hang);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "查询失败！");
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
					}// 表格不允许被编辑
				};

				table4.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);// 只允许选中一行
				DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
				tcr.setHorizontalAlignment(JLabel.CENTER);// 这句和上句作用一样
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
		// TODO 自动生成的方法存根
		new TeaScore(currentTno);
		TeacherUI.scrollPane4.setViewportView(TeaScore.table4);
	}

}
