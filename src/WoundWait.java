import java.util.Calendar;
import java.sql.Timestamp;
public class WoundWait{
	public static void main(String args[]){
		Timestamp t1 = new Timestamp(Calendar.getInstance().getTime().getTime());
		Timestamp t2 = new Timestamp(Calendar.getInstance().getTime().getTime());
		System.out.println(t1.getTime()+"<"+t2.getTime()+":"+(t1.getTime()<t2.getTime()));
		System.out.println(Calendar.getInstance().getTime());
	}
}