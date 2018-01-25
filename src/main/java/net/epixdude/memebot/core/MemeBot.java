package net.epixdude.memebot.core;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Emote;
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
import net.epixdude.memebot.commands.CommandManager;
import net.epixdude.memebot.crypto.Bitcoin;
import net.epixdude.memebot.crypto.BulkCryptoCurrencyPriceGetter;
import net.epixdude.memebot.crypto.CryptoData;
import net.epixdude.memebot.crypto.Cryptocurrency;
import net.epixdude.memebot.crypto.Dogecoin;
import net.epixdude.memebot.crypto.Ethereum;
import net.epixdude.memebot.crypto.Litecoin;
import net.epixdude.memebot.crypto.PortfolioManager;
import net.epixdude.memebot.util.BotCommand;

public class MemeBot extends ListenerAdapter{

    private static final String TTS_CHANNEL_NAME = "319858339293167616";

	private static final String BOT_CHANNEL_ID = "261176936510783488";

	private static final String COMMAND = "!MemeBot";
	private static final String SHORTCOMMAND = "!mb";

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
    private static Set<String> commandList;
    private static CommandManager cm;
    // map of command descriptions
    private static HashMap<BotCommand,String> commandDescriptions;
    
    private static final String wheresthatnerd = "wheres that nerd ";
    
    private static StringBuffer sb = new StringBuffer();
    
    private static User mbUser;
    
    private static Message owt = null;
    
    private static PortfolioManager portfolioManager = new PortfolioManager();

    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("Usage: java -jar MemeBot.jar token");
            System.exit(1);
        }
        // add token from CLA
        token=args[0];
        // add commands to the maps
        addCommands();
        // build instance of JDA
        try{
            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .addEventListener(new MemeBot())
                    .buildBlocking();
            jda.getPresence().setGame(Game.playing("with dank memes"));
            sb.append(Character.toChars(0x1F914));
            mbUser = (User)jda.getSelfUser();
            portfolioManager.loadPortfolios();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public MemeBot(){
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
        printRandomAirhorn(event.getMember(), true);
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
        String s = m.getContentDisplay();
        String[] message = s.split(" ");
        Member mem = m.getGuild().getMember(m.getAuthor());
        MessageChannel chan = m.getChannel();
        if(message[0].equals(COMMAND) || message[0].equals(SHORTCOMMAND) || m.isMentioned(mbUser, Message.MentionType.USER)){
            if(message.length >= 2 ){
                BotCommand bc = cm.getCommand( message[1] );
                if(bc == null) {
                    chan.sendMessage("ERROR: `" + s + "` is not a command!\nTo see a list of commands, type `!MemeBot commands`").queue();
                }else if(bc == BotCommand.INVALID) {
                    chan.sendMessage("ERROR: `" + s + "` is ambiguous!\nTo see a list of commands, type `!MemeBot commands`").queue();
                }else {
                    parseCommands(bc,mem,chan,m);
                }
                    
            }else{
                chan.sendMessage("ERROR: `" + s + "` is not a command!\nTo see a list of commands, type `!MemeBot commands`").queue();
            }

        }
        else if(s.contains(sb.toString()) && !mem.getUser().isBot()){
        	chan.sendMessage(getRandomThinking() + " :thinking:").queue();
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
            new Thread(()->{
            	try{
            		Thread.sleep(500);
            	}catch(Exception e){
            		e.printStackTrace();
            	}
				manager.closeAudioConnection();
            }).start();

        }
    }
    
    private void outputCryptoCurrency(MessageChannel mc, Cryptocurrency cc, double numberOfCoins) {
        final DecimalFormat format = new DecimalFormat("$###,##0.00####");
        try {
            CryptoData data = cc.getData();
            String output = data.toString();
            if(!Double.isNaN( numberOfCoins )) {
                output += "\nYour " + numberOfCoins + " " + data.getCurrencyabbreviation() + " is worth ";
                output += "`" + format.format( data.getPrice()*numberOfCoins ) + "`";
            }
            mc.sendMessage( output ).queue();
        } catch ( Exception e ) {
            e.printStackTrace();
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
        double count;
        Cryptocurrency cc;
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
            case COMMAND_LIST: // !MemeBot commands
                printCommands(chan);
                break;
            case SHUTDOWN: // !MemeBot shutdown
                if(bm){
                    chan.sendMessage("Shutting down.").complete();
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
                printRandomAirhorn(user, false);
                break;
            case WTN:
                	String name = "";
                	String[] m = mess.getContentRaw().split(" ");
                	for( int i = 2; i < m.length; ++i){
                		name += m[i] + " ";
                	}
                	name = name.toLowerCase().trim();
                	chan.sendMessage(wtn(name)).queue();
                	break;
            case RICESB:
                	String usermessage = "";
                	String[] n = mess.getContentRaw().split(" ");
                	for( int i = 2; i < n.length; ++i){
                		usermessage += n[i] + " ";
                	}
                	usermessage = usermessage.toLowerCase().trim();
                	chan.sendMessage(ricesb(usermessage)).queue();
                	break;
            case DEL:
                	if(bm){
                		TextChannel tc = (TextChannel)chan;
                		int c = Integer.parseInt(mess.getContentRaw().split(" ")[2]);
                		List<Message> messages = tc.getHistory().retrievePast(c).complete();
                		tc.deleteMessages(messages).queue();
                		tc.sendMessage("Deleted " + c + " messages").queue();
                	}
                	break;
            case TEST:
                	if(bm){
                		chan.sendMessage(tilt(chan)).queue();
                	}
                	break;
            case ETHER:
                    try {
                        count = Double.parseDouble( mess.getContentRaw().split( " " )[2] );
                    } catch ( Exception e ) {
                        count = Double.NaN;
                    }
                    cc = new Ethereum();
                    outputCryptoCurrency( chan, cc, count );
                    break;
            case BITCOIN:
                    try {
                        count = Double.parseDouble( mess.getContentRaw().split( " " )[2] );
                    } catch ( Exception e ) {
                        count = Double.NaN;
                    }
                    cc = new Bitcoin();
                    outputCryptoCurrency( chan, cc, count );
                    break;
            case LITECOIN:
                    try {
                        count = Double.parseDouble( mess.getContentRaw().split( " " )[2] );
                    } catch ( Exception e ) {
                        count = Double.NaN;
                    }
                    cc = new Litecoin();
                    outputCryptoCurrency( chan, cc, count );
                    break;
            case DOGECOIN:
                    try {
                        count = Double.parseDouble( mess.getContentRaw().split( " " )[2] );
                    } catch ( Exception e ) {
                        count = Double.NaN;
                    }
                    cc = new Dogecoin();
                    outputCryptoCurrency( chan, cc, count );
                    break;
            case CCPRICE:
                    try {
                        LinkedList<String> currencies = new LinkedList<>(Arrays.stream( mess.getContentRaw().split( " " ) )
                                .map( (String s) -> s.toUpperCase() )
                                .collect(Collectors.toList()));
                        currencies.removeFirst();
                        currencies.removeFirst();
                        chan.sendMessage( BulkCryptoCurrencyPriceGetter.getPrices( currencies ) ).queue();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            case PORTFOLIO:
                	String[] o = mess.getContentRaw().split(" ");
                	Long idLong = user.getUser().getIdLong();
                	if(o.length < 3) {
                	    chan.sendMessage( portfolioManager.checkPortfolio( idLong ) ).queue();
                	}else if(o[2].equals( "add" ) && o.length >= 5) {
                	    chan.sendMessage( portfolioManager.addCoin( idLong, o[3], Double.valueOf( o[4] ) )).queue();
                        portfolioManager.savePortfolios();
                	}else if(o[2].equals( "remove" ) && o.length >= 4) {
                	    portfolioManager.removeCoin( idLong, o[3] );
                        portfolioManager.savePortfolios();
                	}else if(o[2].equals( "reset" )) {
                	    portfolioManager.resetPortfolio( idLong );
                        portfolioManager.savePortfolios();
                	}else if(o[2].equals( "help" )) {
                	    String help = "`!MemeBot portfolio help` prints this help message\n";
                	    help += "`!MemeBot portfolio` prints the summary of your portfolio\n";
                	    help += "`!MemeBot portfolio add SYMBOL amount` sets the amount of SYMBOL currency to amount\n";
                	    help += "`!MemeBot portfolio remove SYMBOL` removes SYMBOL coin from your portfolio\n";
                	    help += "`!MemeBot portfolio reset` resets your portfolio";
                	    chan.sendMessage( help ).queue();
                	}
                	break;
                    
            case ILLUMINATI:
                	playIlluminati(user);
                	break;

            	
        }
    }
    
    private static String regIndEmoji( char ch ){
    	return ":regional_indicator_" + ch + ": ";
    }
    
    private static String ricesb( String message ){
    	String temp = "";
    	for( char c: message.toLowerCase().replaceAll("[^a-z ]", "").toCharArray() ){
    		if( c == ' '){
    			temp += ":clap: ";
    		}else{
    			temp += regIndEmoji(c);
    		}
    	}
    	return temp;	
    }
    
    private static String wtn( String name ){
    	return ricesb( wheresthatnerd + name );
    }
    
    private static Message tilt(MessageChannel chan){
	Emote fgm = chan.getJDA().getEmotesByName("FeelsGoodMan", true).get(0);
	MessageBuilder fgmb = new MessageBuilder();
	fgmb.append(fgm);
	for( String word : "IM READY TO TILT".split(" ")){
		fgmb.append(word);
		fgmb.append(fgm);
	}
	owt = fgmb.build();
    	return owt;
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
		// initialize the hashmaps
		commandList = new HashSet<>();
		commandDescriptions = new HashMap<>();
		cm = new CommandManager();
		// add the commands
		cm.addCommand("airhornOn",BotCommand.AIRHORN_ON);
		commandList.add("airhornOn");
		commandDescriptions.put(BotCommand.AIRHORN_ON, "Enable the airhorn functionality of MemeBot");

		cm.addCommand("airhornOff",BotCommand.AIRHORN_OFF);
		commandList.add("airhornOff");
		commandDescriptions.put(BotCommand.AIRHORN_OFF, "Disable the airhorn functionality of MemeBot");

		cm.addCommand("airhornStatus",BotCommand.AIRHORN_STATUS);
		commandList.add("airhornStatus");
		commandDescriptions.put(BotCommand.AIRHORN_STATUS, "Check the airhorn functionality status of MemeBot");

		cm.addCommand("commands",BotCommand.COMMAND_LIST);
		commandList.add("commands");
		commandDescriptions.put(BotCommand.COMMAND_LIST, "Print a list of MemeBot commands");

		cm.addCommand("shutdown",BotCommand.SHUTDOWN);
		commandList.add("shutdown");
		commandDescriptions.put(BotCommand.SHUTDOWN, "Shut down MemeBot");

		cm.addCommand("airhornCommands",BotCommand.AIRHORN_COMMANDS);
		commandList.add("airhornCommands");
		commandDescriptions.put(BotCommand.AIRHORN_COMMANDS, "Print a list of Airhorn Solutions commands");

		cm.addCommand("com+desc",BotCommand.COMMANDS_DESCRIPTIONS);
		commandList.add("com+desc");
		commandDescriptions.put(BotCommand.COMMANDS_DESCRIPTIONS, "Prints a list of MemeBot commands and their descriptions");

		cm.addCommand("random",BotCommand.RANDOM_AIRHORN);
		commandList.add("random");
		commandDescriptions.put(BotCommand.RANDOM_AIRHORN, "Prints a random Airhorn Solutions command.");

		cm.addCommand("wtn",BotCommand.WTN);
		commandList.add("wtn");
		commandDescriptions.put(BotCommand.WTN, "Wheres that nerd?");

		cm.addCommand("ricesb",BotCommand.RICESB);
		commandList.add("ricesb");
		commandDescriptions.put(BotCommand.RICESB, "Convert string into a sequence of regional indicator and clap emojis.");

		cm.addCommand("del",BotCommand.DEL);
		commandList.add("del");
		commandDescriptions.put(BotCommand.DEL, "Delete the n most recent messages from the channel.");

		cm.addCommand("test",BotCommand.TEST);
		commandList.add("test");
		commandDescriptions.put(BotCommand.TEST, "A test command.");
		
		cm.addCommand("ether",BotCommand.ETHER);
		commandList.add("ether");
		commandDescriptions.put(BotCommand.ETHER, "Gets the current ethereum price from GDAX.");
		
		cm.addCommand("illuminati",BotCommand.ILLUMINATI);
		commandList.add("illuminati");
		commandDescriptions.put(BotCommand.ILLUMINATI, "Illuminati confirmed?");

		cm.addCommand("bitcoin",BotCommand.BITCOIN);
		commandList.add("bitcoin");
		commandDescriptions.put(BotCommand.BITCOIN, "Gets the current bitcoin price from GDAX.");

		cm.addCommand("litecoin",BotCommand.LITECOIN);
		commandList.add("litecoin");
		commandDescriptions.put(BotCommand.LITECOIN, "Gets the current litecoin price from GDAX.");

		cm.addCommand("dogecoin",BotCommand.DOGECOIN);
		commandList.add("dogecoin");
		commandDescriptions.put(BotCommand.DOGECOIN, "Gets the current dogecoin price from CryptoCompare.");
		
		cm.addCommand( "ccprice", BotCommand.CCPRICE );
		commandList.add("ccprice");
		commandDescriptions.put( BotCommand.CCPRICE, "Gets the current price of any number of cryptocurrencies from CryptoCompare" );
		
		cm.addCommand( "portfolio", BotCommand.PORTFOLIO );
		commandList.add("portfolio");
		commandDescriptions.put( BotCommand.PORTFOLIO, "Performs various operations with your cryptocurrency portfolio." );

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
        for(String c : commandList){
            /* add the commands to a temp string. If the command is restricted, the command will be underlined (surrounded by __ in discord markdown).
             * In addition, the command itself will be placed inside of a code block (surrounded by ` in discord).
             */
            BotCommand com = cm.getCommand(c);
            temp = temp + "\n" + (com.isRestricted() ? "__" : "") + "`" +COMMAND + " " + c + "`" + (com.isRestricted() ? "__" : "");
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
        for(String c : commandList){
            BotCommand com = cm.getCommand(c);
            temp = temp + "\n" + (com.isRestricted() ? "__" : "") + "`" +COMMAND + " " + c + "`" + (com.isRestricted() ? "__" : "");
            // add the descriptions for the commands (if they exist)
            if(commandDescriptions.containsKey(com)){
                temp = temp + "\n" + "\t\t" + commandDescriptions.get(com);
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

    private void printRandomAirhorn(Member mem, boolean join){
        // ensure that we do not disconnect immediately after beginning a connection
        dc = false;
        if(airhornOn){
            // ensure joinee is not a bot
            if(!mem.getUser().isBot() && mem.getVoiceState().inVoiceChannel()){
            	new Thread(()->{
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
					// bot channel
					TextChannel chan = g.getTextChannelById(BOT_CHANNEL_ID);
					chan.sendMessage(getRandomAirhorn()).queue();
					if(join){
						// tts channel
						TextChannel tts = g.getTextChannelById(TTS_CHANNEL_NAME);
						/* use a messagebuilder so that we can send a tts message */
						Message ttsm = new MessageBuilder().append(mem.getNickname() + " has joined the channel").setTTS(true).build();
						tts.sendMessage(ttsm).queue();
					}
            	}).start();
                // wait to disconnect in a separate thrad
                new Thread(() -> waitToDisconnect()).start();
            }
        }
    }

    private void playIlluminati(Member mem){
        // ensure that we do not disconnect immediately after beginning a connection
        dc = false;
        if(airhornOn){
            // ensure joinee is not a bot
            if(!mem.getUser().isBot() && mem.getVoiceState().inVoiceChannel()){
            	new Thread(()->{
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
					// bot channel
					TextChannel chan = g.getTextChannelById(BOT_CHANNEL_ID);
					chan.sendMessage(";;play https://www.youtube.com/watch?v=GRWbIoIR04c").queue();
					try{
						Thread.sleep(18000);
					}catch(Exception e){
						e.printStackTrace();
					}
					chan.sendMessage(";;stop").queue();
            	}).start();
				// wait to disconnect in a separate thrad
				new Thread(() -> waitToDisconnect()).start();
            }
        }
    }


}
