package animalbattle;
import javax.swing.*;
import java.util.HashSet;
public class chesspiece extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7085412541922738700L;
	public String type;
	public String team;
	public String name;
	public Point point;
	public HashSet<Point> availPoint;
	public int attack;
	ImageIcon ima;
	chesspiece(String path,String type,String team,Point point,int attack)
	{
		ima = new ImageIcon(getClass().getResource(path));
		this.type = type;
		this.team = team;
		this.point = point;
		this.attack = attack;
		name = team + type;
		availPoint = new HashSet<Point>();        //����ǰ���ӿ��ߵĵ㶼�����ڸü�����
		super.setIcon(ima);
		super.setSize(ima.getIconWidth(), ima.getIconHeight());
//		super.setFocusPainted(true);           //����JButton���ƽ���߿�
		super.setBorder(null);                 //����JButton�ޱ߿�
		super.setContentAreaFilled(false);     //����JButton����͸��
	}
}
