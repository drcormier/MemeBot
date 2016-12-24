import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemeBot extends ListenerAdapter{
	
	private static final String token = "MjYyMDY1NzIwMzQ1NjI0NTc3.Cz-DWQ.OAc46MCLG7QCLkqa3h_gw9aSJ24";
	
	public static void main(String[] args){
		try{
			JDA jda = new JDABuilder(AccountType.BOT)
					.setToken(token)
					.addListener(new MemeBot())
					.buildBlocking();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
}