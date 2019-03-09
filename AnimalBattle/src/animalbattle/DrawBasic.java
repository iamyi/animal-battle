package animalbattle;
import java.awt.*;
import java.util.*;
import java.awt.event.*;


import javax.swing.*;

public class DrawBasic extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4924681229136467338L;
	static final String team[] = {"��","��"};
	static final String trap[] = {"������","������"};
	static final String type[] = {"��","ʨ","��","��","��","��","è","��"};
	static final String mode[] = {"�˻���ս","���˶�ս"};
	private int difficulty;
	private int switchListener;      //����������ģʽ����ͬswitchlistenerֵ��Ӧ��ͬ�ļ���������
	private HashSet<chesspiece> deathChess;   //�����������Ӽ���
	private HashSet<chesspiece> redliveChess;    //��Ȼ�����ĺ�ɫ�����Ӽ���
	private HashSet<chesspiece> blueliveChess;   //��Ȼ��������ɫ�����Ӽ���
	private chesspiece curChess;              //��ǰѡ�е�����
	private chesspiece AIchess;
	private Point AIpoint;
	public int cnt;                   //��¼��ǰ����Ķ���
	private String curmode;       //��ǰ��Ϸģʽ
	JMenuBar menuBar;
	JMenu menu,pvc,help;
	JMenuItem pvp,undo,quit,easy,normal,hard,explain,about;
	JLabel jb,jc,jd;
	Point point[][] = new Point[11][9];    //����Ϊ9*7�� ���ݸ����2Ϊ�����ж��Ƿ����
	chesspiece chess[] = new chesspiece[16];
	JLabel redWin,blueWin;
	JLabel conside;
	class undoUnit           //����һ���ƶ����������ݵ�Ԫ
	{
		public chesspiece movechess, deadchess;   //��һ���ƶ��������ƶ������ӣ���ȥ�������Լ�Ŀ���ƶ��㣬���ڳ���һ���ƶ�����
		public Point frompoint,topoint;
		public undoUnit next;
	}
	undoUnit head;
	class menuListener implements ActionListener
	{
		JFrame tmp;
		menuListener(JFrame tmp)
		{
			this.tmp = tmp;
		}
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if (e.getSource() == easy)
			{
				clearchess();
				switchListener = 0;
				curmode = mode[0];
				difficulty = 3;
				chessInit();
			}
			if (e.getSource() == normal)
			{
				clearchess();
				switchListener = 0;
				curmode = mode[0];
				difficulty = 4;
				chessInit();
			}
			if (e.getSource() == hard)
			{
				clearchess();
				switchListener = 0;
				curmode = mode[0];
				difficulty = 5;
				chessInit();
			}
			if (e.getSource() == pvp)
			{
				clearchess();
				switchListener = 0;
				curmode = mode[1];
				difficulty = 3;
				chessInit();
			}
			if (e.getSource() == undo)
			{
				if (curmode == mode[1])
				{	
					undo(head);
					head.movechess.setLocation(head.frompoint.y1+50-(head.movechess.ima.getIconWidth())/2, 
							head.frompoint.x1+50-(head.movechess.ima.getIconHeight())/2);
					jd.setVisible(false);
					if (head.deadchess != null)
					{
						head.deadchess.setLocation(head.topoint.y1+50-(head.deadchess.ima.getIconWidth())/2,
								head.topoint.x1+50-(head.deadchess.ima.getIconHeight())/2);
						tmp.getContentPane().add(head.deadchess);
						head.deadchess.setVisible(true);
					}
					jc.setLocation(head.frompoint.y1, head.frompoint.x1);
					curChess = head.movechess;
					head = head.next;
					undo.setEnabled(false);
				}
				if (curmode == mode[0])
				{
					undo(head);
					head.movechess.setLocation(head.frompoint.y1+50-(head.movechess.ima.getIconWidth())/2, 
							head.frompoint.x1+50-(head.movechess.ima.getIconHeight())/2);
					jd.setVisible(false);
					if (head.deadchess != null)
					{
						head.deadchess.setLocation(head.topoint.y1+50-(head.deadchess.ima.getIconWidth())/2,
								head.topoint.x1+50-(head.deadchess.ima.getIconHeight())/2);
						tmp.getContentPane().add(head.deadchess);
						head.deadchess.setVisible(true);
					}
					head = head.next;
					undo(head);
					head.movechess.setLocation(head.frompoint.y1+50-(head.movechess.ima.getIconWidth())/2, 
							head.frompoint.x1+50-(head.movechess.ima.getIconHeight())/2);
					if (head.deadchess != null)
					{
						head.deadchess.setLocation(head.topoint.y1+50-(head.deadchess.ima.getIconWidth())/2,
								head.topoint.x1+50-(head.deadchess.ima.getIconHeight())/2);
						tmp.getContentPane().add(head.deadchess);
						head.deadchess.setVisible(true);
					}
					jc.setLocation(head.frompoint.y1, head.frompoint.x1);
					curChess = head.movechess;
					head = head.next;
					undo.setEnabled(false);
				}
			}
			if (e.getSource() == quit)
			{
				tmp.dispose();
			}
			if (e.getSource() == explain)
			{
				rule r = new rule(750,580,"/gamerule.txt");
				r.setVisible(true);
			}
			if (e.getSource() == about)
			{
				rule r = new rule(450,200,"/about.txt");
				r.setVisible(true);
			}
		}
	}
	DrawBasic()
	{
		super.setTitle("������1.0.0");
		this.setLocation(580,200);
	
    	this.setLayout(null);
    	backgroundInit();
	
//		chessInit();
    	switchListener = 1;
		this.setSize(996, 993);
    	((JPanel)this.getContentPane()).setOpaque(false);  //����������屳��͸��
    	this.addMouseListener(new FrameMouseListener(this));
		this.setVisible(true);
		this.setResizable(false);         //���ô��ڴ�С���ɸı�
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	void clearchess()
	{
		for (int i = 0;i < 16; i++)
			if (chess[i] != null)
				this.getContentPane().remove(chess[i]);
		jd.setVisible(false);
		jc.setVisible(false);
	}
	void connect(chesspiece chess,Point point)
	{
		chess.point = point;
		point.chess = chess;
	}
	
	void chessInit()         //���Ӻ����������ʼ���ķ���
	{
		cnt = 0;
		curChess = null;
		for (int i = 1; i < 10; i++)         //Ϊ�����ϵĵ㸳��ֵ
		{
			for (int j = 1; j < 8; j++)
			
			{
				if ((i >= 4 && i<= 6) && ((j >= 2 && j <= 3)||(j >= 5 && j <=6)))
					point[i][j] = new Point((i-1)*100,(j-1)*100,null,"��",0);
				else
					point[i][j] = new Point((i-1)*100,(j-1)*100,null,"·",0);
				point[i][j].j = j;
				point[i][j].i = i;
			}
		}
		for (int j = 0; j < 9; j++)       //Ϊ���̱߿��ϵĵ㸳��ֵ,���⿪ʼ
		{
			point[0][j] = new Point(0,0,null,"��",0);
			point[10][j] = new Point(0,0,null,"��",0);
		}
		for (int i = 1; i < 10; i++)
		{
			point[i][0] = new Point(0,0,null,"��",0);
			point[i][8] = new Point(0,0,null,"��",0);
		}                                 //              �������
		
		for (int i = 0; i < 16; i++)      //Ϊ���Ӹ���ֵ
		{
			chess[i] = new chesspiece("/icon/"+type[i%8]+"-"+team[i/8]+".png",type[i%8],team[i/8],null,800-(i%8)*100);
		}
		//�����ϵĵ�ļ�ֵ
		int poValue[][] = 
			{
					{0, 0,  0,   0,   0,     0,   0,   0,  0},
					{0, 50, 75,  200, 10000, 200, 75,  50, 0},
					{0, 50, 75,  100, 200,   100, 75,  50, 0},
					{0, 50, 75,  75,  100,   75,  75,  50, 0},
					{0, 50, 50,  50,  50,    50,  50,  50, 0},
					{0, 10, 25,  25,  10,    25,  25,  10, 0},
					{0, 10, 25,  25,  10,    25,  25,  10, 0},
					{0, 5,  5,   5,   5,     5,   5,   5,  0},
					{0, 4,  4,   4,   4,     4,   4,   4,  0},
					{0, 3,  3,   3,   3,     3,   3,   3,  0},
					{0, 0,  0,   0,   0,     0,   0,   0,  0},
			};
		for (int i = 0; i < 11; i++)
			for (int j = 0; j < 9; j++)
			{
				point[i][j].poValue[0] = poValue[i][j];    //���ں�ɫ����˵��ļ�ֵ
				point[i][j].poValue[1] = poValue[10-i][j]; //������ɫ����˵��ļ�ֵ�����ɫ�����¶Գ�
			}
		point[1][3].deAttack = -800;      //ĳЩ��������
		point[1][3].road = trap[1];
		point[1][5].deAttack = -800;
		point[1][5].road = trap[1];
		point[2][4].deAttack = -800;
		point[2][4].road = trap[1];
		point[8][4].deAttack = -800;
		point[8][4].road = trap[0];
		point[9][3].deAttack = -800;
		point[9][3].road = trap[0];
		point[9][5].deAttack = -800;
		point[9][5].road = trap[0];
//		point[1][4].poValue = 10000;
		point[1][4].road = team[1];
//		point[9][4].poValue = 10000;
		point[9][4].road = team[0];
		connect(chess[0],point[7][1]);             //�����Ӱڷ��ڳ�ʼλ����
		connect(chess[1],point[9][7]);
		connect(chess[2],point[9][1]);
		connect(chess[3],point[7][5]);
		connect(chess[4],point[7][3]);
		connect(chess[5],point[8][6]);
		connect(chess[6],point[8][2]);
		connect(chess[7],point[7][7]);
		connect(chess[8],point[3][7]);
		connect(chess[9],point[1][1]);
		connect(chess[10],point[1][7]);
		connect(chess[11],point[3][3]);
		connect(chess[12],point[3][5]);
		connect(chess[13],point[2][2]);
		connect(chess[14],point[2][6]);
		connect(chess[15],point[3][1]);
		deathChess = new HashSet<chesspiece>();        //�����������Լ����������Ӽ��ϳ�ʼ��
		redliveChess = new HashSet<chesspiece>();
		blueliveChess = new HashSet<chesspiece>();
		for (int i = 0; i < 8; i++)
			redliveChess.add(chess[i]);
		for (int i = 8; i < 16; i++)
			blueliveChess.add(chess[i]);
		for (int i = 0; i < 16; i++)
		{
			this.getContentPane().add(chess[i]);
			chess[i].setLocation(chess[i].point.y1+50-(chess[i].ima.getIconWidth())/2, chess[i].point.x1+50-(chess[i].ima.getIconHeight())/2);
			chess[i].addMouseListener(new FrameMouseListener(this));
		}
		redWin.setVisible(false);
		blueWin.setVisible(false);
	}
	

	void backgroundInit()
	{
		Image title = new ImageIcon(getClass().getResource("/icon/Ƥ����.jpg")).getImage();
		this.setIconImage(title);
		ImageIcon back = new ImageIcon(getClass().getResource("/icon/����.jpg"));
		jb = new JLabel(back);
		JLayeredPane pane = this.getLayeredPane();      //��ô���Ĳ����
		pane.add(jb,new Integer(Integer.MIN_VALUE));
		jb.setBounds(0, 0, back.getIconWidth(), back.getIconHeight());
		ImageIcon choose = new ImageIcon(getClass().getResource("/icon/ѡ�п�.png"));
		jc = new JLabel(choose);
		this.getContentPane().add(jc);
		jc.setSize(choose.getIconWidth(), choose.getIconHeight());
		jc.setVisible(false);
		ImageIcon step = new ImageIcon(getClass().getResource("/icon/��ӡ.png"));
		jd = new JLabel(step);
		this.getContentPane().add(jd);
		jd.setSize(step.getIconWidth(), step.getIconHeight());
		jd.setVisible(false);
		ImageIcon redwin = new ImageIcon(getClass().getResource("/icon/��ɫ��ʤ��.png"));
		redWin = new JLabel(redwin);
		this.getContentPane().add(redWin);
		redWin.setBounds(100,300,redwin.getIconWidth(),200);
		redWin.setVisible(false);
		ImageIcon bluewin = new ImageIcon(getClass().getResource("/icon/��ɫ��ʤ��.png"));
		blueWin = new JLabel(bluewin);
		this.getContentPane().add(blueWin);
		blueWin.setBounds(100,300,redwin.getIconWidth(),200);
		blueWin.setVisible(false);
		ImageIcon consideIcon = new ImageIcon(getClass().getResource("/icon/˼����-��.gif"));
		conside = new JLabel(consideIcon);
		this.getContentPane().add(conside);
		conside.setBounds(100,300,consideIcon.getIconWidth(),200);
		conside.setVisible(false);
		menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		menu = new JMenu("��ʼ");
		menuBar.add(menu);
		pvp = new JMenuItem("�µ����˶�ս");
		pvp.addActionListener(new menuListener(this));
		pvc = new JMenu("�µ��˻���ս");
		menu.add(pvc);
		menu.add(pvp);
		undo = new JMenuItem("����");
		undo.addActionListener(new menuListener(this));
		undo.setEnabled(false);
		menu.add(undo);
		quit = new JMenuItem("�˳�");
		quit.addActionListener(new menuListener(this));
		menu.add(quit);
		easy = new JMenuItem("��");
		easy.addActionListener(new menuListener(this));
		pvc.add(easy);
		normal = new JMenuItem("�е�");
		normal.addActionListener(new menuListener(this));
		pvc.add(normal);
		hard = new JMenuItem("����");
		hard.addActionListener(new menuListener(this));
		pvc.add(hard);
		help = new JMenu("����");
		menuBar.add(help);
		explain = new JMenuItem("��Ϸ����");
		explain.addActionListener(new menuListener(this));
		help.add(explain);
		about = new JMenuItem("����");
		about.addActionListener(new menuListener(this));
		help.add(about);
		
	}
	public static void main(String args[])
	{
		new DrawBasic();
	}
	private static int toJ(int x)            //�������� �� point��ά���� ���±�ת������
	{
		return (x- Point.X_BORDER)/100 + 1;
	}
	private static int toI(int y)
	{
		return (y - 70)/100 + 1;       //���������Yֵ��Y_BORDER��ƫ���Ӧ������������Yֵ�Ѵ��嶥���Ŀ��Ҳ������
	}
	private void clickAction(int x,int y,String mod)
	{
		int i = toI(y);
		int j = toJ(x);
//		System.out.println(point[i][j].chess.team + point[i][j].chess.type);
//		System.out.println(x + "  " + y);
//		System.out.println("��ǰ��Ϊpoint"+i+j);
		if (x < Point.X_BORDER || x > 7*100+Point.X_BORDER || y < 70 || y > 9*100+70)
		{
//			System.out.println("ʲô��û��");
		}
		else
		{
			if (curChess == null)            //��ǰû��ѡ������ʱ���������Ϊѡ������
			{
				//����Ĳ��ǿյز����ǵ�ǰ�غϷ�ʱ  ѡ������
				if (point[i][j].chess != null && point[i][j].chess.team == team[cnt%2])
				{	
					curChess = point[i][j].chess;
					jc.setLocation(curChess.point.y1, curChess.point.x1);
					jc.setVisible(true);
//						System.out.println("��ǰѡ������Ϊ" + curChess.type);
				}
				else
				{
//						System.out.println("û��ѡ������!");
				}
			}
			else        //��ǰ�Ѿ���ѡ�е�����ʱ���������Ϊ�ƶ����ӻ�ѡ��������������
			{
				if (point[i][j].chess != null && point[i][j].chess.team == team[cnt%2])
				{
					curChess = point[i][j].chess;
					jc.setLocation(curChess.point.y1, curChess.point.x1);
					jc.setVisible(true);
//						System.out.println("���ѡ������Ϊ" + curChess.type);
				}
				else
				{
					addAvailPoint_(curChess);
					if (moveable(curChess,point[i][j]))      //������ƶ����ƶ��������ƶ�ʱʲô������
					{
						chessPictureUpdate(curChess,point[i][j]);
						undoUnit tmp = new undoUnit();
						move(curChess,point[i][j],tmp);
						addundoUnit(tmp);
						curChess = null;						
						String result1 = winnable();  //�ƶ����Ժ�ʤ�����
						if (result1 == team[0])
						{
							conside.setVisible(false);
							redWin.setVisible(true);
							switchListener = 1;
							return;
						}else if (result1 == team[1])
						{
							conside.setVisible(false);
							blueWin.setVisible(true);
							switchListener = 1;
							return;
						}				
						if (curmode == mode[0])              //��ǰģʽΪ�˻���սʱִ�е��Բ���
						{	
							conside.setVisible(true);
							pvp.setEnabled(false);
							pvc.setEnabled(false);
							undo.setEnabled(false);
							switchListener = 1;
							new Thread(new AImove(this)).start();
						}
					}
					else
						System.out.println("�����ƶ���");
				}
			}
		}
	}
	private boolean moveable(chesspiece c,Point p)       //�ж��Ƿ���ƶ�,pΪĿ������
	{
		addAvailPoint_(c);
		if (c == null)
			return false;
		else
		{
			if (c.availPoint.contains(p)) //��Ŀ�������ڿ��ߵĵ�ķ�Χ��
			{
				if (p.chess == null)    //��Ŀ��������û�����ӣ�����ƶ�
					return true;
				else                    //��Ŀ��������������
				{
					if (p.chess.team == c.team)  //��Ŀ�������ϵ�����Ϊ�������򲻿��ƶ�
						return false;
					else                      //������Ϊ�з�ʱ
					{
						if (c.point.road == trap[(cnt+1)%2]) //�����ǰ�����ڵз��������У��޷�����
							return false;
						if (c.type == "��" && p.chess.type == "��")
						{	
							if(p.road == trap[cnt%2])
								return true;
							else 
								return false;
						}
						if (c.point.road == "·" && p.road == "��")
							return false;
						if (c.point.road == "��" && p.road == "·")
							return false;
						if (c.type == "��" && p.chess.type == "��" && c.point.road == p.road)
							return true;
						if (c.attack >= p.chess.attack + (p.chess.team+"����"==p.road?0:1)*p.deAttack)//������ӹ��������ڵ��ڵз������Գ���
							return true;
						else
							return false;
					}
				}
			}
			else
				return false;
		}
	}
	private void addAvailPoint_(chesspiece c)
	{
		c.availPoint.clear();         //�������c֮ǰ�Ŀɵ���λ��
		addAvailPoint(c.availPoint,c.type,c.point.i,c.point.j,0,1);
		addAvailPoint(c.availPoint,c.type,c.point.i,c.point.j,0,-1);
		addAvailPoint(c.availPoint,c.type,c.point.i,c.point.j,1,0);
		addAvailPoint(c.availPoint,c.type,c.point.i,c.point.j,-1,0);
	}
	//availPoint��ʾ��ǰ���ӿɵ��������㼯��
	//i,j��ʾ��ǰ�������꣬i_,j_��ʾ����ڵ�ǰ���������ƫ�������÷��������ĴΣ�ƫ�����ֱ�Ϊ��-1,0���� +1,0�� ��0��-1���� 0��+1��
	//���ֱ�Ե�ǰ����������ҵ�����жϣ������� �Ͱ������뵽availPoint������
	private void addAvailPoint(HashSet<Point> availPoint,String type,int i,int j,int i_,int j_)
	{
		if (point[i+i_][j+j_].road == "��")
			return;
		if (point[i+i_][j+j_].road == team[cnt%2])
			return;
		if (point[i+i_][j+j_].road == "��")
		{
			if (type == "��")
			{
//				System.out.print("("+(i+i_)+","+(j+j_)+")");
				availPoint.add(point[i+i_][j+j_]);
				return;
			}
			if (type == "ʨ" || type == "��")
			{
				int cnt = 1;       
				boolean flag = true;  //�жϺ�������ı�־
				while (point[i+i_*cnt][j+j_*cnt].road == "��")
				{
					if (point[i+i_*cnt][j+j_*cnt].chess != null)
						flag = false;  //��ʱ��������
					cnt++;
				}
				if (flag)
				{
//					System.out.print("("+(i+i_)+","+(j+j_)+")");
					availPoint.add(point[i+i_*cnt][j+j_*cnt]);
				}
				return;
			}
		}
		if (point[i+i_][j+j_].road == (team[(cnt+1)%2]))
		{
			availPoint.add(point[i+i_][j+j_]);
			return;
		}
		if (point[i+i_][j+j_].road == "·" || point[i+i_][j+j_].road == trap[0]|| point[i+i_][j+j_].road == trap[1])
		{
//			System.out.print("("+(i+i_)+","+(j+j_)+")");
			availPoint.add(point[i+i_][j+j_]);
		}
	}
	private void chessPictureUpdate(chesspiece c, Point p)
	{

		if (p.chess == null)
		{
			jd.setLocation(c.point.y1,c.point.x1);   //�Ų�ͼ����ʾ
			jd.setVisible(false);
			jd.setVisible(true);
			c.setLocation(p.y1+50-(c.ima.getIconWidth())/2, p.x1+50-(c.ima.getIconHeight())/2);

		}
		else
		{
			jd.setLocation(c.point.y1,c.point.x1);    //�Ų�ͼ����ʾ
			jd.setVisible(false);
			jd.setVisible(true);
			p.chess.setVisible(false);
			this.getContentPane().remove(p.chess);    //�����߶���ͼ���ƶ�  �Լ������е�point���Ժ͵��е�chess����ͬʱ����
			c.setLocation(p.y1+50-(c.ima.getIconWidth())/2, p.x1+50-(c.ima.getIconHeight())/2);
		}
//		jc.setVisible(false);    //ѡ�����ӵ�ͼ������
		jc.setLocation(p.y1, p.x1);
	}
	private void move(chesspiece c,Point p,undoUnit u)     //�ƶ�����һ��
	{
		if (p.chess == null)
		{	
			u.movechess = c;
			u.frompoint = c.point;
			u.topoint = p;
			u.deadchess = null;	
			c.point.chess = null;            //�����е�point���Ժ͵��е�chess����ͬʱ����
			c.point = p;
			p.chess = c;
		}
		else
		{
			u.movechess = c;
			u.frompoint = c.point;
			u.topoint = p;
			u.deadchess = p.chess;
			c.point.chess = null;
			deathChess.add(p.chess);                  //�����������������Ӹ���
			redliveChess.remove(p.chess);             
			blueliveChess.remove(p.chess);
			c.point = p;
			p.chess = c;
		}
		cnt++;                   //�غ�ת��
	}
	private String winnable()   //����ʤ���� ��û�н�����Ϸ�򷵻�"��"
	{
		if (this.redliveChess.isEmpty() || point[9][4].chess != null)
			return team[1];
		if (this.blueliveChess.isEmpty() || point[1][4].chess != null)
			return team[0];
		return "��";
	}
	private int eveluation(int turn)    //�����ֵ����
	{
		int redSum = 0, blueSum = 0;
		int basicValue = 0;       //���ӻ�����ֵ
		int flexValue = 0;        //��������Լ�ֵ
		for (Iterator<chesspiece> it1 = blueliveChess.iterator();it1.hasNext();)
		{
			chesspiece c = (chesspiece)it1.next();
			if (c == chess[15] && redliveChess.contains(chess[0]))
				basicValue += 400;
			basicValue += c.attack;
			flexValue += c.point.poValue[1];
			addAvailPoint_(c);
			HashSet<Point> availPoint = new HashSet<Point>(c.availPoint);
			for (Iterator<Point> it2 = availPoint.iterator(); it2.hasNext();)
			{
				Point p = it2.next();
				if (p.chess == null)
				{
					flexValue += c.attack/6;
				}
				else if (p.chess.team == team[0])
				{
					if (moveable(c,p))
					{
						flexValue += (team[1] == team[turn] ? p.chess.attack/2:c.attack/2);
					}
				}
			}
		}
		blueSum = basicValue + flexValue;
		basicValue = 0;
		flexValue = 0;
		for (Iterator<chesspiece> it1 = redliveChess.iterator();it1.hasNext();)
		{
			chesspiece c = (chesspiece)it1.next();
			if (c == chess[7] && redliveChess.contains(chess[8]))
				basicValue += 400;
			basicValue += c.attack;
			flexValue += c.point.poValue[0];
			addAvailPoint_(c);
			HashSet<Point> availPoint = new HashSet<Point>(c.availPoint);
			for (Iterator<Point> it2 = availPoint.iterator(); it2.hasNext();)
			{
				Point p = it2.next();
				if (p.chess == null)
				{
					flexValue += c.attack/6;
				}else if (p.chess.team == team[1])
				{
					if (moveable(c,p))
					{
						flexValue += (team[0] == team[turn] ? p.chess.attack/2:c.attack/2);
					}
				}
			}
		}
		redSum = basicValue + flexValue;
		return blueSum - redSum;
	}
	private void undo(undoUnit u)        //����һ���ƶ�����
	{
		if (u.deadchess == null)
		{
			u.movechess.point = u.frompoint;
			u.topoint.chess = null;
			u.frompoint.chess = u.movechess;
		}
		else
		{
			u.movechess.point = u.frompoint;
			u.deadchess.point = u.topoint;
			u.frompoint.chess = u.movechess;
			u.topoint.chess = u.deadchess;
			deathChess.remove(u.deadchess);
			if (u.deadchess.team == team[0])
				redliveChess.add(u.deadchess);
			else
				blueliveChess.add(u.deadchess);
		}
		cnt--;
	}
	private void addundoUnit(undoUnit u)     //���Ʋ���¼�����м����µĽڵ�
	{
		u.next = head;
		head = u;
		undo.setEnabled(true);
	}
	final boolean fa = false;
	public  int MaxMinSearch(int deapth,int turn,boolean flag,int alpha,int beta)
	{
		 undoUnit u = new undoUnit();
		 int score = 0;
		 if (turn%2 == 0)
			 score = 10000;
		 else
			 score = -10000;	 
		 if (winnable() == team[0] || winnable() == team[1] )
			 return eveluation(turn%2);
		 if (deapth == 0)
		 {
			 int a = eveluation(turn%2);
	//		 System.out.print(a + "  ");
			 return a;
		 }
		 HashSet<chesspiece> redlive = new HashSet<chesspiece>(redliveChess);
		 HashSet<chesspiece> bluelive = new HashSet<chesspiece>(blueliveChess);
		 for (Iterator<chesspiece> it1 = (team[0] == team[turn])?redlive.iterator():bluelive.iterator();it1.hasNext(); )
		 {
			 chesspiece c = (chesspiece)it1.next();
			 addAvailPoint_(c);
			 HashSet<Point> availPoint = new HashSet<Point>(c.availPoint);
				boolean fl = true;
			for (Iterator<Point> it2 = availPoint.iterator();it2.hasNext();)
			{
				Point p = (Point)it2.next();
//				System.out.println("��ǰ��  "+c.name+"��ǰĿ��λ��  ��"+p.i+" "+p.j+")");
				if (fl)
					if (moveable(c,p))			
					{
						move(c,p,u);
//						System.out.println("�ƶ�"+c.name+"����"+p.i+" "+p.j+")");
						if (turn == 0)         //ȡ��Сֵ
						{
							int min = MaxMinSearch(deapth-1,(turn+1)%2,fa,alpha,beta);
							score = score <= min ? score : min;
							if (score <= beta)
							{
								beta = score;
								if (alpha > beta)
									fl = false;
							}
						}					
						else                   //ȡ����ֵ
						{
							int max = MaxMinSearch(deapth-1,(turn+1)%2,fa,alpha,beta);
							score = score >= max ? score : max;
	//						System.out.print("score:"+score + "  ");
							if (score >= alpha)
							{
								alpha = score;
								if (alpha > beta)
									fl = false;
							}
							if (flag == true)
							{
								if (score == max)
								{
									AIchess = c;
									AIpoint = p;
								}
							}
						}
						undo(u);
//						System.out.println("�����ƶ�"+c.name+"����"+p.i+" "+p.j+")");
					}
			}
		}
		 return score;
	}
	//���������¼�������
	public class FrameMouseListener implements MouseListener{
	
		private JFrame tmp;
		FrameMouseListener(JFrame tmp)
		{
			this.tmp = tmp;
		}
		@Override
	public void mouseClicked(MouseEvent e) {
			if (switchListener == 0)
			{
				// TODO Auto-generated method stub
				if(e.getSource() != tmp)
					clickAction(e.getX() + ((chesspiece)e.getSource()).getX(),e.getY() + ((chesspiece)e.getSource()).getY() + 40,curmode);
				else
					clickAction(e.getX(),e.getY(),curmode);
			}
	}
		@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}
	}
	class AImove implements Runnable
	{
		JFrame tmp;
		AImove(JFrame tmp)
		{
			this.tmp = tmp;
		}
		public void run()
		{
			int score = MaxMinSearch(difficulty,1,true,-10000,10000);           //�����ƶ� 
			conside.setVisible(false);
			pvp.setEnabled(true);
			pvc.setEnabled(true);
			undo.setEnabled(true);
			if (score == -10000)
			{
				redWin.setVisible(true);
				switchListener = 1;
				return;
			}
			else if (score == 10000)
			{
				blueWin.setVisible(true);
				switchListener = 1;
				return;
			}
			chessPictureUpdate(AIchess,AIpoint);
			undoUnit tmp = new undoUnit();
			move(AIchess,AIpoint,tmp);
			addundoUnit(tmp);
			AIchess = null;
			AIpoint = null;	
			String result2 = winnable();  //�ƶ����Ժ�ʤ�����
			if (result2 == team[0])
			{
				redWin.setVisible(true);
				switchListener = 1;
				return;
			}else if (result2 == team[1])
			{
				blueWin.setVisible(true);
				switchListener = 1;
				return;
			}
			switchListener = 0;
		}
	}
}
class Point{
	public static final int X_BORDER = 150;
	public static final int Y_BORDER = 5;
	public int x1,x2,y1,y2;
	public int deAttack;         //�����ӹ�����������ֵ
	public int poValue[];          //��ǰ��ļ�ֵ,poValue0Ϊ�Ժ�ɫ���ļ�ֵpoValue1Ϊ����ɫ���ļ�ֵ
	public int i;
	public int j;
	public chesspiece chess;
	public String road;
	Point()
	{
		x1 = x2 = y1 = y2 = -1;
		chess = null;
		road = null;
		deAttack = 0;
	}
	Point(int i,int j,chesspiece chess,String road,int deAttack)
	{
		x1 = i + Y_BORDER;
		x2 = x1 + 100;
		y1 = j + X_BORDER;
		y2 = y1 + 100;
		this.chess = chess;
		this.road = road;
		this.deAttack = deAttack;
		poValue = new int[2];
	}
}