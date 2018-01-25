package net.epixdude.memebot.commands;

import java.util.HashMap;

import net.epixdude.memebot.util.BotCommand;

public class CommandManager {

    private final HashMap<Character, CommandNode<BotCommand>> commandHeads = new HashMap<>();

    public void addCommand(String input, BotCommand command) {
        CommandNode<BotCommand> current = commandHeads.computeIfAbsent( input.charAt( 0 ),
                c -> new CommandNode<BotCommand>( input.charAt( 0 ) ) );
        for ( final char ch : input.substring( 1 ).toCharArray() ) {
            current = current.addNextLetter( ch );
        }
        current.addData( command );
    }

    public BotCommand getCommand(String input) {
        try {
            CommandNode<BotCommand> current = commandHeads.get( input.charAt( 0 ) );
            for ( final char ch : input.substring( 1 ).toCharArray() ) {
                current = current.getResults( ch );
            }
            return current.getResults();
        } catch ( final MultipleValidCommandsException e ) {
            return BotCommand.INVALID;
        } catch ( NoValidCommandsException | NullPointerException e ) {
            return null;
        }
    }
}
