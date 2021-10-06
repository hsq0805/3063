package ѧ��ѡ��ϵͳ;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import java.awt.Toolkit;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;

/* StudentUIѧ����¼����
 * ������������壺ȫ���γ���Ϣ��ѡ�ι����γ̳ɼ�
 * CourseInfo��SelectCourseInfo��ScoreInfo
 * */

public class StudentUI extends JFrame{
		
	private static final long serialVersionUID = 4197017698513729527L;
	Course c = new Course();
	static String currentsno;
	Score sc;
	SelectCourse se; 
	static JTabbedPane tabbedPane;
	static JToolBar toolBar_3; // �γ̹�������ˢ��
	static JToolBar toolBar_5; // ѡ�ι�������ѡ��ζΣ��鿴ѡ�Σ�ˢ��
	static JButton RefreshCourse;
	static JButton AddSelCou;
	static JButton SearchSelCou;
	static JButton RefreshSelCou;

	static JScrollPane scrollPane3; //ȫ���γ������
	static JScrollPane scrollPane4; //ѡ�ι��������
	static JScrollPane scrollPane5; //�γ̳ɼ������
	
	JPanel CourseInfo = new JPanel();  //ȫ���γ����
    JPanel ScoreInfo = new JPanel();   // �γ̳ɼ����
    JPanel SelectCourseInfo = new JPanel(); //ѡ�ι������
    
	
	public StudentUI() {
		// TODO Auto-generated constructor stub
	}
	
	public StudentUI(String Sno) 
    {
		currentsno =Sno;
		se = new SelectCourse(currentsno);
		sc = new Score(currentsno);
		
        setIconImage(Toolkit.getDefaultToolkit().getImage(".jpg"));
        this.setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(565, 500);
        this.setLocation(550, 0);
        this.setVisible(true);
        this.setTitle("��ӭ��������ϵͳ");
        getContentPane().setLayout(new BorderLayout(0, 0));
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        
        
        tabbedPane.add(CourseInfo,"ȫ���γ���Ϣ");
        CourseInfo.setLayout(new BorderLayout(0, 0));
        toolBar_3 = new JToolBar();
        CourseInfo.add(toolBar_3, BorderLayout.NORTH);
       
        RefreshCourse = new JButton("ˢ��");
        RefreshCourse.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		c.refreshCourse();
        	}
        });
        toolBar_3.add(RefreshCourse);
        scrollPane3 = new JScrollPane();
        CourseInfo.add(scrollPane3, BorderLayout.CENTER);
        scrollPane3.setViewportView(Course.table3);
        
        
        
        
        
        tabbedPane.add(SelectCourseInfo,"ѡ�ι���");
        SelectCourseInfo.setLayout(new BorderLayout(0, 0));
        toolBar_5 = new JToolBar();
        SelectCourseInfo.add(toolBar_5, BorderLayout.NORTH);
        
        AddSelCou = new JButton("ѡ��ζ�");
        AddSelCou.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		se.addSelCou(Sno);
        	}
        });
        toolBar_5.add(AddSelCou);
        
        SearchSelCou = new JButton("�鿴��ѡ����Ϣ");
        SearchSelCou.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		se.searchSelCou(Sno);
        	}
        });
        toolBar_5.add(SearchSelCou);
        
        RefreshSelCou = new JButton("ˢ��");
        RefreshSelCou.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		se.refreshSelCou1();
        	}
        });
        toolBar_5.add(RefreshSelCou);
        
        scrollPane5 = new JScrollPane();
        SelectCourseInfo.add(scrollPane5, BorderLayout.CENTER);
        scrollPane5.setViewportView(SelectCourse.table5);
        
             
        
        
        
        tabbedPane.add(ScoreInfo,"�γ̳ɼ�");
        ScoreInfo.setLayout(new BorderLayout(0, 0));
        //toolBar_4 = new JToolBar();
        //ScoreInfo.add(toolBar_4, BorderLayout.NORTH);
        scrollPane4 = new JScrollPane();
        ScoreInfo.add(scrollPane4, BorderLayout.CENTER);  
        scrollPane4.setViewportView(Score.table4);
        ScoreInfo.setVisible(true);
                                  
    }
	
	static public void getStudentUI(String sno) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					StudentUI frame = new StudentUI(sno);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
    
    public static void clickable() {
    	tabbedPane.setEnabled(true);
		RefreshCourse.setEnabled(true);
		AddSelCou.setEnabled(true);
		SearchSelCou.setEnabled(true);
		RefreshSelCou.setEnabled(true);

    }
    
  public static void unclickable() {
    	tabbedPane.setEnabled(false);
		RefreshCourse.setEnabled(false);
		AddSelCou.setEnabled(false);
		SearchSelCou.setEnabled(false);
		RefreshSelCou.setEnabled(false);
    }
}

