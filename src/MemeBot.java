import java.io.File;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
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
    // if airhorns are enabled
    public static boolean airhornOn = true;
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
    private static HashMap<String,BotCommand> commands;
    private static HashMap<BotCommand,String> commandDescriptions;
    
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Usage: java -jar MemeBot.jar token");
            System.exit(1);
        }
        // add token from CLA
        token=args[0];
        addCommands();
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
        dc = false;
        if(airhornOn){
            // ensure that we do not disconnect immediately after beginning a connection
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
    }
    
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        Message m = event.getMessage();
        String s = m.getContent();
        Member mem = m.getGuild().getMember(m.getAuthor());
        MessageChannel chan = m.getChannel();
        if(commands.containsKey(s)){
            parseCommands(commands.get(s),mem,chan);
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

    private void parseCommands(BotCommand com, Member user, MessageChannel chan){
        boolean bm = false;
        List<Role> r = user.getGuild().getRolesByName("Botnet Managers",false);
        List<Member> mems = user.getGuild().getMembersWithRoles(r);
        if(mems.contains(user)){
            bm = true;
        } 
        switch(com){
            case AIRHORN_ON:
                if(bm){
                    airhornOn = true;
                    chan.sendMessage("Airhorning enabled.").queue();
                }
                break;
            case AIRHORN_OFF:
                if(bm){
                    airhornOn = false;
                    chan.sendMessage("Airhorning disabled.").queue();
                }
                break;
            case AIRHORN_STATUS:
                chan.sendMessage("Airhorning is "+ (airhornOn ? "enabled." : "disabled.")).queue();
                break;
            case MEISENNERD:
                if(user.getUser().getId().equals("107272630842728448")){
                    try{
                        File f = new File("data/meis.png");
                        chan.sendFile(f,null).queue();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case COMMAND_LIST:
                printCommands(chan);
                break;
            case SHUTDOWN:
                if(bm){
                    chan.sendMessage("Shutting down.").queue();
                    chan.getJDA().shutdown();
                }
                break;
            case AIRHORN_COMMANDS:
                printAirhorn(chan);
                break;
            case AIRHORN_COMMANDS_DESCRIPTIONS:
                printCommandsAndDescriptions(chan);
                break;
        }
    }

    private static void addCommands(){
        if(commands == null && commandDescriptions == null){
            commands = new HashMap<>();
            commandDescriptions = new HashMap<>();
            commands.put("!MemeBot airhornOn",BotCommand.AIRHORN_ON);
            commandDescriptions.put(BotCommand.AIRHORN_ON, "Enable the airhorn functionality of MemeBot");
            commands.put("!MemeBot airhornOff",BotCommand.AIRHORN_OFF);
            commandDescriptions.put(BotCommand.AIRHORN_OFF, "Disable the airhorn functionality of MemeBot");
            commands.put("!MemeBot airhornStatus",BotCommand.AIRHORN_STATUS);
            commandDescriptions.put(BotCommand.AIRHORN_STATUS, "Check the airhorn functionality status of MemeBot");
            commands.put("!MemeBot meisennerd",BotCommand.MEISENNERD);
            commandDescriptions.put(BotCommand.MEISENNERD, "Print a picture of Ben");
            commands.put("!MemeBot commands",BotCommand.COMMAND_LIST);
            commandDescriptions.put(BotCommand.COMMAND_LIST, "Print a list of MemeBot commands");
            commands.put("!MemeBot shutdown",BotCommand.SHUTDOWN);
            commandDescriptions.put(BotCommand.SHUTDOWN, "Shut down MemeBot");
            commands.put("!MemeBot airhornCommands",BotCommand.AIRHORN_COMMANDS);
            commandDescriptions.put(BotCommand.AIRHORN_COMMANDS, "Print a list of Airhorn Solutions commands");
            commands.put("!MemeBot com+desc",BotCommand.AIRHORN_COMMANDS_DESCRIPTIONS);
            commandDescriptions.put(BotCommand.AIRHORN_COMMANDS_DESCRIPTIONS, "Prints a list of MemeBot commands and their descriptions");
        }
    }

    private void printCommands(MessageChannel mc){
        String temp = "**NOTE:** If the command is __underlined__ then the command is restricted in use.\n";
        temp = temp + "*for descriptions of the commands, use* `!MemeBot com+desc`\n";
        temp = temp + "Current MemeBot commands:\n";
        for(String c : commands.keySet()){
            temp = temp + "\n" + (commands.get(c).isRestricted() ? "__" : "") + "`" + c + "`" + (commands.get(c).isRestricted() ? "__" : "");
        }
        mc.sendMessage(temp).queue();
    }

    private void printCommandsAndDescriptions(MessageChannel mc){
        String temp = "**NOTE:** If the command is __underlined__ then the command is restricted in use.\n";
        temp = temp + "Current MemeBot commands:\n";
        for(String c : commands.keySet()){
            temp = temp + "\n" + (commands.get(c).isRestricted() ? "__" : "") + "`" + c + "`" + (commands.get(c).isRestricted() ? "__" : "");
            if(commandDescriptions.containsKey(commands.get(c))){
                temp = temp + "\n" + "\t\t" + commandDescriptions.get(commands.get(c));
            }
        }
        mc.sendMessage(temp).queue();

    }

    private void printAirhorn(MessageChannel mc){
        String temp="Current airhorn commands:\n";
        for(String c : airhorns){
            temp = temp + "\n" + c;
        }
        mc.sendMessage(temp).queue();
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
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
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

enum BotCommand{
    AIRHORN_ON (true),
    AIRHORN_OFF (true),
    AIRHORN_STATUS (false),
    MEISENNERD (true),
    COMMAND_LIST (false),
    SHUTDOWN (true),
    AIRHORN_COMMANDS (false),
    AIRHORN_COMMANDS_DESCRIPTIONS (false);

    BotCommand(boolean r){
        restricted=r;
    }

    private final boolean restricted;
    boolean isRestricted(){return restricted;}
}
