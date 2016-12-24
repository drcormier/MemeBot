import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.audio.hooks.ListenerProxy;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.entities.impl.JDAImpl;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

public class MemeBot extends ListenerAdapter{
    
    private static String token;
    private AudioManager manager;
    public static boolean dc = false;
    public static Object lock = new Object();
    private final String[] airhorns = {
            "!airhorn default",
            "!airhorn reverb",
            "!airhorn spam",
            "!airhorn tripletap",
            "!airhorn fourtap",
            "!airhorn distant",
            "!airhorn echo",
            "!airhorn clownfull",
            "!airhorn clownshort",
            "!airhorn clownspam",
            "!airhorn highfartlong",
            "!airhorn highfartshort",
            "!airhorn midshort",
            "!airhorn truck",
            "!anotha one",
            "!anotha one_classic",
            "!anotha one_echo",
            "!cena airhorn",
            "!cena full",
            "!cena jc",
            "!cena nameis",
            "!cena spam",
            "!eb areyou_classic",
            "!eb areyou_condensed",
            "!eb areyou_crazy",
            "!eb areyou_ethan",
            "!eb classic",
            "!eb echo",
            "!eb high",
            "!eb slowandlow",
            "!eb cuts",
            "!eb beat",
            "!eb sodiepop",
            "!stan herd",
            "!stan moo",
            "!stan x3",
            "!bday horn",
            "!bday horn3",
            "!bday sadhorn",
            "!bday weakhorn",
            "!wtc"};
    
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
        dc = false;
        long started = System.currentTimeMillis();
        Member mem = event.getMember();
        if(!mem.getUser().isBot()){
            VoiceChannel voiceChan = event.getChannelJoined();
            connectTo(voiceChan);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            Guild g = event.getGuild();
            TextChannel chan = g.getTextChannelById("261176936510783488");
            chan.sendMessage(getRandomAirhorn()).queue();
            
            new Thread(() -> waitToDisconnect()).start();
            //JDAImpl temp = (JDAImpl)chan.getJDA();
            //temp.getAudioManagerMap().forEach((guildID, mng) -> mng.closeAudioConnection());
            //temp.getClient().close();
            
        }
    }

    private void connectTo(VoiceChannel channel){
        if(manager == null){
            manager = channel.getGuild().getAudioManager();
        }
        System.out.println(manager.getQueuedAudioConnection());
        //ListenerProxy lp = new ListenerProxy();
        MemeListener ml = new MemeListener();
        manager.setConnectionListener(ml);
        manager.openAudioConnection(channel);
        System.out.println(manager.getQueuedAudioConnection());
    }

    private String getRandomAirhorn(){
        int randInt = (int) (airhorns.length*Math.random());
        return airhorns[randInt];
    }

    private void waitToDisconnect(){
        synchronized(lock){
            while(dc == false){
                try{
                    lock.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            manager.closeAudioConnection();

        }
    }
}

class MemeListener implements ConnectionListener{

    @Override
    public void onPing(long arg0) {
        //System.out.println(arg0);
        
    }

    @Override
    public void onStatusChange(ConnectionStatus arg0) {
        System.out.println(arg0);
        if(arg0.equals(ConnectionStatus.CONNECTED)){
            synchronized(MemeBot.lock){
                MemeBot.dc = true;
                MemeBot.lock.notifyAll();
            }
        }
        
    }

    @Override
    public void onUserSpeaking(User arg0, boolean arg1) {
        //System.out.println(arg0 + " " + arg1);  
    }

}
