import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
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
	
	@Override
	public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        Member mem = event.getMember();
        if(!mem.getUser().isBot()){
            VoiceChannel voiceChan = event.getChannelJoined();
            connectTo(voiceChan);
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Guild g = event.getGuild();
            TextChannel chan = g.getTextChannelById("261176936510783488");
            chan.sendMessage("!eb sodiepop").queue();
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            disconnectFrom(voiceChan);
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
