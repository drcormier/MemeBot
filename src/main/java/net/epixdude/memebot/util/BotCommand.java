package net.epixdude.memebot.util;

/**
 * Enum specifying the type of command being sent by the user.
 * Contains a boolean value restricted, denoting if the command is restricted
 * in use, either to specific people or specific roles. To access this data,
 * call isRestricted() on the enum.
 * @author Daniel Cormier
 * @author Cosmo Viola
 */
public enum BotCommand{
    // current list of commands
    AIRHORN_ON (true),
    AIRHORN_OFF (true),
    AIRHORN_STATUS (false),
    COMMAND_LIST (false),
    SHUTDOWN (true),
    AIRHORN_COMMANDS (false),
    COMMANDS_DESCRIPTIONS (false),
    RANDOM_AIRHORN (false),
    RICESB (false),
    DEL (true),
    TEST (true),
	WTN (false),
	ETHER (false),
	ILLUMINATI(false),
	BITCOIN(false),
	LITECOIN(false),
	DOGECOIN(false),
	CCPRICE(false);

    // constructor for saving restricted state
    BotCommand(boolean r){
        restricted=r;
    }

    // store and access restricted state
    private final boolean restricted;
    public boolean isRestricted(){return restricted;}
}