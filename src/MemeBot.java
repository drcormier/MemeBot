import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

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

    private void connectTo(VoiceChannel channel){
        AudioManager manager = channel.getGuild().getAudioManager();
        manager.openAudioConnection(channel);
    }

    private void disconnectFrom(VoiceChannel channel){
        AudioManager manager = channel.getGuild().getAudioManager();
        manager.closeAudioConnection();
        
    }
}
