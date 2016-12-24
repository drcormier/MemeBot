import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

public class MemeBot extends ListenerAdapter{
    
    // the token associated with the bot
    private static String token;
    // the audiomanager for the guild
    private AudioManager manager;
    // the boolean to disconnect
    public static boolean dc = false;
    // the object to lock onto
    public static Object lock = new Object();
    // the list of airhorn solutions commands
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
            System.out.println("Usage: java -jar MemeBot.jar token");
            System.exit(1);
        }
        // add token from CLA
        token=args[0];
        // build instance of JDA
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
    
    /**
     * Do stuff when a user connects to a voice channel in a guild (discord server).
     * In this instance, the bot will join the channel, send a random airhorn solutions bot command,
     * wait to connect, then disconnect.
     * @param event the GuildVoiceJoinEvent that is associated with this event
     */
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        // ensure that we do not disconnect immediately after beginning a connection
        dc = false;
        Member mem = event.getMember();
        // ensure joinee is not a bot
        if(!mem.getUser().isBot()){
            VoiceChannel voiceChan = event.getChannelJoined();
            // connect to the voice channel
            connectTo(voiceChan);
            // wait a little bit
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            // send the message to the bot channel
            Guild g = event.getGuild();
            TextChannel chan = g.getTextChannelById("261176936510783488");
            chan.sendMessage(getRandomAirhorn()).queue();
            // wait to disconnect in a separate thrad
            new Thread(() -> waitToDisconnect()).start();
            
        }
    }

    /**
     * Connect to a given channel.
     *
     * @param channel the voicechannel to connect to.
     */
    private void connectTo(VoiceChannel channel){
        if(manager == null){
            manager = channel.getGuild().getAudioManager();
        }
        // make a new connection listener instance
        MemeListener ml = new MemeListener();
        // give the audiomanager the connection listener
        manager.setConnectionListener(ml);
        // open audio connection
        manager.openAudioConnection(channel);
    }

    /**
     * Get a random airhorn solutions bot command
     *
     * @return the int index to pull the command from
     */
    private String getRandomAirhorn(){
        int randInt = (int) (airhorns.length*Math.random());
        return airhorns[randInt];
    }

    /**
     * Wait to disconnect from the audio connection.
     * If the bot disconnects before the connection is fully connected,
     * the bot will be stuck in the channel. As such, we wait until
     * the bot is connected before disconnecting.
     * NOTE: must be run in a separate thread.
     */
    private void waitToDisconnect(){
        // wait until we can disconnect
        synchronized(lock){
            while(dc == false){
                try{
                    lock.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            // disconnect
            manager.closeAudioConnection();

        }
    }
}

/**
 * An implementation of ConnectionListener designed to send
 * the instance of MemeBot a signal to disconnect.
 * To do this, we use OnStatusChange.
 * @author Daniel Cormier
 * @author Cosmo Viola
 */
class MemeListener implements ConnectionListener{

    @Override
    public void onPing(long arg0) {
        //System.out.println(arg0);
        
    }

    @Override
    public void onStatusChange(ConnectionStatus arg0) {
        // if we are connected
        if(arg0.equals(ConnectionStatus.CONNECTED)){
            // tell the disconnect thread to disconnect
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
