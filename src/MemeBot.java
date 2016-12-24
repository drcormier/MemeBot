import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MemeBot extends ListenerAdapter{
	
	private static String token;
	
	public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Usage: java MemeBot token");
            System.exit(1);
        }
        token=args[0];
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
