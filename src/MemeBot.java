import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.audio.hooks.ConnectionListener;
import net.dv8tion.jda.core.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.core.entities.Game;
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

    private static final String COMMAND = "!MemeBot";

    private final boolean log;
    
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
    public static boolean harassBen = false;
    // the list of airhorn solutions commands
    private static final String[] airhornCommands = {
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

    // list of commands used in random command selection
    private static final String[] airhorns = {
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
            "!eb sodiepop",
            "!stan herd",
            "!stan moo",
            "!bday horn",
            "!bday sadhorn",
            "!bday weakhorn",
            "!wtc"};
    
    private static final String[] thinking = {
    		"hmm...",
    		"really makes you wonder",
    		"does 1+1=2?",
    		"is ben a nerd?",
    		"what is the meaning of life?",
    		"can jet fuel melt steel beams?",
    		"did bush do 9/11?",
    		"is MemeBot in the illuminati?",
    		"should I choose symmetra if my team needs a support?",
    		"let's think about this for a second",
    		"did pepe deserve to be classified a hate symbol?",
    		"should I buy an AWP if the rest of my team is on an eco?"
    };
    // map of commands
    private static HashMap<String,BotCommand> commands;
    // map of command descriptions
    private static HashMap<BotCommand,String> commandDescriptions;
    
    private static final String wheresthatnerd = ":regional_indicator_w: :regional_indicator_h: :regional_indicator_e: :regional_indicator_r: :regional_indicator_e: :regional_indicator_s: :clap: :regional_indicator_t: :regional_indicator_h: :regional_indicator_a: :regional_indicator_t: :clap: :regional_indicator_n: :regional_indicator_e: :regional_indicator_r: :regional_indicator_d: :clap: ";
    
    private static StringBuffer sb = new StringBuffer();

    public static void main(String[] args){
        boolean l = false;
        if(args.length < 1){
            System.out.println("Usage: java -jar MemeBot.jar token");
            System.exit(1);
        }
        // add token from CLA
        token=args[0];
        if(args.length == 2){
            l=Boolean.parseBoolean(args[1]);
        }
        // add commands to the maps
        addCommands();
        // build instance of JDA
        try{
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addListener(new MemeBot(l))
                    .buildBlocking();
            jda.getPresence().setGame(Game.of("with dank memes"));
            sb.append(Character.toChars(0x1F914));
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public MemeBot(){
        this.log=false;
    }

    public MemeBot(boolean log){
        this.log=log;
    }
    
    /**
     * Do stuff when a user connects to a voice channel in a guild (discord server).
     * In this instance, the bot will join the channel, send a random airhorn solutions bot command,
     * wait to connect, then disconnect.
     * @param event the GuildVoiceJoinEvent that is associated with this event. from the event the following
     * can be extracted: the user/member who joined, the voice channel they joined, and the guild the voice
     * channel was in, among other things.
     */
    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event){
        printRandomAirhorn(event.getMember());
    }
    
    /**
     * Execute code when the bot detects a message sent to a guild
     * message channel.
     * @param event the event associated with the message. from the event the following
     * can be extracted: the message, the user/member who sent it, the content of the
     * message, the message channel it was sent in, and the guild it was sent in,
     * among other things.
     */
    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event){
        Message m = event.getMessage();
        String s = m.getContent();
        String[] message = s.split(" ");
        Member mem = m.getGuild().getMember(m.getAuthor());
        MessageChannel chan = m.getChannel();
        if(message[0].equals(COMMAND)){
            if(commands.containsKey(message[1])){
                //printLogMessage(mem.getNickname() + " sent the command " + commands.get(message[1]));
                parseCommands(commands.get(message[1]),mem,chan,m);
            }else{
                chan.sendMessage("ERROR: `" + s + "` is not a command!\nTo see a list of commands, type `!MemeBot commands`").queue();
            }

        }
        else if(s.contains(sb.toString()) && !mem.getUser().getId().equals("262065720345624577")){
        	chan.sendMessage(getRandomThinking() + " :thinking:").queue();
        }
        else if(harassBen && mem.getUser().getId().equals("107272630842728448")){
            chan.sendMessage("Did you mean to type `!MemeBot meisennerd`?").queue();
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
        MemeListener ml = new MemeListener(channel.getGuild(),this);
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
        int randInt = (int) Math.round((airhorns.length-1)*Math.random());
        return airhornCommands[randInt];
    }
    
    private String getRandomThinking(){
        int randInt = (int) Math.round((thinking.length-1)*Math.random());
        return thinking[randInt];
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

    /**
     * Parse bot commands.
     * Takes in the bot commands and performs the expected actions.
     * @param com the command to be processed.
     * @param user the user issuing the command. Used to determine permissions for restricted commands.
     * @param chan the MessageChannel that the command is given in. Used for the bot to respond to the commands.
     */
    private void parseCommands(BotCommand com, Member user, MessageChannel chan, Message mess){
        /* Most of the restricted commands (like shutdown) are restricted to users
         * with the "Botnet Managers" role. As such, when parsing  commands, we check
         * if the user making the request has that role before allowing them to use
         * those restricted commands.
         */
        boolean bm = false; // stores botnet manager status of the user
        // get the roles named "Botnet Managers"
        List<Role> r = user.getGuild().getRolesByName("Botnet Managers",false);
        // get the list of members with those roles
        List<Member> mems = user.getGuild().getMembersWithRoles(r);
        // if the user has the role, set their manager status as true
        if(mems.contains(user)){
            bm = true;
        }
        // switch statements for readability
        switch(com){
            case AIRHORN_ON: // !MemeBot airhornON
                if(bm){
                    airhornOn = true;
                    chan.sendMessage("Airhorning enabled.").queue();
                }
                break;
            case AIRHORN_OFF: // !MemeBot airhornOff
                if(bm){
                    airhornOn = false;
                    chan.sendMessage("Airhorning disabled.").queue();
                }
                break;
            case AIRHORN_STATUS: // !MemeBot airhornStatus
                chan.sendMessage("Airhorning is "+ (airhornOn ? "enabled." : "disabled.")).queue();
                break;
            case MEISENNERD: // !MemeBot meisennerd
                if(user.getUser().getId().equals("107272630842728448")){
                    try{
                        InputStream stream = MemeBot.class.getResourceAsStream("meis.png");
                        chan.sendFile(stream, "meis.png", null);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            case COMMAND_LIST: // !MemeBot commands
                printCommands(chan);
                break;
            case SHUTDOWN: // !MemeBot shutdown
                if(bm){
                    chan.sendMessage("Shutting down.").queue();
                    chan.getJDA().shutdown();
                }
                break;
            case AIRHORN_COMMANDS: // !MemeBot airhornCommands
                printAirhorn(chan);
                break;
            case COMMANDS_DESCRIPTIONS: // !MemeBot com+desc
                printCommandsAndDescriptions(chan);
                break;
            case RANDOM_AIRHORN:
                printRandomAirhorn(user);
                break;
            case HARASS_BEN:
                if(bm){
                    harassBen = !harassBen;
                }
                break;
            case WTN:
            	String name = "";
            	String[] m = mess.getRawContent().split(" ");
            	for( int i = 2; i < m.length; ++i){
            		name += m[i] + " ";
            	}
            	name = name.toLowerCase().trim();
            	chan.sendMessage(wtn(name)).queue();
            	break;
        }
    }
    
    private static String wtn( String name ){
    	String temp = wheresthatnerd;
    	for( char c: name.toCharArray() ){
    		if( c == ' '){
    			temp += ":clap: ";
    		}else{
    			temp += ":regional_indicator_" + c + ": ";
    		}
    	}
    	return temp;
    }

    /**
     * Helper function that adds commands to the respective data structures.
     * There are two HashMaps that store command data: commands and commandDescriptions.
     * commands maps the string representation of the command to its associated BotCommand enum type,
     * for determining which command to parse.
     * commandDescroptions maps the BotCommand enum types to a String containing a description of
     * the command, for usage in the com+desc command.
     */
    private static void addCommands(){
        if(commands == null && commandDescriptions == null){
            // initialize the hashmaps
            commands = new HashMap<>();
            commandDescriptions = new HashMap<>();
            // add the commands
            commands.put("airhornOn",BotCommand.AIRHORN_ON);
            commandDescriptions.put(BotCommand.AIRHORN_ON, "Enable the airhorn functionality of MemeBot");

            commands.put("airhornOff",BotCommand.AIRHORN_OFF);
            commandDescriptions.put(BotCommand.AIRHORN_OFF, "Disable the airhorn functionality of MemeBot");

            commands.put("airhornStatus",BotCommand.AIRHORN_STATUS);
            commandDescriptions.put(BotCommand.AIRHORN_STATUS, "Check the airhorn functionality status of MemeBot");

            commands.put("meisennerd",BotCommand.MEISENNERD);
            commandDescriptions.put(BotCommand.MEISENNERD, "Print a picture of Ben");

            commands.put("commands",BotCommand.COMMAND_LIST);
            commandDescriptions.put(BotCommand.COMMAND_LIST, "Print a list of MemeBot commands");

            commands.put("shutdown",BotCommand.SHUTDOWN);
            commandDescriptions.put(BotCommand.SHUTDOWN, "Shut down MemeBot");

            commands.put("airhornCommands",BotCommand.AIRHORN_COMMANDS);
            commandDescriptions.put(BotCommand.AIRHORN_COMMANDS, "Print a list of Airhorn Solutions commands");

            commands.put("com+desc",BotCommand.COMMANDS_DESCRIPTIONS);
            commandDescriptions.put(BotCommand.COMMANDS_DESCRIPTIONS, "Prints a list of MemeBot commands and their descriptions");

            commands.put("random",BotCommand.RANDOM_AIRHORN);
            commandDescriptions.put(BotCommand.RANDOM_AIRHORN, "Prints a random Airhorn Solutions command.");

            commands.put("harass",BotCommand.HARASS_BEN);
            commandDescriptions.put(BotCommand.HARASS_BEN, "Toggle bot harassment.");
            
            commands.put("wtn",BotCommand.WTN);
            commandDescriptions.put(BotCommand.WTN, "Wheres that nerd?");
        }
    }

    /**
     * Print the list of commands to a given MessageChannel.
     * Note: ordering is based on the internal ordering of HashMap.
     * @param mc the MessageChannel to output to.
     */
    private void printCommands(MessageChannel mc){
        // starting info
        String temp = "**NOTE:** If the command is __underlined__ then the command is restricted in use.\n";
        temp = temp + "*for descriptions of the commands, use* `!MemeBot com+desc`\n";
        temp = temp + "Current MemeBot commands:\n";
        // iterate over command strings
        for(String c : commands.keySet()){
            /* add the commands to a temp string. If the command is restricted, the command will be underlined (surrounded by __ in discord markdown).
             * In addition, the command itself will be placed inside of a code block (surrounded by ` in discord).
             */
            temp = temp + "\n" + (commands.get(c).isRestricted() ? "__" : "") + "`" +COMMAND + " " + c + "`" + (commands.get(c).isRestricted() ? "__" : "");
        }
        // send the message
        mc.sendMessage(temp).queue();
    }
    /**
     * Print the list of commands and their descriptions to a given MessageChannel.
     * Note: ordering is based on the internal ordering of HashMap. Descriptions will
     * only be printed if they exist.
     * @param mc the MessageChannel to output to.
     */
    private void printCommandsAndDescriptions(MessageChannel mc){
        // see printCommands() for more code explanations
        String temp = "**NOTE:** If the command is __underlined__ then the command is restricted in use.\n";
        temp = temp + "Current MemeBot commands:\n";
        for(String c : commands.keySet()){
            temp = temp + "\n" + (commands.get(c).isRestricted() ? "__" : "") + "`" +COMMAND + " " + c + "`" + (commands.get(c).isRestricted() ? "__" : "");
            // add the descriptions for the commands (if they exist)
            if(commandDescriptions.containsKey(commands.get(c))){
                temp = temp + "\n" + "\t\t" + commandDescriptions.get(commands.get(c));
            }
        }
        mc.sendMessage(temp).queue();

    }

    /**
     * Print a list of Airhorn Solutions commands.
     * Note: prints the list of commands used internally. Does not automatically
     * update if the Airhorn Solutions bot updates
     * @param mc the MessageChannel to output to.
     */
    private void printAirhorn(MessageChannel mc){
        String temp="Current airhorn commands:\n";
        // iterate over commands in airhorn commands array
        for(String c : airhornCommands){
            temp = temp + "\n" + c;
        }
        mc.sendMessage(temp).queue();
    }

    private void printRandomAirhorn(Member mem){
        // ensure that we do not disconnect immediately after beginning a connection
        dc = false;
        if(airhornOn){
            // ensure joinee is not a bot
            if(!mem.getUser().isBot() && mem.getVoiceState().inVoiceChannel()){
                VoiceChannel voiceChan = mem.getVoiceState().getChannel();
                // connect to the voice channel
                connectTo(voiceChan);
                // wait a little bit
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                // send the message to the bot channel
                Guild g = mem.getGuild();
                TextChannel chan = g.getTextChannelById("261176936510783488");
                TextChannel tts = g.getTextChannelById("319858339293167616");
                chan.sendMessage(getRandomAirhorn()).queue();
                tts.sendMessage(mem.getNickname() + " has joined the channel").queue();
                // wait to disconnect in a separate thrad
                new Thread(() -> waitToDisconnect()).start();
            }
        }
    }

    /**
     * Log a message to the console.
     * The message will take the format: [HH:mm:ss] [level] message
     * @param level the level of the message
     * @param message the message to be logged
     */
    private void printConsoleMessage(LogLevel level, String message){
    	LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter form = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = date.format(form);
        String output = "[" + time + "] ";
        output += "[" + level + "] ";
        output += message;
        System.out.println(output);
    }

    /**
     * Log a message to the console, defaulting to the "LOG" level.
     * The message will take the format: [HH:mm:ss] [LOG] message
     * @param message the message to be logged
     */
    private void printLogMessage(String message){
        printConsoleMessage(LogLevel.LOG,message);
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

    Guild guild;

    MemeBot memes;

    public MemeListener(Guild g, MemeBot m){
        guild=g;
        memes=m;
    }

    /**
     * Execute code on change in ping (presumably).
     * Note: currently unused
     * @param arg0 the ping to discord
     */
    @Override
    public void onPing(long arg0) { 
    }

    /**
     * Execute code when the ConnectionStatus changes.
     * This is used to determine when the bot should try to disconnect
     * from the voice channel. When the bot tries to disconnect from the
     * voice channel before being connected, the bot becomes stuck in the voice
     * channel.
     * @param arg0
     */
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

    /**
     * Execute code when a user starts speaking.
     * @param arg0 the user speaking
     * @param arg1 if the user is speaking or not
     */
    @Override
    public void onUserSpeaking(User arg0, boolean arg1) {
    }

    /**
     * @return the guild
     */
    public Guild getGuild() {
        return guild;
    }

    /**
     * @param guild the guild to set
     */
    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    /**
     * @return the memes
     */
    public MemeBot getMemes() {
        return memes;
    }

    /**
     * @param memes the memes to set
     */
    public void setMemes(MemeBot memes) {
        this.memes = memes;
    }

}


/**
 * Enum specifying the type of command being sent by the user.
 * Contains a boolean value restricted, denoting if the command is restricted
 * in use, either to specific people or specific roles. To access this data,
 * call isRestricted() on the enum.
 * @author Daniel Cormier
 * @author Cosmo Viola
 */
enum BotCommand{
    // current list of commands
    AIRHORN_ON (true),
    AIRHORN_OFF (true),
    AIRHORN_STATUS (false),
    MEISENNERD (true),
    COMMAND_LIST (false),
    SHUTDOWN (true),
    AIRHORN_COMMANDS (false),
    COMMANDS_DESCRIPTIONS (false),
    RANDOM_AIRHORN (false),
    HARASS_BEN (true),
	WTN (false);

    // constructor for saving restricted state
    BotCommand(boolean r){
        restricted=r;
    }

    // store and access restricted state
    private final boolean restricted;
    boolean isRestricted(){return restricted;}
}

enum LogLevel{
    INFO,
    LOG,
    WARNING,
    ERROR
}
