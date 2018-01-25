package net.epixdude.memebot.commands;

import java.util.HashMap;

public class CommandNode<T> {

    private final Character letter;
    private final HashMap<Character, CommandNode<T>> nextLetters = new HashMap<>();
    private T data;

    protected CommandNode(Character letter) {
        this.letter = letter;
        this.data = null;
    }

    protected void addData(T data) {
        this.data = data;
    }

    protected CommandNode<T> addNextLetter(Character letter) {
        return nextLetters.computeIfAbsent( letter, l -> new CommandNode<T>( letter ) );
    }

    protected CommandNode<T> addNextLetter(Character letter, T data) {
        final CommandNode<T> temp = nextLetters.computeIfAbsent( letter, l -> new CommandNode<T>( letter ) );
        temp.addData( data );
        return temp;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final CommandNode<T> other = (CommandNode<T>) obj;
        if ( letter == null ) {
            if ( other.letter != null ) {
                return false;
            }
        } else if ( !letter.equals( other.letter ) ) {
            return false;
        }
        return true;
    }

    protected T getResults() throws MultipleValidCommandsException, NoValidCommandsException {
        if ( this.data != null ) {
            return this.data;
        } else if ( nextLetters.size() == 1 ) {
            return nextLetters.values().iterator().next().getResults();
        } else if ( nextLetters.size() > 1 ) {
            throw new MultipleValidCommandsException();
        } else {
            throw new NoValidCommandsException();
        }
    }

    protected CommandNode<T> getResults(Character nextLetter) throws NoValidCommandsException {
        if ( !nextLetters.containsKey( nextLetter ) ) {
            throw new NoValidCommandsException();
        }
        return nextLetters.get( nextLetter );
    }

    @Override
    public int hashCode() {
        return letter.hashCode();
    }

}
